package org.redcross.sar.wp.messageLog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;

import org.redcross.sar.app.Utils;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.data.AbstractDerivedList;
import org.redcross.sar.mso.data.ICommunicatorIf;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IUnitIf.UnitType;

import com.esri.arcgis.geometry.IUnit;

/**
 * Dialog shown when setting the to field of a message in broadcast mode
 * 
 * @author thomasl
 *
 */
public class BroadcastToDialog extends DiskoDialog implements IEditMessageDialogIf
{
	protected final int NUM_ROWS_COMMUNICATOR_LIST = 6;
	
	protected IDiskoWpMessageLog m_wpMessageLog = null;
	
	protected JPanel m_contentsPanel = null;
	protected JPanel m_buttonRowPanel = null;
	
	protected JButton m_selectionButton = null;
	protected JButton m_confirmButton = null;
	protected JButton m_allButton = null;
	protected JButton m_noneButton = null;
	
	protected boolean m_selectionMode = true;
	
	protected JPanel m_unitTypePanel = null;
	protected JButton m_teamButton = null;
	protected JButton m_dogButton = null;
	protected JButton m_vehicleButton = null;
	protected JButton m_aircraftButton = null;
	protected JButton m_boatButton = null;
	protected JButton m_commandPostButton = null;
	
	protected JLabel m_confirmationStatusLabel = null;
	
	protected JPanel m_listArea = null;
	//protected HashMap<ICommunicatorIf, boolean> m_selectedCommuicators;
	//protected HashMap<ICommunicatorIf, boolean> m_confirmedCommunicators;
	protected HashMap<JToggleButton, ICommunicatorIf> m_buttonCommunicatorMap = null;
	
	public BroadcastToDialog(IDiskoWpMessageLog wp)
	{
		super(wp.getApplication().getFrame());
		
		m_wpMessageLog = wp;
		
		m_buttonCommunicatorMap = new HashMap<JToggleButton, ICommunicatorIf>();
		
		initContentsPanel();
		initActionButtons();
		m_listArea = new JPanel();
		m_listArea.setBorder(BorderFactory.createLineBorder(Color.green));
		m_listArea.setLayout(new BoxLayout(m_listArea, BoxLayout.LINE_AXIS));
		m_contentsPanel.add(m_listArea);
		
		updateCommunicatorList();
		
		
		
		this.pack();
	}

	// Add communicator buttons
	private void updateCommunicatorList()
	{
		m_listArea.removeAll();
		m_buttonCommunicatorMap.clear();
		
		AbstractDerivedList<ICommunicatorIf> communicators = m_wpMessageLog.getMsoManager().getCmdPost().getCommunicatorList();
		int i = 0;
		JPanel buttonPanel = null;
		for(ICommunicatorIf communicator : communicators.getItems())
		{
			// Necessary for laying buttons out correctly, due to the lack of layout managers in Swing
			if(i%NUM_ROWS_COMMUNICATOR_LIST == 0)
			{
				buttonPanel = new JPanel();
				buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
				m_listArea.add(buttonPanel);
			}
			
			JToggleButton button = new JToggleButton(String.valueOf(communicator.getCommunicatorNumberPrefix()) + " " + String.valueOf(communicator.getCommunicatorNumber()));
			
			// Store mapping between button and communicator
			m_buttonCommunicatorMap.put(button, communicator);
			
			buttonPanel.add(button);
			i++;
		}
	}
	
	private void updateCommunicatorListSelection()
	{
		
	}

	private void initActionButtons()
	{
		m_buttonRowPanel = new JPanel();
		m_buttonRowPanel.setPreferredSize(new Dimension(SingleUnitListSelectionDialog.PANEL_WIDTH, 
				MessageLogPanel.SMALL_BUTTON_SIZE.height));
		m_contentsPanel.setBorder(BorderFactory.createLineBorder(Color.red));
		m_buttonRowPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		m_buttonRowPanel.setLayout(new BoxLayout(m_buttonRowPanel, BoxLayout.LINE_AXIS));
		
		m_selectionButton = new JButton(m_wpMessageLog.getText("SelectionButton.text"));
		m_selectionButton.setPreferredSize(ChangeToDialog.BUTTON_SIZE);
		m_selectionButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				setSelectionMode();
			}
		});
		m_buttonRowPanel.add(m_selectionButton);
		m_buttonRowPanel.setMaximumSize(new Dimension(MessageLogPanel.PANEL_WIDTH, MessageLogPanel.SMALL_BUTTON_SIZE.height));
		
		m_confirmButton = new JButton(m_wpMessageLog.getText("ConfirmButton.text"));
		m_confirmButton.setMinimumSize(ChangeToDialog.BUTTON_SIZE);
		m_confirmButton.setPreferredSize(ChangeToDialog.BUTTON_SIZE);
		m_confirmButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				setConfirmationMode();
			}
		});
		m_buttonRowPanel.add(m_confirmButton);
		
		m_buttonRowPanel.add(Box.createHorizontalGlue());
		
		m_buttonRowPanel.add(new JSeparator(JSeparator.VERTICAL));
		m_buttonRowPanel.add(new JSeparator(JSeparator.VERTICAL));
		
		m_buttonRowPanel.add(Box.createHorizontalGlue());
		
		m_allButton = new JButton(m_wpMessageLog.getText("AllButton.text"));
		m_allButton.setMinimumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		m_allButton.setMaximumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		m_buttonRowPanel.add(m_allButton);
		
		m_noneButton = new JButton(m_wpMessageLog.getText("NoneButton.text"));
		m_noneButton.setMinimumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		m_noneButton.setMaximumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		m_buttonRowPanel.add(m_noneButton);
		
		m_buttonRowPanel.add(Box.createHorizontalGlue());
		
		m_buttonRowPanel.add(new JSeparator(JSeparator.VERTICAL));
		m_buttonRowPanel.add(new JSeparator(JSeparator.VERTICAL));
		
		m_buttonRowPanel.add(Box.createHorizontalGlue());
		
		m_unitTypePanel = new JPanel();
		m_unitTypePanel.setLayout(new BoxLayout(m_unitTypePanel, BoxLayout.LINE_AXIS));
		
		m_teamButton = createUnitButton(UnitType.TEAM);
		m_dogButton = createUnitButton(UnitType.DOG);
		m_vehicleButton = createUnitButton(UnitType.VEHICLE);
		m_aircraftButton = createUnitButton(UnitType.AIRCRAFT);
		m_boatButton = createUnitButton(UnitType.BOAT);
		m_commandPostButton = createUnitButton(UnitType.COMMAND_POST);
		
		m_confirmationStatusLabel = new JLabel();
		m_buttonRowPanel.add(m_confirmationStatusLabel);
		
		m_buttonRowPanel.add(m_unitTypePanel);
		
		m_contentsPanel.add(m_buttonRowPanel);
	}
	
	private JButton createUnitButton(UnitType type)
	{
		JButton button = new JButton();
		button.setIcon(Utils.getIcon(type));
		button.setMinimumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		button.setMaximumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		m_unitTypePanel.add(button);
		return button;
	}

	private void initContentsPanel()
	{
		m_contentsPanel = new JPanel();
		m_contentsPanel.setLayout(new BoxLayout(m_contentsPanel, BoxLayout.PAGE_AXIS));
		m_contentsPanel.setPreferredSize(new Dimension(SingleUnitListSelectionDialog.PANEL_WIDTH, 
				MessageLogPanel.SMALL_BUTTON_SIZE.height*(NUM_ROWS_COMMUNICATOR_LIST+1)));
		m_contentsPanel.setBorder(BorderFactory.createLineBorder(Color.red));
		this.add(m_contentsPanel);
	}

	public void clearContents()
	{
		// TODO Auto-generated method stub
		
	}

	public void hideDialog()
	{
		this.setVisible(false);
		
	}

	public void newMessageSelected(IMessageIf message)
	{
		// TODO Auto-generated method stub
		
	}
	
	public void setSelectionMode()
	{
		m_selectionMode = true;
		m_unitTypePanel.setVisible(true);
		m_confirmationStatusLabel.setVisible(false);
	}
	
	public void setConfirmationMode()
	{
		m_selectionMode = false;
		m_unitTypePanel.setVisible(false);
		m_confirmationStatusLabel.setVisible(true);
	}

	public void showDialog()
	{
		this.setVisible(true);
		if(m_selectionMode)
		{
			setSelectionMode();
		}
		else
		{
			setConfirmationMode();
		}
		
	}
}
