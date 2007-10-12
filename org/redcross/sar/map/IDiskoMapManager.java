package org.redcross.sar.map;

import java.util.List;

public interface IDiskoMapManager {

	public IDiskoMap getMapInstance();

	public List getMaps();
	
	public String getCurrentMxd();
	
	public IDiskoMap getPrintMap();
}