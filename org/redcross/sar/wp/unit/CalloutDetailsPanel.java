package org.redcross.sar.wp.unit;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.redcross.sar.gui.DiskoButtonFactory;

/**
 * JPanel displaying alert details
 * 
 * @author thomasl
 */
public class CalloutDetailsPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private JTextField m_topTextField;
	private JButton m_printButton;
	
	private JTextField m_titleTextField;
	private JTextField m_createdTextField;
	private JTextField m_organizationTextField;
	private JTextField m_departmentTextField;
	
	private JTable m_personnelTable;
	
	private final static ResourceBundle m_resources = ResourceBundle.getBundle("org.redcross.sar.wp.unit.unit");
	
	public CalloutDetailsPanel()
	{
		initialize();
	}
	
	private void initialize()
	{
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(4, 4, 4, 4);
		
		// Top panel
		JPanel topPanel = new JPanel(new BorderLayout());
		m_topTextField = new JTextField();
		m_topTextField.setEditable(false);
		topPanel.add(m_topTextField, BorderLayout.CENTER);
		m_printButton = DiskoButtonFactory.createSmallButton("Print"/*, m_resources.getString("PrintButton.icon")*/);
		topPanel.add(m_printButton, BorderLayout.EAST);
		gbc.gridwidth = 2;
		this.add(topPanel, gbc);
		gbc.gridy++;
		gbc.gridwidth = 1;
		
		// Title
		m_titleTextField = new JTextField();

		layoutComponent(m_resources.getString("Title.text"), m_titleTextField, gbc);
		
		// Created
		m_createdTextField = new JTextField();
		layoutComponent(m_resources.getString("Created.text"), m_createdTextField, gbc);
		
		// Organization
		m_organizationTextField = new JTextField();
		layoutComponent(m_resources.getString("Organization.text"), m_organizationTextField, gbc);
		
		// Department
		m_departmentTextField = new JTextField();
		layoutComponent(m_resources.getString("Department.text"), m_departmentTextField, gbc);
		
		// Personnel table
		m_personnelTable = new JTable();
		JScrollPane personnelTableScrollPane = new JScrollPane(m_personnelTable);
		gbc.weighty = 1.0;
		gbc.gridwidth = 2;
		this.add(personnelTableScrollPane, gbc);
	}
	
	private void layoutComponent(String label, JComponent component, GridBagConstraints gbc)
	{
		gbc.weightx = 1.0;
		gbc.gridx = 1;
		this.add(component, gbc);
		
		gbc.weightx = 0.0;
		gbc.gridx = 0;
		this.add(new JLabel(label), gbc);
		
		gbc.gridy++;
	}
}
