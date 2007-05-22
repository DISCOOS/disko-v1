package org.redcross.sar.map;

import java.io.IOException;
import java.util.List;
import org.redcross.sar.map.layer.AbstractMsoLayer;
import com.esri.arcgis.geometry.IEnvelope;
import com.esri.arcgis.interop.AutomationException;

public interface IDiskoMapManager {

	public IDiskoMap getMapInstance();

	public List getMaps();

	/**
	 * Refresh all map in this DiskoApplication. Should be called after database updates.
	 * @param env A envelope to define the area to refresh
	 */
	public void refreshAllMaps(IEnvelope env) throws AutomationException, IOException;
	
	public List getMsoLayers();
	
	public AbstractMsoLayer getMsoLayer(Enum classCode);
}