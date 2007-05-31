package org.redcross.sar.map;

import java.io.IOException;
import java.util.ArrayList;

import com.esri.arcgis.carto.IFeatureLayer;
import com.esri.arcgis.carto.ILayer;
import com.esri.arcgis.carto.IMap;
import com.esri.arcgis.interop.AutomationException;

public class SnapLayerSelectionModel extends FeatureLayerSelectionModel {
	
	public SnapLayerSelectionModel(DiskoMap map) throws IOException, AutomationException {
		super(map);
		initialize();
	}
	
	private void initialize() throws IOException, AutomationException {
		ArrayList<IFeatureLayer> snapableLayers = new ArrayList<IFeatureLayer>();
		IMap focusMap = map.getActiveView().getFocusMap();
		for (int i = 0; i < focusMap.getLayerCount(); i++) {
			ILayer layer = focusMap.getLayer(i);
			if (layer instanceof IFeatureLayer) {
				IFeatureLayer flayer = (IFeatureLayer) layer;
				int shapeType = flayer.getFeatureClass().getShapeType();
				if (shapeType == com.esri.arcgis.geometry.esriGeometryType.esriGeometryPolyline ||
					shapeType == com.esri.arcgis.geometry.esriGeometryType.esriGeometryPolygon  ||
					shapeType == com.esri.arcgis.geometry.esriGeometryType.esriGeometryBag) {
					snapableLayers.add(flayer);
				}
			}
		}
		layers = new IFeatureLayer[snapableLayers.size()];
		selected = new boolean[snapableLayers.size()];
		for (int i = 0; i < selected.length; i++) {
			layers[i] = snapableLayers.get(i);
			selected[i] = false;
		}
	}
}
