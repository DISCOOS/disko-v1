package org.redcross.sar.map.layer;

import org.redcross.sar.mso.IMsoManagerIf;

import com.esri.arcgis.carto.IFeatureLayer;

public interface IMsoFeatureLayer extends IFeatureLayer {
	
	public enum LayerCode {
		AREA_LAYER,
		OPERATION_AREA_LAYER,
		SEARCH_AREA_LAYER,
		POI_LAYER,
		FLANK_LAYER,
		OPERATION_AREA_MASK_LAYER
    }
	
	public IMsoManagerIf.MsoClassCode getClassCode();

	public void setClassCode(IMsoManagerIf.MsoClassCode classCode) ;
	
	public void setLayerCode(IMsoFeatureLayer.LayerCode layerCode);
	
	public IMsoFeatureLayer.LayerCode getLayerCode();
}
