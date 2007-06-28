package org.redcross.sar.map.feature;

import java.io.IOException;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.IOperationAreaIf;
import org.redcross.sar.mso.event.MsoEvent.EventType;
import org.redcross.sar.mso.event.MsoEvent.Update;

import com.esri.arcgis.geometry.esriGeometryType;
import com.esri.arcgis.interop.AutomationException;

public class OperationAreaMaskFeatureClass extends AbstractMsoFeatureClass {

	private static final long serialVersionUID = 1L;
	
	public OperationAreaMaskFeatureClass(IMsoManagerIf.MsoClassCode classCode, IMsoModelIf msoModel) {
		super(classCode, msoModel);
	}
	
	public void handleMsoUpdateEvent(Update e) {
		try {
			int type = e.getEventTypeMask();
			IOperationAreaIf opArea = (IOperationAreaIf)e.getSource();
			IMsoFeature msoFeature = getFeature(opArea.getObjectId());
			
			if (type == EventType.ADDED_REFERENCE_EVENT.maskValue()) {
				msoFeature = new OperationAreaMaskFeature();
				msoFeature.setSpatialReference(srs);
				msoFeature.setMsoObject(opArea);
				data.add(msoFeature);
			}
			else if (type == EventType.MODIFIED_DATA_EVENT.maskValue() && 
					msoFeature != null && 
					!opArea.getGeodata().equals(msoFeature.getGeodata())) {
				msoFeature.msoGeometryChanged();
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

	public int getShapeType() throws IOException, AutomationException {
		return esriGeometryType.esriGeometryBag;
	}
}
