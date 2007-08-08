package org.redcross.sar.map.feature;

import java.io.IOException;

import org.redcross.sar.mso.data.IMsoObjectIf;

import com.esri.arcgis.geodatabase.IFields;
import com.esri.arcgis.geodatabase.IObjectClass;
import com.esri.arcgis.geodatabase.ITable;
import com.esri.arcgis.geometry.IEnvelope;
import com.esri.arcgis.geometry.IGeometry;
import com.esri.arcgis.geometry.ISpatialReference;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.system.IClone;

public abstract class AbstractMsoFeature implements IMsoFeature {
	
	private static final long serialVersionUID = 1L;
	protected IMsoObjectIf msoObject = null;
	protected IGeometry geometry = null;
	protected ISpatialReference srs = null;
	protected boolean isSelected = false;
	
	public AbstractMsoFeature() {}
	
	public Object getID() {
		return msoObject.getObjectId();
	}

	public IMsoObjectIf getMsoObject() {
		return msoObject;
	}

	public void setMsoObject(IMsoObjectIf msoObject) throws IOException, AutomationException {
		this.msoObject = msoObject;
		if (msoObject != null) {
			msoGeometryChanged();
		}
	}
	
	public boolean geometryIsChanged(IMsoObjectIf msoObj) {
		return false;
	}

	public void msoGeometryChanged() throws IOException, AutomationException {
		// TODO Auto-generated method stub
	}
	
	public void setSelected(boolean selected) {
		isSelected = selected;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public Object getGeodata() {
		return null;
	}
	
	public int getGeodataCount() {
		return 0;
	}

	public IEnvelope getExtent() throws IOException, AutomationException {
		if (geometry != null) {
			return geometry.getEnvelope();
		}
		return null;
	}

	public IGeometry getShape() throws IOException, AutomationException {
		return geometry;
	}
	
	public IGeometry getShapeCopy() throws IOException, AutomationException {
		return (IGeometry)((IClone)geometry).esri_clone();
	}

	public void setShapeByRef(IGeometry geom) throws IOException, AutomationException {
	}

	public int getFeatureType() throws IOException, AutomationException {
		return com.esri.arcgis.geodatabase.esriFeatureType.esriFTSimple;
	}

	public IObjectClass esri_getClass() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete() throws IOException, AutomationException {
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
	
	public void setSpatialReference(ISpatialReference srs) throws IOException, AutomationException {
		this.srs = srs;
	}

	public ISpatialReference getSpatialReference() throws IOException, AutomationException {
		return srs;
	}
}
