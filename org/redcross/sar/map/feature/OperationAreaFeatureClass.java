package org.redcross.sar.map.feature;

import java.io.IOException;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IOperationAreaIf;
import org.redcross.sar.mso.data.IOperationAreaListIf;
import org.redcross.sar.mso.event.MsoEvent.EventType;
import org.redcross.sar.mso.event.MsoEvent.Update;

import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geometry.esriGeometryType;
import com.esri.arcgis.interop.AutomationException;

public class OperationAreaFeatureClass extends AbstractMsoFeatureClass {

	private static final long serialVersionUID = 1L;
	
	public OperationAreaFeatureClass(IMsoManagerIf.MsoClassCode classCode, IMsoModelIf msoModel) {
		super(classCode, msoModel);
	}
	
	public void handleMsoUpdateEvent(Update e) {
		try {
			int type = e.getEventTypeMask();
			IOperationAreaIf opArea = (IOperationAreaIf)e.getSource();
			IMsoFeature msoFeature = getFeature(opArea.getObjectId());
			IMsoModelIf.ModificationState geodataState = opArea.getGeodataState();
			System.out.println(classCode.name()+" ModificationState: "+geodataState.name());
			
			if (type == EventType.ADDED_REFERENCE_EVENT.maskValue() &&
					geodataState == IMsoModelIf.ModificationState.STATE_SERVER_MODIFIED) {
				System.out.println(classCode.name()+" .... created");
				createFeature(opArea);
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
	
	@SuppressWarnings("unchecked")
	private OperationAreaFeature createFeature(IMsoObjectIf obj) throws AutomationException, IOException {
		OperationAreaFeature feature = new OperationAreaFeature();
		feature.setSpatialReference(srs);
		feature.setMsoObject(obj);
		data.add(feature);
		return feature;
 	}

	public IFeature createFeature() throws IOException, AutomationException {
		ICmdPostIf cmdPost = msoModel.getMsoManager().getCmdPost();
		IOperationAreaListIf opAreaList = cmdPost.getOperationAreaList();
		return createFeature(opAreaList.createOperationArea());
	}

	public int getShapeType() throws IOException, AutomationException {
		return esriGeometryType.esriGeometryPolygon;
	}
}
