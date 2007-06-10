package org.redcross.sar.map;

import java.io.IOException;
import java.util.ArrayList;

import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.IMsoFeatureClass;

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
	private ArrayList<IMsoFeatureClass> featureClasses = null;
	
	/**
	 * Constructs the DrawTool
	 */
	public EraseTool() throws IOException {
		p = new Point();
		p.setX(0);
		p.setY(0);
		featureClasses = new ArrayList<IMsoFeatureClass>();
	}

	public void onCreate(Object obj) throws IOException, AutomationException {
		if (obj instanceof IDiskoMap) {
			map = (DiskoMap)obj;
		}
	}
	
	public void addFeatureClass(IMsoFeatureClass fc) {
		featureClasses.add(fc);
	}
	
	public void removeAll() {
		featureClasses.clear();
	}

	public void onMouseDown(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		p.setX(x);
		p.setY(y); 
		transform(p);

		for (int i = 0; i < featureClasses.size(); i++) {
			IMsoFeatureClass fc = (IMsoFeatureClass)featureClasses.get(i);
			IFeature feature = search(fc, p);
			if (feature != null && feature instanceof IMsoFeature) {
				editFeature = (IMsoFeature)feature;
				IGeometry geom = editFeature.getShape();
				if (fc.getShapeType() == esriGeometryType.esriGeometryBag) {
					GeometryBag geomBag = (GeometryBag)geom;
					editFeature.removeGeodataAt(getGeomIndex(geomBag, p));
				}
				else {
					//editFeature.removeGeodata(null);
					editFeature.delete();
				}
				map.partialRefresh(null);
				break;
			}
		}
	}
}
