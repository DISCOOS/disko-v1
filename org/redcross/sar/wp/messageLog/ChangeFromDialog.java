package org.redcross.sar.wp.messageLog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.redcross.sar.gui.DiskoBorder;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.ErrorDialog;
import org.redcross.sar.gui.NumPadDialog;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.IMessageIf;

/**
 * 
 * @author thomasl
 *
 * The dialog for changing the from field of a message in the message log wp
 */

public class ChangeFromDialog extends DiskoDialog implements IEditMessageDialogIf, KeyListener
{
	private JPanel m_contentsPanel = null;
	
	private JPanel m_unitTypePanel = null;
	private JTextField m_unitTypeField = null;
	private JComponent m_unitTypePadArea = null;
	private UnitTypeDialog m_unitTypePad = null;
	
	private JPanel m_unitNumberPanel = null;
	private JTextField m_unitNumberField = null;
	private JComponent m_unitNumberPadArea = null;
	private NumPadDialog m_unitNumberPad;
	
	private IDiskoWpMessageLog m_wp;
	
	private boolean m_notebookMode = true;
	
	public ChangeFromDialog(IDiskoWpMessageLog messageLog)
	{
		super(messageLog.getApplication().getFrame());
		
		m_wp = messageLog;
		
		m_contentsPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		
		initUnitTypePanel();
		initUnitNumberPanel();
		m_contentsPanel.add(m_unitTypePanel, gbc);
		gbc.gridx++;
		m_contentsPanel.add(m_unitNumberPanel, gbc);
		gbc.gridx--;
		gbc.gridy++;
		m_contentsPanel.add(Box.createVerticalGlue(), gbc);
		gbc.gridx++;
		m_contentsPanel.add(Box.createVerticalGlue(), gbc);
		
		this.add(m_contentsPanel);
		this.pack();
	}
	
	private void initUnitTypePanel()
	{
		m_unitTypePanel = new JPanel(new BorderLayout());
		m_unitTypeField = new JTextField(8);
		m_unitTypeField.addKeyListener(this);
		m_unitTypePanel.add(m_unitTypeField, BorderLayout.NORTH);
		
		// Do not create extra area if not in notebook mode
		if(m_notebookMode)
		{
			getUnitTypePad();
			m_unitTypePadArea = (JComponent)Box.createRigidArea(m_unitTypePad.getSize());
			m_unitTypePanel.add(m_unitTypePadArea, BorderLayout.CENTER);
		}
	}
	
	private void getUnitTypePad()
	{
		if(m_notebookMode && m_unitTypePad == null)
		{
			m_unitTypePad = new UnitTypeDialog(m_wp, m_unitTypeField);
			m_unitTypePad.setVisible(false);
		}
	}

	private void initUnitNumberPanel()
	{
		m_unitNumberPanel = new JPanel(new BorderLayout());
		
		m_unitNumberField = new JTextField(8);
		m_unitNumberField.addKeyListener(this);
		m_unitNumberPanel.add(m_unitNumberField, BorderLayout.NORTH);
		
		if(m_notebookMode)
		{
			getUnitNumPad();
			m_unitNumberPad.setTextField(m_unitNumberField);
			m_unitNumberPadArea = (JComponent)Box.createRigidArea(m_unitNumberPad.getSize());
			m_unitNumberPanel.add(m_unitNumberPadArea, BorderLayout.CENTER);
		}
	}

	private void getUnitNumPad()
	{
		if(m_notebookMode && m_unitNumberPad == null)
		{
			m_unitNumberPad = new NumPadDialog(m_wp.getApplication().getFrame());
			m_unitNumberPad.setVisible(false);
			// Add action listener to ok button. Check that unit exists, give error message if not
			m_unitNumberPad.getOkButton().addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					if(unitExists())
					{
						fireDialogFinished();
					}
					else
					{
						ErrorDialog error = new ErrorDialog(m_wp.getApplication().getFrame());
						error.showError(m_wp.getText("NonexistingUnitErrorMessage.text"),
								m_unitTypeField.getText() + " " + m_unitNumberField.getText() +
								" " + m_wp.getText("NonexistingUnitErrorDetails.text"));
					}
				}	
			});
		}
	}
	
	protected boolean unitExists()
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void showDialog()
	{
		this.setVisible(true);
		
		// Extra dialogs only exists if in notebook mode
		if(m_notebookMode)
		{
			m_unitNumberPadArea.setSize(m_unitNumberPad.getSize());
			m_unitNumberPad.setLocation(m_unitNumberPadArea.getLocationOnScreen());
			m_unitNumberPad.setVisible(true);
			
			m_unitTypePadArea.setSize(m_unitTypePad.getSize());
			m_unitTypePad.setLocation(m_unitTypePadArea.getLocationOnScreen());
			m_unitTypePad.setVisible(true);
			
			m_unitTypeField.setFocusable(false);
			//m_unitTypeField.setEditable(false);
			m_unitNumberField.setFocusable(false);
			//m_unitNumberField.setEditable(false);
		}
	}
	
	@Override
	public void hideDialog()
	{
		this.setVisible(false);
		
		if(m_notebookMode)
		{
			m_unitTypePad.setVisible(false);
			m_unitNumberPad.setVisible(false);
		}
	}

	@Override
	public void newMessageSelected(IMessageIf message)
	{
		ICmdPostIf sender = message.getSender();
		if(sender != null)
		{
			// TODO implement
			m_unitTypeField.setText(message.getSender().getCallSign());
		}
		else
		{
			m_unitTypeField.setText("");
		}
	}

	@Override
	public void keyPressed(KeyEvent ke)
	{
		if(ke.getKeyCode() == KeyEvent.VK_ENTER)
		{
			if(isValidUnitType() && isValidUnitNumber())
			{
				fireDialogFinished();
			}
			else
			{
				System.err.println("Unit type or unit number is not valid");
			}
		}
	}

	private boolean isValidUnitNumber()
	{
		String field = m_unitNumberField.getText();
		
		// Check for empty string
		if(field.length()==0)
		{
			return false;
		}
		
		// Check that field is a number
		try
		{
			Integer.valueOf(field);
		}
		catch(Exception e)
		{
			return false;
		}
		
		return true;
	}

	private boolean isValidUnitType()
	{	
		String unitType = m_unitTypeField.getText();
		
		// TODO get list of valid types from?
		String[] validUnitTypes = {"KO", "L", "B", "M"};
		
		for(int i=0; i<validUnitTypes.length; i++)
		{
			if(unitType.equals(validUnitTypes[i]))
			{
				return true;
			}
		}
		
		return false;
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

	public String getText()
	{
		return m_unitTypeField.getText() + " " + m_unitNumberField.getText();
	}

	@Override
	public void clearContents()
	{
		m_unitNumberField.setText("");
		m_unitTypeField.setText("");
	}
	
}