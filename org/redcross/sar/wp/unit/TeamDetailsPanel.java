package org.redcross.sar.wp.unit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.redcross.sar.gui.DiskoButtonFactory;

/**
 * JPanel displaying team details
 * 
 * @author thomasl
 */
public class TeamDetailsPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private final static ResourceBundle m_resources = ResourceBundle.getBundle("org.redcross.sar.wp.unit.unit");
	
	private JLabel m_topLabel;
	private JButton m_dismissButton;
	
	private JTextField m_nameTextField;
	private JTextField m_cellTextField;
	private JTextField m_propertyTextField;
	private JTextField m_organizationTextField;
	private JTextField m_departmentTextField;
	private JTextField m_roleTextField;
	private JTextField m_unitTextField;
	private JTextField m_alertedTextField;
	private JTextField m_expectedArrivalTextField;
	private JTextField m_arrivedTextField;
	private JTextField m_dismissedTextField;
	private JTextArea m_notesTextArea;

	public TeamDetailsPanel()
	{
		initialize();
	}
	
	private void initialize()
	{
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.weighty = 0.5;
		
		// Top
		gbc.gridwidth = 4;
		gbc.gridheight = 1;
		JPanel topPanel = new JPanel(new BorderLayout());
		m_topLabel = new JLabel();
		topPanel.add(m_topLabel, BorderLayout.CENTER);
		m_dismissButton = DiskoButtonFactory.createSmallButton("DismissButton.text"/*, "DismissButton.icon"*/);
		topPanel.add(m_dismissButton, BorderLayout.EAST);
		this.add(topPanel, gbc);
		gbc.gridy++;
		
		// Name
		m_nameTextField = new JTextField();
		gbc.gridwidth = 3;
		layoutComponent(0, m_resources.getString("FullName.text"), m_nameTextField, gbc, 1);
		
		// Cell
		m_cellTextField = new JTextField();
		gbc.gridwidth = 3;
		layoutComponent(0, m_resources.getString("CellularPhone.text"), m_cellTextField, gbc, 1);
		
		// Property
		m_propertyTextField = new JTextField();
		gbc.gridwidth = 3;
		layoutComponent(0, m_resources.getString("Property.text"), m_propertyTextField, gbc, 1);
		
		// Organization
		m_organizationTextField = new JTextField();
		gbc.gridwidth = 3;
		layoutComponent(0, m_resources.getString("Organization.text"), m_organizationTextField, gbc, 1);
		
		// Department
		m_departmentTextField = new JTextField();
		gbc.gridwidth = 3;
		layoutComponent(0, m_resources.getString("Department.text"), m_departmentTextField, gbc, 1);
		
		// Role
		gbc.gridy++;
		m_roleTextField = new JTextField();
		layoutComponent(0, m_resources.getString("Role.text"), m_roleTextField, gbc, 0);
		
		// Unit
		m_unitTextField = new JTextField();
		layoutComponent(2, m_resources.getString("Unit.text"), m_unitTextField, gbc, 1);
		
		// Alerted
		m_alertedTextField = new JTextField();
		layoutComponent(0, m_resources.getString("Alerted.text"), m_alertedTextField, gbc, 0);
		
		// Expected
		m_expectedArrivalTextField = new JTextField();
		layoutComponent(2, m_resources.getString("ExpectedArrival.text"), m_expectedArrivalTextField, gbc, 1);
		
		// Arrived
		m_arrivedTextField = new JTextField();
		layoutComponent(0, m_resources.getString("Arrived.text"), m_arrivedTextField, gbc, 0);
		
		// Dismissed
		m_dismissedTextField = new JTextField();
		layoutComponent(2, m_resources.getString("Dismissed.text"), m_dismissedTextField, gbc, 1);
		
		// Notes
		m_notesTextArea = new JTextArea();
		m_notesTextArea.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		m_notesTextArea.setRows(5);
		m_notesTextArea.setWrapStyleWord(true);
		m_notesTextArea.setLineWrap(true);
		JScrollPane notesScrollPane = new JScrollPane(m_notesTextArea);
		gbc.gridwidth = 3;
		layoutComponent(0, m_resources.getString("Notes.text"), notesScrollPane, gbc, 5);
		
	}
	
	private void layoutComponent(int column, String label, JComponent component, GridBagConstraints gbc, int height)
	{
		gbc.weightx = 1.0;
		gbc.gridheight = Math.max(1, height);
		gbc.gridx = column + 1;
		this.add(component, gbc);
		
		gbc.weightx = 0.0;
		gbc.gridx = column;
		gbc.gridwidth = 1;
		this.add(new JLabel(label), gbc);
		
		gbc.gridy += height;
	}
}
