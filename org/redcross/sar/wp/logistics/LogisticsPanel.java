package org.redcross.sar.wp.logistics;

import com.esri.arcgis.interop.AutomationException;
import org.redcross.sar.gui.renderers.IconRenderer;
import org.redcross.sar.map.IDiskoMap;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.*;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent;
import org.redcross.sar.util.except.DuplicateIdException;
import org.redcross.sar.util.except.IllegalOperationException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.EnumSet;
/**
 * Created by IntelliJ IDEA.
 * User: vinjar
 * Date: 11.apr.2007
 * To change this template use File | Settings | File Templates.
 */

/**
 *
 */
public class LogisticsPanel implements IMsoUpdateListenerIf
{

    private JPanel WorkspacePanel;
    private IDiskoMap m_map;
    private JPanel m_assignmentPanel;
    private JPanel m_unitPanel;
    private JPanel m_infoPanel;
    //    private UnitJTable m_unit1Table;
    //    private JTable m_assignmentTable;
    private JTable m_unitTable;
    private JSplitPane m_splitter1;
    private JSplitPane m_splitter2;
    private JSplitPane m_splitter3;
    private JScrollPane m_scrollPane1;
    private JScrollPane m_AssignmentSubPaneLeft;
    private JScrollPane m_AssignmentSubPaneRight;
    private DiskoWpLogisticsImpl m_wpModule;
    private IUnitListIf m_unitList;
    private IAssignmentListIf m_assignmentList;

    private AssignmentScrollPanel m_selectableAssignmentsPanel;
    private AssignmentScrollPanel m_priAssignmentsPanel;
    private AssignmentTransferHandler m_assignmentTransferHandler;

    private InfoPanelHandler m_infoPanelHandler;

    private AssignmentLabel.AssignmentLabelActionHandler m_labelActionHandler;
    private AssignmentLabel.AssignmentLabelActionHandler m_listPanelActionHandler;

    private IconRenderer.LogisticsIconActionHandler m_iconActionHandler;

    public LogisticsPanel(DiskoWpLogisticsImpl aWp)
    {
        m_wpModule = aWp;
        m_map = m_wpModule.getMap();
        m_unitList = m_wpModule.getMsoManager().getCmdPost().getUnitList();
        m_assignmentList = m_wpModule.getMsoManager().getCmdPost().getAssignmentList();


        if (!defineTransferHandler())
        {
            return;
        }
        defineSubpanelActionHandlers();
        m_splitter3.setLeftComponent((JComponent) m_map);
        setSplitters();
        setPanelSizes();
        initUnitTable();
        initInfoPanels();
        initAssignmentPanels();

        addToListeners();
    }

    private boolean defineTransferHandler()
    {
        try
        {
            m_assignmentTransferHandler = new AssignmentTransferHandler(m_wpModule);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;
        }
        return true;
    }

    private void defineSubpanelActionHandlers()
    {
        m_labelActionHandler = new AssignmentLabel.AssignmentLabelActionHandler()
        {
            public void handleClick(IAssignmentIf anAssignment)
            {
                singelAssignmentClick(anAssignment, false);
            }
        };

        m_listPanelActionHandler = new AssignmentLabel.AssignmentLabelActionHandler()
        {
            public void handleClick(IAssignmentIf anAssignment)
            {
                singelAssignmentClick(anAssignment, true);
            }
        };

        m_iconActionHandler = new IconRenderer.LogisticsIconActionHandler()
        {
            public void handleClick(IUnitIf aUnit)
            {
                getInfoPanelHandler().setUnit(aUnit);
            }

            public void handleClick(IAssignmentIf anAssignment)
            {
                singelAssignmentClick(anAssignment, false);
            }

            public void handleClick(IUnitIf aUnit, int aSelectorIndex)
            {
                getInfoPanelHandler().setUnitSelection(aUnit, aSelectorIndex);
            }
        };
    }

    private void singelAssignmentClick(IAssignmentIf anAssignment, boolean calledFromListPanel)
    {
        getInfoPanelHandler().setAssignment(anAssignment, calledFromListPanel);
        try
        {
            m_map.zoomToMsoObject(anAssignment);
            //m_map.setSelected(anAssignment, true);
        }
        catch (AutomationException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void initAssignmentPanels()
    {
        m_AssignmentSubPaneLeft.setPreferredSize(new Dimension(120, 0));
        m_AssignmentSubPaneLeft.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        m_AssignmentSubPaneLeft.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        m_AssignmentSubPaneRight.setMinimumSize(new Dimension(60, 0));
        m_AssignmentSubPaneRight.setPreferredSize(new Dimension(60, 0));
        m_AssignmentSubPaneRight.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        m_AssignmentSubPaneRight.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        m_selectableAssignmentsPanel = new AssignmentScrollPanel(m_AssignmentSubPaneLeft, new FlowLayout(FlowLayout.LEFT, 5, 5), m_labelActionHandler, true);
        m_selectableAssignmentsPanel.setTransferHandler(m_assignmentTransferHandler);

        JLabel hl;
        hl = m_selectableAssignmentsPanel.getHeaderLabel();
        hl.setHorizontalAlignment(SwingConstants.CENTER);
        hl.setPreferredSize(new Dimension(40, 40));

        m_priAssignmentsPanel = new AssignmentScrollPanel(m_AssignmentSubPaneRight, new FlowLayout(FlowLayout.LEFT, 5, 5), m_labelActionHandler, true);
        m_priAssignmentsPanel.setTransferHandler(m_assignmentTransferHandler);
        hl = m_priAssignmentsPanel.getHeaderLabel();
        hl.setHorizontalAlignment(SwingConstants.CENTER);
        hl.setPreferredSize(new Dimension(40, 40));

        AssignmentDisplayModel adm = new AssignmentDisplayModel(m_selectableAssignmentsPanel, m_priAssignmentsPanel, m_wpModule.getMmsoEventManager(), m_assignmentList);
    }

    private void initUnitTable()
    {
        final UnitTableModel model = new UnitTableModel(m_unitTable, m_wpModule, m_unitList, m_iconActionHandler);
        m_unitTable.setModel(model);
        m_unitTable.setAutoCreateColumnsFromModel(true);
        m_unitTable.setDefaultRenderer(IconRenderer.UnitIcon.class, new LogisticsIconRenderer());
        m_unitTable.setDefaultRenderer(IconRenderer.AssignmentIcon.class, new LogisticsIconRenderer());
        m_unitTable.setDefaultRenderer(IconRenderer.InfoIcon.class, new LogisticsIconRenderer.InfoIconRenderer());
        m_unitTable.setShowHorizontalLines(false);
        m_unitTable.setShowVerticalLines(true);
        m_unitTable.setRowMargin(2);
        JTableHeader tableHeader = m_unitTable.getTableHeader();
        tableHeader.setResizingAllowed(false);
        tableHeader.setReorderingAllowed(false);
        m_unitTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m_unitTable.setCellSelectionEnabled(true);
        JTableHeader th = m_unitTable.getTableHeader();
        th.setPreferredSize(new Dimension(40, 40));
        m_unitTable.setTransferHandler(m_assignmentTransferHandler);

        ListSelectionListener l = new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                if (e.getValueIsAdjusting() || !m_unitTable.hasFocus())
                {
                    return;
                }

                model.setSelectedCell(m_unitTable.getSelectionModel().getLeadSelectionIndex(),
                        m_unitTable.getColumnModel().getSelectionModel().
                                getLeadSelectionIndex());

            }
        };
        m_unitTable.getSelectionModel().addListSelectionListener(l);
        m_unitTable.getColumnModel().getSelectionModel().addListSelectionListener(l);
        m_unitTable.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent e)
            {
                System.out.println("Focus gained");
                model.setSelectedCell(m_unitTable.getSelectionModel().getLeadSelectionIndex(),
                        m_unitTable.getColumnModel().getSelectionModel().
                                getLeadSelectionIndex());
            }

            public void focusLost(FocusEvent e)
            {
                m_unitTable.clearSelection();
                System.out.println("Focus lost");
            }
        });

        m_unitTable.setDragEnabled(true);

        m_unitTable.addMouseMotionListener(new MouseMotionListener()
        {
            public void mouseDragged(MouseEvent e)
            {
                System.out.println("Mouse dragged");
            }

            public void mouseMoved(MouseEvent e)
            {
                System.out.println("Mouse moved");
            }
        });


    }

    private void initInfoPanels()
    {
        m_infoPanelHandler = new InfoPanelHandler(m_infoPanel, m_wpModule, m_listPanelActionHandler);
        m_infoPanelHandler.setSelectionTransferHandler(m_assignmentTransferHandler);
    }

    private void addToListeners()
    {
        m_wpModule.getMmsoEventManager().addClientUpdateListener(this);
    }

    private void setPanelSizes()
    {
        // minimum and preferred sizes are nice to have
        m_unitPanel.setMinimumSize(new Dimension(320, 600));
        m_unitPanel.setPreferredSize(new Dimension(320, 600));
        m_assignmentPanel.setMinimumSize(new Dimension(250, 600));
        m_assignmentPanel.setPreferredSize(new Dimension(250, 600));
        m_infoPanel.setMinimumSize(new Dimension(325, 200));
        m_infoPanel.setPreferredSize(new Dimension(325, 200));
    }

    private void setSplitters()
    {
        // Splitter between map/info panels and assignment/unit panels
        m_splitter1.setContinuousLayout(true);
        m_splitter1.setDividerLocation(375);
        m_splitter1.setResizeWeight(1.0);

        // Splitter between assignment and unit panels
        m_splitter2.setContinuousLayout(true);
        m_splitter2.setDividerLocation(250);
        m_splitter2.setResizeWeight(1.0);

        // Splitter between map and info panels, make tha map initially a square
        m_splitter3.setContinuousLayout(true);
        m_splitter3.setDividerLocation(375);
        m_splitter3.setResizeWeight(1.0);
    }

    private final EnumSet<IMsoManagerIf.MsoClassCode> myInterests = EnumSet.of(IMsoManagerIf.MsoClassCode.CLASSCODE_UNIT,
            IMsoManagerIf.MsoClassCode.CLASSCODE_ASSIGNMENT);

    public boolean hasInterestIn(IMsoObjectIf aMsoObject)
    {
        return myInterests.contains(aMsoObject.getMsoClassCode());
    }

    public void handleMsoUpdateEvent(MsoEvent.Update e)
    {
    }

    private void moveUnit() // todo remove
    {
        java.util.List<IUnitIf> emptyUnits = m_unitList.selectItems(new AbstractMsoObject.StatusSelector<IUnitIf, IUnitIf.UnitStatus>(IUnitIf.UnitStatus.EMPTY),
                null);
        if (emptyUnits.size() > 0)
        {
            emptyUnits.get(0).setStatus(IUnitIf.UnitStatus.READY);
            m_wpModule.getMsoModel().commit();
        }
    }

    private final static AbstractMsoObject.StatusSelector<IAssignmentIf, IAssignmentIf.AssignmentStatus> m_readyAssignmentSelector = new AbstractMsoObject.StatusSelector<IAssignmentIf, IAssignmentIf.AssignmentStatus>(IAssignmentIf.AssignmentStatus.READY);

    private void allocateAssignment(int aUnitNr)  // todo remove
    {
        IUnitIf unit = m_unitList.getUnit(aUnitNr);
        java.util.List<IAssignmentIf> assignments = m_assignmentList.selectItems(m_readyAssignmentSelector, null);
        if (assignments.size() > 0)
        {
            try
            {
                unit.addUnitAssignment(assignments.get(0), IAssignmentIf.AssignmentStatus.ASSIGNED);
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

    private boolean testAssignment(int aUnitNr)      // todo remove
    {
        IUnitIf unit = m_unitList.getUnit(aUnitNr);

        return unit.getExecutingAssigment().size() > 0 || unit.getAssignedAssignments().size() > 0 || unit.getAllocatedAssignments().size() > 0;
    }

    private void moveAssigment(int aUnitNr)     // todo remove
    {
        IUnitIf unit = m_unitList.getUnit(aUnitNr);
        java.util.List<IAssignmentIf> assigments;


        try
        {
            assigments = unit.getExecutingAssigment();
            if (assigments.size() > 0)
            {
                if (m_wpModule.confirmTransfer(assigments.get(0), IAssignmentIf.AssignmentStatus.FINISHED, null))
                {
                    assigments.get(0).setStatusAndOwner(IAssignmentIf.AssignmentStatus.FINISHED, unit);
                }
            } else
            {
                assigments = unit.getAssignedAssignments();
                if (assigments.size() > 0)
                {
                    if (m_wpModule.confirmTransfer(assigments.get(0), IAssignmentIf.AssignmentStatus.EXECUTING, null))
                    {
                        assigments.get(0).setStatusAndOwner(IAssignmentIf.AssignmentStatus.EXECUTING, unit);
                        assigments.get(0).setTimeStarted(Calendar.getInstance());
                    }
                } else
                {
                    assigments = unit.getAllocatedAssignments();
                    if (assigments.size() > 0)
                    {
                        if (m_wpModule.confirmTransfer(assigments.get(0), IAssignmentIf.AssignmentStatus.ASSIGNED, null))
                        {
                            assigments.get(0).setStatusAndOwner(IAssignmentIf.AssignmentStatus.ASSIGNED, unit);
                            assigments.get(0).setTimeAssigned(Calendar.getInstance());
                        }
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
        return WorkspacePanel;
    }

    public IDiskoMap getMap()
    {
        return m_map;
    }

    public InfoPanelHandler getInfoPanelHandler()
    {
        return m_infoPanelHandler;
    }

    public JTable getUnitTable()
    {
        return m_unitTable;
    }

//    public JTable getAssignmentTable()
//    {
//        return m_assignmentTable;
//    }


    private void setTableData()
    {
        UnitTableModel utm = (UnitTableModel) m_unitTable.getModel();
        utm.buildTable();
    }

    //
//    public static class UnitButton extends JButton
//    {
//        static final HashMap<IUnitIf.UnitType, Icon> icons = new HashMap<IUnitIf.UnitType, Icon>();
//        private final UnitButtonListener m_buttonListener;
//
//        static
//        {
//            String iconName = "MapZoomInTool.icon";
//            try
//            {
//                Icon icon = Utils.createImageIcon("icons/zoom_in_tool_1.gif", iconName);
//                icons.put(IUnitIf.UnitType.AIRCRAFT, icon);
//                icon = Utils.createImageIcon("icons/zoom_out_tool_1.gif", iconName);
//                icons.put(IUnitIf.UnitType.TEAM, icon);
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
//        }
//
//        public UnitButton(IUnitIf aUnit)
//        {
//            super();
//            m_buttonListener = new UnitButtonListener();
//            setUnit(aUnit);
//            setup();
//        }
//
//        public void setUnit(IUnitIf aUnit)
//        {
//            setText("" + aUnit.getNumber());
//            setIcon(icons.get(aUnit.getType()));
//            m_buttonListener.setUnit(aUnit);
//        }
//
//        private void setup()
//        {
//            setPreferredSize(buttonDim);
//            setMinimumSize(buttonDim);
//            setMaximumSize(buttonDim);
//            setEnabled(true);
//        }
//
//    }
//
//    public static class UnitButtonListener extends MouseAdapter
//    {
//        IUnitIf m_unit;
//
//        public void setUnit(IUnitIf aUnit)
//        {
//            m_unit = aUnit;
//        }
//
//        @Override
//        public void mouseClicked(MouseEvent e)
//        {
//            System.out.println("Unit info " + m_unit.getNumber());
//        }
//    }
//
//    public static class AssignmentPanelRenderer extends DefaultTableCellRenderer
//    {
//        int m_currentRowHeight;
//        int m_maxRowHeight;
//
//        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
//        {
//            if (column == 0)
//            {
//                m_currentRowHeight = table.getRowHeight(row);
//                m_maxRowHeight = 0;
//            }
//
//            JPanel retVal = new JPanel();
//            retVal.setBackground(Color.WHITE);
//            retVal.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
//            int itemCount = 1;
//            for (IAssignmentIf assignment : (java.util.List<IAssignmentIf>) value)
//            {
//                JButton asgButton = new AssignmentButton(assignment);
//                retVal.add(asgButton);
////                asgButton.addActionListener(new AssignmentButtonListener(assignment));
//                asgButton.addActionListener(new ActionListener()
//                {
//                    public void actionPerformed(ActionEvent e)
//                    {
//                        System.out.println("Jeg er trykket");
//                    }
//                });
//                itemCount++;
//            }
//
//            if (isSelected || hasFocus)
//            {
//                super.getTableCellRendererComponent(table, retVal, isSelected, hasFocus, row, column);
//            }
//            retVal.setBorder(BorderFactory.createEtchedBorder());
//
//            TableColumn col = table.getColumn(table.getModel().getColumnName(table.convertColumnIndexToModel(column)));
//            int colWidth = col.getWidth();
//
//            int horizontalCount = (colWidth - 5) / ((int) buttonDim.getWidth() + 5);
//            if (horizontalCount == 0)
//            {
//                horizontalCount = 1;
//            }
//            int verticalCount = (itemCount + horizontalCount - 1) / horizontalCount;
//            int height = verticalCount * ((int) buttonDim.getHeight() + 5);
//            m_maxRowHeight = Math.max(height + 10, m_maxRowHeight);
//
//            if (column == table.getColumnCount() - 1 && m_currentRowHeight != m_maxRowHeight)
//            {
//                table.setRowHeight(row, m_maxRowHeight);
//            }
//            return retVal;
//        }
//    }
//
//    public static class AssignmentButton extends JButton
//    {
//        static final HashMap<Integer, Icon> icons = new HashMap<Integer, Icon>();
//        private final AssignmentButtonListener m_buttonListener;
//        private final AssignmentListButtonListener m_buttonListListener;
//        private final boolean m_singleAssigmentButton;
//
//        static
//        {
//            String iconName = "MapZoomInTool.icon";
//            try
//            {
//                Icon icon = Utils.createImageIcon("icons/arrow_left.gif", iconName);
//                icons.put(0, icon);
//                icon = Utils.createImageIcon("icons/arrow_right.gif", iconName);
//                icons.put(1, icon);
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
//        }
//
//        public AssignmentButton(IAssignmentIf anAssignment)
//        {
//            super();
//            m_singleAssigmentButton = true;
//            m_buttonListener = new AssignmentButtonListener();
//            m_buttonListListener = null;
//            addMouseListener(m_buttonListener);
//            setAssignment(anAssignment);
//            setup();
//        }
//
//        public AssignmentButton(java.util.List<IAssignmentIf> theAssignments)
//        {
//            super();
//            m_singleAssigmentButton = false;
//            m_buttonListener = null;
//            m_buttonListListener = new AssignmentListButtonListener();
//            addMouseListener(m_buttonListListener);
//            setAssignments(theAssignments);
//            setup();
//        }
//
//        private void setAssignmentIcon(IAssignmentIf anAssignment)
//        {
//            if (anAssignment != null)
//            {
//                setIcon(icons.get(anAssignment.getTypenr()));
//            } else
//            {
//                setIcon(null);
//            }
//        }
//
//        public void setAssignment(IAssignmentIf anAssignment)
//        {
//            // todo test on m_singleAssigmentButton
//            setText("" + anAssignment.getNumber());
//            setAssignmentIcon(anAssignment);
//            m_buttonListener.setAssignment(anAssignment);
//            setEnabled(true);
//            setVisible(true);
//        }
//
//        public void setAssignments(java.util.List<IAssignmentIf> theAssignments)
//        {
//            // todo test on m_singleAssigmentButton
//            if (theAssignments.size() == 0)
//            {
//                setText("");
//                setIcon(null);
//            } else if (theAssignments.size() == 1)
//            {
//                setText("" + theAssignments.get(0).getNumber());
//                setAssignmentIcon(theAssignments.get(0));
//            } else
//            {
//                setText("" + theAssignments.get(0).getNumber() + "(" + theAssignments.size() + ")");
//                setAssignmentIcon(theAssignments.get(0));
//            }
//            m_buttonListListener.setAssignmentList(theAssignments);
//            setEnabled(true);
//            setVisible(true);
//        }
//
//        private void setup()
//        {
//            setPreferredSize(buttonDim);
//            setMinimumSize(buttonDim);
//            setMaximumSize(buttonDim);
//            setEnabled(true);
//            setRolloverEnabled(true);
//        }
//    }
//
//    public static class AssignmentButtonListener extends MouseAdapter
//    {
//        IAssignmentIf m_assignment;
//
//        public void setAssignment(IAssignmentIf anAssignment)
//        {
//            m_assignment = anAssignment;
//        }
//
//        @Override
//        public void mouseClicked(MouseEvent e)
//        {
//            System.out.println("Assignment info click" + m_assignment.getNumber());
//        }
//
//        @Override
//        public void mouseEntered(MouseEvent e)
//        {
//            System.out.println("Assignment info enter" + m_assignment.getNumber());
//        }
//
//        @Override
//        public void mouseExited(MouseEvent e)
//        {
//            System.out.println("Assignment info exit" + m_assignment.getNumber());
//        }
//    }

//    public static class AssignmentListButtonListener extends MouseAdapter
//    {
//        java.util.List<IAssignmentIf> m_assignmentList;
//
//        public void setAssignmentList(java.util.List<IAssignmentIf> aList)
//        {
//            m_assignmentList = aList;
//        }
//
//        @Override
//        public void mouseClicked(MouseEvent e)
//        {
//            System.out.println("Assignment info " + m_assignmentList.size());
//        }
//
//        @Override
//        public void mouseEntered(MouseEvent e)
//        {
//            System.out.println("Assignment info enter" + m_assignmentList.size());
//        }
//
//        @Override
//        public void mouseExited(MouseEvent e)
//        {
//            System.out.println("Assignment info exit" + m_assignmentList.size());
//        }
//    }
//
//
//    public static class InfoButton extends JButton
//    {
//        public InfoButton(String anInfo)
//        {
//            super();
//            setInfo(anInfo);
//        }
//
//        public void setInfo(String anInfo)
//        {
//            if (anInfo.length() > 0)
//            {
//                setText("!");
//            } else
//            {
//                setText("");
//            }
//        }
//
//        private void setup()
//        {
//            setPreferredSize(buttonDim);
//            setMinimumSize(buttonDim);
//            setMaximumSize(buttonDim);
//            setEnabled(true);
//        }
//    }
//
//    public static class UnitJTable extends JTable
//    {
//        public TableCellRenderer getCellRenderer(int row, int column)
//        {
//            TableColumn tableColumn = getColumnModel().getColumn(column);
//            TableCellRenderer renderer = tableColumn.getCellRenderer();
//            if (renderer == null)
//            {
//                Class c = getColumnClass(column);
//                if (c.equals(Object.class))
//                {
//                    Object o = getValueAt(row, column);
//                    if (o != null)
//                    {
//                        c = getValueAt(row, column).getClass();
//                    }
//                }
//                renderer = getDefaultRenderer(c);
//            }
//            return renderer;
//        }
//
//        public TableCellEditor getCellEditor(int row, int column)
//        {
//            TableColumn tableColumn = getColumnModel().getColumn(column);
//            TableCellEditor editor = tableColumn.getCellEditor();
//            if (editor == null)
//            {
//                Class c = getColumnClass(column);
//                if (c.equals(Object.class))
//                {
//                    Object o = getValueAt(row, column);
//                    if (o != null)
//                    {
//                        c = getValueAt(row, column).getClass();
//                    }
//                }
//                editor = getDefaultEditor(c);
//            }
//            return editor;
//        }
//
//    }
//
//    class LocalMouseAdapter extends MouseAdapter // todo fjern etter test
//    {
//        final int buttonnr;
//
//        public LocalMouseAdapter(int anr)
//        {
//            buttonnr = anr;
//        }
//
//        @Override
//        public void mouseEntered(MouseEvent e)
//        {
//            System.out.println("Entered " + buttonnr);
//        }
//
//        @Override
//        public void mouseExited(MouseEvent e)
//        {
//            System.out.println("Exited " + buttonnr);
//        }
//
//        @Override
//        public void mouseClicked(MouseEvent e)
//        {
//            System.out.println("Clicked " + buttonnr);
//        }
//
//    }

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
        WorkspacePanel = new JPanel();
        WorkspacePanel.setLayout(new BorderLayout(0, 0));
        m_splitter1 = new JSplitPane();
        m_splitter1.setContinuousLayout(true);
        m_splitter1.setRequestFocusEnabled(true);
        WorkspacePanel.add(m_splitter1, BorderLayout.CENTER);
        m_splitter2 = new JSplitPane();
        m_splitter2.setContinuousLayout(true);
        m_splitter1.setRightComponent(m_splitter2);
        m_unitPanel = new JPanel();
        m_unitPanel.setLayout(new BorderLayout(0, 0));
        m_unitPanel.setFocusCycleRoot(true);
        m_splitter2.setRightComponent(m_unitPanel);
        m_scrollPane1 = new JScrollPane();
        m_scrollPane1.setOpaque(false);
        m_unitPanel.add(m_scrollPane1, BorderLayout.CENTER);
        m_unitTable = new JTable();
        m_scrollPane1.setViewportView(m_unitTable);
        m_assignmentPanel = new JPanel();
        m_assignmentPanel.setLayout(new BorderLayout(0, 0));
        m_splitter2.setLeftComponent(m_assignmentPanel);
        m_AssignmentSubPaneLeft = new JScrollPane();
        m_assignmentPanel.add(m_AssignmentSubPaneLeft, BorderLayout.CENTER);
        m_AssignmentSubPaneRight = new JScrollPane();
        m_AssignmentSubPaneRight.setRequestFocusEnabled(false);
        m_assignmentPanel.add(m_AssignmentSubPaneRight, BorderLayout.EAST);
        m_splitter3 = new JSplitPane();
        m_splitter3.setContinuousLayout(true);
        m_splitter3.setOrientation(0);
        m_splitter1.setLeftComponent(m_splitter3);
        m_infoPanel = new JPanel();
        m_infoPanel.setLayout(new CardLayout(0, 0));
        m_splitter3.setRightComponent(m_infoPanel);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return WorkspacePanel;
    }
}
