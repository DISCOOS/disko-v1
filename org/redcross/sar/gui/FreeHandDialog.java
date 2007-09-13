package org.redcross.sar.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.map.FreeHandTool;
import org.redcross.sar.map.IDiskoMap;

import com.borland.jbcl.layout.VerticalFlowLayout;
import com.esri.arcgis.carto.IFeatureLayer;
import com.esri.arcgis.interop.AutomationException;

public class FreeHandDialog extends DiskoDialog {
	
	private static final long serialVersionUID = 1L;
	private FreeHandTool tool = null;
	private JPanel mainPanel = null;
	private JSlider snapToleranceSlider = null;
	private JPanel layerSelectionPanel = null;
	private JPanel centerPanel = null;
	private ArrayList<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();
	private ArrayList<IFeatureLayer> layers = new ArrayList<IFeatureLayer>();
	
	public FreeHandDialog(IDiskoApplication app, FreeHandTool tool) {
		super(app.getFrame());
		this.tool = tool;
		checkBoxes = new ArrayList<JCheckBox>();
		layers = new ArrayList<IFeatureLayer>();
		initialize();
	}
	
	public void onLoad(IDiskoMap map) throws IOException {
		getSnapToleranceSlider().setValue((int)tool.getSnapTolerance());
	}
	
	private void initialize() {
		try {
            this.setContentPane(getMainPanel());
            this.setPreferredSize(new Dimension(225,500));
            this.pack();
				
		}
		catch (java.lang.Throwable e) {
			//  Do Something
		}
	}
	
	
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			try {
				mainPanel = new JPanel();
				mainPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				mainPanel.setLayout(new BorderLayout());
				mainPanel.add(getCenterPanel(), BorderLayout.CENTER);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return mainPanel;
	}
	
	public void setSnapTolerance(double tol) {
		snapToleranceSlider.setValue((int)tol);
	}
	
	private JSlider getSnapToleranceSlider() {
		if (snapToleranceSlider == null) {
			try {
				snapToleranceSlider = new JSlider();
				snapToleranceSlider.setOrientation(JSlider.HORIZONTAL);
				snapToleranceSlider.setMinorTickSpacing(10);
				snapToleranceSlider.setMajorTickSpacing(50);
				snapToleranceSlider.setPaintLabels(true);
				snapToleranceSlider.setPaintTicks(true);
				snapToleranceSlider.setMaximum(250);
				snapToleranceSlider.setBorder(BorderFactory.createTitledBorder(null, 
						"Snapp Tol", TitledBorder.LEFT, TitledBorder.TOP, 
						new Font("Tahoma", Font.PLAIN, 11), new Color(0, 70, 213)));
				
				snapToleranceSlider.addChangeListener(new javax.swing.event.ChangeListener() {
					public void stateChanged(javax.swing.event.ChangeEvent e) {
						try {
							tool.setSnapTolerance(snapToleranceSlider.getValue());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return snapToleranceSlider;
	}
	
	private JPanel getSnapLayerPanel() {
		if (layerSelectionPanel == null) {
			try {
				layerSelectionPanel = new JPanel();
				VerticalFlowLayout vfl = new VerticalFlowLayout();
				vfl.setVgap(0);
				vfl.setHgap(0);
				vfl.setAlignment(VerticalFlowLayout.TOP);
				layerSelectionPanel.setLayout(vfl);
				layerSelectionPanel.setBorder(BorderFactory.createTitledBorder(null, 
						"Snapp til", TitledBorder.LEFT, TitledBorder.TOP, 
						new Font("Tahoma", Font.PLAIN, 11), new Color(0, 70, 213)));
			} catch (java.lang.Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return layerSelectionPanel;
	}
	
	private JCheckBox getCheckBox(String name) {
		for (int i = 0; i < checkBoxes.size(); i++) {
			JCheckBox cb = (JCheckBox)checkBoxes.get(i);
			if (cb.getText().equals(name)) {
				return cb;
			}
		}
		return null;
	}
	
	private void hideAll() {
		for (int i = 0; i < checkBoxes.size(); i++) {
			JCheckBox cb = (JCheckBox)checkBoxes.get(i);
			cb.setVisible(false);
		}
	}
	
	public List getSnapableLayers() {
		ArrayList<IFeatureLayer> snapableLayers = new ArrayList<IFeatureLayer>();
		for (int i = 0; i < checkBoxes.size(); i++) {
			JCheckBox cb = (JCheckBox)checkBoxes.get(i);
			if (cb.isVisible() && cb.isSelected()) {
				snapableLayers.add(layers.get(i));
			}
		}
		return snapableLayers;
	}
	
	public void updateLayerSelection(List<IFeatureLayer> updateLayers) {
		//adding checkboxes
		try {
			hideAll();
			for (int i = 0; i < updateLayers.size(); i++) {
				IFeatureLayer flayer = (IFeatureLayer)updateLayers.get(i);
				JCheckBox cb = getCheckBox(flayer.getName());
				if (cb == null) {
					cb = new JCheckBox(flayer.getName());
					checkBoxes.add(cb);
					layers.add(flayer);
					getSnapLayerPanel().add(cb);
					cb.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							tool.setIsDirty();
						}
					});
				}
				cb.setVisible(true);
			}
		} catch (AutomationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method initializes centerPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCenterPanel() {
		if (centerPanel == null) {
			try {
				BorderLayout borderLayout = new BorderLayout();
				borderLayout.setVgap(5);
				centerPanel = new JPanel();
				centerPanel.setLayout(borderLayout);
				centerPanel.add(getSnapToleranceSlider(), BorderLayout.NORTH);
				centerPanel.add(getSnapLayerPanel(), BorderLayout.CENTER);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return centerPanel;
	}
}
