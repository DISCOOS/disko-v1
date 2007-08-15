package org.redcross.sar.wp.messageLog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Point2D;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.renderers.SimpleListCellRenderer;
import org.redcross.sar.map.DiskoMap;
import org.redcross.sar.map.IDiskoMap;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.mso.data.IMessageLineIf.MessageLineType;
import org.redcross.sar.mso.data.IPOIIf.POIType;
import org.redcross.sar.mso.event.IMsoGisListenerIf;
import org.redcross.sar.mso.event.MsoEvent.EventType;
import org.redcross.sar.mso.event.MsoEvent.Gis;
import org.redcross.sar.util.mso.Position;

import com.esri.arcgis.carto.NewDimensionFeedback;
import com.esri.arcgis.interop.AutomationException;

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
	protected boolean m_positionMessageLine = true;
	
	public MessagePOIDialog(IDiskoWpMessageLog wp, boolean position)
	{
		super(wp.getApplication().getFrame());
		
		m_wpMessageLog = wp;
		m_positionMessageLine = position;
		
		initialize();
	}
	
	private void initialize()
	{
		// TODO numpads?
		
		initButtons();
		initContents();
		
		IDiskoMap map = MessageLogPanel.getMap();
		try
		{
			m_tool = new SinglePOITool(m_wpMessageLog.getApplication(), this, m_positionMessageLine);
			m_tool.setMap(map);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			MessageLogPanel.getMap().setCurrentToolByRef(m_tool);
		} 
		catch (AutomationException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
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
		if(m_positionMessageLine)
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
		poi.setType(this.getSelectedPOIType());
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
		
		m_showInMapButton = DiskoButtonFactory.createSmallToggleButton("Vis i kart"); // TODO internasjonaliser
		m_showInMapButton.setAlignmentY(JComponent.TOP_ALIGNMENT);
		m_showInMapButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
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
	 * 
	 * @param types set to null in order to hide combo box
	 */
	public void setPOITypes(POIType[] types)
	{
		if(types == null)
		{
			m_poiTypeLabel.setVisible(false);
			m_poiTypesComboBox.removeAllItems();
			m_poiTypesComboBox.setVisible(false);
		}
		else
		{
			for(POIType type : types)
			{
				m_poiTypesComboBox.addItem(type);
			}
			m_poiTypeLabel.setVisible(true);
			m_poiTypesComboBox.setSelectedItem(types[0]);
			m_poiTypesComboBox.setVisible(true);
		}
		m_poiTypes = types;
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
		m_xField.addCaretListener(new CaretListener()
		{
			public void caretUpdate(CaretEvent e)
			{
				updatePOI();
			}
		});
		fieldPanel.add(m_xField);
		
		// Y
		m_yLabel = new JLabel("Y    ");
		labelPanel.add(m_yLabel);
		m_yField = new JTextField(12);
		m_yField.addCaretListener(new CaretListener()
		{
			public void caretUpdate(CaretEvent e)
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
		this.setPOITypes(null);
		
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
	}

	public void newMessageSelected(IMessageIf message)
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
		if(m_poiTypes == null || m_positionMessageLine)
		{
			return POIType.GENERAL;
		}
		else
		{
			String type = m_poiTypesComboBox.getSelectedItem().toString();
			return POIType.valueOf(type);
		}
	}
}
