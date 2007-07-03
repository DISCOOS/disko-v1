package org.redcross.sar.map;

import java.awt.Toolkit;
import java.io.IOException;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.POIDialog;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.IMsoFeatureClass;
import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.map.layer.OperationAreaLayer;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.mso.data.IPOIIf.POIType;

import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.Point;
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
		
	/**
	 * Constructs the DrawTool
	 */
	public POITool(IDiskoApplication app) throws IOException {
		dialog = new POIDialog(app, this);
		dialog.setIsToggable(false);
	}

	public void onCreate(Object obj) throws IOException, AutomationException {
		if (obj instanceof IDiskoMap) {
			map = (DiskoMap)obj;
			map.addDiskoMapEventListener(this);
			poiDialog = (POIDialog)dialog;
			poiDialog.setLocationRelativeTo(map, DiskoDialog.POS_WEST, false);
			
			//getting operation areas
			opAreaLayer = (OperationAreaLayer) map.getMapManager().getMsoLayer(
					IMsoFeatureLayer.LayerCode.OPERATION_AREA_LAYER);
		}
	}
	
	/**
	 * Set an MSO Area to add POI's. POI's will also snap to geometries in Area.
	 * @param area
	 */
	public void setArea(IAreaIf area) {
		this.area   = area;
	}

	public void onMouseDown(int button, int shift, int x, int y)
			throws IOException, AutomationException {
	}	
	
	public void addPOIAt(double x, double y) throws IOException, AutomationException {
		Point p = new Point();
		p.setX(x);
		p.setY(y);
		addPOIAt(p);
	}
	
	public void addPOIAt(Point point) throws IOException, AutomationException {
		addPOIAt(point, poiDialog.getSelectedType());
	}
	
	public void addPOIAt(Point point, POIType poiType) throws IOException, AutomationException {
		//check if point inside operation area
		if (!insideOpArea(point)) {
			Toolkit.getDefaultToolkit().beep();
			return;
		}
		IMsoFeatureClass featureClass = (IMsoFeatureClass)editLayer.getFeatureClass();
		editFeature = featureClass.createMsoFeature();
		IPOIIf poi = (IPOIIf)editFeature.getMsoObject();
		point.setSpatialReferenceByRef(map.getSpatialReference());
		poi.setPosition(MapUtil.getMsoPosistion(point));
		poi.setType(poiType);
		
		if (area != null) {
			area.addAreaPOI(poi);
		}
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
}
