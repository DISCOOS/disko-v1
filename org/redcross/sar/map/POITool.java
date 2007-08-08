package org.redcross.sar.map;

import java.awt.Toolkit;
import java.io.IOException;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.POIDialog;
import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.map.layer.OperationAreaLayer;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.mso.data.IPOIListIf;
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
	private IDiskoApplication app = null;
	private POIDialog poiDialog = null;
	private Point p = null;
	private IPOIIf poi = null; // current POI
		
	/**
	 * Constructs the DrawTool
	 */
	public POITool(IDiskoApplication app) throws IOException {
		this.app = app;
		dialog = new POIDialog(app, this);
		dialog.setIsToggable(false);
		p = new Point();
		p.setX(0);
		p.setY(0);
	}

	public void onCreate(Object obj) throws IOException, AutomationException {
		if (obj instanceof IDiskoMap) {
			map = (DiskoMap)obj;
			poiDialog = (POIDialog)dialog;
			poiDialog.setLocationRelativeTo(map, DiskoDialog.POS_WEST, false);
			
			//getting operation areas
			opAreaLayer = (OperationAreaLayer) map.getMapManager().getMsoLayer(
					IMsoFeatureLayer.LayerCode.OPERATION_AREA_LAYER);
		}
	}

	public void onMouseDown(int button, int shift, int x, int y)
			throws IOException, AutomationException {
	}	
	
	public IPOIIf getCurrentPOI() {
		return poi;
	}
	
	public void movePOI(Point point) throws IOException, AutomationException {
		if (poi != null) {
			//check if point inside operation area
			if (!insideOpArea(point)) {
				Toolkit.getDefaultToolkit().beep();
				return;
			}
			poi.setPosition(MapUtil.getMsoPosistion(point));

			//pan to POI if not in current extent
			Envelope extent = (Envelope)map.getActiveView().getExtent();
			if (!extent.contains(point)) {
				extent.centerAt(point);
				map.setExtent(extent);
			}
			map.refreshMsoLayers();
		}
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
	
	public void addPOIAt(Point point, final POIType poiType) 
			throws IOException, AutomationException {
		//check if point inside operation area
		if (!insideOpArea(point)) {
			Toolkit.getDefaultToolkit().beep();
			return;
		}
		ICmdPostIf cmdPost = app.getMsoModel().getMsoManager().getCmdPost();
		IPOIListIf poiList = cmdPost.getPOIList();
		poi = poiList.createPOI();
		poi.setPosition(MapUtil.getMsoPosistion(point));
		poi.setType(poiType);

		//pan to POI if not in current extent
		Envelope extent = (Envelope)map.getActiveView().getExtent();
		if (!extent.contains(point)) {
			extent.centerAt(point);
			map.setExtent(extent);
		}
		map.setSelected(poi, true);
	}
	
	public void onMouseUp(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		p = transform(x, y);
		p.setSpatialReferenceByRef(map.getSpatialReference());
		addPOIAt(p);
	}
}
