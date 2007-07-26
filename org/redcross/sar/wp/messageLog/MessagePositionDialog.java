package org.redcross.sar.wp.messageLog;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.POIDialog;
import org.redcross.sar.map.POITool;
import org.redcross.sar.mso.data.IMessageIf;

public class MessagePositionDialog extends POIDialog implements IEditMessageDialogIf
{
	IDiskoApplication m_application;
	POITool m_tool;
	public MessagePositionDialog(IDiskoWpMessageLog wp, POITool tool)
	{
		super(wp.getApplication(), tool);
		m_application = wp.getApplication();
		m_tool = tool;
	}

	public void updateContents(IMessageIf message) 
	{
		// TODO Auto-generated method stub	
	}

	public void clearPanelContents()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hideDialog()
	{
		this.setVisible(false);
	}

	@Override
	public void newMessageSelected(IMessageIf message)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showDialog()
	{
		this.setVisible(true);
	}

	@Override
	public void clearContents()
	{
		// TODO Auto-generated method stub
		
	}

}
