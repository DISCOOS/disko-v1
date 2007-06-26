package org.redcross.sar.map.feature;

import java.io.IOException;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.mso.data.IAreaListIf;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.event.MsoEvent.EventType;
import org.redcross.sar.mso.event.MsoEvent.Update;

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
			
			if (type == EventType.ADDED_REFERENCE_EVENT.maskValue()) {
				createFeature(area);
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
	
	public String createMsoObject() {
		ICmdPostIf cmdPost = msoModel.getMsoManager().getCmdPost();
		IAreaListIf areaList = cmdPost.getAreaList();
		IAreaIf area = areaList.createArea();
		return area.getObjectId();
	}
	
	@SuppressWarnings("unchecked")
	private AreaFeature createFeature(IMsoObjectIf obj) throws AutomationException, IOException {
		AreaFeature feature = new AreaFeature();
		feature.setSpatialReference(srs);
		feature.setMsoObject(obj);
		data.add(feature);
		return feature;
 	}

	public int getShapeType() throws IOException, AutomationException {
		return esriGeometryType.esriGeometryBag;
	}
}
