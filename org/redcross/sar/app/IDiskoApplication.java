package org.redcross.sar.app;

import org.redcross.sar.gui.UIFactory;
import org.redcross.sar.map.DiskoMap;
import org.redcross.sar.mso.IMsoModelIf;

import com.esri.arcgis.geometry.IEnvelope;

import javax.swing.*;


/**
 * Provides access to properties and methods in the Disko application.
 * @author geira
 *
 */
public interface IDiskoApplication {
	
	/**
	 * Get the current (active) role
	 * @return The current role
	 */
	public IDiskoRole getCurrentRole();
	
	/**
	 * Get the current (active) map
	 * @return The current active map
	 */
	public DiskoMap getCurrentMap();
	
	/**
	 * Refresh all map in this DiskoApplication. Should be called after database updates.
	 * @param env A envelope to define the area to refresh
	 */
	public void refreshAllMaps(IEnvelope env);
	
	/**
	 * Get a reference to the main frame in this application. Is used
	 * when creating other frame and dialogs.
	 * @return The main frame
	 */
	public JFrame getFrame();
	
	/**
	 * Get a property whith the given name.
	 * @param key The name of the property
	 * @return A property with the given name
	 */
	public String getProperty(String key);
	
	/**
	 * Return a reference to the UIFactory class. This class contains
	 * methods to create GUI component for this application
	 * @return A reference to the UIFactory
	 */
	public UIFactory getUIFactory();
	
	/**
	 * Get a reference to the DiskoModuleLoader. This class is responsible
	 * for loading work process modules
	 * @return A reference to the DiskoModuleLoader
	 */
	public DiskoModuleLoader getDiskoModuleLoader();
	
	/**
	 * Open the login dialog
	 * @param rolleName Default role name
	 * @param user Default user name
	 * @param password Default password
	 */
	public void login(String rolleName, String user, char[] password);

    /**
     * Get a reference to the MsoModel.
     * This class is responsible for all communication with the data model and data server (SARA).
     * @return A reference to the MsoModel.
     */
    public IMsoModelIf getMsoModel();

}