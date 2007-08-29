package org.redcross.sar.wp.messageLog;

import org.redcross.sar.mso.data.IMessageIf;

/**
 * Implemented by all components editing messages. Needed for updating, showing, hiding, etc.
 * 
 * @author thomasl
 *
 */
public interface IEditMessageComponentIf
{
	/**
	 * Updates the contents of a dialog when a new message is selected, or an old one is changed
	 * @param message
	 */
	public void newMessageSelected(IMessageIf message);
	
	public void showComponent();
	
	public void hideComponent();
	
	public void clearContents();
}
