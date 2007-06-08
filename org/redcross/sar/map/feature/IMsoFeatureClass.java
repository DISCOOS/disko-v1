package org.redcross.sar.map.feature;

import java.io.IOException;
import java.util.List;

import org.redcross.sar.event.IDiskoMapEventListener;
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
	
	public void setSelected(IMsoFeature msoFeature, boolean selected);
	
	public void clearSelected() throws AutomationException, IOException;
	
	public List getSelected() throws AutomationException, IOException;
	
	public List getSelectedMsoObjects() throws AutomationException, IOException;
	
	public void addDiskoMapEventListener(IDiskoMapEventListener listener);
	
	public void removeDiskoMapEventListener(IDiskoMapEventListener listener);
}
