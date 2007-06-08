package org.redcross.sar.event;

import java.io.IOException;
import java.util.EventListener;

import com.esri.arcgis.interop.AutomationException;

public interface IDiskoMapEventListener extends EventListener {
	
	public void onExtentUpdated(DiskoMapEvent e) throws IOException, AutomationException;

	public void onMapReplaced(DiskoMapEvent e) throws IOException, AutomationException;
	
	public void onAfterScreenDraw(DiskoMapEvent e) throws IOException, AutomationException;
	
	public void onSelectionChanged(DiskoMapEvent e) throws IOException, AutomationException; 
	
	public void editLayerChanged(DiskoMapEvent e);
}
