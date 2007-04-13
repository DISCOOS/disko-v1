package org.redcross.sar.wp.A008;

import org.redcross.sar.app.Utils;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IAssignmentIf;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.*;
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

    private static final Dimension labelDim=new Dimension(60,60);
    private JPanel panel1;
    private JPanel m_mapPanel;
    private JPanel m_assignmentPanel;
    private JPanel m_unitPanel;
    private JPanel m_infoPanel;
    private JTable m_unitTable;
    private JTable m_assignmentTable;

    public LogisticsPanel()
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
        m_unitTable.setDefaultRenderer(UnitTableModel.TmpUnit.class,new UnitRenderer());
        m_unitTable.setDefaultRenderer(UnitTableModel.TmpAssignment[].class,new AssignmentRenderer());
        m_unitTable.setRowHeight(32);
    }

    public JPanel getPanel()
    {
        setTmpData();
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


    private void setTmpData()
    {
        UnitTableModel utm = (UnitTableModel)m_unitTable.getModel();
        UnitTableModel.TmpUnit un;
        UnitTableModel.TmpAssignment as;
        un = new UnitTableModel.TmpUnit(1,IUnitIf.UnitType.AIRCRAFT);
        as = new UnitTableModel.TmpAssignment(1,0, IAssignmentIf.AssignmentStatus.FINISHED);
        un.getNesteList().add(as);
        utm.addUnit(un);
    }

    public void main(String[] args)
    {
        JFrame frame = new JFrame("frame");
        LogisticsPanel lp = new LogisticsPanel();
        frame.getContentPane().add(lp.getPanel());
        lp.setTmpData();
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
            UnitTableModel.TmpUnit tmpUnit = (UnitTableModel.TmpUnit) value;


            JLabel retVal = new JLabel("" + tmpUnit.getEnhnr(), icons.get(tmpUnit.getType()), JLabel.CENTER);
            if (isSelected || hasFocus)
            {
                retVal=(JLabel) super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
                retVal.setText("" + tmpUnit.getEnhnr());
                retVal.setIcon(icons.get(tmpUnit.getType()));
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
            UnitTableModel.TmpAssignment[] tmpAssignment = (UnitTableModel.TmpAssignment[]) value;
            JLabel retVal=null;
            if(tmpAssignment.length==0)
            {
                retVal=new JLabel();
            }
            else
            {
            retVal = new JLabel("" + tmpAssignment.length, icons.get(tmpAssignment[0].getType()), JLabel.LEFT);
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
