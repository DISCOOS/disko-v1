package org.redcross.sar.wp.messageLog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import org.redcross.sar.event.DialogEvent;
import org.redcross.sar.event.IDialogEventListener;
import org.redcross.sar.gui.DiskoButtonFactory;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.data.ICommunicatorIf;
import org.redcross.sar.mso.data.IMessageIf;

/**
 * Provides a dialog for selecting broadcast or non-broadcast receiver. This dialog also handles sub-dialogs 
 * such as field based unit selection, list unit selection and the broadcast dialogs
 * 
 * @author thomasl
 */
public class ChangeToDialog extends DiskoDialog implements IEditMessageComponentIf, IDialogEventListener
{
	private static final long serialVersionUID = 1L;
	
	protected JToggleButton m_broadcastButton;
	protected JToggleButton m_nonBroadcastButton;
	protected ButtonGroup m_buttonGroup;
	protected JPanel m_contentsPanel;
	
	protected UnitFieldSelectionDialog m_nbFieldDialog;
	protected SingleUnitListSelectionDialog m_nbListDialog;
	
	protected BroadcastToDialog m_broadcastDialog;
	
	protected boolean m_broadcast = false;
	
	protected IDiskoWpMessageLog m_wpMessageLog;
	
	/**
	 * @param wp Message log work process reference
	 */
	public ChangeToDialog(IDiskoWpMessageLog wp)
	{
		super(wp.getApplication().getFrame());
		
		m_wpMessageLog = wp;
		
		initialize();
	}

	private void initialize()
	{
		initContentsPanel();
		initButtons();
		initDialogs();
		this.pack();
	}
	
	private void initDialogs()
	{
		m_nbFieldDialog = new UnitFieldSelectionDialog(m_wpMessageLog, false);
		m_nbFieldDialog.addDialogListener(this);
		m_nbFieldDialog.getOKButton().addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				ICommunicatorIf singleReceiver = m_nbFieldDialog.getCommunicator();
				if(singleReceiver != null)
				{
					IMessageIf message = MessageLogBottomPanel.getCurrentMessage(true);
					message.setSingleReceiver(singleReceiver);
				}
				fireDialogFinished();
			}	
		});
		m_nbListDialog = new SingleUnitListSelectionDialog(m_wpMessageLog, false);
		m_broadcastDialog = new BroadcastToDialog(m_wpMessageLog);
		
		m_nbFieldDialog.addDialogListener(this);
		m_nbListDialog.addDialogListener(this);
		m_broadcastDialog.addDialogListener(this);
		
		m_nbListDialog.addActionListener(m_nbFieldDialog);
		m_nbFieldDialog.addActionListener(m_nbListDialog);
	}

	private void initContentsPanel()
	{
		m_contentsPanel = new JPanel();
		m_contentsPanel.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		m_contentsPanel.setLayout(new BoxLayout(m_contentsPanel, BoxLayout.LINE_AXIS));
		m_contentsPanel.setPreferredSize(new Dimension(DiskoButtonFactory.LARGE_BUTTON_SIZE.width*2,
				DiskoButtonFactory.LARGE_BUTTON_SIZE.height));
		this.add(m_contentsPanel);
	}
	
	private void initButtons()
	{
		m_buttonGroup = new ButtonGroup();
		m_nonBroadcastButton = DiskoButtonFactory.createLargeToggleButton(m_wpMessageLog.getText("NonBroadcastButton.text"));
		m_nonBroadcastButton.setHorizontalAlignment(SwingConstants.LEFT);
		m_nonBroadcastButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				m_broadcast = false;
				MessageLogBottomPanel.getCurrentMessage(true).setBroadcast(false);
				
				m_broadcastDialog.hideComponent();
				m_broadcastDialog.clearSelection();
				showNonBroadcast();
			}	
		});
		m_nonBroadcastButton.setSelected(true);
		m_buttonGroup.add(m_nonBroadcastButton);
		m_contentsPanel.add(m_nonBroadcastButton);
		
		m_broadcastButton = DiskoButtonFactory.createLargeToggleButton(m_wpMessageLog.getText("BroadcastButton.text"));
		m_broadcastButton.setHorizontalAlignment(SwingConstants.LEFT);
		m_broadcastButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				m_broadcast = true;
				MessageLogBottomPanel.getCurrentMessage(true).setBroadcast(true);
				
				m_nbFieldDialog.hideComponent();
				m_nbListDialog.hideComponent();
				m_nbListDialog.clearSelection();
				
				showBroadcast();
			}
		});
		m_buttonGroup.add(m_broadcastButton);
		m_contentsPanel.add(m_broadcastButton);
		
		m_contentsPanel.add(Box.createHorizontalGlue());
	}
	
	/**
	 * Hides dialogs
	 */
	public void hideComponent()
	{
		this.setVisible(false);
		m_nbListDialog.hideComponent();
		m_nbFieldDialog.hideComponent();
		m_broadcastDialog.hideComponent();
	}

	/**
	 * Set broadcast mode if message is broadcast message
	 */
	public void newMessageSelected(IMessageIf message)
	{
		m_broadcast = message.isBroadcast();
		if(m_broadcast)
		{
			m_broadcastButton.setSelected(true);
		}
		else
		{
			m_nonBroadcastButton.setSelected(true);
		}
		
		m_nbListDialog.newMessageSelected(message);
		m_nbFieldDialog.newMessageSelected(message);
		m_broadcastDialog.newMessageSelected(message);
	}
	
	public String getCommunicatorText()
	{
		if(m_broadcast)
		{
			return "FA";
		}
		else
		{
			return m_nbFieldDialog.getCommunicatorText();
		}
	}

	/**
	 * If broadcast message broadcast dialogs are shown, else single receiver mode dialogs are shown
	 */
	public void showComponent()
	{
		IMessageIf message = MessageLogBottomPanel.getCurrentMessage(true);
		this.setVisible(true);
		if(message.isBroadcast())
		{
			showBroadcast();
		}
		else
		{
			showNonBroadcast();
		}
	}

	private void showNonBroadcast()
	{
		IMessageIf message = MessageLogBottomPanel.getCurrentMessage(false);
		if(message != null)
		{
			ICommunicatorIf receiver = message.getSingleReceiver();
			if(receiver != null)
			{
				m_nbFieldDialog.setCommunicatorNumber(receiver.getCommunicatorNumber());
				m_nbFieldDialog.setCommunicatorNumberPrefix(receiver.getCommunicatorNumberPrefix());
			}
		}
		
		Point location = m_nonBroadcastButton.getLocationOnScreen();
		location.y -= m_nbFieldDialog.getHeight();
		m_nbFieldDialog.setLocation(location);
		m_nbFieldDialog.showComponent();
		
		location = m_nonBroadcastButton.getLocationOnScreen();
		location.y -= m_nbListDialog.getHeight();
		location.x += m_nbFieldDialog.getWidth();
		m_nbListDialog.setLocation(location);
		m_nbListDialog.showComponent();
	}

	private void showBroadcast()
	{
		Point location = m_nonBroadcastButton.getLocationOnScreen();
		location.y -= m_broadcastDialog.getHeight();
		m_broadcastDialog.setLocation(location);
		m_broadcastDialog.showComponent();
	}
	
	/**
	 * {@link IEditMessageComponentIf#clearContents()}
	 */
	public void clearContents()
	{
		m_nonBroadcastButton.setSelected(true);
		m_broadcastDialog.clearContents();
		m_nbFieldDialog.clearContents();
		m_nbListDialog.clearContents();
	}

	/**
	 * {@link IDialogEventListener#dialogCanceled(DialogEvent)}
	 */
	public void dialogCanceled(DialogEvent e)
	{
		fireDialogCanceled();
	}

	/**
	 * {@link IDialogEventListener#dialogFinished(DialogEvent)}
	 */
	public void dialogFinished(DialogEvent e)
	{
		fireDialogFinished();
	}

	/**
	 * {@link IDialogEventListener#dialogStateChanged(DialogEvent)}
	 */
	public void dialogStateChanged(DialogEvent e)
	{
		fireDialogStateChanged();
	}
	
	/**
	 * Keep track of broadcast or not
	 * @param broadcast
	 */
	public void setBroadcast(boolean broadcast)
	{
		m_broadcast = broadcast;
	}
	
	/**
	 * @return Whether in broadcast mode or not
	 */
	public boolean getBroadcast()
	{
		return m_broadcast;
	}
}
