package org.redcross.sar.wp;

/**
 * Provides access to properties and methods for all members that 
 * implements this interface.  All work processes (arbeidsprosesser) 
 * must implement this interface.
 * @author geira
 *
 */
public interface IDiskoWp {
	
	/**
	 * Must be called when a task is canceled. Rollback.
	 */
	public void cancel();
	
	/**
	 * Must be called when a task is finished
	 */
	public void finish();
}