/*
 * Author: Thomas Landvik
 * Created: 27.06.07
 */
package org.redcross.sar.wp.messageLog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import no.cmr.view.JOptionPaneExt;

import org.redcross.sar.app.Utils;
import org.redcross.sar.event.DialogEvent;
import org.redcross.sar.event.IDialogEventListener;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLogIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.Update;
import org.redcross.sar.util.except.IllegalMsoArgumentException;
import org.redcross.sar.util.mso.DTG;
import org.redcross.sar.util.mso.Selector;

import com.sun.corba.se.impl.javax.rmi.CORBA.Util;
import com.sun.xml.internal.messaging.saaj.soap.MessageImpl;

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
	
	List<DiskoDialog> m_dialogs;
	
	private JPanel m_nrPanel;
	private JLabel m_nrLabel;
	
	private JPanel m_dtgPanel;
	private JLabel m_dtgLabel;
	private ChangeDTGDialog m_changeDTGDialog;
    private JButton m_changeDTGButton;
    
    private JPanel m_fromPanel;
    private JLabel m_fromLabel;
    private ChangeFromDialog m_changeFromDialog;
    private JButton m_changeFromButton;
    
    private JPanel m_toPanel;
    private JLabel m_toLabel;
    private ChangeToDialog m_changeToDialog;
    private JButton m_changeToButton;
    
    private JPanel m_messagePanel;
    private JLabel m_messagePanelTopLabel;
    private JComponent m_dialogArea;
    private JPanel m_buttonRow;
	private JButton m_textButton;
	private MessageTextDialog m_messageTextDialog;
	private JButton m_positionButton;
	private MessagePositionDialog m_messagePositionDialog;
	private JButton m_findingButton;
	private MessageFindingDialog m_messageFindingDialog;
	private JButton m_assignedButton;
	private MessageAssignedDialog m_messageAssignedDialog;
	private JButton m_startedButton;
	private MessageStartedDialog m_messageStartedDialog;
	private JButton m_completedButton;
	private MessageCompletedDialog m_messageCompletedDialog;
	private JButton m_listButton;
	private MessageListDialog m_messageListDialog;
	private JButton m_deleteButton;
	private MessageDeleteDialog m_messageDeleteDialog;
    
    private JPanel m_taskPanel;
    private JLabel m_taskLabel;
    private ChangeTaskDialog m_changeTaskDialog;
    private JButton m_changeTaskButton;
    
    private JPanel m_statusPanel;
    private JButton m_cancelStatusButton;
    private JButton m_waitEndStatusButton;
    private JButton m_finishedStatusButton;
    
    public MessageLogTopPanel(IMessageLogIf messageLog)
    {
    	m_messageLog = messageLog;
    	
    	m_newMessage = true;
    	m_currentMessageNr = 0;
    	
    	m_dialogs = new LinkedList<DiskoDialog>();
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

        panel.add(label);
    	return panel;
    }
    
    private JButton createChangeButton()
    {
    	JButton button = new JButton();
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
    
    private ChangeFromDialog getChangeFromDialog()
    {
    	if(m_changeFromDialog == null)
    	{
    		m_changeFromDialog = new ChangeFromDialog(m_wpMessageLog);
    		m_changeFromDialog.addDialogListener(this);
    		m_dialogs.add(m_changeFromDialog);
    	}
    	
    	return m_changeFromDialog;
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
    		m_messagePositionDialog = new MessagePositionDialog(m_wpMessageLog);
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
        m_nrPanel = createPanel(SMALL_PANEL_WIDTH/2, PANEL_HEIGHT, "Nr");
        m_nrPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        m_nrLabel = new JLabel();
        m_nrPanel.add(m_nrLabel);
        m_nrPanel.add(Box.createVerticalGlue());
        m_nrPanel.add(Box.createRigidArea(MessageLogPanel.SMALL_BUTTON_SIZE));
        this.add(m_nrPanel, gbc);
        gbc.gridx++;
        this.add(new JSeparator(SwingConstants.VERTICAL), gbc);
       
        // DTG panel
        m_dtgPanel = createPanel(SMALL_PANEL_WIDTH, PANEL_HEIGHT, "DTG");
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
        m_fromPanel = createPanel(SMALL_PANEL_WIDTH, PANEL_HEIGHT, "Fra");
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
        m_toPanel = createPanel(SMALL_PANEL_WIDTH, PANEL_HEIGHT, "Til");
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
        m_messagePanel.setLayout(new BoxLayout(m_messagePanel, BoxLayout.Y_AXIS));
        m_messagePanelTopLabel = new JLabel("Tekst");
        m_messagePanel.add(m_messagePanelTopLabel);
        m_messagePanel.add(new JSeparator(JSeparator.HORIZONTAL));
        m_dialogArea = (JComponent)Box.createGlue();
        m_dialogArea.setPreferredSize(new Dimension(600, 120));
        m_messagePanel.add(m_dialogArea, Component.CENTER_ALIGNMENT);
        m_buttonRow.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        m_buttonRow.setMaximumSize(new Dimension(SMALL_PANEL_WIDTH*9, (int)MessageLogPanel.SMALL_BUTTON_SIZE.getHeight()));
        m_messagePanel.add(m_buttonRow);
        gbc.gridx++;
        this.add(m_messagePanel, gbc);
        gbc.weightx = 0.0;
        gbc.gridx++;
        this.add(new JSeparator(SwingConstants.VERTICAL), gbc);
        
        // Task panel
        gbc.weightx = 0.0;
        m_taskPanel = createPanel(2*SMALL_PANEL_WIDTH, PANEL_HEIGHT, "Oppgave");
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
    	getChangeFromDialog();
    	getChangeToDialog();
    	getChangeTaskDialog();
    	getMessageTextDialog();
    	getMessagePositionDialog();
    	
    	getMessageListDialog();
    	
    }
    
    public void initButtons()
    {
    	m_buttonRow = new JPanel(new FlowLayout(FlowLayout.LEADING, 4, 0));
    	
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
		if(m_messageDirty)
		{
			int n = JOptionPaneExt.showConfirmDialog(this, "Du er i ferd med å avbryte nåværende melding. Vil du fortsette?",
					"Melding ikkje lagra", true, JOptionPane.YES_NO_OPTION);
			if(n == JOptionPane.NO_OPTION)
			{
				return;
			}
		}
		
		m_currentMessageNr = messageNr;
		
		// Editing an existing message
		m_newMessage = false;
		
		// Get the message
		List<IMessageIf> messages = m_messageLog.selectItems(m_messageSelector, m_lineNumberComparator);
		m_currentMessage = messages.get(0);
		
		updateMessageGUI();
	}
	
	private void updateMessageGUI()
	{
		if(m_currentMessage != null)
		{
			// Update panel contents
			m_nrLabel.setText(Integer.toString(m_currentMessage.getNumber()));
			m_dtgLabel.setText(m_currentMessage.getDTG());
			if(m_changeDTGDialog != null)
			{
				m_changeDTGDialog.newMessageSelected(m_currentMessage);
			}
			//m_fromLabel.setText(message.get); 
			//m_toLabel.setText(message.get); //
			//m_taskLabel.setText(message.get);
			
			// Update dialogs
			m_changeDTGDialog.setCreated(m_currentMessage.getCreated());
			m_changeDTGDialog.setTime(m_currentMessage.getCalendar());
			//m_messageTextDialog.setText(m_currentMessage.get);
			m_messageListDialog.setMessageLines(m_currentMessage.getLines());
		}
	}
	
	private final Selector<IMessageIf> m_messageSelector = new Selector<IMessageIf>()
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

	@Override
	public void handleMsoUpdateEvent(Update e)
	{
		// TODO update dialogs or warnings?
		updateMessageGUI();
	}

	private final EnumSet<IMsoManagerIf.MsoClassCode> myInterests = EnumSet.of(IMsoManagerIf.MsoClassCode.CLASSCODE_MESSAGE);
	
	@Override
	public boolean hasInterestIn(IMsoObjectIf msoObject)
	{
		 return myInterests.contains(msoObject.getMsoClassCode());
	}

	@Override
	public void dialogCanceled(DialogEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dialogFinished(DialogEvent e)
	{
		if(e.getSource().getClass() == ChangeDTGDialog.class)
		{
			if(m_newMessage)
			{
				m_dtgLabel.setText(m_changeDTGDialog.getTime());
			}
			else
			{
				try
				{
					m_currentMessage.setCalendar(DTG.DTGToCal(m_changeDTGDialog.getTime()));
				} 
				catch (IllegalMsoArgumentException e1)
				{
					System.err.println("Error parsing DTG");
					//e1.printStackTrace();
				}
			}
		}
	}

	@Override
	public void dialogStateChanged(DialogEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	
	private JButton createWaitEndButton()
    {
    	if(m_waitEndStatusButton == null)
    	{
    		m_waitEndStatusButton = new JButton();
    		//String iconPath = "icons/60x60/waitend.gif";
    		//m_waitEndStatusButton.setIcon(createImageIcon(iconPath));
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
    		m_finishedStatusButton.setPreferredSize(MessageLogPanel.SMALL_BUTTON_SIZE);
    		m_finishedStatusButton.setMaximumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
    	}
    	return m_finishedStatusButton;
    }
    
    private JButton createChangeDtgButton()
    {
    	m_changeDTGButton = createChangeButton();
    	m_changeDTGButton.addActionListener(new ActionListener()
    	{
    		@Override
    		// Display the change DTG dialog when button is pressed
    		public void actionPerformed(ActionEvent e)
    		{
    			getChangeDTGDialog();
    			Point location = m_changeDTGButton.getLocationOnScreen();
    			location.y -= m_changeDTGDialog.getHeight();
    			m_changeDTGDialog.setLocation(location);
    			m_changeDTGDialog.setVisible(true);
    			
    			// TODO check for notebook mode, show numpad
    		}
    	});
    	return m_changeDTGButton;
    }
    
    private JButton createChangeTaskButton()
    {
    	if(m_changeTaskButton == null)
    	{
    		m_changeTaskButton = createChangeButton();
    		m_changeTaskButton.addActionListener(new ActionListener()
    		{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					getChangeTaskDialog();
					hideDialogs();
					m_changeTaskDialog.setVisible(true);
				}	
    		});
    	}
    	return m_changeTaskButton;
    }
    
    private JButton createChangeFromButton()
    {
    	if(m_changeFromButton == null)
    	{
    		m_changeFromButton = createChangeButton();
    		m_changeFromButton.addActionListener(new ActionListener()
    		{

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					getChangeFromDialog();
					hideDialogs();
					m_changeFromDialog.setVisible(true);
				}
    			
    		});
    	}
    	return m_changeFromButton;
    }
    
    private JButton createChangeToButton()
    {
    	if(m_changeToButton == null)
    	{
    		m_changeToButton = createChangeButton();
    		m_changeToButton.addActionListener(new ActionListener()
    		{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					getChangeToDialog();
					hideDialogs();
					m_changeToDialog.setVisible(true);
				}
    		});
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
				@Override
				public void actionPerformed(ActionEvent e)
				{
					// Remove current message from panel
					clearPanelContents();
					
					// TODO Roll-back changes made to existing messages in log
					
					// Current empty message has not been sent to mso model
					m_newMessage = true;
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

			@Override
			public void actionPerformed(ActionEvent e)
			{
				m_messagePanelTopLabel.setText("Slett");
			}
			
		});
		m_buttonRow.add(m_deleteButton);
	}

	private void createListButton()
	{
		m_listButton = createButton("LIST", "icons/60x60/list.gif");
		m_listButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				m_messagePanelTopLabel.setText("Liste");
				getMessageListDialog();
				
				hideDialogs();
				positionDialogInArea(m_messageListDialog);
				m_messageListDialog.setVisible(true);
			}
			
		});
		m_buttonRow.add(m_listButton);
	}

	private void createCompletedButton()
	{
		m_completedButton = createButton("COMPLETED", null);
		m_completedButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				m_messagePanelTopLabel.setText("Ferdig");
				
			}
			
		});
		m_buttonRow.add(m_completedButton);
	}

	private void createStartedButton()
	{
		m_startedButton = createButton("STARTED", null);
		m_startedButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				m_messagePanelTopLabel.setText("Startet");
			}
			
		});
		m_buttonRow.add(m_startedButton);
	}

	private void createAssignedButton()
	{
		m_assignedButton = createButton("ASSIGNED", null);
		m_assignedButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				m_messagePanelTopLabel.setText("Tildelt");
			}
			
		});
		m_buttonRow.add(m_assignedButton);
	}

	private void createFindingButton()
	{
		m_findingButton = createButton("FINDING", "icons/60x60/discovery.gif");
		m_findingButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				m_messagePanelTopLabel.setText("Funn");
			}	
		});
		m_buttonRow.add(m_findingButton);
	}

	private void createPositionButton()
	{
		m_positionButton = createButton("POI", null);
		m_positionButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				m_messagePanelTopLabel.setText("Posisjon");
			}	
		});
		m_buttonRow.add(m_positionButton);
	}

	private void createTextButton()
	{
		m_textButton = createButton("TEXT", "icons/60x60/text.gif");
		m_textButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				m_messagePanelTopLabel.setText("Tekst");
				getMessageTextDialog();
				hideDialogs();
    			m_messageTextDialog.setVisible(true);
    			positionDialogInArea(m_messageTextDialog);
			}	
		});
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
	private void hideDialogs()
	{
		for(int i=0; i<m_dialogs.size(); i++)
		{
			m_dialogs.get(i).setVisible(false);
		}
	}

	private JButton createButton(String text, String iconPath)
	{
		JButton button = new JButton();
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