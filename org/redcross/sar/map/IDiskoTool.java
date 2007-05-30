package org.redcross.sar.map;

import java.io.IOException;

import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.IMsoFeatureClass;

import com.esri.arcgis.interop.AutomationException;

public interface IDiskoTool {
	
	public void toolActivated() throws IOException, AutomationException;
	
	public void toolDeactivated() throws IOException, AutomationException;
	
	public DiskoDialog getDialog();
	
	public IDiskoMap getMap();
	
	public void setFeatureClass(IMsoFeatureClass featureClass);
	
	public IMsoFeatureClass getFeatureClass();
	
	public void setEditFeature(IMsoFeature editFeature);
	
	public IMsoFeature getEditFeature();
	
	public IEditFeedback getEditFeedback();

	public void setEditFeedback(IEditFeedback editFeedback);
}
