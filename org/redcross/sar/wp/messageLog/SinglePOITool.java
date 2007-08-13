package org.redcross.sar.wp.messageLog;

import java.io.IOException;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.POIDialog;
import org.redcross.sar.map.AbstractCommandTool;
import org.redcross.sar.map.DiskoMap;
import org.redcross.sar.map.IDiskoMap;
import org.redcross.sar.map.MapUtil;
import org.redcross.sar.map.POITool;
import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.map.layer.OperationAreaLayer;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.mso.data.IMessageLineIf.MessageLineType;
import org.redcross.sar.mso.data.IPOIIf.POIType;

import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.interop.AutomationException;

/**
 * Class POITool adds a POI when mouse is clicked, in the message edit dialog only a single POI should be added/manipulated
 * Redesign:	Common abstract class for POI tools
 * 				Common abstract class for tool property dialogs, should not be bound to POIDialog layout/contents
 * 
 * @author thomasl
 *
 */
public class SinglePOITool extends AbstractCommandTool
{

	protected IDiskoApplication m_app;
	protected IDiskoWpMessageLog m_wpMessageLog;
	protected boolean m_positionMessageLine = true;

	public SinglePOITool(IDiskoApplication app, MessagePOIDialog dialog, boolean position) throws IOException
	{
		this.m_app = app;
		// TODO Auto-generated constructor stub
		
		m_wpMessageLog = dialog.getWP();
		
		m_positionMessageLine = position;
		
		this.dialog = dialog;
	}
	
	public void setMap(IDiskoMap map)
	{
		this.map = (DiskoMap)map; 
	}
	
	public void onCreate(Object obj) throws IOException, AutomationException 
	{
		if (obj instanceof IDiskoMap) {
			map = (DiskoMap)obj;
			
			opAreaLayer = (OperationAreaLayer) map.getMapManager().getMsoLayer(
					IMsoFeatureLayer.LayerCode.AREA_LAYER);
		}
	}
	
	public void onMouseDown(int button, int shift, int x, int y) throws IOException, AutomationException {}	
	
	/**
	 * Update message POI(s) when map position is clicked. Event causes relevant dialogs to update, no need for
	 * explicit update
	 */
	public void onMouseUp(int button, int shift, int x, int y) throws IOException, AutomationException 
	{
		
		IMessageIf message = MessageLogTopPanel.getCurrentMessage();
		IMessageLineIf messageLine;
		
		if(m_positionMessageLine)
		{
			// Update/add POI in current message
			messageLine = message.findMessageLine(MessageLineType.POI, true);
		}
		else
		{
			// Update finding in message
			messageLine = message.findMessageLine(MessageLineType.FINDING, true);
		}
		
		IPOIIf poi = messageLine.getLinePOI();
		if(poi == null)
		{
			poi = m_wpMessageLog.getMsoManager().createPOI();
			messageLine.setLinePOI(poi);
		}
		
		POIType type = ((MessagePOIDialog)dialog).getSelectedPOIType();
		poi.setType(type);

		// Update position
		Point point = new Point();
		point = transform(x, y);
		point.setSpatialReferenceByRef(map.getSpatialReference());
		poi.setPosition(MapUtil.getMsoPosistion(point));
	}
}
