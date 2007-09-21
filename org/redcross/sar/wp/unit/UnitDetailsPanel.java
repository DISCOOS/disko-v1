package org.redcross.sar.wp.unit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumSet;
import java.util.ResourceBundle;

import javax.swing.AbstractCellEditor;
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
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.redcross.sar.gui.DiskoButtonFactory;
import org.redcross.sar.gui.ErrorDialog;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IPersonnelIf;
import org.redcross.sar.mso.data.IPersonnelListIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IUnitIf.UnitStatus;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.Update;
import org.redcross.sar.util.except.IllegalOperationException;

/**
 * JPanel displaying unit details
 * 
 * @author thomasl
 */
public class UnitDetailsPanel extends JPanel implements IMsoUpdateListenerIf
{
	private static final long serialVersionUID = 1L;
	
	private final static ResourceBundle m_resources = ResourceBundle.getBundle("org.redcross.sar.wp.unit.unit");
	
	private IUnitIf m_currentUnit;
	
	private JLabel m_topPanelLabel;
	private JToggleButton m_pauseToggleButton;
	private JButton m_releaseButton;
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
	
	public UnitDetailsPanel(IDiskoWpUnit wp)
	{
		wp.getMmsoEventManager().addClientUpdateListener(this);
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
		m_pauseToggleButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if(m_currentUnit != null)
				{
					try
					{
						UnitUtilities.toggleUnitPause(m_currentUnit);
						
						// Commit small changes right away (?)
						DiskoWpUnitImpl.commit();
					} 
					catch (IllegalOperationException e)
					{
					}
				}
			}
		});
		topButtonsPanel.add(m_pauseToggleButton);
		m_releaseButton = DiskoButtonFactory.createSmallButton(m_resources.getString("DissolveButton.text")/*, ""*/);
		m_releaseButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				// Try to release unit
				IUnitIf unit = DiskoWpUnitImpl.getEditingUnit();
				
				try
				{
					UnitUtilities.releaseUnit(unit);
					
					// Commit
					DiskoWpUnitImpl.commit();
				} 
				catch (IllegalOperationException e)
				{
					ErrorDialog error = new ErrorDialog(null);
					error.showError(m_resources.getString("ReleaseUnitError.header"),
							m_resources.getString("ReleaseUnitError.text"));
				}
			}
		});
		topButtonsPanel.add(m_releaseButton);
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
		UnitLeaderColumnRenderer leaderRenderer = new UnitLeaderColumnRenderer();
		leaderRenderer.setTable(m_personnelTable);
		
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
			
			// Pause button
			m_pauseToggleButton.setSelected(m_currentUnit.getStatus() == UnitStatus.PAUSED);
			
			// Released button
			m_releaseButton.setSelected(m_currentUnit.getStatus() == UnitStatus.RELEASED);
			
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
	 * @return Current unit
	 */
	public IUnitIf getUnit()
	{
		return m_currentUnit;
	}

	/**
	 * Display any updates made to current editing unit
	 */
	public void handleMsoUpdateEvent(Update e)
	{
		IUnitIf unit = (IUnitIf)e.getSource();
		if(unit == m_currentUnit)
		{
			updateComponents();
		}
	}

	/**
	 * Interested in unit updates
	 */
	EnumSet<IMsoManagerIf.MsoClassCode> interestedIn = EnumSet.of(IMsoManagerIf.MsoClassCode.CLASSCODE_UNIT);
	public boolean hasInterestIn(IMsoObjectIf msoObject)
	{
		return interestedIn.contains(msoObject.getMsoClassCode());
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
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) 
		{
			return columnIndex == 2;
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
	 * Renderer and editor for the leader selection column
	 * 
	 * @author thomasl
	 */
	private class UnitLeaderColumnRenderer extends AbstractCellEditor implements TableCellEditor, TableCellRenderer
	{
		private static final long serialVersionUID = 1L;
		
		private JPanel m_panel;
		private JButton m_leaderButton;
		JTable m_table;
		private int m_editingRow;
		
		public UnitLeaderColumnRenderer()
		{
		
			m_panel = new JPanel();
			
			m_leaderButton = DiskoButtonFactory.createTableButton(m_resources.getString("LeaderButton.letter"));
			m_leaderButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					// Set unit leader to selected personnel
					IUnitIf editingUnit = DiskoWpUnitImpl.getEditingUnit();
					
					int index = m_table.convertRowIndexToModel(m_editingRow);
					UnitPersonnelTableModel model = (UnitPersonnelTableModel)m_table.getModel();
					IPersonnelIf newLeader = model.getPersonnel(index);
					
					editingUnit.setUnitLeader(newLeader);
					
					// Commit changes
					DiskoWpUnitImpl.commit();
					
					fireEditingStopped();
				}
			});
			m_panel.add(m_leaderButton);
		}
		
		public void setTable(JTable table)
		{
			m_table = table;
			m_panel.setBackground(m_table.getBackground());
			
			TableColumn column = m_table.getColumnModel().getColumn(2);
			column.setCellEditor(this);
			column.setCellRenderer(this);
			column.setWidth(DiskoButtonFactory.TABLE_BUTTON_SIZE.width + 10);
			m_table.setRowHeight(DiskoButtonFactory.TABLE_BUTTON_SIZE.height + 10);
		}
		
		
		public Component getTableCellEditorComponent(JTable arg0, Object arg1,
				boolean arg2, int row, int column)
		{
			m_editingRow = row;
			return m_panel;
		}
		
		
		public Object getCellEditorValue()
		{
			return null;
		}
		
		
		public Component getTableCellRendererComponent(JTable arg0,
				Object arg1, boolean arg2, boolean arg3, int row, int column)
		{
			int index = m_table.convertRowIndexToModel(row);
			UnitPersonnelTableModel model = (UnitPersonnelTableModel)m_table.getModel();
			IPersonnelIf personnel  = model.getPersonnel(index);
			
			IUnitIf editingUnit = DiskoWpUnitImpl.getEditingUnit();
			
			m_leaderButton.setSelected(editingUnit.getUnitLeader() == personnel);
			
			return m_panel;
		}
	}
}
