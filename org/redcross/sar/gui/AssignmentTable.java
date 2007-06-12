package org.redcross.sar.gui;

import java.awt.Dimension;
import java.util.Hashtable;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import org.redcross.sar.app.Utils;
import org.redcross.sar.gui.models.AssignmentTableModel;
import org.redcross.sar.gui.models.AssignmentTableModel.EditAction;
import org.redcross.sar.gui.renderers.AssignmentTableCellRenderer;
import org.redcross.sar.gui.renderers.AssignmentTableStringConverter;
import org.redcross.sar.gui.renderers.BooleanTableCellRenderer;
import org.redcross.sar.gui.renderers.EditActionTableCellEditor;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.AssignmentImpl;
import org.redcross.sar.mso.data.IAssignmentIf.AssignmentStatus;

public class AssignmentTable extends JTable {

	private static final long serialVersionUID = 1L;
	private TableRowSorter<AssignmentTableModel> tableRowSorter = null;
	private Hashtable<AssignmentStatus, RowFilter<?, ?>> rowFilters = null;
	
	@SuppressWarnings("unchecked")
	public AssignmentTable(IMsoModelIf msoModel) {
		AssignmentTableModel model = new AssignmentTableModel(msoModel);
		setModel(model);
		
		tableRowSorter = new TableRowSorter(model);
		tableRowSorter.setStringConverter(new AssignmentTableStringConverter());
		setRowSorter(tableRowSorter);
		
		setDefaultRenderer(EditAction.class, 
				new EditActionTableCellEditor());
		setDefaultRenderer(Boolean.class, 
				new BooleanTableCellRenderer());
		setDefaultRenderer(AssignmentImpl.class, 
				new AssignmentTableCellRenderer());
		
		setRowHeight(34);
		setColumnSelectionAllowed(false);
		setIntercellSpacing(new Dimension(10, 1));
		setColumnWidths();
	}
	
	private void setColumnWidths() {
		for (int i = 0; i < 4; i++) {
			TableColumn column = getColumnModel().getColumn(i);
			column.setResizable(false);
			switch(i) {
				case 0: 
					column.setPreferredWidth(45);
					break;
				case 1:
					column.setPreferredWidth(175);
					break;
				case 2: 
					column.setPreferredWidth(175);
					break;
				case 3: 
					column.setPreferredWidth(225);
					break;
			}
		}
	}
	
	public void setEditActionEditor(AbstractCellEditor editor) {
		TableColumn editColumn = getColumnModel().getColumn(3);
		editColumn.setCellEditor((TableCellEditor)editor);
	}
	
	@SuppressWarnings("unchecked")
	public void showOnly(Object obj) {
		if (rowFilters == null) {
			rowFilters = new Hashtable<AssignmentStatus, RowFilter<?, ?>>();
			AssignmentStatus[] values = AssignmentStatus.values();
			for (int i = 0; i < values.length; i++) {
				rowFilters.put(values[i], RowFilter.regexFilter(".*"+Utils.translate(values[i])+".*"));
			}
		}
		tableRowSorter.setRowFilter((RowFilter)rowFilters.get(obj));
	}
}
