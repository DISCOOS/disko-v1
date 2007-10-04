/**
 * 
 */
package org.redcross.sar.ds;

import java.lang.Math;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

import org.redcross.sar.map.DiskoMap;

import com.esri.arcgis.carto.ILayer;
import com.esri.arcgis.carto.FeatureLayer;
import com.esri.arcgis.carto.RasterLayer;

/**
 *  Route Cost property class
 *  
 * @author kennetgu
 *
 */

public class RouteCostProp {
	
	protected ResourceBundle m_res;	  	
  	
	private int m_count;
	private int m_default;
	private int[] m_dims;
	private double m_min;
	private double m_max;
	private double m_weight;
	private double m_alfa;
	private double m_alfa_inv;
	private boolean m_constant;
	private String m_key;
	private String m_name;
  	private HashMap m_keys;
  	private String[] m_captions;
  	private Object[] m_params;
  	private HashMap<String,String> m_fields;
  	private HashMap<String,ILayer> m_layers;
  	private HashMap<String,Integer> m_mapping;
  	private DiskoMap m_map;
	
	/**
	 *  Constructor initialize the maximum index value
	 *  
	 */		
	public RouteCostProp(RouteCostProps parent, String key) {

		// save resource bundle handle
		m_res = parent.getResource();
		
		// save map handle
		m_map = parent.getMap();
		
		// save property key
		m_key = key;
		
		// get property name
		m_name = m_res.getString(key);
		
		// forward
		fill();
	}

	/**
	 *  Fills property from resource file
	 *  
	 */		
	@SuppressWarnings("unchecked")
	public void fill() {
		
		// create array lists
		m_fields = new HashMap<String,String>();
		m_layers = new HashMap<String,ILayer>();
		m_mapping = new HashMap<String,Integer>();
		
		// initialize
		m_default = 0;
		
		// get attribute count
		m_count = Integer.valueOf(getProp(m_key + ".COUNT")).intValue();
		
		// get min and max value
		m_min = Double.valueOf(getProp(m_key + ".MIN")).doubleValue();
		m_max = Double.valueOf(getProp(m_key + ".MAX")).doubleValue();
		
		// calculate alfa
		m_alfa = (m_max - m_min) / (m_count - 1);
		
		// calculate inverted alfa
		m_alfa_inv = 1 / m_alfa;
		
		// get constant flag
		m_constant = Boolean.valueOf(getProp(m_key + ".CONSTANT")).booleanValue();
		
		// create key-to-index map
		m_keys = new HashMap(m_count);
		
		// allocate memory
		m_captions = new String[m_count];
					
		// get all keys and captions
		for(int i=0;i<m_count;i++){
			// put key i
			m_keys.put(getProp(m_key + ".KEY." + i),i);
			// put caption i
			m_captions[i] = getProp(m_key + ".CAPTION." + i);				
		}

		// get parameters
		if (m_constant) {
			// get weight
			m_weight = Double.valueOf(getProp(m_key + ".WEIGHT")).doubleValue();
		}
		else {
			// apply default weight
			m_weight = 1;			
			// get parameter dimension count
			int dim = Integer.valueOf(getProp(m_key + ".PARAM.DIM.COUNT")).intValue();
			// allocate dimensions array
			m_dims = new int[dim];
			// loop over the next dimensions
			for(int i=0;i<dim;i++){
				// get getProp(namecount
				int upper = Integer.valueOf(getProp(m_key + ".PARAM.DIM." + i)).intValue();
				// save in dimension size array
				m_dims[i] = upper;
				// add dimension
				addDimension(m_params, upper + 1);
			}
			// get all parameters
			getParams(m_params,m_key + ".PARAM");
			// has feature class?
			if (Boolean.valueOf(getProp(m_key + ".FEATURECLASS")).booleanValue())
			{
				// get feature class count
				int count = Integer.valueOf(getProp(m_key + ".FEATURECLASS.COUNT")).intValue();
				try {
					// loop over the next dimensions
					for(int i=0;i<count;i++){
						// raster layer?
						if (Boolean.valueOf(getProp(m_key + ".FEATURECLASS.RASTER." + i)).booleanValue()) {
							// get name
							String name = getProp(m_key + ".FEATURECLASS.NAME." + i);
							// get field name
							m_fields.put(name,"");
							// create raster layer
							RasterLayer layer = new RasterLayer();
							layer.createFromFilePath(getProp(m_key + ".FEATURECLASS.FILE." + i));
							// add layer
							m_layers.put(name, layer);
						}
						else {
							// get name
							String name = getProp(m_key + ".FEATURECLASS.NAME." + i);
							// get field name
							m_fields.put(name,getProp(m_key + ".FEATURECLASS.FIELD." + i));
							// add layer
							m_layers.put(name, (ILayer)m_map.getFeatureLayer(getProp(m_key + ".FEATURECLASS.LAYER." + i)));
						}
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				// has feature class?
				if (Boolean.valueOf(getProp(m_key + ".FEATURECLASS.MAPPING")).booleanValue()){
					// get mapping
					for(int i=0;i<m_count;i++){
						String map = getProp(m_key + ".FEATURECLASS.MAPPING." + i);
						if (map.length()>0) {
							String[] splits = map.split("#");
							for(int j=0; j<splits.length;j++){
								m_mapping.put(splits[j], i);
							}
						}
					}						
					// get default map index
					m_default = Integer.valueOf(getProp(m_key + ".FEATURECLASS.MAPPING.DEFAULT"));
				}
			}
		}					
	}
	
	/**
	 *  Gets property resource bundle key
	 *  
	 */		
	public String getKey() {
		return m_key;
	}
	
	/**
	 *  Gets property variable name
	 *  
	 */		
	public String getName() {
		return m_name;
	}
	
	/**
	 *  Gets minimum property variable value
	 *  
	 */		
	public double getMin() {
		return m_min;
	}
	
	/**
	 *  Gets maxmum property property value
	 *  
	 */		
	public double getMax() {
		return m_max;
	}
	
	/**
	 *  Gets is property is a constant. If false, then the property have parameters
	 *  
	 */		
	public boolean isConstant() {
		return !m_constant;
	}
	
	/**
	 *  Gets minimum property variable value
	 *  
	 */		
	public double getWeight() {
		return m_weight;
	}
	
	/**
	 *  Gets property index count
	 *  
	 */		
	public int getCount() {
		return m_count;
	}
	
	/**
	 *  Gets property from index from variable value
	 *  
	 */		
	public int getIndex(double variable) {
		// apply min-max on variable
		variable = Math.max(Math.min(variable,m_max),m_min);
		// index = (count - 1)/(max - min) * (variable - min)
		return (int)(m_alfa_inv * (variable - m_min));
	}
	
	/**
	 *  Gets property index from map value
	 *  
	 */		
	public int getIndexFromMap(int map) {
		// initialize
		int index = m_default;
		String key = String.valueOf(map);
		// Has map?
		if (m_mapping.containsKey(key)) {
			// get map index
			index = m_mapping.get(key);
		} 
		// return index
		return index;
	}
	
	/**
	 *  Gets property value at index with specified arguments
	 *  
	 */		
	public double getValue(int index, int[] args, boolean weigth) {
		if(weigth){
			// value = Weigth * Parameter * Variable
			return m_weight * getParam(args)*getVariable(index);						
		}
		else {
			// value = Parameter * Variable
			return getParam(args)*getVariable(index);			
		}
	}
	
	/**
	 *  Gets weighted property variable
	 *  
	 */		
	public double getWeightedVariable(double variable) {
		// apply min-max on variable
		variable = Math.max(Math.min(variable,m_max),m_min);
		// value = weigth * Variable
		return m_weight*variable;
	}
	
	/**
	 *  Limit property variable value
	 *  
	 */		
	public double limitVariable(double variable) {
		// apply min-max on variable
		return Math.max(Math.min(variable,m_max),m_min);
	}
	
	/**
	 *  Gets property variable value from index
	 *  
	 */		
	public double getVariable(int index) {
		// variable = (max - min)/(count - 1) * index + min
		return m_alfa * index + m_min;
	}
	
	/**
	 *  Gets property value caption from index
	 *  
	 */		
	public String getCaption(int index) {
		return m_captions[index];
	}
	
	/**
	 *  Gets parameter argument count
	 *  
	 */		
	public int getArgumentCount() {
		return m_dims.length;
	}
	
	/**
	 *  Gets maximum argument value from index
	 *  
	 */		
	public int getArgumentMax(int index) {
		return m_dims[index];
	}
	
	/**
	 *  Gets property parameter value from argument array
	 *  
	 */		
	public double getParam(int[] args) {
		// start to recurse on first argument
		return getParam(m_params,args,0);
	}

	/**
	 *  Gets layer
	 *  
	 */		
	public ILayer getLayer(String name) {
		return m_layers.get(name);
	}
	
	/**
	 *  Gets field name
	 *  
	 */		
	public String getField(String name) {
		if (getLayer(name) instanceof FeatureLayer)
			return m_fields.get(name);
		else
			return null;
	}

	/**
	 *  Gets field index
	 *  
	 */		
	public int getFieldIndex(String name) {
		// initialize
		int index = -1;
		// get layer
		ILayer layer = getLayer(name);
		// get index
		if (layer instanceof FeatureLayer) {
			
			try {
				// get field index
				index = ((FeatureLayer)layer).getFields().findField(m_fields.get(name));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return index;
	}
	
	
	/**
	 *  Allocates a new array to all cells in array
	 *  
	 */		
	private void addDimension(Object[] arr, int size) {
		// is empy?
		if (arr == null){
			// create dimension array
			m_params = new Object[size];			
		}
		else {
			for(int i=0;i<arr.length;i++){
				// get object
				Object o = arr[i];
				// has value?
				if(o != null){
					// is array?
					if(arr[i].getClass().isArray()){
						// cast and recurse
						addDimension((Object[])arr[i],size);
					}
				}
				// allocate
				arr[i]= new Object[size];
			}
		}
	}
	
	/**
	 *  get all parameters in current dimension
	 *  
	 */		
	private void getParams(Object[] arr, String key) {
		for(int i=0;i<arr.length;i++){
			// initialize
			boolean bfill = true;
			// get object
			Object o = arr[i];
			// get key
			String nextKey = new String(key + "." + i);
			// has value?
			if(o != null){
				// is array?
				if(arr[i].getClass().isArray()){
					// cast and recurse
					getParams((Object[])arr[i],nextKey);
					// do not fill this cell...
					bfill = false;
				}
			}
			// get property value?
			if (bfill){
				// try to get property value
				try
				{
					// get parameter
					arr[i]= Double.valueOf(m_res.getString(nextKey)).doubleValue();
				}
				catch(MissingResourceException e)
				{
					System.err.println("Unable to get property " + nextKey + " in RouteCostParams");
				}
			}
		}
	}
	
	/**
	 *  get parameter value as function of arguments = {i1,i2,...,in}
	 *  
	 */		
	private double getParam(Object[] arr, int[] ids, int i) {
		int j = ids[i];
		// is array?
		if(arr[j].getClass().isArray()){
			// cast and recurse
			return getParam((Object[])arr[j],ids,i+1);
		}
		else {
			// get parameter value
			double param = ((Double)arr[j]).doubleValue();
			// 
			return param;
		}
	}
	
	/**
	 *  get property from resourse bundle
	 *  
	 */		
	private String getProp(String name) {
		String value = null;
		// try to get properties
		try
		{
			value = m_res.getString(name);
		}
		catch(MissingResourceException e)
		{
			System.err.println("Unable to get property " + name + " in RouteCostParams");
		}
		return value;		
	}
}
