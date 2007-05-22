package org.redcross.sar.map;

import java.io.IOException;

import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.map.layer.MsoFeature;
import com.esri.arcgis.geodatabase.IFeatureClass;
import com.esri.arcgis.interop.AutomationException;

public interface IDiskoTool {
	
	public void toolActivated() throws IOException, AutomationException;
	
	public void toolDeactivated() throws IOException, AutomationException;
	
	public DiskoDialog getDialog();
	
	public IDiskoMap getMap();
	
	public IFeatureClass getFeatureClass();

	public void setFeatureClass(IFeatureClass featureClass);
	
	public void setEditFeature(MsoFeature msoFeature);
	
	public MsoFeature getEditFeature();
	
	public IEditFeedback getEditFeedback();

	public void setEditFeedback(IEditFeedback editFeedback);
}
