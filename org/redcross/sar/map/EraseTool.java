package org.redcross.sar.map;

import java.io.IOException;
import java.util.List;

import com.esri.arcgis.carto.IFeatureLayer;
import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geodatabase.IFeatureClass;
import com.esri.arcgis.geodatabase.IFeatureCursor;
import com.esri.arcgis.geodatabase.ISpatialFilter;
import com.esri.arcgis.geodatabase.SpatialFilter;
import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.GeometryBag;
import com.esri.arcgis.geometry.IEnvelope;
import com.esri.arcgis.geometry.IGeometry;
import com.esri.arcgis.geometry.IRelationalOperator;
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
	private Envelope env = null;
	
	/**
	 * Constructs the DrawTool
	 */
	public EraseTool() throws IOException {
		p = new Point();
		p.setX(0);
		p.setY(0);
		env = new Envelope();
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
		
		double size = map.getActiveView().getExtent().getWidth()/50;
		double xmin = p.getX()-size/2;
		double ymin = p.getY()-size/2;
		double xmax = p.getX()+size/2;
		double ymax = p.getY()+size/2;
		env.putCoords(xmin, ymin, xmax, ymax);
		
		IEnvelope refreshEnvelope = null;
		ISpatialFilter filter = new SpatialFilter();
		filter.setGeometryByRef(env);
		List layers = map.getMapManager().getMsoLayers();
		for (int i = 0; i < layers.size(); i++) {
			IFeatureClass fc = ((IFeatureLayer)layers.get(i)).getFeatureClass();
			IFeatureCursor cursor = fc.search(filter, false);
			IFeature feature = cursor.nextFeature();
			if (feature != null) {
				IGeometry geom = feature.getShape();
				refreshEnvelope = geom.getEnvelope();
				if (geom instanceof GeometryBag) {
					GeometryBag geomBag = (GeometryBag)geom;
					for (int j = 0; j < geomBag.getGeometryCount(); j++) {
						IRelationalOperator relOp = (IRelationalOperator)geomBag.getGeometry(j);
						if (!relOp.disjoint(env)) {
							geomBag.removeGeometries(j, 1);
						}
					}
				}
				else {
					refreshEnvelope = geom instanceof Point ? env : geom.getEnvelope();
					feature.setShapeByRef(null);
				}
				break;
			}
		}
		if (refreshEnvelope != null) {
			map.getActiveView().partialRefresh(
					com.esri.arcgis.carto.esriViewDrawPhase.esriViewGeography, 
					null, refreshEnvelope);
		}
	}
}
