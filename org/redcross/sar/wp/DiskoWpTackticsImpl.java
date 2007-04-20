package org.redcross.sar.wp;

import java.io.IOException;

import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.gui.NavBar;
import org.redcross.sar.gui.UIFactory;
import org.redcross.sar.map.DiskoMap;

import com.esri.arcgis.carto.FeatureLayer;

/**
 * Implements the DiskoApTaktikk interface
 * @author geira
 *
 */
public class DiskoWpTackticsImpl extends AbstractDiskoWpModule implements IDiskoWpTacktics {
	
	private FeatureLayer basisLinjeFL = null;
	private FeatureLayer flankeFL = null;
	
	/**
	 * Constructs a DiskoApTaktikkImpl
	 * @param rolle A reference to the DiskoRolle
	 */
	public DiskoWpTackticsImpl(IDiskoRole rolle) {
		super(rolle);
		initialize();
	}
	
	private void initialize() {
		loadProperties("properties");
		DiskoMap map = getMap();
		layoutComponent(map);
	}
	
	public void onMapReplaced(DiskoMapEvent e) throws IOException {
		DiskoMap map = getMap();
		map.setName(getName()+"Map");	
		basisLinjeFL = map.getFeatureLayer(getProperty("BasicLine.featureClass.Name"));
		flankeFL = map.getFeatureLayer(getProperty("BufferPath.featureClass.Name"));
		basisLinjeFL.setSelectable(true);
		flankeFL.setSelectable(true);
		map.setEditLayer(basisLinjeFL);
	}
	
	public void activated() {
		UIFactory uiFactory = getDiskoRole().getApplication().getUIFactory();
		NavBar navBar = uiFactory.getMainPanel().getNavBar();
		int[] buttonIndexes = {
				NavBar.FLANK_TOOL,
				NavBar.DRAW_TOOL,
				NavBar.ERASE_TOOL,
				NavBar.SPLIT_TOOL,
				NavBar.ZOOM_IN_TOOL,
				NavBar.ZOOM_OUT_TOOL,
				NavBar.PAN_TOOL,
				NavBar.ZOOM_IN_FIXED_COMMAND,
				NavBar.ZOOM_OUT_FIXED_COMMAND,
				NavBar.ZOOM_FULL_EXTENT_COMMAND,
				NavBar.ZOOM_TO_LAST_EXTENT_FORWARD_COMMAND,
				NavBar.ZOOM_TO_LAST_EXTENT_BACKWARD_COMMAND
		};
		navBar.showButtons(buttonIndexes);
	}
	
	/* (non-Javadoc)
	 * @see com.geodata.engine.disko.task.DiskoAp#getName()
	 */
	public String getName() {
		return "Taktikk";
	}

	/* (non-Javadoc)
	 * @see com.geodata.engine.disko.task.DiskoAp#cancel()
	 */
	public void cancel() {
	}
	
	/* (non-Javadoc)
	 * @see com.geodata.engine.disko.task.DiskoAp#finish()
	 */
	public void finish() {
	}
}
