package org.redcross.sar.wp.messageLog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.text.html.HTMLDocument.HTMLReader.SpecialAction;

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
	
	protected JToggleButton m_selectionButton = null;
	protected JToggleButton m_confirmButton = null;
	protected JButton m_allButton = null;
	protected JButton m_noneButton = null;
	
	protected boolean m_selectionMode = true;
	
	protected JPanel m_unitTypePanel = null;
	protected JToggleButton m_teamButton = null;
	protected JToggleButton m_dogButton = null;
	protected JToggleButton m_vehicleButton = null;
	protected JToggleButton m_aircraftButton = null;
	protected JToggleButton m_boatButton = null;
	protected JToggleButton m_commandPostButton = null;
	
	protected JLabel m_confirmationStatusLabel = null;
	protected JComponent m_labelGlue = null;
	
	protected JPanel m_listArea = null;
	protected List<ICommunicatorIf> m_selectedCommuicators;
	protected List<ICommunicatorIf> m_confirmedCommunicators;
	protected HashMap<JToggleButton, ICommunicatorIf> m_buttonCommunicatorMap = null;
	protected HashMap<ICommunicatorIf, JToggleButton> m_communicatorButtonMap = null;
	
	/**
	 * @param wp
	 */
	public BroadcastToDialog(IDiskoWpMessageLog wp)
	{
		super(wp.getApplication().getFrame());
		
		m_wpMessageLog = wp;
		
		m_selectedCommuicators = new LinkedList<ICommunicatorIf>();
		m_confirmedCommunicators = new LinkedList<ICommunicatorIf>();
		
		m_buttonCommunicatorMap = new HashMap<JToggleButton, ICommunicatorIf>();
		m_communicatorButtonMap = new HashMap<ICommunicatorIf, JToggleButton>();
		
		initContentsPanel();
		initActionButtons();
		
		m_contentsPanel.add(new JSeparator(JSeparator.HORIZONTAL));
		
		m_listArea = new JPanel();
		m_listArea.setLayout(new BoxLayout(m_listArea, BoxLayout.LINE_AXIS));
		m_listArea.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		m_contentsPanel.add(m_listArea);
		
		this.pack();
	}
 
	/**
	 * Builds buttons based on which communicators are present in the command post. Also stores mapping between these buttons and 
	 * the communicator in a hash map
	 */
	private void updateCommunicatorList()
	{
		m_listArea.removeAll();
		m_buttonCommunicatorMap.clear();
		m_communicatorButtonMap.clear();
		
		AbstractDerivedList<ICommunicatorIf> communicators = m_wpMessageLog.getMsoManager().getCmdPost().getCommunicatorList();
		int i = 0;
		JPanel buttonPanel = null;
		for(final ICommunicatorIf communicator : communicators.getItems())
		{
			// Necessary for laying buttons out correctly, due to the lack of layout managers in Swing
			if(i%NUM_ROWS_COMMUNICATOR_LIST == 0)
			{
				buttonPanel = new JPanel();
				buttonPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
				buttonPanel.setAlignmentY(JComponent.TOP_ALIGNMENT);
				buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
				m_listArea.add(buttonPanel);
			}
			
			JToggleButton button = new JToggleButton(String.valueOf(communicator.getCommunicatorNumberPrefix()) 
					+ " " + String.valueOf(communicator.getCommunicatorNumber()));
			button.setMinimumSize(ChangeToDialog.BUTTON_SIZE);
			button.setPreferredSize(ChangeToDialog.BUTTON_SIZE);
			button.setMaximumSize(ChangeToDialog.BUTTON_SIZE);
			
			button.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					JToggleButton button = (JToggleButton)e.getSource();
					if(!button.isSelected())
					{
						if(m_selectionMode)
						{
							m_selectedCommuicators.remove(communicator);
						}
						else
						{
							m_confirmedCommunicators.remove(communicator);
						}
					}
					else
					{
						if(m_selectionMode)
						{
							m_selectedCommuicators.add(communicator);
						}
						else
						{
							m_confirmedCommunicators.add(communicator);
							
						}
					}
					updateStatusLabel();
				}
			});
			
			// Store mapping between button and communicator
			m_buttonCommunicatorMap.put(button, communicator);
			m_communicatorButtonMap.put(communicator, button);
			
			buttonPanel.add(button);
			i++;
		}
	}
	
	/**
	 * Updates label giving information about current state of selection, i.e. number of confirmed/unconfirmed receivers
	 */
	private void updateStatusLabel()
	{
		int numReceivers = m_selectedCommuicators.size();
		int numConfirmedReceivers = m_confirmedCommunicators.size();
		String messageText = m_wpMessageLog.getText("BroadcastStatusLabel.text");
		m_confirmationStatusLabel.setText(String.format(messageText, numConfirmedReceivers, numReceivers));
	}	
	
	/**
	 *  Updates button selection based on selection mode
	 */
	private void updateButtonSelection()
	{
		JToggleButton button = null;
		if(m_selectionMode)
		{
			for(ICommunicatorIf communicator : m_wpMessageLog.getMsoManager().getCmdPost().getCommunicatorList().getItems())
			{
				button = m_communicatorButtonMap.get(communicator);
				if(m_selectedCommuicators.contains(communicator))
				{
					button.setSelected(true);
				}
				else
				{
					button.setSelected(false);
				}
			}
		}
		else
		{
			for(ICommunicatorIf communicator : m_wpMessageLog.getMsoManager().getCmdPost().getCommunicatorList().getItems())
			{
				button = m_communicatorButtonMap.get(communicator);
				if(m_confirmedCommunicators.contains(communicator))
				{
					button.setSelected(true);
				}
				else
				{
					button.setSelected(false);
				}
			}
		}
	}

	private void initActionButtons()
	{
		m_buttonRowPanel = new JPanel();
		m_buttonRowPanel.setLayout(new BoxLayout(m_buttonRowPanel, BoxLayout.LINE_AXIS));
		m_buttonRowPanel.setPreferredSize(new Dimension(SingleUnitListSelectionDialog.PANEL_WIDTH, 
				MessageLogPanel.SMALL_BUTTON_SIZE.height));
		m_buttonRowPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		m_buttonRowPanel.setLayout(new BoxLayout(m_buttonRowPanel, BoxLayout.LINE_AXIS));
		
		m_selectionButton = new JToggleButton(m_wpMessageLog.getText("SelectionButton.text"));
		m_selectionButton.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		m_selectionButton.setMinimumSize(ChangeToDialog.BUTTON_SIZE);
		m_selectionButton.setPreferredSize(ChangeToDialog.BUTTON_SIZE);
		m_selectionButton.setMaximumSize(ChangeToDialog.BUTTON_SIZE);
		m_selectionButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if(!m_selectionMode)
				{
					setSelectionMode();
					updateButtonSelection();
				}
			}
		});
		m_selectionButton.setSelected(true);
		m_buttonRowPanel.add(m_selectionButton);
		m_buttonRowPanel.setMaximumSize(new Dimension(MessageLogPanel.PANEL_WIDTH, MessageLogPanel.SMALL_BUTTON_SIZE.height));
		
		m_confirmButton = new JToggleButton(m_wpMessageLog.getText("ConfirmButton.text"));
		m_confirmButton.setMinimumSize(ChangeToDialog.BUTTON_SIZE);
		m_confirmButton.setPreferredSize(ChangeToDialog.BUTTON_SIZE);
		m_confirmButton.setMaximumSize(ChangeToDialog.BUTTON_SIZE);
		m_confirmButton.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		m_confirmButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(m_selectionMode)
				{
					setConfirmationMode();
					updateButtonSelection();
				}
			}
		});
		m_buttonRowPanel.add(m_confirmButton);
		
//		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
//		separator.setAlignmentX(JComponent.LEFT_ALIGNMENT);
//		m_buttonRowPanel.add(separator);
//		separator = new JSeparator(JSeparator.VERTICAL);
//		separator.setAlignmentX(JComponent.LEFT_ALIGNMENT);
//		m_buttonRowPanel.add(separator);
		
		m_allButton = new JButton(m_wpMessageLog.getText("AllButton.text"));
		m_allButton.setMinimumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		m_allButton.setPreferredSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		m_allButton.setMaximumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		m_allButton.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		m_allButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// TODO update unit list?
				if(m_selectionMode)
				{
					m_selectedCommuicators.clear();
					for(ICommunicatorIf communicator : m_wpMessageLog.getMsoManager().getCmdPost().getCommunicatorList().getItems())
					{
						m_selectedCommuicators.add(communicator);
						JToggleButton button = m_communicatorButtonMap.get(communicator);
						button.setSelected(true);
					}
				}
				else
				{
					m_confirmedCommunicators.clear();
					for(ICommunicatorIf communicator : m_wpMessageLog.getMsoManager().getCmdPost().getCommunicatorList().getItems())
					{
						m_confirmedCommunicators.add(communicator);
						JToggleButton button = m_communicatorButtonMap.get(communicator);
						button.setSelected(true);
					}
				}
			}
		});
		m_buttonRowPanel.add(m_allButton);
		
		m_noneButton = new JButton(m_wpMessageLog.getText("NoneButton.text"));
		m_noneButton.setMinimumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		m_noneButton.setPreferredSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		m_noneButton.setMaximumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		m_noneButton.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		// None button should remove all selected items from the current selection mode (selection/confirmation)
		m_noneButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JToggleButton button = null;
				if(m_selectionMode)
				{
					for(ICommunicatorIf communicator : m_selectedCommuicators)
					{
						button = m_communicatorButtonMap.get(communicator);
						button.setSelected(false);
					}
					m_selectedCommuicators.clear();
				}
				else
				{
					for(ICommunicatorIf communicator : m_confirmedCommunicators)
					{
						button = m_communicatorButtonMap.get(communicator);
						button.setSelected(false);
					}
					m_confirmedCommunicators.clear();
				}
			}
		});
		m_buttonRowPanel.add(m_noneButton);
		
//		separator = new JSeparator(JSeparator.VERTICAL);
//		separator.setAlignmentX(JComponent.LEFT_ALIGNMENT);
//		m_buttonRowPanel.add(separator);
//		separator = new JSeparator(JSeparator.VERTICAL);
//		separator.setAlignmentX(JComponent.LEFT_ALIGNMENT);
//		m_buttonRowPanel.add(separator);
		
		m_unitTypePanel = new JPanel();
		m_unitTypePanel.setLayout(new BoxLayout(m_unitTypePanel, BoxLayout.LINE_AXIS));
		
		m_teamButton = createUnitButton(UnitType.TEAM);
		m_dogButton = createUnitButton(UnitType.DOG);
		m_vehicleButton = createUnitButton(UnitType.VEHICLE);
		m_aircraftButton = createUnitButton(UnitType.AIRCRAFT);
		m_boatButton = createUnitButton(UnitType.BOAT);
		m_commandPostButton = createUnitButton(UnitType.COMMAND_POST);
		
		m_buttonRowPanel.add(m_unitTypePanel);
		
		m_confirmationStatusLabel = new JLabel("");
		updateStatusLabel();
		m_confirmationStatusLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		m_buttonRowPanel.add(m_confirmationStatusLabel);
		m_labelGlue = (JComponent)Box.createHorizontalGlue();
		m_labelGlue.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		m_buttonRowPanel.add(m_labelGlue);
		
		m_contentsPanel.add(m_buttonRowPanel, BorderLayout.NORTH);
	}
	
	private JToggleButton createUnitButton(UnitType type)
	{
		JToggleButton button = new JToggleButton();
		button.setIcon(Utils.getIcon(type));
		button.setMinimumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		button.setPreferredSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		button.setMaximumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		m_unitTypePanel.add(button);
		return button;
	}

	private void initContentsPanel()
	{
		m_contentsPanel = new JPanel();
		m_contentsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		m_contentsPanel.setLayout(new BoxLayout(m_contentsPanel, BoxLayout.PAGE_AXIS));
		m_contentsPanel.setPreferredSize(new Dimension(SingleUnitListSelectionDialog.PANEL_WIDTH, 
				MessageLogPanel.SMALL_BUTTON_SIZE.height*(NUM_ROWS_COMMUNICATOR_LIST+1)+6));
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
		if(message.isBroadcast())
		{
			// TODO Message is broadcast, select buttons and update maps based on message receiver list
		}
		else
		{
			m_confirmedCommunicators.clear();
			m_selectedCommuicators.clear();
			for(JToggleButton button : m_buttonCommunicatorMap.keySet())
			{
				button.setSelected(false);
			}
		}
		
		// TODO
		updateStatusLabel();
	}
	
	public void setSelectionMode()
	{
		m_confirmButton.setSelected(false);
		m_selectionButton.setSelected(true);
		
		m_selectionMode = true;
		m_unitTypePanel.setVisible(true);
		m_confirmationStatusLabel.setVisible(false);
		m_labelGlue.setVisible(false);
	}
	
	public void setConfirmationMode()
	{
		m_selectionButton.setSelected(false);
		m_confirmButton.setSelected(true);
		
		m_selectionMode = false;
		m_unitTypePanel.setVisible(false);
		m_confirmationStatusLabel.setVisible(true);
		m_labelGlue.setVisible(true);
	}

	public void showDialog()
	{
		this.setVisible(true);
		m_listArea.revalidate();
		// Refresh list of communicators
		updateCommunicatorList();
		
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
