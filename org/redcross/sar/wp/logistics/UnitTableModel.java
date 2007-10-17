package org.redcross.sar.wp.logistics;

import org.redcross.sar.gui.AbstractPopupHandler;
import org.redcross.sar.gui.PopupListener;
import org.redcross.sar.gui.renderers.IconRenderer;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IUnitListIf;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent;
import org.redcross.sar.util.AssignmentTransferUtilities;
import org.redcross.sar.util.Internationalization;
import org.redcross.sar.util.mso.Selector;
import org.redcross.sar.wp.IDiskoWpModule;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.*;
import java.util.List;
/**
 * Created by IntelliJ IDEA.
 * User: vinjar
 * Date: 12.apr.2007
 * To change this template use File | Settings | File Templates.
 */

/**
 * Table model for unit table in logistics WP
 */
public class UnitTableModel extends AbstractTableModel implements IMsoUpdateListenerIf
{
    private final JTable m_table;
    private IMsoEventManagerIf m_eventManager;
    private IUnitListIf m_unitList;
    private ArrayList<Icon[]> m_iconRows = new ArrayList<Icon[]>();
    private int m_actualUnitCount;
    private UnitTableRowSorter m_rowSorter;
    private int m_selectedRow = -1;
    private int m_selectedColunm = -1;
    private EnumSet<IUnitIf.UnitType> m_unitTypeSelection;
    IDiskoWpLogistics m_wpModule;
    IconRenderer.LogisticsIconActionHandler m_actionHandler;

    /**
     * Creator
     *
     * @param aTable          The displayed table.
     * @param aWp             The related wor process.
     * @param aUnitList       Reference to the list of units.
     * @param anActionHandler The handler of icon actions.
     */
    public UnitTableModel(JTable aTable, IDiskoWpLogistics aWp, IUnitListIf aUnitList, IconRenderer.LogisticsIconActionHandler anActionHandler)
    {
        m_table = aTable;
        m_wpModule = aWp;
        m_eventManager = aWp.getMmsoEventManager();
        m_eventManager.addClientUpdateListener(this);
        m_unitList = aUnitList;
        m_actionHandler = anActionHandler;
        m_actualUnitCount = 0;
        setRowSorter();
        m_unitTypeSelection = EnumSet.allOf(IUnitIf.UnitType.class);

        PopupListener listener = new PopupListener(new UnitTableModel.HeaderPopupHandler(this, m_table));
        JTableHeader tableHeader = m_table.getTableHeader();
        tableHeader.addMouseListener(listener);

    }

    private void setRowSorter()
    {
        m_rowSorter = new UnitTableRowSorter(this);
        m_table.setRowSorter(m_rowSorter);
    }

    public UnitTableRowSorter getRowSorter()
    {
        return m_rowSorter;
    }

    public void handleMsoUpdateEvent(MsoEvent.Update e)
    {
        buildTable();
        fireTableDataChanged();
    }

    public void scrollToTableCellPosition(int aRowNumber)
    {
        Rectangle rowRect = m_table.getCellRect(m_table.convertRowIndexToView(aRowNumber), 0, true);
        Rectangle visibleRect = m_table.getVisibleRect();
        if (!visibleRect.contains(rowRect))
        {
            int visibleHeight = visibleRect.height;

            if (rowRect.y > visibleHeight / 2)
            {
                rowRect.y = rowRect.y - visibleHeight / 2;
            } else
            {
                rowRect.y = 0;
            }
            rowRect.height = visibleRect.height;
            m_table.scrollRectToVisible(rowRect);
        }
    }

    private final EnumSet<IMsoManagerIf.MsoClassCode> myInterests = EnumSet.of(IMsoManagerIf.MsoClassCode.CLASSCODE_UNIT, IMsoManagerIf.MsoClassCode.CLASSCODE_ASSIGNMENT);

    public boolean hasInterestIn(IMsoObjectIf aMsoObject)
    {
        return myInterests.contains(aMsoObject.getMsoClassCode());
    }

    private final Selector<IUnitIf> m_unitSelector = new Selector<IUnitIf>()
    {
        public boolean select(IUnitIf aUnit)
        {
            return (EnumSet.range(IUnitIf.UnitStatus.READY, IUnitIf.UnitStatus.PENDING).contains(aUnit.getStatus()) &&
                    m_unitTypeSelection.contains(aUnit.getType()));
        }
    };

    private Icon[] newIconRow(IUnitIf aUnit)
    {
        Icon[] retVal = new Icon[6];
        retVal[0] = createUnitIcon(aUnit);
        retVal[1] = createAssignmentIcon(aUnit, 0);
        retVal[2] = createAssignmentIcon(aUnit, 1);
        retVal[3] = createAssignmentIcon(aUnit, 2);
        retVal[4] = createAssignmentIcon(aUnit, 3);
        retVal[5] = createInfoIcon(aUnit.getRemarks());  // todo getInfo
        return retVal;
    }

    private void assignIconRow(int i, IUnitIf aUnit)
    {
        Icon[] icons = m_iconRows.get(i);
        ((IconRenderer.UnitIcon) icons[0]).setUnit(aUnit);
        ((IconRenderer.AssignmentIcon) icons[1]).setAssignments(aUnit, 0);
        ((IconRenderer.AssignmentIcon) icons[2]).setAssignments(aUnit, 1);
        ((IconRenderer.AssignmentIcon) icons[3]).setAssignments(aUnit, 2);
        ((IconRenderer.AssignmentIcon) icons[4]).setAssignments(aUnit, 3);
        ((IconRenderer.InfoIcon) icons[5]).setInfo(aUnit.getRemarks());  // todo getInfo
    }

    public static IAssignmentIf.AssignmentStatus getSelectedAssignmentStatus(int aSelectorIndex)
    {
        switch (aSelectorIndex)
        {
            case 0:
                return IAssignmentIf.AssignmentStatus.ALLOCATED;
            case 1:
                return IAssignmentIf.AssignmentStatus.ASSIGNED;
            case 2:
                return IAssignmentIf.AssignmentStatus.EXECUTING;
            default:
                return IAssignmentIf.AssignmentStatus.FINISHED;
        }
    }

    public static List<IAssignmentIf> getSelectedAssignments(IUnitIf aUnit, int aSelectorIndex)
    {
        switch (aSelectorIndex)
        {
            case 0:
                return aUnit.getAllocatedAssignments();
            case 1:
                return aUnit.getAssignedAssignments();
            case 2:
                return aUnit.getExecutingAssigments();
            default:
                return aUnit.getFinishedAssigments();
        }
    }

    public static String getSelectedAssignmentText(IDiskoWpModule aWpModule, int aSelectorIndex)
    {
        return aWpModule.getText(MessageFormat.format("UnitTable_hdr_{0}.text", aSelectorIndex + 1));
    }

    void buildTable()
    {
        m_actualUnitCount = 0;
        for (IUnitIf unit : m_unitList.selectItems(m_unitSelector, IUnitIf.UNIT_TYPE_AND_NUMBER_COMPARATOR))
        {
            m_actualUnitCount++;
            if (m_iconRows.size() < m_actualUnitCount)
            {
                m_iconRows.add(newIconRow(unit));
            } else
            {
                assignIconRow(m_actualUnitCount - 1, unit);
            }
        }
    }

    void reInitModel(IUnitListIf aUnitList)
    {
        m_unitList = aUnitList;
        buildTable();
        fireTableDataChanged();
    }

    @Override
    public String getColumnName(int column)
    {
        return m_wpModule.getText(MessageFormat.format("UnitTable_hdr_{0}.text", column));
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        if (columnIndex == 0)
        {
            return IconRenderer.UnitIcon.class;
        } else if (columnIndex == 5)
        {
            return IconRenderer.InfoIcon.class;
        } else
        {
            return IconRenderer.AssignmentIcon.class;
        }
    }

    public int getRowCount()
    {
        return m_actualUnitCount;
    }

    public int getColumnCount()
    {
        return 6;
    }


    public Object getValueAt(int rowIndex, int columnIndex)
    {
        if (rowIndex < 0 || rowIndex >= m_iconRows.size() || columnIndex < 0 || columnIndex > 5)
        {
            return null;
        }
        Icon[] buttons = m_iconRows.get(rowIndex);
        return buttons[columnIndex];
    }

    private IconRenderer.UnitIcon createUnitIcon(IUnitIf aUnit)
    {
        return new IconRenderer.UnitIcon(aUnit, false, m_actionHandler);
    }

    private IconRenderer.AssignmentIcon createAssignmentIcon(IUnitIf aUnit, int aSelectorIndex)
    {
        return new IconRenderer.AssignmentIcon(aUnit, aSelectorIndex, false, m_actionHandler);
    }

    private IconRenderer.InfoIcon createInfoIcon(String anInfo)
    {
        return new IconRenderer.InfoIcon(anInfo, false);
    }

    @Override
    public boolean isCellEditable(int row, int column)
    {
        return true;
    }

    public void setSelectedCell(int aRow, int aColumn)
    {
//        if (aRow != m_selectedRow || aColumn != m_selectedColunm)
        {
            if (aRow < 0 || aRow >= getRowCount() || aColumn < 0 || aColumn >= getColumnCount())
            {
                return;
            }
            try
            {
                m_selectedRow = m_table.convertRowIndexToModel(aRow);
                m_selectedColunm = m_table.convertColumnIndexToModel(aColumn);
                Object value = getValueAt(m_selectedRow, m_selectedColunm);
                if (value instanceof IconRenderer)
                {
                    ((IconRenderer) value).iconSelected();
                }
            }
            catch (IndexOutOfBoundsException e)
            {
            }
        }
    }

    public final static Comparator<IconRenderer.AssignmentIcon> ListLengthComparator = new Comparator<IconRenderer.AssignmentIcon>()
    {
        public int compare(IconRenderer.AssignmentIcon o1, IconRenderer.AssignmentIcon o2)
        {
            int l1 = o1.getAssignmentList() != null ? o1.getAssignmentList().size() : 0;
            int l2 = o2.getAssignmentList() != null ? o2.getAssignmentList().size() : 0;
            return l1 - l2;
        }
    };

    public final static Comparator<IconRenderer.AssignmentIcon> PriorityComparator = new Comparator<IconRenderer.AssignmentIcon>()
    {
        public int compare(IconRenderer.AssignmentIcon o1, IconRenderer.AssignmentIcon o2)
        {
            IAssignmentIf.AssignmentPriority p1 = getHighestPriority(o1.getAssignmentList());
            IAssignmentIf.AssignmentPriority p2 = getHighestPriority(o2.getAssignmentList());
            return p1.compareTo(p2);
        }

        private IAssignmentIf.AssignmentPriority getHighestPriority(java.util.List<IAssignmentIf> aList)
        {
            IAssignmentIf.AssignmentPriority retVal = IAssignmentIf.AssignmentPriority.NONE;
            if (aList != null)
            {
                for (IAssignmentIf asg : aList)
                {
                    if (asg.getPriority().ordinal() < retVal.ordinal())
                    {
                        retVal = asg.getPriority();
                    }
                }
            }
            return retVal;
        }
    };


    public EnumSet<IUnitIf.UnitType> getUnitTypeSelection()
    {
        return m_unitTypeSelection;
    }

    public void setTypeSelections(String aType, boolean aFlag)
    {
        IUnitIf.UnitType t = IUnitIf.UnitType.valueOf(aType);
        if (t != null)
        {
            setTypeSelections(t, aFlag);
        }
    }

    public void setTypeSelections(IUnitIf.UnitType aType, boolean aFlag)
    {
        if (aFlag)
        {
            m_unitTypeSelection.add(aType);
        } else
        {
            m_unitTypeSelection.remove(aType);
        }
        buildTable();
        fireTableDataChanged();
    }

    public void setTypeSelections(IUnitIf.UnitType[] theTypes, boolean aFlag)
    {
        for (IUnitIf.UnitType t : theTypes)
        {
            if (aFlag)
            {
                m_unitTypeSelection.add(t);
            } else
            {
                m_unitTypeSelection.remove(t);
            }
        }
        buildTable();
        fireTableDataChanged();
    }

    public IUnitIf getUnitAt(int aRow)
    {
        return ((IconRenderer.UnitIcon) m_iconRows.get(aRow)[0]).getUnit();
    }

    public boolean canAcceptAssignment(IAssignmentIf anAssignment, int aRow, int aColumn)
    {
        if (aColumn == 0 || aColumn == 5)
        {
            return false;
        }
        IUnitIf rowUnit = getUnitAt(aRow);
        IAssignmentIf.AssignmentStatus columnStatus = UnitTableModel.getSelectedAssignmentStatus(aColumn - 1);
        return AssignmentTransferUtilities.assignmentCanChangeToStatus(anAssignment, columnStatus, rowUnit);
    }

    public abstract static class TimeComparator implements Comparator<IconRenderer.AssignmentIcon>
    {
        public int compare(IconRenderer.AssignmentIcon o1, IconRenderer.AssignmentIcon o2)
        {
            Calendar c1 = getCompareTime(o1.getAssignmentList());
            Calendar c2 = getCompareTime(o2.getAssignmentList());
            if (c1 != null & c2 != null)
            {
                return c1.compareTo(c2);
            } else if (c1 == null && c1 == null)
            {
                return 0;
            } else if (c1 == null)
            {
                return -1;
            } else
            {
                return 1;
            }
        }

        protected abstract Calendar getCompareTime(java.util.List<IAssignmentIf> aList);
    }

    public final static TimeComparator AssignmentTimeComparator = new TimeComparator()
    {
        protected Calendar getCompareTime(java.util.List<IAssignmentIf> aList)
        {
            if (aList.size() > 0)
            {
                return aList.get(0).getTimeAssigned();
            }
            return null;
        }
    };

    public final static TimeComparator StartTimeComparator = new TimeComparator()
    {
        protected Calendar getCompareTime(java.util.List<IAssignmentIf> aList)
        {
            if (aList.size() > 0)
            {
                return aList.get(0).getTimeStarted();
            }
            return null;
        }
    };

    public final static TimeComparator EstimatedEndTimeComparator = new TimeComparator()
    {
        protected Calendar getCompareTime(java.util.List<IAssignmentIf> aList)
        {
            if (aList.size() > 0)
            {
                return aList.get(0).getTimeEstimatedFinished();
            }
            return null;
        }
    };

    public final static Comparator<IconRenderer.UnitIcon> UnitTypeAndNumberComparator = new Comparator<IconRenderer.UnitIcon>()
    {
        public int compare(IconRenderer.UnitIcon o1, IconRenderer.UnitIcon o2)
        {
            IUnitIf u1 = o1.getUnit();
            IUnitIf u2 = o2.getUnit();
            int i1 = u1.getUnitNumberPrefix();
            int i2 = u2.getUnitNumberPrefix();
            if (i1 == i2)
            {
                return u1.getNumber() - u2.getNumber();
            } else
            {
                return i1 - i2;
            }
        }
    };

    public final static Comparator<IconRenderer.UnitIcon> UnitSpeedComparator = new Comparator<IconRenderer.UnitIcon>()
    {
        public int compare(IconRenderer.UnitIcon o1, IconRenderer.UnitIcon o2)
        {
            return o1.getUnit().getSpeed() - o2.getUnit().getSpeed();
        }
    };

    public static class UnitTableRowSorter extends TableRowSorter<UnitTableModel>
    {
        int[] m_sortKeys = new int[]{0, 1, 2, 2, 2, 0}; // default initial values

        public UnitTableRowSorter(UnitTableModel aModel)
        {
            super(aModel);
        }

        public Comparator<?> getComparator(int column)
        {
            switch (column)
            {
                case 0:
                    switch (m_sortKeys[column])
                    {
                        case 2:
                            return UnitSpeedComparator;
                        default:
                            return UnitTypeAndNumberComparator;
                    }
                case 1:
                case 2:
                case 3:
                case 4:
                    switch (m_sortKeys[column])
                    {
                        case 1:
                            return ListLengthComparator;
                        case 3:
                            return AssignmentTimeComparator;
                        case 4:
                            return StartTimeComparator;
                        case 5:
                            return EstimatedEndTimeComparator;
                        default:
                            return PriorityComparator;
                    }
                default:
                    return null;
            }
        }

        @Override
        public boolean useToString(int column)
        {
            return column == 5;
        }

        public void setSortKey(int aColumn, int aKeyIndex)
        {
            m_sortKeys[aColumn] = aKeyIndex;
            List<? extends RowSorter.SortKey> keyList = getSortKeys();
            boolean changeColumn = true;
            boolean sortAscending = true;
            if (keyList.size() > 0)
            {
                RowSorter.SortKey key = keyList.get(0);
                changeColumn = key.getColumn() != aColumn;
                sortAscending = key.getSortOrder() == SortOrder.ASCENDING;
            }
            if (changeColumn)
            {
                toggleSortOrder(aColumn);
                if (!sortAscending)
                {
                    toggleSortOrder(aColumn);
                }
            }
            fireSortOrderChanged();
        }

        public int getFirstSortColumn()
        {
            List<? extends RowSorter.SortKey> keyList = getSortKeys();
            if (keyList.size() > 0)
            {
                RowSorter.SortKey key = keyList.get(0);
                return key.getColumn();
            }
            return 0;
        }
    }

    public class HeaderPopupHandler extends AbstractPopupHandler implements ActionListener
    {
        private final TableColumnModel m_columnModel;
        private final JPopupMenu[] m_menus = new JPopupMenu[6];
        private final Vector<JRadioButtonMenuItem>[] m_buttons = new Vector[6];
        private final ButtonGroup[] m_buttonGroups = new ButtonGroup[6];
        private final LinkedHashMap<String, JMenuItem> m_unitSelections = new LinkedHashMap<String, JMenuItem>();
        private JMenuItem m_selectAll;
        private JMenuItem m_deselectAll;
        private JSeparator m_menuSeparator;

        public HeaderPopupHandler(UnitTableModel aModel, JTable aTable)
        {
            m_columnModel = aTable.getColumnModel();
            m_rowSorter = getRowSorter();

            createUnitSelectionBoxes();
            m_menuSeparator = new JSeparator();

            int column;

            column = 0;
            setupColumn(column);
            addButton(buttonWithAction("UnitTable_menu_unitType.text", column, 1), column);
            addButton(buttonWithAction("UnitTable_menu_speed.text", column, 2), column);
            addButton(buttonWithAction("UnitTable_menu_pauseTime.text", column, 3), column);
            addButton(buttonWithAction("UnitTable_menu_workTime.text", column, 4), column);
            addButton(buttonWithAction("UnitTable_menu_idleTime.text", column, 5), column);
            m_buttons[column].get(0).setSelected(true);
            m_menus[column].add(new JSeparator());

            column++; // 1
            setupColumn(column);
            addButton(buttonWithAction("UnitTable_menu_qty.text", column, 1), column);
            addButton(buttonWithAction("UnitTable_menu_priority.text", column, 2), column);
            m_buttons[column].get(0).setSelected(true);
            m_menus[column].add(new JSeparator());

            column++; // 2
            setupColumn(column);
            addButton(buttonWithAction("UnitTable_menu_priority.text", column, 2), column);
            addButton(buttonWithAction("UnitTable_menu_assignedtime.text", column, 3), column);
            m_buttons[column].get(0).setSelected(true);
            m_menus[column].add(new JSeparator());

            column++; // 3
            setupColumn(column);
            addButton(buttonWithAction("UnitTable_menu_priority.text", column, 2), column);
            addButton(buttonWithAction("UnitTable_menu_starttime.text", column, 4), column);
            addButton(buttonWithAction("UnitTable_menu_endtime.text", column, 5), column);
            m_buttons[column].get(0).setSelected(true);
            m_menus[column].add(new JSeparator());

            column++; // 4
            setupColumn(column);
            addButton(buttonWithAction("UnitTable_menu_qty.text", column, 1), column);
            addButton(buttonWithAction("UnitTable_menu_priority.text", column, 2), column);
            m_buttons[column].get(0).setSelected(true);
            m_menus[column].add(new JSeparator());

            column++;  // 5
            setupColumn(column);
        }

        private void setupColumn(int column)
        {
            m_menus[column] = new JPopupMenu();
            m_buttons[column] = new Vector<JRadioButtonMenuItem>();
            m_buttonGroups[column] = new ButtonGroup();
        }

        private void addButton(JRadioButtonMenuItem aButton, int aColumn)
        {
            m_menus[aColumn].add(aButton);
            m_buttons[aColumn].add(aButton);
            m_buttonGroups[aColumn].add(aButton);
        }

        private JRadioButtonMenuItem buttonWithAction(String aText, final int aColumn, final int aKeyIndex)
        {
            String labelText = m_wpModule.getText(aText);
            AbstractAction action = new AbstractAction(labelText)
            {
                public void actionPerformed(ActionEvent e)
                {
                    getRowSorter().setSortKey(aColumn, aKeyIndex);
                }
            };
            return new JRadioButtonMenuItem(action);
        }


        private void createUnitSelectionBoxes()
        {
            for (IUnitIf.UnitType t : IUnitIf.UnitType.values())
            {
                if (t.equals(IUnitIf.UnitType.COMMAND_POST))
                {
                    continue;
                }
                String aText = Internationalization.translate(t);
                String aCommand = t.name();

                JMenuItem c = createMenuItem(true, aText, aCommand);
                c.setSelected(true);
                m_unitSelections.put(aCommand, c);
            }

            m_selectAll = createMenuItem(false, m_wpModule.getText("UnitTable_menu_selectAll.text"), "SelectAll");
            m_deselectAll = createMenuItem(false, m_wpModule.getText("UnitTable_menu_deselectAll.text"), "DeselectAll");
        }

        private JMenuItem createMenuItem(boolean makeCheckBox, String aText, String aCommand)
        {
            JMenuItem c = makeCheckBox ? new JCheckBoxMenuItem(aText) : new JMenuItem(aText);
            c.setActionCommand(aCommand);
            c.addActionListener(this);
            return c;
        }

        private void addSelectionBoxes(int aColumn)
        {
            for (JMenuItem c : m_unitSelections.values())
            {
                m_menus[aColumn].add(c);
            }
            m_menus[aColumn].add(m_menuSeparator);
            m_menus[aColumn].add(m_selectAll);
            m_menus[aColumn].add(m_deselectAll);
        }

        public JPopupMenu getMenu(MouseEvent e)
        {
            Point p = e.getPoint();
            int index = m_columnModel.getColumnIndexAtX(p.x);
            int realIndex = m_columnModel.getColumn(index).getModelIndex();
            if (m_buttons[realIndex].size() >= 0)
            {
                addSelectionBoxes(realIndex);
                return m_menus[realIndex];
            }
            return null;
        }


        public void actionPerformed(ActionEvent e)
        {
            String command = e.getActionCommand();
            JMenuItem c = (JMenuItem) e.getSource();
            if ("SelectAll".equals(command))
            {
                selectAll(true);
            } else if ("DeselectAll".equals(command))
            {
                selectAll(false);
            } else if (c != null)
            {
                setTypeSelections(command, c.isSelected());
            }
        }

        private void selectAll(boolean aFlag)
        {
            for (JMenuItem m : m_unitSelections.values())
            {
                m.setSelected(aFlag);
            }
            setTypeSelections(IUnitIf.UnitType.values(), aFlag);
        }
    }
}
