package org.redcross.sar.wp.messageLog;

import com.esri.arcgis.interop.AutomationException;

import org.redcross.sar.gui.DiskoButtonFactory;
import org.redcross.sar.gui.MGRSField;
import org.redcross.sar.gui.NumPadDialog;
import org.redcross.sar.gui.DiskoButtonFactory.ButtonType;
import org.redcross.sar.gui.renderers.SimpleListCellRenderer;
import org.redcross.sar.map.IDiskoMap;
import org.redcross.sar.map.MapUtil;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.ITaskIf;
import org.redcross.sar.mso.data.IMessageLineIf.MessageLineType;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.mso.data.IPOIIf.POIType;
import org.redcross.sar.mso.data.ITaskIf.TaskType;
import org.redcross.sar.util.mso.Position;

import javax.swing.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Dialog used in position and finding when editing the message log. A separate POI dialog class was created for
 * the message log, could be merged with POIDialog / extract a common super class, if this should prove beneficial
 * 
 * @author thomasl
 */
public class MessagePOIPanel extends JPanel implements IEditMessageComponentIf
{
	private static final long serialVersionUID = 1L;
	
	protected JButton m_okButton = null;
	protected JButton m_cancelButton = null;
	protected MGRSField m_mgrsField;
	protected JLabel m_poiTypeLabel = null;
	protected JComboBox m_poiTypesComboBox = null;
	protected POIType[] m_poiTypes = null;
	protected IDiskoWpMessageLog m_wpMessageLog = null;
	protected SinglePOITool m_tool = null;
	protected boolean m_notebookMode = true;

	/**
	 * @param wp Message log work process
	 * @param poiTypes Which POI types are valid in panel
	 */
	public MessagePOIPanel(IDiskoWpMessageLog wp, POIType[] poiTypes)
	{
		m_wpMessageLog = wp;
		m_poiTypes = poiTypes;
		initialize();
	}

	private void initialize()
	{
		initButtons();
		initContents();

		try
		{
			m_tool = new SinglePOITool(m_wpMessageLog, this);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		m_tool.setMap(m_wpMessageLog.getMap());

		this.hideComponent();
	}

	/**
	 * Update POI position and type in current message based on values in GUI fields
	 */
	private void updatePOI()
	{
		IMessageIf message = MessageLogBottomPanel.getCurrentMessage(true);
		Position newPosition = null;
		
		// Get message line
		IMessageLineIf messageLine = null;
		if(m_poiTypes == null)
		{
			messageLine = message.findMessageLine(MessageLineType.POI, true);
		}
		else
		{
			messageLine = message.findMessageLine(MessageLineType.FINDING, true);
		}

		// Create new POI and message line if POI message line did not exists
		IPOIIf poi = messageLine.getLinePOI();
		if(poi == null)
		{
			poi = m_wpMessageLog.getMsoManager().createPOI();
			messageLine.setLinePOI(poi);
		}		
		
		// Update POI
		poi.suspendNotify();
		
		try
		{
			newPosition = MapUtil.getPositionFromMGRS(m_mgrsField.getText());
			poi.setPosition(newPosition);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		poi.setType(getSelectedPOIType());
		poi.resumeNotify();
	}
	
	/**
	 * Reverts contents of text fields to what is stored in MSO
	 */
	private void revertPOI()
	{
		IMessageIf message = MessageLogBottomPanel.getCurrentMessage(false);
		if(message != null)
		{
			IMessageLineIf line = null;
			if(m_poiTypes == null)
			{
				line = message.findMessageLine(MessageLineType.POI, false);
			}
			else
			{
				line = message.findMessageLine(MessageLineType.FINDING, false);
			}
			
			if(line != null)
			{
				IPOIIf poi = line.getLinePOI();
				updateMGRSField(poi);
			}
		}
	}

	private void initButtons()
	{
		m_okButton = DiskoButtonFactory.createSmallButton(ButtonType.OkButton);
		m_okButton.addActionListener(new ActionListener()
		{
			/**
			 * Add/update POI in current message
			 */
			public void actionPerformed(ActionEvent e)
			{
				NumPadDialog numPad = m_wpMessageLog.getApplication().getUIFactory().getNumPadDialog();
				numPad.setVisible(false);
				updatePOI();
				
				MessageLogBottomPanel.showListPanel();
			}
		});

		m_cancelButton = DiskoButtonFactory.createSmallButton(ButtonType.CancelButton);
		m_cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				NumPadDialog numPad = m_wpMessageLog.getApplication().getUIFactory().getNumPadDialog();
				numPad.setVisible(false);
				revertPOI();
			}
		});
	}

	/**
	 * Set GUI according to POI types
	 */
	private void updatePOITypes()
	{
		if(m_poiTypes == null)
		{
			m_poiTypeLabel.setVisible(false);
			m_poiTypesComboBox.removeAllItems();
			m_poiTypesComboBox.setVisible(false);
		}
		else
		{
			for(POIType type : m_poiTypes)
			{
				m_poiTypesComboBox.addItem(type);
			}
			m_poiTypeLabel.setVisible(true);
			m_poiTypesComboBox.setSelectedItem(m_poiTypes[0]);
			m_poiTypesComboBox.setVisible(true);
		}
	}

	private void initContents()
	{	
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		
		// MGRS
		m_mgrsField = new MGRSField(m_wpMessageLog.getApplication());
		this.add(m_mgrsField, gbc);
		gbc.gridy++;
		
		// Combo-box
		gbc.gridy++;
		JPanel typePanel = new JPanel();
		m_poiTypeLabel = new JLabel(m_wpMessageLog.getText("Type.text"));
		typePanel.add(m_poiTypeLabel);
		m_poiTypesComboBox = new JComboBox();
		m_poiTypesComboBox.setRenderer(new SimpleListCellRenderer());
		m_poiTypesComboBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// Set finding POI type
				JComboBox cb = (JComboBox)e.getSource();
				POIType type = (POIType)cb.getSelectedItem();

				IMessageIf message = MessageLogBottomPanel.getCurrentMessage(false);
				if(message != null)
				{
					IMessageLineIf messageLine = message.findMessageLine(MessageLineType.FINDING, false);
					if(messageLine != null)
					{
						// Update type
						IPOIIf poi = messageLine.getLinePOI();
						if(poi == null)
						{
							poi = m_wpMessageLog.getMsoManager().createPOI();
							messageLine.setLinePOI(poi);
						}
						
						// Update type
						poi.setType(type);
						
						// Update related intelligence task
						String taskText = m_wpMessageLog.getText("FindingFinding.text").split(":")[0];
						for(ITaskIf messageTask : message.getMessageTasksItems())
						{
							if(messageTask.getType() == TaskType.INTELLIGENCE)
							{
								if(taskText.equals(messageTask.getTaskText().split(":")[0]))
								{
									String findingText = null;
									if(type == POIType.FINDING)
									{
										findingText = String.format(m_wpMessageLog.getText("TaskSubType.FINDING.text"),
												m_wpMessageLog.getText("Finding.text"));
									}
									else
									{
										findingText = String.format(m_wpMessageLog.getText("TaskSubType.FINDING.text"),
												m_wpMessageLog.getText("SilentWitness.text"));
									}
									messageTask.setTaskText(findingText);
								}
							}
						}
					}
				}
			}
		});
		typePanel.add(m_poiTypesComboBox);
		this.add(typePanel, gbc);
		updatePOITypes();
		
		// Action buttons
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.gridy = 0;
		gbc.gridx++;
		gbc.gridheight = 2;
		this.add(m_cancelButton, gbc);
		gbc.gridy += 2;
		this.add(m_okButton, gbc);
	}

	public void clearContents()
	{
		m_mgrsField.setText("");
	}

	public void hideComponent()
	{
		NumPadDialog numPad = m_wpMessageLog.getApplication().getUIFactory().getNumPadDialog();
		numPad.setVisible(false);
		this.setVisible(false);
        MessageLogPanel.hideMap();
        showPOI(false);
    }

	private void updateFields(IMessageIf message)
	{
		IMessageLineIf messageLine = null;
		if(m_poiTypes == null)
		{
			messageLine = message.findMessageLine(MessageLineType.POI, false);
		}
		else
		{
			messageLine = message.findMessageLine(MessageLineType.FINDING, false);
		}

		try
		{
			// Update components
			if(messageLine == null)
			{
				// Message don't have a POI message line
				m_mgrsField.setText("");
			}
			else
			{
				IPOIIf poi = messageLine.getLinePOI();
				if(poi != null)
				{
					if(poi.getPosition() == null)
					{
						m_mgrsField.setText("");
					}
					else
					{
						updateMGRSField(poi);
					}
				}
			}
		}
		catch(Exception e){}
	}
	
	private void updateMGRSField(IPOIIf poi)
	{
		String mgrs = null;
		try
		{
			mgrs = MapUtil.getMGRSfromPosition(poi.getPosition());
		} 
		catch (AutomationException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		m_mgrsField.setText(mgrs);
	}

	private void updateComboBox(IMessageIf message)
	{
		if(m_poiTypes != null)
		{
			IMessageLineIf messageLine = message.findMessageLine(MessageLineType.FINDING, false);
			if(messageLine != null)
			{
				IPOIIf poi = messageLine.getLinePOI();
				if(poi != null)
				{
					POIType type = messageLine.getLinePOI().getType();
					m_poiTypesComboBox.setSelectedItem(type);
				}
			}
		}
	}

	/**
	 * Update position fields with message POI position. Zoom to POI
	 */
	public void newMessageSelected(IMessageIf message)
	{
		// Update dialog
		updateComboBox(message);
		updateFields(message);
	}

	/**
	 * Show map if show in map button is selected
	 */
	public void showComponent()
	{
		MessageLogPanel.showMap();
		showPOI(true);
		
		this.setVisible(true);
	}

	/**
	 * Get the message log work process
	 */
	public IDiskoWpMessageLog getWP()
	{
		return m_wpMessageLog;
	}

	/**
	 * @return POI type selected in combo box
	 */
	public POIType getSelectedPOIType()
	{
		if(m_poiTypes == null)
		{
			return POIType.GENERAL;
		}
		else
		{
			return  (POIType)m_poiTypesComboBox.getSelectedItem();
		}
	}
	
	/**
	 * Set the tool for the current work process map
	 */
	public void setMapTool()
	{
		IDiskoMap map = m_wpMessageLog.getMap();
		try
		{
			map.setCurrentToolByRef(m_tool);
		} 
		catch (AutomationException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Set selection for POI in map
	 */
	public void showPOI(boolean select)
	{
		// Get POI
		IMessageIf message = MessageLogBottomPanel.getCurrentMessage(false);
		if(message != null)
		{
			IPOIIf poi = null;
			if(m_poiTypes == null)
			{
				IMessageLineIf poiLine = message.findMessageLine(MessageLineType.POI, false);
				if(poiLine != null)
				{
					poi = poiLine.getLinePOI();
				}
			}
			else
			{
				IMessageLineIf findingLine = message.findMessageLine(MessageLineType.FINDING, false);
				if(findingLine != null)
				{
					poi = findingLine.getLinePOI();
				}
			}
			
			// Select POI object in map
			if(poi != null)
			{
				try
				{
					m_wpMessageLog.getMap().setSelected(poi, select);
					m_wpMessageLog.getMap().zoomToMsoObject(poi);
				} 
				catch (AutomationException e1)
				{
					e1.printStackTrace();
				} 
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}	
		}
	}
}
