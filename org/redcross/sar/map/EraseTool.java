package org.redcross.sar.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import org.redcross.sar.map.feature.AreaFeatureClass;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.IMsoFeatureClass;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.util.mso.GeoCollection;

import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geometry.GeometryBag;
import com.esri.arcgis.geometry.IGeometry;
import com.esri.arcgis.geometry.Point;
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
		if (featureClasses.indexOf(fc) == -1) {
			featureClasses.add(fc);
		}
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
				if (fc instanceof AreaFeatureClass) {
					GeometryBag geomBag = (GeometryBag)geom;
					int index = getGeomIndex(geomBag, p);
					IAreaIf area = (IAreaIf)editFeature.getMsoObject();
					GeoCollection clone = cloneGeoCollection(area.getGeodata());
					((Vector)clone.getPositions()).remove(index);
					area.setGeodata(clone);
				}
				else {
					editFeature.getMsoObject().deleteObject();
				}
				map.fireEditLayerChanged();
				break;
			}
		}
	}
}
