package org.redcross.sar.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.ButtonGroup;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.event.IDiskoMapEventListener;
import org.redcross.sar.map.DiskoMap;
import org.redcross.sar.map.IDiskoMap;
import org.redcross.sar.map.IDiskoTool;
import org.redcross.sar.map.TocTool;
import org.redcross.sar.map.MsoLayerSelectionModel;
import org.redcross.sar.map.DefaultMapLayerSelectionModel;
import org.redcross.sar.map.WMSLayerSelectionModel;

import com.borland.jbcl.layout.VerticalFlowLayout;
import com.esri.arcgis.carto.IFeatureLayer;
import com.esri.arcgis.carto.WMSMapLayer;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.systemUI.ICommand;

import javax.swing.JLabel;
import java.awt.event.KeyEvent;

public class TocDialog extends DiskoDialog implements IDiskoMapEventListener{
	
	private IDiskoApplication app = null;
	private static final long serialVersionUID = 1L;
	private JPanel contentPanel = null;
	private JPanel layerSelectionPanel = null;
	private ButtonGroup bgLayers = new ButtonGroup();
	private MsoLayerSelectionModel msoLayerSelectionModel = null;  //  @jve:decl-index=0:
	private DefaultMapLayerSelectionModel defaultMapLayerSelectionModel = null;
	private WMSLayerSelectionModel wmsLayerSelectionModel = null;
	private JLabel labelMsoLayers = null;
	private JLabel labeWMSLayers = null;
	private JLabel labelDefMapLayers = null;
	private DiskoMap map = null;
	
	public TocDialog(IDiskoApplication app, TocTool tool) {
		super(app.getFrame());
		this.app = app;
		map = (DiskoMap) app.getCurrentMap();
        map.addDiskoMapEventListener(this);
		initialize();
	}
	
	public void onExtentUpdated(DiskoMapEvent e) throws IOException, AutomationException{
		//todo
	}

	public void onMapReplaced(DiskoMapEvent e) throws IOException, AutomationException{		
		//Må oppdaterer kartlagsliste
		
		//MsoLayerSelectionModel er den samme
		map.setMsoLayerSelectionModel();
		this.msoLayerSelectionModel = map.getMsoLayerSelectionModel();

		//DefaultMapLayerSelectionModel må oppdateres		
		map.setDefaultMapLayerSelectionModel();
		this.defaultMapLayerSelectionModel = map.getDefaultMapLayerSelectionModel();
		
		//WMSLayerSelectionModel må oppdateres
		map.setWMSLayerSelectionModel();
		this.wmsLayerSelectionModel = map.getWMSLayerSelectionModel();
		
		//todo - må oppdatere snapLayerModell og flankLayerModell også 
		
		updateLayerSelection(map);

		this.layerSelectionPanel.updateUI();
		
	}
	
	public void onAfterScreenDraw(DiskoMapEvent e) throws IOException, AutomationException{
//		todo
	}
	
	public void onSelectionChanged(DiskoMapEvent e) throws IOException, AutomationException{
//		todo
	}
	
	public void editLayerChanged(DiskoMapEvent e){
//		todo
	}
	
	
	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		try {
			
            this.setContentPane(getContentPanel());
            this.setPreferredSize(new Dimension(190, 400));
            this.pack();
		}
		catch (java.lang.Throwable e) {
			//  Do Something
		}
	}
	
	public void onLoad(IDiskoMap map) throws IOException {
		this.msoLayerSelectionModel = map.getMsoLayerSelectionModel();
		this.defaultMapLayerSelectionModel = map.getDefaultMapLayerSelectionModel();
		this.wmsLayerSelectionModel = map.getWMSLayerSelectionModel();
		updateLayerSelection(map);
		//updateLayerSelection();
		//getSnapToleranceSlider().setValue((int)tool.getSnapTolerance());
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
				contentPanel.add(getLayerPanel(), BorderLayout.CENTER);
				//contentPanel.add(getNorthPanel(), BorderLayout.NORTH);
				//contentPanel.add(getSouthPanel(), BorderLayout.SOUTH);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}			
		}
		return contentPanel;
	}
	
	private JPanel getLayerPanel() {
		if (layerSelectionPanel == null) {
			try {						
				
				layerSelectionPanel = new JPanel();
				VerticalFlowLayout vfl = new VerticalFlowLayout();
				vfl.setVgap(0);
				vfl.setHgap(0);
				vfl.setAlignment(VerticalFlowLayout.TOP);
				layerSelectionPanel.setLayout(vfl);
				layerSelectionPanel.setBorder(BorderFactory.createTitledBorder(null, 
						"Kartlag", TitledBorder.LEFT, TitledBorder.TOP, 
						new Font("Tahoma", Font.PLAIN, 11), new Color(0, 70, 213)));				
				
			} catch (java.lang.Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return layerSelectionPanel;
	}
	
	private void updateLayerSelection(IDiskoMap map) {
		//adding checkboxes
		try {
			getLayerPanel().removeAll();
			
			
			//adding checkboxes for MSO layers
			labelMsoLayers = new JLabel();
			labelMsoLayers.setText("Mso kartlag");
			labelMsoLayers.setFont(new Font("Dialog", Font.BOLD, 14));
			labelMsoLayers.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
			getLayerPanel().add(labelMsoLayers);						
			for (int i = 0; i < msoLayerSelectionModel.getLayerCount(); i++) {
				final int index = i;
				final JCheckBox cb = new JCheckBox();
				IFeatureLayer flayer = msoLayerSelectionModel.getFeatureLayer(i);				
				cb.setText(flayer.getName().toLowerCase());
				cb.setSelected(msoLayerSelectionModel.isSelected(i));
				getLayerPanel().add(cb);	
				cb.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						msoLayerSelectionModel.setSelected(index, cb.isSelected());
						try {
							msoLayerSelectionModel.setlayerVisibility(cb.isSelected(), index);
						} catch (AutomationException a1) {
							// TODO Auto-generated catch block
							a1.printStackTrace();							
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}					
					}
				});
			}		
			
			//adding checkboxes for WMS layers
			if(wmsLayerSelectionModel.getLayerCount() > 0){
				JSeparator jSep = new JSeparator();
				getLayerPanel().add(jSep);
				labeWMSLayers = new JLabel();
				labeWMSLayers.setText("WMS kartlag");
				labeWMSLayers.setFont(new Font("Dialog", Font.BOLD, 14));
				labeWMSLayers.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
				getLayerPanel().add(labeWMSLayers);
				
				for (int i = 0; i < wmsLayerSelectionModel.getLayerCount(); i++) {
					final int index = i;
					final JCheckBox cb_wms = new JCheckBox();
					WMSMapLayer wmslayer = (WMSMapLayer) wmsLayerSelectionModel.getFeatureLayer(i);				
					cb_wms.setText(wmslayer.getName().toLowerCase());
					cb_wms.setSelected(wmsLayerSelectionModel.isSelected(i));
					getLayerPanel().add(cb_wms);	
					cb_wms.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							wmsLayerSelectionModel.setSelected(index, cb_wms.isSelected());
							try {
								wmsLayerSelectionModel.setlayerVisibility(cb_wms.isSelected(), index);
							} catch (AutomationException a1) {
								// TODO Auto-generated catch block
								a1.printStackTrace();							
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}					
						}
					});
				}	
			}
			
			//adding one checkbox for default mapdatalayers
			if (defaultMapLayerSelectionModel.getLayerCount() > 0){
				JSeparator jSep2 = new JSeparator();
				getLayerPanel().add(jSep2);
				//legge til overskrift
				labelDefMapLayers = new JLabel();
				labelDefMapLayers.setText("Grunnkart");
				labelDefMapLayers.setFont(new Font("Dialog", Font.BOLD, 14));
				getLayerPanel().add(labelDefMapLayers);
				
				final JCheckBox cb_baseMap = new JCheckBox();
				cb_baseMap.setText("Grunnkart");
				cb_baseMap.setSelected(true);
				getLayerPanel().add(cb_baseMap);	
				cb_baseMap.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent ev2) {
						defaultMapLayerSelectionModel.setAllSelected(cb_baseMap.isSelected());
						try {
							defaultMapLayerSelectionModel.setAllLayerVisibility(cb_baseMap.isSelected());
						} catch (AutomationException a2) {
							// TODO Auto-generated catch block
							a2.printStackTrace();							
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
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
	
	
}
