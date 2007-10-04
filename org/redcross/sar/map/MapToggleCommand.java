package org.redcross.sar.map;

import java.io.IOException;

import com.esri.arcgis.interop.AutomationException;

public class MapToggleCommand extends AbstractCommandTool {

	private static final long serialVersionUID = 1L;
	
	public void onCreate(Object obj) throws IOException, AutomationException {
		if (obj instanceof IDiskoMap) {
			map = (DiskoMap)obj;
		}
	}

	public void onClick() throws IOException, AutomationException {
	}

	public void toolActivated() throws IOException, AutomationException {
		// forward
		super.toolActivated();
		// 
		DiskoMapManagerImpl man = (DiskoMapManagerImpl) map.getMapManager();
		man.toggleMap(); 
	}
	
	public void toolDeactivated() throws IOException, AutomationException {
		// forward
		super.toolDeactivated();
		// 
	}	
}
