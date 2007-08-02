package org.redcross.sar.wp.messageLog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.redcross.sar.event.DialogEvent;
import org.redcross.sar.event.IDialogEventListener;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.data.ICommunicatorIf;
import org.redcross.sar.mso.data.IMessageIf;

/**
 * Provides a dialog for selecting broadcast or non-broadcast receiver. This dialog also handles sub-dialogs 
 * such as field based unit selection, list unit selection and the broadcast dialogs
 * 
 * @author thomasl
 *
 */
public class ChangeToDialog extends DiskoDialog implements IEditMessageDialogIf, IDialogEventListener
{
	protected JButton m_broadcastButton;
	protected JButton m_nonBroadcastButton;
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
		m_nbFieldDialog = new UnitFieldSelectionDialog(m_wpMessageLog);
		m_nbListDialog = new SingleUnitListSelectionDialog(m_wpMessageLog);
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
		m_contentsPanel.setLayout(new BoxLayout(m_contentsPanel, BoxLayout.LINE_AXIS));
		m_contentsPanel.setPreferredSize(new Dimension(SingleUnitListSelectionDialog.PANEL_WIDTH, BUTTON_SIZE.height));
		this.add(m_contentsPanel);
	}
	
	private void initButtons()
	{
		m_nonBroadcastButton = new JButton(m_wpMessageLog.getText("NonBroadcastButton.text"));
		m_nonBroadcastButton.setPreferredSize(BUTTON_SIZE);
		m_nonBroadcastButton.setMaximumSize(BUTTON_SIZE);
		m_nonBroadcastButton.setHorizontalAlignment(SwingConstants.LEFT);
		m_nonBroadcastButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				m_nbFieldDialog.showDialog();
				m_nbListDialog.showDialog();
				m_broadcastDialog.hideDialog();
				m_broadcast = false;
			}	
		});
		m_contentsPanel.add(m_nonBroadcastButton);
		
		m_broadcastButton = new JButton(m_wpMessageLog.getText("BroadcastButton.text"));
		m_broadcastButton.setPreferredSize(BUTTON_SIZE);
		m_broadcastButton.setMaximumSize(BUTTON_SIZE);
		m_broadcastButton.setHorizontalAlignment(SwingConstants.LEFT);
		m_broadcastButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				m_nbFieldDialog.hideDialog();
				m_nbListDialog.hideDialog();
				
				Point location = m_nonBroadcastButton.getLocationOnScreen();
				location.y += BUTTON_SIZE.height;
				m_broadcastDialog.setLocation(location);
				m_broadcastDialog.showDialog();
				m_broadcast = true;
			}
		});
		m_contentsPanel.add(m_broadcastButton);
		
		m_contentsPanel.add(Box.createHorizontalGlue());
	}
	
	public void hideDialog()
	{
		this.setVisible(false);
		m_nbListDialog.hideDialog();
		m_nbFieldDialog.hideDialog();
		m_broadcastDialog.hideDialog();
	}

	public void newMessageSelected(IMessageIf message)
	{
		// TODO Auto-generated method stub
	}
	
	public String getCommunicatorName()
	{
		if(m_broadcast)
		{
			// TODO
			return null;
		}
		else
		{
			return m_nbFieldDialog.getCommunicatorName();
		}
	}

	public void showDialog()
	{
		this.setVisible(true);
		
		if(m_broadcast)
		{
			
		}
		else
		{
			Point location = m_nonBroadcastButton.getLocationOnScreen();
			location.y += BUTTON_SIZE.height;
			m_nbListDialog.setLocation(location);
			m_nbListDialog.showDialog();
			
			location = m_nonBroadcastButton.getLocationOnScreen();
			location.y -= (m_nbFieldDialog.getHeight() + BUTTON_SIZE.height + 4);
			m_nbFieldDialog.setLocation(location);
			m_nbFieldDialog.showDialog();
		}
	}

	public void clearContents()
	{
		// TODO Auto-generated method stub
		
	}

	public void dialogCanceled(DialogEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	public void dialogFinished(DialogEvent e)
	{
		Object source = e.getSource();
		if(source instanceof SingleUnitListSelectionDialog || source instanceof UnitFieldSelectionDialog)
		{
			fireDialogFinished();
		}
	}

	public void dialogStateChanged(DialogEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	
	public void setBroadcast(boolean broadcast)
	{
		m_broadcast = broadcast;
	}
	
	public boolean getBroadcast()
	{
		return m_broadcast;
	}

	public List<ICommunicatorIf> getCommunicators()
	{
		List<ICommunicatorIf> communicators = new LinkedList<ICommunicatorIf>();
		if(m_broadcast)
		{
			// TODO get from broadcast dialog
		}
		else
		{
			communicators.add(m_nbFieldDialog.getCommunicator());
		}
		return communicators;
	}
}
