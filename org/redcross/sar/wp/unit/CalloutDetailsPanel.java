package org.redcross.sar.wp.unit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.redcross.sar.gui.DiskoButtonFactory;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.ICalloutIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IPersonnelIf;
import org.redcross.sar.mso.data.IPersonnelIf.PersonnelStatus;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.Update;
import org.redcross.sar.util.mso.DTG;
import org.redcross.sar.util.mso.Selector;

/**
 * JPanel displaying alert details
 * 
 * @author thomasl
 */
public class CalloutDetailsPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private JLabel m_topLabel;
	private JButton m_printButton;
	
	private JTextField m_titleTextField;
	private JTextField m_createdTextField;
	private JTextField m_organizationTextField;
	private JTextField m_departmentTextField;
	private JTable m_personnelTable;
	
	private ICalloutIf m_callout;
	
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
		m_topLabel = new JLabel();
		topPanel.add(m_topLabel, BorderLayout.CENTER);
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
		m_personnelTable = new JTable(new CallOutPersonnelTableModel(null));
		CallOutPersonnelStatusEditor editor = new CallOutPersonnelStatusEditor();
		m_personnelTable.setRowSelectionAllowed(false);
		m_personnelTable.setColumnSelectionAllowed(false);
		m_personnelTable.setRowHeight(DiskoButtonFactory.TABLE_BUTTON_SIZE.height + 10);
		TableColumn column = m_personnelTable.getColumnModel().getColumn(2);
		column.setCellEditor(editor);
		column.setCellRenderer(editor);
		column.setPreferredWidth(DiskoButtonFactory.TABLE_BUTTON_SIZE.width * 2 + 15);
		column.setMaxWidth(DiskoButtonFactory.TABLE_BUTTON_SIZE.width * 2 + 15);
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
	
	/**
	 * Update field contents based on current call-out
	 */
	public void updateFieldContents()
	{
		if(m_callout == null)
		{
			m_topLabel.setText("");
			m_titleTextField.setText("");
			m_createdTextField.setText("");
			m_organizationTextField.setText("");
			m_departmentTextField.setText("");
		}
		else
		{
			String topText = m_resources.getString("CallOut.text") + " " + DTG.CalToDTG(m_callout.getCreated());
			m_topLabel.setText(topText);
			
			m_titleTextField.setText(m_callout.getTitle());
			
			String created = DTG.CalToDTG(m_callout.getCreated());
			m_createdTextField.setText(created);
			
			m_organizationTextField.setText(m_callout.getOrganization());
			
			m_departmentTextField.setText(m_callout.getDepartment());
			
			CallOutPersonnelTableModel model = (CallOutPersonnelTableModel)m_personnelTable.getModel();
			model.setCallOut(m_callout);
		}
	}
	
	/*
	 * Setters and getters
	 */
	public ICalloutIf getCallOut()
	{
		return m_callout;
	}
	
	public void setCallOut(ICalloutIf callout)
	{
		m_callout = callout;
	}
	
	/**
	 * Personnel data for current call-out in details panel
	 * 
	 * @author thomasl
	 */
	private class CallOutPersonnelTableModel extends AbstractTableModel implements IMsoUpdateListenerIf
	{
		private static final long serialVersionUID = 1L;
		
		List<IPersonnelIf> m_personnel;
		
		private final Selector<IPersonnelIf> m_personnelSelector = new Selector<IPersonnelIf>()
		{
			public boolean select(IPersonnelIf anObject)
			{
				return true;
			}
		};
		
		private final Comparator<IPersonnelIf> m_personnelComparator = new Comparator<IPersonnelIf>()
		{
			public int compare(IPersonnelIf arg0, IPersonnelIf arg1)
			{
				int res = arg0.getFirstname().compareTo(arg1.getFirstname());
				return res == 0 ? arg0.getLastname().compareTo(arg1.getLastname()) : res;
			}
		};
		
		public CallOutPersonnelTableModel(ICalloutIf callout)
		{
			m_callout = callout;
			m_personnel = new LinkedList<IPersonnelIf>();
			buildTable();
		}

		public int getColumnCount()
		{
			return 3;
		}

		public int getRowCount()
		{
			return m_personnel.size();
		}

		public Object getValueAt(int row, int column)
		{
			IPersonnelIf personnel = m_personnel.get(row);
			switch(column)
			{
			case 0:
				return personnel.getFirstname() + " " + personnel.getLastname();
			case 1:
				break;
			case 2:
				return personnel;
			}
			return null;
		}
		
		@Override
		public String getColumnName(int column)
		{
			return null;
		}
		
		@Override
		public boolean isCellEditable(int row, int column)
		{
			return row == 2;
		}
		
		private void buildTable()
		{
			if(m_callout != null)
			{
				m_personnel.clear();
				m_personnel.addAll(m_callout.getPersonnelList().selectItems(m_personnelSelector, m_personnelComparator));
			}
		}

		public void handleMsoUpdateEvent(Update e)
		{
			buildTable();
			fireTableDataChanged();
		}
		
		public boolean hasInterestIn(IMsoObjectIf msoObject)
		{
			return msoObject.getMsoClassCode() == IMsoManagerIf.MsoClassCode.CLASSCODE_CALLOUT;
		}
		
		public void setCallOut(ICalloutIf callout)
		{
			m_callout = callout;
			buildTable();
			fireTableDataChanged();
		}
		
		public ICalloutIf getCallOut()
		{
			return m_callout;
		}
	}
	
	/**
	 * Column editor for call-out personnel status changes
	 * 
	 * @author thomasl
	 */
	private class CallOutPersonnelStatusEditor extends AbstractCellEditor 
		implements TableCellEditor, TableCellRenderer
	{
		private static final long serialVersionUID = 1L;
		
		private JPanel m_panel;
		private JButton m_arrivedButton;
		private JButton m_releaseButton;
		
		private int m_editingRow;
		
		public CallOutPersonnelStatusEditor()
		{
			m_panel = new JPanel();
			m_panel.setBackground(m_personnelTable.getBackground());
			
			m_arrivedButton = DiskoButtonFactory.createTableButton(m_resources.getString("ArrivedButton.letter"));
			m_arrivedButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					// Set personnel status to arrived
					CallOutPersonnelTableModel model = (CallOutPersonnelTableModel)m_personnelTable.getModel();
					IPersonnelIf personnel = (IPersonnelIf)model.getValueAt(m_editingRow, 2);
					PersonnelUtilities.arrivedPersonnel(personnel);
					fireEditingStopped();
				}
			});
			m_panel.add(m_arrivedButton);
			
			m_releaseButton = DiskoButtonFactory.createTableButton(m_resources.getString("ReleaseButton.letter"));
			m_releaseButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					// Release personnel
					CallOutPersonnelTableModel model = (CallOutPersonnelTableModel)m_personnelTable.getModel();
					IPersonnelIf personnel = (IPersonnelIf)model.getValueAt(m_editingRow, 2);
					PersonnelUtilities.releasePersonnel(personnel);
					fireEditingStopped();
				}
			});
			m_panel.add(m_releaseButton);
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column)
		{
			updateCell(row);
			return m_panel;
		}

		public Component getTableCellEditorComponent(JTable table, Object value,
				boolean arg2, int row, int column)
		{
			m_editingRow = row;
			return m_panel;
		}

		public Object getCellEditorValue()
		{
			return null;
		}
		
		private void updateCell(int row)
		{
			CallOutPersonnelTableModel model = (CallOutPersonnelTableModel)m_personnelTable.getModel();
			IPersonnelIf personnel = (IPersonnelIf)model.getValueAt(row, 2);
			
			m_arrivedButton.setSelected(personnel.getStatus() == PersonnelStatus.ARRIVED);
			m_releaseButton.setSelected(personnel.getStatus() == PersonnelStatus.RELEASED);
		}
	}
}
