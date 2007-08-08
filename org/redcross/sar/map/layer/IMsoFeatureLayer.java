package org.redcross.sar.map.layer;

import java.io.IOException;
import java.util.List;

import org.redcross.sar.event.IMsoLayerEventListener;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;

import com.esri.arcgis.carto.IFeatureLayer;
import com.esri.arcgis.interop.AutomationException;

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
	
	public IMsoFeatureLayer.LayerCode getLayerCode();
	
	public void setSelected(IMsoFeature msoFeature, boolean selected);
	
	public void clearSelected() throws AutomationException, IOException;
	
	public List getSelected() throws AutomationException, IOException;
	
	public List getSelectedMsoObjects() throws AutomationException, IOException;
	
	public void addDiskoLayerEventListener(IMsoLayerEventListener listener);
	
	public void removeDiskoLayerEventListener(IMsoLayerEventListener listener);
	
	public IMsoModelIf getMsoModel();
	
	public boolean isDirty();
}
