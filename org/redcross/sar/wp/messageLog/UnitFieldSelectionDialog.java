package org.redcross.sar.wp.messageLog;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.redcross.sar.app.Utils;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.ErrorDialog;
import org.redcross.sar.gui.NumPadDialog;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IUnitListIf;
import org.redcross.sar.mso.data.IUnitIf.UnitType;

/**
 * 
 * @author thomasl
 *
 * The dialog for selecting unit type and number.
 * Dialog loads unit information from resource file {@link org.redcross.sar.mso.data.properties.Unit.properties}
 */

public class UnitFieldSelectionDialog extends DiskoDialog implements IEditMessageDialogIf, KeyListener
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
	
	protected ResourceBundle m_unitResources;
	
	public UnitFieldSelectionDialog(IDiskoWpMessageLog messageLog)
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
		
		try
		{
			m_unitResources = ResourceBundle.getBundle("org.redcross.sar.mso.data.properties.Unit");
		}
		catch(MissingResourceException e)
		{
			System.err.println("Unable to load unit resources in UnitSelectionDialog");
		}
		
		this.setModalityType(ModalityType.MODELESS);
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
			m_unitTypePad.setAlwaysOnTop(true);
			//m_unitTypePad.setModalityType(ModalityType.MODELESS);
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
			m_unitNumberPad.setAlwaysOnTop(true);
			m_unitNumberPad.setVisible(false);
			
			// Remove previous action listeners for the ok button, this sets the visibility of the pad to false, which is
			// not always desirable
			ActionListener[] actionListeners = m_unitNumberPad.getOkButton().getActionListeners();
			m_unitNumberPad.getOkButton().removeActionListener(actionListeners[0]);
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
						ErrorDialog error = Utils.getErrorDialog();
						error.setAlwaysOnTop(true);
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
		// Empty fields, use standard value
		if(m_unitNumberField.getText().length()==0 && m_unitTypeField.getText().length()==0)
		{
			return true;
		}
		
		// TODO vinjar/stian sjekk om dette er korrekt
		IUnitListIf units = m_wp.getMsoManager().getCmdPost().getUnitList();
		
		IUnitIf unit = null;
		try
		{
			unit = units.getUnit(Integer.valueOf(m_unitNumberField.getText()));
		}
		catch(Exception e)
		{
			return false;
		}
		
		// If unit number does not exist the unit can not exist
		if(unit == null)
		{
			return false;
		}

		// Check unit type for match
		if(! (unit.getType() == getUnitType(m_unitTypeField.getText())))
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * @param unitTypeString The text string containing the unit type code 
	 * @return
	 */
	protected UnitType getUnitType(String unitTypeString)
	{
		try
		{
			if(unitTypeString.equals(m_unitResources.getString("UnitType.VEHICLE.letter")))
			{
				return UnitType.VEHICLE;
			}
			else if(unitTypeString.equals(m_unitResources.getString("UnitType.BOAT.letter")))
			{
				return UnitType.BOAT;
			}
			else if(unitTypeString.equals(m_unitResources.getString("UnitType.COMMAND_POST.letter")))
			{
				return UnitType.COMMAND_POST;
			}
			else if(unitTypeString.equals(m_unitResources.getString("UnitType.TEAM.letter")))
			{
				return UnitType.TEAM;
			}
			else if(unitTypeString.equals(m_unitResources.getString("UnitType.AIRCRAFT.letter")))
			{
				return UnitType.AIRCRAFT;
			}
			else if(unitTypeString.equals(m_unitResources.getString("UnitType.DOG.letter")))
			{
				return UnitType.DOG;
			}
		}
		catch(MissingResourceException e)
		{
			System.err.println("Error getting unit resource");
		}
		
		
		return null;
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
			m_unitNumberPad.setAlwaysOnTop(true);
			
			m_unitTypePadArea.setSize(m_unitTypePad.getSize());
			m_unitTypePad.setLocation(m_unitTypePadArea.getLocationOnScreen());
			m_unitTypePad.setVisible(true);
			m_unitNumberPad.setAlwaysOnTop(true);
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
			//String senderString = sender.toString();
			m_unitTypeField.setText(sender.getCallSign());
			m_unitNumberField.setText("");
		}
		else
		{
			m_unitTypeField.setText("");
			m_unitNumberField.setText("");
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
		
		String[] validUnitTypes = {
				m_unitResources.getString("UnitType.COMMAND_POST.letter"),
				m_unitResources.getString("UnitType.BOAT.letter"), 
				m_unitResources.getString("UnitType.AIRCRAFT.letter"), 
				m_unitResources.getString("UnitType.TEAM.letter"),
				m_unitResources.getString("UnitType.VEHICLE.letter"),
				m_unitResources.getString("UnitType.DOG.letter")};
		
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
	public void keyReleased(KeyEvent arg0){}

	@Override
	public void keyTyped(KeyEvent arg0){}

	public String getText()
	{
		return m_unitTypeField.getText() + " " + m_unitNumberField.getText();
	}
	
	public UnitType getUnitType()
	{
		return getUnitType(m_unitTypeField.getText());
	}
	
	public int getUnitNumber()
	{
		try
		{
			return Integer.valueOf(m_unitNumberField.getText());
		}
		catch(Exception e)
		{
			return 0;
		}
	}

	@Override
	public void clearContents()
	{
		m_unitNumberField.setText("");
		m_unitTypeField.setText("");
	}
	
	public JButton getOKButton()
	{
		return m_unitNumberPad.getOkButton();
	}

	public void addActionListener(UnitListSelectionDialog fromDialog)
	{
		LinkedList<JButton> buttons = m_unitTypePad.getButtons();
		for(JButton button : buttons)
		{
			button.addActionListener(fromDialog);
		}
	}
}