package org.redcross.sar.wp.messageLog;

import org.redcross.sar.mso.data.IMessageIf;

/**
 * Implemented by all dialogs editing messages. Needed for updating dialogs, showing, hiding, etc.
 * 
 * @author thomasl
 *
 */
public interface IEditMessageDialogIf
{
	/**
	 * Updates the contents of a dialog when a new message is selected, or an old one is changed
	 * @param message
	 */
	public void newMessageSelected(IMessageIf message);
	
	public void showDialog();
	
	public void hideDialog();
	
	public void clearContents();
}
