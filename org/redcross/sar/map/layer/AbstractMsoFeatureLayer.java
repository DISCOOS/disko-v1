package org.redcross.sar.map.layer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.redcross.sar.app.Utils;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.IMsoFeatureClass;
import org.redcross.sar.mso.IMsoManagerIf;
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
		IPersistVariant, ILayerGeneralProperties {
	
	protected String name = null;
	protected IMsoManagerIf.MsoClassCode classCode = null;
	protected IEnvelope extent = null;
	protected ISpatialReference srs = null;
	protected IFeatureClass featureClass = null;
	protected boolean isCached = false;
	protected boolean isValid, isVisible = true;
	protected double maximumScale, minimumScale = 0;
	protected boolean showTips = false;
	protected boolean isDirty = false;

	public AbstractMsoFeatureLayer() {
	}
	
	public IFeature searchData(Envelope env) throws IOException, AutomationException {
		return null;
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

	public void setClassCode(IMsoManagerIf.MsoClassCode classCode) {
		this.classCode = classCode;
		this.name = Utils.translate(classCode);
	}
	
	public void clearSelected() throws AutomationException, IOException {
		for (int i = 0; i < featureClass.featureCount(null); i++) {
			IMsoFeature feature = (IMsoFeature)featureClass.getFeature(i);
			feature.setSelected(false);
		}
	}
	
	public List getSelected() throws AutomationException, IOException {
		ArrayList selection = new ArrayList();
		for (int i = 0; i < featureClass.featureCount(null); i++) {
			IMsoFeature feature = (IMsoFeature)featureClass.getFeature(i);
			selection.add(feature);
		}
		return selection;
	}
	
	public List getSelectedMsoObjects() throws AutomationException, IOException {
		ArrayList selection = new ArrayList();
		for (int i = 0; i < featureClass.featureCount(null); i++) {
			IMsoFeature feature = (IMsoFeature)featureClass.getFeature(i);
			selection.add(feature.getMsoObject());
		}
		return selection;
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
		if (featureClass != null) {
			((IMsoFeatureClass)featureClass).setSpatialReference(srs);
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
		// TODO Auto-generated method stub
		return false;
	}

	public IFeatureCursor search(IQueryFilter arg0, boolean arg1) throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
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

	public void setSelectable(boolean arg0) throws IOException, AutomationException {
		// TODO Auto-generated method stub
		
	}
}