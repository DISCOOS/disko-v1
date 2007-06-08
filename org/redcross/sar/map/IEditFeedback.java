package org.redcross.sar.map;

import org.redcross.sar.map.feature.IMsoFeature;

public interface IEditFeedback {
	
	public void editStarted();
	
	public void editFinished(IMsoFeature editFeature);

}
