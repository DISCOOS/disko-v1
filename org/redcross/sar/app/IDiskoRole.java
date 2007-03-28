package org.redcross.sar.app;

import org.redcross.sar.wp.IDiskoWpModule;

/**
 * Provides access to properties and methods for all members that 
 * implements this interface.
 * @author geira
 *
 */

public interface IDiskoRole {

	/**
	 * Add a new work process module to this DiskoRole.
	 * @param module A class that implements the IDiskoWpModule interface.
	 */
	public void addDiskoWpModule(IDiskoWpModule module);
	
	/**
	 * Activate an work process module with the given ID, 
	 * usually the name of the work process
	 * @param id An id that identifies a work process module
	 */
	public void selectDiskoWpModule(String id);
	
	/**
	 * Activate an works process module at the given index.
	 * @param index And index that identifies a work process module
	 */
	public void selectDiskoWpModule(int index);

	/**
	 * Get the name of this DiskoRole.
	 * @return The name
	 */
	public String getName();

	/**
	 * Get the title of this DiskoRole.
	 * @return The title
	 */
	public String getTitle();

	/**
	 * Get a description of this DiskoRole.
	 * @return The description
	 */
	public String getDescription();
	
	/**
	 * Get a reference to the current (active) work process module
	 * @return The current worksprocess
	 */
	public IDiskoWpModule getCurrentDiskoWpModule();
	
	/**
	 * Get a reference to the DiskoApplication.
	 * @return A reference to the DiskoApplication
	 */
	public IDiskoApplication getApplication();
	
}