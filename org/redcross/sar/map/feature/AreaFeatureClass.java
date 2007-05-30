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

import com.esri.arcgis.geodatabase.IFeature;
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
			IMsoModelIf.ModificationState geodataState = area.getGeodataState();
			
			System.out.println(classCode.name()+" ModificationState: "+geodataState.name());
			
			if (type == EventType.ADDED_REFERENCE_EVENT.maskValue() &&
					geodataState == IMsoModelIf.ModificationState.STATE_SERVER_MODIFIED) {
				System.out.println(classCode.name()+" .... created");
				createFeature(area);
			}
			else if (type == EventType.MODIFIED_DATA_EVENT.maskValue() && msoFeature != null) {
				System.out.println(classCode.name()+" .... modified");
				msoFeature.msoGeometryChanged();
			}
			else if (type == EventType.DELETED_OBJECT_EVENT.maskValue() && msoFeature != null) {
				System.out.println(classCode.name()+" .... deleted");
				removeFeature(msoFeature);
			}
		} catch (AutomationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public IFeature createFeature() throws IOException, AutomationException {
		ICmdPostIf cmdPost = msoModel.getMsoManager().getCmdPost();
		IAreaListIf areaList = cmdPost.getAreaList();
		return createFeature(areaList.createArea());
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
