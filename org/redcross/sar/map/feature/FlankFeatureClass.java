package org.redcross.sar.map.feature;

import java.io.IOException;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.mso.event.MsoEvent.EventType;
import org.redcross.sar.mso.event.MsoEvent.Update;

import com.esri.arcgis.geometry.esriGeometryType;
import com.esri.arcgis.interop.AutomationException;

public class FlankFeatureClass extends AbstractMsoFeatureClass {

	private static final long serialVersionUID = 1L;
	
	public FlankFeatureClass(IMsoManagerIf.MsoClassCode classCode, IMsoModelIf msoModel) {
		super(classCode, msoModel);
	}
	
	public void handleMsoUpdateEvent(Update e) {
		try {
			int type = e.getEventTypeMask();
			IAreaIf area = (IAreaIf)e.getSource();
			IMsoFeature msoFeature = getFeature(area.getObjectId());
			if (type == EventType.ADDED_REFERENCE_EVENT.maskValue() && 
					msoFeature == null) {
				msoFeature = new FlankFeature();
				msoFeature.setSpatialReference(srs);
				msoFeature.setMsoObject(area);
				data.add(msoFeature);
			}
			else if (type == EventType.MODIFIED_DATA_EVENT.maskValue() && 
					msoFeature != null) {
				msoFeature.msoGeometryChanged();
				isDirty = true;
			}
			else if (type == EventType.DELETED_OBJECT_EVENT.maskValue() && 
					msoFeature != null) {
				msoFeature.setShapeByRef(null);
				msoFeature.setMsoObject(null);
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
