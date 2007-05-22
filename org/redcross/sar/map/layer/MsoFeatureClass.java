package org.redcross.sar.map.layer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.mso.data.IAreaListIf;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IOperationAreaIf;
import org.redcross.sar.mso.data.IOperationAreaListIf;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.mso.data.IPOIListIf;
import org.redcross.sar.mso.data.ISearchAreaIf;
import org.redcross.sar.mso.data.ISearchAreaListIf;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.EventType;
import org.redcross.sar.mso.event.MsoEvent.Update;

import com.esri.arcgis.geodatabase.IEnumRelationshipClass;
import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geodatabase.IFeatureBuffer;
import com.esri.arcgis.geodatabase.IFeatureClass;
import com.esri.arcgis.geodatabase.IFeatureCursor;
import com.esri.arcgis.geodatabase.IFeatureDataset;
import com.esri.arcgis.geodatabase.IField;
import com.esri.arcgis.geodatabase.IFields;
import com.esri.arcgis.geodatabase.IGeoDataset;
import com.esri.arcgis.geodatabase.IIndex;
import com.esri.arcgis.geodatabase.IIndexes;
import com.esri.arcgis.geodatabase.IQueryFilter;
import com.esri.arcgis.geodatabase.ISelectionSet;
import com.esri.arcgis.geodatabase.ISpatialFilter;
import com.esri.arcgis.geodatabase.IWorkspace;
import com.esri.arcgis.geometry.GeometryBag;
import com.esri.arcgis.geometry.IEnvelope;
import com.esri.arcgis.geometry.IGeometry;
import com.esri.arcgis.geometry.IRelationalOperator;
import com.esri.arcgis.geometry.ISpatialReference;
import com.esri.arcgis.geometry.esriGeometryType;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.system.IPropertySet;
import com.esri.arcgis.system.IUID;

public class MsoFeatureClass implements IFeatureClass, IGeoDataset, IMsoUpdateListenerIf {

	private static final long serialVersionUID = 1L;
	
	private IMsoManagerIf.MsoClassCode classCode = null;
	private IMsoModelIf msoModel = null;
	private ISpatialReference srs = null;
	private ArrayList data = null;
	private EnumSet<IMsoManagerIf.MsoClassCode> myInterests = null;
	
	public MsoFeatureClass(IMsoManagerIf.MsoClassCode classCode, IMsoModelIf msoModel) {
		this.classCode = classCode;
		this.msoModel = msoModel;
		myInterests = EnumSet.of(classCode);
		IMsoEventManagerIf msoEventManager = msoModel.getEventManager();
		msoEventManager.addClientUpdateListener(this);
		data = new ArrayList();
	}
	
	public void handleMsoUpdateEvent(Update e) {
		try {
			int type = e.getEventTypeMask();
			IMsoObjectIf msoObj = (IMsoObjectIf)e.getSource();
			MsoFeature msoFeature = getData(msoObj.getObjectId());
			
			if (type == EventType.ADDED_REFERENCE_EVENT.maskValue() && msoFeature != null) {
				createFeature(msoObj);
			}
			else if ((type == EventType.REMOVED_REFERENCE_EVENT.maskValue() ||
					 type == EventType.DELETED_OBJECT_EVENT.maskValue()) && msoFeature != null) {
				data.remove(msoFeature);
			}
			else if (type == EventType.MODIFIED_DATA_EVENT.maskValue() && msoFeature != null) {
				//Polygon polygon = MapUtil.getEsriPolygon(opArea.getGeodata(), getSpatialReference());
				//msoFeature.setShape(polygon);
			}
		} catch (AutomationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private MsoFeature getData(String id) {
		for (int i = 0; i < data.size(); i++) {
			MsoFeature msoFeature = (MsoFeature)data.get(i);
			if (msoFeature.getID().equals(id)) {
				return msoFeature;
			}
		}
		return null;
	}

	public boolean hasInterestIn(IMsoObjectIf aMsoObject) {
		return myInterests.contains(aMsoObject.getMsoClassCode());
	}

	public IFeatureCursor IFeatureClass_insert(boolean arg0)
			throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public IFeatureCursor IFeatureClass_update(IQueryFilter arg0, boolean arg1)
			throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}
	
	private MsoFeature createFeature(IMsoObjectIf obj) throws AutomationException, IOException {
		if (obj == null) {
			return null;
		}
		MsoFeature msoFeature = new MsoFeature();
		if (classCode == IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA) {
			IOperationAreaIf opArea = (IOperationAreaIf)obj;
			msoFeature.setMsoObject(opArea);
		}
		else if (classCode == IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA) {
			ISearchAreaIf searchArea = (ISearchAreaIf)obj;
			msoFeature.setMsoObject(searchArea);
		}
		else if (classCode == IMsoManagerIf.MsoClassCode.CLASSCODE_AREA) {
			IAreaIf area = (IAreaIf)obj;
			msoFeature.setMsoObject(area);
			msoFeature.setShapeByRef(new GeometryBag());
		}
		else if (classCode == IMsoManagerIf.MsoClassCode.CLASSCODE_POI) {
			IPOIIf poi = (IPOIIf)obj;
			msoFeature.setMsoObject(poi);
		}
		data.add(msoFeature);
		return msoFeature;
 	}

	public IFeature createFeature() throws IOException, AutomationException {
		IMsoObjectIf msoObj  = null;
		ICmdPostIf cmdPost = msoModel.getMsoManager().getCmdPost();
		if (classCode == IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA) {
			IOperationAreaListIf opAreaList = cmdPost.getOperationAreaList();
			msoObj = opAreaList.createOperationArea();
		}
		else if (classCode == IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA) {
			ISearchAreaListIf searchAreaList = cmdPost.getSearchAreaList();
			msoObj = searchAreaList.createSearchArea();
		}
		else if (classCode == IMsoManagerIf.MsoClassCode.CLASSCODE_AREA) {
			IAreaListIf areaList = cmdPost.getAreaList();
			msoObj = areaList.createArea();
		}
		else if (classCode == IMsoManagerIf.MsoClassCode.CLASSCODE_POI) {
			IPOIListIf poiList = cmdPost.getPOIList();
			msoObj = poiList.createPOI();
		}
		return createFeature(msoObj); 
	}

	public IFeatureBuffer createFeatureBuffer() throws IOException,
			AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public int featureCount(IQueryFilter arg0) throws IOException,
			AutomationException {
		return data.size();
	}

	public IField getAreaField() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public IFeature getFeature(int index) throws IOException,
			AutomationException {
		return (IFeature)data.get(index);
	}

	public int getFeatureClassID() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return 0;
	}

	public IFeatureDataset getFeatureDataset() throws IOException,
			AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getFeatureType() throws IOException, AutomationException {
		return com.esri.arcgis.geodatabase.esriFeatureType.esriFTSimple;
	}

	public IFeatureCursor getFeatures(Object arg0, boolean arg1)
			throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public IField getLengthField() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getShapeFieldName() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getShapeType() throws IOException, AutomationException {
		if (classCode == IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA) {
			return esriGeometryType.esriGeometryPolygon;
		}
		if (classCode == IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA) {
			return esriGeometryType.esriGeometryPolygon;
		}
		if (classCode == IMsoManagerIf.MsoClassCode.CLASSCODE_AREA) {
			return esriGeometryType.esriGeometryBag;
		}
		if (classCode == IMsoManagerIf.MsoClassCode.CLASSCODE_POI) {
			return esriGeometryType.esriGeometryPoint;
		}
		return esriGeometryType.esriGeometryAny;
	}

	public IFeatureCursor search(IQueryFilter filter, boolean arg1)
			throws IOException, AutomationException {
		MsoFeatureCursor cursor = new MsoFeatureCursor();
		if (filter instanceof ISpatialFilter) {
			ISpatialFilter spatialFilter = (ISpatialFilter)filter;
			IGeometry filterGeom = spatialFilter.getGeometry();
			
			for (int i = 0; i < data.size(); i++) {
				IFeature feature = (IFeature)data.get(i);
				IRelationalOperator geom = (IRelationalOperator)feature.getShape();
				if (geom != null && !geom.disjoint(filterGeom)) {
					cursor.add(feature);
				}
			}
		}
		return cursor;
	}

	public ISelectionSet select(IQueryFilter arg0, int arg1, int arg2,
			IWorkspace arg3) throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAliasName() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getObjectClassID() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return 0;
	}

	public IEnumRelationshipClass getRelationshipClasses(int arg0)
			throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public void addField(IField arg0) throws IOException, AutomationException {
		// TODO Auto-generated method stub

	}

	public void addIndex(IIndex arg0) throws IOException, AutomationException {
		// TODO Auto-generated method stub

	}

	public void deleteField(IField arg0) throws IOException,
			AutomationException {
		// TODO Auto-generated method stub

	}

	public void deleteIndex(IIndex arg0) throws IOException,
			AutomationException {
		// TODO Auto-generated method stub

	}

	public int findField(String arg0) throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return 0;
	}

	public IUID getCLSID() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public IUID getEXTCLSID() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getExtension() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public IPropertySet getExtensionProperties() throws IOException,
			AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public IFields getFields() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public IIndexes getIndexes() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getOIDFieldName() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isHasOID() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return false;
	}

	public IEnvelope getExtent() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setSpatialReference(ISpatialReference srs) throws IOException, AutomationException {
		this.srs = srs;
	}

	public ISpatialReference getSpatialReference() throws IOException, AutomationException {
		return srs;
	}
}
