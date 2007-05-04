package org.redcross.sar.wp.tacktics;

import java.awt.Frame;

import org.redcross.sar.gui.DiskoDialog;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.border.BevelBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JTextArea;

public class HypothesesDialog extends DiskoDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel = null;
	private JPanel buttonPanel = null;
	private JButton newButton = null;
	private JScrollPane listScrollPane = null;
	private JList hypothesesList = null;
	private JPanel centerPanel = null;
	private JScrollPane textAreaScrollPane = null;
	private JTextArea descriptionTextArea = null;
	private JPanel propertiesPanel;
	private JLabel statusLabel;
	private JLabel priorityLabel;
	private JComboBox priorityComboBox;
	private JComboBox statusComboBox;

	public HypothesesDialog(Frame owner) {
		super(owner);
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
            this.setPreferredSize(new Dimension(175, 500));
            this.setSize(new Dimension(200, 500));
            this.pack();
		}
		catch (java.lang.Throwable e) {
			//  Do Something
		}
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
				contentPanel.add(getListScrollPane(), BorderLayout.NORTH);
				contentPanel.add(getCenterPanel(), BorderLayout.CENTER);
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
				flowLayout.setAlignment(FlowLayout.RIGHT);
				buttonPanel = new JPanel();
				buttonPanel.setLayout(flowLayout);
				buttonPanel.add(getNewButton(), null);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return buttonPanel;
	}

	/**
	 * This method initializes newButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getNewButton() {
		if (newButton == null) {
			try {
				newButton = new JButton();
				newButton.setPreferredSize(new Dimension(50, 50));
				newButton.setText("NY");
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return newButton;
	}

	/**
	 * This method initializes listScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getListScrollPane() {
		if (listScrollPane == null) {
			try {
				listScrollPane = new JScrollPane();
				listScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				listScrollPane.setPreferredSize(new Dimension(150, 350));
				listScrollPane.setViewportView(getHypothesesList());
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return listScrollPane;
	}

	/**
	 * This method initializes hypothesesList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getHypothesesList() {
		if (hypothesesList == null) {
			try {
				hypothesesList = new JList();
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return hypothesesList;
	}

	/**
	 * This method initializes centerPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCenterPanel() {
		if (centerPanel == null) {
			try {
				centerPanel = new JPanel();
				centerPanel.setLayout(new BorderLayout());
				centerPanel.add(getButtonPanel(), BorderLayout.NORTH);
				centerPanel.add(getTextAreaScrollPane(), BorderLayout.CENTER);
				centerPanel.add(getPropertiesPanel(), BorderLayout.SOUTH);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return centerPanel;
	}

	/**
	 * This method initializes textAreaScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getTextAreaScrollPane() {
		if (textAreaScrollPane == null) {
			try {
				textAreaScrollPane = new JScrollPane();
				textAreaScrollPane.setViewportView(getDescriptionTextArea());
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return textAreaScrollPane;
	}

	/**
	 * This method initializes descriptionTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getDescriptionTextArea() {
		if (descriptionTextArea == null) {
			try {
				descriptionTextArea = new JTextArea();
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return descriptionTextArea;
	}
	
	/**
	 * This method initializes propertiesPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPropertiesPanel() {
		if (propertiesPanel == null) {
			try {
				GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
				gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
				gridBagConstraints11.gridy = 1;
				gridBagConstraints11.weightx = 1.0;
				gridBagConstraints11.anchor = GridBagConstraints.WEST;
				gridBagConstraints11.insets = new Insets(10, 0, 10, 0);
				gridBagConstraints11.gridx = 1;
				GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
				gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
				gridBagConstraints1.gridy = 0;
				gridBagConstraints1.weightx = 1.0;
				gridBagConstraints1.anchor = GridBagConstraints.WEST;
				gridBagConstraints1.insets = new Insets(10, 0, 0, 0);
				gridBagConstraints1.gridx = 1;
				GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
				gridBagConstraints2.gridx = 0;
				gridBagConstraints2.insets = new Insets(10, 5, 0, 5);
				gridBagConstraints2.anchor = GridBagConstraints.WEST;
				gridBagConstraints2.gridy = 0;
				GridBagConstraints gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.gridx = 0;
				gridBagConstraints.anchor = GridBagConstraints.WEST;
				gridBagConstraints.insets = new Insets(10, 5, 10, 0);
				gridBagConstraints.gridy = 1;
				statusLabel = new JLabel();
				statusLabel.setText("Status:");
				priorityLabel = new JLabel();
				priorityLabel.setText("Prioritet:");
				propertiesPanel = new JPanel();
				propertiesPanel.setLayout(new GridBagLayout());
				propertiesPanel.add(priorityLabel, gridBagConstraints2);
				propertiesPanel.add(statusLabel, gridBagConstraints);
				propertiesPanel.add(getPriorityComboBox(), gridBagConstraints1);
				propertiesPanel.add(getStatusComboBox(), gridBagConstraints11);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return propertiesPanel;
	}
	
	/**
	 * This method initializes priorityComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getPriorityComboBox() {
		if (priorityComboBox == null) {
			try {
				priorityComboBox = new JComboBox();
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return priorityComboBox;
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
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return statusComboBox;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
