package org.redcross.sar.gui.models;

import java.util.EnumSet;

import javax.swing.AbstractListModel;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.EventType;
import org.redcross.sar.mso.event.MsoEvent.Update;

public class HypothesesListModel extends AbstractListModel implements
		IMsoUpdateListenerIf {

	private static final long serialVersionUID = 1L;
	private EnumSet<IMsoManagerIf.MsoClassCode> myInterests = null;
	private ICmdPostIf cmdPost = null;
	private Object[] data = null;

	public HypothesesListModel(IMsoModelIf msoModel) {
		myInterests = EnumSet.of(IMsoManagerIf.MsoClassCode.CLASSCODE_HYPOTHESIS);
		IMsoEventManagerIf msoEventManager = msoModel.getEventManager();
		msoEventManager.addClientUpdateListener(this);
		cmdPost = msoModel.getMsoManager().getCmdPost();
		data = cmdPost.getHypothesisListItems().toArray();
		super.fireContentsChanged(this, 0, data.length-1);
	}

	public void handleMsoUpdateEvent(Update e) {
		int type = e.getEventTypeMask();
		if (type == EventType.ADDED_REFERENCE_EVENT.maskValue() ||
				type == EventType.REMOVED_REFERENCE_EVENT.maskValue() ||
				type == EventType.MODIFIED_DATA_EVENT.maskValue()) {
			data = cmdPost.getHypothesisListItems().toArray();
			super.fireContentsChanged(this, 0, data.length-1);
		}
	}

	public boolean hasInterestIn(IMsoObjectIf aMsoObject) {
		return myInterests.contains(aMsoObject.getMsoClassCode());
	}

	public Object getElementAt(int index) {
		return data[index];
	}

	public int getSize() {
		return data.length;
	}
}
