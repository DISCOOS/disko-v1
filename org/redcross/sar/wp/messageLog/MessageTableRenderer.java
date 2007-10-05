package org.redcross.sar.wp.messageLog;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageIf.MessageStatus;

import java.awt.*;

/**
 * Custom cell renderer for message log table
 * 
 * @author vinjar/thomasl
 */
public class MessageTableRenderer extends JTextArea implements TableCellRenderer
{
	private static final long serialVersionUID = 1L;

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
		IMessageIf message = (IMessageIf)model.getValueAt(table.convertRowIndexToModel(row), 7);
		
		int messageNr = message.getNumber();
		
		// Set text
		switch(column)
		{
		case 0:
			// Number cell
			Boolean expanded = model.isMessageExpanded(message.getNumber());
        	StringBuilder string = new StringBuilder(Integer.toString(message.getNumber()));
        	if(model.numRows(row) > 1)
        	{
        		if(expanded)
            	{
            		string.append(" -");
            	}
            	else
            	{
            		string.append(" +");
            	}
        	}
        	setText(string.toString());
        	break;
		case 4:
		case 5:
			// Message lines
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
			break;
		case 6:
			// Message status
			setText(message.getStatusText());
			break;
		default:
			if(value instanceof String)
			{
				setText((String)value);
			}
			break;
		}
        
        // Colors
        IMessageIf selectedMessage = MessageLogBottomPanel.getCurrentMessage(false); 
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
