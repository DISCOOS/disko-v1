package org.redcross.sar.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.app.Utils;
import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.event.IDiskoMapEventListener;
import org.redcross.sar.gui.renderers.SimpleListCellRenderer;
import org.redcross.sar.map.POITool;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.IMsoFeatureClass;
import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.mso.data.IPOIIf.POIType;

import com.esri.arcgis.geometry.esriMGRSModeEnum;
import com.esri.arcgis.interop.AutomationException;

public class POIDialog extends DiskoDialog implements IDiskoMapEventListener {

	private static final long serialVersionUID = 1L;
	private IDiskoApplication app = null;
	private POITool tool = null;
	private JPanel contentPanel = null;
	private NumPadDialog numPadDialog = null;
	private JTextField mgrsTextField = null;
	private JComboBox typeComboBox = null;
	private JLabel txtAreaLabel = null;
	private JTextArea txtArea = null;
	private JPanel coordPanel = null;
	private JLabel mgrsLabel = null;
	private JLabel typeLabel = null;
	private JScrollPane textAreaScrollPane = null;
	private JPanel northPanel = null;
	private JPanel textAreaPanel = null;
	private JPanel buttonPanel = null;
	private JButton finishButton = null;
	
	public POIDialog(IDiskoApplication app, POITool tool) {
		super(app.getFrame());
		this.app = app;
		this.tool = tool;
		
		// listener
		try {
			IMsoFeatureLayer msoLayer = app.getDiskoMapManager().
				getMsoLayer(IMsoFeatureLayer.LayerCode.POI_LAYER);
			IMsoFeatureClass msoFC = (IMsoFeatureClass)msoLayer.getFeatureClass();
			msoFC.addDiskoMapEventListener(this);
		} catch (AutomationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initialize();
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		try {
            this.setPreferredSize(new Dimension(250, 375));
            this.setContentPane(getContentPanel());
            this.pack();
		}
		catch (java.lang.Throwable e) {
			//  Do Something
		}
	}
	
	public void reset() {
		getTxtArea().setText(null);
		getMGRSTextField().setText(null);
		getTypeComboBox().setSelectedIndex(0);
	}
	
	/**
	 * This method initializes contentPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getContentPanel() {
		if (contentPanel == null) {
			try {
				BorderLayout borderLayout1 = new BorderLayout();
				borderLayout1.setVgap(5);
				borderLayout1.setHgap(0);
				txtAreaLabel = new JLabel();
				txtAreaLabel.setText("Merknad:");
				contentPanel = new JPanel();
				contentPanel.setLayout(borderLayout1);
				contentPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				contentPanel.add(getNorthPanel(), BorderLayout.NORTH);
				contentPanel.add(getTextAreaPanel(), BorderLayout.CENTER);
				contentPanel.add(getButtonPanel(), BorderLayout.SOUTH);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return contentPanel;
	}
	
	/**
	 * This method initializes xCoordTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getMGRSTextField() {
		if (mgrsTextField == null) {
			mgrsTextField = new JTextField();
			mgrsTextField.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {					
					if (e.getClickCount() == 2){
						numPadDialog = app.getUIFactory().getNumPadDialog();
						java.awt.Point p = mgrsTextField.getLocationOnScreen();
						p.setLocation(p.x + (mgrsTextField.getWidth()+7), p.y);
						numPadDialog.setLocation(p);					
						numPadDialog.setTextField(mgrsTextField);
						numPadDialog.setVisible(true);	
					}
				}
			});	
		}
		return mgrsTextField;
	}
	
	public void setTypes(POIType[] poiTypes) {
		JComboBox cb = getTypeComboBox();
		cb.removeAllItems();
		for (int i = 0; i < poiTypes.length; i++) {
			cb.addItem(poiTypes[i]);
		}
		cb.setSelectedItem(poiTypes[0]);
	}
	
	public POIType getSelectedType() {
		return (POIType)getTypeComboBox().getSelectedItem();
	}
	
	/**
	 * This method initializes typeComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getTypeComboBox() {
		if (typeComboBox == null) {
			typeComboBox = new JComboBox();
			typeComboBox.setRenderer(new SimpleListCellRenderer());
			typeComboBox.setMaximumRowCount(8);
		}
		return typeComboBox;
	}
	
		
	/**
	 * This method initializes txtArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	public JTextArea getTxtArea() {
		if (txtArea == null) {
			txtArea = new JTextArea();
			txtArea.setLineWrap(true);
		}
		return txtArea;
	}
	/**
	 * This method initializes centerPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCoordPanel() {
		if (coordPanel == null) {
			try {
				GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
				gridBagConstraints31.gridx = 0;
				gridBagConstraints31.anchor = GridBagConstraints.WEST;
				gridBagConstraints31.insets = new Insets(0, 5, 0, 0);
				gridBagConstraints31.gridy = 3;
				typeLabel = new JLabel();
				typeLabel.setText("Type:");
				GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
				gridBagConstraints21.gridx = 0;
				gridBagConstraints21.anchor = GridBagConstraints.WEST;
				gridBagConstraints21.insets = new Insets(0, 5, 10, 0);
				gridBagConstraints21.gridy = 2;
				GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
				gridBagConstraints11.gridx = 0;
				gridBagConstraints11.anchor = GridBagConstraints.WEST;
				gridBagConstraints11.insets = new Insets(0, 5, 10, 0);
				gridBagConstraints11.gridy = 1;
				mgrsLabel = new JLabel();
				mgrsLabel.setText("MGRS:");
				GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
				gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
				gridBagConstraints5.gridy = 3;
				gridBagConstraints5.weightx = 1.0;
				gridBagConstraints5.insets = new Insets(0, 5, 0, 0);
				gridBagConstraints5.gridx = 1;
				GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
				gridBagConstraints4.gridx = 0;
				gridBagConstraints4.gridy = 2;
				GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
				gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
				gridBagConstraints3.gridy = 2;
				gridBagConstraints3.weightx = 1.0;
				gridBagConstraints3.insets = new Insets(0, 5, 10, 0);
				gridBagConstraints3.gridx = 1;
				GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
				gridBagConstraints2.gridx = 0;
				gridBagConstraints2.gridy = 1;
				GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
				gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
				gridBagConstraints1.gridy = 1;
				gridBagConstraints1.weightx = 1.0;
				gridBagConstraints1.insets = new Insets(0, 5, 10, 0);
				gridBagConstraints1.gridx = 1;
				GridBagConstraints gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.gridx = 0;
				gridBagConstraints.gridy = 0;
				coordPanel = new JPanel();
				coordPanel.setLayout(new GridBagLayout());
				coordPanel.add(getMGRSTextField(), gridBagConstraints1);
				coordPanel.add(getTypeComboBox(), gridBagConstraints5);
				coordPanel.add(mgrsLabel, gridBagConstraints11);
				coordPanel.add(typeLabel, gridBagConstraints31);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return coordPanel;
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
				textAreaScrollPane.setViewportView(getTxtArea());
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return textAreaScrollPane;
	}	

	/**
	 * This method initializes northPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getNorthPanel() {
		if (northPanel == null) {
			try {
				BorderLayout borderLayout = new BorderLayout();
				borderLayout.setVgap(0);
				northPanel = new JPanel();
				northPanel.setBorder(BorderFactory.createTitledBorder(
						null, "PUI", TitledBorder.DEFAULT_JUSTIFICATION, 
						TitledBorder.DEFAULT_POSITION, 
						new Font("Tahoma", Font.PLAIN, 11), 
						new Color(0, 70, 213)));
				northPanel.setLayout(borderLayout);
				northPanel.add(getCoordPanel(), BorderLayout.NORTH);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return northPanel;
	}

	/**
	 * This method initializes textAreaPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTextAreaPanel() {
		if (textAreaPanel == null) {
			try {
				textAreaPanel = new JPanel();
				textAreaPanel.setLayout(new BorderLayout());
				textAreaPanel.add(txtAreaLabel, BorderLayout.NORTH);
				textAreaPanel.add(getTextAreaScrollPane(), BorderLayout.CENTER);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return textAreaPanel;
	}
	
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			try {
				buttonPanel = new JPanel();
				FlowLayout fl = new FlowLayout();
				fl.setHgap(0);
				fl.setVgap(0);
				fl.setAlignment(FlowLayout.RIGHT);
				buttonPanel.setLayout(fl);
				buttonPanel.add(getFinishButton());
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return buttonPanel;
	}
	
	private JButton getFinishButton() {
		if (finishButton == null) {
			try {
				finishButton = new JButton();
				String iconName = "finish.icon";
				Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				finishButton.setIcon(icon);
				Dimension size = app.getUIFactory().getSmallButtonSize();
				finishButton.setPreferredSize(size);
				finishButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						// coordinates
						com.esri.arcgis.geometry.Point point = null;
						try {
							point = new com.esri.arcgis.geometry.Point();
							point.setSpatialReferenceByRef(tool.getMap().getSpatialReference());
							point.putCoordsFromMGRS(getMGRSTextField().getText(), 
									esriMGRSModeEnum.esriMGRSMode_NewStyle);
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(app.getFrame(),
			                        "Ugyldig MGRS koordinat", null, 
			                        JOptionPane.WARNING_MESSAGE);
							ex.printStackTrace();
						}
						try {
							if (tool.getEditFeature() == null) {
								tool.addPOIAt(point);
							} else {
								tool.movePOI(point);
							}
						} catch (AutomationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						if (tool.getEditFeature() != null) {
							IPOIIf poi = (IPOIIf)tool.getEditFeature().getMsoObject();
							poi.setRemarks(txtArea.getText());
							poi.setType((POIType)getTypeComboBox().getSelectedItem());
							fireDialogStateChanged();
						}
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return finishButton;
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
		if (tool.getMap() == null) {
			return;
		}
		IMsoFeatureClass msoFC = (IMsoFeatureClass)e.getSource();
		List selection = msoFC.getSelected();
		if (selection != null && selection.size() > 0) {
			IMsoFeature msoFeature = (IMsoFeature)selection.get(0);
			IMsoObjectIf msoObject = msoFeature.getMsoObject();
			if (!isVisible()) {
				tool.getMap().setActiveTool(tool);
			}
			tool.setEditFeature(msoFeature);
			IPOIIf poi = (IPOIIf)msoObject;
			com.esri.arcgis.geometry.Point point = 
				(com.esri.arcgis.geometry.Point)msoFeature.getShape();
			String mgrs = point.createMGRS(5, true, esriMGRSModeEnum.esriMGRSMode_NewStyle);
			getMGRSTextField().setText(mgrs);
			getTxtArea().setText(poi.getRemarks());
			POIType type = poi.getType();
			if (type != null) {
				getTypeComboBox().setSelectedItem(type);
			}
		}
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
