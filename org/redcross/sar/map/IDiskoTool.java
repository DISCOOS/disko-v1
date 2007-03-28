package org.redcross.sar.map;

import java.io.IOException;

import org.redcross.sar.gui.DiskoDialog;

import com.esri.arcgis.interop.AutomationException;

public interface IDiskoTool {
	
	public void toolActivated() throws IOException, AutomationException;
	
	public void toolDeactivated() throws IOException, AutomationException;
	
	public DiskoDialog getDialog();

}
