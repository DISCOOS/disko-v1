package org.redcross.sar.map.feature;

import java.io.IOException;

import org.redcross.sar.map.MapUtil;
import org.redcross.sar.mso.data.IOperationAreaIf;

import com.esri.arcgis.interop.AutomationException;

public class OperationAreaMaskFeature extends AbstractMsoFeature {

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
	
	public Object getGeodata() {
		return polygon;
	}
}
