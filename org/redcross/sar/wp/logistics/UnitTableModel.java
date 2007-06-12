package org.redcross.sar.wp.logistics;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IUnitListIf;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent;
import org.redcross.sar.util.mso.Selector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
/**
 * Created by IntelliJ IDEA.
 * User: vinjar
 * Date: 12.apr.2007
 * To change this template use File | Settings | File Templates.
 */

/**
 *
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

    public UnitTableModel(JTable aTable, IMsoEventManagerIf anEventManager, IUnitListIf aUnitList)
    {
        m_table = aTable;
        m_eventManager = anEventManager;
        m_eventManager.addClientUpdateListener(this);
        m_unitList = aUnitList;
        m_actualUnitCount = 0;
        setRowSorter();
        JTableHeader tableHeader = m_table.getTableHeader();
        m_unitTypeSelection = EnumSet.allOf(IUnitIf.UnitType.class);

        PopupListener listener = new PopupListener(new HeaderPopupHandler(this, m_table));
        tableHeader.addMouseListener(listener);
        ListSelectionListener l = new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                if (e.getValueIsAdjusting())
                {
                    return;
                }

                setSelectedCell(m_table.getSelectionModel().getLeadSelectionIndex(),
                        m_table.getColumnModel().getSelectionModel().
                                getLeadSelectionIndex());

            }
        };
        m_table.getSelectionModel().addListSelectionListener(l);
        m_table.getColumnModel().getSelectionModel().addListSelectionListener(l);

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
        System.out.println(this.getClass() + " " + e + " " + e.getEventTypeMask());
        buildTable();
        fireTableDataChanged();
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
            return (EnumSet.range(IUnitIf.UnitStatus.READY, IUnitIf.UnitStatus.WORKING).contains(aUnit.getStatus()) &&
                    m_unitTypeSelection.contains(aUnit.getType()));
        }
    };

    private final static Comparator<IUnitIf> m_unitTypeAndNumberComparator = new Comparator<IUnitIf>()
    {
        public int compare(IUnitIf u1, IUnitIf u2)
        {
            int typeCompare = u1.getType().compareTo(u2.getType());
            if (typeCompare != 0)
            {
                return typeCompare;
            }
            return u1.getNumber() - u2.getNumber();
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
        ((LogisticsIcon.UnitIcon) icons[0]).setUnit(aUnit);
        ((LogisticsIcon.AssignmentIcon) icons[1]).setAssignments(aUnit, 0);
        ((LogisticsIcon.AssignmentIcon) icons[2]).setAssignments(aUnit, 1);
        ((LogisticsIcon.AssignmentIcon) icons[3]).setAssignments(aUnit, 2);
        ((LogisticsIcon.AssignmentIcon) icons[4]).setAssignments(aUnit, 3);
        ((LogisticsIcon.InfoIcon) icons[5]).setInfo(aUnit.getRemarks());  // todo getInfo
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
                return aUnit.getExecutingAssigment();
            default:
                return aUnit.getFinishedAssigment();
        }
    }

    public static String getSelectedAssignmentText(int aSelectorIndex)
    {
        return getColName(aSelectorIndex + 1);
    }

    void buildTable()
    {
        m_actualUnitCount = 0;
        for (IUnitIf unit : m_unitList.selectItems(m_unitSelector, m_unitTypeAndNumberComparator))
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

    @Override
    public String getColumnName(int column)
    {
        return getColName(column);
    }

    public static String getColName(int column)
    {
        switch (column)
        {
            case 0:
                return "Enhet";
            case 1:
                return "Neste";
            case 2:
                return "Tildelt";
            case 3:
                return "Startet";
            case 4:
                return "S�kt";
            default:
                return "Info";
        }

    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        if (columnIndex == 0)
        {
            return LogisticsIcon.UnitIcon.class;
        } else if (columnIndex == 5)
        {
            return LogisticsIcon.InfoIcon.class;
        } else
        {
            return LogisticsIcon.AssignmentIcon.class;
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

    private LogisticsIcon.UnitIcon createUnitIcon(IUnitIf aUnit)
    {
        LogisticsIcon.UnitIcon retVal = new LogisticsIcon.UnitIcon(aUnit, false);
        return retVal;
    }

    private LogisticsIcon.AssignmentIcon createAssignmentIcon(IUnitIf aUnit, int aSelectorIndex)
    {
        return new LogisticsIcon.AssignmentIcon(aUnit, aSelectorIndex, false);
    }

    private LogisticsIcon.InfoIcon createInfoIcon(String anInfo)
    {
        return new LogisticsIcon.InfoIcon(anInfo, false);
    }

    @Override
    public boolean isCellEditable(int row, int column)
    {
        return true;
    }

    public void setSelectedCell(int aRow, int aColumn)
    {
        if (aRow != m_selectedRow || aColumn != m_selectedColunm)
        {
            m_selectedRow = aRow;
            m_selectedColunm = aColumn;
            Object value = getValueAt(m_selectedRow, m_selectedColunm);
            if (value instanceof LogisticsIcon)
            {
                System.out.println(aRow + " " + aColumn + " " + ((LogisticsIcon) value).getIconWidth());
            }
        }
    }

    public final static Comparator<LogisticsIcon.AssignmentIcon> ListLengthComparator = new Comparator<LogisticsIcon.AssignmentIcon>()
    {
        public int compare(LogisticsIcon.AssignmentIcon o1, LogisticsIcon.AssignmentIcon o2)
        {
            int l1 = o1.getAssignmentList() != null ? o1.getAssignmentList().size() : 0;
            int l2 = o2.getAssignmentList() != null ? o2.getAssignmentList().size() : 0;
            return l1 - l2;
        }
    };

    public final static Comparator<LogisticsIcon.AssignmentIcon> PriorityComparator = new Comparator<LogisticsIcon.AssignmentIcon>()
    {
        public int compare(LogisticsIcon.AssignmentIcon o1, LogisticsIcon.AssignmentIcon o2)
        {
            IAssignmentIf.AssignmentPriority p1 = getHighestPriority(o1.getAssignmentList());
            IAssignmentIf.AssignmentPriority p2 = getHighestPriority(o2.getAssignmentList());
            return p1.compareTo(p2);
        }

        private IAssignmentIf.AssignmentPriority getHighestPriority(java.util.List<IAssignmentIf> aList)
        {
            IAssignmentIf.AssignmentPriority retVal = IAssignmentIf.AssignmentPriority.LOW;
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

    public abstract static class TimeComparator implements Comparator<LogisticsIcon.AssignmentIcon>
    {
        public int compare(LogisticsIcon.AssignmentIcon o1, LogisticsIcon.AssignmentIcon o2)
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
                return aList.get(0).getTimeStarted();
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
                return aList.get(0).getTimeStarted();
            }
            return null;
        }
    };

    public final static Comparator<LogisticsIcon.UnitIcon> UnitNumberComparator = new Comparator<LogisticsIcon.UnitIcon>()
    {
        public int compare(LogisticsIcon.UnitIcon o1, LogisticsIcon.UnitIcon o2)
        {
            int i1 = o1.getUnit().getNumber();
            int i2 = o2.getUnit().getNumber();
            return i1 - i2;
        }
    };

    public static class UnitTableRowSorter extends TableRowSorter<UnitTableModel>
    {
        UnitTableModel m_model;
        int[] m_sortKeys = new int[]{0, 1, 2, 2, 2, 0}; // default initial values

        public UnitTableRowSorter(UnitTableModel aModel)
        {
            super(aModel);
            m_model = aModel;
        }

        public Comparator<?> getComparator(int column)
        {
            switch (column)
            {
                case 0:
                    return UnitNumberComparator;
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
        private final UnitTableModel m_model;
        private final TableColumnModel m_columnModel;
        //        private final UnitTableRowSorter m_rowSorter;
        private final JPopupMenu[] m_menus = new JPopupMenu[6];
        private final Vector<JRadioButtonMenuItem>[] m_buttons = new Vector[6];
        private final ButtonGroup[] m_buttonGroups = new ButtonGroup[6];
        private final LinkedHashMap<String, JMenuItem> m_unitSelections = new LinkedHashMap<String, JMenuItem>();
        private JMenuItem m_selectAll;
        private JMenuItem m_deselectAll;
        private JSeparator m_menuSeparator;

        public HeaderPopupHandler(UnitTableModel aModel, JTable aTable)
        {
            m_model = aModel;
            m_columnModel = aTable.getColumnModel();
            m_rowSorter = m_model.getRowSorter();

            createUnitSelectionBoxes();
            m_menuSeparator = new JSeparator();

            int column;

            column = 0;
            setupColumn(column);

            column++; // 1
            setupColumn(column);
            addButton(buttonWithAction("Antall", column, 1), column);
            addButton(buttonWithAction("Prioritet", column, 2), column);
            m_buttons[column].get(0).setSelected(true);
            m_menus[column].add(new JSeparator());

            column++; // 2
            setupColumn(column);
            addButton(buttonWithAction("Prioritet", column, 2), column);
            addButton(buttonWithAction("Tildelt tid", column, 3), column);
            m_buttons[column].get(0).setSelected(true);
            m_menus[column].add(new JSeparator());

            column++; // 3
            setupColumn(column);
            addButton(buttonWithAction("Prioritet", column, 2), column);
            addButton(buttonWithAction("Start tid", column, 4), column);
            addButton(buttonWithAction("Est. ferdig tid", column, 5), column);
            m_buttons[column].get(0).setSelected(true);
            m_menus[column].add(new JSeparator());

            column++; // 4
            setupColumn(column);
            addButton(buttonWithAction("Antall", column, 1), column);
            addButton(buttonWithAction("Prioritet", column, 2), column);
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
            //aButton.setFont(FontFactory.popupFont());
            m_menus[aColumn].add(aButton);
            m_buttons[aColumn].add(aButton);
            m_buttonGroups[aColumn].add(aButton);
        }

        private JRadioButtonMenuItem buttonWithAction(String aText, final int aColumn, final int aKeyIndex)
        {
            AbstractAction action = new AbstractAction(aText)
            {
                public void actionPerformed(ActionEvent e)
                {
                    m_model.getRowSorter().setSortKey(aColumn, aKeyIndex);
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
                String aText = t.name();
                String aCommand = t.name();

                JMenuItem c = createMenuItem(true, aText, aCommand);
                c.setSelected(true);
                m_unitSelections.put(aCommand, c);
            }

            m_selectAll = createMenuItem(false, "Velg alle", "SelectAll");
            m_deselectAll = createMenuItem(false, "Fjern alle", "DeselectAll");
        }

        private JMenuItem createMenuItem(boolean makeCheckBox, String aText, String aCommand)
        {
            JMenuItem c = makeCheckBox ? new JCheckBoxMenuItem(aText) : new JMenuItem(aText);
            c.setActionCommand(aCommand);
            c.addActionListener(this);
            //c.setFont(FontFactory.popupFont());
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
                m_model.setTypeSelections(command, c.isSelected());
            }
        }

        private void selectAll(boolean aFlag)
        {
            for (JMenuItem m : m_unitSelections.values())
            {
                m.setSelected(aFlag);
            }
            m_model.setTypeSelections(IUnitIf.UnitType.values(), aFlag);
        }
    }

}

