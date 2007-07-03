package org.redcross.sar.map;

import java.io.IOException;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.TocDialog;

import com.esri.arcgis.interop.AutomationException;

public class TocTool extends AbstractTool {
	
	private static final long serialVersionUID = 1L;
	
	private IDiskoApplication app = null; 
	
	/**
	 * Constructs the DrawTool
	 */
	public TocTool(IDiskoApplication app) throws IOException, AutomationException {
		this.app = app;
		//dialog = new TocDialog(app, this);
		//dialog.setIsToggable(false);
		System.out.println("TocTool, heia, does nothing");
	}
	
	public void onCreate(Object obj) throws IOException, AutomationException {
		/*
		 if (obj instanceof IDiskoMap) {
			System.out.println("TocTool onCreate");
			map = (DiskoMap)obj;
			map.addDiskoMapEventListener(this);
			TocDialog tocDialog = (TocDialog)dialog;
			tocDialog.onLoad(map);
			tocDialog.setLocationRelativeTo(map, DiskoDialog.POS_EAST, true);			
		}
		*/
		System.out.println("TocTool, oncreate, does nothing");
	}
	
	public void onClick() throws IOException, AutomationException {
		if (dialog == null){
			dialog = new TocDialog(app, this);
			dialog.setIsToggable(false);
			map = (DiskoMap) app.getCurrentMap();
			map.addDiskoMapEventListener(this);
			TocDialog tocDialog = (TocDialog)dialog;
			tocDialog.onLoad(map);
			tocDialog.setLocationRelativeTo(map, DiskoDialog.POS_EAST, true);

			System.out.println("TocTool, heia, instansiert");
		}
		

	}
	
	
}
