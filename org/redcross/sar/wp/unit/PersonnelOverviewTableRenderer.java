package org.redcross.sar.wp.unit;

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ResourceBundle;

import javax.swing.AbstractCellEditor;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.redcross.sar.gui.DiskoButtonFactory;

/**
 * The personnel overview table renderer
 * 
 * @author thomasl
 */
public class PersonnelOverviewTableRenderer
{
	private static final ResourceBundle m_resources = ResourceBundle.getBundle("org.redcross.sar.wp.unit.unit");
	private JTable m_table;
	
	public void setTable(JTable overviewTable)
	{
		m_table = overviewTable;
		EditPersonnelCellEditor editPersonnel = new EditPersonnelCellEditor();
		TableColumn column = m_table.getColumnModel().getColumn(1);
		column.setCellEditor(editPersonnel);
		column.setCellRenderer(editPersonnel);
		
		PersonnelStatusCellEditor personnelStatusEditor = new PersonnelStatusCellEditor();
		column = m_table.getColumnModel().getColumn(2);
		column.setCellEditor(personnelStatusEditor);
		column.setCellRenderer(personnelStatusEditor);
	}
	
	/**
	 * Cell editor and renderer for changing personnel details
	 * 
	 * @author thomasl
	 */
	public class EditPersonnelCellEditor extends AbstractCellEditor 
		implements TableCellEditor, TableCellRenderer 
	{
		private static final long serialVersionUID = 1L;
		JToggleButton m_editButton;
		JPanel m_panel;
		
		public EditPersonnelCellEditor()
		{
			m_panel = new JPanel();
			m_panel.setBackground(m_table.getBackground());
			
			m_editButton = DiskoButtonFactory.createTableToggleButton(m_resources.getString("EditButton.letter"));
			m_panel.add(m_editButton);
		}

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column)
		{
			// TODO Auto-generated method stub
			return m_panel;
		}

		public Object getCellEditorValue()
		{
			// TODO Auto-generated method stub
			return null;
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column)
		{
			// TODO Auto-generated method stub
			return m_panel;
		}
	}
	
	/**
	 * Cell renderer and editor for changign personnel status
	 * 
	 * @author thomasl
	 */
	public class PersonnelStatusCellEditor extends AbstractCellEditor
		implements TableCellEditor, TableCellRenderer
	{
		private static final long serialVersionUID = 1L;
		
		private JPanel m_panel;
		
		private JToggleButton m_calloutButton;
		private JToggleButton m_arrivedButton;
		private JToggleButton m_dismissedButton;
		
		public PersonnelStatusCellEditor()
		{
			m_panel = new JPanel();
			m_panel.setBackground(m_table.getBackground());
			
			FlowLayout fl = new FlowLayout();
			fl.setAlignment(FlowLayout.RIGHT);
			m_panel.setLayout(fl);
			
			m_calloutButton = DiskoButtonFactory.createTableToggleButton(m_resources.getString("CalloutButton.letter"));
			m_panel.add(m_calloutButton);
			
			m_arrivedButton = DiskoButtonFactory.createTableToggleButton(m_resources.getString("ArrivedButton.letter"));
			m_panel.add(m_arrivedButton);
			
			m_dismissedButton = DiskoButtonFactory.createTableToggleButton(m_resources.getString("DismissedButton.letter"));
			m_panel.add(m_dismissedButton);
		}

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column)
		{
			// TODO Auto-generated method stub
			return m_panel;
		}

		public Object getCellEditorValue()
		{
			// TODO Auto-generated method stub
			return null;
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column)
		{
			// TODO Auto-generated method stub
			return m_panel;
		}

	}
}
