package org.redcross.sar.map.feature;

import java.io.IOException;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.ISearchAreaIf;
import org.redcross.sar.mso.data.ISearchAreaListIf;
import org.redcross.sar.mso.event.MsoEvent.EventType;
import org.redcross.sar.mso.event.MsoEvent.Update;

import com.esri.arcgis.geometry.esriGeometryType;
import com.esri.arcgis.interop.AutomationException;

public class SearchAreaFeatureClass extends AbstractMsoFeatureClass {

	private static final long serialVersionUID = 1L;
	
	public SearchAreaFeatureClass(IMsoManagerIf.MsoClassCode classCode, IMsoModelIf msoModel) {
		super(classCode, msoModel);
	}
	
	public void handleMsoUpdateEvent(Update e) {
		try {
			int type = e.getEventTypeMask();
			ISearchAreaIf searchArea = (ISearchAreaIf)e.getSource();
			IMsoFeature msoFeature = getFeature(searchArea.getObjectId());
			
			if (type == EventType.ADDED_REFERENCE_EVENT.maskValue() && 
					msoFeature == null) {
				msoFeature = new SearchAreaFeature();
				msoFeature.setSpatialReference(srs);
				msoFeature.setMsoObject(searchArea);
				data.add(msoFeature);
			}
			else if (type == EventType.MODIFIED_DATA_EVENT.maskValue() &&
					msoFeature != null) {
				if (searchArea.getGeodata() != null &&
						!searchArea.getGeodata().equals(msoFeature.getGeodata())) {
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
		ISearchAreaListIf searchAreaList = cmdPost.getSearchAreaList();
		ISearchAreaIf searchArea = searchAreaList.createSearchArea();
		return getFeature(searchArea.getObjectId());
	}
	
	public int getShapeType() throws IOException, AutomationException {
		return esriGeometryType.esriGeometryPolygon;
	}
}
