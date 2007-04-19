package org.redcross.sar.wp.A008;

import org.redcross.sar.app.Utils;
import org.redcross.sar.mso.data.*;
import org.redcross.sar.util.except.IllegalOperationException;
import org.redcross.sar.util.except.DuplicateIdException;
import org.redcross.sar.wp.AbstractDiskoWpModule;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
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
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JTable m_assignmentTable;
    private JButton Allocate;
    private AbstractDiskoWpModule m_wpModule;
    private IUnitListIf m_unitList;
    private IAssignmentListIf m_assignmentList;

    public LogisticsPanel(AbstractDiskoWpModule aWp)
    {
        m_infoPanel.addHierarchyBoundsListener(new HierarchyBoundsAdapter()
        {
            public void ancestorResized(HierarchyEvent e)
            {
                super.ancestorResized(e);    //To change body of overridden methods use File | Settings | File Templates.
            }
        });

        m_wpModule = aWp;
        m_unitList = m_wpModule.getMsoManager().getCmdPost().getUnitList();
        m_unitTable.setModel(new UnitTableModel(m_wpModule.getMmsoEventManager(),m_unitList));
        m_unitTable.setAutoCreateColumnsFromModel(true);
        m_unitTable.setDefaultRenderer(IUnitIf.class, new UnitRenderer());
        m_unitTable.setDefaultRenderer(java.util.List.class, new AssignmentRenderer());
        m_unitTable.setRowHeight(40);

        m_assignmentList = m_wpModule.getMsoManager().getCmdPost().getAssignmentList();
        m_assignmentTable.setModel(new AssignmentTableModel(m_wpModule.getMmsoEventManager(),m_assignmentList));
        m_unitTable.setAutoCreateColumnsFromModel(true);
        m_assignmentTable.setDefaultRenderer(IAssignmentIf.class, new AssignmentPanelRenderer());
        m_assignmentTable.setRowHeight(400);



        button1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                moveAssigment(1);
            }
        });
        button2.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                moveAssigment(2);
            }
        });
        button3.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                moveUnit();
            }
        });
        Allocate.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                allocateAssignment(1);
            }
        });
    }

    private void moveUnit()
    {
         java.util.List<IUnitIf> emptyUnits = m_unitList.selectItems(new AbstractMsoObject.StatusSelector<IUnitIf,IUnitIf.UnitStatus>(IUnitIf.UnitStatus.EMPTY),
                 null);
        if (emptyUnits.size()> 0)
        {
            emptyUnits.get(0).setStatus(IUnitIf.UnitStatus.READY);
            m_wpModule.getMsoModel().commit();
        }
    }

    private final static AbstractMsoObject.StatusSelector<IAssignmentIf, IAssignmentIf.AssignmentStatus> m_readyAssignmentSelector = new AbstractMsoObject.StatusSelector<IAssignmentIf, IAssignmentIf.AssignmentStatus>(IAssignmentIf.AssignmentStatus.READY);
    
    private void allocateAssignment(int aUnitNr)
    {
        IUnitIf unit = m_unitList.getUnit(aUnitNr);
        java.util.List<IAssignmentIf> assignments = m_assignmentList.selectItems(m_readyAssignmentSelector,null);
        if (assignments.size() > 0)
        {
            try
            {
                unit.addUnitAssignment(assignments.get(0));
            }
            catch (DuplicateIdException e)
            {
                e.printStackTrace();
            }
            catch (IllegalOperationException e)
            {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        m_wpModule.getMsoModel().commit();
    }

    private void moveAssigment(int aUnitNr)
    {
        IUnitIf unit = m_unitList.getUnit(aUnitNr);
        java.util.List<IAssignmentIf> assigments;

        try
        {
            assigments = unit.getExecutingAssigment();
            if (assigments.size() > 0)
            {
                assigments.get(0).setStatus(IAssignmentIf.AssignmentStatus.FINISHED);
            } else
            {
                assigments = unit.getAssignedAssignments();
                if (assigments.size() > 0)
                {
                    assigments.get(0).setStatus(IAssignmentIf.AssignmentStatus.EXECUTING);
                } else
                {
                    assigments = unit.getAllocatedAssignments();
                    if (assigments.size() > 0)
                    {
                        assigments.get(0).setStatus(IAssignmentIf.AssignmentStatus.ASSIGNED);
                    }
                }
            }
        }
        catch (IllegalOperationException e)
        {
            e.printStackTrace();
        }
        finally
        {
            m_wpModule.getMsoModel().commit();
        }
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


    private void setTableData()
    {
        UnitTableModel utm = (UnitTableModel) m_unitTable.getModel();
        utm.buildTable();
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$()
    {
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        final JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setContinuousLayout(true);
        panel1.add(splitPane1, BorderLayout.CENTER);
        final JSplitPane splitPane2 = new JSplitPane();
        splitPane2.setContinuousLayout(true);
        splitPane2.setOneTouchExpandable(false);
        splitPane1.setRightComponent(splitPane2);
        m_unitPanel = new JPanel();
        m_unitPanel.setLayout(new BorderLayout(0, 0));
        splitPane2.setRightComponent(m_unitPanel);
        final JScrollPane scrollPane1 = new JScrollPane();
        m_unitPanel.add(scrollPane1, BorderLayout.CENTER);
        m_unitTable = new JTable();
        scrollPane1.setViewportView(m_unitTable);
        m_assignmentPanel = new JPanel();
        m_assignmentPanel.setLayout(new BorderLayout(0, 0));
        splitPane2.setLeftComponent(m_assignmentPanel);
        final JScrollPane scrollPane2 = new JScrollPane();
        m_assignmentPanel.add(scrollPane2, BorderLayout.CENTER);
        m_assignmentTable = new JTable();
        scrollPane2.setViewportView(m_assignmentTable);
        final JSplitPane splitPane3 = new JSplitPane();
        splitPane3.setContinuousLayout(true);
        splitPane3.setOneTouchExpandable(false);
        splitPane3.setOrientation(0);
        splitPane1.setLeftComponent(splitPane3);
        m_infoPanel = new JPanel();
        m_infoPanel.setLayout(new BorderLayout(0, 0));
        splitPane3.setRightComponent(m_infoPanel);
        m_mapPanel = new JPanel();
        m_mapPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        splitPane3.setLeftComponent(m_mapPanel);
        button1 = new JButton();
        button1.setEnabled(true);
        button1.setText("Unit 1");
        m_mapPanel.add(button1);
        button2 = new JButton();
        button2.setText("Unit 2");
        m_mapPanel.add(button2);
        button3 = new JButton();
        button3.setText("Ready Unit");
        m_mapPanel.add(button3);
        Allocate = new JButton();
        Allocate.setText("Allocate");
        m_mapPanel.add(Allocate);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return panel1;
    }

    public static class UnitRenderer extends DefaultTableCellRenderer
    {
        static final HashMap<IUnitIf.UnitType, Icon> icons = new HashMap<IUnitIf.UnitType, Icon>();

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
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            java.util.List<IAssignmentIf> assignments = (java.util.List<IAssignmentIf>) value;
            JLabel retVal = null;
            if (assignments.size() == 0)
            {
                retVal = new AssignmentLabel();
            } else
            {
                retVal = new AssignmentLabel(assignments);
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

    public static class AssignmentLabel extends JLabel
    {
        static final HashMap<Integer, Icon> icons = new HashMap<Integer, Icon>();

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

        public AssignmentLabel()
        {
            super();
        }

        public AssignmentLabel(IAssignmentIf anAssignment)
        {
            super("" + anAssignment.getNumber() , JLabel.CENTER);
        }

        public AssignmentLabel(java.util.List<IAssignmentIf> theAssignments)
        {
            super("" + theAssignments.get(0).getNumber() + "(" + theAssignments.size() + ")", icons.get(theAssignments.get(0).getTypenr()), JLabel.CENTER);
        }
    }

    public static class AssignmentPanelRenderer extends DefaultTableCellRenderer
    {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            java.util.List<IAssignmentIf> assignments = (java.util.List<IAssignmentIf>) value;
            JLabel retVal = null;
            if (assignments.size() == 0)
            {
                retVal = new AssignmentLabel();
            } else
            {
                retVal = new AssignmentLabel(assignments);
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
