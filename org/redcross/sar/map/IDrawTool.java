package org.redcross.sar.map;

import java.io.IOException;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.IMsoObjectIf;

import com.esri.arcgis.interop.AutomationException;

public interface IDrawTool {
	
	public void setIsDirty();

	public void setSnapTolerance(double tolerance) throws IOException, AutomationException;
	
	public int getSnapTolerance() throws IOException;

	public void setArea(IMsoObjectIf area);
	
	public void setMsoClassCode(IMsoManagerIf.MsoClassCode msoClassCode);
}
