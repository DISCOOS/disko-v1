package org.redcross.sar.wp.messageLog;

import org.redcross.sar.gui.DiskoButtonFactory;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.ErrorDialog;
import org.redcross.sar.gui.NumPadDialog;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.util.except.IllegalMsoArgumentException;
import org.redcross.sar.util.mso.DTG;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Calendar;

/**
 * Creates the dialog for changing DTG in message log edit mode.
 * @author thomasl
 */
public class ChangeDTGDialog extends DiskoDialog implements KeyListener, IEditMessageComponentIf
{
	private static final long serialVersionUID = 1L;

	private JPanel m_contentsPanel = null;

	private JPanel m_createdPanel;
	private JLabel m_createdLabel;
	private JTextField m_createdTextField;

	private JPanel m_timePanel;
	private JLabel m_timeLabel;
	private JTextField m_timeTextField;
	private IDiskoWpMessageLog m_wp;

	private NumPadDialog m_numpad = null;
	private static final boolean NOTEBOOK_MODE = true;

	/**
	 * @param wp Message log work process reference
	 */
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
			this.setMinimumSize(new Dimension((int)(DiskoButtonFactory.SMALL_BUTTON_SIZE.width*3.5),
					(int)(DiskoButtonFactory.SMALL_BUTTON_SIZE.height*1.5)));
			m_timeTextField.requestFocus();
		}
		catch (java.lang.Throwable e){}

		// Initialize numpad
		if(NOTEBOOK_MODE)
		{
			m_numpad = new NumPadDialog(m_wp.getApplication().getFrame());
			JButton okButton = m_numpad.getOkButton();
			okButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					saveDTG();
				}
			});
			m_numpad.setTextField(m_timeTextField);
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

	/**
	 * @return Text in time text field
	 */
	public String getTime()
	{
		return m_timeTextField.getText();
	}

	/**
	 * Saves DTG if key is enter
	 */
	public void keyPressed(KeyEvent ke)
	{
		// Changes should be checked, and if found correct, sent to mso, not commited
		if(ke.getKeyCode() == KeyEvent.VK_ENTER)
		{
			saveDTG();
		}
	}

	private void saveDTG()
	{
		this.setVisible(false);

		try
		{
			IMessageIf message = MessageLogBottomPanel.getCurrentMessage(true);
			message.setOccuredTime(DTG.DTGToCal(this.getTime()));
		}
		catch (IllegalMsoArgumentException e1)
		{
			ErrorDialog error = new ErrorDialog(m_wp.getApplication().getFrame());
			error.showError(m_wp.getText("InvalidDTG.header"), m_wp.getText("InvalidDTG.details"));
			fireDialogCanceled();
		}
		fireDialogFinished();
	}

	@Override
	public boolean isFocusable()
	{
		return true;
	}

	public void keyReleased(KeyEvent arg0){}
	public void keyTyped(KeyEvent arg0){}

	/**
	 * Sets the DTG field, get it from the message
	 */
	public void newMessageSelected(IMessageIf message)
	{
		setCreated(message.getCreated());
		m_timeTextField.setText(DTG.CalToDTG(message.getOccuredTime()));
	}

	/**
	 * Sets the created text field
	 * @param created
	 */
	public void setCreated(Calendar created)
	{
		m_createdTextField.setText(DTG.CalToDTG(created));
	}

	/**
	 * Sets the time text field
	 * @param calendar
	 */
	public void setTime(Calendar calendar)
	{
		m_timeTextField.setText(DTG.CalToDTG(calendar));
	}

	/**
	 * Hides the numpad if in notebook mode
	 */
	public void hideComponent()
	{
		this.setVisible(false);

		if(NOTEBOOK_MODE)
		{
			m_numpad.setVisible(false);
		}
	}

	/**
	 * Displays the numpad if in notebook mode
	 */
	public void showComponent()
	{
		this.setVisible(true);

		if(m_createdTextField.getText().isEmpty())
		{
			setCreated(Calendar.getInstance());
		}

		if(NOTEBOOK_MODE)
		{
			Point location = m_contentsPanel.getLocationOnScreen();
			location.x += m_contentsPanel.getWidth();
			location.y += m_contentsPanel.getHeight();
			location.y -= m_numpad.getHeight();
			m_numpad.setLocation(location);
			m_numpad.setVisible(true);
		}
	}

	/**
	 * Reset created and time fields
	 */
	public void clearContents()
	{
		m_createdTextField.setText("");
		m_timeTextField.setText("");
	}
}
