package org.redcross.sar.gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import org.redcross.sar.map.IDrawTool;

import com.borland.jbcl.layout.VerticalFlowLayout;

import com.esri.arcgis.carto.IFeatureLayer;
import com.esri.arcgis.interop.AutomationException;

public class SnapPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private IDrawTool tool = null;
	private JPanel snapPanel = null;
	private JPanel snapButtonPanel = null;
	private JPanel snapSelectionPanel = null;
	private JSlider snapToleranceSlider = null;
	private JButton snapAllButton = null;
	private JButton snapNoneButton = null;
	private JButton snapApplyButton = null;
	private ArrayList<JCheckBox> checkBoxes = new ArrayList<JCheckBox>();  //  @jve:decl-index=0:
	private ArrayList<IFeatureLayer> layers = new ArrayList<IFeatureLayer>();
	
	public SnapPanel(IDrawTool tool) {
		this.tool = tool;
		checkBoxes = new ArrayList<JCheckBox>();
		layers = new ArrayList<IFeatureLayer>();
		initialize();
	}
	
	private void initialize() {
		try {
			BorderLayout borderLayout = new BorderLayout();
			borderLayout.setVgap(5);
			this.setLayout(borderLayout);
			this.add(getSnapToleranceSlider(), BorderLayout.NORTH);
			this.add(getSnapPanel(), BorderLayout.CENTER);
		}
		catch (java.lang.Throwable e) {
			//  Do Something
		}
	}
		
	private JPanel getSnapPanel() {
		if (snapPanel == null) {
			try {
				snapPanel = new JPanel();
				VerticalFlowLayout vfl = new VerticalFlowLayout();
				vfl.setVgap(5);
				vfl.setHgap(0);
				vfl.setAlignment(VerticalFlowLayout.TOP);
				snapPanel.setLayout(vfl);
				snapPanel.setBorder(BorderFactory.createTitledBorder(null, 
						"Snapp til kartlag", TitledBorder.LEFT, TitledBorder.TOP, 
						new Font("Tahoma", Font.PLAIN, 11), new Color(0, 70, 213)));
				snapPanel.add(getSnapSelectionPanel(), BorderLayout.NORTH);
				snapPanel.add(getSnapButtonPanel(), BorderLayout.CENTER);
			} catch (java.lang.Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return snapPanel;
	}
	
	private JPanel getSnapButtonPanel() {
		if (snapButtonPanel == null) {
			try {
				
				// create layer
				snapButtonPanel = new JPanel();
				snapButtonPanel.setLayout(new FlowLayout());
				// create buttons
				snapAllButton = DiskoButtonFactory.createSmallButton("Alle");
				snapNoneButton = DiskoButtonFactory.createSmallButton("Ingen");
				snapApplyButton = DiskoButtonFactory.createSmallButton("Bruk");
				// add action listeners
				snapAllButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						checkAll();
					}
				});
				snapNoneButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						checkNone();
					}
				});
				snapApplyButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						// apply Snapping
						tool.setIsDirty();
					}
				});
				// add buttons to panel
				snapButtonPanel.add(snapAllButton);
				snapButtonPanel.add(snapNoneButton);
				snapButtonPanel.add(snapApplyButton);
				
			} catch (java.lang.Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return snapButtonPanel;
	}	
	
	private JPanel getSnapSelectionPanel() {
		if (snapSelectionPanel == null) {
			try {
				snapSelectionPanel = new JPanel();
				VerticalFlowLayout vfl = new VerticalFlowLayout();
				vfl.setVgap(0);
				vfl.setHgap(0);
				vfl.setAlignment(VerticalFlowLayout.TOP);
				snapSelectionPanel.setLayout(vfl);
			} catch (java.lang.Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return snapSelectionPanel;
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
						"Snapping toleranse", TitledBorder.LEFT, TitledBorder.TOP, 
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

	private void checkAll() {
		for (int i = 0; i < checkBoxes.size(); i++) {
			JCheckBox cb = (JCheckBox)checkBoxes.get(i);
			cb.setSelected(true);
		}
	}
	
	private void checkNone() {
		for (int i = 0; i < checkBoxes.size(); i++) {
			JCheckBox cb = (JCheckBox)checkBoxes.get(i);
			cb.setSelected(false);
		}
	}
	
	public ArrayList<IFeatureLayer> getSnapableLayers() {
		ArrayList<IFeatureLayer> snapableLayers = new ArrayList<IFeatureLayer>();
		for (int i = 0; i < checkBoxes.size(); i++) {
			JCheckBox cb = (JCheckBox)checkBoxes.get(i);
			if (cb.isVisible() && cb.isSelected()) {
				snapableLayers.add(layers.get(i));
			}
		}
		return snapableLayers;
	}
	
	public void setSnapTolerance(int value) {
		snapToleranceSlider.setValue(value);
	}
	
	public void updateSnapableLayers(List<IFeatureLayer> updateLayers) {
		//adding checkboxes
		try {
			hideAll();
			JPanel panel = getSnapSelectionPanel();
			for (int i = 0; i < updateLayers.size(); i++) {
				IFeatureLayer flayer = (IFeatureLayer)updateLayers.get(i);
				JCheckBox cb = getCheckBox(flayer.getName());				
				if (cb == null) {
					cb = new JCheckBox(flayer.getName());
					checkBoxes.add(cb);
					layers.add(flayer);
					panel.add(cb);
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
}
