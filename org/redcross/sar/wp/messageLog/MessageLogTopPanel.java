/*
 * Author: Thomas Landvik
 * Created: 27.06.07
 */
package org.redcross.sar.wp.messageLog;

import no.cmr.view.JOptionPaneExt;
import org.redcross.sar.app.Utils;
import org.redcross.sar.event.DialogEvent;
import org.redcross.sar.event.IDialogEventListener;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.ErrorDialog;
import org.redcross.sar.map.POITool;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.*;
import org.redcross.sar.mso.data.IMessageIf.MessageStatus;
import org.redcross.sar.mso.data.IMessageLineIf.MessageLineType;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.Update;
import org.redcross.sar.util.except.IllegalMsoArgumentException;
import org.redcross.sar.util.mso.DTG;
import org.redcross.sar.util.mso.Selector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

public class MessageLogTopPanel extends JPanel implements IMsoUpdateListenerIf, IDialogEventListener
{
	public final static int PANEL_HEIGHT = (MessageLogPanel.SMALL_BUTTON_SIZE.height) * 3 + 20;
	public final static int SMALL_PANEL_WIDTH = 60;

	IMessageLogIf m_messageLog;
	private int m_currentMessageNr;
	private IMessageIf m_currentMessage;
	private boolean m_newMessage;
	private IDiskoWpMessageLog m_wpMessageLog;

	private boolean m_messageDirty = false;

	List<IEditMessageDialogIf> m_dialogs;
	
	private ButtonGroup m_buttonGroup;

	private JPanel m_nrPanel;
	private JLabel m_nrLabel;

	private JPanel m_dtgPanel;
	private JLabel m_dtgLabel;
	private ChangeDTGDialog m_changeDTGDialog;
    private JToggleButton m_changeDTGButton;

    private JPanel m_fromPanel;
    private JLabel m_fromLabel;
    private UnitFieldSelectionDialog m_fieldFromDialog;
    private SingleUnitListSelectionDialog m_listFromDialog;
    private JToggleButton  m_changeFromButton;

    private JPanel m_toPanel;
    private JLabel m_toLabel;
    private ChangeToDialog m_changeToDialog;
    private JToggleButton  m_changeToButton;

    private JPanel m_messagePanel;
    private JLabel m_messagePanelTopLabel;
    private JComponent m_dialogArea;
    private JPanel m_buttonRow;
	private JToggleButton  m_textButton;
	private MessageTextDialog m_messageTextDialog;
	private JToggleButton  m_positionButton;
	private MessagePositionDialog m_messagePositionDialog;
	private JToggleButton  m_findingButton;
	private MessageFindingDialog m_messageFindingDialog;
	private JToggleButton  m_assignedButton;
	private MessageAssignedDialog m_messageAssignedDialog;
	private JToggleButton  m_startedButton;
	private MessageStartedDialog m_messageStartedDialog;
	private JToggleButton  m_completedButton;
	private MessageCompletedDialog m_messageCompletedDialog;
	private JToggleButton  m_listButton;
	private MessageListDialog m_messageListDialog;
	private JToggleButton  m_deleteButton;
	private MessageDeleteDialog m_messageDeleteDialog;

    private JPanel m_taskPanel;
    private JLabel m_taskLabel;
    private ChangeTaskDialog m_changeTaskDialog;
    private JToggleButton  m_changeTaskButton;

    private JPanel m_statusPanel;
    private JButton m_cancelStatusButton;
    private JButton m_waitEndStatusButton;
    private JButton m_finishedStatusButton;

    public MessageLogTopPanel(IMessageLogIf messageLog)
    {
    	m_messageLog = messageLog;

    	m_newMessage = true;
    	m_currentMessageNr = 0;

    	m_dialogs = new LinkedList<IEditMessageDialogIf>();
    }

    /**
	 * Initialize GUI components
	 */
	public void initialize()
	{
		initButtons();
    	initPanels();
    	initDialogs();
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

    private JToggleButton  createChangeButton()
    {
    	JToggleButton  button = new JToggleButton ();
    	try
		{
			button.setIcon(Utils.createImageIcon("icons/60x60/change.gif", "Change"));
		}
    	catch (Exception e)
		{
			e.printStackTrace();
		}
    	button.setMaximumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
        button.setMinimumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
        button.setPreferredSize(MessageLogPanel.SMALL_BUTTON_SIZE);
        button.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        return button;
    }

    private ChangeDTGDialog getChangeDTGDialog()
    {
    	if(m_changeDTGDialog == null)
    	{
    		m_changeDTGDialog = new ChangeDTGDialog(m_wpMessageLog);
    		m_changeDTGDialog.addDialogListener(this);
    		m_dialogs.add(m_changeDTGDialog);
    	}
    	return m_changeDTGDialog;
    }

    private UnitFieldSelectionDialog getFieldChangeFromDialog()
    {
    	if(m_fieldFromDialog == null)
    	{
    		m_fieldFromDialog = new UnitFieldSelectionDialog(m_wpMessageLog, true);
    		m_fieldFromDialog.addDialogListener(this);
    		m_dialogs.add(m_fieldFromDialog);
    	}

    	return m_fieldFromDialog;
    }

    private SingleUnitListSelectionDialog getListChangeFromDialog()
    {
    	if(m_listFromDialog == null)
    	{
    		m_listFromDialog = new SingleUnitListSelectionDialog(m_wpMessageLog, true);
    		m_listFromDialog.addDialogListener(this);
    		m_dialogs.add(m_listFromDialog);
    	}
    	return m_listFromDialog;
    }

    private ChangeToDialog getChangeToDialog()
	{
		if(m_changeToDialog == null)
		{
			m_changeToDialog = new ChangeToDialog(m_wpMessageLog);
			m_changeToDialog.addDialogListener(this);
			m_dialogs.add(m_changeToDialog);
		}
		return m_changeToDialog;
	}

    private MessageTextDialog getMessageTextDialog()
    {
    	if(m_messageTextDialog == null)
    	{
    		m_messageTextDialog = new MessageTextDialog(m_wpMessageLog);
    		m_messageTextDialog.addDialogListener(this);
    		m_dialogs.add(m_messageTextDialog);
    	}
    	return m_messageTextDialog;
    }

    private MessagePositionDialog getMessagePositionDialog()
    {
    	if(m_messagePositionDialog == null)
    	{
    		//TODO
    		POITool tool = null;
			try
			{
				tool = new POITool(m_wpMessageLog.getApplication());
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		m_messagePositionDialog = new MessagePositionDialog(m_wpMessageLog, tool);
    		m_messagePositionDialog.addDialogListener(this);
    		m_dialogs.add(m_messagePositionDialog);
    	}
    	return m_messagePositionDialog;
    }

    private MessageListDialog getMessageListDialog()
    {
    	if(m_messageListDialog == null)
    	{
    		m_messageListDialog = new MessageListDialog(m_wpMessageLog);
    		m_messageListDialog.addDialogListener(this);
    		m_dialogs.add(m_messageListDialog);
    	}
    	return m_messageListDialog;
    }

    private ChangeTaskDialog getChangeTaskDialog()
	{
		if(m_changeTaskDialog == null)
		{
			m_changeTaskDialog = new ChangeTaskDialog(m_wpMessageLog);
			m_changeTaskDialog.addDialogListener(this);
			m_dialogs.add(m_changeTaskDialog);
		}
		return m_changeTaskDialog;
	}

    protected void clearPanelContents()
	{
    	m_nrLabel.setText("");
		m_dtgLabel.setText("");
		m_fromLabel.setText("");
		m_toLabel.setText("");
		m_taskLabel.setText("");
	}

	protected void clearDialogContents()
	{
		for(IEditMessageDialogIf dialog : m_dialogs)
		{
			dialog.clearContents();
		}
	}

    public void initPanels()
    {
    	this.setLayout(new GridBagLayout());
    	GridBagConstraints gbc = new GridBagConstraints();
    	gbc.fill = GridBagConstraints.BOTH;
    	gbc.weightx = 0.0;
    	gbc.weighty = 1.0;
    	gbc.gridx = 0;
    	gbc.gridy = 0;

    	// Nr panel
        m_nrPanel = createPanel(SMALL_PANEL_WIDTH/2, PANEL_HEIGHT, m_wpMessageLog.getText("MessagePanelNrLabel.text"));
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
        m_dialogArea = (JComponent)Box.createGlue();
        m_dialogArea.setPreferredSize(new Dimension(600, 120));
        m_dialogArea.setAlignmentX(0.0f);
        m_messagePanel.add(m_dialogArea);
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
        m_taskPanel.add(m_changeTaskButton);
        gbc.gridx++;
        this.add(m_taskPanel, gbc);
        gbc.gridx++;
        this.add(new JSeparator(SwingConstants.VERTICAL), gbc);

        // Status panel
        m_statusPanel = new JPanel();
        m_statusPanel.setLayout(new BoxLayout(m_statusPanel, BoxLayout.Y_AXIS));
        m_statusPanel.setMinimumSize(new Dimension(SMALL_PANEL_WIDTH, PANEL_HEIGHT));
        m_statusPanel.setPreferredSize(new Dimension(SMALL_PANEL_WIDTH, PANEL_HEIGHT));
        m_statusPanel.setMaximumSize(new Dimension(SMALL_PANEL_WIDTH, PANEL_HEIGHT));
        m_statusPanel.add(new JLabel(" "));
        m_statusPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        m_statusPanel.add(Box.createVerticalGlue());
        m_statusPanel.add(m_cancelStatusButton);
        m_statusPanel.add(m_waitEndStatusButton);
        m_statusPanel.add(m_finishedStatusButton);
        gbc.gridx++;
        this.add(m_statusPanel, gbc);
    }

    public void initDialogs()
    {
    	getChangeDTGDialog();
    	getFieldChangeFromDialog();
    	getListChangeFromDialog();
    	getChangeToDialog();
    	getChangeTaskDialog();
    	getMessageTextDialog();
    	getMessagePositionDialog();

    	getMessageListDialog();

    }

    public void initButtons()
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
        createChangeTaskButton();
        createCancleButton();
        createWaitEndButton();
        createFinishedButton();
    }

    /**
     * An existing message is selected in the message log for editing.
     * @param messageNr
     */
	public void newMessageSelected(int messageNr)
	{
		// Have user confirm message overwrite
		if(m_messageDirty)
		{

			int n = JOptionPaneExt.showConfirmDialog(this, m_wpMessageLog.getText("MessageDirtySaveWarning.text"),
					m_wpMessageLog.getText("MessageDirtySaveHeader.text"), true, JOptionPane.YES_NO_OPTION);
			if(n == JOptionPane.NO_OPTION)
			{
				return;
			}
			else
			{
				// Roll back any changes made to the message
				// TODO sjekk med vinjar/stian
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
		m_currentMessage = messages.get(0);

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
			IMsoListIf<ICommunicatorIf> receivers = m_currentMessage.getConfirmedReceivers();
			if(receivers.size() == 0)
			{
				ICommunicatorIf defaultReciever = (ICommunicatorIf)m_wpMessageLog.getMsoManager().getCmdPost();
				m_toLabel.setText(defaultReciever.getCommunicatorNumberPrefix() + " " +
						defaultReciever.getCommunicatorNumber());
			}
			else
			{
				if(m_currentMessage.isBroadcast())
				{
					m_toLabel.setText(m_wpMessageLog.getText("BroadcastLabel.text"));
				}
				else
				{
					ICommunicatorIf receiver = m_currentMessage.getSingleReceiver();
					m_toLabel.setText(receiver.getCommunicatorNumberPrefix() + " " + receiver.getCommunicatorNumber());
				}
			}

			ITaskListIf tasks = m_currentMessage.getMessageTasks();
			StringBuilder tasksString = new StringBuilder();
			for(ITaskIf task : tasks.getItems())
			{
				tasksString.append(task.toString() + "\n");
			}
			m_taskLabel.setText(tasksString.toString());

			// Update dialogs
			for(int i=0; i<m_dialogs.size(); i++)
			{
				m_dialogs.get(i).newMessageSelected(m_currentMessage);
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

	public void setWp(IDiskoWpMessageLog wp)
	{
		m_wpMessageLog = wp;
	}

	public void handleMsoUpdateEvent(Update e)
	{
		// TODO update dialogs or warnings?
		updateMessageGUI();
	}

	private final EnumSet<IMsoManagerIf.MsoClassCode> myInterests = EnumSet.of(IMsoManagerIf.MsoClassCode.CLASSCODE_MESSAGE);

	public boolean hasInterestIn(IMsoObjectIf msoObject)
	{
		 return myInterests.contains(msoObject.getMsoClassCode());
	}

	public void dialogCanceled(DialogEvent e)
	{
		Object sender = e.getSource();
		
		m_buttonGroup.clearSelection();

		if(sender.getClass() == SingleUnitListSelectionDialog.class)
		{
		}
		if(sender.getClass() == ChangeDTGDialog.class)
		{
			m_changeDTGDialog.hideDialog();
		}
	}

	private boolean validMessage()
	{
		// TODO Auto-generated method stub
		// TODO data should be valid already, perhaps not needed?
		return true;
	}

	/**
	 * Gets new data from a dialog once a dialog finishes
	 */
	public void dialogFinished(DialogEvent e)
	{
		Object source = e.getSource();
		
		m_buttonGroup.clearSelection();
		
		// If no message is selected a new one should be created once a field is edited
		if(m_currentMessage == null)
		{
			m_currentMessage = m_wpMessageLog.getMsoManager().createMessage();
			m_newMessage = true;
		}
		
		if(source instanceof ChangeDTGDialog)
		{
			// Check validity of DTG
			try
			{
				m_currentMessage.setOccuredTime(DTG.DTGToCal(m_changeDTGDialog.getTime()));
				//m_dtgLabel.setText(m_changeDTGDialog.getTime());
			}
			catch (IllegalMsoArgumentException e1)
			{
				System.err.println("Not a valid DTG format");
			}
		}
		else if(source instanceof UnitFieldSelectionDialog ||
				source instanceof SingleUnitListSelectionDialog)
		{
			m_fieldFromDialog.hideDialog();
			m_listFromDialog.hideDialog();
			
			ICommunicatorIf sender = null;
			if(m_fieldFromDialog.getText().isEmpty())
			{
				sender = (ICommunicatorIf)m_wpMessageLog.getMsoManager().getCmdPost();
				//m_fieldFromDialog.setCommunicatorNumberPrefix(cp.getCommunicatorNumberPrefix());
				//m_fieldFromDialog.setCommunicatorNumber(cp.getCommunicatorNumber());
			}
			else
			{
				sender = m_fieldFromDialog.getCommunicator();
			}
			//m_fromLabel.setText(m_fieldFromDialog.getText());
			m_currentMessage.setSender(sender);
		}
		else if(source instanceof ChangeToDialog)
		{
			// set receiver(s) in current message
			List<ICommunicatorIf> communicators = m_changeToDialog.getCommunicators();
			if(communicators.size() == 1)
			{
				// Not broadcast
				m_currentMessage.setSingleReceiver(communicators.get(0));
			}
			else
			{
				m_currentMessage.setBroadcast(true);
				// TODO
			}
			m_changeToDialog.hideDialog();
		}
		else if(source instanceof MessageTextDialog)
		{
			IMessageLineIf textLine = m_currentMessage.findMessageLine(MessageLineType.TEXT, true);
			textLine.setText(m_messageTextDialog.getText());
		}

		updateMessageGUI();
		m_messageDirty = true;
	}

	public void dialogStateChanged(DialogEvent e)
	{
		Object source = e.getSource();
		if(source instanceof ChangeToDialog)
		{
			// TODO create new message if single/broadcast is selected?
			if(m_currentMessage == null)
			{
				m_newMessage = true;
				m_messageDirty = true;
				m_currentMessage = m_wpMessageLog.getMsoManager().createMessage();
			}
			// Toggle broadcast
			m_currentMessage.setBroadcast(m_changeToDialog.getBroadcast());
		}
	}

	private JButton createWaitEndButton()
    {
    	if(m_waitEndStatusButton == null)
    	{
    		m_waitEndStatusButton = new JButton();
    		//String iconPath = "icons/60x60/waitend.gif";
    		//m_waitEndStatusButton.setIcon(createImageIcon(iconPath));
    		m_waitEndStatusButton.addActionListener(new ActionListener()
    		{
				public void actionPerformed(ActionEvent arg0)
				{
					// Commit message, set status to postponed
					m_currentMessage.setStatus(MessageStatus.POSTPONED);
					m_wpMessageLog.getMsoManager().commit();
				}
    		});
    		m_waitEndStatusButton.setMinimumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
    		m_waitEndStatusButton.setPreferredSize(MessageLogPanel.SMALL_BUTTON_SIZE);
    		m_waitEndStatusButton.setMaximumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
    	}
    	return m_waitEndStatusButton;
    }

    private JButton createFinishedButton()
    {
    	if(m_finishedStatusButton == null)
    	{
    		m_finishedStatusButton = new JButton();
    		String iconPath = "icons/60x60/finish.gif";
    		try
			{
				m_finishedStatusButton.setIcon(Utils.createImageIcon(iconPath, "Finished"));
			}
    		catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		m_finishedStatusButton.addActionListener(new ActionListener()
    		{
				public void actionPerformed(ActionEvent arg0)
				{
					if(validMessage())
					{
						// Set message status
						if(m_currentMessage.isBroadcast())
						{
							//TODO check all units have confirmed
						}
						else
						{
							m_currentMessage.setStatus(MessageStatus.CONFIRMED);
						}
						
						clearPanelContents();
						clearDialogContents();
						
						// Commit changes
						m_wpMessageLog.getMsoManager().commit();

						m_currentMessage = null;
						m_messageDirty = false;
					}
					else
					{
						ErrorDialog error = new ErrorDialog(m_wpMessageLog.getApplication().getFrame());
						error.showError("Message not valid", "");
					}
				}
    		});
    		m_finishedStatusButton.setPreferredSize(MessageLogPanel.SMALL_BUTTON_SIZE);
    		m_finishedStatusButton.setMaximumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
    	}
    	return m_finishedStatusButton;
    }

    private JToggleButton  createChangeDtgButton()
    {
    	m_changeDTGButton = createChangeButton();
    	m_changeDTGButton.addActionListener(new ActionListener()
    	{
    		// Display the change DTG dialog when button is pressed
    		public void actionPerformed(ActionEvent e)
    		{
    			hideDialogs();
    			getChangeDTGDialog();
    			Point location = m_changeDTGButton.getLocationOnScreen();
    			location.y -= m_changeDTGDialog.getHeight();
    			m_changeDTGDialog.setLocation(location);
    			m_changeDTGDialog.setVisible(true);
    		}
    	});
    	m_buttonGroup.add(m_changeDTGButton);
    	return m_changeDTGButton;
    }

    private JToggleButton  createChangeTaskButton()
    {
    	if(m_changeTaskButton == null)
    	{
    		m_changeTaskButton = createChangeButton();
    		m_changeTaskButton.addActionListener(new ActionListener()
    		{
				public void actionPerformed(ActionEvent e)
				{
					getChangeTaskDialog();
					hideDialogs();
					m_changeTaskDialog.setVisible(true);
				}
    		});
    		m_buttonGroup.add(m_changeTaskButton);
    	}
    	return m_changeTaskButton;
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
					hideDialogs();
					Point location = m_changeDTGButton.getLocationOnScreen();
	    			location.y -= m_fieldFromDialog.getHeight();
	    			m_fieldFromDialog.setLocation(location);
					m_fieldFromDialog.showDialog();

					getListChangeFromDialog();
					location = m_changeDTGButton.getLocationOnScreen();
					location.y += MessageLogPanel.SMALL_BUTTON_SIZE.height;
					m_listFromDialog.setLocation(location);
					m_listFromDialog.showDialog();

					// Register listeners
					m_fieldFromDialog.addActionListener(m_listFromDialog);
					m_listFromDialog.addActionListener(m_fieldFromDialog);
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
					hideDialogs();
					Point location = m_changeDTGButton.getLocationOnScreen();
					location.y += MessageLogPanel.SMALL_BUTTON_SIZE.height;
					m_changeToDialog.setLocation(location);
					m_changeToDialog.showDialog();
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
    		m_cancelStatusButton = new JButton();
    		String iconPath = "icons/60x60/abort.gif";
    		try
			{
				m_cancelStatusButton.setIcon(Utils.createImageIcon(iconPath, "Cancel status"));
			}
    		catch (Exception e)
			{
				e.printStackTrace();
			}
    		m_cancelStatusButton.setMinimumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
    		m_cancelStatusButton.setPreferredSize(MessageLogPanel.SMALL_BUTTON_SIZE);
    		m_cancelStatusButton.setMaximumSize(MessageLogPanel.SMALL_BUTTON_SIZE);

    		m_cancelStatusButton.addActionListener(new ActionListener()
    		{
				public void actionPerformed(ActionEvent e)
				{
					// Remove current message from panel
					clearPanelContents();
					clearDialogContents();

					m_wpMessageLog.getMsoManager().rollback(); // TODO sjekk om korrekt?
					
					m_currentMessage = null;
					m_messageDirty = false;
				}
    		});
    	}
    	return m_cancelStatusButton;
    }

    private void createDeleteButton()
	{
    	m_deleteButton = createButton("DELETE", "icons/60x60/delete.gif");
		m_deleteButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				m_messagePanelTopLabel.setText(m_wpMessageLog.getText("MessagePanelDeleteLabel.text"));
			}
		});
		m_buttonGroup.add(m_deleteButton);
		m_buttonRow.add(m_deleteButton);
	}

	private void createListButton()
	{
		m_listButton = createButton("LIST", "icons/60x60/list.gif");
		m_listButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				m_messagePanelTopLabel.setText(m_wpMessageLog.getText("MessagePanelListLabel.text"));
				getMessageListDialog();

				hideDialogs();
				positionDialogInArea(m_messageListDialog);
				m_messageListDialog.setVisible(true);
			}

		});
		m_buttonGroup.add(m_listButton);
		m_buttonRow.add(m_listButton);
	}

	private void createCompletedButton()
	{
		m_completedButton = createButton("COMPLETED", null);
		m_completedButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				m_messagePanelTopLabel.setText(m_wpMessageLog.getText("MessagePanelCompletedLabel.text"));

			}
		});
		m_buttonGroup.add(m_completedButton);
		m_buttonRow.add(m_completedButton);
	}

	private void createStartedButton()
	{
		m_startedButton = createButton("STARTED", null);
		m_startedButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				m_messagePanelTopLabel.setText(m_wpMessageLog.getText("MessagePanelStartedLabel.text"));
			}
		});
		m_buttonGroup.add(m_startedButton);
		m_buttonRow.add(m_startedButton);
	}

	private void createAssignedButton()
	{
		m_assignedButton = createButton("ASSIGNED", null);
		m_assignedButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				m_messagePanelTopLabel.setText(m_wpMessageLog.getText("MessagePanelAssignedLabel.text"));
			}
		});
		m_buttonGroup.add(m_assignedButton);
		m_buttonRow.add(m_assignedButton);
	}

	private void createFindingButton()
	{
		m_findingButton = createButton("FINDING", "icons/60x60/discovery.gif");
		m_findingButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				m_messagePanelTopLabel.setText(m_wpMessageLog.getText("MessagePanelFindingLabel.text"));
			}
		});
		m_buttonGroup.add(m_findingButton);
		m_buttonRow.add(m_findingButton);
	}

	private void createPositionButton()
	{
		m_positionButton = createButton("POI", null);
		m_positionButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				m_messagePanelTopLabel.setText(m_wpMessageLog.getText("MessagePanelPositionLabel.text"));
				getMessagePositionDialog();
				hideDialogs();
				m_messagePositionDialog.setVisible(true);
				positionDialogInArea(m_messagePositionDialog);
			}
		});
		m_buttonGroup.add(m_positionButton);
		m_buttonRow.add(m_positionButton);
	}

	private void createTextButton()
	{
		m_textButton = createButton("TEXT", "icons/60x60/text.gif");
		m_textButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				m_messagePanelTopLabel.setText(m_wpMessageLog.getText("MessagePanelTextLabel.text"));
				getMessageTextDialog();
				hideDialogs();
    			m_messageTextDialog.setVisible(true);
    			positionDialogInArea(m_messageTextDialog);
			}
		});
		m_buttonGroup.add(m_textButton);
		m_buttonRow.add(m_textButton);
	}

	private void positionDialogInArea(DiskoDialog dialog)
	{
		Point location = m_dialogArea.getLocationOnScreen();
		Dimension dimension = m_dialogArea.getSize();
		dialog.setLocation(location);
		dialog.setSize(dimension);
	}

	/**
	 * Hides all dialogs in panel
	 */
	public void hideDialogs()
	{
		for(int i=0; i<m_dialogs.size(); i++)
		{
			m_dialogs.get(i).hideDialog();
		}
	}

	private JToggleButton  createButton(String text, String iconPath)
	{
		JToggleButton  button = new JToggleButton ();
		if(iconPath != null)
		{
			try
			{
				button.setIcon(Utils.createImageIcon(iconPath, text));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			button.setText(text);
		}
		button.setMinimumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		button.setPreferredSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		button.setMaximumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		//button.setActionCommand(text);
		return button;
	}
}