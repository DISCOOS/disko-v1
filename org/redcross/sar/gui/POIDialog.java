package org.redcross.sar.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
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
import javax.swing.BoxLayout;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.app.Utils;
import org.redcross.sar.event.IMsoLayerEventListener;
import org.redcross.sar.event.MsoLayerEvent;
import org.redcross.sar.gui.MGRSField;
import org.redcross.sar.gui.renderers.SimpleListCellRenderer;
import org.redcross.sar.map.POITool;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.mso.data.IPOIIf.POIType;

import com.esri.arcgis.geometry.esriMGRSModeEnum;
import com.esri.arcgis.interop.AutomationException;

public class POIDialog extends DiskoDialog implements IMsoLayerEventListener {

	private static final long serialVersionUID = 1L;
	private IDiskoApplication app = null;
	private POITool tool = null;
	private JPanel contentPanel = null;
	private NumPadDialog numPadDialog = null;
	private MGRSField mgrsTextField = null;
	//private JTextField mgrsTextField = null;
	private JComboBox typeComboBox = null;
//	private JLabel txtAreaLabel = null;
	private JTextArea txtArea = null;
	private JPanel coordPanel = null;
//	private JLabel mgrsLabel = null;
//	private JLabel typeLabel = null;
	private JScrollPane textAreaScrollPane = null;
	private JPanel northPanel = null;
//	private JPanel textAreaPanel = null;
	private JPanel buttonPanel = null;
	private JButton finishButton = null;
	
	public POIDialog(IDiskoApplication app, POITool tool) {
		super(app.getFrame());
		this.app = app;
		this.tool = tool;
		
		initialize();
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		try {
            this.setPreferredSize(new Dimension(275, 375));
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
				contentPanel = new JPanel();
				contentPanel.setLayout(borderLayout1);
				contentPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				contentPanel.add(getNorthPanel(), BorderLayout.NORTH);
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
	private MGRSField getMGRSTextField() {
		if (mgrsTextField == null) {
			mgrsTextField = new MGRSField(app);
			/*mgrsTextField.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {					
					if (e.getClickCount() == 2){
						NumPadDialog numPadDialog = app.getUIFactory().getNumPadDialog();
						java.awt.Point p = mgrsTextField.getLocationOnScreen();
						p.setLocation(p.x + (mgrsTextField.getWidth()+7), p.y);
						numPadDialog.setLocation(p);					
						numPadDialog.setTextField(mgrsTextField);
						numPadDialog.setVisible(true);	
					}
				}
			});*/
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
			typeComboBox.setPreferredSize(new Dimension(275,20));
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

				// create constraints
				GridBagConstraints c1 = new GridBagConstraints();
				c1.gridx = 0;
				c1.gridy = 0;
				c1.weightx = 1.0;
				c1.weighty = 0.0;
				c1.anchor = GridBagConstraints.WEST;
				c1.fill = GridBagConstraints.BOTH;
				c1.insets = new Insets(0, 0, 5, 0);
				GridBagConstraints c2 = new GridBagConstraints();
				c2.gridx = 0;
				c2.gridy = 1;
				c2.weightx = 1.0;
				c2.weighty = 0.0;
				c2.anchor = GridBagConstraints.WEST;
				c2.fill = GridBagConstraints.BOTH;
				c2.insets = new Insets(0, 0, 5, 0);
				GridBagConstraints c3 = new GridBagConstraints();
				c3.gridx = 0;
				c3.gridy = 2;
				c3.ipadx = 0;
				c3.ipady = 5;
				c3.weightx = 1.0;
				c3.weighty = 0.5;
				c3.anchor = GridBagConstraints.WEST;
				c3.fill = GridBagConstraints.BOTH;
				c3.insets = new Insets(0, 0, 5, 0);
				
				// create panel
				coordPanel = new JPanel();
				coordPanel.setLayout(new GridBagLayout());
				
				// add components
				coordPanel.add(getMGRSTextField(),c1);
				coordPanel.add(getTypeComboBox(),c2);
				coordPanel.add(getTextAreaScrollPane(),c3);
				
				/*GridBagConstraints c1 = new GridBagConstraints();
				c1.gridx = 0;
				c1.gridy = 0;
				c1.weightx = 0.5;
				c1.weighty = 0.5;
				c1.fill = GridBagConstraints.HORIZONTAL;
				c1.anchor = GridBagConstraints.NORTHWEST;
				GridBagConstraints c2 = new GridBagConstraints();
				c2.gridx = 0;
				c2.gridy = 1;
				c2.weightx = 0.5;
				c2.weighty = 0.5;
				c2.fill = GridBagConstraints.HORIZONTAL;				
				c2.anchor = GridBagConstraints.NORTHWEST;
				GridBagConstraints c3 = new GridBagConstraints();
				c3.gridx = 0;
				c3.gridy = 2;
				c3.weightx = 0.5;
				c3.weighty = 0.5;
				c3.fill = GridBagConstraints.HORIZONTAL;				
				c3.anchor = GridBagConstraints.NORTHWEST;*/
				
				
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
				textAreaScrollPane.setPreferredSize(new Dimension(275,180));
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
				northPanel = new JPanel();
				northPanel.setBorder(BorderFactory.createTitledBorder(
						null, "Punkt av interesse", TitledBorder.DEFAULT_JUSTIFICATION, 
						TitledBorder.DEFAULT_POSITION, 
						new Font("Tahoma", Font.PLAIN, 11), 
						new Color(0, 70, 213)));
				northPanel.setLayout(borderLayout);
				northPanel.add(getCoordPanel(), BorderLayout.NORTH);
				northPanel.setPreferredSize(new Dimension(275,300));

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
	/*private JPanel getTextAreaPanel() {
		if (textAreaPanel == null) {
			try {
				textAreaPanel = new JPanel();
				textAreaPanel.setLayout(new BorderLayout());
				northPanel.setPreferredSize(new Dimension(200,300));
				textAreaPanel.add(txtAreaLabel, BorderLayout.NORTH);
				textAreaPanel.add(getTextAreaScrollPane(), BorderLayout.CENTER);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return textAreaPanel;
	}*/
	
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
									esriMGRSModeEnum.esriMGRSMode_NewWith180InZone01);
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(app.getFrame(),
			                        "Ugyldig MGRS koordinat", null, 
			                        JOptionPane.WARNING_MESSAGE);
							ex.printStackTrace();
						}
						try {
							if (tool.getCurrentPOI() == null) {
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
						if (tool.getCurrentPOI() != null) {
							IPOIIf poi = tool.getCurrentPOI();
							poi.setRemarks(txtArea.getText());
							poi.setType((POIType)getTypeComboBox().getSelectedItem());
							fireDialogStateChanged();
						}
						// hide me
						setVisible(false);
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return finishButton;
	}

	public void onSelectionChanged(MsoLayerEvent e) throws IOException, AutomationException {
		if (tool.getMap() == null) {
			return;
		}
		IMsoFeatureLayer msoLayer = (IMsoFeatureLayer)e.getSource();
		List selection = msoLayer.getSelected();
		if (selection != null && selection.size() > 0) {
			IMsoFeature msoFeature = (IMsoFeature)selection.get(0);
			IMsoObjectIf msoObject = msoFeature.getMsoObject();
			if (!isVisible()) {
				tool.getMap().setActiveTool(tool);
			}
			//tool.setEditObject(msoObject);
			IPOIIf poi = (IPOIIf)msoObject;
			tool.setCurrentPOI(poi);
			com.esri.arcgis.geometry.Point point = 
				(com.esri.arcgis.geometry.Point)msoFeature.getShape();
			String mgrs = point.createMGRS(5, true, esriMGRSModeEnum.esriMGRSMode_NewWith180InZone01);
			getMGRSTextField().setText(mgrs);
			getTxtArea().setText(poi.getRemarks());
			POIType type = poi.getType();
			if (type != null) {
				getTypeComboBox().setSelectedItem(type);
			}
		}
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
