package org.redcross.sar.map;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.redcross.sar.app.Utils;
import org.redcross.sar.util.mso.GeoPos;
import org.redcross.sar.util.mso.Position;
import org.redcross.sar.util.mso.Route;

import com.esri.arcgis.datasourcesGDB.FileGDBWorkspaceFactory;
import com.esri.arcgis.geodatabase.IFeatureClass;
import com.esri.arcgis.geodatabase.Workspace;
import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.IEnvelope;
import com.esri.arcgis.geometry.IGeographicCoordinateSystem;
import com.esri.arcgis.geometry.IPoint;
import com.esri.arcgis.geometry.ISpatialReference;
import com.esri.arcgis.geometry.ISpatialReferenceFactory2;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.geometry.Polygon;
import com.esri.arcgis.geometry.Polyline;
import com.esri.arcgis.geometry.SpatialReferenceEnvironment;
import com.esri.arcgis.geometry.esriSRGeoCSType;
import com.esri.arcgis.interop.AutomationException;

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
}
