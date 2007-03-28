package org.redcross.sar.event;

import java.util.EventListener;

public interface IDiskoWpEventListener extends EventListener {
	
	public void wpCanceled(DiskoWpEvent e);

	public void wpFinished(DiskoWpEvent e);
	
}
