package org.redcross.sar.wp.tacktics;

import java.awt.Dimension;
import java.awt.Frame;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.renderers.CheckableListCellRenderer;
import org.redcross.sar.mso.data.IAssignmentIf;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JList;
import java.awt.BorderLayout;

public class PriorityDialog extends DiskoDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel = null;
	private IAssignmentIf.AssignmentPriority priority = IAssignmentIf.AssignmentPriority.MEDIUM;
	private JList priorityList = null;
	
	public PriorityDialog(Frame frame) {
		super(frame);
		initialize();
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		try {
            this.setContentPane(getContentPanel());
            this.setPreferredSize(new Dimension(100, 80));
            this.pack();
		}
		catch (java.lang.Throwable e) {
			//  Do Something
		}
	}
	
	public IAssignmentIf.AssignmentPriority getPriority() {
		return priority;
	}

	/**
	 * This method initializes contentPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getContentPanel() {
		if (contentPanel == null) {
			try {
				contentPanel = new JPanel();
				contentPanel.setLayout(new BorderLayout());
				contentPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				contentPanel.add(getPriorityList(), BorderLayout.CENTER);
				
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return contentPanel;
	}

	/**
	 * This method initializes priorityList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getPriorityList() {
		if (priorityList == null) {
			try {
				priorityList = new JList();
				priorityList.setCellRenderer(new CheckableListCellRenderer());
				IAssignmentIf.AssignmentPriority[] values = IAssignmentIf.AssignmentPriority.values();
				Object[] listData = new Object[values.length];
				for (int i = 0; i < values.length; i++) {
					listData[i] = values[i];
				}
				priorityList.setListData(listData);
				priorityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				priorityList.setSelectedIndex(1);
				priorityList.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						priority = (IAssignmentIf.AssignmentPriority)priorityList.getSelectedValue();
						setVisible(false);
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return priorityList;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
