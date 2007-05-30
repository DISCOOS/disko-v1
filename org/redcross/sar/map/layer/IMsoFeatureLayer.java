package org.redcross.sar.map.layer;

import java.io.IOException;
import java.util.List;

import org.redcross.sar.mso.IMsoManagerIf;
import com.esri.arcgis.carto.IFeatureLayer;
import com.esri.arcgis.interop.AutomationException;

public interface IMsoFeatureLayer extends IFeatureLayer {
	
	public IMsoManagerIf.MsoClassCode getClassCode();

	public void setClassCode(IMsoManagerIf.MsoClassCode classCode) ;
	
	public void clearSelected() throws AutomationException, IOException;
	
	public List getSelected() throws AutomationException, IOException;
	
	public List getSelectedMsoObjects() throws AutomationException, IOException;
}
