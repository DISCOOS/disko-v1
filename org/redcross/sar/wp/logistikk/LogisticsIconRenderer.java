package org.redcross.sar.wp.logistikk;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

/**
 *
 */
public class LogisticsIconRenderer extends JLabel implements TableCellRenderer
{

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        if (value instanceof LogisticsIcon)
        {
            LogisticsIcon iconValue = (LogisticsIcon) value;
            if (isSelected && !iconValue.isSelectable())
            {
                isSelected = false;
                table.getSelectionModel().clearSelection();
            }
            if (column == 0)
            {
                table.setRowHeight(row, iconValue.getIconHeight() + 5);
            }
            if (row == 0)
            {
                TableColumn col = table.getColumnModel().getColumn(column);
                col.setMaxWidth(iconValue.getIconWidth() + 20);
                col.setMinWidth(iconValue.getIconWidth());
                col.setPreferredWidth(iconValue.getIconWidth() + 5);
            }
            iconValue.setSelected(isSelected && hasFocus);
            setText("");
            setIcon(iconValue);
        } else
        {
            setText(value.toString());
            setIcon(null);
        }
        return this;
    }

    public static class InfoIconRenderer extends LogisticsIconRenderer
    {

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
}


