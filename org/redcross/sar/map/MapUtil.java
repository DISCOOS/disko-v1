package org.redcross.sar.map;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Iterator;

import org.redcross.sar.app.Utils;
import org.redcross.sar.util.mso.GeoPos;
import org.redcross.sar.util.mso.Position;
import org.redcross.sar.util.mso.Route;

import com.esri.arcgis.carto.IIdentify;
import com.esri.arcgis.carto.IRowIdentifyObjectProxy;
import com.esri.arcgis.datasourcesGDB.FileGDBWorkspaceFactory;
import com.esri.arcgis.geodatabase.IFeatureClass;
import com.esri.arcgis.geodatabase.IFeatureProxy;
import com.esri.arcgis.geodatabase.Workspace;
import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.IEnvelope;
import com.esri.arcgis.geometry.IGeographicCoordinateSystem;
import com.esri.arcgis.geometry.IGeometry;
import com.esri.arcgis.geometry.IPoint;
import com.esri.arcgis.geometry.ISpatialReference;
import com.esri.arcgis.geometry.ISpatialReferenceFactory2;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.geometry.Polygon;
import com.esri.arcgis.geometry.Polyline;
import com.esri.arcgis.geometry.SpatialReferenceEnvironment;
import com.esri.arcgis.geometry.esriMGRSModeEnum;
import com.esri.arcgis.geometry.esriSRGeoCSType;
import com.esri.arcgis.geometry.IProximityOperator;
import com.esri.arcgis.geometry.esriSegmentExtension;
import com.esri.arcgis.geometry.esriGeometryType;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.system.IArray;

public class MapUtil {
	
	private static IGeographicCoordinateSystem  geographicCS = null;
	private static Workspace workspace = null;
	
	public static Workspace getWorkspace() throws AutomationException, IOException {
		if (workspace == null) {
			String dbPath = Utils.getProperty("Database.path");
			FileGDBWorkspaceFactory factory = new FileGDBWorkspaceFactory();
			workspace = (Workspace) factory.openFromFile(dbPath, 0);
		}
		return workspace;
	}

	public static IFeatureClass getFeatureClass(String name) throws AutomationException,
			IOException {
		return getWorkspace().openFeatureClass(name);
	}
	
	public static IGeographicCoordinateSystem getGeographicCS() 
			throws IOException, AutomationException {
    	if (geographicCS == null) {
    		ISpatialReferenceFactory2 spaRefFact = new SpatialReferenceEnvironment();
    		geographicCS = spaRefFact.createGeographicCoordinateSystem(
    				esriSRGeoCSType.esriSRGeoCS_WGS1984);
    	}
    	return geographicCS;
    }
	
	public static Point getEsriPoint(Position pos, ISpatialReference srs) 
			throws IOException, AutomationException {
		Point p = new Point();
		p.setX(pos.getPosition().getX());
		p.setY(pos.getPosition().getY());
		p.setSpatialReferenceByRef(getGeographicCS());
		p.project(srs);
		return p;
	}
	
	public static Point getEsriPoint(double x, double y, ISpatialReference srs) 
	throws IOException, AutomationException {
		Point p = new Point();
		p.setX(x);
		p.setY(y);
		p.setSpatialReferenceByRef(getGeographicCS());
		p.project(srs);
		return p;
	}
	
	public static Position getMsoPosistion(Point p)  
			throws IOException, AutomationException {
		Point prjPoint = (Point)p.esri_clone();
		prjPoint.project(getGeographicCS());
		return new Position(null, prjPoint.getX(), prjPoint.getY());
	}
	
	public static Polygon getEsriPolygon(org.redcross.sar.util.mso.Polygon msoPolygon, 
			ISpatialReference srs) throws IOException, AutomationException {
		Polygon esriPolygon = new Polygon();
		Collection vertices = msoPolygon.getVertices();
		Iterator iter = vertices.iterator();
		while(iter.hasNext()) {
			GeoPos pos = (GeoPos)iter.next();
			Point2D.Double pnt2D = pos.getPosition();
			Point p = new Point();
			p.setX(pnt2D.getX());
			p.setY(pnt2D.getY());
			esriPolygon.addPoint(p, null, null);
		}
		esriPolygon.setSpatialReferenceByRef(getGeographicCS());
		esriPolygon.project(srs);
		return esriPolygon;
	}
	
	public static org.redcross.sar.util.mso.Polygon getMsoPolygon(Polygon p) 
			throws IOException, AutomationException {
		Polygon prjPolygon = (Polygon)p.esri_clone();
		prjPolygon.project(getGeographicCS());
		int numPoints = prjPolygon.getPointCount();
		org.redcross.sar.util.mso.Polygon msoPolygon = 
			new org.redcross.sar.util.mso.Polygon(null, null, numPoints);
		
		for (int i = 0; i < numPoints; i++) {
			IPoint pnt = prjPolygon.getPoint(i);
			msoPolygon.add(pnt.getX(), pnt.getY());
		}
		return msoPolygon;
	}
	
	public static Polyline getEsriPolyline(Route route, ISpatialReference srs) 
			throws IOException, AutomationException {
		Polyline esriPolyline = new Polyline();
		esriPolyline.setSpatialReferenceByRef(getGeographicCS());
		Collection vertices = route.getPositions();
		Iterator iter = vertices.iterator();
		while(iter.hasNext()) {
			GeoPos pos = (GeoPos)iter.next();
			Point2D.Double pnt2D = pos.getPosition();
			Point p = new Point();
			p.setX(pnt2D.getX());
			p.setY(pnt2D.getY());
			esriPolyline.addPoint(p, null, null);
		}
		esriPolyline.project(srs);
		return esriPolyline;
	}
	
	public static Route getMsoRoute(Polyline pl) 
			throws IOException, AutomationException {
		Polyline prjPolyline = (Polyline)pl.esri_clone();
		prjPolyline.project(getGeographicCS());
		int numPoints = prjPolyline.getPointCount();
		Route route = new Route(null, null, numPoints);
		for (int i = 0; i < numPoints; i++) {
			IPoint pnt = prjPolyline.getPoint(i);
			route.add(pnt.getX(), pnt.getY());
		}
		return route;
	}

	public static IEnvelope getEnvelope(IPoint p, double size) 
			throws IOException, AutomationException {
		IEnvelope env = new Envelope();
		double xmin = p.getX()-size/2;
		double ymin = p.getY()-size/2;
		double xmax = p.getX()+size/2;
		double ymax = p.getY()+size/2;
		env.putCoords(xmin, ymin, xmax, ymax);
		return env;
	}
	
	public static String getMGRSfromPosition(Position pos) 
			throws AutomationException, IOException {
		Point point = new Point();
		point.setX(pos.getPosition().getX());
		point.setY(pos.getPosition().getY());
		point.setSpatialReferenceByRef(getGeographicCS());
		return point.createMGRS(5, true, esriMGRSModeEnum.esriMGRSMode_NewWith180InZone01);
	}
	
	public static Position getPositionFromMGRS(String mgrs) 
			throws UnknownHostException, IOException {
		Point point = new Point();
		point.setSpatialReferenceByRef(getGeographicCS());
		point.putCoordsFromMGRS(mgrs, esriMGRSModeEnum.esriMGRSMode_NewWith180InZone01);
		return new Position(null, point.getX(), point.getY());
	}
	
	/**
	 * Calculate great circle distance in meters.
	 * 
	 * @param lat1 - Latitude of origin point in decimal degrees
	 * @param lon1 - longitude of origin point in deceimal degrees
	 * @param lat2 - latitude of destination point in decimal degrees
	 * @param lon2 - longitude of destination point in decimal degrees
	 * 
	 * @return metricDistance - great circle distance in meters
	 */
	public static double greatCircleDistance(double lat1, double lon1, double lat2, double lon2) {
		double R = 6367444.6571; // mean polar and equatorial radius (WGS84)
		double dLat = Math.toRadians(lat2-lat1);
		double dLon = Math.toRadians(lon2-lon1); 
		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		        Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * 
		        Math.sin(dLon/2) * Math.sin(dLon/2); 
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		return R * c;
	}
	
	
	/**
	 * Calculate Spherical Azimuth (Bearing) in degrees
	 * 
	 * @param lat0 - Latitude of origin point in decimal degrees
	 * @param lon0 - longitude of origin point in deceimal degrees
	 * @param lat  - latitude of destination point in decimal degrees
	 * @param lon  - longitude of destination point in decimal degrees
	 * 
	 * @return Spherical Azimuth (Bearing) in degrees
	 */
	public static double sphericalAzimuth(double lat0, double lon0, double lat, double lon) {
		double radLat0 = Math.toRadians(lat0);
		double radLon0 = Math.toRadians(lon0);
		double radLat  = Math.toRadians(lat);
		double radLon  = Math.toRadians(lon);
		double diff = radLon - radLon0;
		double coslat = Math.cos(radLat);

		return Math.toDegrees(normalizeAngle(Math.atan2(
			coslat * Math.sin(diff),
			(Math.cos(radLat0) * Math.sin(radLat) -
			Math.sin(radLat0) * coslat * Math.cos(diff))
		)));
	}
	
	public static double normalizeAngle(double angle) {
		if (angle < 0) angle = 2*Math.PI + angle;
		return angle;
	}
	
	public static void snapPolyLineTo(Polyline pl, Collection<IIdentify> list, double size) {
		
		try {
			// initialize
			double min = -1; 
			IEnvelope pSearch = null;
			int count = pl.getPointCount();
			IFeatureProxy pFeature = null;			
			IProximityOperator pOperator = null;
			IRowIdentifyObjectProxy pRowObj = null;   
			
			// create search envelope
			pSearch = new Envelope();
			pSearch.putCoords(0, 0, 0, 0);
			pSearch.setHeight(size);
			pSearch.setWidth(size);		
			
			// loop over all points
			for(int i=0;i<count;i++) {
			
				// update current
				IPoint p = pl.getPoint(i);
							
				// prepare search envelope
				pSearch.centerAt(p);
				
				// get iterator
				Iterator<IIdentify> it = list.iterator();
				
				// initialize
				min = -1;
				pOperator = null;				
				
				// loop over all
				while(it.hasNext()) {
				
					// identify height below point
					IArray arr = it.next().identify(pSearch);
								
					// found road?
					if (arr != null) {
					
						// Get the feature that was identified by casting to IRowIdentifyObject   
						pRowObj = new IRowIdentifyObjectProxy(arr.getElement(0));   
						pFeature = new IFeatureProxy(pRowObj.getRow());
						
						// get geometry
						IGeometry geom = pFeature.getShape(); 
						
						// get geometry shape
						int type = geom.getGeometryType();
						
						// has proximity operator?
						if (type == esriGeometryType.esriGeometryPoint || 
								type == esriGeometryType.esriGeometryPolyline || 
								type == esriGeometryType.esriGeometryPolygon) {
							
							// return nearest distance
							double d = ((IProximityOperator)geom).returnDistance(p);
							
							// less?
							if (min == -1 || d < min) {
								min = d;
								pOperator = (IProximityOperator)geom;							
							}							
						}
					}
				}
				
				// return nearest distance
				if (pOperator != null) {
					IPoint near = pOperator.returnNearestPoint(p, esriSegmentExtension.esriNoExtension);
					// snap to nearest point?
					if(near != null)
						pl.updatePoint(i, near);
				}
				
			}			
		}
		catch (Exception e) {
			e.printStackTrace();
		}					
	}
	
}
