package org.redcross.sar.wp.messageLog;

import java.awt.Frame;

import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.data.IMessageIf;

/**
 * 
 * @author thomasl
 *
 * The dialog for changing the from field of a message in the message log wp
 */

public class ChangeFromDialog extends DiskoDialog implements IEditMessageDialogIf
{

	public ChangeFromDialog(IDiskoWpMessageLog messageLog)
	{
		super(messageLog.getApplication().getFrame());
		// TODO Auto-generated constructor stub
	}

	@Override
	public void newMessageSelected(IMessageIf message)
	{
		// TODO Auto-generated method stub
		
	}
	
}
