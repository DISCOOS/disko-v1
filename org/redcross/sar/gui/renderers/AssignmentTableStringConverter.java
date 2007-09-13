package org.redcross.sar.gui.renderers;

import javax.swing.table.TableModel;
import javax.swing.table.TableStringConverter;

import org.redcross.sar.app.Utils;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.ISearchIf;

public class AssignmentTableStringConverter extends TableStringConverter {

	@Override
	public String toString(TableModel model, int row, int column) {
		Object value = model.getValueAt(row, column);
		if (value instanceof IAssignmentIf) {
			IAssignmentIf assignment = (IAssignmentIf)value;
			String text = null;
			if (column == 1) {
				text = Utils.translate(assignment instanceof ISearchIf ? 
						((ISearchIf)assignment).getSubType() : assignment.getType()) + " " + assignment.getNumber();
			}
			else if (column == 2) {
				text = Utils.translate(assignment.getStatus());
			}
			return text;
		}
		return value.toString();
	}
}
