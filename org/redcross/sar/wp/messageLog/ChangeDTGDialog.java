package org.redcross.sar.wp.messageLog;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.util.mso.DTG;

/**
 * 
 * @author thomasl
 *
 * Creates the dialog for changing DTG in message log edit mode. 
 */
public class ChangeDTGDialog extends DiskoDialog implements KeyListener, IEditMessageDialogIf
{
	private JPanel m_contentsPanel = null;
	
	private JPanel m_createdPanel;
	private JLabel m_createdLabel;
	private JTextField m_createdTextField;
	
	private JPanel m_timePanel;
	private JLabel m_timeLabel;
	private JTextField m_timeTextField;
	private IDiskoWpMessageLog m_wp;

	public ChangeDTGDialog(IDiskoWpMessageLog wp) 
	{
		super(wp.getApplication().getFrame());	
		m_wp = wp;
		
		initialize();
	}
	
	private void initialize()
	{
		try 
		{
            this.setContentPane(getContentPanel());
			this.pack();
			this.setMinimumSize(new Dimension((int)(MessageLogPanel.SMALL_BUTTON_SIZE.getWidth()*3.5),
					(int)(MessageLogPanel.SMALL_BUTTON_SIZE.getHeight()*1.5)));
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
				
				m_createdPanel = new JPanel();
				m_createdLabel = new JLabel(m_wp.getText("ChangeDTGDialogCreated.text"));
				m_createdLabel.setHorizontalAlignment(SwingConstants.CENTER);
				m_createdPanel.add(m_createdLabel);
				m_createdTextField = new JTextField(6);
				m_createdTextField.setEditable(false);
				m_createdPanel.add(m_createdTextField);
				m_contentsPanel.add(m_createdPanel);
				
				m_timePanel = new JPanel();
				m_timeLabel = new JLabel(m_wp.getText("ChangeDTGDialogTime.text"));
				m_timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
				m_timePanel.add(m_timeLabel);
				m_timeTextField = new JTextField(6);
				m_timeTextField.addKeyListener(this);
				m_timePanel.add(m_timeTextField);
				m_contentsPanel.add(m_timePanel);
				m_contentsPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			} 
			catch (java.lang.Throwable e) 
			{
			}
		}
		return m_contentsPanel;
	}
	
	public String getTime()
	{
		return m_timeTextField.getText();
	}

	public void keyPressed(KeyEvent ke)
	{
		// Changes should be checked, and if found correct, sent to mso, not commited 
		if(ke.getKeyCode() == KeyEvent.VK_ENTER)
		{
			this.setVisible(false);
			if(validDTG())
			{
				fireDialogFinished();
			}
			else
			{
				fireDialogCanceled();
			}
		}
	}
	
	private boolean validDTG()
	{
		try
		{
			DTG.DTGToCal(m_timeTextField.getText());
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public boolean isFocusable()
	{
		return true;
	}

	public void keyReleased(KeyEvent arg0){}
	public void keyTyped(KeyEvent arg0){}

	public void newMessageSelected(IMessageIf message)
	{
		setCreated(message.getCreated());
		m_timeTextField.setText(DTG.CalToDTG(message.getCalendar()));
	}

	public void setCreated(Calendar created)
	{
		m_createdTextField.setText(DTG.CalToDTG(created));
	}

	public void setTime(Calendar calendar)
	{
		m_timeTextField.setText(DTG.CalToDTG(calendar));
	}

	public void hideDialog()
	{
		this.setVisible(false);
	}

	public void showDialog()
	{
		this.setVisible(true);
	}

	public void clearContents()
	{
		m_createdTextField.setText("");
		m_timeTextField.setText("");
	}
}
