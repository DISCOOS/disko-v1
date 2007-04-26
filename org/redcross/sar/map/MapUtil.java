package org.redcross.sar.map;

import java.io.IOException;

import org.redcross.sar.util.mso.Position;

import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.IGeographicCoordinateSystem;
import com.esri.arcgis.geometry.ISpatialReferenceFactory2;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.geometry.SpatialReferenceEnvironment;
import com.esri.arcgis.geometry.esriSRGeoCSType;
import com.esri.arcgis.interop.AutomationException;

public class MapUtil {
	
	private static IGeographicCoordinateSystem  geographicCS = null;
	
	public static IGeographicCoordinateSystem getGeographicCS() throws IOException, AutomationException {
    	if (geographicCS == null) {
    		ISpatialReferenceFactory2 spaRefFact = new SpatialReferenceEnvironment();
    		geographicCS = spaRefFact.createGeographicCoordinateSystem(
    				esriSRGeoCSType.esriSRGeoCS_WGS1984);
    	}
    	return geographicCS;
    }
	
	public static Point getPoint(Position pos) throws IOException, AutomationException {
		Point p = new Point();
		p.setX(pos.getPosition().getX());
		p.setY(pos.getPosition().getY());
		p.setSpatialReferenceByRef(getGeographicCS());
		return p;
	}
	
	public static Position getPosistion(Point p)  throws IOException, AutomationException {
		Point prjPoint = (Point)p.esri_clone();
		prjPoint.project(getGeographicCS());
		return new Position(null, prjPoint.getX(), prjPoint.getY());
	}
	
	public static Envelope getEnvelope(Point p, double size) throws IOException, AutomationException {
		Envelope env = new Envelope();
		double xmin = p.getX()-size/2;
		double ymin = p.getY()-size/2;
		double xmax = p.getX()+size/2;
		double ymax = p.getY()+size/2;
		env.putCoords(xmin, ymin, xmax, ymax);
		return env;
	}
}
