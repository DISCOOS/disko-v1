package org.redcross.sar.wp.messageLog;

import com.esri.arcgis.interop.AutomationException;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.renderers.SimpleListCellRenderer;
import org.redcross.sar.map.IDiskoMap;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.IMessageLineIf.MessageLineType;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.mso.data.IPOIIf.POIType;
import org.redcross.sar.util.mso.Position;

import javax.swing.*;
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
public class MessagePOIDialog extends DiskoDialog implements IEditMessageDialogIf
{
	protected JPanel m_contentsPanel = null;
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

	public MessagePOIDialog(IDiskoWpMessageLog wp, POIType[] poiTypes)
	{
		super(wp.getApplication().getFrame());

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
			m_tool = new SinglePOITool(m_wpMessageLog.getApplication(), this);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		m_tool.setMap(m_wpMessageLog.getMap());

		this.hideDialog();
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
				fireDialogFinished();
			}
		});

		m_cancelButton = DiskoButtonFactory.createSmallCancelButton();
		m_cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				fireDialogCanceled();
			}
		});

		m_showInMapButton = DiskoButtonFactory.createSmallToggleButton(m_wpMessageLog.getText("ShowInMapButton.text"),
				m_wpMessageLog.getText("ShowInMapButton.icon"));
		m_showInMapButton.setAlignmentY(JComponent.TOP_ALIGNMENT);
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
		m_contentsPanel = new JPanel();
		m_contentsPanel.setLayout(new BoxLayout(m_contentsPanel, BoxLayout.LINE_AXIS));

		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.PAGE_AXIS));

		JPanel fieldPanel = new JPanel();
		fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.PAGE_AXIS));

		// X
		m_xLabel = new JLabel("X    ");
		labelPanel.add(m_xLabel);
		m_xField = new JTextField(12);
		m_xField.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				updatePOI();
			}
		});
		fieldPanel.add(m_xField);

		// Y
		m_yLabel = new JLabel("Y    ");
		labelPanel.add(m_yLabel);
		m_yField = new JTextField(12);
		m_yField.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				updatePOI();
			}
		});
		fieldPanel.add(m_yField);

		// Combo box
		m_poiTypeLabel = new JLabel("Type "); // TODO internasjonaliser
		labelPanel.add(m_poiTypeLabel);
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
				IMessageLineIf messageLine = message.findMessageLine(MessageLineType.FINDING, true);
				IPOIIf poi = messageLine.getLinePOI();
				if(poi == null)
				{
					poi = m_wpMessageLog.getMsoManager().createPOI();
					messageLine.setLinePOI(poi);
				}

				poi.setType(type);
				// TODO Fire change? Update map?
			}
		});
		fieldPanel.add(m_poiTypesComboBox);
		updatePOITypes();

		JPanel fieldsPanel = new JPanel();
		fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.LINE_AXIS));
		fieldsPanel.add(labelPanel);
		fieldsPanel.add(fieldPanel);

		JPanel completePOIPanel = new JPanel();
		completePOIPanel.add(fieldsPanel);
		completePOIPanel.add(m_showInMapButton);

		m_contentsPanel.add(completePOIPanel);

		// Dialog buttons
		JPanel dialogButtonsPane = new JPanel();
		dialogButtonsPane.setLayout(new BoxLayout(dialogButtonsPane, BoxLayout.PAGE_AXIS));
		dialogButtonsPane.add(m_cancelButton);
		dialogButtonsPane.add(m_okButton);
		m_contentsPanel.add(dialogButtonsPane);

		this.add(m_contentsPanel);
		this.pack();
	}

	public void clearContents()
	{
		m_xField.setText("");
		m_yField.setText("");
	}

	public void hideDialog()
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
				POIType type = messageLine.getLinePOI().getType();
				m_poiTypesComboBox.setSelectedItem(type);
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

	public void showDialog()
	{
		if(m_showInMapButton.isSelected())
		{
			MessageLogPanel.showMap();
		}
		
		this.setVisible(true);
	}

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
