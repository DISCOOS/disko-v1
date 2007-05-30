package org.redcross.sar.map.feature;

import java.io.IOException;

import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.util.mso.IGeodataIf;

import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geometry.ISpatialReference;
import com.esri.arcgis.interop.AutomationException;

public interface IMsoFeature extends IFeature {
	
	public Object getID();
	
	public IMsoObjectIf getMsoObject();
	
	public void setMsoObject(IMsoObjectIf msoObject) throws IOException, AutomationException;

	public void msoGeometryChanged() throws IOException, AutomationException;
	
	public void setGeodata(IGeodataIf geodata);
	
	public void removeGeodata(IGeodataIf geodata);
	
	public void splitGeodata(IGeodataIf geodata);
	
	public void addGeodataToCollection(IGeodataIf geodata);
	
	public void removeGeodataFromCollectionAt(int index);
	
	public void setSpatialReference(ISpatialReference srs) throws IOException, AutomationException;

	public ISpatialReference getSpatialReference() throws IOException, AutomationException;
	
	public void setSelected(boolean selected);
	
	public boolean isSelected();
	
}
