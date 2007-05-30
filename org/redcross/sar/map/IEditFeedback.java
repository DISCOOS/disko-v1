package org.redcross.sar.map;

import com.esri.arcgis.geodatabase.IFeature;

public interface IEditFeedback {
	
	public void editStarted(IFeature editFeature);
	
	public void editFinished(IFeature editFeature);

}
