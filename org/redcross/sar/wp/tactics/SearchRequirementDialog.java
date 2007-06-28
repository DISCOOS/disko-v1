package org.redcross.sar.wp.tactics;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.event.IDiskoMapEventListener;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.NumPadDialog;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.IMsoFeatureClass;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.ISearchIf;
import org.redcross.sar.mso.data.IAssignmentIf.AssignmentPriority;

import com.esri.arcgis.interop.AutomationException;

public class SearchRequirementDialog extends DiskoDialog implements IDiskoMapEventListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel = null;
	private JLabel accuracyLabel = null;
	private JSlider accuracySlider = null;
	private JTextField accuracyTextField = null;
	private JLabel priorityLabel = null;
	private JSlider prioritySlider = null;
	private JTextField priorityTextField = null;
	private JLabel personelLabel = null;
	private JScrollPane criticalQuestionsScrollPane = null;
	private JTextArea criticalQuestionsTextArea = null;
	private JTabbedPane tabbedPane = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private JTextField personelTextField = null;
	private DiskoWpTacticsImpl wp = null;
	
	
	public SearchRequirementDialog(DiskoWpTacticsImpl wp) {
		super(wp.getApplication().getFrame());
		this.wp = wp;
		initialize();
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		try {
            this.setPreferredSize(new Dimension(800, 145));
            this.setSize(new Dimension(989, 145));
            this.setContentPane(getTabbedPane());
            this.pack();
		}
		catch (java.lang.Throwable e) {
			//  Do Something
		}
	}
	
	/**
	 * This method initializes tabbedPane	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			try {
				tabbedPane = new JTabbedPane();
				tabbedPane.addTab("Krav", null, getContentPanel(), null);
				tabbedPane.addTab("Merknad", null, getCriticalQuestionsScrollPane(), null);
				tabbedPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return tabbedPane;
	}
	
	public void reset() {
		getAccuracyTextField().setText(null);
		getPriorityTextField().setText(null);
		getCriticalQuestionsTextArea().setText(null);
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
		try {
			return Integer.parseInt(getPersonelTextField().getText());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	public String getCriticalQuestions() {
		return getCriticalQuestionsTextArea().getText();
	}
	
	public void setAccuracy(int accuracy) {
		getAccuracySlider().setValue(accuracy);
	}
	
	public void setPriority(AssignmentPriority priority) {
		if (priority == AssignmentPriority.LOW) {
			getPrioritySlider().setValue(1);
		}
		else if (priority == AssignmentPriority.MEDIUM) {
			getPrioritySlider().setValue(2);
		}
		if (priority == AssignmentPriority.HIGH) {
			getPrioritySlider().setValue(3);
		}
	}
	
	public void setPersonelNeed(int need) {
		getPersonelTextField().setText(String.valueOf(need));
	}
	
	public void setCriticalQuestions(String questions) {
		getCriticalQuestionsTextArea().setText(questions);
	}

	/**
	 * This method initializes contentPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getContentPanel() {
		if (contentPanel == null) {
			try {
				GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
				gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
				gridBagConstraints1.gridy = 1;
				gridBagConstraints1.weightx = 1.0;
				gridBagConstraints1.anchor = GridBagConstraints.NORTHWEST;
				gridBagConstraints1.gridx = 3;
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
				GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
				gridBagConstraints6.gridx = 3;
				gridBagConstraints6.anchor = GridBagConstraints.SOUTHWEST;
				gridBagConstraints6.insets = new Insets(0, 0, 0, 0);
				gridBagConstraints6.gridy = 0;
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
				contentPanel.add(accuracyLabel, gridBagConstraints);
				contentPanel.add(getAccuracySlider(), gridBagConstraints61);
				contentPanel.add(getAccuracyTextField(), gridBagConstraints71);
				contentPanel.add(getPrioritySlider(), gridBagConstraints81);
				contentPanel.add(getPriorityTextField(), gridBagConstraints91);
				contentPanel.add(getPersonelTextField(), gridBagConstraints1);
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
						fireDialogStateChanged();
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
						fireDialogStateChanged();
					}
				});
				Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
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
	 * This method initializes personelTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getPersonelTextField() {
		if (personelTextField == null) {
			try {
				personelTextField = new JTextField();
				personelTextField.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent e) {					
						if (e.getClickCount() == 2){
							NumPadDialog npDialog = wp.getApplication().
								getUIFactory().getNumPadDialog();
							Point p = personelTextField.getLocationOnScreen();
							p.setLocation(p.x + personelTextField.getWidth()-
									npDialog.getWidth(), p.y-npDialog.getHeight()-2);
							npDialog.setLocation(p);					
							npDialog.setTextField(personelTextField);
							npDialog.setVisible(true);	
						}
					}
				});	
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return personelTextField;
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
				criticalQuestionsTextArea.setLineWrap(true);
				criticalQuestionsTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
					public void keyTyped(java.awt.event.KeyEvent e) {
						fireDialogStateChanged();
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return criticalQuestionsTextArea;
	}

	public void editLayerChanged(DiskoMapEvent e) {
		// TODO Auto-generated method stub
	}

	public void onAfterScreenDraw(DiskoMapEvent e) throws IOException, AutomationException {
		// TODO Auto-generated method stub
	}

	public void onExtentUpdated(DiskoMapEvent e) throws IOException, AutomationException {
		// TODO Auto-generated method stub
	}

	public void onMapReplaced(DiskoMapEvent e) throws IOException, AutomationException {
		// TODO Auto-generated method stub
	}

	public void onSelectionChanged(DiskoMapEvent e) throws IOException, AutomationException {
		IMsoFeatureClass msoFC = (IMsoFeatureClass)e.getSource();
		List selection = msoFC.getSelected();
		if (selection != null && selection.size() > 0) {
			IMsoFeature msoFeature = (IMsoFeature)selection.get(0);
			IAreaIf area = (IAreaIf)msoFeature.getMsoObject();
			IAssignmentIf assignment = area.getOwningAssignment();
			if (assignment instanceof ISearchIf) {
				ISearchIf search = (ISearchIf)assignment;
				setPriority(search.getPriority());
				setAccuracy(search.getPlannedAccuracy());
				setPersonelNeed(search.getPlannedPersonnel());
				return;
			}
		}
		reset();
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"

