package org.redcross.sar.map;

import java.io.IOException;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.TocDialog;

import com.esri.arcgis.interop.AutomationException;

public class TocCommand extends AbstractCommandTool {
	
	private static final long serialVersionUID = 1L; 
	
	/**
	 * Constructs the DrawTool
	 */
	public TocCommand(IDiskoApplication app) throws IOException, AutomationException {
		dialog = new TocDialog(app, this);
		dialog.setIsToggable(false);
	}
	
	public void onCreate(Object obj) throws IOException, AutomationException {
		 if (obj instanceof IDiskoMap) {
			map = (DiskoMap)obj;
			TocDialog tocDialog = (TocDialog)dialog;
			tocDialog.onLoad(map);
			tocDialog.setLocationRelativeTo(map, DiskoDialog.POS_EAST, true);			
		}
	}
}
