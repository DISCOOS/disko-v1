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
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;

import org.redcross.sar.gui.DiskoButtonFactory;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IPersonnelIf;
import org.redcross.sar.mso.data.IPersonnelListIf;
import org.redcross.sar.mso.data.IUnitIf;

/**
 * JPanel displaying unit details
 * 
 * @author thomasl
 */
public class UnitDetailsPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private final static ResourceBundle m_resources = ResourceBundle.getBundle("org.redcross.sar.wp.unit.unit");
	
	private IUnitIf m_currentUnit;
	
	private JLabel m_topPanelLabel;
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
		m_topPanelLabel = new JLabel();
		topPanel.add(m_topPanelLabel, BorderLayout.CENTER);
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
		m_personnelTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		m_personnelTable.setFillsViewportHeight(true);
		m_personnelTable.setModel(new UnitPersonnelTableModel());
		m_personnelTable.setDragEnabled(true);
		try
		{
			m_personnelTable.setTransferHandler(new PersonnelTransferHandler());
		} 
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
		JTableHeader tableHeader = m_personnelTable.getTableHeader();
        tableHeader.setResizingAllowed(false);
        tableHeader.setReorderingAllowed(false);
		
		JScrollPane personnelTableScrollPane = new JScrollPane(m_personnelTable);
		gbc.gridwidth = 4;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.BOTH;
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

	public void setUnit(IUnitIf unit)
	{
		m_currentUnit = unit;
		updateComponents();
	}

	private void updateComponents()
	{
		if(m_currentUnit != null)
		{
			// Fill in fields with unit values
			String topText = m_currentUnit.getTypeText() + " " + m_currentUnit.getNumber() +
			" (" + m_currentUnit.getStatusText() + ")"; 
			m_topPanelLabel.setText(topText);
			
			IPersonnelIf leader = m_currentUnit.getUnitLeader();
			String leaderName = leader == null ? "" : leader.getFirstname() + " " + leader.getLastname();
			m_leaderTextField.setText(leaderName);
			
			String cell = leader == null ? "" : leader.getTelephone1();
			m_cellPhoneTextField.setText(cell);
			
//			String fiveTone = m_currentUnit.get// ?
//			m_fiveToneTextField.setText(fiveTone); TODO
			
//			String created = DTG.CalToDTG(m_currentUnit.get)
//			m_createdTextField.setText(created); TODO
			
			String callsign = m_currentUnit.getCallSign();
			m_callsignTextField.setText(callsign);
			
//			String fieldTime = DTG.CalToDTG(m_currentUnit.gets);
//			m_fieldTimeTextField.setText(fieldTime); TODO
			
			IAssignmentIf assignment = m_currentUnit.getActiveAssignment();
			String assignmentString = assignment == null ? "" : assignment.getTypeAndNumber();
			m_assignmentTextField.setText(assignmentString);
			
//			String stopTime = DTG.CalToDTG(m_currentUnit.get);
//			m_stopTimeTextField.setText(stopTime); TODO
			
			UnitPersonnelTableModel model = (UnitPersonnelTableModel)m_personnelTable.getModel();
			model.setPersonnel(m_currentUnit.getUnitPersonnel());
		}
		else
		{
			// Unit is null, clear fields
			m_topPanelLabel.setText("");
			m_leaderTextField.setText("");
			m_cellPhoneTextField.setText("");
			m_fiveToneTextField.setText("");
			m_createdTextField.setText("");
			m_callsignTextField.setText("");
			m_fieldTimeTextField.setText("");
			m_assignmentTextField.setText("");
			m_stopTimeTextField.setText("");
			UnitPersonnelTableModel model = (UnitPersonnelTableModel)m_personnelTable.getModel();
			model.setPersonnel(null);
		}
	}
	
	/**
	 * Data model for table containing current unit personnel
	 * 
	 * @author thomasl
	 */
	public class UnitPersonnelTableModel extends AbstractTableModel
	{
		private static final long serialVersionUID = 1L;
		
		IPersonnelListIf m_personnel;
		
		/**
		 * Sets the current personnel and updates table
		 * @param personnel
		 */
		public void setPersonnel(IPersonnelListIf personnel)
		{
			m_personnel = personnel;
			fireTableDataChanged();
		}

		@Override
		public String getColumnName(int column)
		{
			return null;
		}
		
		public int getColumnCount()
		{
			return 3;
		}

		public int getRowCount()
		{
			return m_personnel == null ? 0 : m_personnel.size();
		}

		public Object getValueAt(int row, int column)
		{
			IPersonnelIf personnel = (IPersonnelIf)m_personnel.getItems().toArray()[row];
			switch(column)
			{
			case 0:
				return personnel.getFirstname() + " " + personnel.getLastname();
			case 1:
				return personnel.getTelephone1();
			}
			return null;
		}

		public IPersonnelIf getPersonnel(int selectedRow)
		{
			return m_personnel == null ? null : (IPersonnelIf)m_personnel.getItems().toArray()[selectedRow];
		}

		public IPersonnelListIf getPersonnel()
		{
			return m_personnel;
		}
	}

	/**
	 * @return Current unit
	 */
	public IUnitIf getUnit()
	{
		return m_currentUnit;
	}
}
