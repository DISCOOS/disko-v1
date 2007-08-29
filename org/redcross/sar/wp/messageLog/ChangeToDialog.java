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
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.data.IMessageIf;

/**
 * Provides a dialog for selecting broadcast or non-broadcast receiver. This dialog also handles sub-dialogs 
 * such as field based unit selection, list unit selection and the broadcast dialogs
 * 
 * @author thomasl
 *
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
	
	public static final Dimension BUTTON_SIZE = new Dimension(MessageLogPanel.SMALL_BUTTON_SIZE.width*3, 
			MessageLogPanel.SMALL_BUTTON_SIZE.height);
	
	
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
		m_contentsPanel.setPreferredSize(new Dimension(SingleUnitListSelectionDialog.PANEL_WIDTH, BUTTON_SIZE.height));
		this.add(m_contentsPanel);
	}
	
	private void initButtons()
	{
		m_buttonGroup = new ButtonGroup();
		m_nonBroadcastButton = new JToggleButton(m_wpMessageLog.getText("NonBroadcastButton.text"));
		m_nonBroadcastButton.setMinimumSize(BUTTON_SIZE);
		m_nonBroadcastButton.setPreferredSize(BUTTON_SIZE);
		m_nonBroadcastButton.setMaximumSize(BUTTON_SIZE);
		m_nonBroadcastButton.setHorizontalAlignment(SwingConstants.LEFT);
		m_nonBroadcastButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				m_broadcast = false;
				MessageLogTopPanel.getCurrentMessage().setBroadcast(false);
				
				m_broadcastDialog.hideComponent();
				m_broadcastDialog.clearSelection();
				showNonBroadcast();
			}	
		});
		m_nonBroadcastButton.setSelected(true);
		m_buttonGroup.add(m_nonBroadcastButton);
		m_contentsPanel.add(m_nonBroadcastButton);
		
		m_broadcastButton = new JToggleButton(m_wpMessageLog.getText("BroadcastButton.text"));
		m_broadcastButton.setMinimumSize(BUTTON_SIZE);
		m_broadcastButton.setPreferredSize(BUTTON_SIZE);
		m_broadcastButton.setMaximumSize(BUTTON_SIZE);
		m_broadcastButton.setHorizontalAlignment(SwingConstants.LEFT);
		m_broadcastButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				m_broadcast = true;
				MessageLogTopPanel.getCurrentMessage().setBroadcast(true);
				
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
	
	public void hideComponent()
	{
		this.setVisible(false);
		m_nbListDialog.hideComponent();
		m_nbFieldDialog.hideComponent();
		m_broadcastDialog.hideComponent();
	}

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

	public void showComponent()
	{
		IMessageIf message = MessageLogTopPanel.getCurrentMessage();
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
		Point location = m_nonBroadcastButton.getLocationOnScreen();
		location.y += BUTTON_SIZE.height - 1;
		location.x--;
		m_nbListDialog.setLocation(location);
		m_nbListDialog.showComponent();
		
		location = m_nonBroadcastButton.getLocationOnScreen();
		location.y -= (m_nbFieldDialog.getHeight() + BUTTON_SIZE.height + 5);
		location.x--;
		m_nbFieldDialog.setLocation(location);
		m_nbFieldDialog.showComponent();
	}

	private void showBroadcast()
	{
		Point location = m_nonBroadcastButton.getLocationOnScreen();
		location.y += BUTTON_SIZE.height - 1;
		location.x--;
		m_broadcastDialog.setLocation(location);
		m_broadcastDialog.showComponent();
	}
	

	public void clearContents()
	{
		m_nonBroadcastButton.setSelected(true);
		m_broadcastDialog.clearContents();
		m_nbFieldDialog.clearContents();
		m_nbListDialog.clearContents();
	}

	public void dialogCanceled(DialogEvent e){}

	public void dialogFinished(DialogEvent e)
	{
		Object source = e.getSource();
		if(source instanceof SingleUnitListSelectionDialog || source instanceof UnitFieldSelectionDialog)
		{
			fireDialogFinished();
		}
	}

	public void dialogStateChanged(DialogEvent e){}
	
	public void setBroadcast(boolean broadcast)
	{
		m_broadcast = broadcast;
	}
	
	public boolean getBroadcast()
	{
		return m_broadcast;
	}
}
