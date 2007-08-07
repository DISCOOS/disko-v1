package org.redcross.sar.wp.messageLog;

import java.awt.Frame;

import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.POIDialog;
import org.redcross.sar.map.POITool;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IPOIIf.POIType;

public class FindingDialog extends POIDialog implements IEditMessageDialogIf
{

	public FindingDialog(IDiskoWpMessageLog wp, POITool tool)
	{
		super(wp.getApplication(), tool);
		// TODO Auto-generated constructor stub
		
		POIType[] types = {POIType.FINDING};
		this.setTypes(types);
	}

	public void clearContents()
	{
		// TODO Auto-generated method stub
		
	}

	public void hideDialog()
	{
		// TODO Auto-generated method stub
		
	}

	public void newMessageSelected(IMessageIf message)
	{
		// TODO Auto-generated method stub
		
	}

	public void showDialog()
	{
		// TODO Auto-generated method stub
		
	}
}
