package org.redcross.sar.map;

import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.app.Utils;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.POIDialog;
import org.redcross.sar.map.feature.AreaFeature;
import org.redcross.sar.map.feature.AreaFeatureClass;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.OperationAreaFeatureClass;
import org.redcross.sar.map.layer.OperationAreaLayer;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.mso.data.IPOIIf.POIType;

import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geodatabase.IFeatureClass;
import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.GeometryBag;
import com.esri.arcgis.geometry.IEnvelope;
import com.esri.arcgis.geometry.IGeometry;
import com.esri.arcgis.geometry.IPolyline;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.geometry.Polygon;
import com.esri.arcgis.geometry.Polyline;
import com.esri.arcgis.interop.AutomationException;

/**
 * A custom draw tool.
 * @author geira
 *
 */
public class POITool extends AbstractCommandTool {

	private static final long serialVersionUID = 1L;
	
	private POIDialog poiDialog = null;
	private IAreaIf area = null;
	private OperationAreaFeatureClass opAreaFC = null;
	private AreaFeatureClass areaFC = null;
	private IEnvelope env = null;
		
	/**
	 * Constructs the DrawTool
	 */
	public POITool(IDiskoApplication app) throws IOException {
		dialog = new POIDialog(app, this);
		dialog.setIsToggable(false);
		env = new Envelope();
	}

	public void onCreate(Object obj) throws IOException, AutomationException {
		if (obj instanceof IDiskoMap) {
			map = (DiskoMap)obj;
			map.addDiskoMapEventListener(this);
			IDiskoMapManager mapManager = getMap().getMapManager();
			
			List layers = mapManager.getMsoLayers(
					IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA);
			OperationAreaLayer opAreaLayer = (OperationAreaLayer) layers.get(0);
			opAreaFC = (OperationAreaFeatureClass)opAreaLayer.getFeatureClass();
			poiDialog = (POIDialog)dialog;
			poiDialog.setLocationRelativeTo(map, DiskoDialog.POS_WEST, false);
		}
	}
	
	/**
	 * Set an MSO Area to add POI's. POI's will also snap to geometries in Area.
	 * @param area
	 */
	public void setArea(IAreaIf area, AreaFeatureClass areaFC) {
		this.area   = area;
		this.areaFC = areaFC;
	}

	public void onMouseDown(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		if (editFeature != null) {
			editFeature.setSelected(false);
		}
	}	
	
	public void addPOIAt(double x, double y) throws IOException, AutomationException {
		if (editFeature != null) {
			featureClass.clearSelected();
			refresh((Point)editFeature.getShape());
		}
		Point p = new Point();
		p.setX(x);
		p.setY(y);
		addPOIAt(p);
	}
	
	public void addPOIAt(Point point) throws IOException, AutomationException {
		// check if POI is inside Operation area
		if (!isContaining(opAreaFC, point)) {
			showWarning("PUI er utenfor operasjonsområdet");
			return;
		}
		String objID = featureClass.createMsoObject();
		editFeature = (IMsoFeature) featureClass.getFeature(objID);
		
		if (area != null) {
			// snapping
			IFeature feature = search(areaFC, point);
			if (feature != null && feature instanceof AreaFeature) {
				AreaFeature areaFeature = (AreaFeature)feature;
				GeometryBag geomBag = (GeometryBag)areaFeature.getShape();
				int index = getGeomIndex(geomBag, point);
				if (index > -1) {
					IGeometry subGeom = geomBag.getGeometry(index);
					if (subGeom instanceof IPolyline) {
						Polyline pline = (Polyline)subGeom;
						POIType poiType = poiDialog.getSelectedType();
						if (poiType == POIType.START) {
							pline.queryFromPoint(point);
						}
						else if (poiType == POIType.STOP) {
							pline.queryToPoint(point);
						}
					}
				}
			}
			IPOIIf poi = (IPOIIf)editFeature.getMsoObject();
			area.addAreaPOI(poi);
		}
		point.setSpatialReferenceByRef(map.getSpatialReference());
		editFeature.setGeodata(MapUtil.getMsoPosistion(point));
		featureClass.setSelected(editFeature, true);
		poiDialog.setMsoFeature(editFeature);
		
		//pan to POI if not in current extent
		Envelope extent = (Envelope)map.getActiveView().getExtent();
		if (!extent.contains(point)) {
			extent.centerAt(point);
			map.setExtent(extent);
		}
		map.fireEditLayerChanged();
	}
	
	public void onMouseUp(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		Point p = transform(x, y);
		addPOIAt(p);
	}
	
	public IMsoFeature getCurrent() {
		return editFeature;
	}
	
	private void refresh(Point p) throws AutomationException, IOException {
		if (p != null) {
			double size = map.getActiveView().getExtent().getWidth()/50;
			double xmin = p.getX()-size/2;
			double ymin = p.getY()-size/2;
			double xmax = p.getX()+size/2;
			double ymax = p.getY()+size/2;
			env.putCoords(xmin, ymin, xmax, ymax);
			map.getActiveView().partialRefresh(
				com.esri.arcgis.carto.esriViewDrawPhase.esriViewGeography,null, env);
		}
		
	}
	
	private boolean isContaining(IFeatureClass fc, IGeometry geom) 
			throws AutomationException, IOException {
		boolean flag = false;
		for (int i = 0; i < fc.featureCount(null); i++) {
			Polygon polygon = (Polygon)fc.getFeature(i).getShape();
			if (polygon.contains(geom)) {
				flag = true;
			}
		}
		return flag;
	}
	
	private void showWarning(final String msg) {
		Runnable r = new Runnable(){
            public void run() {
            	JOptionPane.showMessageDialog(map, 
            		msg, Utils.translate(opAreaFC.getClassCode()),
            		JOptionPane.WARNING_MESSAGE);
            }
        };
        SwingUtilities.invokeLater(r);
	}
}
