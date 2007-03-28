package org.redcross.sar.map;

import java.awt.Toolkit;
import java.io.IOException;


import com.esri.arcgis.carto.FeatureLayer;
import com.esri.arcgis.carto.ILayer;
import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geodatabase.IFeatureClass;
import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.geometry.Polyline;
import com.esri.arcgis.interop.AutomationException;

/**
 * A custom draw tool.
 * @author geira
 *
 */
public class SplitTool extends AbstractCommandTool {

	private static final long serialVersionUID = 1L;
	private Envelope searchEnvelope = null;
	
	/**
	 * Constructs the DrawTool
	 */
	public SplitTool() throws IOException {
		searchEnvelope = new Envelope();
		searchEnvelope.putCoords(0, 0, 0, 0);
		searchEnvelope.setHeight(10);
		searchEnvelope.setWidth(10);
	}

	public void onCreate(Object obj) throws IOException, AutomationException {
		if (obj instanceof DiskoMap) {
			map = (DiskoMap)obj;
		}
	}

	public void onMouseDown(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		Point p = transform(x,y);
		searchEnvelope.centerAt(p);
		for (int i = 0; i < map.getLayerCount(); i++) {
			ILayer layer = map.getLayer(i);
			if (layer instanceof FeatureLayer) {
				FeatureLayer flayer = (FeatureLayer)layer;
				if (flayer.isSelectable()) {
					IFeature feature = search(flayer, searchEnvelope);
					if (feature != null) {
						int geomType = feature.getShape().getGeometryType();
						if (geomType == com.esri.arcgis.geometry.esriGeometryType.
								esriGeometryPolyline) {
							split(flayer, feature, p);
							break; // first hit will be deleted
						}
					}
				}
			}
		}	
	}
	
	private void split(FeatureLayer flayer, IFeature feature, Point nearPoint) 
			throws IOException, AutomationException {
		Polyline orginal = (Polyline)feature.getShapeCopy();
		boolean[] splitHappened = new boolean[2];
		int[] newPartIndex = new int[2];
		int[] newSegmentIndex = new int[2];
		orginal.splitAtPoint(nearPoint, true, true, splitHappened, 
				newPartIndex, newSegmentIndex);
		// two new polylines
		Polyline pline1 = new Polyline();
		pline1.addGeometry(orginal.getGeometry(newPartIndex[0]), null, null);
		Polyline pline2 = new Polyline();
		pline2.addGeometry(orginal.getGeometry(newPartIndex[1]), null, null);
		
		// update orginal feature
		feature.setShapeByRef(pline1);
		feature.store();
		// create new feature
		IFeatureClass fclass = flayer.getFeatureClass();
		IFeature newFeature = fclass.createFeature();
		newFeature.setShapeByRef(pline2);
		newFeature.store();
		
		Toolkit.getDefaultToolkit().beep();
		//clear selection and refresh
		map.getMap().clearSelection();
		map.getActiveView().refresh();
	}
	
	public void setTolerance(double tolerance) throws IOException {
		searchEnvelope.setHeight(tolerance);
		searchEnvelope.setWidth(tolerance);
	}
}
