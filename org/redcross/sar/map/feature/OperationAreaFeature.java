package org.redcross.sar.map.feature;

import java.io.IOException;

import org.redcross.sar.map.MapUtil;
import org.redcross.sar.mso.data.IOperationAreaIf;
import org.redcross.sar.util.mso.IGeodataIf;

import com.esri.arcgis.interop.AutomationException;

public class OperationAreaFeature extends AbstractMsoFeature {

	private static final long serialVersionUID = 1L;
	private org.redcross.sar.util.mso.Polygon polygon = null;

	public void msoGeometryChanged() throws IOException, AutomationException {
		IOperationAreaIf opArea = (IOperationAreaIf)msoObject;
		polygon = opArea.getGeodata();
		if (polygon != null) {
			geometry = MapUtil.getEsriPolygon(polygon, srs);
		}
		else {
			geometry = null;
		}
	}
	
	public void setGeodata(IGeodataIf geodata) {
		if (geodata instanceof org.redcross.sar.util.mso.Polygon) {
			IOperationAreaIf opArea = (IOperationAreaIf)msoObject;
			opArea.setGeodata((org.redcross.sar.util.mso.Polygon)geodata);
		}
	}
	
	public void removeGeodata(IGeodataIf geodata) {
		IOperationAreaIf opArea = (IOperationAreaIf)msoObject;
		opArea.setGeodata(null);
	}
	
	public int getGeodataCount() {
		return polygon != null ? 1: 0;
	}
	
	public Object getGeodata() {
		return polygon;
	}
}
