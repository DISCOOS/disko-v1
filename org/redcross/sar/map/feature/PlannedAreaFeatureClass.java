package org.redcross.sar.map.feature;

import java.io.IOException;
import java.util.Iterator;

import org.redcross.sar.map.MapUtil;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.mso.data.IAreaListIf;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.mso.data.IPOIListIf;
import org.redcross.sar.mso.data.IPOIIf.POIType;
import org.redcross.sar.mso.event.MsoEvent.EventType;
import org.redcross.sar.mso.event.MsoEvent.Update;
import org.redcross.sar.util.mso.GeoCollection;

import com.esri.arcgis.geometry.GeometryBag;
import com.esri.arcgis.geometry.IPolyline;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.geometry.esriGeometryType;
import com.esri.arcgis.interop.AutomationException;

public class PlannedAreaFeatureClass extends AbstractMsoFeatureClass {

	private static final long serialVersionUID = 1L;
	
	public PlannedAreaFeatureClass(IMsoManagerIf.MsoClassCode classCode, IMsoModelIf msoModel) {
		super(classCode, msoModel);
	}
	
	public void handleMsoUpdateEvent(Update e) {
		try {
			int type = e.getEventTypeMask();
			IAreaIf area = (IAreaIf)e.getSource();
			IMsoFeature msoFeature = getFeature(area.getObjectId());
			
			if (type == EventType.ADDED_REFERENCE_EVENT.maskValue() && 
					msoFeature == null) {
				msoFeature = new PlannedAreaFeature();
				msoFeature.setSpatialReference(srs);
				msoFeature.setMsoObject(area);
				data.add(msoFeature);
			}
			else if (type == EventType.MODIFIED_DATA_EVENT.maskValue() && 
					msoFeature != null) {
				if (area.getGeodata() != null &&
						!area.getGeodata().equals(msoFeature.getGeodata())) {
					msoFeature.msoGeometryChanged();
				}
				isDirty = true;
			}
			else if (type == EventType.DELETED_OBJECT_EVENT.maskValue() && 
					msoFeature != null) {
				removeFeature(msoFeature);
				isDirty = true;
			}
		} catch (AutomationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public IMsoFeature createMsoFeature() {
		ICmdPostIf cmdPost = msoModel.getMsoManager().getCmdPost();
		IAreaListIf areaList = cmdPost.getAreaList();
		IAreaIf area = areaList.createArea();
		area.setGeodata(new GeoCollection(null));
		return getFeature(area.getObjectId());
	}

	public int getShapeType() throws IOException, AutomationException {
		return esriGeometryType.esriGeometryBag;
	}
	
	public void deleteAreaPOIs(IAreaIf area) throws IOException, AutomationException {
		IMsoFeature msoFeature = getFeature(area.getObjectId());
		if (msoFeature == null) {
			return;
		}
		Iterator iter = area.getAreaPOIs().getItems().iterator();
		while (iter.hasNext()) {
			IPOIIf poi = (IPOIIf)iter.next();
			poi.deleteObject();
		}
	}
	
	public void updateAreaPOIs(IAreaIf area) throws IOException, AutomationException {
		//adding start and stop POI's
		IMsoFeature msoFeature = getFeature(area.getObjectId());
		if (msoFeature == null) {
			return;
		}
		GeometryBag geomBag = (GeometryBag)msoFeature.getShape();
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
