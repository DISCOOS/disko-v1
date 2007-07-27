package org.redcross.sar.wp.messageLog;

import java.awt.Frame;

import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.POIDialog;
import org.redcross.sar.map.POITool;
import org.redcross.sar.mso.data.IPOIIf.POIType;

public class MessageFindingDialog extends POIDialog
{

	public MessageFindingDialog(IDiskoWpMessageLog wp, POITool tool)
	{
		super(wp.getApplication(), tool);
		// TODO Auto-generated constructor stub
		
		POIType[] types = {POIType.FINDING};
		this.setTypes(types);
	}
}
