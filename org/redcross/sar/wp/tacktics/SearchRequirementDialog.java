package org.redcross.sar.wp.tacktics;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.renderers.SimpleListCellRenderer;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IAssignmentIf.AssignmentPriority;

import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.BorderFactory;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import java.awt.Insets;
import java.util.Hashtable;
import javax.swing.JComboBox;

public class SearchRequirementDialog extends DiskoDialog {

	private static final long serialVersionUID = 1L;
	//private IDiskoApplication app = null;
	private JPanel contentPanel = null;
	private JLabel accuracyLabel = null;
	private JSlider accuracySlider = null;
	private JTextField accuracyTextField = null;
	private JLabel priorityLabel = null;
	private JSlider prioritySlider = null;
	private JTextField priorityTextField = null;
	private JLabel personelLabel = null;
	private JSpinner personelSpinner = null;
	private JLabel progressLabel = null;
	private JSpinner progressSpinner = null;
	private JLabel criticalQuestionsLabel = null;
	private JScrollPane criticalQuestionsScrollPane = null;
	private JTextArea criticalQuestionsTextArea = null;
	private JLabel statusLabel = null;
	private JComboBox statusComboBox = null;
	
	
	public SearchRequirementDialog(IDiskoApplication app) {
		super(app.getFrame());
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
            this.setPreferredSize(new Dimension(800, 230));
            this.setSize(new Dimension(800, 230));
            this.pack();
				
		}
		catch (java.lang.Throwable e) {
			//  Do Something
		}
	}
	
	public int getAccuracy() {
		return getAccuracySlider().getValue();
	}
	
	public AssignmentPriority getPriority() {
		int value = getPrioritySlider().getValue();
		switch (value) {
			case 1: return AssignmentPriority.LOW;
			case 2: return AssignmentPriority.MEDIUM;
			case 3: return AssignmentPriority.HIGH;
		}
		return null;
	}
	
	public int getPersonelNeed() {
		return ((Integer)getPersonelSpinner().getValue()).intValue();
	}
	
	public int getEstimatedProgress() {
		return ((Integer)getProgressSpinner().getValue()).intValue();
	}
	
	public IAssignmentIf.AssignmentStatus getStatus() {
		return (IAssignmentIf.AssignmentStatus)getStatusComboBox().getSelectedItem();
	}
	
	public String getCriticalQuestions() {
		return getCriticalQuestionsTextArea().getText();
	}

	/**
	 * This method initializes contentPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getContentPanel() {
		if (contentPanel == null) {
			try {
				GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
				gridBagConstraints13.fill = GridBagConstraints.NONE;
				gridBagConstraints13.gridy = 4;
				gridBagConstraints13.weightx = 1.0;
				gridBagConstraints13.anchor = GridBagConstraints.WEST;
				gridBagConstraints13.insets = new Insets(0, 0, 5, 0);
				gridBagConstraints13.gridx = 1;
				GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
				gridBagConstraints12.gridx = 0;
				gridBagConstraints12.anchor = GridBagConstraints.WEST;
				gridBagConstraints12.insets = new Insets(0, 10, 0, 0);
				gridBagConstraints12.gridy = 4;
				statusLabel = new JLabel();
				statusLabel.setText("Status:");
				GridBagConstraints gridBagConstraints91 = new GridBagConstraints();
				gridBagConstraints91.fill = GridBagConstraints.NONE;
				gridBagConstraints91.gridx = 2;
				gridBagConstraints91.gridy = 1;
				gridBagConstraints91.anchor = GridBagConstraints.NORTHWEST;
				gridBagConstraints91.weightx = 1.0;
				GridBagConstraints gridBagConstraints81 = new GridBagConstraints();
				gridBagConstraints81.fill = GridBagConstraints.HORIZONTAL;
				gridBagConstraints81.gridx = 1;
				gridBagConstraints81.gridy = 1;
				gridBagConstraints81.anchor = GridBagConstraints.WEST;
				gridBagConstraints81.insets = new Insets(5, 0, 0, 0);
				gridBagConstraints81.weightx = 1.0;
				GridBagConstraints gridBagConstraints71 = new GridBagConstraints();
				gridBagConstraints71.fill = GridBagConstraints.NONE;
				gridBagConstraints71.anchor = GridBagConstraints.NORTHWEST;
				gridBagConstraints71.insets = new Insets(5, 0, 5, 0);
				gridBagConstraints71.weightx = 1.0;
				GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
				gridBagConstraints61.fill = GridBagConstraints.HORIZONTAL;
				gridBagConstraints61.anchor = GridBagConstraints.WEST;
				gridBagConstraints61.insets = new Insets(5, 0, 5, 0);
				gridBagConstraints61.weightx = 1.0;
				GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
				gridBagConstraints11.fill = GridBagConstraints.BOTH;
				gridBagConstraints11.gridy = 5;
				gridBagConstraints11.weightx = 1.0;
				gridBagConstraints11.weighty = 1.0;
				gridBagConstraints11.gridheight = 1;
				gridBagConstraints11.anchor = GridBagConstraints.WEST;
				gridBagConstraints11.insets = new Insets(0, 0, 5, 0);
				gridBagConstraints11.gridx = 1;
				GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
				gridBagConstraints10.gridx = 0;
				gridBagConstraints10.anchor = GridBagConstraints.NORTHWEST;
				gridBagConstraints10.fill = GridBagConstraints.NONE;
				gridBagConstraints10.insets = new Insets(0, 10, 0, 10);
				gridBagConstraints10.gridy = 5;
				criticalQuestionsLabel = new JLabel();
				criticalQuestionsLabel.setText("Kritiske spørsmål:");
				GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
				gridBagConstraints9.fill = GridBagConstraints.VERTICAL;
				gridBagConstraints9.gridy = 3;
				gridBagConstraints9.weightx = 1.0;
				gridBagConstraints9.anchor = GridBagConstraints.WEST;
				gridBagConstraints9.insets = new Insets(0, 0, 5, 0);
				gridBagConstraints9.gridx = 1;
				GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
				gridBagConstraints8.gridx = 0;
				gridBagConstraints8.anchor = GridBagConstraints.WEST;
				gridBagConstraints8.insets = new Insets(0, 10, 0, 0);
				gridBagConstraints8.gridy = 3;
				progressLabel = new JLabel();
				progressLabel.setText("Estimert tidsbruk i timer:");
				GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
				gridBagConstraints7.fill = GridBagConstraints.VERTICAL;
				gridBagConstraints7.gridy = 2;
				gridBagConstraints7.weightx = 1.0;
				gridBagConstraints7.anchor = GridBagConstraints.WEST;
				gridBagConstraints7.insets = new Insets(5, 0, 5, 0);
				gridBagConstraints7.gridx = 1;
				GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
				gridBagConstraints6.gridx = 0;
				gridBagConstraints6.anchor = GridBagConstraints.WEST;
				gridBagConstraints6.insets = new Insets(0, 10, 0, 0);
				gridBagConstraints6.gridy = 2;
				personelLabel = new JLabel();
				personelLabel.setText("Mannskapsbehov:");
				GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
				gridBagConstraints3.gridx = 0;
				gridBagConstraints3.anchor = GridBagConstraints.WEST;
				gridBagConstraints3.insets = new Insets(0, 10, 0, 0);
				gridBagConstraints3.gridy = 1;
				priorityLabel = new JLabel();
				priorityLabel.setText("Prioritet:");
				GridBagConstraints gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.gridx = 0;
				gridBagConstraints.anchor = GridBagConstraints.WEST;
				gridBagConstraints.insets = new Insets(0, 10, 10, 0);
				gridBagConstraints.gridy = 0;
				accuracyLabel = new JLabel();
				accuracyLabel.setText("Nøyaktighet:");
				contentPanel = new JPanel();
				contentPanel.setLayout(new GridBagLayout());
				contentPanel.add(priorityLabel, gridBagConstraints3);
				contentPanel.add(personelLabel, gridBagConstraints6);
				contentPanel.add(progressLabel, gridBagConstraints8);
				contentPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				contentPanel.add(criticalQuestionsLabel, gridBagConstraints10);
				contentPanel.add(getCriticalQuestionsScrollPane(), gridBagConstraints11);
				contentPanel.add(accuracyLabel, gridBagConstraints);
				contentPanel.add(getPersonelSpinner(), gridBagConstraints7);
				contentPanel.add(getProgressSpinner(), gridBagConstraints9);
				contentPanel.add(getAccuracySlider(), gridBagConstraints61);
				contentPanel.add(getAccuracyTextField(), gridBagConstraints71);
				contentPanel.add(getPrioritySlider(), gridBagConstraints81);
				contentPanel.add(getPriorityTextField(), gridBagConstraints91);
				contentPanel.add(statusLabel, gridBagConstraints12);
				contentPanel.add(getStatusComboBox(), gridBagConstraints13);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return contentPanel;
	}

	/**
	 * This method initializes accuracySlider	
	 * 	
	 * @return javax.swing.JSlider	
	 */
	private JSlider getAccuracySlider() {
		if (accuracySlider == null) {
			try {
				accuracySlider = new JSlider();
				accuracySlider.setMajorTickSpacing(50);
				accuracySlider.setPaintLabels(true);
				accuracySlider.setPreferredSize(new Dimension(583, 40));
				accuracySlider.setMinorTickSpacing(10);
				accuracySlider.setSnapToTicks(true);
				accuracySlider.setPaintTicks(true);
				accuracySlider.addChangeListener(new javax.swing.event.ChangeListener() {
					public void stateChanged(javax.swing.event.ChangeEvent e) {
						getAccuracyTextField().setText(accuracySlider.getValue()+"%");
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return accuracySlider;
	}
	
	/**
	 * This method initializes prioritySlider	
	 * 	
	 * @return javax.swing.JSlider	
	 */
	private JSlider getPrioritySlider() {
		if (prioritySlider == null) {
			try {
				prioritySlider = new JSlider();
				prioritySlider.setPreferredSize(new Dimension(583, 40));
				prioritySlider.setPaintLabels(true);
				prioritySlider.setMinorTickSpacing(0);
				prioritySlider.setMajorTickSpacing(1);
				prioritySlider.setSnapToTicks(true);
				prioritySlider.setMinimum(1);
				prioritySlider.setMaximum(3);
				prioritySlider.setValue(3);
				prioritySlider.setPaintTicks(true);
				prioritySlider.addChangeListener(new javax.swing.event.ChangeListener() {
					public void stateChanged(javax.swing.event.ChangeEvent e) {
						Integer key = Integer.valueOf(prioritySlider.getValue());
						String text = ((JLabel)prioritySlider.getLabelTable().get(key)).getText();
						getPriorityTextField().setText(text);
					}
				});
				Hashtable labels = new Hashtable();
				labels.put(new Integer(1), new JLabel("LAV"));
				labels.put(new Integer(2), new JLabel("NORMAL"));
				labels.put(new Integer(3), new JLabel("HØY"));
				prioritySlider.setLabelTable(labels);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return prioritySlider;
	}

	/**
	 * This method initializes accuracyTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getAccuracyTextField() {
		if (accuracyTextField == null) {
			try {
				accuracyTextField = new JTextField();
				accuracyTextField.setPreferredSize(new Dimension(75, 20));
				accuracyTextField.setEditable(true);
				accuracyTextField.setText("50%");
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return accuracyTextField;
	}

	/**
	 * This method initializes priorityTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getPriorityTextField() {
		if (priorityTextField == null) {
			try {
				priorityTextField = new JTextField();
				priorityTextField.setPreferredSize(new Dimension(75, 20));
				priorityTextField.setEditable(true);
				priorityTextField.setText("HØY");
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return priorityTextField;
	}

	/**
	 * This method initializes personelComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JSpinner getPersonelSpinner() {
		if (personelSpinner == null) {
			try {
				personelSpinner = new JSpinner();
				personelSpinner.setPreferredSize(new Dimension(125, 20));
				SpinnerNumberModel model = new SpinnerNumberModel(5, 1, 50, 1); 
				personelSpinner.setModel(model);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return personelSpinner;
	}

	/**
	 * This method initializes progressComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JSpinner getProgressSpinner() {
		if (progressSpinner == null) {
			try {
				progressSpinner = new JSpinner();
				progressSpinner.setPreferredSize(new Dimension(125, 20));
				SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 24, 1); 
				progressSpinner.setModel(model);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return progressSpinner;
	}

	/**
	 * This method initializes criticalQuestionsScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getCriticalQuestionsScrollPane() {
		if (criticalQuestionsScrollPane == null) {
			try {
				criticalQuestionsScrollPane = new JScrollPane();
				criticalQuestionsScrollPane.setViewportView(getCriticalQuestionsTextArea());
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return criticalQuestionsScrollPane;
	}

	/**
	 * This method initializes criticalQuestionsTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getCriticalQuestionsTextArea() {
		if (criticalQuestionsTextArea == null) {
			try {
				criticalQuestionsTextArea = new JTextArea();
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return criticalQuestionsTextArea;
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
				IAssignmentIf.AssignmentStatus[] values = IAssignmentIf.AssignmentStatus.values();
				for (int i = 0; i <values.length; i++) {
					statusComboBox.addItem(values[i]);
				}
				statusComboBox.setSelectedItem(IAssignmentIf.AssignmentStatus.DRAFT);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return statusComboBox;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
