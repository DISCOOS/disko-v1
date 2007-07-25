package org.redcross.sar.wp.messageLog;

import org.redcross.sar.mso.data.IMessageIf;

public interface IEditMessageDialogIf
{
	public void newMessageSelected(IMessageIf message);
	
	public void showDialog();
	
	public void hideDialog();
}
