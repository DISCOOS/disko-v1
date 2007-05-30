package org.redcross.sar.map.feature;

import java.io.IOException;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.ISearchAreaIf;
import org.redcross.sar.mso.data.ISearchAreaListIf;
import org.redcross.sar.mso.event.MsoEvent.EventType;
import org.redcross.sar.mso.event.MsoEvent.Update;

import com.esri.arcgis.geodatabase.IFeature;
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
			IMsoModelIf.ModificationState geodataState = searchArea.getGeodataState();
			System.out.println(classCode.name()+" ModificationState: "+geodataState.name());
			
			if (type == EventType.ADDED_REFERENCE_EVENT.maskValue() &&
					geodataState == IMsoModelIf.ModificationState.STATE_SERVER_MODIFIED) {
				System.out.println(classCode.name()+" .... created");
				createFeature(searchArea);
			}
			else if (type == EventType.MODIFIED_DATA_EVENT.maskValue()) {
				System.out.println(classCode.name()+" .... modified");
				msoFeature.msoGeometryChanged();
			}
			else if (type == EventType.DELETED_OBJECT_EVENT.maskValue()) {
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
		ISearchAreaListIf searchAreaList = cmdPost.getSearchAreaList();
		return createFeature(searchAreaList.createSearchArea());
	}
	
	@SuppressWarnings("unchecked")
	private SearchAreaFeature createFeature(IMsoObjectIf obj) throws AutomationException, IOException {
		SearchAreaFeature feature = new SearchAreaFeature();
		feature.setSpatialReference(srs);
		feature.setMsoObject(obj);
		data.add(feature);
		return feature;
 	}
	
	public int getShapeType() throws IOException, AutomationException {
		return esriGeometryType.esriGeometryPolygon;
	}
}
