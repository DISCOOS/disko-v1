package org.redcross.sar.ds;

import java.util.Calendar;
import java.util.ArrayList;

import java.awt.geom.Point2D;
import java.io.IOException;

import org.redcross.sar.util.mso.Route;
import org.redcross.sar.util.mso.GeoPos;
import org.redcross.sar.map.DiskoMap;
import org.redcross.sar.map.MapUtil;

import com.esri.arcgis.system.Array;
import com.esri.arcgis.display.IDisplayTransformation;
import com.esri.arcgis.carto.RasterLayer;
import com.esri.arcgis.carto.FeatureLayer;
import com.esri.arcgis.carto.IRowIdentifyObjectProxy;
import com.esri.arcgis.geodatabase.IFeatureProxy;
import com.esri.arcgis.datasourcesraster.Raster;
import com.esri.arcgis.geometry.ISpatialReference;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.geometry.IPoint;
import com.esri.arcgis.geometry.Polyline;
import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.esriSegmentExtension;
import com.esri.arcgis.interop.AutomationException;


/**
 *  Calculates route time cost along a spesified route
 *  
 * @author kennetgu
 *
 */
public class RouteCost {

    public enum SurfaceType
    {
        DEFAULT,
    	PATH,
        OPEN,
        SCATTERED,
        DENSE,
        VERY_DENSE,
        INFINITE
    }
    
	private RouteCostProps m_params = null;

    private double m_propulsion = 0;
	private ArrayList<Double> m_distances = null;
	private ArrayList<Double> m_slopes = null;
	private ArrayList<Double> m_terrainUnitCosts = null;
	private ArrayList<Double> m_weatherUnitCosts = null;
	private ArrayList<Double> m_lightUnitCosts = null;
	private ArrayList<Double> m_cumulativeCosts = null;
	private ArrayList<Double> m_cumulativeLength = null;
	private ArrayList<Integer> m_themes = null;
	private ArrayList<Calendar> m_timeSteps = null;
	private ArrayList<GeoPos> m_pl = null;
	private Calendar m_t0 = null;
	private boolean m_isDirty = false;
    
	private Route m_route = null;
	private SnowInfo m_info_si = null;
	private LightInfo m_info_li = null;
	private WeatherInfo m_info_wi = null;
	
	private Point m_point1 = null;	
	private Point m_point2 = null;	
	private Polyline m_polyline = null;
	
	private DiskoMap m_map = null;
	private Envelope m_extent = null;
	private Envelope m_search = null;
	private ISpatialReference m_srs = null;
	private IDisplayTransformation m_transform = null;
	private Raster m_altitude = null;
	private FeatureLayer m_roads = null;
	private FeatureLayer m_surface = null;
	private int m_roadsIndex = 0;
	private int m_surfaceIndex = 0;
	
	// properties
	private RouteCostProp m_p;
	private RouteCostProp m_sst;
	private RouteCostProp m_nsd;
	private RouteCostProp m_ss;
	private RouteCostProp m_su;
	private RouteCostProp m_s;
	private RouteCostProp m_pre;
	private RouteCostProp m_win;
	private RouteCostProp m_tem;
	private RouteCostProp m_w;
	private RouteCostProp m_li;
	
	// arguments
	private int[] m_arg_su = {0,0}; // {propulsion,snow state} 
	private int[] m_arg_s = {0,0}; // {propulsion,snow state}
	private int[] m_arg_w = {0}; // {propulsion}
	private int[] m_arg_li = {0}; // {propulsion}
	
	/**
	 *  Constructor
	 *  
	 *  @param route The Route to calculate time cost for
	 */		
	public RouteCost(Route route, int propulsion, DiskoMap map) {
		
		// save by reference
		m_map = map;
		
		// create objects
		m_info_wi = new WeatherInfo();
		m_info_si = new SnowInfo();
		m_info_li = new LightInfo();
		
		// create parameters
		m_params = RouteCostProps.getInstance();
		m_params.create(m_map);
		
		// get properties
		m_p = m_params.getProp("P");
		m_sst = m_params.getProp("P");
		m_nsd = m_params.getProp("NSD");
		m_ss = m_params.getProp("SS");
		m_su = m_params.getProp("SU");
		m_s = m_params.getProp("S");
		m_pre = m_params.getProp("PRE");
		m_win = m_params.getProp("WIN");
		m_tem = m_params.getProp("TEM");
		m_w = m_params.getProp("W");
		m_li = m_params.getProp("LI");
		
		try {
  			// get map spatial reference 
  			m_srs = map.getSpatialReference();
  			// create new points
			m_point1 = new Point();
			m_point1.setX(0);
			m_point1.setY(0);
			m_point1.setSpatialReferenceByRef(MapUtil.getGeographicCS());
			m_point2 = new Point();
			m_point2.setX(0);
			m_point2.setY(0);
			m_point2.setSpatialReferenceByRef(MapUtil.getGeographicCS());
			// create altitude information
			m_altitude = (Raster)((RasterLayer)m_s.getLayer("ALTITUDE")).getRaster();
			// get surface information
			m_roads = (FeatureLayer)m_su.getLayer("ROADS");
			m_surface = (FeatureLayer)m_su.getLayer("SURFACE");
			m_roadsIndex = m_su.getFieldIndex("ROADS");
			m_surfaceIndex = m_su.getFieldIndex("SURFACE");
			// create search envelope
			m_search = new Envelope();
			m_search.putCoords(0, 0, 0, 0);
			m_search.setHeight(10);
			m_search.setWidth(10);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		// save members
		setPropulsion(propulsion);
		
		// forward
		setRoute(route);
		
	}

	/**
	 *  Gets the spesified route
	 *  
	 *  @return The route to estimate time cost for
	 */		
	public Route getRoute() {
		// return specified route
		return m_route;
	}

	/**
	 *  Sets the spesified route
	 *  
	 *  @param The route to estimate time cost for
	 */		
	public void setRoute(Route route) {
		// save route
		m_route = route;
		try {
			// get polyline
			m_polyline = MapUtil.getEsriPolyline(m_route,m_map.getSpatialReference());						
		}
		catch (Exception e) {
			e.printStackTrace();
		}		
		// get least bounding square around route
		m_extent = getLeastRectangle();
		// set dirty flag
		m_isDirty = true;
	}

	/**
	 *  Gets the spesified propulsion method along the route
	 *  
	 *  @return Returns propulsion method along the route
	 */		
	public double getPropulsion() {
		// return propulsion method
		return m_propulsion;
	}

	/**
	 *  Sets the spesified propulsion method along the route
	 *  
	 *  @param The propulsion method along the route
	 *  @return Returns the propulsion index
	 */		
	public int setPropulsion(double propulsion) {
		// limit propulsion and save
		m_propulsion = m_p.limitVariable(propulsion);
		// update arguments
		m_arg_su[0] = m_p.getIndex(m_propulsion);
		m_arg_s[0] = m_arg_su[0];
		m_arg_w[0] = m_arg_su[0];
		m_arg_li[0] = m_arg_su[0];
		// return index
		return m_arg_su[0];
	}
	
	/**
	 *  Gets the route length
	 *  
	 *  @return Returns length of route
	 */		
	public double getLength() {
		// return great circle distance?
		if (!m_isDirty)
			return m_cumulativeLength.get(m_pl.size() - 2);
		else
			return 0;
	}	
	
	/**
	 *  Gets the route length at position index
	 *  
	 *  @return Returns route length at position index
	 */		
	public double getLength(int index) {
		// return great circle distance?
		if (!m_isDirty)
			if (index == 0)
				return 0;
			else
				return m_cumulativeLength.get(index - 1);
		else
			return 0;
	}	
	
	/**
	 *  Calculated estimated time enroute for a specified route.
	 *  
	 *  @return Estimated time enroute for a specified route
	 */		
	public int estimate() throws Exception{
		return estimateRouteCost(0,null);			
	}
	
	/**
	 *  Calculated estimated time enroute for a specified route.
	 *  
	 *  @param from Start estimation from position index
	 *  @return Estimated time enroute for a specified route
	 */		
	public int estimate(int from, Calendar t0) throws Exception{
		return estimateRouteCost(from,null);			
	}
	
	/**
	 *  Calculates estimated time enroute for a spesified route from a given position.
	 *  
	 *  @param pos Calculation is started from a given position
	 *  @param t0 Initial point in time
	 *  @return Estimated time enroute for a specified route from a given position and point in time
	 */		
	public int estimate(GeoPos pos, Calendar t0) throws Exception {
		return estimateRouteCost(findNearest(pos),t0);
	}

	/**
	 *  Calculates estimated time enroute to a given position
	 *  
	 *  @param index Enroute position
	 *  @return Estimated time enroute to a given position
	 */		
	public int ete() {
		// return great circle distance?
		if (!m_isDirty)
			return m_cumulativeCosts.get(m_cumulativeCosts.size() - 2).intValue();
		else
			return 0;
	}	
	
	/**
	 *  Calculates estimated time enroute to a given position
	 *  
	 *  @param index Enroute position
	 *  @return Estimated time enroute to a given position
	 */		
	public int ete(int index) {
		// return great circle distance?
		if (!m_isDirty)
			if (index == 0)
				return 0;
			else
				return m_cumulativeCosts.get(index - 1).intValue();
		else
			return 0;
	}
	
	/**
	 *  Calculates estimated time of arrival.
	 *  
	 *  @return Estimated time of arrival at last position
	 */		
	public Calendar eta() {
		if (!m_isDirty)
			return m_timeSteps.get(m_timeSteps.size() -1 );
		else
			return null;
	}
		
	/**
	 *  Calculates estimated time of arrival at position.
	 *  
	 *  @param index Positon index
	 *  @return Estimated time of arrival at position
	 */		
	public Calendar eta(int index) {
		if (!m_isDirty)
			return m_timeSteps.get(index);
		else
			return null;
	}
	
	/**
	 *  Get the nearest position in route
	 *  
	 *  @param match Time position
	 *  @return Returns the closest position found
	 */		
	private int findNearest(GeoPos match) throws Exception {
		// initialize variables
		GeoPos gp = null;
		
		// create position
		IPoint p = new Point(); 			
		p.setX(match.getPosition().x);
		p.setY(match.getPosition().y);
		p.setSpatialReferenceByRef(m_map.getSpatialReference());	
		// find closest
		p = m_polyline.returnNearestPoint(p, esriSegmentExtension.esriNoExtension);
		// update match
		gp = new GeoPos(p.getX(),p.getY());

		// get iterator
		ArrayList<GeoPos> it = new ArrayList<GeoPos>(m_route.getPositions());
		
		// get count
		int count = it.size();
		
		// search for object 
		for(int i = 0; i < count; i++){
			// found?
			if (it.get(i).equals(gp))
				return i;
		}
		// return not found
		return -1;
	}
	
	
	/**
	 *  Get the position count in the internal route
	 *  
	 *  @return Returns the position count in the internal route
	 */		
	private int getCount() {
		// get count
		return m_route.getPositions().size();
	}	
	
	/**
	 *  Get the least bounding rectangle around route
	 *  
	 *  @return Returns the least bounding rectangle around route
	 */		
	private Envelope getLeastRectangle() {
		// initialize
		Envelope extent = null;			
		try {
			// get extent
			extent = (Envelope)m_polyline.getEnvelope();			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// return extent		
		return extent;
	}
	
	/**
	 *  Get distance between two positions
	 *  
	 *  @param p1 Start position in desimal degrees
	 * 	@param p2 Stop position  in desimal degrees
	 *  @return Returns distance between two positions in desimal degrees
	 */		
	private double getDistance(GeoPos p1, GeoPos p2) {
		// initialize
		double d = 0 ;
		try {
			/*String mgrs1 = null;
			String mgrs2 = null;
			mgrs1 = MapUtil.getMGRSfromPosition(new Position("P1",p1.getPosition()));
			mgrs2 = MapUtil.getMGRSfromPosition(new Position("P2",p2.getPosition()));
			System.out.println(mgrs1);
			System.out.println(mgrs2);
			// print positions in lon/lat format
			System.out.println(point1.toString());
			System.out.println(point2.toString());*/
			// create positions
			Point2D.Double point1 = p1.getPosition();
			Point2D.Double point2 = p2.getPosition();
			// calculate distance
			d = MapUtil.greatCircleDistance(point1.y, point1.x, point2.y, point2.x);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		// return distance
		return d;			
	}

	/**
	 *  Get slope between two positions.
	 *  
	 *  If returned slope is positive: uphill.
	 *  If returned slope is negative: downhill.
	 *  
	 *  @param p1 Start position
	 * 	@param p2 Stop position 
	 *  @return Returns slope between two positions
	 */		
	private double getSlope(GeoPos p1, GeoPos p2) {
		// initialize
		double s = 0;
		int[] col = {0};
		int[] row = {0};
		try {
			// get heigth from model
			m_altitude.mapToPixel(m_point1.getX(), m_point1.getY(), col, row);
			// get height
			s = new Double(m_altitude.getPixelValue(0, col[0], row[0]).toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// set to - from*/
		return s;
	}
	
	/**
	 *  Get terrain unit cost
	 *  
	 *  @param s Slope angle between position pair (pi,i)
	 *  @param t Time at current position
	 *  @param i Current position
	 *  @return Returns terrain unit cost for position pair (pi,i)
	 */		
	private double getTerrainCost(double s, Calendar t, int i) {
		int ss = 0;				// snow state
		int rt = 0;				// resistance state type
		double cost = 0;
		
		// get types indexes
		ss = getSnowState(t,i-1);				
		rt = getResistanceType(i);		
		
		// get unit costs
		cost = getSurfaceCost(ss,rt);		
		cost += getSlopeCost(ss,s);		
		
		return cost;
	}

	/**
	 *  Get weather unit cost at position i
	 *  
	 *  @param t Time at position
	 *  @param i Current position  
	 *  @return Returns weather unit cost at position i
	 */		
	private double getWeatherCost(Calendar t, GeoPos i) {
		// get weather type
		int wt = getWeatherType(t,i);		
		// return lookup value
		return m_w.getValue(wt, m_arg_w, false);
	}
	
	/**
	 *  Get weather type
	 *  
	 *  @param pre Precipitation type
	 *  @param win Wind type
	 *  @param tem Temperature type
	 *  @return Returns weather type
	 */		
	private int getWeatherType(Calendar t, GeoPos i) {
		// initialize
		double wt = 0;
		try {
			// get point
			Point2D.Double p = i.getPosition();
			// get forecasted weather at position
			WeatherInfoPoint wf = m_info_wi.getForecast(t,p.x,p.y);
			// found?
			if(wf != null) {
				// get weather type
				wt = m_pre.getWeightedVariable(wf.m_pre) 
						  	+ m_win.getWeightedVariable(wf.m_win) 
			  				+ m_tem.getWeightedVariable(wf.m_tem); 
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// return weather type
		return (int)wt;			
	}
	
	/**
	 *  Get light unit cost at position i
	 *  
	 *  @param t Time at position
	 *  @param i Current position  
	 *  @return Returns light unit cost at position i
	 */		
	private double getLightCost(Calendar t, GeoPos i) {
		// get day or night at position and time
		int lt = getLightType(t,i);				
		// return lookup value
		return m_li.getValue(lt, m_arg_li, false);
	}

	/**
	 *  Get surface unit cost
	 *  
	 *  @param ss Snow state
	 *  @param rt Resistance type
	 *  @return Returns surface unit cost
	 */		
	private double getSurfaceCost(int ss, int su) {
		// update snow state
		m_arg_su[1] = ss;
		// return lookup value
		return m_su.getValue(su, m_arg_su, false);
	}

	/**
	 *  Get slope unit cost
	 *  
	 *  @param ss Snow State at previous position pi
	 *  @param s Slope angle between position pair (pi,i)
	 *  @param pi Previous position
	 *  @param i Current position
	 *  @return Returns surface unit cost between position pair (pi,i)
	 */		
	private double getSlopeCost(int ss, double s) {
		// update snow state
		m_arg_s[1] = ss;
		// get slope type
		int st = getSlopeType(s);
		// return lookup
		return m_s.getValue(st, m_arg_s, false);			
	}

	/**
	 *  Get resistance type
	 *  
	 *  @param s Slope angle between position pair (pi,i)
	 *  @param t Time at current position
	 *  @param i Current position
	 *  @return Returns resistance type between position pair (pi,i)
	 */		
	private int getResistanceType(int i) {
		try {
			
			// initialize
			IFeatureProxy pFeature = null;			
			IRowIdentifyObjectProxy pRowObj = null;   
			
			// save current
			m_point2 = m_point1;
			
			// update current
			m_point1 = (Point)m_polyline.getPoint(i);
			
			// prepare search envelope
			m_search.centerAt(m_point1);
			
			// identify height below point
			Array arr = (Array)m_roads.identify(m_search);
			
			// found road?
			if (arr != null) {
			
				// Get the feature that was identified by casting to IRowIdentifyObject   
				pRowObj = new IRowIdentifyObjectProxy(arr.getElement(0));   
				pFeature = new IFeatureProxy(pRowObj.getRow());
				
				// get map value
				int map = Integer.valueOf(pFeature.getValue(m_roadsIndex).toString());
				
				// get variable index
				int index = m_su.getIndexFromMap(map);
				
				// get feature value
				m_themes.add(index);
			
			} 
			else {
			
				// identify surface below point
				arr = (Array)m_surface.identify(m_search);
				
				// found anything?
				if (arr != null) {
				
					// Get the feature that was identified by casting to IRowIdentifyObject   
					pRowObj = new IRowIdentifyObjectProxy(arr.getElement(0));   
					pFeature = new IFeatureProxy(pRowObj.getRow());
					
					// get map value
					int map = Integer.valueOf(pFeature.getValue(m_surfaceIndex).toString());
					
					// get variable index
					int index = m_su.getIndexFromMap(map);
					
					// get feature value
					m_themes.add(index);
				
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// return default
		return 0;
	}
	
	/**
	 *  Get slope type index
	 *  
	 *  @param s Slope angle between position pair (pi,i)
	 *  @return Returns slope type between position pair (pi,i)
	 */		
	private int getSlopeType(double s) {
		// limit variable and return slope type index
		return (int)m_s.getIndex(s);
	}	

	/**
	 *  Get snow state index at position i
	 *  
	 *  @param t Time at current position
	 *  @param i Current position
	 *  @return Returns snow type at position i
	 */		
	private int getSnowState(Calendar t, int i) {
		// initialize
		double st = 0;
		try {
			// get position
			Point2D.Double p = m_pl.get(i).getPosition();
			// get forecasted snow state at position
			SnowInfoPoint sf = m_info_si.getForecast(t,p.x,p.y);
			// found?
			if(sf != null) {
				// get snow type
				st = m_sst.getWeightedVariable(sf.m_sst) 
						  	+ m_nsd.getWeightedVariable(sf.m_nsd); 
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		// return snow state index
		return (int)m_ss.getIndex(st);						
	}
		
	/**
	 *  Get light type at position and time
	 *  
	 *  @param t Time at position
	 *  @param i Current position  
	 *  @return Returns light type at position and time
	 */		
	private int getLightType(Calendar t, GeoPos i) {
		// initialize
		double lt = 0;
		try {
			// get point
			Point2D.Double p = i.getPosition();
			// get forecasted light at position
			LightInfoPoint lf = m_info_li.getForecast(t,p.x,p.y);
			// found?
			if(lf != null ){
				// translate to type
				lt = m_li.getWeightedVariable(lf.m_li);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return (int)lt;
	}

	/**
	 *  identify point
	 */		
	private void identify(int i) throws IOException, AutomationException {
		
		/*
		// initialize
		IFeatureProxy pFeature = null;			
		IRowIdentifyObjectProxy pRowObj = null;   
		
		// save current
		m_point2 = m_point1;
		
		// update current
		m_point1 = (Point)m_polyline.getPoint(i);
		
		// get field index
		int index = m_surface.getFields().findField("FTEMA");

		// identify surface below point
		Array arr = (Array)m_surface.identify(m_point1);
		
		// found anything?
		if (arr.getCount()>0) {
		
			// Get the feature that was identified by casting to IRowIdentifyObject   
			pRowObj = new IRowIdentifyObjectProxy(arr.getElement(0));   
			pFeature = new IFeatureProxy(pRowObj.getRow());
			
			// get feature value
			m_themes.add((Double)pFeature.getValue(index));
		
		}*/
		
	}
	
	/**
	 *  Internal algorithm that calculates time cost along a spesified route 
	 *  from a given position.
	 *  
	 *  @param offset Calculation is started from given position
	 *  @param t0 Initial point in time
	 *  @return Estimated elapsed time from given position
	 */		
	private int estimateRouteCost(int offset, Calendar t0) throws Exception  {

		// initialize
		double cost = 0;			// cumulative time cost in seconds
	
		//m_params.create(m_map);
		//m_params.getMapAnalyst().buildIndexes(m_extent);
		
		// force initial time?
		if(t0 == null)
			t0 = Calendar.getInstance();
		
		// valid parameters?
		if(offset > -1 && t0 != null){
			
			// create or update?
			if (m_isDirty || true)
				cost = createEstimate(t0);
			else
				cost = updateEstimate(offset,t0);
			
		}

		// return estimated cost
		return (int)cost;
		
	}
	
	/**
	 *  Create estimate information
	 */		
	private double createEstimate(Calendar t0) throws Exception  {

		// initialize
		GeoPos i  = null;			// current slope
		GeoPos pi = null;			// previous position
		double d = 0;				// distance between previous and current position
		double s = 0;				// slope between previous and current position
		Calendar t = null;			// set current time
		double cost = 0;			// cumulative time cost in seconds
		double length = 0;			// route length in meters
		double utc = 0;				// unit terrain cost in seconds
		double uwc = 0;				// unit weather cost in seconds
		double ulc = 0;				// unit light cost in seconds
		double cd = 0;				// segment time cost in seconds
	
		// get point count
		int count = getCount();
		
		// create objects
		m_distances = new ArrayList<Double>(count - 2);
		m_slopes = new ArrayList<Double>(count - 2);
		m_cumulativeCosts = new ArrayList<Double>(count - 2);
		m_cumulativeLength = new ArrayList<Double>(count - 2);
		m_terrainUnitCosts = new ArrayList<Double>(count - 2);
		m_weatherUnitCosts = new ArrayList<Double>(count - 2);
		m_lightUnitCosts = new ArrayList<Double>(count - 2);
		m_timeSteps = new ArrayList<Calendar>(count - 1);
		m_themes = new ArrayList<Integer>(count);

						
		// get positions as array list
		m_pl = new ArrayList<GeoPos>(m_route.getPositions());
		
		// initialize time
		m_t0 = t0;
			
		// update local time
		t = t0;
		
		// loop over all positions
		for(int j = 0; j < count; j++){
			
			// get position
			i = m_pl.get(j);
			
			// set to list
			m_timeSteps.add(t);
			
			// valid paramter?
			if(pi != null) {

				// get current time
				t.add(Calendar.SECOND, (int)cost);
				
				// get distance and slope angle
				d = getDistance(pi,i);
				s = getSlope(pi,i);
				
				// update segment distance
				m_distances.add(d);
				
				// estimate unit costs
				utc = getTerrainCost(s,t,j);
				uwc = getWeatherCost(t,pi);
				ulc = getLightCost(t,pi);
				
				// save unit costs
				m_terrainUnitCosts.add(utc);
				m_weatherUnitCosts.add(uwc);
				m_lightUnitCosts.add(ulc);
				
				// calculate segment cost
				cd = d*(utc + uwc + ulc);
				
				// add segment cost
				cost += cd;
				
				// save cumulative cost
				m_cumulativeCosts.add(cost);
				
				// add segment length
				length += d;
				
				// save cumulative cost
				m_cumulativeLength.add(length);
			}
			// update last position
			pi = i;
		}						

		// reset flag
		m_isDirty = false;			

		// return estimated cost
		return cost;
		
	}	
	
	/**
	 *  Update estimate information
	 */		
	private double updateEstimate(int offset, Calendar t0) throws Exception  {

		// initialize
		GeoPos i  = null;			// current slope
		GeoPos pi = null;			// previous position
		double d = 0;				// distance between previous and current position
		Calendar t = null;			// set current time
		double cost = 0;			// cumulative time cost in seconds
		double length = 0;			// route length in meters
		double utc = 0;				// unit terrain cost in seconds
		double uwc = 0;				// unit weather cost in seconds
		double ulc = 0;				// unit light cost in seconds
		double cd = 0;				// segment time cost in seconds
	
		// get point count
		int count = getCount();
		
		// update current time
		t = t0;
			
		// loop over all positions
		for(int j = offset; j < count; j++){
			
			// get position
			i = m_pl.get(j);
			
			// set to list
			m_timeSteps.set(j,t);
			
			// valid paramter?
			if(pi != null) {

				// get current time
				t.add(Calendar.SECOND, (int)cost);
				
				// get distance
				d = m_distances.get(j - 1);

				// get dsave unit costs
				utc = m_terrainUnitCosts.get(j - 1);
				uwc = m_weatherUnitCosts.get(j - 1);
				ulc = m_lightUnitCosts.get(j - 1);
				
				// calculate segment cost
				cd = d *(utc + uwc + ulc);
				
				// add segment cost
				cost += cd;
				
				// save cumulative cost
				m_cumulativeCosts.set(j - 1,cost);
				
				// add segment length
				length += d;
				
				// save cumulative cost
				m_cumulativeLength.set(j - 1,length);
			}
			// update last position
			pi = i;
		}
		
		// return calculated cost
		return cost;
	}	
	
}
