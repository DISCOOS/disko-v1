package org.redcross.sar.gui;

import java.io.IOException;
import java.util.ArrayList;
import org.redcross.sar.map.IDiskoMap;

import com.esri.arcgis.carto.IFeatureLayer;

public interface IDrawDialog {
	
	public void onLoad(IDiskoMap map) throws IOException;
	
	public void setSnapTolerance(int value);
	
	public ArrayList<IFeatureLayer> getSnapableLayers();
	
	public void updateSnapableLayers(ArrayList<IFeatureLayer> updateLayers);

}
