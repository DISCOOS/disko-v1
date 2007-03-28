package org.redcross.sar.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.redcross.sar.map.DiskoMap;

import com.esri.arcgis.carto.FeatureLayer;
import com.esri.arcgis.carto.ILayer;

public class SnapLayerSelectionModel extends FeatureLayerSelectionModel {
	
	public SnapLayerSelectionModel(DiskoMap map) throws IOException {
		super(map);
		initialize();
	}
	
	private void initialize() throws IOException {
		ArrayList<FeatureLayer> snapableLayers = new ArrayList<FeatureLayer>();
		for (int i = 0; i < map.getLayerCount(); i++) {
			ILayer layer = map.getLayer(i);
			FeatureLayer flayer = (FeatureLayer) layer;
			int shapeType = flayer.getFeatureClass().getShapeType();
			if (shapeType == com.esri.arcgis.geometry.
					esriGeometryType.esriGeometryPolyline ||
					shapeType == com.esri.arcgis.geometry.
					esriGeometryType.esriGeometryPolygon) {
				snapableLayers.add(flayer);
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
