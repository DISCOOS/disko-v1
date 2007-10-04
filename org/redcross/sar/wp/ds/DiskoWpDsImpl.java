package org.redcross.sar.wp.ds;

import java.util.EnumSet;

import javax.swing.JToggleButton;

import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.gui.NavBar;
import org.redcross.sar.gui.UIFactory;
import org.redcross.sar.wp.AbstractDiskoWpModule;

/**
 * Implements the DiskoWpStates interface
 * 
 * @author kengu
 * 
 */
public class DiskoWpDsImpl extends AbstractDiskoWpModule 
		implements IDiskoWpDs {

    private RouteCostPanel m_routeCost;
    
	/**
	 * Constructs a DiskoWpDsImpl
	 * 
	 * @param rolle
	 *            A reference to the DiskoRolle
	 */
	public DiskoWpDsImpl(IDiskoRole rolle) {
		super(rolle);
		// initialize gui
	    initialize();
	}

	private void initialize() {
		loadProperties("properties");						
        m_routeCost = new RouteCostPanel(this);
        layoutComponent(m_routeCost);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.geodata.engine.disko.task.DiskoAp#getName()
	 */
	public String getName() {
		return "Beslutningsstøtte";
	}

	public void activated() {
		super.activated();
		NavBar navBar = getApplication().getNavBar();
		
		// add navbar tools
		EnumSet<NavBar.ToolCommandType> myTools = EnumSet.of(NavBar.ToolCommandType.FREEHAND_TOOL);
		myTools.add(NavBar.ToolCommandType.ERASE_COMMAND);
		
		myTools.add(NavBar.ToolCommandType.ZOOM_IN_TOOL);
		myTools.add(NavBar.ToolCommandType.ZOOM_OUT_TOOL);
		myTools.add(NavBar.ToolCommandType.PAN_TOOL);
		myTools.add(NavBar.ToolCommandType.SELECT_FEATURE_TOOL);
		myTools.add(NavBar.ToolCommandType.ZOOM_FULL_EXTENT_COMMAND);
		myTools.add(NavBar.ToolCommandType.ZOOM_TO_LAST_EXTENT_FORWARD_COMMAND);
		myTools.add(NavBar.ToolCommandType.ZOOM_TO_LAST_EXTENT_BACKWARD_COMMAND);
		myTools.add(NavBar.ToolCommandType.MAP_TOGGLE_COMMAND);
		myTools.add(NavBar.ToolCommandType.TOC_COMMAND);
		navBar.showButtons(myTools);

		//this will show the Navbar
		UIFactory uiFactory = getApplication().getUIFactory();
		JToggleButton navToggle = uiFactory.getMainMenuPanel().getNavToggleButton();
		if (!navToggle.isSelected()) {
			navToggle.doClick();
		}
		
	}
	
	public void deactivated() {
		super.deactivated();
		NavBar navBar = getApplication().getNavBar();
		navBar.hideDialogs();
	}
	
	public void cancel() {
		// TODO Auto-generated method stub
	}

	public void finish() {
		// TODO Auto-generated method stub
	}	
}
