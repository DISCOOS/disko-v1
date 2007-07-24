package org.redcross.sar.wp.messageLog;

import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.data.IMessageIf;

public class MessagePositionDialog extends DiskoDialog
{
	public MessagePositionDialog(IDiskoWpMessageLog wp)
	{
		super(wp.getApplication().getFrame());
	}

	public void updateContents(IMessageIf message) 
	{
		// TODO Auto-generated method stub	
	}

	public void clearPanelContents()
	{
		// TODO Auto-generated method stub
		
	}

}
