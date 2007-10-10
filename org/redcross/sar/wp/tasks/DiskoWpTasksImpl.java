package org.redcross.sar.wp.tasks;

import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.event.ITickEventListenerIf;
import org.redcross.sar.event.TickEvent;
import org.redcross.sar.gui.*;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.ITaskIf;
import org.redcross.sar.mso.data.ITaskIf.TaskStatus;
import org.redcross.sar.mso.data.ITaskListIf;
import org.redcross.sar.wp.AbstractDiskoWpModule;
import org.redcross.sar.wp.IDiskoWpModule;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the tasks work process
 *
 * @author thomasl
 */
public class DiskoWpTasksImpl extends AbstractDiskoWpModule implements IDiskoWpTasks
{
    private JPanel m_contentsPanel;

    private JTable m_taskTable;

    private JButton m_newButton;
    private JButton m_changeButton;
    private JButton m_deleteButton;
    private JButton m_performedButton;

    private DeleteTaskDialog m_deleteTaskDialog;
    private List<DiskoDialog> m_dialogs;

    private ITaskIf m_currentTask;

    private static final int TASK_ALERT_TIME = 10000;
    private long m_timeCounter = 0;

    public DiskoWpTasksImpl(IDiskoRole role)
    {
        super(role);

        m_dialogs = new LinkedList<DiskoDialog>();

        setTaskAlertTimer();

        initialize();
    }

    private void initialize()
    {
        loadProperties("properties");
        assignWpBundle("org.redcross.sar.wp.tasks.tasks");

        m_contentsPanel = new JPanel(new BorderLayout());
        initTable();
        initButtons();
        layoutComponent(m_contentsPanel);

        m_deleteTaskDialog = new DeleteTaskDialog(this);
        m_dialogs.add(m_deleteTaskDialog);
        m_dialogs.add(this.getApplication().getUIFactory().getTaskDialog());
    }

    /**
     * Checking if any tasks have reached their alert time, and give the appropriate role a warning
     */
    private void setTaskAlertTimer()
    {
        this.addTickEventListener(new ITickEventListenerIf()
        {
            public long getInterval()
            {
                return TASK_ALERT_TIME;
            }

            public void setTimeCounter(long counter)
            {
                m_timeCounter = counter;
            }

            public long getTimeCounter()
            {
                return m_timeCounter;
            }

            @SuppressWarnings("unchecked")
            public void handleTick(TickEvent e)
            {
                ICmdPostIf cmdPost = getCmdPost();
                if (cmdPost == null)
                {
                    return;
                }
                ITaskListIf tasks = cmdPost.getTaskList();

                Calendar currentTime = Calendar.getInstance();
                IDiskoRole role = getDiskoRole();

                boolean alert = false;
                boolean isAlertTime = false;
                boolean isAlertStatus = false;
                boolean isAlertRole = false;
                for (ITaskIf task : tasks.getItems())
                {
                    Calendar alertTime = task.getAlert();
                    isAlertTime = alertTime != null && alertTime.before(currentTime);
                    isAlertStatus = task.getStatus() != TaskStatus.FINISHED && task.getStatus() != TaskStatus.DELETED;
                    isAlertRole = task.getResponsibleRole() == null ||
                            task.getResponsibleRole().equals("") ||
                            task.getResponsibleRole().equals(role.getTitle());
                    if (isAlertTime && isAlertStatus && isAlertRole)
                    {
                        alert = true;
                        break;
                    }
                }

                List<IDiskoWpModule> modules = role.getDiskoWpModules();
                int index = modules.indexOf(getDiskoWpTasks());
                MainMenuPanel mainMenu = getApplication().getUIFactory().getMainMenuPanel();
                AbstractButton button = mainMenu.getButton(role.getName(), index);
                if (alert)
                {
                    button.setBorder(BorderFactory.createLineBorder(Color.red));
                } else
                {
                    button.setBorder(null);
                }
            }
        });
    }

    /**
     * Used by the alert timer
     *
     * @return
     */
    protected IDiskoWpModule getDiskoWpTasks()
    {
        return this;
    }

    @Override
    public String getName()
    {
        return "Oppgaver";
    }

    private void initButtons()
    {
        m_newButton = DiskoButtonFactory.createSmallButton("", getText("NewButton.icon"));
        m_newButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent arg0)
            {
                newTask();
            }
        });
        layoutButton(m_newButton, true);

        m_changeButton = DiskoButtonFactory.createSmallButton("", getText("ChangeButton.icon"));
        m_changeButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                changeTask();
            }
        });
        layoutButton(m_changeButton, true);

        m_deleteButton = DiskoButtonFactory.createSmallButton("", this.getText("DeleteButton.icon"));
        m_deleteButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                deleteTask();
            }
        });
        layoutButton(m_deleteButton, true);

        m_performedButton = DiskoButtonFactory.createSmallButton("", this.getText("PerformedButton.icon"));
        m_performedButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                performedTask();
            }
        });
        layoutButton(m_performedButton);
    }

    private void initTable()
    {
        m_taskTable = new JTable();
        TaskTableModel model = new TaskTableModel(this, m_taskTable);
        m_taskTable.setModel(model);
        m_taskTable.getSelectionModel().addListSelectionListener(new TaskSelectionListener(this, m_taskTable));
        m_taskTable.setDefaultRenderer(Object.class, new TaskTableRenderer());
        m_taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        TableColumn column = m_taskTable.getColumnModel().getColumn(0);
        column.setMaxWidth(75);
        column.setPreferredWidth(75);
        column = m_taskTable.getColumnModel().getColumn(1);
        column.setPreferredWidth(150);
        column.setMaxWidth(150);
        column = m_taskTable.getColumnModel().getColumn(3);
        column.setPreferredWidth(150);
        column.setMaxWidth(150);
        column = m_taskTable.getColumnModel().getColumn(4);
        column.setPreferredWidth(100);
        column.setMaxWidth(100);
        column = m_taskTable.getColumnModel().getColumn(5);
        column.setPreferredWidth(100);
        column.setMaxWidth(100);

        m_taskTable.setRowMargin(2);
        m_taskTable.setRowHeight(22);

        JTableHeader tableHeader = m_taskTable.getTableHeader();
        tableHeader.setResizingAllowed(false);
        tableHeader.setReorderingAllowed(false);

        JScrollPane tableScrollPane = new JScrollPane(m_taskTable);
        tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        m_contentsPanel.add(tableScrollPane, BorderLayout.CENTER);
    }

    @Override
    public void activated()
    {
        super.activated();
        SubMenuPanel subMenu = this.getApplication().getUIFactory().getSubMenuPanel();
        subMenu.getFinishButton().setEnabled(false);
        subMenu.getCancelButton().setEnabled(false);
    }

    @Override
    public void deactivated()
    {
        super.deactivated();
        SubMenuPanel subMenu = this.getApplication().getUIFactory().getSubMenuPanel();
        subMenu.getFinishButton().setEnabled(true);
        subMenu.getCancelButton().setEnabled(true);
        
        TaskDialog taskDialog = getApplication().getUIFactory().getTaskDialog();
        taskDialog.setVisible(false);
    }
    
    @Override
    public boolean confirmDeactivate()
    {
    	if(getMsoModel().hasUncommitedChanges())
    	{
    		Object[] dialogOptions = {getText("Yes.text"), getText("No.text")};
    		int n = JOptionPane.showOptionDialog(this.getApplication().getFrame(), 
    				getText("UncommittedChanges.text"), 
    				getText("UncommittedChanges.header"), 
    				JOptionPane.YES_NO_OPTION, 
    				JOptionPane.QUESTION_MESSAGE, 
    				null, 
    				dialogOptions, 
    				dialogOptions[0]);
    		return (n == JOptionPane.YES_OPTION);
    	}
    	else
    	{
    		return true;
    	}
    }

    public void cancel()
    {
    }

    public void finish()
    {
    }

    /**
     * Adds a new task
     */
    private void newTask()
    {
        hideDialogs();
        TaskDialog taskDialog = this.getApplication().getUIFactory().getTaskDialog();
        taskDialog.setLocationRelativeTo(m_contentsPanel, DiskoDialog.POS_CENTER, false);
        taskDialog.setVisible(true);
        taskDialog.setTask(null);
    }

    /**
     * Change task selected in tasks table
     */
    private void changeTask()
    {
        if (m_currentTask != null)
        {
            hideDialogs();
            TaskDialog taskDialog = this.getApplication().getUIFactory().getTaskDialog();
            taskDialog.setLocationRelativeTo(m_contentsPanel, DiskoDialog.POS_CENTER, false);
            taskDialog.setVisible(true);
            taskDialog.setTask(m_currentTask);
        }
    }

    /**
     * Delete selected task in tasks table
     */
    private void deleteTask()
    {
        if (m_currentTask != null)
        {
            if (m_currentTask.getStatus() == TaskStatus.FINISHED)
            {
                ErrorDialog error = new ErrorDialog(this.getApplication().getFrame());
                error.showError(this.getText("CanNotDeleteTaskError.header"),
                        this.getText("CanNotDeleteTaskError.text"));
            } else
            {
                hideDialogs();
                m_deleteTaskDialog.setTask(m_currentTask);
                m_deleteTaskDialog.setLocationRelativeTo(m_contentsPanel, DiskoDialog.POS_CENTER, false);
                m_deleteTaskDialog.setVisible(true);
            }
        }
    }

    /**
     * Mark selected task as performed. Also possible to change task status in the change task
     * dialog
     */
    private void performedTask()
    {
        if (m_currentTask != null)
        {
            m_currentTask.setProgress(100);
            m_currentTask.setStatus(TaskStatus.FINISHED);
            this.getMsoModel().commit();
        }
        hideDialogs();
    }

    private void hideDialogs()
    {
        for (DiskoDialog dialog : m_dialogs)
        {
            dialog.setVisible(false);
        }
    }

    public void setCurrentTask(ITaskIf task)
    {
        m_currentTask = task;
    }

	public void reInitWP()
	{
		// TODO Auto-generated method stub
		
	}
}
