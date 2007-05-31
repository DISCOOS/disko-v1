package org.redcross.sar.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.esri.arcgis.carto.IFeatureLayer;

public abstract class FeatureLayerSelectionModel {
	
	protected DiskoMap map = null;
	protected IFeatureLayer[] layers = null;
	protected boolean[] selected = null;
	
	public FeatureLayerSelectionModel(DiskoMap map) throws IOException {
		this.map = map;
	}
	
	public int getLayerCount() {
		return layers.length;
	}
	
	public IFeatureLayer getFeatureLayer(int i) {
		if (i < layers.length) {
			return layers[i];
		}
		return null;
	}
	
	public boolean isSelected(int i) {
		if (i < selected.length) {
			return selected[i];
		}
		return false;
	}
	
	public void setSelected(int i, boolean b) {
		if (i < selected.length) {
			selected[i] = b;
		}
	}
	
	public List getSelected() {
		ArrayList<IFeatureLayer> result = new ArrayList<IFeatureLayer>();
		for (int i = 0; i < layers.length; i++) {
			if (selected[i]) {
				result.add(layers[i]);
			}
		}
		return result;
	}
}
