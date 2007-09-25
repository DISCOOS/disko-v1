package org.redcross.sar.wp.unit;

import org.redcross.sar.wp.IDiskoWp;
import org.redcross.sar.wp.IDiskoWpModule;

public interface IDiskoWpUnit extends IDiskoWpModule, IDiskoWp
{	
	/*
	 * Getters and setters
	 */
	public boolean getNewPersonnel();
	public void setNewPersonnel(boolean newPersonnel);
	public boolean getNewUnit();
	public void setNewUnit(boolean newUnit);
	public boolean getNewCallOut();
	public void setNewCallOut(boolean newCallOut);
}
