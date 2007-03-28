/**
 * 
 */
package org.redcross.sar.wp;

import java.io.IOException;

import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.map.DiskoMap;

import com.esri.arcgis.carto.FeatureLayer;

/**
 * Implements the DiskoApKart interface
 * @author geira
 *
 */
public class DiskoWpMapImpl extends AbstractDiskoWpModule implements IDiskoWpMap {

	private FeatureLayer puiLayer = null;
	
	/**
	 * Constructs a DiskoApKartImpl
	 * @param rolle A reference to the DiskoRolle
	 */
	public DiskoWpMapImpl(IDiskoRole rolle) {
		super(rolle);
		initialize();
	}
	
	private void initialize() {
		DiskoMap map = getMap();
		map.setIsEditable(true);
		layoutComponent(map);
	}
	
	public void onMapReplaced(DiskoMapEvent e) throws IOException {
		DiskoMap map = getMap();
		puiLayer = map.getFeatureLayer(getProperty("PUI.featureClass.Name"));
		puiLayer.setSelectable(true);
		map.setEditLayer(puiLayer);
	}
	
	/* (non-Javadoc)
	 * @see com.geodata.engine.disko.task.DiskoAp#getName()
	 */
	public String getName() {
		return "Kart";
	}

	/* (non-Javadoc)
	 * @see com.geodata.engine.disko.task.DiskoAp#cancel()
	 */
	public void cancel() {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.geodata.engine.disko.task.DiskoAp#finish()
	 */
	public void finish() {
		// TODO Auto-generated method stub	
	}
}
