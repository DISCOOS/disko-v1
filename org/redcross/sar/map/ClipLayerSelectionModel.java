package org.redcross.sar.map;

import java.io.IOException;
import java.util.ArrayList;

import org.redcross.sar.gui.FeatureLayerSelectionModel;

import com.esri.arcgis.carto.FeatureLayer;
import com.esri.arcgis.carto.ILayer;
import com.esri.arcgis.carto.IMap;

public class ClipLayerSelectionModel extends FeatureLayerSelectionModel {
	
	public ClipLayerSelectionModel(DiskoMap map) throws IOException {
		super(map);
		initialize();
	}
	
	private void initialize() throws IOException {
		ArrayList<FeatureLayer> snapableLayers = new ArrayList<FeatureLayer>();
		IMap focusMap = map.getActiveView().getFocusMap();
		for (int i = 0; i < focusMap.getLayerCount(); i++) {
			ILayer layer = focusMap.getLayer(i);
			if (layer instanceof FeatureLayer) {
				FeatureLayer flayer = (FeatureLayer) layer;
				int shapeType = flayer.getFeatureClass().getShapeType();
				if (shapeType == com.esri.arcgis.geometry.
						esriGeometryType.esriGeometryPolygon) {
					snapableLayers.add(flayer);
				}
			}
		}
		layers = new FeatureLayer[snapableLayers.size()];
		selected = new boolean[snapableLayers.size()];
		for (int i = 0; i < selected.length; i++) {
			layers[i] = snapableLayers.get(i);
			selected[i] = false;
		}
	}
}
