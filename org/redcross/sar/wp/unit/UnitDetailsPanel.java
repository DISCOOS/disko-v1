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
import javax.swing.JToggleButton;

import org.redcross.sar.gui.DiskoButtonFactory;

/**
 * JPanel displaying unit details
 * 
 * @author thomasl
 */
public class UnitDetailsPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private final static ResourceBundle m_resources = ResourceBundle.getBundle("org.redcross.sar.wp.unit.unit");
	
	private JTextField m_topPanelTextField;
	private JToggleButton m_pauseToggleButton;
	private JButton m_dissolveButton;
	private JButton m_showReportButton;
	
	private JTextField m_leaderTextField;
	private JTextField m_cellPhoneTextField;
	private JTextField m_fiveToneTextField;
	private JTextField m_createdTextField;
	private JTextField m_callsignTextField;
	private JTextField m_fieldTimeTextField;
	private JTextField m_assignmentTextField;
	private JTextField m_stopTimeTextField;
	private JTable m_personnelTable;
	
	public UnitDetailsPanel()
	{
		initialize();
	}
	
	private void initialize()
	{
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(4, 4, 4, 4);
		
		// Top panel
		JPanel topPanel = new JPanel(new BorderLayout());
		JPanel topButtonsPanel = new JPanel();
		topButtonsPanel.setBorder(null);
		m_topPanelTextField = new JTextField();
		m_topPanelTextField.setEditable(false);
		topPanel.add(m_topPanelTextField, BorderLayout.CENTER);
		m_pauseToggleButton = DiskoButtonFactory.createSmallToggleButton(m_resources.getString("PauseButton.text"));
		topButtonsPanel.add(m_pauseToggleButton);
		m_dissolveButton = DiskoButtonFactory.createSmallButton(m_resources.getString("DissolveButton.text")/*, ""*/);
		topButtonsPanel.add(m_dissolveButton);
		m_showReportButton = DiskoButtonFactory.createSmallButton(m_resources.getString("ShowReportButton.text")/*, iconPath*/);
		topButtonsPanel.add(m_showReportButton);
		topPanel.add(topButtonsPanel, BorderLayout.EAST);
		gbc.gridwidth = 4;
		this.add(topPanel, gbc);
		gbc.gridy++;
		
		// Leader
		m_leaderTextField = new JTextField();
		gbc.gridwidth = 3;
		layoutComponent(0, m_resources.getString("Leader.text"), m_leaderTextField, gbc, 1);
		
		// Cell phone
		m_cellPhoneTextField = new JTextField();
		gbc.gridwidth = 3;
		layoutComponent(0, m_resources.getString("CellularPhone.text"), m_cellPhoneTextField, gbc, 1);
		
		// 5-tone
		m_fiveToneTextField = new JTextField();
		layoutComponent(0, m_resources.getString("FiveTone.text"), m_fiveToneTextField, gbc, 0);
		
		// Created
		m_createdTextField = new JTextField();
		layoutComponent(2, m_resources.getString("Created.text"), m_createdTextField, gbc, 1);
		
		// Call sign
		m_callsignTextField = new JTextField();
		layoutComponent(0, m_resources.getString("CallSign.text"), m_callsignTextField, gbc, 0);
		
		// Field time
		m_fieldTimeTextField = new JTextField();
		layoutComponent(2, m_resources.getString("FieldTime.text"), m_fieldTimeTextField, gbc, 1);
		
		// Assignment
		m_assignmentTextField = new JTextField();
		layoutComponent(0, m_resources.getString("Assignment.text"), m_assignmentTextField, gbc, 0);
		
		// Stop time
		m_stopTimeTextField = new JTextField();
		layoutComponent(2, m_resources.getString("StopTime.text"), m_stopTimeTextField, gbc, 1);
		
		// Personnel table
		m_personnelTable = new JTable();
		JScrollPane personnelTableScrollPane = new JScrollPane(m_personnelTable);
		gbc.gridwidth = 4;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.gridx = 0;
		this.add(personnelTableScrollPane, gbc);
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
