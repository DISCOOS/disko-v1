package org.redcross.sar.map;

import java.io.IOException;
import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.POIDialog;
import org.redcross.sar.map.layer.MsoFeature;
import org.redcross.sar.mso.data.IPOIIf;
import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.IEnvelope;
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
			poiDialog = (POIDialog)dialog;
			poiDialog.setLocationRelativeTo(map, DiskoDialog.POS_WEST, false);
		}
	}

	public void onMouseDown(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		if (editFeedback != null) {
			editFeedback.editStarted(editFeature);
		}
	}	
	
	public void onMouseMove(int button, int shift, int x, int y)
			throws IOException, AutomationException {
	}
	
	public void addPOIAt(double x, double y) throws IOException, AutomationException {
		Point p = new Point();
		p.setX(x);
		p.setY(y);
		addPOIAt(p);
	}
	
	public void addPOIAt(Point point) throws IOException, AutomationException {
		if (editFeature != null) {
			editFeature.setSelected(false);
			refresh();
		}
		editFeature = (MsoFeature)featureClass.createFeature();
		IPOIIf poi = (IPOIIf)editFeature.getMsoObject();
		point.setSpatialReferenceByRef(map.getSpatialReference());
		poi.setPosition(MapUtil.getMsoPosistion(point));
		poi.setType(poiDialog.getSelectedType());
		editFeature.setShapeByRef(point);
		refresh();
		
		if (editFeedback != null) {
			editFeedback.editFinished(editFeature);
		}
	}
	
	public MsoFeature getSelectedMsoFeature() {
		return editFeature;
	}

	public void onMouseUp(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		Point p = transform(x, y);
		addPOIAt(p);
	}
	
	private void refresh() throws AutomationException, IOException {
		Point p = (Point)editFeature.getShape();
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
}
