package org.redcross.sar.wp.messageLog;

import org.redcross.sar.app.Utils;
import org.redcross.sar.gui.DiskoButtonFactory;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IUnitIf.UnitType;
import org.redcross.sar.util.Internationalization;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Dialog for selecting unit type
 *
 * @author thomasl
 */
public class UnitTypeDialog extends DiskoDialog implements IEditMessageComponentIf
{
	private static final long serialVersionUID = 1L;

	private JPanel m_contentsPanel = null;

	private JButton m_aircraftButton = null;
	private JButton m_boatButton = null;
	private JButton m_dogButton = null;
	private JButton m_vehicleButton = null;
	private JButton m_teamButton = null;
	private JButton m_commandPostButton = null;
	private JTextField m_textField = null;
	private LinkedList<JButton> m_buttons;

	/**
	 * @param wp Message log work process
	 * @param textField Text field displaying communicator number prefix
	 */
	public UnitTypeDialog(IDiskoWpMessageLog wp, JTextField textField)
	{
		super(wp.getApplication().getFrame());

		m_textField = textField;

		m_contentsPanel = new JPanel(new GridLayout(3, 2));

		initButtons();

		this.add(m_contentsPanel);
		this.pack();
	}

	/**
	 *
	 */
	private void initButtons()
	{
		m_buttons = new LinkedList<JButton>();

		try
		{
			ResourceBundle resources = Internationalization.getBundle(IUnitIf.class);

			m_aircraftButton = addButton(resources.getString("UnitType.AIRCRAFT.text"),
					resources.getString("UnitType.AIRCRAFT.letter"),
					resources.getString("UnitType.AIRCRAFT.icon"),
					UnitType.AIRCRAFT);
			m_buttons.add(m_aircraftButton);

			m_boatButton = addButton(resources.getString("UnitType.BOAT.text"),
					resources.getString("UnitType.BOAT.letter"),
					resources.getString("UnitType.BOAT.icon"),
					UnitType.BOAT);
			m_buttons.add(m_boatButton);

			m_dogButton = addButton(resources.getString("UnitType.DOG.text"),
					resources.getString("UnitType.DOG.letter"),
					resources.getString("UnitType.DOG.icon"),
					UnitType.DOG);
			m_buttons.add(m_dogButton);

			m_vehicleButton = addButton(resources.getString("UnitType.VEHICLE.text"),
					resources.getString("UnitType.VEHICLE.letter"),
					resources.getString("UnitType.VEHICLE.icon"),
					UnitType.VEHICLE);
			m_buttons.add(m_vehicleButton);

			m_teamButton = addButton(resources.getString("UnitType.TEAM.text"),
					resources.getString("UnitType.TEAM.letter"),
					resources.getString("UnitType.TEAM.icon"),
					UnitType.TEAM);
			m_buttons.add(m_teamButton);

			m_commandPostButton = addButton(resources.getString("UnitType.COMMAND_POST.text"),
					resources.getString("UnitType.COMMAND_POST.letter"),
					resources.getString("UnitType.COMMAND_POST.icon"),
					UnitType.COMMAND_POST);
			m_buttons.add(m_commandPostButton);
		}
		catch(MissingResourceException e)
		{
			System.err.println("Could not find unit resource file");
		}
	}

	private JButton addButton(String name, final String unitTypeLetter, String iconPath, UnitType unitType)
	{
		JButton button = DiskoButtonFactory.createSmallButton();
		button.setActionCommand(unitType.name());
		try
		{
			Icon buttonIcon = Utils.createImageIcon(iconPath, name);
			button.setIcon(buttonIcon);
		}
		catch (Exception e)
		{
			System.err.println("Error getting icon: " + iconPath + " in UnitTypeDialog");
		}

		// Let the buttons manipulate the text field, setting the contents to the unit type code
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if(m_textField != null)
				{
					m_textField.setText(unitTypeLetter);
				}
				else
				{
					System.err.println("Text-field not set");
				}
			}
		});

		m_contentsPanel.add(button);

		return button;
	}

	public LinkedList<JButton> getButtons()
	{
		return m_buttons;
	}

	/**
	 *
	 */
	public void clearContents()
	{
	}

	/**
	 * {
	 */
	public void hideComponent()
	{
		this.setVisible(false);
	}

	/**
	 *
	 */
	public void newMessageSelected(IMessageIf message)
	{
	}

	/**
	 *
	 */
	public void showComponent()
	{
		this.setVisible(true);
	}

}
