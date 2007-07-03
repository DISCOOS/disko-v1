package org.redcross.sar.map;

import java.io.IOException;

import com.esri.arcgis.interop.AutomationException;

public class MapToggleCommand extends AbstractCommand {

	private static final long serialVersionUID = 1L;
	
	public void onCreate(Object obj) throws IOException, AutomationException {
		if (obj instanceof IDiskoMap) {
			map = (DiskoMap)obj;
		}
	}

	public void onClick() throws IOException, AutomationException {
		DiskoMapManagerImpl man = (DiskoMapManagerImpl) map.getMapManager();
		man.toggleMap(); 
	}
}
