package org.redcross.sar.event;

import java.util.EventListener;

public interface IDialogEventListener extends EventListener {
	
	public void dialogFinished(DialogEvent e);
	
	public void dialogCanceled(DialogEvent e);
}
