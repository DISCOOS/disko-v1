package org.redcross.sar.wp.messageLog;

import javax.swing.JComponent;

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
	 * Updates the contents of the component when a new message is selected, or an old one gets a field updated
	 * @param message
	 */
	public void newMessageSelected(IMessageIf message);
	
	/**
	 * Displays the component. {@link JComponent#setVisible(boolean)} doesn't suffice, components need to show
	 * other components(dialogs)
	 */
	public void showComponent();
	
	/**
	 * Displays the component. {@link JComponent#setVisible(boolean)} doesn't suffice, components need to hide
	 * other components(dialogs)
	 */
	public void hideComponent();
	
	/**
	 * Clears the content of child components for the components, e.g. resets text fields in a panel
	 */
	public void clearContents();
}
