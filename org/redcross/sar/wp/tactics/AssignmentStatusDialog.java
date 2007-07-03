package org.redcross.sar.wp.tactics;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.redcross.sar.app.Utils;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IAssignmentIf.AssignmentStatus;
import org.redcross.sar.util.except.IllegalOperationException;
import org.redcross.sar.wp.tactics.IDiskoWpTactics.TacticsTaskType;

public class AssignmentStatusDialog extends DiskoDialog {
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPanel = null;
	private JButton draftButton = null;
	private JButton readyButton = null;
	private Dimension buttonSize = null;
	private IAssignmentIf assignment = null;  //  @jve:decl-index=0:

	public AssignmentStatusDialog(DiskoWpTacticsImpl wp) {
		super(wp.getApplication().getFrame());
		buttonSize = wp.getApplication().getUIFactory().getSmallButtonSize();
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
            int size = (int)buttonSize.getWidth();
            this.setPreferredSize(new Dimension(size*2+4, size));
            this.setModal(true);
            this.pack();
		}
		catch (java.lang.Throwable e) {
			//  Do Something
		}
	}
	
	public void setAssignment(IAssignmentIf assignment) {
		this.assignment = assignment;
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
				FlowLayout fl = new FlowLayout();
				fl.setHgap(0);
				fl.setVgap(0);
				contentPanel.setLayout(fl);
				contentPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				contentPanel.add(getReadyButton(), null);
				contentPanel.add(getDraftButton(), null);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return contentPanel;
	}

	/**
	 * This method initializes draftButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getDraftButton() {
		if (draftButton == null) {
			try {
				draftButton = new JButton();
				Enum key = TacticsTaskType.MAKE_DRAFT_TASK;
				ImageIcon icon = Utils.getIcon(key);
				if (icon != null) {
					draftButton.setIcon(icon);
				} else {
					draftButton.setText(key.name());
				}
				draftButton.setPreferredSize(buttonSize);
				draftButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						try {
							assignment.setStatus(AssignmentStatus.DRAFT);
						} catch (IllegalOperationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						setVisible(false);
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return draftButton;
	}

	/**
	 * This method initializes readyButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getReadyButton() {
		if (readyButton == null) {
			try {
				readyButton = new JButton();
				Enum key = TacticsTaskType.MAKE_READY_TASK;
				ImageIcon icon = Utils.getIcon(key);
				if (icon != null) {
					readyButton.setIcon(icon);
				} else {
					readyButton.setText(key.name());
				}
				readyButton.setPreferredSize(buttonSize);
				readyButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						try {
							assignment.setStatus(AssignmentStatus.READY);
						} catch (IllegalOperationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						setVisible(false);
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return readyButton;
	}
}
