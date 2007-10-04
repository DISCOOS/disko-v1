package org.redcross.sar.wp.messageLog;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageIf.MessageStatus;
import org.redcross.sar.util.Internationalization;

import java.awt.*;
import java.util.ResourceBundle;

/**
 * Custom cell renderer for message log table
 * 
 * @author vinjar/thomasl
 */
public class MessageTableRenderer extends JTextArea implements TableCellRenderer
{
	private static final long serialVersionUID = 1L;
	
	private static final ResourceBundle m_messageResources = 
		ResourceBundle.getBundle("org.redcross.sar.mso.data.properties.Message");

	/**
	 * @param log
	 */
    public MessageTableRenderer()
	{
    	setOpaque(true);
    	setLineWrap(true);
        setWrapStyleWord(true);
	}

    /**
     * Get cell component. Message lines with status postponed have pink background 
     */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
		LogTableModel model = (LogTableModel)table.getModel();
		int messageNr = Integer.valueOf(((String)model.getValueAt(table.convertRowIndexToModel(row), 0)).split("\\s")[0]);
		
        // Contents
        if(value instanceof String[])
        {	
        	Boolean extended = model.isMessageExpanded(messageNr);
        	StringBuilder messageString = new StringBuilder();
        	String[] messageLines = (String[]) value;
      
        	if(extended != null && extended)
        	{
        		// Show lines in expanded mode
        		for (int i = 0; i < messageLines.length; i++)
                {
                    messageString.append(messageLines[i]);
                    messageString.append("\n");
                }
        	}
        	else
        	{
        		// Show lines in compressed mode
        		for (int i = 0; i < messageLines.length; i++)
                {
                    messageString.append(messageLines[i]);
                    messageString.append(" ");
                }
        	}
        	
            setText(messageString.toString());
            setForeground(table.getForeground());
            
        }
        else if(value instanceof String)
        {
        	setText((String)value);
        }
        else if(value instanceof MessageStatus)
        {
        	setText(Internationalization.getEnumText(m_messageResources, (MessageStatus)value));
        }
        
        // Colors
        IMessageIf selectedMessage = MessageLogTopPanel.getCurrentMessage(false); 
        if(selectedMessage != null && selectedMessage.getNumber() == messageNr)
        {
        	setForeground(table.getSelectionForeground());
        	setBackground(table.getSelectionBackground());
        }
        else
        {
        	setForeground(table.getForeground());
        	
        	if(((MessageStatus)model.getValueAt(table.convertRowIndexToModel(row), 6)) == MessageStatus.POSTPONED)
        	{
        		setBackground(Color.pink);
        	}
        	else
        	{
        		setBackground(table.getBackground());
        	}
        }
        
        return this;
    }

}
