package org.redcross.sar.wp.messageLog;

import org.redcross.sar.app.Utils;
import org.redcross.sar.event.DialogEvent;
import org.redcross.sar.event.IDialogEventListener;
import org.redcross.sar.gui.DiskoButtonFactory;
import org.redcross.sar.gui.ErrorDialog;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.*;
import org.redcross.sar.mso.data.IMessageIf.MessageStatus;
import org.redcross.sar.mso.data.IMessageLineIf.MessageLineType;
import org.redcross.sar.mso.data.IPOIIf.POIType;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.Update;
import org.redcross.sar.util.AssignmentTransferUtilities;
import org.redcross.sar.util.except.IllegalOperationException;
import org.redcross.sar.util.mso.DTG;

import javax.swing.*;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Top edit panel
 *
 * @author thomasl
 */
public class MessageLogBottomPanel extends JPanel implements IMsoUpdateListenerIf, IDialogEventListener
{
	private static final long serialVersionUID = 1L;

	public final static int PANEL_HEIGHT = (DiskoButtonFactory.SMALL_BUTTON_SIZE.height) * 3 + 24;
	public final static int SMALL_PANEL_WIDTH = DiskoButtonFactory.SMALL_BUTTON_SIZE.width;

	private final static String EMPTY_PANEL_ID = "EMPTY_PANEL";
	private final static String TEXT_PANEL_ID = "TEXT_PANEL";
	private final static String POSITION_PANEL_ID = "POSITION_PANEL";
	private final static String FINDING_PANEL_ID = "FINDING_PANEL";
	private final static String ASSIGNED_PANEL_ID = "ASSIGNED_PANEL";
	private final static String STARTED_PANEL_ID = "STARTED_PANEL";
	private final static String COMPLETED_PANEL_ID = "COMPLETED_PANEL";
	private final static String LIST_PANEL_ID = "LIST_PANEL";

	private static IDiskoWpMessageLog m_wpMessageLog;

	// Current message Singleton
	private static boolean m_newMessage;
	private static IMessageIf m_currentMessage = null;
	private static boolean m_messageDirty = false;
	
	/**
	 * Get current message. Singleton
	 * @return The message
	 * @param Whether to create new or not if current message is {@code null}
	 */
	public static IMessageIf getCurrentMessage(boolean createNew)
	{
		if(m_currentMessage == null && createNew)
		{
			m_currentMessage = m_wpMessageLog.getMsoManager().createMessage();
			m_currentMessage.setCreated(Calendar.getInstance());
			m_currentMessage.setOccuredTime(Calendar.getInstance());
			m_newMessage = true;
			m_messageDirty = false;
		}
		return m_currentMessage;
	}

	/**
	 *
	 * @return Whether the current message is new or not, i.e. not stored in MSO
	 */
	public static boolean isNewMessage()
	{
		return m_newMessage;
	}

	private MessageLineType m_currentMessageLineType = null;

	static List<IEditMessageComponentIf> m_editComponents;

	private static ButtonGroup m_buttonGroup;

	private JPanel m_nrPanel;
	private static JLabel m_nrLabel;

	private JPanel m_dtgPanel;
	private static JLabel m_dtgLabel;
	private ChangeDTGDialog m_changeDTGDialog;
    private JToggleButton m_changeDTGButton;

    private JPanel m_fromPanel;
    private static JLabel m_fromLabel;
    private UnitFieldSelectionDialog m_fieldFromDialog;
    private SingleUnitListSelectionDialog m_listFromDialog;
    private JToggleButton  m_changeFromButton;

    private JPanel m_toPanel;
    private static JLabel m_toLabel;
    private ChangeToDialog m_changeToDialog;
    private JToggleButton  m_changeToButton;

    private JPanel m_messagePanel;

    private JPanel m_buttonRow;
	private static JToggleButton  m_textButton;
	private static JToggleButton  m_positionButton;
	private static JToggleButton  m_findingButton;
	private static JToggleButton  m_assignButton;
	private static JToggleButton  m_startButton;
	private static JToggleButton  m_completedButton;
	private static JToggleButton  m_listButton;
	private JButton m_deleteButton;

	private static JPanel m_cardsPanel;
	private static TextPanel m_textPanel;
	private static MessagePOIPanel m_messagePOIPanel;
	private static MessagePOIPanel m_messageFindingPanel;
	private static AbstractAssignmentPanel m_messageAssignedPanel;
	private static AbstractAssignmentPanel m_messageStartedPanel;
	private static AbstractAssignmentPanel m_messageCompletedPanel;
	private static LineListPanel m_messageListPanel;

    private JPanel m_taskPanel;
    private static JLabel m_taskLabel;
    private ChangeTasksDialog m_changeTasksDialog;
    private JToggleButton  m_changeTasksButton;

    private JPanel m_statusPanel;
    private JButton m_cancelStatusButton;
    private JButton m_waitEndStatusButton;
    private JButton m_finishedStatusButton;

    /**
     * Constructor
     * @param messageLog Message log reference
     */
    public MessageLogBottomPanel()
    {
    	m_newMessage = true;
    	m_editComponents = new LinkedList<IEditMessageComponentIf>();
    }

    /**
	 * Initialize GUI components
	 */
	public void initialize(JTable logTable)
	{
		initButtons();
    	initPanels(logTable);
    	initComponents();
	}

    private JPanel createPanel(int width, int height)
    {
    	JPanel panel = new JPanel();
    	panel.setLayout(new BorderLayout());

    	panel.setMinimumSize(new Dimension(width, height));
    	panel.setPreferredSize(new Dimension(width, height));
    	panel.setMaximumSize(new Dimension(width, height));

    	return panel;
    }

    private ChangeDTGDialog getChangeDTGDialog()
    {
    	if(m_changeDTGDialog == null)
    	{
    		m_changeDTGDialog = new ChangeDTGDialog(m_wpMessageLog);
    		m_changeDTGDialog.addDialogListener(this);
    		m_editComponents.add(m_changeDTGDialog);
    	}
    	return m_changeDTGDialog;
    }

    private UnitFieldSelectionDialog getFieldChangeFromDialog()
    {
    	if(m_fieldFromDialog == null)
    	{
    		m_fieldFromDialog = new UnitFieldSelectionDialog(m_wpMessageLog, true);
    		m_fieldFromDialog.addDialogListener(this);
    		m_fieldFromDialog.getOKButton().addActionListener(new ActionListener()
    		{
				public void actionPerformed(ActionEvent e)
				{
					ICommunicatorIf sender = m_fieldFromDialog.getCommunicator();
					if(sender != null)
					{
						getCurrentMessage(true).setSender(sender);
					}
					m_fieldFromDialog.hideComponent();
					m_listFromDialog.hideComponent();
				}
    			
    		});
    		m_editComponents.add(m_fieldFromDialog);
    	}

    	return m_fieldFromDialog;
    }

    private SingleUnitListSelectionDialog getListChangeFromDialog()
    {
    	if(m_listFromDialog == null)
    	{
    		m_listFromDialog = new SingleUnitListSelectionDialog(m_wpMessageLog, true);
    		m_listFromDialog.addDialogListener(this);
    		m_editComponents.add(m_listFromDialog);
    	}
    	return m_listFromDialog;
    }

    private ChangeToDialog getChangeToDialog()
	{
		if(m_changeToDialog == null)
		{
			m_changeToDialog = new ChangeToDialog(m_wpMessageLog);
			m_changeToDialog.addDialogListener(this);
			m_editComponents.add(m_changeToDialog);
		}
		return m_changeToDialog;
	}

    private TextPanel getMessageTextPanel()
    {
    	if(m_textPanel == null)
    	{
    		m_textPanel = new TextPanel(m_wpMessageLog);
    		m_editComponents.add(m_textPanel);
    		m_cardsPanel.add(m_textPanel, TEXT_PANEL_ID);
    	}
    	return m_textPanel;
    }

    private MessagePOIPanel getMessagePOIPanel()
    {
    	if(m_messagePOIPanel == null)
    	{
    		m_messagePOIPanel = new MessagePOIPanel(m_wpMessageLog, null);
    		m_editComponents.add(m_messagePOIPanel);
    		m_cardsPanel.add(m_messagePOIPanel, POSITION_PANEL_ID);
    	}
    	return m_messagePOIPanel;
    }

    private MessagePOIPanel getMessageFindingPanel()
    {
    	if(m_messageFindingPanel == null)
    	{
    		POIType[] poiTypes = {POIType.FINDING, POIType.SILENT_WITNESS};
    		m_messageFindingPanel = new MessagePOIPanel(m_wpMessageLog, poiTypes);
    		m_editComponents.add(m_messageFindingPanel);
    		m_cardsPanel.add(m_messageFindingPanel, FINDING_PANEL_ID);
    	}
    	return m_messageFindingPanel;
    }

    private void getAssignedPanel()
    {
    	if(m_messageAssignedPanel == null)
    	{
    		m_messageAssignedPanel = new AssignedAssignmentPanel(m_wpMessageLog);
    		m_editComponents.add(m_messageAssignedPanel);
    		m_cardsPanel.add(m_messageAssignedPanel, ASSIGNED_PANEL_ID);
    	}
    }

    private void getStartedPanel()
    {
    	if(m_messageStartedPanel == null)
    	{
    		m_messageStartedPanel = new StartedAssignmentPanel(m_wpMessageLog);
    		m_editComponents.add(m_messageStartedPanel);
    		m_cardsPanel.add(m_messageStartedPanel, STARTED_PANEL_ID);
    	}
    }
    private void getCompletedPanel()
	{
		if(m_messageCompletedPanel == null)
		{
			m_messageCompletedPanel = new CompletedAssignmentPanel(m_wpMessageLog);
			m_editComponents.add(m_messageCompletedPanel);
			m_cardsPanel.add(m_messageCompletedPanel, COMPLETED_PANEL_ID);
		}

	}

    private LineListPanel getMessageListPanel()
    {
    	if(m_messageListPanel == null)
    	{
    		m_messageListPanel = new LineListPanel(m_wpMessageLog);
    		m_editComponents.add(m_messageListPanel);
    		m_cardsPanel.add(m_messageListPanel, LIST_PANEL_ID);
    	}
    	return m_messageListPanel;
    }

    private ChangeTasksDialog getChangeTasksDialog()
	{
		if(m_changeTasksDialog == null)
		{
			m_changeTasksDialog = new ChangeTasksDialog(m_wpMessageLog);
			m_changeTasksDialog.addDialogListener(this);
			m_editComponents.add(m_changeTasksDialog);
		}
		return m_changeTasksDialog;
	}

    protected static void clearPanelContents()
	{
    	m_nrLabel.setText("");
		m_dtgLabel.setText("");
		m_fromLabel.setText("");
		m_toLabel.setText("");
		m_taskLabel.setText("");

		for(IEditMessageComponentIf component : m_editComponents)
		{
			component.clearContents();
			component.hideComponent();
		}
	}

    private void initPanels(JTable logTable)
    {
    	this.setLayout(new GridBagLayout());
    	GridBagConstraints gbc = new GridBagConstraints();
    	gbc.fill = GridBagConstraints.BOTH;
    	gbc.weightx = 1.0;
    	gbc.weighty = 0.0;
    	gbc.gridx = 0;
    	gbc.gridy = 0;
    	
    	// Add table header
    	gbc.gridwidth = 15;
    	JTableHeader header = logTable.getTableHeader();
    	// Remove mouse listeners, hack to bypass Swing bug 4178930
    	for(MouseListener ml : header.getMouseListeners())
    	{
    		header.removeMouseListener(ml);
    	}
    	this.add(header, gbc);
    	logTable.setTableHeader(null);
    	
    	gbc.gridwidth = 1;
    	gbc.weighty = 1.0;
    	gbc.weightx = 0.0;

    	// Nr panel
        m_nrPanel = createPanel(SMALL_PANEL_WIDTH - 2, PANEL_HEIGHT - header.getHeight());
        m_nrLabel = new JLabel();
        m_nrPanel.add(m_nrLabel, BorderLayout.CENTER);
        m_nrPanel.add(Box.createRigidArea(DiskoButtonFactory.SMALL_BUTTON_SIZE), BorderLayout.SOUTH);
        gbc.gridy = 1;
        this.add(m_nrPanel, gbc);
        gbc.gridx++;
        this.add(new JSeparator(JSeparator.VERTICAL), gbc);

        // DTG panel
        m_dtgPanel = createPanel(SMALL_PANEL_WIDTH, PANEL_HEIGHT - header.getHeight());
        m_dtgLabel = new JLabel();
        m_dtgPanel.add(m_dtgLabel, BorderLayout.CENTER);
        m_dtgPanel.add(m_changeDTGButton, BorderLayout.SOUTH);
        gbc.gridx++;
        this.add(m_dtgPanel, gbc);
        gbc.gridx++;
        this.add(new JSeparator(JSeparator.VERTICAL), gbc);
        
        // From panel
        m_fromPanel = createPanel(SMALL_PANEL_WIDTH, PANEL_HEIGHT - header.getHeight());
        m_fromLabel = new JLabel();
        m_fromPanel.add(m_fromLabel, BorderLayout.CENTER);
        m_fromPanel.add(m_changeFromButton, BorderLayout.SOUTH);
        gbc.gridx++;
        this.add(m_fromPanel, gbc);
        gbc.gridx++;
        this.add(new JSeparator(JSeparator.VERTICAL), gbc);
        
        // To panel
        m_toPanel = createPanel(SMALL_PANEL_WIDTH, PANEL_HEIGHT - header.getHeight());
        m_toLabel = new JLabel();
        m_toPanel.add(m_toLabel, BorderLayout.CENTER);
        m_toPanel.add(m_changeToButton, BorderLayout.SOUTH);
        gbc.gridx++;
        this.add(m_toPanel, gbc);
        gbc.gridx++;
        this.add(new JSeparator(JSeparator.VERTICAL), gbc);

        // Message panel
        gbc.weightx = 1.0;
        m_messagePanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(m_messagePanel, BoxLayout.Y_AXIS);
        m_messagePanel.setLayout(boxLayout);
        m_cardsPanel = new JPanel();
        m_cardsPanel.setLayout(new CardLayout());
        m_cardsPanel.setPreferredSize(new Dimension(600, PANEL_HEIGHT - header.getHeight()));
        m_cardsPanel.setAlignmentX(0.0f);
        m_cardsPanel.add(new JPanel(), EMPTY_PANEL_ID);
        CardLayout layout = (CardLayout)m_cardsPanel.getLayout();
        layout.show(m_cardsPanel, EMPTY_PANEL_ID);
        m_messagePanel.add(m_cardsPanel);

        m_buttonRow.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        m_buttonRow.setMaximumSize(new Dimension(SMALL_PANEL_WIDTH*9, 
        		(int)DiskoButtonFactory.SMALL_BUTTON_SIZE.getHeight()));
        m_buttonRow.setAlignmentX(0.0f);
        m_messagePanel.add(m_buttonRow);
        gbc.gridx++;
        this.add(m_messagePanel, gbc);
        gbc.weightx = 0.0;
        gbc.gridx++;
        this.add(new JSeparator(JSeparator.VERTICAL), gbc);

        // Task panel
        gbc.weightx = 0.0;
        m_taskPanel = createPanel(2*SMALL_PANEL_WIDTH - 1, PANEL_HEIGHT - header.getHeight());
        m_taskLabel = new JLabel();
        m_taskPanel.add(m_taskLabel, BorderLayout.CENTER);
        JPanel taskButtonPanel = new JPanel();
        taskButtonPanel.setLayout(new BoxLayout(taskButtonPanel, BoxLayout.PAGE_AXIS));
        taskButtonPanel.setBorder(null);
        taskButtonPanel.add(m_changeTasksButton);
        m_taskPanel.add(taskButtonPanel, BorderLayout.SOUTH);
        gbc.gridx++;
        this.add(m_taskPanel, gbc);
        gbc.gridx++;
        this.add(new JSeparator(JSeparator.VERTICAL), gbc);
        
        // Fill to match table scroll-bar
        gbc.gridx++;
        this.add(Box.createRigidArea(new Dimension(43, 10)), gbc);
        
        // Status panel
        m_statusPanel = createPanel(SMALL_PANEL_WIDTH + 2, PANEL_HEIGHT - header.getHeight());
        JPanel actionButtonPanel = new JPanel();
        actionButtonPanel.setLayout(new BoxLayout(actionButtonPanel, BoxLayout.PAGE_AXIS));
        actionButtonPanel.add(m_cancelStatusButton);
        actionButtonPanel.add(m_waitEndStatusButton);
        actionButtonPanel.add(m_finishedStatusButton);
        m_statusPanel.add(actionButtonPanel, BorderLayout.SOUTH);
        gbc.gridx++;
        this.add(m_statusPanel, gbc);
    }

    private void initComponents()
    {
    	getChangeDTGDialog();
    	getFieldChangeFromDialog();
    	getListChangeFromDialog();
    	getChangeToDialog();
    	getMessageTextPanel();
    	getMessagePOIPanel();
    	getMessageFindingPanel();
    	getAssignedPanel();
    	getStartedPanel();
    	getCompletedPanel();
    	getChangeTasksDialog();
    	getMessageListPanel();
    	
    	// Register listeners
		m_fieldFromDialog.addActionListener(m_listFromDialog);
    }

    private void initButtons()
    {
    	m_buttonRow = new JPanel(new FlowLayout(FlowLayout.LEADING, 4, 0));
    	m_buttonGroup = new ButtonGroup();

    	createChangeDtgButton();
        createChangeFromButton();
        createChangeToButton();
        createTextButton();
        createPositionButton();
        createFindingButton();
        createAssignedButton();
        createStartedButton();
        createCompletedButton();
        createListButton();
        createDeleteButton();
        createChangeTasksButton();
        createCancleButton();
        createWaitEndButton();
        createFinishedButton();
    }

    /**
     * An existing message is selected in the message log for editing.
     * @param messageNr The number of the selected message
     */
	public void newMessageSelected(int messageNr)
	{
		// Have user confirm message overwrite
		if(m_currentMessage != null && m_currentMessage.getNumber() != messageNr && m_messageDirty)
		{

			Object[] options = {m_wpMessageLog.getText("yes.text"), m_wpMessageLog.getText("no.text")};
			int n = JOptionPane.showOptionDialog(m_wpMessageLog.getApplication().getFrame(),
					m_wpMessageLog.getText("DirtyMessageWarning.text"),
					m_wpMessageLog.getText("DirtyMessageWarning.header"),
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[0]);

			if(n == JOptionPane.NO_OPTION)
			{
				return;
			}
			else
			{
				// Roll back any changes made to the message
				if(m_newMessage)
				{
					m_currentMessage.deleteObject();
				}
				else
				{
					m_wpMessageLog.getMsoManager().rollback();
				}
			}
		}
		m_messageDirty = false;
		m_newMessage = false;

		// Get the message
		IMessageLogIf messageLog = m_wpMessageLog.getCmdPost().getMessageLog();
		for(IMessageIf message : messageLog.getItems())
		{
			if(message.getNumber() == messageNr)
			{
				m_currentMessage = message;
				break;
			}
		}

		updateMessageGUI();
	}

	private void updateMessageGUI()
	{
		if(m_currentMessage != null)
		{
			// Update panel contents
			m_nrLabel.setText(Integer.toString(m_currentMessage.getNumber()));
			m_dtgLabel.setText(DTG.CalToDTG(m_currentMessage.getOccuredTime()));

			// Update from
			ICommunicatorIf sender = m_currentMessage.getSender();
			if(sender == null)
			{
				sender = (ICommunicatorIf)m_wpMessageLog.getCmdPost();
			}
			m_fromLabel.setText(sender.getCommunicatorNumberPrefix() + " " + sender.getCommunicatorNumber());

			// Update to
			if(m_currentMessage.isBroadcast())
			{
				m_toLabel.setText(m_wpMessageLog.getText("BroadcastLabel.text"));
			}
			else
			{
				ICommunicatorIf receiver = m_currentMessage.getSingleReceiver();
				if(receiver == null)
				{
					receiver = (ICommunicatorIf)m_wpMessageLog.getMsoManager().getCmdPost();
				}
				m_toLabel.setText(receiver.getCommunicatorNumberPrefix() + " " + receiver.getCommunicatorNumber());
			}

			ITaskListIf tasks = m_currentMessage.getMessageTasks();
			StringBuilder tasksString = new StringBuilder("<html>");
			for(ITaskIf task : tasks.getItems())
			{
				tasksString.append(task.getTaskText() + "<br>");
			}
			tasksString.append("</html>");
			m_taskLabel.setText(tasksString.toString());

			// Update dialogs
			for(int i=0; i<m_editComponents.size(); i++)
			{
				m_editComponents.get(i).newMessageSelected(m_currentMessage);
			}
		}
	}

    /**
     * Set the message log reference
     * @param wp
     */
	public void setWp(IDiskoWpMessageLog wp)
	{
		m_wpMessageLog = wp;
	}

	/**
	 * {@link IMsoUpdateListenerIf#handleMsoUpdateEvent(Update)}
	 */
	public void handleMsoUpdateEvent(Update e)
	{
		updateMessageGUI();
	}

	private final EnumSet<IMsoManagerIf.MsoClassCode> myInterests = EnumSet.of(
			IMsoManagerIf.MsoClassCode.CLASSCODE_MESSAGE,
			IMsoManagerIf.MsoClassCode.CLASSCODE_MESSAGELINE,
			IMsoManagerIf.MsoClassCode.CLASSCODE_POI);

	/**
	 * {@link IMsoUpdateListenerIf#hasInterestIn(IMsoObjectIf)}
	 */
	public boolean hasInterestIn(IMsoObjectIf msoObject)
	{
		 return myInterests.contains(msoObject.getMsoClassCode());
	}

	/**
	 * {@link IDialogEventListener#dialogCanceled(DialogEvent)}
	 */
	public void dialogCanceled(DialogEvent e)
	{
		//Object sender = e.getSource();
		m_buttonGroup.clearSelection();
		hideEditPanels();
	}

	/**
	 * Gets new data from a dialog once a dialog finishes
	 */
	public void dialogFinished(DialogEvent e)
	{
		// If no message is selected a new one should be created once a field is edited
		if(m_currentMessage == null)
		{
			getCurrentMessage(true);
		}

		hideEditPanels();
		m_buttonGroup.clearSelection();
		updateMessageGUI();
		m_messageDirty = true;
	}

	public void dialogStateChanged(DialogEvent e)
	{
	}

	private JButton createWaitEndButton()
    {
    	if(m_waitEndStatusButton == null)
    	{
    		m_waitEndStatusButton = DiskoButtonFactory.createSmallButton(m_wpMessageLog.getText("WaitEndButton.text")/*,
    				m_wpMessageLog.getText("WaitEndButton.icon")*/);
    		m_waitEndStatusButton.addActionListener(new ActionListener()
    		{
				public void actionPerformed(ActionEvent arg0)
				{
					if(m_currentMessage != null)
					{
						// Commit message, set status to postponed
						m_currentMessage.setStatus(MessageStatus.POSTPONED);

						m_wpMessageLog.getMsoModel().commit();

						m_messageDirty = false;

						for(IEditMessageComponentIf dialog : m_editComponents)
						{
							dialog.hideComponent();
						}

						m_buttonGroup.clearSelection();
						clearPanelContents();
					}
				}
    		});
    	}
    	return m_waitEndStatusButton;
    }

    private JButton createFinishedButton()
    {
    	if(m_finishedStatusButton == null)
    	{
    		m_finishedStatusButton = DiskoButtonFactory.createSmallButton(m_wpMessageLog.getText("FinishedButton.text"),
    				m_wpMessageLog.getText("FinishedButton.icon"));

    		m_finishedStatusButton.addActionListener(new ActionListener()
    		{
				// Commit current message
				public void actionPerformed(ActionEvent arg0)
				{
					if(m_currentMessage != null)
					{
						// Set message status
						if(m_currentMessage.isBroadcast())
						{
							if(m_currentMessage.getUnconfirmedReceivers().size() == 0)
							{
								m_currentMessage.setStatus(MessageStatus.CONFIRMED);
							}
							else
							{
								// If broadcast all units have to confirm to get confirmed status
								m_currentMessage.setStatus(MessageStatus.UNCONFIRMED);
							}
						}
						else
						{
							m_currentMessage.setStatus(MessageStatus.CONFIRMED);
						}

						// Handle assignments
						updateAssignments();
						
						// Commit changes
						m_wpMessageLog.getMsoModel().commit();

						m_currentMessage = null;
						m_messageDirty = false;

						
						// GUI clean-up
						clearPanelContents();

						m_buttonGroup.clearSelection();
					}
				}
    		});
    	}
    	return m_finishedStatusButton;
    }

    private JToggleButton createChangeDtgButton()
    {
    	m_changeDTGButton = DiskoButtonFactory.createSmallToggleButton(m_wpMessageLog.getText("DTGButton.text"));
    	m_changeDTGButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
    	m_changeDTGButton.addActionListener(new ActionListener()
    	{
    		// Display the change DTG dialog when button is pressed
    		public void actionPerformed(ActionEvent e)
    		{
    			if(getChangeDTGDialog().isVisible())
    			{
    				getChangeDTGDialog().hideComponent();
    				m_buttonGroup.clearSelection();
    			}
    			else
    			{
    				hideEditPanels();
        			getChangeDTGDialog();
        			Point location = m_changeDTGButton.getLocationOnScreen();
        			location.y -= m_changeDTGDialog.getHeight();
        			m_changeDTGDialog.setLocation(location);
        			m_changeDTGDialog.showComponent();
    			}
    		}
    	});
    	m_buttonGroup.add(m_changeDTGButton);
    	return m_changeDTGButton;
    }

    private JToggleButton  createChangeTasksButton()
    {
    	if(m_changeTasksButton == null)
    	{
    		m_changeTasksButton = DiskoButtonFactory.createSmallToggleButton(m_wpMessageLog.getText("TasksButton.text"));
    		m_changeTasksButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
    		m_changeTasksButton.addActionListener(new ActionListener()
    		{
				public void actionPerformed(ActionEvent e)
				{
					if(getChangeTasksDialog().isVisible())
					{
						getChangeTasksDialog().hideComponent();
						m_buttonGroup.clearSelection();
					}
					else
					{
						// Create new message if null
						getCurrentMessage(true);
						m_messageDirty = true;

						getChangeTasksDialog();
						hideEditPanels();
						Point location = m_changeTasksButton.getLocationOnScreen();
		    			location.y -= m_changeTasksDialog.getHeight();
		    			location.x -= m_changeTasksDialog.getWidth();
		    			m_changeTasksDialog.setLocation(location);
						m_changeTasksDialog.showComponent();
					}
				}
    		});
    		m_buttonGroup.add(m_changeTasksButton);
    	}
    	return m_changeTasksButton;
    }

    private JToggleButton  createChangeFromButton()
    {
    	if(m_changeFromButton == null)
    	{
    		m_changeFromButton = DiskoButtonFactory.createSmallToggleButton(m_wpMessageLog.getText("FromButton.text"));
    		m_changeFromButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
    		m_changeFromButton.addActionListener(new ActionListener()
    		{
				public void actionPerformed(ActionEvent arg0)
				{
					if(getFieldChangeFromDialog().isVisible())
					{
						// Toggle dialogs if visible
						getFieldChangeFromDialog().hideComponent();
						getListChangeFromDialog().hideComponent();
						m_buttonGroup.clearSelection();
					}
					else
					{
						hideEditPanels();
						
						// Initialize fields
						if(m_currentMessage != null)
						{
							ICommunicatorIf sender = m_currentMessage.getSender();
							if(sender != null)
							{
								m_fieldFromDialog.setCommunicatorNumber(sender.getCommunicatorNumber());
								m_fieldFromDialog.setCommunicatorNumberPrefix(sender.getCommunicatorNumberPrefix());
							}
						}
						
						Point location = m_changeFromButton.getLocationOnScreen();
						location.y -= m_fieldFromDialog.getHeight();
						m_fieldFromDialog.setLocation(location);
						m_fieldFromDialog.showComponent();
						
						location = m_changeFromButton.getLocationOnScreen();
						location.y -= m_listFromDialog.getHeight();
						location.x += m_fieldFromDialog.getWidth();
						m_listFromDialog.setLocation(location);
						m_listFromDialog.showComponent();
					}
				}
    		});
    		m_buttonGroup.add(m_changeFromButton);
    	}
    	return m_changeFromButton;
    }

    private JToggleButton  createChangeToButton()
    {
    	if(m_changeToButton == null)
    	{
    		m_changeToButton = DiskoButtonFactory.createSmallToggleButton(m_wpMessageLog.getText("ToButton.text"));
    		m_changeToButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
    		m_changeToButton.addActionListener(new ActionListener()
    		{
				public void actionPerformed(ActionEvent e)
				{
					if(getChangeToDialog().isVisible())
					{
						getChangeToDialog().hideComponent();
						m_buttonGroup.clearSelection();
					}
					else
					{
						getChangeToDialog();
						hideEditPanels();
						Point location = m_changeToButton.getLocationOnScreen();
						location.y -= DiskoButtonFactory.LARGE_BUTTON_SIZE.height;
						m_changeToDialog.setLocation(location);
						m_changeToDialog.showComponent();
					}
				}
    		});
    		m_buttonGroup.add(m_changeToButton);
    	}
    	return m_changeToButton;
    }

    private JButton createCancleButton()
    {
    	if(m_cancelStatusButton == null)
    	{
    		m_cancelStatusButton = DiskoButtonFactory.createSmallButton(m_wpMessageLog.getText("CancelButton.text"),
    				m_wpMessageLog.getText("CancelButton.icon"));

    		m_cancelStatusButton.addActionListener(new ActionListener()
    		{
				public void actionPerformed(ActionEvent e)
				{
					clearCurrentMessage();
				}
    		});
    	}
    	return m_cancelStatusButton;
    }

    private void createDeleteButton()
	{
    	m_deleteButton = DiskoButtonFactory.createSmallButton(m_wpMessageLog.getText("DeleteButton.text"));
		m_deleteButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// Delete if message is not committed, don't create new message when pressing delete
				if(m_currentMessage != null && m_currentMessageLineType != null && m_newMessage)
				{
					// If line that has other assignment lines depending on it is deleted, delete them as well
					switch(m_currentMessageLineType)
					{
					case ASSIGNED:
						for(IMessageLineIf line : m_messageAssignedPanel.getAddedLines())
						{
							line.deleteObject();
						}
						m_messageAssignedPanel.lineRemoved(null);
						break;
					case STARTED:
						for(IMessageLineIf line : m_messageStartedPanel.getAddedLines())
						{
							line.deleteObject();
						}
						m_messageStartedPanel.lineRemoved(null);
						break;
					case COMPLETE:
						for(IMessageLineIf line : m_messageCompletedPanel.getAddedLines())
						{
							line.deleteObject();
						}
						m_messageCompletedPanel.lineRemoved(null);
						break;
					default:
						IMessageIf message = MessageLogBottomPanel.getCurrentMessage(false);
						IMessageLineIf line = null;
						if(message != null)
						{
							line = message.findMessageLine(m_currentMessageLineType, false);
						}
						if(line != null)
						{
							line.deleteObject();
						}
						break;
					}
				}
				else
				{
					ErrorDialog error = new ErrorDialog(m_wpMessageLog.getApplication().getFrame());
					error.showError(m_wpMessageLog.getText("CanNotDeleteMessageLine.header"),
							m_wpMessageLog.getText("CanNotDeleteMessageLine.details"));
				}
			}
		});

		m_buttonGroup.add(m_deleteButton);
		m_buttonRow.add(m_deleteButton);
	}

	private void createListButton()
	{
		m_listButton = DiskoButtonFactory.createSmallToggleButton(m_wpMessageLog.getText("ListButton.text"));
		m_listButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				getMessageListPanel();
				hideEditPanels();

				CardLayout layout = (CardLayout)m_cardsPanel.getLayout();
				layout.show(m_cardsPanel, LIST_PANEL_ID);

				m_messageListPanel.showComponent();
			}

		});
		m_buttonGroup.add(m_listButton);
		m_buttonRow.add(m_listButton);
	}

	private void createCompletedButton()
	{
		m_completedButton = DiskoButtonFactory.createSmallToggleButton(m_wpMessageLog.getText("CompletedButton.text"));
		m_completedButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ErrorDialog errorDialog = Utils.getErrorDialog();
				hideEditPanels();

				if(getCurrentMessage(true).isBroadcast())
				{
					errorDialog.showError(m_wpMessageLog.getText("CompletedError.header"),
							m_wpMessageLog.getText("CompletedError.details"));
				}
				else if(isReceiverCommandPost())
				{
					// Not possible to assign when receiver is CP
					errorDialog.showError(m_wpMessageLog.getText("ReceiverCommandPostError.header"),
							m_wpMessageLog.getText("ReceiverCommandPostError.details"));
				}
				else if(!isAssignmentOperationLegal())
				{
					// Require certain message status
					errorDialog.showError(m_wpMessageLog.getText("MessageTaskOperationError.header"),
							m_wpMessageLog.getText("MessageTaskOperationError.details"));
				}
				else
				{
					getCompletedPanel();
					m_currentMessageLineType = MessageLineType.COMPLETE;

					CardLayout layout = (CardLayout)m_cardsPanel.getLayout();
					layout.show(m_cardsPanel, COMPLETED_PANEL_ID);
					m_messageCompletedPanel.showComponent();
				}
			}
		});
		m_buttonGroup.add(m_completedButton);
		m_buttonRow.add(m_completedButton);
	}

	private void createStartedButton()
	{
		m_startButton = DiskoButtonFactory.createSmallToggleButton(m_wpMessageLog.getText("StartedButton.text"));
		m_startButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ErrorDialog errorDialog = Utils.getErrorDialog();
				hideEditPanels();

				if(getCurrentMessage(true).isBroadcast())
				{
					errorDialog.showError(m_wpMessageLog.getText("StartedError.header"),
							m_wpMessageLog.getText("StartedError.details"));
				}
				else if(isReceiverCommandPost())
				{
					// Not possible to assign when receiver is CP
					errorDialog.showError(m_wpMessageLog.getText("ReceiverCommandPostError.header"),
							m_wpMessageLog.getText("ReceiverCommandPostError.details"));
				}
				else if(!isAssignmentOperationLegal())
				{
					// Require certain message status
					errorDialog.showError(m_wpMessageLog.getText("MessageTaskOperationError.header"),
							m_wpMessageLog.getText("MessageTaskOperationError.details"));
				}
				else
				{
					getStartedPanel();
					m_currentMessageLineType = MessageLineType.STARTED;

					CardLayout layout = (CardLayout)m_cardsPanel.getLayout();
					layout.show(m_cardsPanel, STARTED_PANEL_ID);
					m_messageStartedPanel.showComponent();
				}
			}
		});
		m_buttonGroup.add(m_startButton);
		m_buttonRow.add(m_startButton);
	}

	private void createAssignedButton()
	{
		m_assignButton = DiskoButtonFactory.createSmallToggleButton(m_wpMessageLog.getText("AssignedButton.text"));
		m_assignButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ErrorDialog errorDialog = Utils.getErrorDialog();
				hideEditPanels();

				if(getCurrentMessage(true).isBroadcast())
				{
					// Only legal if message isn't broadcast
					errorDialog.showError(m_wpMessageLog.getText("AssignmentError.header"),
							m_wpMessageLog.getText("AssignmentError.details"));
				}
				else if(isReceiverCommandPost())
				{
					// Not possible to assign when receiver is CP
					errorDialog.showError(m_wpMessageLog.getText("ReceiverCommandPostError.header"),
							m_wpMessageLog.getText("ReceiverCommandPostError.details"));
				}
				else if(!isAssignmentOperationLegal())
				{
					// Require certain message status
					errorDialog.showError(m_wpMessageLog.getText("MessageTaskOperationError.header"),
							m_wpMessageLog.getText("MessageTaskOperationError.details"));
				}
				else
				{
					// Task can be changed
					getAssignedPanel();
					m_currentMessageLineType = MessageLineType.ASSIGNED;

					CardLayout layout = (CardLayout)m_cardsPanel.getLayout();
					layout.show(m_cardsPanel, ASSIGNED_PANEL_ID);
					m_messageAssignedPanel.showComponent();
				}
			}
		});
		m_buttonGroup.add(m_assignButton);
		m_buttonRow.add(m_assignButton);
	}

	private boolean isReceiverCommandPost()
	{
		if(m_currentMessage != null)
		{
			ICommunicatorIf receiver = m_currentMessage.getSingleReceiver();
			if(receiver != null)
			{
				return receiver instanceof ICmdPostIf;
			}
			else
			{
				// Receiver is, by default, command post
				return true;
			}
		}
		
		return false;
	}

	private boolean isAssignmentOperationLegal()
	{
		if(m_newMessage)
		{
			return true;
		}

		if(m_currentMessage == null)
		{
			return false;
		}
		
		MessageStatus status = m_currentMessage.getStatus();
		return (status == MessageStatus.UNCONFIRMED || status == MessageStatus.POSTPONED);
	}

	/**
	 * Ensures that units and assignments affected by the added message lines in the current message are
	 * updated (status, etc.). Message lines are also updated according to what operations are legal
	 */
	private void updateAssignments()
	{

		if(m_currentMessage != null && !m_currentMessage.isBroadcast())
		{
			ICommunicatorIf communicator = m_currentMessage.getSingleReceiver();
			IUnitIf unit = null;
			if(communicator instanceof IUnitIf)
			{
				unit = (IUnitIf)communicator;
			}

			if(unit != null)
			{
				ErrorDialog error = new ErrorDialog(m_wpMessageLog.getApplication().getFrame());
				IAssignmentIf assignment = null;
				List<IMessageLineIf> messageLines = new LinkedList<IMessageLineIf>();

				// Get all assignment lines. Lines from complete is placed first, started second, assign last.
				// This should ensure that unit statuses are updated in the correct order
				messageLines.addAll(m_messageCompletedPanel.getAddedLines());
				messageLines.addAll(m_messageStartedPanel.getAddedLines());
				messageLines.addAll(m_messageAssignedPanel.getAddedLines());

				// Update status
				for(IMessageLineIf line : messageLines)
				{
					assignment = line.getLineAssignment();

						switch(line.getLineType())
						{
						case ASSIGNED:
							try
							{
								AssignmentTransferUtilities.assignAssignmentToUnit(assignment, unit);
							}
							catch(IllegalOperationException e)
							{
								line.deleteObject();
								error.showError(m_wpMessageLog.getText("CanNotAssignError.header"),
										String.format(m_wpMessageLog.getText("CanNotAssignError.details"), unit.getTypeAndNumber(), assignment.getTypeAndNumber()));
							}
							break;
						case STARTED:
							try
							{
								AssignmentTransferUtilities.unitStartAssignment(unit, assignment);
							}
							catch(IllegalOperationException e)
							{
								line.deleteObject();
								error.showError(m_wpMessageLog.getText("CanNotStartError.header"),
										String.format(m_wpMessageLog.getText("CanNotStartError.details"), unit.getTypeAndNumber(), assignment.getTypeAndNumber()));
							}
							break;
						case COMPLETE:
							try
							{
								AssignmentTransferUtilities.unitCompleteAssignment(unit, assignment);
							}
							catch(IllegalOperationException e)
							{
								line.deleteObject();
								error.showError(m_wpMessageLog.getText("CanNotCompleteError.header"),
										String.format(m_wpMessageLog.getText("CanNotCompleteError.details"), unit.getTypeAndNumber(), assignment.getTypeAndNumber()));
							}
							break;
						default:
							continue;
						}
				}
			}

			// Keep track of which lines are added
			m_messageAssignedPanel.lineRemoved(null);
			m_messageStartedPanel.lineRemoved(null);
			m_messageCompletedPanel.lineRemoved(null);
		}
	}

	private void createFindingButton()
	{
		m_findingButton = DiskoButtonFactory.createSmallToggleButton(m_wpMessageLog.getText("FindingButton.text"));
		m_findingButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				getMessageFindingPanel();
				hideEditPanels();
				m_currentMessageLineType = MessageLineType.FINDING;
				m_messageFindingPanel.setMapTool();

				CardLayout layout = (CardLayout)m_cardsPanel.getLayout();
				layout.show(m_cardsPanel, FINDING_PANEL_ID);

				m_messageFindingPanel.showComponent();
			}
		});
		m_buttonGroup.add(m_findingButton);
		m_buttonRow.add(m_findingButton);
	}

	private void createPositionButton()
	{
		m_positionButton = DiskoButtonFactory.createSmallToggleButton(m_wpMessageLog.getText("PositionButton.text"));
		m_positionButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				getMessagePOIPanel();
				hideEditPanels();
				m_currentMessageLineType = MessageLineType.POI;
				m_messagePOIPanel.setMapTool();

				CardLayout layout = (CardLayout)m_cardsPanel.getLayout();
				layout.show(m_cardsPanel, POSITION_PANEL_ID);

				m_messagePOIPanel.showComponent();
			}
		});
		m_buttonGroup.add(m_positionButton);
		m_buttonRow.add(m_positionButton);
	}

	private void createTextButton()
	{
		m_textButton = DiskoButtonFactory.createSmallToggleButton(m_wpMessageLog.getText("TextButton.text"));
		m_textButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				getMessageTextPanel();
				hideEditPanels();
				m_currentMessageLineType = MessageLineType.TEXT;

				CardLayout layout = (CardLayout)m_cardsPanel.getLayout();
				layout.show(m_cardsPanel, TEXT_PANEL_ID);

				m_textPanel.showComponent();
			}
		});
		m_buttonGroup.add(m_textButton);
		m_buttonRow.add(m_textButton);
	}

	/**
	 * Hides all panels
	 */
	public void hideEditPanels()
	{
		for(int i=0; i<m_editComponents.size(); i++)
		{
			m_editComponents.get(i).hideComponent();
		}
	}

	/**
	 * Unselect all field edit buttons
	 */
	public void clearSelection()
	{
		m_buttonGroup.clearSelection();
	}

	public static void showAssignPanel()
	{
		m_assignButton.doClick();
	}

	public static void showListPanel()
	{
		m_listButton.doClick();
	}

	public static void showCompletePanel()
	{
		m_completedButton.doClick();
	}

	public static void showStartPanel()
	{
		m_startButton.doClick();
	}

	public static void showTextPanel()
	{
		m_textButton.doClick();
	}

	public static void showPositionPanel()
	{
		m_positionButton.doClick();
	}

	public static void showFindingPanel()
	{
		m_findingButton.doClick();
	}

	public static void cancelAssign()
	{
		m_messageAssignedPanel.cancelUpdate();
	}

	public static void cancelStarted()
	{
		m_messageStartedPanel.cancelUpdate();
	}

	public static boolean isMessageDirty()
	{
		return m_messageDirty || m_wpMessageLog.getMsoModel().hasUncommitedChanges();
	}

	/**
	 * Remove any changes since last commit. Clear panel contents
	 */
	public static void clearCurrentMessage()
	{
		if(m_newMessage && m_currentMessage != null)
		{
			m_currentMessage.deleteObject();
		}
		
		m_currentMessage = null;
		m_messageDirty = false;
		m_buttonGroup.clearSelection();

		clearPanelContents();
		
		m_wpMessageLog.getMsoModel().rollback();
	}
}