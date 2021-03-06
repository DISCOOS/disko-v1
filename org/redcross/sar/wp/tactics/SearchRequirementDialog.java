package org.redcross.sar.wp.tactics;

import com.esri.arcgis.interop.AutomationException;
import org.redcross.sar.event.IMsoLayerEventListener;
import org.redcross.sar.event.MsoLayerEvent;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.NumPadDialog;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IAssignmentIf.AssignmentPriority;
import org.redcross.sar.mso.data.ISearchIf;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

public class SearchRequirementDialog extends DiskoDialog implements IMsoLayerEventListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel = null;
	private JLabel accuracyLabel = null;
	private JSlider accuracySlider = null;
	private JTextField accuracyTextField = null;
	private JLabel priorityLabel = null;
	private JSlider prioritySlider = null;
	private JTextField priorityTextField = null;
	private JLabel personelLabel = null;
	private JScrollPane remarksScrollPane = null;
	private JTextArea remarksTextArea = null;
	private JTabbedPane tabbedPane = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private JTextField personelTextField = null;
	private DiskoWpTacticsImpl wp = null;


	public SearchRequirementDialog(DiskoWpTacticsImpl wp) {
		super(wp.getApplication().getFrame());
		this.wp = wp;
		//listener
		IMsoFeatureLayer msoLayer = wp.getMap().getMsoLayer(IMsoFeatureLayer.LayerCode.AREA_LAYER);
		msoLayer.addDiskoLayerEventListener(this);
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
				tabbedPane.addTab("Merknad", null, getRemarksScrollPane(), null);
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
		getRemarksTextArea().setText(null);
		getPersonelTextField().setText(null);

		getPrioritySlider().setValue(2);
		getAccuracySlider().setValue(50);
	}

	public int getAccuracy() {
		return getAccuracySlider().getValue();
	}

	public AssignmentPriority getPriority() {
		int value = getPrioritySlider().getValue();
		switch (value) {
			case 1: return AssignmentPriority.LOW;
			case 2: return AssignmentPriority.NORMAL;
			case 3: return AssignmentPriority.HIGH;
		}
		return null;
	}

	public int getPersonelNeed() {
		try {
			return Integer.parseInt(getPersonelTextField().getText());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
		}
		return 0;
	}

	public String getRemarks() {
		return getRemarksTextArea().getText();
	}

	public void setAccuracy(int accuracy) {
		getAccuracySlider().setValue(accuracy);
	}

	public void setPriority(AssignmentPriority priority) {
		if (priority == AssignmentPriority.LOW) {
			getPrioritySlider().setValue(1);
		}
		else if (priority == AssignmentPriority.NORMAL) {
			getPrioritySlider().setValue(2);
		}
		if (priority == AssignmentPriority.HIGH) {
			getPrioritySlider().setValue(3);
		}
	}

	public void setPersonelNeed(int need) {
		getPersonelTextField().setText(String.valueOf(need));
	}

	public void setRemarks(String remarks) {
		getRemarksTextArea().setText(remarks);
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
				accuracyLabel.setText("N�yaktighet:");
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
				accuracySlider.setMinimum(0);
				accuracySlider.setMaximum(100);
				accuracySlider.setValue(50);
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
				prioritySlider.setValue(2);
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
				labels.put(new Integer(3), new JLabel("H�Y"));
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
				priorityTextField.setText("H�Y");
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
				personelTextField.setText("0");
				personelTextField.addKeyListener(new java.awt.event.KeyAdapter() {
					public void keyTyped(java.awt.event.KeyEvent e) {
						fireDialogStateChanged();
					}
				});
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
							fireDialogStateChanged();
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
	private JScrollPane getRemarksScrollPane() {
		if (remarksScrollPane == null) {
			try {
				remarksScrollPane = new JScrollPane();
				remarksScrollPane.setViewportView(getRemarksTextArea());
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return remarksScrollPane;
	}

	/**
	 * This method initializes criticalQuestionsTextArea
	 *
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getRemarksTextArea() {
		if (remarksTextArea == null) {
			try {
				remarksTextArea = new JTextArea();
				remarksTextArea.setLineWrap(true);
				remarksTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
					public void keyTyped(java.awt.event.KeyEvent e) {
						fireDialogStateChanged();
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return remarksTextArea;
	}

	public void onSelectionChanged(MsoLayerEvent e) throws IOException, AutomationException {
		IMsoFeatureLayer msoLayer = (IMsoFeatureLayer)e.getSource();
		List selection = msoLayer.getSelected();
		if (selection != null && selection.size() > 0) {
			IMsoFeature msoFeature = (IMsoFeature)selection.get(0);
			IAreaIf area = (IAreaIf)msoFeature.getMsoObject();
			IAssignmentIf assignment = area.getOwningAssignment();
			if (assignment instanceof ISearchIf) {
				ISearchIf search = (ISearchIf)assignment;
				setPriority(search.getPriority());
				setAccuracy(search.getPlannedAccuracy());
				setPersonelNeed(search.getPlannedPersonnel());
				setRemarks(search.getRemarks());
				return;
			}
		}
		reset();
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"

