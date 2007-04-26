package org.redcross.sar.map;

import java.util.List;
import com.esri.arcgis.carto.FeatureLayer;
import com.esri.arcgis.carto.IFeatureLayer;
import com.esri.arcgis.geometry.IEnvelope;

public interface IDiskoMapManager {

	public IDiskoMap getMapInstance();

	public List getMaps();

	/**
	 * Refresh all map in this DiskoApplication. Should be called after database updates.
	 * @param env A envelope to define the area to refresh
	 */
	public void refreshAllMaps(IEnvelope env);
	
	public IFeatureLayer getPoiFeatureLayer();
	
	public FeatureLayer getBasicLineFeatureLayer();

	public FeatureLayer getFlankFeatureLayer();
}