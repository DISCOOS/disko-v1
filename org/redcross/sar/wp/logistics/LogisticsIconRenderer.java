package org.redcross.sar.wp.logistics;

import org.redcross.sar.gui.DiskoBorder;
import org.redcross.sar.gui.renderers.IconRenderer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

/**
 *
 */
public class LogisticsIconRenderer extends JLabel implements TableCellRenderer
{

    private final static Border firstCellBorder = new DiskoBorder(2, 8, true, true, true, false);
    private final static Border middleCellBorder = new DiskoBorder(2, 8, true, false, true, false);
    private final static Border lastCellBorder = new DiskoBorder(2, 8, true, false, true, true);

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        if (value instanceof IconRenderer)
        {
            IconRenderer iconValue = (IconRenderer) value;
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
            setBorder(firstCellBorder);
        } else if (column == table.getColumnCount() - 1)
        {
            setBorder(lastCellBorder);
        } else
        {
            setBorder(middleCellBorder);
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


