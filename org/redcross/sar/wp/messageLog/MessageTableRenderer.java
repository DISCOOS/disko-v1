package org.redcross.sar.wp.messageLog;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class MessageTableRenderer extends JTextArea implements TableCellRenderer
{
    public MessageTableRenderer()
    {
        setLineWrap(true);
        setWrapStyleWord(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
    	
    	
        StringBuilder messageString = new StringBuilder();
        String[] messageLines = (String[]) value;
        for (int i = 0; i < messageLines.length; i++)
        {
            messageString.append(messageLines[i]);
            messageString.append("\n");
        }
        setText(messageString.toString());
        setSize(table.getColumnModel().getColumn(column).getWidth(),
                getPreferredSize().height);
        if (isSelected)
        {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        }
        else
        {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }
        
        return this;
    }

}
