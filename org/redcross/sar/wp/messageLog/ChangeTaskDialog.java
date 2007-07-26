package org.redcross.sar.wp.messageLog;

import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.data.IMessageIf;

public class ChangeTaskDialog extends DiskoDialog implements IEditMessageDialogIf
{
	public ChangeTaskDialog(IDiskoWpMessageLog wp)
	{
		super(wp.getApplication().getFrame());
	}

	@Override
	public void hideDialog()
	{
		this.setVisible(false);
	}

	@Override
	public void newMessageSelected(IMessageIf message)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showDialog()
	{
		this.setVisible(true);
	}

	@Override
	public void clearContents()
	{
		// TODO Auto-generated method stub
		
	}
}
