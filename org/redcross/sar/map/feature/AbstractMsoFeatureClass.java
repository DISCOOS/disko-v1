package org.redcross.sar.map.feature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.redcross.sar.app.Utils;
import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.event.IDiskoMapEventListener;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.Update;

import com.esri.arcgis.geodatabase.IEnumRelationshipClass;
import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geodatabase.IFeatureBuffer;
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
import com.esri.arcgis.geometry.IEnvelope;
import com.esri.arcgis.geometry.IGeometry;
import com.esri.arcgis.geometry.IRelationalOperator;
import com.esri.arcgis.geometry.ISpatialReference;
import com.esri.arcgis.geometry.esriGeometryType;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.system.IPropertySet;
import com.esri.arcgis.system.IUID;

public abstract class AbstractMsoFeatureClass implements IMsoFeatureClass, IGeoDataset, 
		IMsoUpdateListenerIf {

	private static final long serialVersionUID = 1L;
	
	protected IMsoManagerIf.MsoClassCode classCode = null;
	protected IMsoModelIf msoModel = null;
	protected ISpatialReference srs = null;
	protected ArrayList<IMsoFeature> data = null;
	protected ArrayList<IDiskoMapEventListener> listeners = null;
	protected DiskoMapEvent diskoMapEvent = null;
	protected EnumSet<IMsoManagerIf.MsoClassCode> myInterests = null;
	protected boolean isDirty = false;
	
	public AbstractMsoFeatureClass(IMsoManagerIf.MsoClassCode classCode, IMsoModelIf msoModel) {
		this.classCode = classCode;
		this.msoModel = msoModel;
		myInterests = EnumSet.of(classCode);
		IMsoEventManagerIf msoEventManager = msoModel.getEventManager();
		msoEventManager.addClientUpdateListener(this);
		data = new ArrayList<IMsoFeature>();
		listeners = new ArrayList<IDiskoMapEventListener>();
		diskoMapEvent = new DiskoMapEvent(this);
	}
	
	public IMsoManagerIf.MsoClassCode getClassCode() {
		return classCode;
	}

	public void setClassCode(IMsoManagerIf.MsoClassCode classCode) {
		this.classCode = classCode;
	}

	public IMsoModelIf getMsoModel() {
		return msoModel;
	}

	public void setMsoModel(IMsoModelIf msoModel) {
		this.msoModel = msoModel;
	}
	
	public IMsoFeature createMsoFeature() {
		return null;
	}
	
	public void setIsDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}
	
	public boolean getIsDirty() {
		return isDirty;
	}

	public void handleMsoUpdateEvent(Update e) {
	}
	
	public boolean hasInterestIn(IMsoObjectIf aMsoObject) {
		return myInterests.contains(aMsoObject.getMsoClassCode());
	}
	
	public IMsoFeature getFeature(String id) {
		for (int i = 0; i < data.size(); i++) {
			IMsoFeature feature = (IMsoFeature)data.get(i);
			if (feature.getID().equals(id)) {
				return feature;
			}
		}
		return null;
	}
	
	public void setSelected(IMsoFeature msoFeature, boolean selected) {
		msoFeature.setSelected(selected);
		fireOnSelectionChanged();
	}
	
	public void clearSelected() throws AutomationException, IOException {
		for (int i = 0; i < featureCount(null); i++) {
			IMsoFeature feature = (IMsoFeature)getFeature(i);
			feature.setSelected(false);
			fireOnSelectionChanged();
		}
	}
	
	public List getSelected() throws AutomationException, IOException {
		ArrayList<IMsoFeature> selection = new ArrayList<IMsoFeature>();
		for (int i = 0; i < featureCount(null); i++) {
			IMsoFeature feature = (IMsoFeature)getFeature(i);
			if (feature.isSelected()) {
				selection.add(feature);
			}
		}
		return selection;
	}
	
	public List getSelectedMsoObjects() throws AutomationException, IOException {
		ArrayList<IMsoObjectIf> selection = new ArrayList<IMsoObjectIf>();
		for (int i = 0; i < featureCount(null); i++) {
			IMsoFeature feature = (IMsoFeature)getFeature(i);
			selection.add(feature.getMsoObject());
		}
		return selection;
	}
	
	public void addDiskoMapEventListener(IDiskoMapEventListener listener) {
		if (listeners.indexOf(listener) == -1) {
			listeners.add(listener);
		}
	}
	
	public void removeDiskoMapEventListener(IDiskoMapEventListener listener) {
		listeners.remove(listener);
	}
	
	protected void fireOnSelectionChanged() {
		for (int i = 0; i < listeners.size(); i++) {
			try {
				listeners.get(i).onSelectionChanged(diskoMapEvent);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
	
	protected void removeFeature(IFeature feature) throws IOException, AutomationException {
		feature.setShapeByRef(null);
		if (feature instanceof IMsoFeature) {
			((IMsoFeature)feature).setMsoObject(null);
		}
		data.remove(feature);
	}

	public IFeature createFeature() throws IOException, AutomationException {
		return null;
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
		return Utils.translate(classCode);
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
