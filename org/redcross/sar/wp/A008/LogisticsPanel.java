package org.redcross.sar.wp.A008;

import org.redcross.sar.app.Utils;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IUnitListIf;
import org.redcross.sar.util.mso.Selector;
import org.redcross.sar.wp.AbstractDiskoWpModule;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
/**
 * Created by IntelliJ IDEA.
 * User: vinjar
 * Date: 11.apr.2007
 * To change this template use File | Settings | File Templates.
 */

/**
 *
 */
public class LogisticsPanel
{

    private static final Dimension labelDim = new Dimension(60, 60);
    private JPanel panel1;
    private JPanel m_mapPanel;
    private JPanel m_assignmentPanel;
    private JPanel m_unitPanel;
    private JPanel m_infoPanel;
    private JTable m_unitTable;
    private JTable m_assignmentTable;
    private AbstractDiskoWpModule m_wpModule;

    public LogisticsPanel(AbstractDiskoWpModule aWp)
    {
        m_infoPanel.addHierarchyBoundsListener(new HierarchyBoundsAdapter()
        {
            public void ancestorResized(HierarchyEvent e)
            {
                super.ancestorResized(e);    //To change body of overridden methods use File | Settings | File Templates.
            }
        });

        m_unitTable.setModel(new UnitTableModel());
        m_unitTable.setAutoCreateColumnsFromModel(true);
        m_unitTable.setDefaultRenderer(IUnitIf.class, new UnitRenderer());
        m_unitTable.setDefaultRenderer(java.util.List.class, new AssignmentRenderer());
        m_unitTable.setRowHeight(32);
        m_wpModule = aWp;
    }

    public JPanel getPanel()
    {
        setTableData();
        return panel1;
    }

    public JPanel getMapPanel()
    {

        return m_mapPanel;
    }

    public JPanel getInfoPanel()
    {
        return m_infoPanel;
    }

    public JTable getUnitTable()
    {
        return m_unitTable;
    }

    public JTable getAssignmentTable()
    {
        return m_assignmentTable;
    }


    private final static Selector<IUnitIf> m_activeUnitSelector = new Selector<IUnitIf>()
    {
        private EnumSet<IUnitIf.UnitStatus> m_activeStatus = EnumSet.range(IUnitIf.UnitStatus.READY, IUnitIf.UnitStatus.WORKING);

        public boolean select(IUnitIf anObject)
        {
            return m_activeStatus.contains(anObject.getStatus());
        }
    };

    private final static Comparator<IUnitIf> m_unitNumberComparator = new Comparator<IUnitIf>()
    {
        public int compare(IUnitIf u1,IUnitIf u2)
        {
            return u1.getNumber() - u2.getNumber();
        }
    };

    private void setTableData()
    {
        UnitTableModel utm = (UnitTableModel) m_unitTable.getModel();
        IUnitListIf unitList = m_wpModule.getMsoManager().getCmdPost().getUnitList();
        for (IUnitIf unit : unitList.selectItems(m_activeUnitSelector, m_unitNumberComparator))
        {
            utm.addUnit(unit);
        }
    }

    public static class UnitRenderer extends DefaultTableCellRenderer
    {
        static final HashMap<IUnitIf.UnitType, Icon> icons = new HashMap();

        static
        {
            String iconName = "MapZoomInTool.icon";
            try
            {
                Icon icon = Utils.createImageIcon("icons/zoom_in_tool_1.gif", iconName);
                icons.put(IUnitIf.UnitType.AIRCRAFT, icon);
                icon = Utils.createImageIcon("icons/zoom_out_tool_1.gif", iconName);
                icons.put(IUnitIf.UnitType.TEAM, icon);
            }
            catch (Exception e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            IUnitIf unit = (IUnitIf) value;


            JLabel retVal = new JLabel("" + unit.getNumber(), icons.get(unit.getType()), JLabel.CENTER);
            if (isSelected || hasFocus)
            {
                retVal = (JLabel) super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
                retVal.setText("" + unit.getNumber());
                retVal.setIcon(icons.get(unit.getType()));
                retVal.setHorizontalAlignment(JLabel.CENTER);
            }
            return retVal;

        }
    }

    public static class AssignmentRenderer extends DefaultTableCellRenderer
    {
        static final HashMap<Integer, Icon> icons = new HashMap();

        static
        {
            String iconName = "MapZoomInTool.icon";
            try
            {
                Icon icon = Utils.createImageIcon("icons/arrow_left.gif", iconName);
                icons.put(0, icon);
                icon = Utils.createImageIcon("icons/arrow_right.gif", iconName);
                icons.put(1, icon);
            }
            catch (Exception e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            java.util.List<IAssignmentIf> assignments = (java.util.List<IAssignmentIf>) value;
            JLabel retVal = null;
            if (assignments.size() == 0)
            {
                retVal = new JLabel();
            } else
            {
                retVal = new JLabel("" + assignments.get(0).getNumber() + "(" + assignments.size() + ")", icons.get(assignments.get(0).getTypenr()), JLabel.LEFT);
            }
            if (isSelected || hasFocus)
            {
                super.getTableCellRendererComponent(table, retVal, isSelected, hasFocus, row, column);
            }
            retVal.setPreferredSize(labelDim);
            retVal.setMinimumSize(labelDim);
            retVal.setMaximumSize(labelDim);
            retVal.setBorder(BorderFactory.createEtchedBorder());

            return retVal;
        }
    }
}
