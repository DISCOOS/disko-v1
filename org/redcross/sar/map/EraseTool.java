package org.redcross.sar.map;

import java.io.IOException;


import com.esri.arcgis.carto.FeatureLayer;
import com.esri.arcgis.carto.ILayer;
import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.IEnvelope;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.interop.AutomationException;

/**
 * A custom draw tool.
 * @author geira
 *
 */
public class EraseTool extends AbstractCommandTool {

	private static final long serialVersionUID = 1L;
	private Envelope searchEnvelope = null;
	
	/**
	 * Constructs the DrawTool
	 */
	public EraseTool() throws IOException {
		searchEnvelope = new Envelope();
		searchEnvelope.putCoords(0, 0, 0, 0);
		searchEnvelope.setHeight(50);
		searchEnvelope.setWidth(50);
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
					IFeature result = search(flayer, searchEnvelope);
					if (result != null) {
						IEnvelope env = null;
						if (result.getShape() instanceof Point) {
							env = searchEnvelope.getEnvelope();
							env.expand(2, 2, true);
						}
						else {
							env = result.getExtent().getEnvelope();
						}
						result.delete();
						partialRefresh(env);
						break; // first hit will be deleted
					}
				}
			}
		}	
	}
	
	public void setTolerance(double tolerance) throws IOException {
		searchEnvelope.setHeight(tolerance);
		searchEnvelope.setWidth(tolerance);
	}
}
