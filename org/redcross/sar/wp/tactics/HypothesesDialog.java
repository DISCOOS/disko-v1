package org.redcross.sar.wp.tactics;

import com.esri.arcgis.interop.AutomationException;
import org.redcross.sar.event.IMsoLayerEventListener;
import org.redcross.sar.event.MsoLayerEvent;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.models.HypothesesListModel;
import org.redcross.sar.gui.renderers.HypothesesListCellRenderer;
import org.redcross.sar.gui.renderers.SimpleListCellRenderer;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.IHypothesisIf;
import org.redcross.sar.mso.data.IHypothesisIf.HypothesisStatus;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.ISearchAreaIf;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class HypothesesDialog extends DiskoDialog implements IMsoLayerEventListener {

	private static final long serialVersionUID = 1L;
	private IMsoModelIf msoModel = null;
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

	private IHypothesisIf selectedHypotheses = null;

	public HypothesesDialog(DiskoWpTacticsImpl wp) {
		super(wp.getApplication().getFrame());
		this.msoModel = wp.getMsoModel();
		//listener
		IMsoFeatureLayer msoLayer = wp.getMap().getMsoLayer(IMsoFeatureLayer.LayerCode.SEARCH_AREA_LAYER);
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
            this.setContentPane(getContentPanel());
            this.setPreferredSize(new Dimension(500, 125));
            this.pack();
		}
		catch (java.lang.Throwable e) {
			//  Do Something
		}
	}

	public void reset() {
		getDescriptionTextArea().setText(null);
		getPriorityComboBox().setSelectedIndex(0);
		getStatusComboBox().setSelectedIndex(0);
		selectedHypotheses = null;
	}

	public IHypothesisIf getSelectedHypotheses() {
		applyChanges();
		return selectedHypotheses;
	}

	private void applyChanges() {
		if (selectedHypotheses == null) {
			return;
		}
		if (!selectedHypotheses.getDescription().equals(getDescriptionTextArea().getText())) {
			selectedHypotheses.setDescription(getDescriptionTextArea().getText());
		}
		if (selectedHypotheses.getPriorityIndex()-1 != getPriorityComboBox().getSelectedIndex()) {
			selectedHypotheses.setPriorityIndex(getPriorityComboBox().getSelectedIndex()+1);
		}
		if (selectedHypotheses.getStatus() != getStatusComboBox().getSelectedItem()) {
			selectedHypotheses.setStatus((HypothesisStatus)getStatusComboBox().getSelectedItem());
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
				contentPanel.setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createBevelBorder(BevelBorder.RAISED),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)));
				contentPanel.add(getListScrollPane(), BorderLayout.WEST);
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
				newButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						// create a new hypotheses
						ICmdPostIf cmdPost = msoModel.getMsoManager().getCmdPost();
						IHypothesisIf hypotheses = cmdPost.getHypothesisList().createHypothesis();
						getHypothesesList().setSelectedValue(hypotheses, true);
					}
				});
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
				listScrollPane.setPreferredSize(new Dimension(150, 150));
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
				HypothesesListModel listModel = new HypothesesListModel(msoModel);
				hypothesesList = new JList(listModel);
				hypothesesList.setCellRenderer(new HypothesesListCellRenderer());
				hypothesesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				hypothesesList.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						if (e.getValueIsAdjusting()) {
							return;
						}
						getDescriptionTextArea().setEnabled(true);
						applyChanges();
						selectedHypotheses = (IHypothesisIf)hypothesesList.getSelectedValue();
						if (selectedHypotheses == null) {
							return;
						}
						getDescriptionTextArea().setText(selectedHypotheses.getDescription());
						if (selectedHypotheses.getPriorityIndex() > 0) {
							getPriorityComboBox().setSelectedIndex(selectedHypotheses.getPriorityIndex()-1);
						}
						else {
							getPriorityComboBox().setSelectedIndex(0);
						}
						getStatusComboBox().setSelectedItem(selectedHypotheses.getStatus());
						fireDialogStateChanged();
					}
				});
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
				centerPanel.add(getButtonPanel(), BorderLayout.WEST);
				centerPanel.add(getTextAreaScrollPane(), BorderLayout.CENTER);
				centerPanel.add(getPropertiesPanel(), BorderLayout.EAST);
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
				descriptionTextArea.setLineWrap(true);
				descriptionTextArea.setEnabled(false);
				descriptionTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
					public void keyTyped(java.awt.event.KeyEvent e) {
						fireDialogStateChanged();
					}
				});
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
				propertiesPanel.setPreferredSize(new Dimension(175, 150));
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
				for (int i = 1; i < 6; i++) {
					priorityComboBox.addItem(new Integer(i));
				}
				priorityComboBox.setSelectedIndex(0);
				priorityComboBox.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						fireDialogStateChanged();
					}
				});

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
				statusComboBox.setRenderer(new SimpleListCellRenderer());
				HypothesisStatus[] values = HypothesisStatus.values();
				for (int i = 0; i < values.length; i++) {
					statusComboBox.addItem(values[i]);
				}
				statusComboBox.setSelectedIndex(0);
				statusComboBox.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						fireDialogStateChanged();
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return statusComboBox;
	}

	public void onSelectionChanged(MsoLayerEvent e) throws IOException, AutomationException {
		IMsoFeatureLayer msoLayer = (IMsoFeatureLayer)e.getSource();
		List selection = msoLayer.getSelected();
		if (selection != null && selection.size() > 0) {
			IMsoFeature msoFeature = (IMsoFeature)selection.get(0);
			IMsoObjectIf msoObject = msoFeature.getMsoObject();
			if (msoObject instanceof ISearchAreaIf) {
				ISearchAreaIf searchArea = (ISearchAreaIf)msoObject;
				getHypothesesList().setSelectedValue(searchArea.getSearchAreaHypothesis(), false);
				return;
			}
		}
		getHypothesesList().setSelectedIndex(0);
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
