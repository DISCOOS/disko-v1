package org.redcross.sar.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.map.IDrawTool;
import org.redcross.sar.map.IDiskoMap;

import com.esri.arcgis.carto.IFeatureLayer;

public class FreeHandDialog extends DiskoDialog implements IDrawDialog {
	
	private static final long serialVersionUID = 1L;
	private IDrawTool tool = null;
	private JPanel mainPanel = null;
	private SnapPanel snapPanel = null;
	
	public FreeHandDialog(IDiskoApplication app, IDrawTool tool) {
		super(app.getFrame());
		this.tool = tool;
		initialize();
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
				// create panels
				mainPanel = new JPanel();
				snapPanel = new SnapPanel(tool);
				BorderLayout borderLayout = new BorderLayout();
				borderLayout.setVgap(5);
				mainPanel.setLayout(borderLayout);
				mainPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				mainPanel.add(snapPanel, BorderLayout.NORTH);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return mainPanel;
	}
		
	public void onLoad(IDiskoMap map) throws IOException {
		snapPanel.setSnapTolerance((int)tool.getSnapTolerance());
	}
	
	public void setSnapTolerance(int value) {
		snapPanel.setSnapTolerance(value);
	}
	
	public ArrayList<IFeatureLayer> getSnapableLayers() {
		return snapPanel.getSnapableLayers();
	}
	
	public void updateSnapableLayers(ArrayList<IFeatureLayer> updateLayers) {
		snapPanel.updateSnapableLayers(updateLayers);
	}
}
