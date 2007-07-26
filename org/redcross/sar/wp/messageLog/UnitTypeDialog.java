package org.redcross.sar.wp.messageLog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.redcross.sar.app.Utils;
import org.redcross.sar.gui.DiskoDialog;

/**
 * 
 * @author thomasl
 *
 * Dialog for selecting unit type
 */
public class UnitTypeDialog extends DiskoDialog
{
	private IDiskoWpMessageLog m_wp;
	private JPanel m_contentsPanel = null;
	
	
	private JButton m_planeButton = null;
	private JButton m_boatButton = null;
	private JButton m_dogButton = null;
	private JButton m_carButton = null;
	private JButton m_manButton = null;
	private JButton m_koButton = null;
	private JTextField m_textField = null;
	
	public UnitTypeDialog(IDiskoWpMessageLog wp, JTextField textField)
	{
		super(wp.getApplication().getFrame());
		m_wp = wp;
	
		m_textField = textField;
		
		m_contentsPanel = new JPanel(new GridLayout(3, 2));
		
		initButtons();
		
		this.add(m_contentsPanel);
		this.pack();
	}
	
	private void initButtons()
	{
		m_planeButton = addButton("Plane", "P", "");
		m_boatButton = addButton("Boat", "B", "");
		m_dogButton = addButton("Dog", "D", m_wp.getText("DogUnit.icon"));
		m_carButton = addButton("Car", "C", m_wp.getText("CarUnit.icon"));
		m_manButton = addButton("Man", "M", m_wp.getText("ManUnit.icon"));
		m_koButton = addButton("KO", "KO", "");
	}
	
	private JButton addButton(String name, final String unitType, String iconPath)
	{
		JButton button = new JButton(name);
		try
		{
			button.setIcon(Utils.createImageIcon(iconPath, name));
		} 
		catch (Exception e)
		{
			System.err.println("Error getting icon: " + iconPath + " in UnitTypeDialog");
		}
		
		// Let the buttons manipulate the text field, setting the contents to the unit type code
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if(m_textField != null)
				{
					m_textField.setText(unitType);
				}
				else
				{
					System.err.println("Text-field not set, unable to add listeners");
				}
			}	
		});
		
		// Button layout
		button.setPreferredSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		button.setMinimumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		button.setMaximumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		
		m_contentsPanel.add(button);
		
		return button;
	}

}
