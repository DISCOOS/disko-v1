/**
 * 
 */
package org.redcross.sar.ds;

import java.util.HashMap;
//import java.util.logging.Logger;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

import org.redcross.sar.map.DiskoMap;

/**
 * Singleton class with route cost parameters
 * 
 * @author kennetgu
 *
 */
public class RouteCostProps {

	// Logger for this object
	//private static final Logger LOGGER = Logger.getLogger(
	//		"org.redcross.sar.ds.RouteCostProps");
		
	private DiskoMap m_map;
	private ResourceBundle m_res;
	private HashMap<String,RouteCostProp> m_props;

  	private static RouteCostProps m_this;	  	
  	
	/**
 	* Constructor
 	*/
  	private RouteCostProps() {
		// try to get resource bundle
		try
		{
			// get resource
			m_res = ResourceBundle.getBundle("org.redcross.sar.ds.properties.RouteCostParams");
			// populate
		}
		catch(MissingResourceException e)
		{
			System.err.println("Unable to load resource bundle RouteCostParams in RouteCostParams");
		}		
  	}

	/**
	 * Get singleton instance of class
	 * 
	 * @return Returns singleton instance of class
	 */
  	public static synchronized RouteCostProps getInstance()
  	{
  		if (m_this == null) {
  			// it's ok, we can call this constructor
  			m_this = new RouteCostProps();
  		}
  		// return reference
  		return m_this;
  	}

	/**
	 * Method overridden to protect singleton
	 * 
	 * @throws CloneNotSupportedException
	 * @return Returns nothing. Method overridden to protect singleton 
	 */
  	public Object clone() throws CloneNotSupportedException
  	{
  		throw new CloneNotSupportedException(); 
  		// that'll teach 'em
  	}
  	
	/**
	 * Gets Route Cost property object
	 * 
	 */
  	public RouteCostProp getProp(String key)
  	{
  		// get and return
  		return (RouteCostProp)m_props.get(key);
  	}  	
  	
  	/**
	 * Get resource bundle
	 * 
	 */
	public ResourceBundle getResource() 
  	{
  		return m_res;
  	}

  	/**
	 * Get map 
	 * 
	 */
	public DiskoMap getMap() 
  	{
  		return m_map;
  	}
	
	/**
	 * Get properties and load to memory
	 * 
	 */
  	@SuppressWarnings("unchecked")
	private void getProps() 
  	{

  		// create hash map
  		m_props = new HashMap<String,RouteCostProp>(11);
  		
  		// get propulsion
  		m_props.put("P", new RouteCostProp(this,"P"));
  		
  		// get Snow State Type
  		m_props.put("SST", new RouteCostProp(this,"SST"));
  		
  		// get New Snow Depth
  		m_props.put("NSD", new RouteCostProp(this,"NSD"));
  		
  		// get Snow State
  		m_props.put("SS", new RouteCostProp(this,"SS"));
  		
  		// get Surface
  		m_props.put("SU", new RouteCostProp(this,"SU"));
  		
  		// get Slope
  		m_props.put("S", new RouteCostProp(this,"S"));
  		
  		// get Precipitation
  		m_props.put("PRE", new RouteCostProp(this,"PRE"));
  		
  		// get Wind
  		m_props.put("WIN", new RouteCostProp(this,"WIN"));
  		
  		// get Temperature
  		m_props.put("TEM", new RouteCostProp(this,"TEM"));
  		
  		// get Temperature
  		m_props.put("W", new RouteCostProp(this,"W"));
  		
  		// get Light
  		m_props.put("LI", new RouteCostProp(this,"LI"));
  		
  	}  	

	/**
	 *  create the properties
	 *  
	 */		
	public void create(DiskoMap map) {

		long startTime = System.currentTimeMillis();

		// initialize
		String aliasName = null;
		boolean roadsAdded = false;
		
		// save map
		m_map = map;
		
		// update parameters
		getProps();
		
		/*
		// catch errors
		try {
			// get map in focus
			IMap m = map.getActiveView().getFocusMap();
			// add layers
			for (int i = 0; i < m.getLayerCount(); i++) {
				ILayer l = m.getLayer(i);
				if (l instanceof IFeatureLayer) {
					IFeatureLayer fl = (IFeatureLayer)l;
					aliasName = fl.getFeatureClass().getAliasName();
					if(aliasName != null) {
						if (aliasName.startsWith("N50_VEG_L") && !roadsAdded ) {
							m_analyst.addSearch(fl.getFeatureClass(),esriSpatialRelEnum.esriSpatialRelCrosses,"LTEMA");
							roadsAdded = true;
						} else {
							if (aliasName.startsWith("N50_AREAL_F")) {
								m_analyst.addSearch(fl.getFeatureClass(),esriSpatialRelEnum.esriSpatialRelCrosses,"FTEMA");									
							}
						}
					}
				}
			}
			long endTime = System.currentTimeMillis();
			System.out.println("RouteCostProps.create: "+(endTime-startTime)+" ms");
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		*/
	}
}



	