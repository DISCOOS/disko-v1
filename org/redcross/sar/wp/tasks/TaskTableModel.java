package org.redcross.sar.wp.tasks;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import org.redcross.sar.gui.AbstractPopupHandler;
import org.redcross.sar.gui.PopupListener;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.AbstractUnit;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.ITaskIf;
import org.redcross.sar.mso.data.ITaskListIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.TaskListImpl;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.Update;
import org.redcross.sar.util.mso.Selector;
import org.redcross.sar.wp.logistics.UnitTableModel;

public class TaskTableModel extends AbstractTableModel implements IMsoUpdateListenerIf
{
	private static final long serialVersionUID = 1L;
	
	protected List<ITaskIf> m_tasks;
	protected IDiskoWpTasks m_wpTasks;
	
	protected JTable m_table;
	protected TaskTableRowSorter m_rowSorter;
	
//	protected EnumSet<TaskType> m_typeFilter = 
//	protected EnumSet<TaskPriority> m_priorityFilter = 
	/**
	 * Filters and comparators
	 */
	private static final Selector<ITaskIf> m_taskSelector = new Selector<ITaskIf>()
	{
		public boolean select(ITaskIf anObject)
		{
			// TODO Check that all filter enum sets contains task properties
			return true;
		}
	};
	
	private final static Comparator<ITaskIf> m_nrComparator = new Comparator<ITaskIf>()
	{
		public int compare(ITaskIf arg0, ITaskIf arg1)
		{
			// TODO Update with comparators
			return 0;
		}
	};
	
	private static Comparator<ITaskIf> m_taskComparator = m_nrComparator;
	
	public TaskTableModel(IDiskoWpTasks wp, JTable table)
	{
		m_wpTasks = wp;
		m_table = table;
		m_tasks = wp.getMsoManager().getCmdPost().getTaskList().selectItems(m_taskSelector, m_taskComparator);
		
		wp.getMmsoEventManager().addClientUpdateListener(this);
		
		m_rowSorter = new TaskTableRowSorter(this);
		m_table.setRowSorter(m_rowSorter);
		
		PopupListener listener = new PopupListener(new TaskTableModel.HeaderPopupHandler(this, m_table));
        JTableHeader tableHeader = m_table.getTableHeader();
        tableHeader.addMouseListener(listener);
	}
	
	public TaskTableRowSorter getRowSorter()
	{
		return m_rowSorter;
	}
	
	public int getColumnCount()
	{
		return 6;
	}

	public int getRowCount()
	{
		return m_tasks.size();
	}
	
    @Override
    public String getColumnName(int column)
    {
    	return m_wpTasks.getText("TableHeader" + column + ".text");
    }
    
    protected void buildTable()
    {
    	m_tasks = m_wpTasks.getMsoManager().getCmdPost().getTaskList().selectItems(m_taskSelector, null);
    }


	public Object getValueAt(int row, int column)
	{
		ITaskIf task = m_tasks.get(row);
		
		switch(column)
		{
		case 0:
			return "1";
		case 1:
			return "2";
		case 2:
			return task.toString();
		case 3:
			return "4";
		case 4:
			return "5";
		case 5:
			return "6";
		}
		
		return null;
	}
	
	public void handleMsoUpdateEvent(Update e)
	{
		// Rebuild list
		buildTable();
		fireTableDataChanged();
	}

	private final EnumSet<IMsoManagerIf.MsoClassCode> myInterests = EnumSet.of(
    		IMsoManagerIf.MsoClassCode.CLASSCODE_TASK);
	public boolean hasInterestIn(IMsoObjectIf msoObject)
	{
		return myInterests.contains(msoObject.getMsoClassCode());
	}
	
	/**
	 * Sorts and filters table
	 * 
	 * @author thomasl
	 */
	public class TaskTableRowSorter extends TableRowSorter<TaskTableModel>
	{
		public TaskTableRowSorter(TaskTableModel model)
		{
			super(model);
		}
		
		@Override
		 public Comparator<?> getComparator(int column)
		 {
			switch (column)
			{
			case 0:
				return m_nrComparator;
			case 1:
//				return m_priComparator;
			case 2:
			case 3:
			case 4:
			case 5:
			default:
				return null;
			}
		 }
	}
	
	/**
	 * Pop-up menu for table header
	 * 
	 * @author thomasl
	 */
	public class HeaderPopupHandler extends AbstractPopupHandler implements ActionListener
    {
        private final TableColumnModel m_columnModel;
        private final JPopupMenu[] m_menus = new JPopupMenu[6];
        
        private final static String ALL_TASKS_COMMAND = "ALL_TASKS";
        private final static String OWN_TASKS_COMMAND = "OWN_TASKS";
        private final static String LOW_PRIORITY_COMMAND = "LOW_PRIORITY";
        private final static String NORMAL_PRIORITY_COMMAND = "NORMAL_PRIORITY";
        private final static String HIGH_PRIORITY_COMMAND = "HIGH_PRIORITY";

        public HeaderPopupHandler(TaskTableModel model, JTable table)
        {
        	m_columnModel = table.getColumnModel();
        	m_rowSorter = getRowSorter();

        	// Priority pop-up menu
        	addMenuCheckBox(1, "Høy", HIGH_PRIORITY_COMMAND, true);
        	addMenuCheckBox(1, "Normal", NORMAL_PRIORITY_COMMAND, true);
        	addMenuCheckBox(1, "Lav", LOW_PRIORITY_COMMAND, true);

        	// Task pop-up menu
        	addMenuCheckBox(2, m_wpTasks.getText("AllRolesCheckBox.text"), ALL_TASKS_COMMAND, true);
        	addMenuCheckBox(2, m_wpTasks.getText("OwnTasksCheckBox.text"), OWN_TASKS_COMMAND, false);
        }
        
        private void addMenuCheckBox(int menu, String label, String command, boolean selected)
        {
        	if(m_menus[menu] == null)
        	{
        		 m_menus[menu] = new JPopupMenu();
        	}
        	
        	JCheckBoxMenuItem checkBox = new JCheckBoxMenuItem();
        	checkBox.setText(label);
        	checkBox.setActionCommand(command);
        	checkBox.addActionListener(this);
        	checkBox.setSelected(selected);
        	
        	m_menus[menu].add(checkBox);
        }

        public void actionPerformed(ActionEvent e)
        {
        	String command = e.getActionCommand();
        	
        	if(command.equals(ALL_TASKS_COMMAND))
        	{
        		// TODO Set filter
        	}
        	else if(command.equals(OWN_TASKS_COMMAND))
        	{
        		// TODO Set filter
        	}
        	
        	buildTable();
        	fireTableDataChanged();
        }

		@Override
		protected JPopupMenu getMenu(MouseEvent e)
		{
			Point p = e.getPoint();
            int index = m_columnModel.getColumnIndexAtX(p.x);
            int realIndex = m_columnModel.getColumn(index).getModelIndex();
            return m_menus[realIndex];
		}
    }
}
