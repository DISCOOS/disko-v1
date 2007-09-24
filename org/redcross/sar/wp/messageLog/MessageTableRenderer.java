package org.redcross.sar.wp.messageLog;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLogIf;
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
	
	private IMessageLogIf m_log = null;

	/**
	 */
	public MessageTableRenderer()
    {
        setLineWrap(true);
        setWrapStyleWord(true);
    }

	/**
	 * @param log
	 */
    public MessageTableRenderer(IMessageLogIf log)
	{
    	setOpaque(true);
    	setLineWrap(true);
        setWrapStyleWord(true);
        m_log  = log;
	}

    /**
     * Get cell component. Message lines with status postponed have pink background 
     */
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
		LogTableModel model = (LogTableModel)table.getModel();
        // Contents
        if(value instanceof String[])
        {	
        	Boolean extended = model.isMessageExpanded(row+1);
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
        
//        // Size
//        setSize(table.getColumnModel().getColumn(column).getWidth(),
//                getPreferredSize().height);
        
        // Colors
        int messageNr = Integer.valueOf(((String)model.getValueAt(row, 0)).split("\\s")[0]);
        IMessageIf selectedMessage = MessageLogTopPanel.getSelectedMessage(); 
        if(selectedMessage != null && selectedMessage.getNumber() == messageNr)
        {
        	setForeground(table.getSelectionForeground());
        	setBackground(table.getSelectionBackground());
        }
        else
        {
        	setForeground(table.getForeground());
        	setBackground(table.getBackground());
        	
        	if(m_log != null)
            {
            	for(IMessageIf message : m_log.getItems())
                {
                	if(message.getNumber() == (row+1) && message.getStatus() == MessageStatus.POSTPONED)
                	{
                		setBackground(Color.pink);
                	}
                }
            }
        }
        
        return this;
    }

}
