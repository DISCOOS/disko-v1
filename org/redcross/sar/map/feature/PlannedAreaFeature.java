package org.redcross.sar.map.feature;

import java.io.IOException;
import java.util.Iterator;

import org.redcross.sar.map.MapUtil;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.util.mso.GeoCollection;
import org.redcross.sar.util.mso.IGeodataIf;
import org.redcross.sar.util.mso.Route;

import com.esri.arcgis.geometry.GeometryBag;
import com.esri.arcgis.geometry.Polyline;
import com.esri.arcgis.interop.AutomationException;

public class PlannedAreaFeature extends AbstractMsoFeature {

	private static final long serialVersionUID = 1L;
	private GeoCollection geoColl = null;

	@Override
	public void msoGeometryChanged() throws IOException, AutomationException {
		IAreaIf area = (IAreaIf)msoObject;
		geoColl = area.getGeodata();
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
	
	public Object getGeodata() {
		return geoColl;
	}
	
	public int getGeodataCount() {
		return geoColl != null ? geoColl.getPositions().size() : 0;
	}
}
