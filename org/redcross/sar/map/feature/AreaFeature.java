package org.redcross.sar.map.feature;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import org.redcross.sar.map.MapUtil;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.util.mso.GeoCollection;
import org.redcross.sar.util.mso.IGeodataIf;
import org.redcross.sar.util.mso.Route;

import com.esri.arcgis.geometry.GeometryBag;
import com.esri.arcgis.geometry.Polyline;
import com.esri.arcgis.interop.AutomationException;

public class AreaFeature extends AbstractMsoFeature {

	private static final long serialVersionUID = 1L;

	@Override
	public void msoGeometryChanged() throws IOException, AutomationException {
		IAreaIf area = (IAreaIf)msoObject;
		GeoCollection geoColl = area.getGeodata();
		if (geoColl != null) {
			GeometryBag geomBag = new GeometryBag();
			Iterator iter = geoColl.getPositions().iterator();
			while (iter.hasNext()) {
				IGeodataIf geodata = (IGeodataIf) iter.next();
				if (geodata instanceof Route) {
					Polyline polyline = MapUtil.getEsriPolyline((Route)geodata, srs);
					geomBag.addGeometry(polyline, null, null);
				}
			}
			geometry = geomBag;
		}
		else {
			geometry = null;
		}
	}
	
	public void addGeodataToCollection(IGeodataIf geodata) {
		IAreaIf area = (IAreaIf)msoObject;
		GeoCollection clone = clone(area.getGeodata());
		clone.add(geodata);
		area.setGeodata(clone);
	}
	
	public void removeGeodataFromCollectionAt(int index) {
		IAreaIf area = (IAreaIf)msoObject;
		GeoCollection clone = clone(area.getGeodata());
		((Vector)clone.getPositions()).remove(index);
		area.setGeodata(clone);
	}
	
	private GeoCollection clone(GeoCollection oldColl) {
		GeoCollection newColl = new GeoCollection(null);
		if (oldColl != null) {
			Iterator iter = oldColl.getPositions().iterator();
			while (iter.hasNext()) {
				newColl.add((IGeodataIf)iter.next());
			}
		}
		return newColl;
	}
}
