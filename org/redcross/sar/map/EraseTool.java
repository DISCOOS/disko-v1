package org.redcross.sar.map;

import java.io.IOException;

import org.redcross.sar.map.feature.IMsoFeature;

import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geometry.GeometryBag;
import com.esri.arcgis.geometry.IGeometry;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.geometry.esriGeometryType;
import com.esri.arcgis.interop.AutomationException;

/**
 * A custom draw tool.
 * @author geira
 *
 */
public class EraseTool extends AbstractCommandTool {

	private static final long serialVersionUID = 1L;
	private Point p = null;
	
	/**
	 * Constructs the DrawTool
	 */
	public EraseTool() throws IOException {
		p = new Point();
		p.setX(0);
		p.setY(0);
	}

	public void onCreate(Object obj) throws IOException, AutomationException {
		if (obj instanceof IDiskoMap) {
			map = (DiskoMap)obj;
		}
	}

	public void onMouseDown(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		p.setX(x);
		p.setY(y); 
		transform(p);

		IFeature feature = search(p);
		if (feature != null && feature instanceof IMsoFeature) {
			editFeature = (IMsoFeature)feature;
			IGeometry geom = editFeature.getShape();
			if (featureClass.getShapeType() == esriGeometryType.esriGeometryBag) {
				GeometryBag geomBag = (GeometryBag)geom;
				editFeature.removeGeodataFromCollectionAt(getGeomIndex(geomBag, p));
			}
			else {
				editFeature.removeGeodata(null);
			}
		}
	}
}
