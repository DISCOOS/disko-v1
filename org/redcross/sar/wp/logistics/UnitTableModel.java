package org.redcross.sar.wp.logistics;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.*;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
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

    public UnitTableModel(JTable aTable, IMsoEventManagerIf anEventManager, IUnitListIf aUnitList)
    {
        m_table = aTable;
        m_eventManager = anEventManager;
        m_eventManager.addClientUpdateListener(this);
        m_unitList = aUnitList;
        m_actualUnitCount = 0;
        setRowSorter();
        JTableHeader tableHeader = m_table.getTableHeader();

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
        this.fireTableDataChanged();
    }

    private final EnumSet<IMsoManagerIf.MsoClassCode> myInterests = EnumSet.of(IMsoManagerIf.MsoClassCode.CLASSCODE_UNIT, IMsoManagerIf.MsoClassCode.CLASSCODE_ASSIGNMENT);

    public boolean hasInterestIn(IMsoObjectIf aMsoObject)
    {
        return myInterests.contains(aMsoObject.getMsoClassCode());
    }

    private final static AbstractMsoObject.StatusSetSelector<IUnitIf, IUnitIf.UnitStatus> m_activeUnitSelector =
            new AbstractMsoObject.StatusSetSelector<IUnitIf, IUnitIf.UnitStatus>(EnumSet.range(IUnitIf.UnitStatus.READY, IUnitIf.UnitStatus.WORKING));

    private final static Comparator<IUnitIf> m_unitNumberComparator = new Comparator<IUnitIf>()
    {
        public int compare(IUnitIf u1, IUnitIf u2)
        {
            return u1.getNumber() - u2.getNumber();
        }
    };

    private Icon[] newIconRow(IUnitIf aUnit)
    {
        Icon[] retVal = new Icon[6];
        retVal[0] = createUnitIcon(aUnit);
        retVal[1] = createAssignmentIcon(aUnit.getAllocatedAssignments());
        retVal[2] = createAssignmentIcon(aUnit.getAssignedAssignments());
        retVal[3] = createAssignmentIcon(aUnit.getExecutingAssigment());
        retVal[4] = createAssignmentIcon(aUnit.getFinishedAssigment());
        retVal[5] = createInfoIcon(aUnit.getRemarks());  // todo getInfo
        return retVal;
    }

    private void assignIconRow(int i, IUnitIf aUnit)
    {
        Icon[] icons = m_iconRows.get(i);
        ((LogisticsIcon.UnitIcon) icons[0]).setUnit(aUnit);
        ((LogisticsIcon.AssignmentIcon) icons[1]).setAssignments(aUnit.getAllocatedAssignments());
        ((LogisticsIcon.AssignmentIcon) icons[2]).setAssignments(aUnit.getAssignedAssignments());
        ((LogisticsIcon.AssignmentIcon) icons[3]).setAssignments(aUnit.getExecutingAssigment());
        ((LogisticsIcon.AssignmentIcon) icons[4]).setAssignments(aUnit.getFinishedAssigment());
        ((LogisticsIcon.InfoIcon) icons[5]).setInfo(aUnit.getRemarks());  // todo getInfo
    }

    void buildTable()
    {
        m_actualUnitCount = 0;
        for (IUnitIf unit : m_unitList.selectItems(m_activeUnitSelector, m_unitNumberComparator))
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
                return "Søkt";
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
        Icon[] buttons = m_iconRows.get(rowIndex);
        return buttons[columnIndex];
    }

    private LogisticsIcon.UnitIcon createUnitIcon(IUnitIf aUnit)
    {
        LogisticsIcon.UnitIcon retVal = new LogisticsIcon.UnitIcon(aUnit, false);
        return retVal;
    }

    private LogisticsIcon.AssignmentIcon createAssignmentIcon(List<IAssignmentIf> assignments)
    {
        return new LogisticsIcon.AssignmentIcon(assignments, false);
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
            Object value = getValueAt(m_selectedRow,  m_selectedColunm);
            if (value instanceof LogisticsIcon)
            {
                System.out.println(aRow+ " " + aColumn + " " + ((LogisticsIcon)value).getIconWidth());
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
        int[] m_sortKeys = new int[]{0,1,2,2,2,0}; // default initial values

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


    public class HeaderPopupHandler extends AbstractPopupHandler
    {
        private final UnitTableModel m_model;
        private final TableColumnModel m_columnModel;
        private final UnitTableRowSorter m_rowSorter;
        private final JPopupMenu[] m_menus = new JPopupMenu[6];
        private final Vector<JRadioButtonMenuItem>[] m_buttons = new Vector[6];
        private final ButtonGroup[] m_buttonGroups = new ButtonGroup[6];

        public HeaderPopupHandler(UnitTableModel aModel, JTable aTable)
        {
            m_model = aModel;
            m_columnModel = aTable.getColumnModel();
            m_rowSorter = m_model.getRowSorter();

            int column;

            column = 0;
            setupColumn(column);

            column++; // 1
            setupColumn(column);
            addButton(buttonWithAction("Antall", column, 1), column);
            addButton(buttonWithAction("Prioritet", column, 2), column);
            m_buttons[column].get(0).setSelected(true);

            column++; // 2
            setupColumn(column);
            addButton(buttonWithAction("Prioritet", column, 2), column);
            addButton(buttonWithAction("Tildelt tid", column, 3), column);
            m_buttons[column].get(0).setSelected(true);

            column++; // 3
            setupColumn(column);
            addButton(buttonWithAction("Prioritet", column, 2), column);
            addButton(buttonWithAction("Start tid", column, 4), column);
            addButton(buttonWithAction("Est. ferdig tid", column, 5), column);
            m_buttons[column].get(0).setSelected(true);

            column++; // 4
            setupColumn(column);
            addButton(buttonWithAction("Antall", column, 1), column);
            addButton(buttonWithAction("Prioritet", column, 2), column);
            m_buttons[column].get(0).setSelected(true);

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
            AbstractAction action = new AbstractAction(aText)
            {
                public void actionPerformed(ActionEvent e)
                {
                    m_model.getRowSorter().setSortKey(aColumn, aKeyIndex);
                }
            };
            return new JRadioButtonMenuItem(action);
        }


        public JPopupMenu getMenu(MouseEvent e)
        {
            Point p = e.getPoint();
            int index = m_columnModel.getColumnIndexAtX(p.x);
            int realIndex = m_columnModel.getColumn(index).getModelIndex();
            if (m_buttons[realIndex].size() > 0)
            {
                return m_menus[realIndex];
            }
            return null;
        }
    }

}

