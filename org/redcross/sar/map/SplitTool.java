package org.redcross.sar.map;

import java.awt.Toolkit;
import java.io.IOException;
import java.util.Vector;

import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.IMsoFeatureClass;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.util.mso.GeoCollection;
import org.redcross.sar.util.mso.IGeodataIf;

import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geometry.GeometryBag;
import com.esri.arcgis.geometry.IGeometry;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.geometry.Polyline;
import com.esri.arcgis.geometry.esriGeometryType;
import com.esri.arcgis.interop.AutomationException;

/**
 * A custom draw tool.
 * @author geira
 *
 */
public class SplitTool extends AbstractTool {

	private static final long serialVersionUID = 1L;
	private Point p = null;
	
	/**
	 * Constructs the DrawTool
	 */
	public SplitTool() throws IOException {
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

		IMsoFeatureClass featureClass = (IMsoFeatureClass)editLayer.getFeatureClass();
		IFeature feature = search(featureClass, p);
		if (feature != null && feature instanceof IMsoFeature) {
			editFeature = (IMsoFeature)feature;
			IGeometry geom = editFeature.getShape();
			if (featureClass.getShapeType() == esriGeometryType.esriGeometryBag) {
				GeometryBag geomBag = (GeometryBag)geom;
				int index = getGeomIndex(geomBag, p);
				if (index > -1) {
					IGeometry subGeom = geomBag.getGeometry(index);
					if (subGeom instanceof Polyline) {
						Polyline[] result = split((Polyline)subGeom, p); 
						IAreaIf area = (IAreaIf)editFeature.getMsoObject();
						GeoCollection clone = cloneGeoCollection(area.getGeodata());
						((Vector<IGeodataIf>)clone.getPositions()).set(index, 
								MapUtil.getMsoRoute(result[0]));
						clone.add(MapUtil.getMsoRoute(result[1]));
						area.setGeodata(clone);
						map.fireEditLayerChanged();
					}
				}
			}
			else {
				//TODO:
			}
		}
	}
	
	private Polyline[] split(Polyline orginal, Point nearPoint) 
			throws IOException, AutomationException {
		Polyline[] result = new Polyline[2];
		boolean[] splitHappened = new boolean[2];
		int[] newPartIndex = new int[2];
		int[] newSegmentIndex = new int[2];
		orginal.splitAtPoint(nearPoint, true, true, splitHappened, 
				newPartIndex, newSegmentIndex);
		
		// two new polylines
		result[0] = new Polyline();
		result[0].addGeometry(orginal.getGeometry(newPartIndex[0]), null, null);
		result[0].setSpatialReferenceByRef(map.getSpatialReference());
		result[1] = new Polyline();
		result[1].addGeometry(orginal.getGeometry(newPartIndex[1]), null, null);
		result[1].setSpatialReferenceByRef(map.getSpatialReference());
		
		Toolkit.getDefaultToolkit().beep();
		return result;
	}
}
