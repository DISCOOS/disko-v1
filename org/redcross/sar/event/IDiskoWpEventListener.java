package org.redcross.sar.event;

import java.util.EventListener;

public interface IDiskoWpEventListener extends EventListener {
	
	public void taskStarted(DiskoWpEvent e);
	
	public void taskCanceled(DiskoWpEvent e);

	public void taskFinished(DiskoWpEvent e);
	
}
