package org.redcross.sar.map.feature;

import java.io.IOException;

import org.redcross.sar.map.MapUtil;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.util.mso.IGeodataIf;
import org.redcross.sar.util.mso.Position;

import com.esri.arcgis.interop.AutomationException;

public class POIFeature extends AbstractMsoFeature {

	private static final long serialVersionUID = 1L;
	private Position pos = null;

	@Override
	public void msoGeometryChanged() throws IOException, AutomationException {
		IPOIIf poi = (IPOIIf)msoObject;
		pos = poi.getPosition();
		if (pos != null) {
			geometry = MapUtil.getEsriPoint(pos, srs);
		}
		else {
			geometry = null;
		}
	}
	
	public void setGeodata(IGeodataIf geodata) {
		if (geodata instanceof Position) {
			IPOIIf poi = (IPOIIf)msoObject;
			poi.setPosition((Position)geodata);
		}
	}
	
	public void removeGeodata(IGeodataIf geodata) {
		IPOIIf poi = (IPOIIf)msoObject;
		poi.setPosition(null);
	}
	
	public int getGeodataCount() {
		return pos != null ? 1: 0;
	}
	
	public Object getGeodata() {
		return pos;
	}
}
