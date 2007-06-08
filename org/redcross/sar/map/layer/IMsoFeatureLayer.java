package org.redcross.sar.map.layer;

import org.redcross.sar.mso.IMsoManagerIf;

import com.esri.arcgis.carto.IFeatureLayer;

public interface IMsoFeatureLayer extends IFeatureLayer {
	
	public IMsoManagerIf.MsoClassCode getClassCode();

	public void setClassCode(IMsoManagerIf.MsoClassCode classCode) ;
}
