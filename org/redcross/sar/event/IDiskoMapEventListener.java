package org.redcross.sar.event;

import java.io.IOException;
import java.util.EventListener;

public interface IDiskoMapEventListener extends EventListener {
	
	public void onExtentUpdated(DiskoMapEvent e) throws IOException;

	public void onMapReplaced(DiskoMapEvent e) throws IOException;
	
	public void onAfterScreenDraw(DiskoMapEvent e) throws IOException;
	
	public void onSelectionChanged(DiskoMapEvent e) throws IOException; 
	
	public void editLayerChanged(DiskoMapEvent e);
	
}
