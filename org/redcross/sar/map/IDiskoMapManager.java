package org.redcross.sar.map;

import java.util.List;

import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.mso.IMsoManagerIf;

public interface IDiskoMapManager {

	public IDiskoMap getMapInstance();

	public List getMaps();
	
	public List getMsoLayers();
	
	public List getMsoLayers(IMsoManagerIf.MsoClassCode classCode);
	
	public IMsoFeatureLayer getMsoLayer(IMsoFeatureLayer.LayerCode layerCode);
	
	public String getCurrentMxd();
}