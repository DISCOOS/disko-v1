package org.redcross.sar.wp.states;

import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.wp.AbstractDiskoWpModule;

/**
 * Implements the DiskoWpStates interface
 * 
 * @author kengu
 * 
 */
public class DiskoWpStatesImpl extends AbstractDiskoWpModule 
		implements IDiskoWpStates {

    private States m_states;

	/**
	 * Constructs a DiskoWpStatesImpl
	 * 
	 * @param rolle
	 *            A reference to the DiskoRolle
	 */
	public DiskoWpStatesImpl(IDiskoRole rolle) {
		super(rolle);
	    initialize();
	}

	private void initialize() {
		loadProperties("properties");						
        m_states = new States();
        layoutComponent(m_states);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.geodata.engine.disko.task.DiskoAp#getName()
	 */
	public String getName() {
		return "Tilstand";
	}

	public void activated() {
		super.activated();
	}
	
	public void deactivated() {
		super.deactivated();
	}
	
	public void cancel() {
		// TODO Auto-generated method stub
	}

	public void finish() {
		// TODO Auto-generated method stub
	}	
}
