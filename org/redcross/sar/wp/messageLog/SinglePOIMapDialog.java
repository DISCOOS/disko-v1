package org.redcross.sar.wp.messageLog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.map.IDiskoMap;
import org.redcross.sar.map.POITool;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.mso.data.IMessageLineIf.MessageLineType;
import org.redcross.sar.mso.data.IPOIIf.POIType;

import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.interop.AutomationException;

public class SinglePOIMapDialog extends DiskoDialog implements IEditMessageDialogIf
{
	protected IDiskoWpMessageLog m_wpMessageLog = null;
	protected IDiskoMap m_map = null;
	protected POIType m_poiType = null;
	protected POITool m_tool = null;
	
	public SinglePOIMapDialog(IDiskoWpMessageLog wp)
	{
		super(wp.getApplication().getFrame());
		
		m_wpMessageLog = wp;
		
		try
		{
			m_tool = new POITool(wp.getApplication());
			m_tool.getDialog().setVisible(false);
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initMap();
	}
	
	private void initMap()
	{
		m_map = m_wpMessageLog.getMap().getMapManager().getMapInstance();
		this.add((JComponent)m_map);
	}

	public void clearContents()
	{
		// TODO Auto-generated method stub
		
	}
	
	public void hideDialog()
	{
		try
		{
			m_map.setActiveTool(null);
		} 
		catch (AutomationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setVisible(false);
	}

	public void newMessageSelected(IMessageIf message)
	{
		zoomToPOI();
	}

	public void showDialog()
	{	
		try
		{
			m_map.setActiveTool(m_tool);
		} 
		catch (AutomationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setVisible(true);
	}
	
	private void zoomToPOI()
	{
		// Update position
		IMessageIf message = MessageLogTopPanel.getCurrentMessage();
		IMessageLineIf messageLine = null;
		IPOIIf poi = null;
		if(m_poiType == null || m_poiType == POIType.GENERAL)
		{
			// The general POI of the message should be displayed
			messageLine = message.findMessageLine(MessageLineType.POI, false);
		}
		else
		{
			// Displaying the finding of a message
			messageLine = message.findMessageLine(MessageLineType.FINDING, false);
		}
		
		if(messageLine != null)
		{
			poi = messageLine.getLinePOI();
		}
		
		// If point is specified in current message, set position in map
		if(poi != null)
		{
			try
			{
				//m_map.setSelected(poi, true);
				//m_map.zoomToSelected();
				m_map.zoomToMsoObject(poi);
			} 
			catch(AutomationException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch(IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
