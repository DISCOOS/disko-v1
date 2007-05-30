package org.redcross.sar.map.feature;

import java.io.IOException;

import org.redcross.sar.map.MapUtil;
import org.redcross.sar.mso.data.ISearchAreaIf;
import org.redcross.sar.util.mso.IGeodataIf;

import com.esri.arcgis.interop.AutomationException;

public class SearchAreaFeature extends AbstractMsoFeature {

	
	private static final long serialVersionUID = 1L;

	@Override
	public void msoGeometryChanged() throws IOException, AutomationException {
		ISearchAreaIf searchArea = (ISearchAreaIf)msoObject;
		org.redcross.sar.util.mso.Polygon msoPolygon = searchArea.getGeodata();
		if (msoPolygon != null) {
			geometry = MapUtil.getEsriPolygon(msoPolygon, srs);
		}
		else {
			geometry = null;
		}
	}
	
	public void setGeodata(IGeodataIf geodata) {
		if (geodata instanceof org.redcross.sar.util.mso.Polygon) {
			ISearchAreaIf searchArea = (ISearchAreaIf)msoObject;
			searchArea.setGeodata((org.redcross.sar.util.mso.Polygon)geodata);
		}
	}
	
	public void removeGeodata(IGeodataIf geodata) {
		ISearchAreaIf searchArea = (ISearchAreaIf)msoObject;
		searchArea.setGeodata(null);
	}
}
