package org.redcross.sar.wp.messageLog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.data.IMessageIf;

public class ChangeToDialog extends DiskoDialog implements IEditMessageDialogIf
{
	protected JButton m_broadcastButton;
	protected JButton m_nonBroadcastButton;
	protected JPanel m_contentsPanel;
	
	protected UnitFieldSelectionDialog m_nbFieldDialog;
	protected UnitListSelectionDialog m_nbListDialog;
	
	protected boolean m_broadcast = false;
	
	protected IDiskoWpMessageLog m_wpMessageLog;
	
	private final Dimension BUTTON_SIZE = new Dimension(MessageLogPanel.SMALL_BUTTON_SIZE.width*3, 
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
		
		m_nbListDialog = new UnitListSelectionDialog(m_wpMessageLog);
	}

	private void initContentsPanel()
	{
		m_contentsPanel = new JPanel(new FlowLayout());
		m_contentsPanel.setPreferredSize(new Dimension(800, BUTTON_SIZE.height));
		this.add(m_contentsPanel);
	}
	private void initButtons()
	{
		m_broadcastButton = new JButton();
		m_broadcastButton.setPreferredSize(BUTTON_SIZE);
		m_broadcastButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				m_nbFieldDialog.hideDialog();
				m_nbListDialog.hideDialog();
				m_broadcast = true;
			}
		});
		m_contentsPanel.add(m_broadcastButton);
		
		m_nonBroadcastButton = new JButton();
		m_nonBroadcastButton.setPreferredSize(BUTTON_SIZE);
		m_nonBroadcastButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				m_nbFieldDialog.showDialog();
				m_nbListDialog.showDialog();
				m_broadcast = false;
			}	
		});
		m_contentsPanel.add(m_nonBroadcastButton);
	}
	
	public void hideDialog()
	{
		this.setVisible(false);
		m_nbListDialog.hideDialog();
		m_nbFieldDialog.hideDialog();
	}

	public void newMessageSelected(IMessageIf message)
	{
		// TODO Auto-generated method stub
	}

	public void showDialog()
	{
		this.setVisible(true);
		m_contentsPanel.setVisible(true);
		if(m_broadcast)
		{
			
		}
		else
		{
			Point location = m_contentsPanel.getLocationOnScreen();
			location.y += BUTTON_SIZE.height;
			m_nbListDialog.setLocation(location);
			m_nbListDialog.showDialog();
			
			location = m_contentsPanel.getLocationOnScreen();
			location.y -= m_nbFieldDialog.getHeight();
			
			m_nbFieldDialog.showDialog();
		}
	}

	public void clearContents()
	{
		// TODO Auto-generated method stub
		
	}
}
