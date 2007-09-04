package org.redcross.sar.gui.renderers;

import java.awt.Component;
import java.util.Calendar;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.redcross.sar.util.mso.DTG;

public class DTGListCellRenderer extends JLabel implements ListCellRenderer
{
    private static final long serialVersionUID = 1L;

    public DTGListCellRenderer()
    {
        super.setOpaque(true);
    }

    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus)
    {
        setText(DTG.CalToDTG((Calendar)value));
        if (isSelected)
        {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else
        {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }
}