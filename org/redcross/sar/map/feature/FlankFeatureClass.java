package org.redcross.sar.map.feature;

import java.io.IOException;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
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
			if (type == EventType.ADDED_REFERENCE_EVENT.maskValue()) {
				createFeature(area);
			}
			else if (type == EventType.MODIFIED_DATA_EVENT.maskValue() && msoFeature != null) {
				msoFeature.msoGeometryChanged();
			}
			else if (type == EventType.DELETED_OBJECT_EVENT.maskValue() && msoFeature != null) {
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
	private FlankFeature createFeature(IMsoObjectIf obj) throws AutomationException, IOException {
		FlankFeature feature = new FlankFeature();
		feature.setSpatialReference(srs);
		feature.setMsoObject(obj);
		data.add(feature);
		return feature;
 	}

	public int getShapeType() throws IOException, AutomationException {
		return esriGeometryType.esriGeometryBag;
	}
}
