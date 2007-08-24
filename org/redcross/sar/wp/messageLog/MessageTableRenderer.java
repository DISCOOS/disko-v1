package org.redcross.sar.wp.messageLog;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLogIf;
import org.redcross.sar.mso.data.IMessageIf.MessageStatus;

import java.awt.*;

public class MessageTableRenderer extends JTextArea implements TableCellRenderer
{
    private IMessageLogIf m_log = null;

	public MessageTableRenderer()
    {
        setLineWrap(true);
        setWrapStyleWord(true);
    }

    public MessageTableRenderer(IMessageLogIf log)
	{
    	setLineWrap(true);
        setWrapStyleWord(true);
        
        m_log  = log;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        // Contents
        if(value instanceof String[])
        {
        	StringBuilder messageString = new StringBuilder();
        	String[] messageLines = (String[]) value;
            for (int i = 0; i < messageLines.length; i++)
            {
                messageString.append(messageLines[i]);
                messageString.append("\n");
            }
            setText(messageString.toString());
            setForeground(table.getForeground());
            
        }
        else if(value instanceof String)
        {
        	setText((String)value);
        }
        
        // Size
        setSize(table.getColumnModel().getColumn(column).getWidth(),
                getPreferredSize().height);
        
        // Colors
        setBackground(table.getBackground());
        if(m_log != null)
        {
        	for(IMessageIf message : m_log.getItems())
            {
            	// TODO is message number and row always consistent?
            	if(message.getNumber() == (row+1) && message.getStatus() == MessageStatus.POSTPONED)
            	{
            		setBackground(Color.pink);
            	}
            }
        }
        
        return this;
    }

}
