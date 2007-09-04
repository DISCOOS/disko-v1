package org.redcross.sar.map;

import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geometry.*;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.interop.AutomationException;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.MsoFeatureClass;
import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.mso.MsoModelImpl;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.IRouteIf;

import java.awt.*;
import java.io.IOException;

/**
 * A custom draw tool.
 * @author geira
 *
 */
public class SplitTool extends AbstractCommandTool {

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

		IMsoFeatureLayer editLayer = map.getMapManager().getMsoLayer(IMsoFeatureLayer.LayerCode.AREA_LAYER);
		MsoFeatureClass featureClass = (MsoFeatureClass)editLayer.getFeatureClass();
		IFeature feature = search(featureClass, p);
		if (feature != null && feature instanceof IMsoFeature) {
			IMsoFeature editFeature = (IMsoFeature)feature;
			IGeometry geom = editFeature.getShape();
			if (featureClass.getShapeType() == esriGeometryType.esriGeometryBag) {
				GeometryBag geomBag = (GeometryBag)geom;
				int index = getGeomIndex(geomBag, p);
				if (index > -1) {
					IGeometry subGeom = geomBag.getGeometry(index);
					if (subGeom instanceof Polyline) {
						Polyline[] result = split((Polyline)subGeom, p);
						IAreaIf area = (IAreaIf)editFeature.getMsoObject();
//						GeoList clone = cloneGeoList(area.getGeodata());
//						((Vector<IGeodataIf>)clone.getPositions()).set(index,
//								MapUtil.getMsoRoute(result[0]));
//						clone.add(MapUtil.getMsoRoute(result[1]));
//						area.setGeodata(clone);
                        ICmdPostIf cmdPost = MsoModelImpl.getInstance().getMsoManager().getCmdPost();      // todo sjekk etter endring av GeoCollection
                        IRouteIf route = cmdPost.getRouteList().createRoute(MapUtil.getMsoRoute(result[0]));
                        area.setAreaGeodataItem(index,route);
                        route = cmdPost.getRouteList().createRoute(MapUtil.getMsoRoute(result[1]));
                        area.addAreaGeodata(route);
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
