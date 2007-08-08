package org.redcross.sar.wp.messageLog;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.map.DiskoMap;
import org.redcross.sar.map.IDiskoMap;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.mso.data.IMessageLineIf.MessageLineType;
import org.redcross.sar.mso.data.IPOIIf.POIType;
import org.redcross.sar.util.mso.Position;

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
	protected JButton m_showInMapButton = null;
	protected JLabel m_xLabel = null;
	protected JLabel m_yLabel = null;
	protected JTextField m_xField = null;
	protected JTextField m_yField = null;
	protected JLabel m_poiTypeLabel = null;
	protected JComboBox m_poiTypesComboBox = null;
	protected POIType[] m_poiTypes = null;
	protected IDiskoMap m_map = null;
	protected IDiskoWpMessageLog m_wpMessageLog = null;
	
	public MessagePOIDialog(IDiskoWpMessageLog wp)
	{
		super(wp.getApplication().getFrame());
		
		m_wpMessageLog = wp;
		
		m_map = wp.getMap();
		
		initialize();
	}
	
	private void initialize()
	{
		initButtons();
		initContents();
	}

	private void initButtons()
	{
		m_okButton = DiskoButtonFactory.createSmallOKButton();
		m_okButton.addActionListener(new ActionListener()
		{
			IMessageIf message = MessageLogTopPanel.getCurrentMessage();
			
			public void actionPerformed(ActionEvent e)
			{	
				double xCoordinate = 0;
				try
				{
					xCoordinate = Double.valueOf(m_xField.getText());
				}
				catch(NumberFormatException nfe){return;}//TODO inform user of invalid x-coordinate?
				
				double yCoordinate = 0;
				try
				{
					yCoordinate = Double.valueOf(m_yField.getText());
				}
				catch(NumberFormatException nfe){return;}
				
				// Create POI
				IPOIIf poi = null;
				
				IMessageLineIf messageLine = message.findMessageLine(MessageLineType.POI, false);
				// Create new POI and message line if POI message line did not exists
				if(messageLine == null)
				{
					messageLine = message.findMessageLine(MessageLineType.POI, true);
					poi = m_wpMessageLog.getMsoManager().createPOI();
					
					messageLine.setLinePOI(poi);
				}
				else
				{
					poi = messageLine.getLinePOI();
				}
				
				// Update POI
				poi.getPosition().setPosition(xCoordinate, yCoordinate);
				
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
		
		m_showInMapButton = DiskoButtonFactory.createSmallButton("Vis i kart");
		m_showInMapButton.setAlignmentY(JComponent.TOP_ALIGNMENT);
		m_showInMapButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// TODO display map dialog
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
		
		// X
		JPanel xPanel = new JPanel();
		m_xLabel = new JLabel("X");
		xPanel.add(m_xLabel);
		m_xField = new JTextField(12);
		m_xField.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// TODO Update POI in map
			}
		});
		xPanel.add(m_xField);
		
		// Y
		JPanel yPanel = new JPanel();
		m_yLabel = new JLabel("Y");
		yPanel.add(m_yLabel);
		m_yField = new JTextField(12);
		m_yField.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// TODO Update POI in map
				
			}
		});
		yPanel.add(m_yField);
		
		// Combo box
		JPanel comboBoxPanel = new JPanel();
		m_poiTypeLabel = new JLabel("Type"); // TODO internasjonaliser
		comboBoxPanel.add(m_poiTypeLabel);
		m_poiTypesComboBox = new JComboBox();
		comboBoxPanel.add(m_poiTypesComboBox);
		this.setPOITypes(null);
		
		JPanel fieldsPanel = new JPanel();
		fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.PAGE_AXIS));
		fieldsPanel.add(xPanel);
		fieldsPanel.add(yPanel);
		fieldsPanel.add(comboBoxPanel);
		
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
		IMessageLineIf messageLine = message.findMessageLine(MessageLineType.POI, false);
		
		if(messageLine == null)
		{
			m_xField.setText("");
			m_yField.setText("");
			setPOITypes(null);
		}
		else
		{
			IPOIIf poi = messageLine.getLinePOI();
			Position position = poi.getPosition();
			//m_xField.setText(String.valueOf(position.getPosition()));
			//m_yField.setText(String.valueOf(position.getPosition()));
		}
		
	}

	public void showDialog()
	{
		this.setVisible(true);
	}
}
