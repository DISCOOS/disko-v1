package org.redcross.sar.wp;

import java.io.IOException;

import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.event.DiskoMapEvent;
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
		map.setIsEditable(true);
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
