package org.redcross.sar.map.feature;

import java.io.IOException;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.mso.data.IPOIListIf;
import org.redcross.sar.mso.event.MsoEvent.EventType;
import org.redcross.sar.mso.event.MsoEvent.Update;

import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geometry.esriGeometryType;
import com.esri.arcgis.interop.AutomationException;

public class POIFeatureClass extends AbstractMsoFeatureClass {

	private static final long serialVersionUID = 1L;
	
	public POIFeatureClass(IMsoManagerIf.MsoClassCode classCode, IMsoModelIf msoModel) {
		super(classCode, msoModel);
	}
	
	public void handleMsoUpdateEvent(Update e) {
		try {
			int type = e.getEventTypeMask();
			IPOIIf poi = (IPOIIf)e.getSource();
			IMsoFeature msoFeature = getFeature(poi.getObjectId());
			IMsoModelIf.ModificationState geodataState = poi.getPositionState();
			System.out.println(classCode.name()+" ModificationState: "+geodataState.name());
			
			if (type == EventType.ADDED_REFERENCE_EVENT.maskValue() &&
					geodataState == IMsoModelIf.ModificationState.STATE_SERVER_MODIFIED) {
				System.out.println(classCode.name()+" .... created");
				createFeature(poi);
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
		IPOIListIf poiList = cmdPost.getPOIList();
		return createFeature(poiList.createPOI());
	}
	
	@SuppressWarnings("unchecked")
	private POIFeature createFeature(IMsoObjectIf obj) throws AutomationException, IOException {
		POIFeature feature = new POIFeature();
		feature.setSpatialReference(srs);
		feature.setMsoObject(obj);
		data.add(feature);
		return feature;
 	}

	public int getShapeType() throws IOException, AutomationException {
		return esriGeometryType.esriGeometryPoint;
	}
}
