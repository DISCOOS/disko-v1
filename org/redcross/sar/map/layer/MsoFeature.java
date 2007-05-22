package org.redcross.sar.map.layer;

import java.io.IOException;

import org.redcross.sar.mso.data.IMsoObjectIf;

import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geodatabase.IFields;
import com.esri.arcgis.geodatabase.IObjectClass;
import com.esri.arcgis.geodatabase.ITable;
import com.esri.arcgis.geometry.IEnvelope;
import com.esri.arcgis.geometry.IGeometry;
import com.esri.arcgis.interop.AutomationException;

public class MsoFeature implements IFeature {
	
	private static final long serialVersionUID = 1L;
	private IMsoObjectIf msoObject = null;
	private IGeometry geometry = null;
	private boolean isSelected = false;
	
	public MsoFeature() {}
	
	public Object getID() {
		return msoObject.getObjectId();
	}

	public IMsoObjectIf getMsoObject() {
		return msoObject;
	}

	public void setMsoObject(IMsoObjectIf msoObject) {
		this.msoObject = msoObject;
	}

	public IEnvelope getExtent() throws IOException, AutomationException {
		return geometry.getEnvelope();
	}

	public IGeometry getShape() throws IOException, AutomationException {
		return geometry;
	}
	
	public IGeometry getShapeCopy() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setShapeByRef(IGeometry geom) throws IOException, AutomationException {
		geometry = geom;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public int getFeatureType() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return 0;
	}

	public IObjectClass esri_getClass() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete() throws IOException, AutomationException {
		msoObject.deleteObject();
	}

	public int getOID() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return 0;
	}

	public ITable getTable() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isHasOID() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return false;
	}

	public void store() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		
	}

	public IFields getFields() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue(int arg0) throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setValue(int arg0, Object arg1) throws IOException, AutomationException {
		// TODO Auto-generated method stub
		
	}
}
