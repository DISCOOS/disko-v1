package org.redcross.sar.gui.models;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
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
	private ArrayList<Object> hypotheses = null;

	public HypothesesListModel(IMsoModelIf msoModel) {
		myInterests = EnumSet.of(IMsoManagerIf.MsoClassCode.CLASSCODE_HYPOTHESIS);
		hypotheses = new ArrayList<Object>();
		
		IMsoEventManagerIf msoEventManager = msoModel.getEventManager();
		msoEventManager.addClientUpdateListener(this);
		
		// pupulates from mso
		ICmdPostIf cmdPost = msoModel.getMsoManager().getCmdPost();
		Iterator iter = cmdPost.getHypothesisListItems().iterator();
		while (iter.hasNext()) {
			hypotheses.add(iter.next());
		}
		super.fireContentsChanged(this, 0, hypotheses.size()-1);
	}

	public void handleMsoUpdateEvent(Update e) {
		int type = e.getEventTypeMask();
		if (type == EventType.ADDED_REFERENCE_EVENT.maskValue()) {
			hypotheses.add(e.getSource());
			int index = hypotheses.size()-1;
			super.fireContentsChanged(this, index, index);
		}
		else if (type == EventType.REMOVED_REFERENCE_EVENT.maskValue()) {
			hypotheses.remove(e.getSource());
			super.fireContentsChanged(this, 0, hypotheses.size()-1);
		}
	}

	public boolean hasInterestIn(IMsoObjectIf aMsoObject) {
		return myInterests.contains(aMsoObject.getMsoClassCode());
	}

	public Object getElementAt(int index) {
		if (index >= 0 && index < hypotheses.size()) {
			return hypotheses.get(index);
		}
		return null;
	}

	public int getSize() {
		return hypotheses.size();
	}
}
