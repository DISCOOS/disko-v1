package org.redcross.sar.wp.tactics;

import org.redcross.sar.app.Utils;
import org.redcross.sar.gui.AssignmentTable;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.renderers.EditActionTableCellEditor;
import org.redcross.sar.gui.renderers.SimpleListCellRenderer;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IAssignmentIf.AssignmentStatus;
import org.redcross.sar.util.except.IllegalOperationException;
import org.redcross.sar.wp.tactics.IDiskoWpTactics.TacticsTaskType;

import com.esri.arcgis.interop.AutomationException;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import java.awt.Color;
import java.io.IOException;

public class ListDialog extends DiskoDialog {

	private static final long serialVersionUID = 1L;
	private DiskoWpTacticsImpl wp = null;
	private Dimension buttonSize = null;
	private JPanel contentPanel = null;
	private JPanel buttonPanel = null;
	private JButton printButton = null;
	private JButton makeReadyButton = null;
	private JScrollPane tableScrollPane = null;
	private AssignmentTable assignmentTable = null;
	private JLabel statusLabel = null;
	private JComboBox statusComboBox = null;
	
	public ListDialog(DiskoWpTacticsImpl wp) {
		super(wp.getApplication().getFrame());
		this.wp = wp;
		buttonSize = wp.getApplication().getUIFactory().getSmallButtonSize();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		try {
            this.setPreferredSize(new Dimension(593, 600));
            this.setContentPane(getContentPanel());
			this.pack();
		}
		catch (java.lang.Throwable e) {
			//  Do Something
		}
	}
	
	private void makeReady() {
		try {
			JTable table = getAssignmentTable();
			int[] selection = table.getSelectedRows();
			for (int i = 0; i < selection.length; i++) {
				int row = selection[i];
				IAssignmentIf assignment = (IAssignmentIf)table.getValueAt(row,1);
				assignment.setStatus(IAssignmentIf.AssignmentStatus.READY);
			}
		} catch (IllegalOperationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void enableButtons() {
		int[] selection = assignmentTable.getSelectedRows();
		boolean enable = selection != null && selection.length > 0;
		getMakeReadyButton().setEnabled(enable);
		getPrintButton().setEnabled(enable);
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
				contentPanel.add(getButtonPanel(), BorderLayout.SOUTH);
				contentPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				contentPanel.add(getTableScrollPane(), BorderLayout.CENTER);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return contentPanel;
	}

	/**
	 * This method initializes buttonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			try {
				FlowLayout flowLayout = new FlowLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				statusLabel = new JLabel();
				statusLabel.setText("Vis status:");
				buttonPanel = new JPanel();
				buttonPanel.setLayout(flowLayout);
				buttonPanel.add(getPrintButton(), null);
				buttonPanel.add(getMakeReadyButton(), null);
				buttonPanel.add(statusLabel, null);
				buttonPanel.add(getStatusComboBox(), null);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return buttonPanel;
	}

	/**
	 * This method initializes makeReadyButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getMakeReadyButton() {
		if (makeReadyButton == null) {
			try {
				makeReadyButton = new JButton();
				Enum key = TacticsTaskType.MAKE_READY_TASK;
				ImageIcon icon = Utils.getIcon(key);
				if (icon != null) {
					makeReadyButton.setIcon(icon);
				} else {
					makeReadyButton.setText(key.name());
				}
				makeReadyButton.setPreferredSize(buttonSize);
				makeReadyButton.setEnabled(false);
				makeReadyButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						makeReady();
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return makeReadyButton;
	}
	
	/**
	 * This method initializes printButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getPrintButton() {
		if (printButton == null) {
			try {
				printButton = new JButton();
				Enum key = TacticsTaskType.PRINT_TASK;
				ImageIcon icon = Utils.getIcon(key);
				if (icon != null) {
					printButton.setIcon(icon);
				} else {
					printButton.setText(key.name());
				}
				printButton.setPreferredSize(buttonSize);
				printButton.setEnabled(false);
				printButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return printButton;
	}

	/**
	 * This method initializes tableScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getTableScrollPane() {
		if (tableScrollPane == null) {
			try {
				tableScrollPane = new JScrollPane();
				tableScrollPane.getViewport().setBackground(Color.white);
				tableScrollPane.setViewportView(getAssignmentTable());
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return tableScrollPane;
	}

	/**
	 * This method initializes assignmentTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private AssignmentTable getAssignmentTable() {
		if (assignmentTable == null) {
			try {
				assignmentTable = new AssignmentTable(wp.getMsoModel());
				
				EditActionTableCellEditor editor = new EditActionTableCellEditor();
				editor.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						String action = e.getActionCommand();
						int[] rows = assignmentTable.getSelectedRows();
						if (rows != null && rows.length == 1) {
							IAssignmentIf assignment = (IAssignmentIf)assignmentTable.getValueAt(rows[0],1);
							if (action.equals("EDIT")) {
								try {
									wp.getMap().setSelected(assignment, true);
									wp.getMap().zoomToMsoObject(assignment);
								} catch (AutomationException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
							else if (action.equals("COPY")) {
								// copy not yest implemented
							}
							setVisible(false);
						}
					}
				});
				assignmentTable.setEditActionEditor(editor);
				//selection listener
				assignmentTable.getSelectionModel().addListSelectionListener(
						new ListSelectionListener() {

					public void valueChanged(ListSelectionEvent e) {
						//Ignore extra messages.
	                    if (e.getValueIsAdjusting()) return;
						enableButtons();
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return assignmentTable;
	}

	/**
	 * This method initializes statusComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getStatusComboBox() {
		if (statusComboBox == null) {
			try {
				statusComboBox = new JComboBox();
				statusComboBox.setRenderer(new SimpleListCellRenderer());
				statusComboBox.setPreferredSize(new Dimension(125, 20));
				statusComboBox.addItem("SHOW_ALL");
				AssignmentStatus[] values = AssignmentStatus.values();
				for (int i = 0; i < values.length; i++) {
					statusComboBox.addItem(values[i]);
				}
				statusComboBox.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						assignmentTable.showOnly(statusComboBox.getSelectedItem());
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return statusComboBox;
	}


}  //  @jve:decl-index=0:visual-constraint="10,2"
