package org.redcross.sar.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.map.DrawTool;
import org.redcross.sar.map.IDiskoMap;
import org.redcross.sar.map.SnapLayerSelectionModel;

import com.borland.jbcl.layout.VerticalFlowLayout;
import com.esri.arcgis.carto.IFeatureLayer;
import com.esri.arcgis.interop.AutomationException;

public class DrawDialog extends DiskoDialog {
	
	private static final long serialVersionUID = 1L;
	private DrawTool tool = null;
	private SnapLayerSelectionModel snapLayerSelectionModel = null;  //  @jve:decl-index=0:
	private JPanel mainPanel = null;
	private JSlider snapToleranceSlider = null;
	private JPanel layerSelectionPanel = null;
	private JPanel centerPanel = null;
	
	public DrawDialog(IDiskoApplication app, DrawTool tool) {
		super(app.getFrame());
		this.tool = tool;
		initialize();
	}
	
	public void onLoad(IDiskoMap map) throws IOException {
		this.snapLayerSelectionModel = map.getSnapLayerSelectionModel();
		updateLayerSelection();
		getSnapToleranceSlider().setValue((int)tool.getSnapTolerance());
	}
	
	public SnapLayerSelectionModel getSnapModel() {
		return snapLayerSelectionModel;
	}
	
	private void initialize() {
		try {
            this.setContentPane(getMainPanel());
            this.setPreferredSize(new Dimension(195,500));
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
				//mainPanel.add(getSouthPanel(), BorderLayout.SOUTH);
				//mainPanel.add(getNorthPanel(), BorderLayout.NORTH);
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
				snapToleranceSlider.setOrientation(JSlider.VERTICAL);
				snapToleranceSlider.setMinorTickSpacing(5);
				snapToleranceSlider.setMajorTickSpacing(20);
				snapToleranceSlider.setPaintLabels(true);
				snapToleranceSlider.setPaintTicks(true);
				snapToleranceSlider.setMaximum(100);
				snapToleranceSlider.setPreferredSize(new Dimension(80, 175));
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
	
	private void updateLayerSelection() {
		//adding checkboxes
		try {
			getSnapLayerPanel().removeAll();
			for (int i = 0; i < snapLayerSelectionModel.getLayerCount(); i++) {
				final int index = i;
				final JCheckBox cb = new JCheckBox();
				IFeatureLayer flayer = snapLayerSelectionModel.getFeatureLayer(i);
				cb.setText(flayer.getName());
				cb.setSelected(snapLayerSelectionModel.isSelected(i));
				getSnapLayerPanel().add(cb);
				cb.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						snapLayerSelectionModel.setSelected(index, cb.isSelected());
						try {
							tool.setSnapableLayers(snapLayerSelectionModel.getSelected());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
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
