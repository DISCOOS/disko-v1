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
import org.redcross.sar.util.mso.Selector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Top edit panel
 *
 * @author thomasl
 */
public class MessageLogTopPanel extends JPanel implements IMsoUpdateListenerIf, IDialogEventListener
{
	private static final long serialVersionUID = 1L;

	public final static int PANEL_HEIGHT = (MessageLogPanel.SMALL_BUTTON_SIZE.height) * 3 + 20;
	public final static int SMALL_PANEL_WIDTH = 60;

	private final static String EMPTY_PANEL_ID = "EMPTY_PANEL";
	private final static String TEXT_PANEL_ID = "TEXT_PANEL";
	private final static String POSITION_PANEL_ID = "POSITION_PANEL";
	private final static String FINDING_PANEL_ID = "FINDING_PANEL";
	private final static String ASSIGNED_PANEL_ID = "ASSIGNED_PANEL";
	private final static String STARTED_PANEL_ID = "STARTED_PANEL";
	private final static String COMPLETED_PANEL_ID = "COMPLETED_PANEL";
	private final static String LIST_PANEL_ID = "LIST_PANEL";

	IMessageLogIf m_messageLog;

	private static IDiskoWpMessageLog m_wpMessageLog;

	// Current message Singleton
	private static int m_currentMessageNr;
	private static boolean m_newMessage;
	private static IMessageIf m_currentMessage = null;
	private static boolean m_messageDirty = false;
	/**
	 * Get current message, Singleton
	 * @return The message
	 */
	public static IMessageIf getCurrentMessage()
	{
		if(m_currentMessage == null)
		{
			m_currentMessage = m_wpMessageLog.getMsoManager().createMessage();
			m_currentMessage.setCreated(Calendar.getInstance());
			m_currentMessage.setOccuredTime(Calendar.getInstance());
			m_newMessage = true;
			m_messageDirty = false;
			m_currentMessageNr = m_currentMessage.getNumber();
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
    private JLabel m_messagePanelTopLabel;

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
    public MessageLogTopPanel(IMessageLogIf messageLog)
    {
    	m_messageLog = messageLog;

    	m_newMessage = true;
    	m_currentMessageNr = 0;

    	m_editComponents = new LinkedList<IEditMessageComponentIf>();
    }

    /**
	 * Initialize GUI components
	 */
	public void initialize()
	{
		initButtons();
    	initPanels();
    	initComponents();
	}

    private JPanel createPanel(int width, int height, String labelString)
    {
    	JPanel panel = new JPanel();
    	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    	panel.setMinimumSize(new Dimension(width, height));
    	panel.setPreferredSize(new Dimension(width, height));
    	panel.setMaximumSize(new Dimension(width, height));

    	// Top row label
        JLabel label = new JLabel(labelString);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
    	return panel;
    }

    private JToggleButton createChangeButton()
    {
    	JToggleButton  button = DiskoButtonFactory.createSmallToggleButton(m_wpMessageLog.getText("ChangeButton.text"),
    			m_wpMessageLog.getText("ChangeButton.icon"));
        button.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        return button;
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
		}
	}

    private void initPanels()
    {
    	this.setLayout(new GridBagLayout());
    	GridBagConstraints gbc = new GridBagConstraints();
    	gbc.fill = GridBagConstraints.BOTH;
    	gbc.weightx = 0.0;
    	gbc.weighty = 1.0;
    	gbc.gridx = 0;
    	gbc.gridy = 0;

    	// Nr panel
        m_nrPanel = createPanel(SMALL_PANEL_WIDTH, PANEL_HEIGHT, m_wpMessageLog.getText("MessagePanelNrLabel.text"));
        m_nrPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        m_nrLabel = new JLabel();
        m_nrLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        m_nrPanel.add(m_nrLabel);
        m_nrPanel.add(Box.createVerticalGlue());
        Dimension nrBoxDimension = new Dimension(MessageLogPanel.SMALL_BUTTON_SIZE);
        nrBoxDimension.width = SMALL_PANEL_WIDTH/2;
        JComponent nrButtonBox = (JComponent)Box.createRigidArea(nrBoxDimension);
        nrButtonBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        m_nrPanel.add(nrButtonBox);
        this.add(m_nrPanel, gbc);
        gbc.gridx++;
        this.add(new JSeparator(SwingConstants.VERTICAL), gbc);

        // DTG panel
        m_dtgPanel = createPanel(SMALL_PANEL_WIDTH, PANEL_HEIGHT, m_wpMessageLog.getText("MessagePanelDTGLabel.text"));
        m_dtgPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        m_dtgLabel = new JLabel();
        m_dtgPanel.add(m_dtgLabel);
        m_dtgPanel.add(Box.createVerticalGlue());
        m_dtgPanel.add(m_changeDTGButton);
        gbc.gridx++;
        this.add(m_dtgPanel, gbc);
        gbc.gridx++;
        this.add(new JSeparator(SwingConstants.VERTICAL), gbc);

        // From panel
        gbc.gridx++;
        m_fromPanel = createPanel(SMALL_PANEL_WIDTH, PANEL_HEIGHT, m_wpMessageLog.getText("MessagePanelFromLabel.text"));
        m_fromPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        m_fromLabel = new JLabel();
        m_fromPanel.add(m_fromLabel);
        m_fromPanel.add(Box.createVerticalGlue());
        m_fromPanel.add(m_changeFromButton);
        gbc.gridx++;
        this.add(m_fromPanel, gbc);
        gbc.gridx++;
        this.add(new JSeparator(SwingConstants.VERTICAL), gbc);

        // To panel
        m_toPanel = createPanel(SMALL_PANEL_WIDTH, PANEL_HEIGHT, m_wpMessageLog.getText("MessagePanelToLabel.text"));
        m_toPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        m_toLabel = new JLabel();
        m_toPanel.add(m_toLabel);
        m_toPanel.add(Box.createVerticalGlue());
        m_toPanel.add(m_changeToButton);
        gbc.gridx++;
        this.add(m_toPanel, gbc);
        gbc.gridx++;
        this.add(new JSeparator(SwingConstants.VERTICAL), gbc);

        // Message panel
        gbc.weightx = 1.0;
        m_messagePanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(m_messagePanel, BoxLayout.Y_AXIS);
        m_messagePanel.setLayout(boxLayout);
        m_messagePanelTopLabel = new JLabel(m_wpMessageLog.getText("MessagePanelTextLabel.text"));
        m_messagePanelTopLabel.setAlignmentX(0.0f);
        m_messagePanel.add(m_messagePanelTopLabel);
        m_messagePanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        m_cardsPanel = new JPanel();
        m_cardsPanel.setLayout(new CardLayout());
        m_cardsPanel.setPreferredSize(new Dimension(600, 120));
        m_cardsPanel.setAlignmentX(0.0f);
        m_cardsPanel.add(new JPanel(), EMPTY_PANEL_ID);
        CardLayout layout = (CardLayout)m_cardsPanel.getLayout();
        layout.show(m_cardsPanel, EMPTY_PANEL_ID);
        m_messagePanel.add(m_cardsPanel);

        m_buttonRow.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        m_buttonRow.setMaximumSize(new Dimension(SMALL_PANEL_WIDTH*9, (int)MessageLogPanel.SMALL_BUTTON_SIZE.getHeight()));
        m_buttonRow.setAlignmentX(0.0f);
        m_messagePanel.add(m_buttonRow);
        gbc.gridx++;
        this.add(m_messagePanel, gbc);
        gbc.weightx = 0.0;
        gbc.gridx++;
        this.add(new JSeparator(SwingConstants.VERTICAL), gbc);

        // Task panel
        gbc.weightx = 0.0;
        m_taskPanel = createPanel(2*SMALL_PANEL_WIDTH, PANEL_HEIGHT, m_wpMessageLog.getText("MessagePanelTaskLabel.text"));
        m_taskPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        m_taskLabel = new JLabel();
        m_taskPanel.add(m_taskLabel);
        m_taskPanel.add(Box.createVerticalGlue());
        m_taskPanel.add(m_changeTasksButton);
        gbc.gridx++;
        this.add(m_taskPanel, gbc);
        gbc.gridx++;
        this.add(new JSeparator(SwingConstants.VERTICAL), gbc);

        // Status panel
        m_statusPanel = new JPanel();
        m_statusPanel.setLayout(new BoxLayout(m_statusPanel, BoxLayout.Y_AXIS));
        m_statusPanel.setMinimumSize(new Dimension(SMALL_PANEL_WIDTH + 18, PANEL_HEIGHT));
        m_statusPanel.setPreferredSize(new Dimension(SMALL_PANEL_WIDTH + 18, PANEL_HEIGHT));
        m_statusPanel.setMaximumSize(new Dimension(SMALL_PANEL_WIDTH + 18, PANEL_HEIGHT));
        m_statusPanel.add(new JLabel(" "));
        m_statusPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        m_statusPanel.add(m_cancelStatusButton);
        m_statusPanel.add(m_waitEndStatusButton);
        m_statusPanel.add(m_finishedStatusButton);
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
    	getAssignedPanel();
    	getStartedPanel();
    	getCompletedPanel();
    	getChangeTasksDialog();
    	getMessageListPanel();
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
		// No need to update if current message is selected
		if(messageNr == m_currentMessageNr)
		{
			return;
		}

		// Have user confirm message overwrite
		if(m_messageDirty)
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
		m_currentMessageNr = messageNr;
		m_newMessage = false;

		// Get the message
		List<IMessageIf> messages = m_messageLog.selectItems(m_currentMessageSelector, m_lineNumberComparator);
		if(!messages.isEmpty())
		{
			// Check that an uncommitted message is not reselected
			m_currentMessage = messages.get(0);
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
				sender = (ICommunicatorIf)m_wpMessageLog.getMsoManager().getCmdPost();
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

	private final Selector<IMessageIf> m_currentMessageSelector = new Selector<IMessageIf>()
    {
        public boolean select(IMessageIf aMessage)
        {
        	if(aMessage.getNumber() == m_currentMessageNr)
        	{
        		return true;
        	}
        	else
        	{
        		return false;
        	}
        }
    };

    private final static Comparator<IMessageIf> m_lineNumberComparator = new Comparator<IMessageIf>()
    {
        public int compare(IMessageIf m1, IMessageIf m2)
        {
            return m1.getNumber() - m2.getNumber();
        }
    };

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

	private boolean validMessage()
	{
		// TODO Auto-generated method stub
		// TODO data should be valid already, perhaps not needed?
		return (m_currentMessage != null);
	}

	/**
	 * Gets new data from a dialog once a dialog finishes
	 */
	public void dialogFinished(DialogEvent e)
	{
		// If no message is selected a new one should be created once a field is edited
		if(m_currentMessage == null)
		{
			getCurrentMessage();
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
					getCurrentMessage();
					// Commit message, set status to postponed
					m_currentMessage.setStatus(MessageStatus.POSTPONED);

					m_wpMessageLog.getMsoManager().commit();

					m_messageDirty = false;

					for(IEditMessageComponentIf dialog : m_editComponents)
					{
						dialog.hideComponent();
					}

					m_buttonGroup.clearSelection();
					clearPanelContents();
					m_currentMessageNr = 0;
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
				public void actionPerformed(ActionEvent arg0)
				{
					if(validMessage())
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

						clearPanelContents();

						// Commit changes
						m_wpMessageLog.getMsoManager().commit();

						m_currentMessage = null;
						m_messageDirty = false;
						m_currentMessageNr = 0;

						for(IEditMessageComponentIf dialog : m_editComponents)
						{
							dialog.hideComponent();
						}

						m_buttonGroup.clearSelection();
					}
					else
					{
						ErrorDialog error = new ErrorDialog(m_wpMessageLog.getApplication().getFrame());
						error.showError(m_wpMessageLog.getText("MessageNotValidError.header"),
								"MessageNotValidError.details");
					}
				}
    		});
    	}
    	return m_finishedStatusButton;
    }

    private JToggleButton createChangeDtgButton()
    {
    	m_changeDTGButton = createChangeButton();
    	m_changeDTGButton.addActionListener(new ActionListener()
    	{
    		// Display the change DTG dialog when button is pressed
    		public void actionPerformed(ActionEvent e)
    		{
    			hideEditPanels();
    			getChangeDTGDialog();
    			Point location = m_changeDTGButton.getLocationOnScreen();
    			location.y -= m_changeDTGDialog.getHeight();
    			m_changeDTGDialog.setLocation(location);
    			m_changeDTGDialog.showComponent();
    		}
    	});
    	m_buttonGroup.add(m_changeDTGButton);
    	return m_changeDTGButton;
    }

    private JToggleButton  createChangeTasksButton()
    {
    	if(m_changeTasksButton == null)
    	{
    		m_changeTasksButton = createChangeButton();
    		m_changeTasksButton.addActionListener(new ActionListener()
    		{
				public void actionPerformed(ActionEvent e)
				{
					// Create new message if null
					getCurrentMessage();
					getChangeTasksDialog();
					hideEditPanels();
					Point location = m_changeTasksButton.getLocationOnScreen();
	    			location.y += m_changeTasksButton.getHeight();
	    			location.x -= DiskoButtonFactory.LARGE_BUTTON_SIZE.width;
	    			m_changeTasksDialog.setLocation(location);
					m_changeTasksDialog.showComponent();
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
    		m_changeFromButton = createChangeButton();
    		m_changeFromButton.addActionListener(new ActionListener()
    		{
				public void actionPerformed(ActionEvent arg0)
				{
					getFieldChangeFromDialog();
					hideEditPanels();
					Point location = m_changeDTGButton.getLocationOnScreen();
	    			location.y -= m_fieldFromDialog.getHeight();
	    			m_fieldFromDialog.setLocation(location);
					m_fieldFromDialog.showComponent();

					getListChangeFromDialog();
					location = m_changeDTGButton.getLocationOnScreen();
					location.y += MessageLogPanel.SMALL_BUTTON_SIZE.height;
					m_listFromDialog.setLocation(location);
					m_listFromDialog.showComponent();

					// Register listeners
					m_fieldFromDialog.addActionListener(m_listFromDialog);
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
    		m_changeToButton = createChangeButton();
    		m_changeToButton.addActionListener(new ActionListener()
    		{
				public void actionPerformed(ActionEvent e)
				{
					getChangeToDialog();
					hideEditPanels();
					Point location = m_changeDTGButton.getLocationOnScreen();
					location.y += MessageLogPanel.SMALL_BUTTON_SIZE.height;
					m_changeToDialog.setLocation(location);
					m_changeToDialog.showComponent();
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
    	m_deleteButton = DiskoButtonFactory.createSmallButton(m_wpMessageLog.getText("DeleteButton.text"),
    			m_wpMessageLog.getText("DeleteButton.icon"));
		m_deleteButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// Delete if message is not committed, don't create new message when pressing delete
				if(m_currentMessage != null && m_newMessage)
				{
					IMessageLineIf line = m_currentMessage.findMessageLine(m_currentMessageLineType, null, false);
					if(line != null)
					{
						// If line that has other assignment lines depending on it is deleted, delete them as well
						switch(line.getLineType())
						{
						case ASSIGNED:
							IMessageLineIf startedLine = m_currentMessage.findMessageLine(MessageLineType.STARTED, null, false);
							if(startedLine != null)
							{
								startedLine.deleteObject();
//								TODO m_messageStartedDialog.lineRemoved();
							}
						case STARTED:
							IMessageLineIf completeLine = m_currentMessage.findMessageLine(MessageLineType.COMPLETE, null, false);
							if(completeLine != null)
							{
								completeLine.deleteObject();
//								TODO m_messageCompletedDialog.lineRemoved();
							}
						}

						if(!line.deleteObject())
						{
							System.err.println("Couldn't delete message line");
						}
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
		m_listButton = DiskoButtonFactory.createSmallToggleButton(m_wpMessageLog.getText("ListButton.text"),
				m_wpMessageLog.getText("ListButton.icon"));
		m_listButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				m_messagePanelTopLabel.setText(m_wpMessageLog.getText("MessagePanelListLabel.text"));
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
		m_completedButton = DiskoButtonFactory.createSmallToggleButton(m_wpMessageLog.getText("CompletedButton.text")/*,
				m_wpMessageLog.getText("CompletedButton.icon")*/);
		m_completedButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ErrorDialog errorDialog = Utils.getErrorDialog();
				hideEditPanels();

				if(getCurrentMessage().isBroadcast())
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
					m_messagePanelTopLabel.setText(m_wpMessageLog.getText("MessagePanelCompletedLabel.text"));
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
		m_startButton = DiskoButtonFactory.createSmallToggleButton(m_wpMessageLog.getText("StartedButton.text")/*,
				m_wpMessageLog.getText("StartedButton.icon")*/);
		m_startButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ErrorDialog errorDialog = Utils.getErrorDialog();
				hideEditPanels();

				if(getCurrentMessage().isBroadcast())
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
					m_messagePanelTopLabel.setText(m_wpMessageLog.getText("MessagePanelStartedLabel.text"));
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
		m_assignButton = DiskoButtonFactory.createSmallToggleButton(m_wpMessageLog.getText("AssignedButton.text")/*,
				m_wpMessageLog.getText("AssignedButton.icon")*/);
		m_assignButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ErrorDialog errorDialog = Utils.getErrorDialog();
				hideEditPanels();

				if(getCurrentMessage().isBroadcast())
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
					m_messagePanelTopLabel.setText(m_wpMessageLog.getText("MessagePanelAssignedLabel.text"));
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
		ICommunicatorIf receiver = getCurrentMessage().getSingleReceiver();
		if(receiver != null)
		{
			if(receiver instanceof ICmdPostIf)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			// Receiver is, by default, command post
			return true;
		}
	}

	private boolean isAssignmentOperationLegal()
	{
		if(m_newMessage)
		{
			return true;
		}

		MessageStatus status = getCurrentMessage().getStatus();
		return (status == MessageStatus.UNCONFIRMED || status == MessageStatus.POSTPONED);
	}

	/**
	 * Ensures that units and assignments affected by the added message lines in the current message are
	 * updated (status, etc.). Message lines are also updated according to what operations are legal
	 */
	private void updateAssignments()
	{
		
		IMessageIf message = MessageLogTopPanel.getCurrentMessage();
		ICommunicatorIf communicator = message.getSingleReceiver();
		IUnitIf unit = (IUnitIf)communicator;
		

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

	private void createFindingButton()
	{
		m_findingButton = DiskoButtonFactory.createSmallToggleButton(m_wpMessageLog.getText("FindingButton.text"),
				m_wpMessageLog.getText("FindingButton.icon"));
		m_findingButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				m_messagePanelTopLabel.setText(m_wpMessageLog.getText("MessagePanelFindingLabel.text"));
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
		m_positionButton = DiskoButtonFactory.createSmallToggleButton(m_wpMessageLog.getText("PositionButton.text"),
				m_wpMessageLog.getText("PositionButton.icon"));
		m_positionButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				m_messagePanelTopLabel.setText(m_wpMessageLog.getText("MessagePanelPositionLabel.text"));
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
		m_textButton = DiskoButtonFactory.createSmallToggleButton(m_wpMessageLog.getText("TextButton.text"),
				m_wpMessageLog.getText("TextButton.icon"));
		m_textButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				m_messagePanelTopLabel.setText(m_wpMessageLog.getText("MessagePanelTextLabel.text"));
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
		return m_messageDirty;
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

		m_wpMessageLog.getMsoManager().rollback();

		m_currentMessage = null;
		m_messageDirty = false;
		m_currentMessageNr = 0;

		for(IEditMessageComponentIf dialog : m_editComponents)
		{
			dialog.hideComponent();
		}

		m_buttonGroup.clearSelection();

		clearPanelContents();
	}
}