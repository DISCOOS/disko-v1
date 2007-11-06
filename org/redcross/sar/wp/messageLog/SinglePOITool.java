package org.redcross.sar.wp.messageLog;

import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.interop.AutomationException;
import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.map.AbstractCommandTool;
import org.redcross.sar.map.DiskoMap;
import org.redcross.sar.map.IDiskoMap;
import org.redcross.sar.map.MapUtil;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.IMessageLineIf.MessageLineType;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.mso.data.IPOIIf.POIType;
import org.redcross.sar.mso.data.ITaskIf;
import org.redcross.sar.mso.data.ITaskIf.TaskPriority;
import org.redcross.sar.mso.data.ITaskIf.TaskType;

import java.io.IOException;
import java.util.Calendar;

/**
 * Class POITool adds a POI when mouse is clicked, in the message edit dialog only a single POI should be added/manipulated
 * Redesign:	Common abstract class for POI tools
 * 				Common abstract class for tool property dialogs, should not be bound to POIDialog layout/contents
 *
 * @author thomasl
 */
public class SinglePOITool extends AbstractCommandTool
{
	private final static long serialVersionUID = 1L;

	protected IDiskoApplication m_app;
	protected IDiskoWpMessageLog m_wpMessageLog;
	protected MessagePOIPanel m_panel;

	/**
	 * @param wp Message log work process
	 * @param panel Panel controlling tool options
	 * @throws IOException
	 */
	public SinglePOITool(IDiskoWpMessageLog wp, MessagePOIPanel panel) throws IOException
	{
		m_wpMessageLog = wp;
		this.m_app = wp.getApplication();
		m_panel = panel;
	}

	/**
	 * Set map tool is operating on
	 * @param map
	 */
	public void setMap(IDiskoMap map)
	{
		this.map = (DiskoMap)map;
	}

	/**
	 *
	 */
	@Override
	public void onCreate(Object obj) throws IOException, AutomationException
	{
		if (obj instanceof IDiskoMap) {
			map = (DiskoMap)obj;
		}
	}

	/**
	 *
	 */
	@Override
	public void onMouseDown(int button, int shift, int x, int y) throws IOException, AutomationException {}

	/**
	 * Update message POI(s) when map position is clicked. Event causes relevant dialogs to update, no need for
	 * explicit update
	 */
	@Override
	public void onMouseUp(int button, int shift, int x, int y) throws IOException, AutomationException
	{

		IMessageIf message = MessageLogBottomPanel.getCurrentMessage(true);
		IMessageLineIf messageLine;

		POIType type = m_panel.getSelectedPOIType();
		if(type == POIType.GENERAL)
		{
			// Update/add POI in current message
			messageLine = message.findMessageLine(MessageLineType.POI, true);
		}
		else
		{
			// Update finding in message
			messageLine = message.findMessageLine(MessageLineType.FINDING, true);

			// Need to add/update task
			ITaskIf task = null;

			String findingText = m_wpMessageLog.getText("TaskSubType.FINDING.text");
			// Check for existing tasks
			for(ITaskIf messageTask : message.getMessageTasksItems())
			{
				if(messageTask.getType() == TaskType.INTELLIGENCE)
				{
					// Check to see if task is a finding task
					String taskText = messageTask.getTaskText().split(":")[0];
					if(taskText.equals(findingText.split(":")[0]))
					{
						// Message has a finding task, update this
						task = messageTask;
					}
				}
			}

			// If message does not have a finding task, create new
			if(task == null)
			{
				task = m_wpMessageLog.getMsoManager().createTask(Calendar.getInstance());
				task.setCreated(Calendar.getInstance());
				task.setAlert(Calendar.getInstance());
				task.setPriority(TaskPriority.HIGH);
				task.setResponsibleRole(null);
				task.setType(TaskType.INTELLIGENCE);
				task.setSourceClass(message.getMsoClassCode());
				task.setCreatingWorkProcess(m_wpMessageLog.getName());
//				task.setDependentObject(message.getSender());
				message.addMessageTask(task);
			}

			// Update task fields
			if(type == POIType.FINDING)
			{
				task.setTaskText(String.format(findingText, m_wpMessageLog.getText("Finding.text")));
			}
			else
			{
				task.setTaskText(String.format(findingText, m_wpMessageLog.getText("SilentWitness.text")));
			}
		}

		IPOIIf poi = messageLine.getLinePOI();
		if(poi == null)
		{
			poi = m_wpMessageLog.getMsoManager().createPOI();
			messageLine.setLinePOI(poi);
		}

		poi.setType(type);

		// Update position
		Point point = new Point();
		point = transform(x, y);
		point.setSpatialReferenceByRef(map.getSpatialReference());
		poi.setPosition(MapUtil.getMsoPosistion(point));
	}
}
