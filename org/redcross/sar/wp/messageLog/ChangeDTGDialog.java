package org.redcross.sar.wp.messageLog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import org.redcross.sar.event.IDialogEventListener;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.data.IMessageIf;

/**
 * 
 * @author thomasl
 *
 * Creates the dialog for changing DTG in message log edit mode. 
 */
public class ChangeDTGDialog extends DiskoDialog implements KeyListener, IEditMessageDialogIf
{
	private JPanel m_contentsPanel = null;
	private JLabel m_createdLabel;
	private JTextField m_createdTextField;
	private JLabel m_timeLabel;
	private JTextField m_timeTextField;

	public ChangeDTGDialog(IDiskoWpMessageLog wp) 
	{
		super(wp.getApplication().getFrame());		
		// Init this
		initialize();
	}
	
	private void initialize()
	{
		try 
		{
            //this.setPreferredSize(new Dimension(800, 150));
            this.setContentPane(getContentPanel());
			this.pack();
			m_timeTextField.requestFocus();
		}
		catch (java.lang.Throwable e) 
		{
		}
	}
	
	private JPanel getContentPanel()
	{
		if (m_contentsPanel == null) {
			try 
			{
				m_contentsPanel = new JPanel();
				m_contentsPanel.setLayout(new GridLayout(2, 2));
				m_contentsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
				
				m_createdLabel = new JLabel("Opprettet:");
				m_contentsPanel.add(m_createdLabel);
				m_createdTextField = new JTextField();
				m_createdTextField.setEditable(false);
				m_contentsPanel.add(m_createdTextField);
				
				
				m_timeLabel = new JLabel("Tidspunkt:");
				m_contentsPanel.add(m_timeLabel);
				m_timeTextField = new JTextField();
				m_timeTextField.addKeyListener(this);
				m_contentsPanel.add(m_timeTextField);
				m_contentsPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			} 
			catch (java.lang.Throwable e) 
			{
			}
		}
		return m_contentsPanel;
	}

	@Override
	public void keyPressed(KeyEvent ke)
	{
		// Changes should be checked, and if found correct, sent to mso, not commited 
		if(ke.getKeyCode() == KeyEvent.VK_ENTER)
		{
			this.setVisible(false);
			
			fireDialogFinished();
		}
	}
	
	@Override
	public boolean isFocusable()
	{
		return true;
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newMessage(IMessageIf message)
	{
		m_createdTextField.setText(message.getCreated().toString());
		m_timeTextField.setText(message.getDTG());
	}
}
