package org.redcross.sar.wp.unit;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.redcross.sar.gui.DiskoButtonFactory;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IUnitIf.UnitStatus;

/**
 * The renderer for unit overview table
 * 
 * @author thomasl
 */
public class UnitOverviewTableRenderer
{
	private static ResourceBundle m_resources = ResourceBundle.getBundle("org.redcross.sar.wp.unit.unit");
	private JTable m_table;
	
	/**
	 * Set column renderer and editor. Column widths, as well as table row height
	 * 
	 * @param unitTable
	 */
	public void setTable(JTable unitTable)
	{
		m_table = unitTable;

		TableColumn column = m_table.getColumnModel().getColumn(1);

		EditUnitCellEditor editUnit = new EditUnitCellEditor();
		column.setCellEditor(editUnit);
		column.setCellRenderer(editUnit);
		column.setPreferredWidth(DiskoButtonFactory.TABLE_BUTTON_SIZE.width + 10);
		column.setMaxWidth(DiskoButtonFactory.TABLE_BUTTON_SIZE.width + 10);

		UnitStatusCellEditor unitStatusEditor = new UnitStatusCellEditor();
		column = m_table.getColumnModel().getColumn(2);
		column.setCellEditor(unitStatusEditor);
		column.setCellRenderer(unitStatusEditor);
		column.setPreferredWidth(DiskoButtonFactory.TABLE_BUTTON_SIZE.width * 2 + 15);
		column.setMaxWidth(DiskoButtonFactory.TABLE_BUTTON_SIZE.width * 2 + 15);
		
		m_table.setRowHeight(DiskoButtonFactory.TABLE_BUTTON_SIZE.height + 10);
	}
	
	/**
	 * Cell editor and renderer for the change unit cell in table
	 * 
	 * @author thomasl
	 */
	public class EditUnitCellEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer
	{
		private static final long serialVersionUID = 1L;
		
		private int m_editingRow;
		
		private JPanel m_panel;
		private JButton m_editButton;
		
		public EditUnitCellEditor()
		{
			m_panel = new JPanel();
			m_panel.setBackground(m_table.getBackground());
			
			m_editButton = DiskoButtonFactory.createTableButton(m_resources.getString("EditButton.letter"));
			m_editButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					// Set unit i unit details view
					int index = m_table.convertRowIndexToModel(m_editingRow);
					UnitOverviewTableModel model = (UnitOverviewTableModel)m_table.getModel();
					IUnitIf unit = model.getUnit(index);
					System.err.println("Selected unit: " + unit);
					DiskoWpUnitImpl.setUnit(unit);
					DiskoWpUnitImpl.setDetailView(DiskoWpUnitImpl.UNIT_VIEW_ID);
				}
			});
			m_panel.add(m_editButton);
		}

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column)
		{
			m_editingRow = row;
			return m_panel;
		}

		public Object getCellEditorValue()
		{
			return null;
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column)
		{
			fireEditingStopped();
			updateCell(row);
			return m_panel;
		}
		
		private void updateCell(int row)
		{
			IUnitIf editingUnit = DiskoWpUnitImpl.getEditingUnit();
			
			// Get unit at row
			int index = m_table.convertRowIndexToModel(row);
			UnitOverviewTableModel model = (UnitOverviewTableModel)m_table.getModel();
			IUnitIf rowUnit = model.getUnit(index);
			
			m_editButton.setSelected(editingUnit == rowUnit);
		}
	}
	
	/**
	 * Cell editor and renderer for unit status in the unit table
	 * 
	 * @author thomasl
	 */
	public class UnitStatusCellEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer
	{
		private static final long serialVersionUID = 1L;
		
		private int m_editingRow;
		
		private JPanel m_panel;
		private JButton m_pauseButton;
		private JCheckBox m_releaseCheckButton;
		
		public UnitStatusCellEditor()
		{
			m_panel = new JPanel();
			m_panel.setBackground(m_table.getBackground());
			
			m_pauseButton = DiskoButtonFactory.createTableButton(m_resources.getString("PauseButton.letter"));
			m_pauseButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent arg0)
				{
					// Toggle working status
					int index = m_table.convertRowIndexToModel(m_editingRow);
					UnitOverviewTableModel model = (UnitOverviewTableModel)m_table.getModel();
					IUnitIf unit = model.getUnit(index);
				
					// TODO Only possible from status ready
					
					if(unit.getStatus() == UnitStatus.PAUSED)
					{
						unit.setStatus(UnitStatus.WORKING);
					}
					else
					{
						unit.setStatus(UnitStatus.PAUSED);
					}
				}
			});
			m_panel.add(m_pauseButton);
			
			m_releaseCheckButton = new JCheckBox();
			m_releaseCheckButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					// Release unit
				}
			});
			m_panel.add(m_releaseCheckButton);
		}

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column)
		{
			m_editingRow = row;
			updateCell(row);
			return m_panel;
		}

		public Object getCellEditorValue()
		{
			return null;
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column)
		{
			updateCell(row);
			fireEditingStopped();
			return m_panel;
		}
		
		private void updateCell(int row)
		{
			int index = m_table.convertRowIndexToModel(row);
			UnitOverviewTableModel model = (UnitOverviewTableModel)m_table.getModel();
			
			IUnitIf unit = model.getUnit(index);
			
			// Update buttons
			m_pauseButton.setSelected(unit.getStatus() == UnitStatus.PAUSED);
			m_releaseCheckButton.setSelected(unit.getStatus() != UnitStatus.RELEASED);
		}
	}
}
