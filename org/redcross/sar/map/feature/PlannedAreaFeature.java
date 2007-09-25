package org.redcross.sar.map.feature;

import com.esri.arcgis.geometry.GeometryBag;
import com.esri.arcgis.geometry.IPolyline;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.geometry.Polyline;
import com.esri.arcgis.interop.AutomationException;
import org.redcross.sar.map.MapUtil;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.*;
import org.redcross.sar.mso.data.IPOIIf.POIType;
import org.redcross.sar.util.mso.IGeodataIf;
import org.redcross.sar.util.mso.Route;

import java.io.IOException;
import java.util.Iterator;

public class PlannedAreaFeature extends AbstractMsoFeature {

	private static final long serialVersionUID = 1L;
    private IMsoListIf<IMsoObjectIf>  geoList = null;
    private IMsoModelIf msoModel = null;

	public PlannedAreaFeature(IMsoModelIf msoModel) {
		this.msoModel = msoModel;
	}

	public boolean geometryIsChanged(IMsoObjectIf msoObj) {
		IAreaIf area = (IAreaIf)msoObject;
        return area.getAreaGeodata() != null && !area.getAreaGeodata().equals(getGeodata());
	}

	@Override
    public void msoGeometryChanged() throws IOException, AutomationException {       // todo sjekk etter endring av GeoCollection
		if (srs == null) return;
		IAreaIf area = (IAreaIf)msoObject;
        geoList = area.getAreaGeodata().getClone();
        if (geoList != null && geoList.size() > 0) {
			GeometryBag geomBag = new GeometryBag();
            Iterator<IGeodataIf> iter = area.getAreaGeodataIterator();
            while (iter.hasNext()) {
				IGeodataIf geodata = iter.next();
				if (geodata instanceof Route) {
					Polyline polyline = MapUtil.getEsriPolyline((Route)geodata, srs);
					geomBag.addGeometry(polyline, null, null);
				}
			}
			geometry = geomBag;
			updateAreaPOIs();
		}
		else {
			geometry = null;
		}
	}

	public Object getGeodata() {
		return geoList;
	}

	public int getGeodataCount() {
        return geoList != null ? geoList.size() : 0;

    }

	public void updateAreaPOIs() throws IOException, AutomationException {
		IAreaIf area = (IAreaIf)msoObject;
		GeometryBag geomBag = (GeometryBag)getShape();
		if (geomBag == null || geomBag.getGeometryCount() == 0) {
			return;
		}
		IPolyline startPline = (IPolyline)geomBag.getGeometry(0);
		IPolyline stopPline  = (IPolyline)geomBag.getGeometry(
				geomBag.getGeometryCount()-1);
		Point startPoint = (Point)startPline.getFromPoint();
		startPoint.setSpatialReferenceByRef(getSpatialReference());
		Point stopPoint  = (Point)stopPline.getToPoint();
		stopPoint.setSpatialReferenceByRef(getSpatialReference());
		addPOI(area, startPoint, POIType.START);
		addPOI(area, stopPoint, POIType.STOP);
	}

	private void addPOI(IAreaIf area, Point point, POIType poiType)
			throws IOException, AutomationException {
		IPOIIf poi = getPOI(area, poiType);
		if (poi == null) {
			// create new
			ICmdPostIf cmdPost = msoModel.getMsoManager().getCmdPost();
			IPOIListIf poiList = cmdPost.getPOIList();
			poi = poiList.createPOI();
			poi.setPosition(MapUtil.getMsoPosistion(point));
			poi.setType(poiType);
			area.addAreaPOI(poi);
		} else {
			// update position
			poi.setPosition(MapUtil.getMsoPosistion(point));
		}
	}

	private IPOIIf getPOI(IAreaIf area, POIType poiType) {
		Iterator iter = area.getAreaPOIs().getItems().iterator();
		while (iter.hasNext()) {
			IPOIIf poi = (IPOIIf)iter.next();
			if (poi.getType() == poiType) {
				return poi;
			}
		}
		return null;
	}
}
