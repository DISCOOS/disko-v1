package org.redcross.sar.wp.messageLog;

import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.data.IMessageIf;

public class ChangeTaskDialog extends DiskoDialog implements IEditMessageDialogIf
{
	public ChangeTaskDialog(IDiskoWpMessageLog wp)
	{
		super(wp.getApplication().getFrame());
	}

	public void hideDialog()
	{
		this.setVisible(false);
	}

	public void newMessageSelected(IMessageIf message)
	{
		// TODO Auto-generated method stub
		
	}

	public void showDialog()
	{
		this.setVisible(true);
	}

	public void clearContents()
	{
		// TODO Auto-generated method stub
		
	}
}
