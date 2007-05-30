package org.redcross.sar.map.feature;

import java.io.IOException;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;

import com.esri.arcgis.geodatabase.IFeatureClass;
import com.esri.arcgis.geometry.ISpatialReference;
import com.esri.arcgis.interop.AutomationException;

public interface IMsoFeatureClass extends IFeatureClass {
	
	public IMsoManagerIf.MsoClassCode getClassCode();

	public void setClassCode(IMsoManagerIf.MsoClassCode classCode);

	public IMsoModelIf getMsoModel();

	public void setMsoModel(IMsoModelIf msoModel);
	
	public void setSpatialReference(ISpatialReference srs) throws IOException, AutomationException;
	
	public IMsoFeature getFeature(String id);
}
