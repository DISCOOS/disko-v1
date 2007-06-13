package org.redcross.sar.wp;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.event.IDiskoWpEventListener;
import org.redcross.sar.map.IDiskoMap;

/**
 * This interface provides access to properties and methods for
 * handling work process modules in the Disko application. 
 * @author geira
 *
 */
public interface IDiskoWpModule {
	
	/**
	 * Get the name of this IDiskoWpModule. This name is used to identify
	 * the gui component (JPanel) where this module is placed. Implementations
	 * of this interface and subclasses must override this method to provide 
	 * a unique name for the spesific work process module.
	 * @return The name of this IDiskoWpModule
	 */
	public String getName();
	
	/**
	 * Get a reference to the DiskoMap. If the implementing class has no map, 
	 * null should be returned
	 * @return A reference to a DiskoMap
	 */
	public IDiskoMap getMap();
	
	public IDiskoRole getDiskoRole();
	
	/**
	 * Get a reference to the DiskoApplication.
	 * @return
	 */
	public IDiskoApplication getApplication();
	
	public void setCallingWp(String name);
	
	public boolean isEditing();
	
	public String getCallingWp();
	
	/**
	 * @return true if DiskoMap is different from null, false otherwise
	 */
	public boolean hasMap();
	
	/**
	 * @return true if this IDiskoWpModule has sub menu, false otherwise
	 */
	public boolean hasSubMenu();
	
	/**
	 * Called when this IDiskoWpModule is activated
	 */
	public void activated();
	
	/**
	 * Called when this IDiskoWpModule is deactivated
	 */
	public void deactivated();
	
	public void addDiskoWpEventListener(IDiskoWpEventListener listener);

    public void removeDiskoWpEventListener(IDiskoWpEventListener listener);
    
    public void showWarning(String msg);
}