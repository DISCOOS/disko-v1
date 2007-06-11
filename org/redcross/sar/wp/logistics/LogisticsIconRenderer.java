package org.redcross.sar.wp.logistics;

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
//            Container p = table.getParent();
//            if (p != null)
//            {
//                iconValue.setBackground(p.getBackground());
//            }
            if (isSelected && !iconValue.isSelectable())
            {
                isSelected = false;
                table.getSelectionModel().clearSelection();
            }
            if (column == 0)
            {
                table.setRowHeight(row, iconValue.getIconHeight() + 16);
            }
            if (row == 0)
            {
                TableColumn col = table.getColumnModel().getColumn(column);
                col.setMaxWidth(iconValue.getIconWidth() + 16);
                col.setMinWidth(iconValue.getIconWidth());
                col.setPreferredWidth(iconValue.getIconWidth() + 16);
            }
            iconValue.setSelected(isSelected && hasFocus);
            setText("");
            setIcon(iconValue);
        } else
        {
            setText(value.toString());
            setIcon(null);
        }
        if (column == 0)
        {
            setBorder(BorderFactory.createMatteBorder(2, 2, 2, 0, Color.BLACK));
        } else if (column == table.getColumnCount() - 1)
        {
            setBorder(BorderFactory.createMatteBorder(2, 0, 2, 2, Color.BLACK));
        } else
        {
            setBorder(BorderFactory.createMatteBorder(2, 0, 2, 0, Color.BLACK));
        }
        setHorizontalAlignment(SwingConstants.CENTER);
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


