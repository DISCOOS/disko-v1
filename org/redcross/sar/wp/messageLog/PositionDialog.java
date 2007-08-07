package org.redcross.sar.wp.messageLog;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.POIDialog;
import org.redcross.sar.map.POITool;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IPOIIf.POIType;

public class PositionDialog extends POIDialog implements IEditMessageDialogIf
{
	IDiskoApplication m_application;
	POITool m_tool;
	public PositionDialog(IDiskoWpMessageLog wp, POITool tool)
	{
		super(wp.getApplication(), tool);
		m_application = wp.getApplication();
		m_tool = tool;
		
		POIType[] types = {POIType.GENERAL};
		this.setTypes(types);
	}

	public void updateContents(IMessageIf message) 
	{
		// TODO Auto-generated method stub	
	}

	public void clearPanelContents()
	{
		// TODO Auto-generated method stub
		
	}

	public void hideDialog()
	{
		this.setVisible(false);
	}

	public void newMessageSelected(IMessageIf message)
	{
		// TODO Auto-generated method stub
		
	}

	public void showDialog()
	{
		this.setVisible(true);
	}

	public void clearContents()
	{
		// TODO Auto-generated method stub
		
	}

}
