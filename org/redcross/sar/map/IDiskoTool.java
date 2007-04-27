package org.redcross.sar.map;

import java.io.IOException;

import org.redcross.sar.gui.DiskoDialog;

import com.esri.arcgis.interop.AutomationException;

public interface IDiskoTool {
	
	public void toolActivated() throws IOException, AutomationException;
	
	public void toolDeactivated() throws IOException, AutomationException;
	
	public DiskoDialog getDialog();
	
	public IDiskoMap getMap();
	
	/**
	 * Set the graphic element name for tools adding graphics to the graphics layer. 
	 * This name is used for identifying graphics in the map.
	 * @param name The name of the graphic element
	 */
	public void setElementName(String name);
	
	/**
	 * Get the graphic element name for tools adding graphics to the graphics layer. 
	 * This name is used for identifying graphics in the map.
	 * @return The name of the graphic element
	 */
	public String getElementName();
}
