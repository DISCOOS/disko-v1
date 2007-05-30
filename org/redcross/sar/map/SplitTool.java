package org.redcross.sar.map;

import java.awt.Toolkit;
import java.io.IOException;

import org.redcross.sar.map.feature.AreaFeature;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.util.mso.GeoCollection;
import org.redcross.sar.util.mso.Route;

import com.esri.arcgis.carto.IElement;
import com.esri.arcgis.carto.LineElement;
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
		
		IFeature feature = search(featureClass, p);
		if (feature != null && feature instanceof IMsoFeature) {
			editFeature = (IMsoFeature)feature;
			IGeometry geom = editFeature.getShape();
			if (featureClass.getShapeType() == esriGeometryType.esriGeometryBag) {
				GeometryBag geomBag = (GeometryBag)geom;
				//editFeature.removeGeodataFromCollectionAt(getGeomIndex(geomBag, p));
			}
			else {
				//editFeature.removeGeodata(null);
				//editFeature.delete();
			}
			map.partialRefresh(null);
		}
		
		/*IElement elem = map.searchGraphics(p);
		if (elem != null && elem instanceof LineElement) {
			Polyline pl = (Polyline)elem.getGeometry();
			// splitting and adding new elements
			split(pl, p);
			// delete the orginal element
			map.deleteGraphics(elem);
		}*/
	}
	
	private void split(Polyline orginal, Point nearPoint) 
			throws IOException, AutomationException {
		
		boolean[] splitHappened = new boolean[2];
		int[] newPartIndex = new int[2];
		int[] newSegmentIndex = new int[2];
		orginal.splitAtPoint(nearPoint, true, true, splitHappened, 
				newPartIndex, newSegmentIndex);
		// two new polylines
		Polyline pline1 = new Polyline();
		pline1.addGeometry(orginal.getGeometry(newPartIndex[0]), null, null);
		Polyline pline2 = new Polyline();
		pline2.addGeometry(orginal.getGeometry(newPartIndex[1]), null, null);
		
		LineElement le1 = new LineElement();
		le1.setGeometry(pline1);
		//map.addGraphics(le1);
		
		LineElement le2 = new LineElement();
		le2.setGeometry(pline2);
		//map.addGraphics(le2);
		
		Toolkit.getDefaultToolkit().beep();
	}
}
