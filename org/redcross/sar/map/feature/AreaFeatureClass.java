package org.redcross.sar.map.feature;

import java.io.IOException;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.mso.data.IAreaListIf;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.event.MsoEvent.EventType;
import org.redcross.sar.mso.event.MsoEvent.Update;
import org.redcross.sar.util.mso.GeoCollection;

import com.esri.arcgis.geometry.esriGeometryType;
import com.esri.arcgis.interop.AutomationException;

public class AreaFeatureClass extends AbstractMsoFeatureClass {

	private static final long serialVersionUID = 1L;
	
	public AreaFeatureClass(IMsoManagerIf.MsoClassCode classCode, IMsoModelIf msoModel) {
		super(classCode, msoModel);
	}
	
	public void handleMsoUpdateEvent(Update e) {
		try {
			int type = e.getEventTypeMask();
			IAreaIf area = (IAreaIf)e.getSource();
			IMsoFeature msoFeature = getFeature(area.getObjectId());
			
			if (type == EventType.CREATED_OBJECT_EVENT.maskValue()) {
				msoFeature = new AreaFeature();
				msoFeature.setSpatialReference(srs);
				msoFeature.setMsoObject(area);
				data.add(msoFeature);
			}
			else if (type == EventType.MODIFIED_DATA_EVENT.maskValue() && msoFeature != null &&
					!area.getGeodata().equals(msoFeature.getGeodata())) {
				msoFeature.msoGeometryChanged();
				isDirty = true;
			}
			else if (type == EventType.DELETED_OBJECT_EVENT.maskValue() && msoFeature != null) {
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
}
