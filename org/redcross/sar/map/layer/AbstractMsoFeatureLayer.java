package org.redcross.sar.map.layer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.redcross.sar.app.Utils;
import org.redcross.sar.event.IMsoLayerEventListener;
import org.redcross.sar.event.MsoLayerEvent;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.MsoFeatureClass;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.EventType;
import org.redcross.sar.mso.event.MsoEvent.Update;

import com.esri.arcgis.carto.ILayerGeneralProperties;
import com.esri.arcgis.carto.esriViewDrawPhase;
import com.esri.arcgis.display.IDisplay;
import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geodatabase.IFeatureClass;
import com.esri.arcgis.geodatabase.IFeatureCursor;
import com.esri.arcgis.geodatabase.IGeoDataset;
import com.esri.arcgis.geodatabase.IQueryFilter;
import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.IEnvelope;
import com.esri.arcgis.geometry.ISpatialReference;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.system.IPersistVariant;
import com.esri.arcgis.system.ITrackCancel;
import com.esri.arcgis.system.IUID;
import com.esri.arcgis.system.IVariantStream;

public abstract class AbstractMsoFeatureLayer implements IMsoFeatureLayer, IGeoDataset,
		IPersistVariant, ILayerGeneralProperties, IMsoUpdateListenerIf {
	
	protected String name = null;
	protected IMsoManagerIf.MsoClassCode classCode = null;
	protected IMsoFeatureLayer.LayerCode layerCode = null;
	protected IEnvelope extent = null;
	protected ISpatialReference srs = null;
	protected IFeatureClass featureClass = null;
	protected boolean isCached = false;
	protected boolean isValid, isVisible = true;
	protected boolean isSelectable = true;
	protected double maximumScale, minimumScale = 0;
	protected boolean showTips, isDirty = false;
	protected ArrayList<IMsoLayerEventListener> listeners = null;
	protected MsoLayerEvent msoLayerEvent = null;
	protected IMsoModelIf msoModel = null;
	protected EnumSet<IMsoManagerIf.MsoClassCode> myInterests = null;

	public AbstractMsoFeatureLayer(IMsoManagerIf.MsoClassCode classCode, 
			IMsoFeatureLayer.LayerCode layerCode, IMsoModelIf msoModel) {
		this.classCode = classCode;
		this.layerCode = layerCode;
		this.msoModel = msoModel;
		featureClass = new MsoFeatureClass();
		name = Utils.translate(layerCode);
		
		listeners = new ArrayList<IMsoLayerEventListener>();
		msoLayerEvent = new MsoLayerEvent(this);
		
		myInterests = EnumSet.of(classCode);
		IMsoEventManagerIf msoEventManager = msoModel.getEventManager();
		msoEventManager.addClientUpdateListener(this);
	}
	
	public IMsoModelIf getMsoModel() {
		return msoModel;
	}
	
	public void handleMsoUpdateEvent(Update e) {
		try {
 			MsoFeatureClass msoFC = (MsoFeatureClass)featureClass;
			int type = e.getEventTypeMask();
			IMsoObjectIf msoObj = (IMsoObjectIf)e.getSource();
			IMsoFeature msoFeature = msoFC.getFeature(msoObj.getObjectId());
			
			if (type == EventType.ADDED_REFERENCE_EVENT.maskValue() && 
					msoFeature == null) {
				//System.out.println("ADDED_REFERENCE_EVENT "+classCode);
				msoFeature = createMsoFeature(msoObj);
				msoFC.addFeature(msoFeature);
				//isDirty = true;
			}
			else if (type == EventType.MODIFIED_DATA_EVENT.maskValue() && 
					msoFeature != null && msoFeature.geometryIsChanged(msoObj)) {
				//System.out.println("MODIFIED_DATA_EVENT "+classCode);
				msoFeature.msoGeometryChanged();
				isDirty = true;
			}
			else if (type == EventType.DELETED_OBJECT_EVENT.maskValue() && 
					msoFeature != null) {
				//System.out.println("DELETED_OBJECT_EVENT "+classCode);
				msoFC.removeFeature(msoFeature);
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

	public boolean hasInterestIn(IMsoObjectIf aMsoObject) {
		return myInterests.contains(aMsoObject.getMsoClassCode());
	}
	
	protected IMsoFeature createMsoFeature(IMsoObjectIf msoFeature) 
			throws AutomationException, IOException {
		return null;
	}
	
	public void addDiskoLayerEventListener(IMsoLayerEventListener listener) {
		if (listeners.indexOf(listener) == -1) {
			listeners.add(listener);
		}
	}
	
	public void removeDiskoLayerEventListener(IMsoLayerEventListener listener) {
		listeners.remove(listener);
	}
	
	protected void fireOnSelectionChanged() {
		for (int i = 0; i < listeners.size(); i++) {
			try {
				listeners.get(i).onSelectionChanged(msoLayerEvent);
			} catch (AutomationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setSelected(IMsoFeature msoFeature, boolean selected) {
		msoFeature.setSelected(selected);
		fireOnSelectionChanged();
	}
	
	public void clearSelected() throws AutomationException, IOException {
		for (int i = 0; i < featureClass.featureCount(null); i++) {
			IMsoFeature feature = (IMsoFeature)featureClass.getFeature(i);
			feature.setSelected(false);
		}
		fireOnSelectionChanged();
	}
	
	public List getSelected() throws AutomationException, IOException {
		ArrayList<IMsoFeature> selection = new ArrayList<IMsoFeature>();
		for (int i = 0; i < featureClass.featureCount(null); i++) {
			IMsoFeature feature = (IMsoFeature)featureClass.getFeature(i);
			if (feature.isSelected()) {
				selection.add(feature);
			}
		}
		return selection;
	}
	
	public List getSelectedMsoObjects() throws AutomationException, IOException {
		ArrayList<IMsoObjectIf> selection = new ArrayList<IMsoObjectIf>();
		for (int i = 0; i < featureClass.featureCount(null); i++) {
			IMsoFeature feature = (IMsoFeature)featureClass.getFeature(i);
			if (feature.isSelected()) {
				selection.add(feature.getMsoObject());
			}
		}
		return selection;
	}
	
	public IFeature searchData(Envelope env) throws IOException, AutomationException {
		return null;
	}
	
	public boolean isDirty() {
		return isDirty;
	}
	
	public String getName() throws IOException, AutomationException {
		return name;
	}

	public void setName(String name) throws IOException, AutomationException {
		this.name = name;
	}

	public IMsoManagerIf.MsoClassCode getClassCode() {
		return classCode;
	}
	
	public IMsoFeatureLayer.LayerCode getLayerCode() {
		return layerCode;
	}

	public boolean isValid() throws IOException, AutomationException {
		return true;
	}

	public double getMinimumScale() throws IOException, AutomationException {
		return minimumScale;
	}

	public void setMinimumScale(double arg0) throws IOException, AutomationException {
		minimumScale = arg0;
	}

	public double getMaximumScale() throws IOException, AutomationException {
		return maximumScale;
	}

	public void setMaximumScale(double arg0) throws IOException, AutomationException {
		maximumScale = arg0;
	}

	public boolean isVisible() throws IOException, AutomationException {
		return isVisible;
	}

	public void setVisible(boolean arg0) throws IOException, AutomationException {
		isVisible = arg0;
	}

	public boolean isShowTips() throws IOException, AutomationException {
		return showTips;
	}

	public void setShowTips(boolean arg0) throws IOException, AutomationException {
		showTips = arg0;
	}

	public boolean isCached() throws IOException, AutomationException {
		return isCached;
	}

	public void setCached(boolean arg0) throws IOException, AutomationException {
		isCached = arg0;
	}

	public void setSpatialReferenceByRef(ISpatialReference srs)
			throws IOException, AutomationException {
		this.srs = srs;
		for (int i = 0; i < featureClass.featureCount(null); i++) {
			IMsoFeature feature = (IMsoFeature)featureClass.getFeature(i);
			feature.setSpatialReference(srs);
		}
	}

	public ISpatialReference getSpatialReference() throws IOException, AutomationException {
		return srs;
	}

	public IEnvelope getExtent() throws IOException, AutomationException {
		return extent;
	}
	
	public void setExtent(IEnvelope extent) {
		this.extent = extent;
	}
	
	public IUID getID() throws IOException, AutomationException {
		return null;
	}

	public void load(IVariantStream arg0) throws IOException, AutomationException {
		// Not Supported in Java
	}

	public void save(IVariantStream arg0) throws IOException, AutomationException {
		// Not Supported in Java
	}

	public IEnvelope getAreaOfInterest() throws IOException, AutomationException {
		return extent;
	}

	public double getLastMinimumScale() throws IOException, AutomationException {
		return 0;
	}

	public double getLastMaximumScale() throws IOException, AutomationException {
		return 0;
	}

	public String ILayerGeneralProperties_getLayerDescription()
			throws IOException, AutomationException {
		return null;
	}

	public void setLayerDescription(String arg0) throws IOException, AutomationException {
	}

	public String getTipText(double arg0, double arg1, double arg2)
			throws IOException, AutomationException {
		return null;
	}

	public int getSupportedDrawPhases() throws IOException, AutomationException {
		return esriViewDrawPhase.esriViewGeography;
	}

	public void draw(int arg0, IDisplay arg1, ITrackCancel arg2)
			throws IOException, AutomationException {
	}
	
	public String getDataSourceType() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDisplayField() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public IFeatureClass getFeatureClass() throws IOException, AutomationException {
		return featureClass;
	}

	public boolean isScaleSymbols() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSelectable() throws IOException, AutomationException {
		return isSelectable;
	}

	public IFeatureCursor search(IQueryFilter filter, boolean b) throws IOException, AutomationException {
		return featureClass.search(filter, b);
	}

	public void setDataSourceType(String arg0) throws IOException, AutomationException {
		// TODO Auto-generated method stub
		
	}

	public void setDisplayField(String arg0) throws IOException, AutomationException {
		// TODO Auto-generated method stub
		
	}

	public void setFeatureClassByRef(IFeatureClass featureClass) throws IOException, AutomationException {
		this.featureClass = featureClass;
		
	}

	public void setScaleSymbols(boolean arg0) throws IOException, AutomationException {
		// TODO Auto-generated method stub
		
	}

	public void setSelectable(boolean isSelectable) throws IOException, AutomationException {
		this.isSelectable = isSelectable;
	}
}