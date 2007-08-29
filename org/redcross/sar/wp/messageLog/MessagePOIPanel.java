package org.redcross.sar.wp.messageLog;

import com.esri.arcgis.interop.AutomationException;
import org.redcross.sar.gui.renderers.SimpleListCellRenderer;
import org.redcross.sar.map.IDiskoMap;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.IMessageLineIf.MessageLineType;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.mso.data.IPOIIf.POIType;
import org.redcross.sar.util.mso.Position;

import javax.swing.*;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.IOException;

/**
 * Dialog used in position and finding when editing the message log. A separate POI dialog class was created for
 * the message log, should be merged with POIDialog / extract a common super class, if this should prove beneficial
 *
 * @author thomasl
 *
 */
public class MessagePOIPanel extends JPanel implements IEditMessageComponentIf
{
	private static final long serialVersionUID = 1L;
	
//	protected JPanel m_contentsPanel = null;
	protected JButton m_okButton = null;
	protected JButton m_cancelButton = null;
	protected JToggleButton m_showInMapButton = null;
	protected JLabel m_xLabel = null;
	protected JLabel m_yLabel = null;
	protected JTextField m_xField = null;
	protected JTextField m_yField = null;
	protected JLabel m_poiTypeLabel = null;
	protected JComboBox m_poiTypesComboBox = null;
	protected POIType[] m_poiTypes = null;
	protected IDiskoWpMessageLog m_wpMessageLog = null;
	protected SinglePOITool m_tool = null;

	public MessagePOIPanel(IDiskoWpMessageLog wp, POIType[] poiTypes)
	{
		m_wpMessageLog = wp;
		
		m_poiTypes = poiTypes;
		
		initialize();
	}

	private void initialize()
	{
		// TODO numpads?

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

	private void updatePOI()
	{
		IMessageIf message = MessageLogTopPanel.getCurrentMessage();
		double xCoordinate = 0.0;
		try
		{
			xCoordinate = Double.valueOf(m_xField.getText());
		}
		catch(NumberFormatException nfe)
		{
			xCoordinate = 0.0;
		}

		double yCoordinate = 0.0;
		try
		{
			yCoordinate = Double.valueOf(m_yField.getText());
		}
		catch(NumberFormatException nfe)
		{
			yCoordinate = 0.0;
		}

		// Get line
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
		poi.setType(getSelectedPOIType());
		Position position = poi.getPosition();
		if(position == null)
		{
			String id = m_wpMessageLog.getMsoModel().getModelDriver().makeObjectId().getId();
			position = new Position(id, xCoordinate, yCoordinate);
			poi.setPosition(position);
		}
		else
		{
			position.setPosition(xCoordinate, yCoordinate);
		}
	}

	private void initButtons()
	{
		m_okButton = DiskoButtonFactory.createSmallOKButton();
		m_okButton.addActionListener(new ActionListener()
		{
			/**
			 * Add/update POI in current message
			 */
			public void actionPerformed(ActionEvent e)
			{

				updatePOI();
//				fireDialogFinished();
			}
		});

		m_cancelButton = DiskoButtonFactory.createSmallCancelButton();
		m_cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
//				fireDialogCanceled();
			}
		});

		m_showInMapButton = DiskoButtonFactory.createSmallToggleButton(m_wpMessageLog.getText("ShowInMapButton.text"),
				m_wpMessageLog.getText("ShowInMapButton.icon"));
		m_showInMapButton.setAlignmentY(Component.TOP_ALIGNMENT);
		m_showInMapButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// Show/hide map
				JToggleButton button = (JToggleButton)e.getSource();
				if(button.isSelected())
				{
					MessageLogPanel.showMap();
				}
				else
				{
					MessageLogPanel.hideMap();
				}
			}
		});
	}

	/**
	 * Set GUI according to poi types
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
//		m_contentsPanel = new JPanel();
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;

		// X
		JPanel xPanel = new JPanel();
		m_xLabel = new JLabel("X");
		xPanel.add(m_xLabel);
		m_xField = new JTextField(12);
		m_xField.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				updatePOI();
			}
		});
		xPanel.add(m_xField, gbc);
		this.add(xPanel, gbc);

		// Y
		gbc.gridy++;
		JPanel yPanel = new JPanel();
		m_yLabel = new JLabel("Y");
		yPanel.add(m_yLabel);
		m_yField = new JTextField(12);
		m_yField.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				updatePOI();
			}
		});
		yPanel.add(m_yField, gbc);
		this.add(yPanel, gbc);

		// Combo box
		gbc.gridy++;
		JPanel typePanel = new JPanel();
		m_poiTypeLabel = new JLabel("Type");
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

				IMessageIf message = MessageLogTopPanel.getCurrentMessage();
				IMessageLineIf messageLine = message.findMessageLine(MessageLineType.FINDING, false);
				if(messageLine != null)
				{
					IPOIIf poi = messageLine.getLinePOI();
					if(poi == null)
					{
						poi = m_wpMessageLog.getMsoManager().createPOI();
						messageLine.setLinePOI(poi);
					}

					poi.setType(type);
				}
			}
		});
		typePanel.add(m_poiTypesComboBox);
		this.add(typePanel, gbc);
		updatePOITypes();

		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.gridy = 0;
		gbc.gridx++;
		gbc.gridheight = 3;
		JPanel showInMapPanel = new JPanel();
		showInMapPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 20));
		showInMapPanel.add(m_showInMapButton);
		this.add(showInMapPanel, gbc);
		
		// Action buttons
		gbc.gridheight = 2;
		gbc.gridx++;
		this.add(m_cancelButton, gbc);
		gbc.gridy += 2;
		this.add(m_okButton, gbc);
	}

	public void clearContents()
	{
		m_xField.setText("");
		m_yField.setText("");
	}

	public void hideComponent()
	{
		this.setVisible(false);
        MessageLogPanel.hideMap();
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
				m_xField.setText("");
				m_yField.setText("");
			}
			else
			{
				IPOIIf poi = messageLine.getLinePOI();
				if(poi != null)
				{
					Position position = poi.getPosition();
					if(position == null)
					{
						m_xField.setText("");
						m_yField.setText("");
					}
					else
					{
						Point2D.Double point = position.getPosition();
						m_xField.setText(String.valueOf(point.x));
						m_yField.setText(String.valueOf(point.y));
					}
				}
			}
		}
		catch(Exception e){}
	}

	private void updateComboBox(IMessageIf message)
	{
		if(m_poiTypes != null)
		{
			IMessageLineIf messageLine = message.findMessageLine(MessageLineType.FINDING, false);
			if(messageLine != null)
			{
				IPOIIf poi = messageLine.getLinePOI();
				// TODO should not occur, remove test when fixed (thomas, aka gro, 24.08.07)
				if(poi != null)
				{
					POIType type = messageLine.getLinePOI().getType();
					m_poiTypesComboBox.setSelectedItem(type);
				}
			}
		}
	}

	public void newMessageSelected(IMessageIf message)
	{
		// Update dialog
		updateComboBox(message);
		updateFields(message);

		// Zoom to POI in map
		IMessageLineIf messageLine = null;
		if(m_poiTypes == null)
		{
			messageLine = message.findMessageLine(MessageLineType.POI, false);
		}
		else
		{
			messageLine = message.findMessageLine(MessageLineType.FINDING, false);
		}

		if(messageLine != null)
		{
			IPOIIf poi = messageLine.getLinePOI();
			if(poi != null)
			{
				try
				{
					MessageLogPanel.getMap().zoomToMsoObject(poi);
				}
				catch (AutomationException e){}
				catch (IOException e){}
			}
		}
	}

	public void showComponent()
	{
		if(m_showInMapButton.isSelected())
		{
			MessageLogPanel.showMap();
		}
		
		this.setVisible(true);
	}
	
//	/**
//	 * Pan to current POI object in map
//	 */
//	private void panToPOI()
//	{
//		// Pan to object
//		POIType type = getSelectedPOIType();
//		IMessageLineIf line = null;
//		IPOIIf poi = null;
//		if(type == POIType.GENERAL)
//		{
//			line = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.POI, false);
//		}
//		else
//		{
//			line = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.FINDING, false);
//		}
//
//		if(line != null)
//		{
//			poi = line.getLinePOI();
//
//			if(poi != null)
//			{
//				IDiskoMap map = m_wpMessageLog.getMap();
//				try
//				{
//					map.zoomToMsoObject(poi);
//					System.err.print("zoom");
//				} 
//				catch (AutomationException e)
//				{
//					e.printStackTrace();
//				} 
//				catch (IOException e)
//				{
//					e.printStackTrace();
//				}
//			}
//		}
//	}

	public IDiskoWpMessageLog getWP()
	{
		return m_wpMessageLog;
	}

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
	 * Set the tool for the current wp map
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
}
