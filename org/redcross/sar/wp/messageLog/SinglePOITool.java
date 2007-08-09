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

	public SinglePOITool(IDiskoApplication app, SinglePOIMapDialog dialog, boolean position) throws IOException
	{
		this.m_app = app;
		// TODO Auto-generated constructor stub
		
		m_wpMessageLog = dialog.getWP();
		
		m_positionMessageLine = position;
		
		this.dialog = dialog;
	}
	
	public void setMap(DiskoMap map)
	{
		this.map = map; 
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
	
	public void onMouseUp(int button, int shift, int x, int y) throws IOException, AutomationException 
	{
		// Update/add POI in current message
		IMessageIf message = MessageLogTopPanel.getCurrentMessage();
		IMessageLineIf messageLine = message.findMessageLine(MessageLineType.POI, true);
		IPOIIf poi = messageLine.getLinePOI();
		if(poi == null)
		{
			poi = m_wpMessageLog.getMsoManager().createPOI();
			
		}
		
		Point point = new Point();
		point = transform(x, y);
		point.setSpatialReferenceByRef(map.getSpatialReference());
		
		poi.setType(((SinglePOIMapDialog)dialog).getType());
		poi.setPosition(MapUtil.getMsoPosistion(point));
	}
}
