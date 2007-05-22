package org.redcross.sar.map;

import org.redcross.sar.map.layer.MsoFeature;

public interface IEditFeedback {
	
	public void editStarted(MsoFeature msoFeature);
	
	public void editFinished(MsoFeature msoFeature);

}
