package org.redcross.sar.wp.messageLog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;

import org.redcross.sar.app.Utils;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.data.AbstractDerivedList;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.ICommunicatorIf;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IUnitListIf;
import org.redcross.sar.mso.data.ICmdPostIf.CmdPostStatus;
import org.redcross.sar.mso.data.IUnitIf.UnitStatus;
import org.redcross.sar.mso.data.IUnitIf.UnitType;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.Update;
import org.redcross.sar.util.mso.Selector;

/**
 * 
 * @author thomasl
 *
 * Dialog containing a list of all units  in command post communicator list
 */
public class SingleUnitListSelectionDialog extends DiskoDialog implements IEditMessageDialogIf, IMsoUpdateListenerIf, ActionListener
{
	protected JPanel m_contentsPanel = null;
	protected JScrollPane m_scrollPane = null;
	protected IDiskoWpMessageLog m_wpMessageLog;
	protected UnitType m_unitTypeFilter = null;
	protected IUnitListIf m_unitList = null;
	protected AbstractDerivedList<ICommunicatorIf> m_communicatorList;
	protected List<ActionListener> m_actionListeners;
	protected ButtonGroup m_buttonGroup = null;
	protected JToggleButton m_currentButton = null;
	protected boolean m_senderList = true;
	
	protected MouseListener m_buttonMouseListener = new MouseListener()
	{
		public void mouseClicked(MouseEvent me)
		{
			JToggleButton button = (JToggleButton)me.getSource();
			if(button == m_currentButton)
			{
				// If a selected unit is de-selected in the list, standard sender should be set
				m_buttonGroup.clearSelection();
				// TODO change default value when multiple command posts are possible
				ICommunicatorIf commandPost = (ICommunicatorIf)m_wpMessageLog.getMsoManager().getCmdPost();
				ActionEvent ae = new ActionEvent(button, 1, commandPost.getCommunicatorNumberPrefix() + " " + commandPost.getCommunicatorNumber());
				ActionListener[] listeners = button.getActionListeners();
				for(ActionListener actionListener : listeners)
				{
					actionListener.actionPerformed(ae);
				}
				m_currentButton = null;
			}
			else
			{
				m_currentButton = button;
			}
			fireDialogFinished();
		}

		public void mouseEntered(MouseEvent arg0){}
		public void mouseExited(MouseEvent arg0){}
		public void mousePressed(MouseEvent arg0){}
		public void mouseReleased(MouseEvent arg0){}		
	};
	
	final private static Dimension BUTTON_SIZE = new Dimension(MessageLogPanel.SMALL_BUTTON_SIZE.width*3, 
			MessageLogPanel.SMALL_BUTTON_SIZE.height);
	final static public int PANEL_WIDTH = BUTTON_SIZE.width * 5 + 6;
	private final int NUMBER_OF_ROWS = 7;
	
	/**
	 * Constructor
	 * @param wp
	 */
	public SingleUnitListSelectionDialog(IDiskoWpMessageLog wp, boolean senderList)
	{
		super(wp.getApplication().getFrame());
		m_wpMessageLog = wp;
		m_wpMessageLog.getMmsoEventManager().addClientUpdateListener(this);
		m_unitList = wp.getMsoManager().getCmdPost().getUnitList();
		m_communicatorList = wp.getMsoManager().getCmdPost().getCommunicatorList();
		
		m_senderList = senderList;
		
		m_actionListeners = new LinkedList<ActionListener>();
		
		m_buttonGroup = new ButtonGroup();
		
		initContentsPanel();
		this.pack();
	}
	
	/**
	 * Sorts units based on  number
	 */
	private Comparator<ICommunicatorIf> m_communicatorComparator = new Comparator<ICommunicatorIf>()
	{
		@Override
		public int compare(ICommunicatorIf arg0, ICommunicatorIf arg1)
		{
			if(arg0 instanceof IUnitIf)
			{
				if(arg1 instanceof IUnitIf)
				{
					return ((IUnitIf)arg0).getNumber() - ((IUnitIf)arg1).getNumber();
				}
				else
				{
					return 1;
				}
			}
			else
			{
				return -1;
			}
		}
	};
	
	/**
	 * Selects active units based on the unit type filter
	 */
	private final EnumSet<UnitStatus> m_activeUnitStatusSet = EnumSet.of(UnitStatus.PAUSED, UnitStatus.READY, UnitStatus.WORKING);
	private final EnumSet<CmdPostStatus> m_activeCmdPostStatusSet = EnumSet.of(CmdPostStatus.IDLE, CmdPostStatus.OPERATING, CmdPostStatus.PAUSED);
	private Selector<ICommunicatorIf> m_communicatorSelector = new Selector<ICommunicatorIf>()
	{
		public boolean select(ICommunicatorIf communicator)
		{
			if(communicator instanceof ICmdPostIf )
			{
				// Command post should only be selected if the type filter is set to cp and the cp is active
				ICmdPostIf cmdPost = (ICmdPostIf)communicator;
				if(m_activeCmdPostStatusSet.contains(cmdPost.getStatus()) &&
						(m_unitTypeFilter == UnitType.COMMAND_POST || m_unitTypeFilter == null))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else if(communicator instanceof IUnitIf)
			{
				// Unit should be selected if it is active, and the unit type filter match
				IUnitIf unit = (IUnitIf)communicator;
				if(m_activeUnitStatusSet.contains(unit.getStatus()) && 
						(m_unitTypeFilter == unit.getType() || m_unitTypeFilter == null))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				// TODO add possible extensions here, should not be reached for the time being 
				return false;
			}
		}
	};
	
	private Icon getUnitIcon(UnitType unitType)
	{
		Icon icon = null;
		try
		{
			ResourceBundle resources = ResourceBundle.getBundle("org.redcross.sar.mso.data.properties.Unit");
			String iconPath = resources.getString("UnitType." + unitType.toString() + ".icon");
			icon = Utils.createImageIcon(iconPath, unitType.toString());
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return icon;
	}

	private void initContentsPanel()
	{
		m_contentsPanel = new JPanel();
		m_contentsPanel.setLayout(new BoxLayout(m_contentsPanel, BoxLayout.LINE_AXIS));
		m_scrollPane = new JScrollPane(m_contentsPanel);
		m_scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		m_scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		m_contentsPanel.setPreferredSize(new Dimension(PANEL_WIDTH, 
				MessageLogPanel.SMALL_BUTTON_SIZE.height*NUMBER_OF_ROWS + 6));
		this.add(m_scrollPane);
		this.pack();
	}

	public void clearContents()
	{
	}

	public void hideDialog()
	{
		this.setVisible(false);
	}

	/**
	 * Search communicator list and mark unit as selected
	 */
	public void newMessageSelected(IMessageIf message)
	{
		m_buttonGroup.clearSelection();
		
		// Update depending on whether this is a list of senders or receivers
		ICommunicatorIf communicator;
		if(m_senderList)
		{
			communicator = message.getSender();
		}
		else
		{
			communicator = message.getSingleReceiver();
		}
		
		// Default value is command post
		if(communicator == null)
		{
			communicator = (ICommunicatorIf)m_wpMessageLog.getMsoManager().getCmdPost();
		}
		
		// Get sender button and mark it
		Enumeration<AbstractButton> buttons = m_buttonGroup.getElements();
		AbstractButton button = null;
		while(buttons.hasMoreElements())
		{
			button = buttons.nextElement();
			String[] command = button.getActionCommand().split(" ");
			if(command[0].charAt(0) == communicator.getCommunicatorNumberPrefix() && 
					Integer.valueOf(command[1]) == communicator.getCommunicatorNumber())
			{
				m_currentButton = (JToggleButton)button;
				button.setSelected(true);
			}
		}
	}

	public void showDialog()
	{
		this.setVisible(true);
	}

	/**
	 * Updates unit list based on mso communicator events
	 * @see org.redcross.sar.mso.event.IMsoUpdateListenerIf#handleMsoUpdateEvent(org.redcross.sar.mso.event.MsoEvent.Update)
	 */
	public void handleMsoUpdateEvent(Update e)
	{	
		buildList();
	}

	public boolean hasInterestIn(IMsoObjectIf msoObject)
	{
		return msoObject instanceof ICommunicatorIf;
	}

	public UnitType getUnitTypeFilter()
	{
		return m_unitTypeFilter;
	}

	/**
	 * Update unit selection list with type filter
	 */
	public void setUnitTypeFilter(UnitType typeFilter)
	{
		m_unitTypeFilter = typeFilter;
		buildList();
	}
	
	/**
	 * Updates the unit list using the unit type as filter
	 */
	public void buildList()
	{
		// Clear previous list, brute force maintenance
		m_contentsPanel.removeAll();
		
		m_buttonGroup = new ButtonGroup();
		
		// Set contents panel size in order to enable scroll pane
		int numberOfColumns = m_communicatorList.selectItems(m_communicatorSelector, m_communicatorComparator).size() / NUMBER_OF_ROWS + 1;
		m_contentsPanel.setMinimumSize(new Dimension(numberOfColumns * BUTTON_SIZE.width, 
				NUMBER_OF_ROWS * BUTTON_SIZE.height));
		m_contentsPanel.setPreferredSize(new Dimension(numberOfColumns * BUTTON_SIZE.width, 
				NUMBER_OF_ROWS * BUTTON_SIZE.height));
		m_contentsPanel.setMaximumSize(new Dimension(numberOfColumns * BUTTON_SIZE.width, 
				NUMBER_OF_ROWS * BUTTON_SIZE.height));
		
		JPanel panel = null;
		int i = 0;
		for(ICommunicatorIf commnicator: m_communicatorList.selectItems(m_communicatorSelector , m_communicatorComparator))
		{
			if(i%NUMBER_OF_ROWS == 0)
			{
				panel = new JPanel();
				panel.setAlignmentX(Component.LEFT_ALIGNMENT);
				panel.setAlignmentY(Component.TOP_ALIGNMENT);
				panel.setPreferredSize(new Dimension(MessageLogPanel.SMALL_BUTTON_SIZE.width*3, 
						MessageLogPanel.SMALL_BUTTON_SIZE.height*NUMBER_OF_ROWS));
				panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
				m_contentsPanel.add(panel);
			}
			addUnitButton(commnicator, panel);
			i++;
		}
	}

	private void addUnitButton(ICommunicatorIf communicator, JPanel buttonPanel)
	{
		JToggleButton button = new JToggleButton();
		
		button.setMinimumSize(BUTTON_SIZE);
		button.setPreferredSize(BUTTON_SIZE);
		button.setMaximumSize(BUTTON_SIZE);
		
		if(communicator instanceof IUnitIf)
		{
			IUnitIf unit = (IUnitIf)communicator;
			button.setIcon(getUnitIcon(unit.getType()));
		}
		else if(communicator instanceof ICmdPostIf)
		{
			button.setIcon(getUnitIcon(IUnitIf.UnitType.COMMAND_POST));
		}
		
		button.setText(communicator.getCommunicatorNumber() + " " + communicator.getCallSign());
		button.setActionCommand(communicator.getCommunicatorNumberPrefix() + " " + communicator.getCommunicatorNumber());
		
		// Add action listeners to button
		for(ActionListener listener : m_actionListeners)
		{
			button.addActionListener(listener);
		}
		
		// De-selecting a button should result in standard sender value being used
		button.addMouseListener(m_buttonMouseListener);
		
		buttonPanel.add(button);
		m_buttonGroup.add(button);
	}

	
	/**
	 * Updates the type filter based on which buttons are pressed in the unit type selection pad
	 */
	public void actionPerformed(ActionEvent e)
	{
		try
		{
			String command = e.getActionCommand();
			UnitType type = UnitType.valueOf(command);
			setUnitTypeFilter(type);
			this.validate();
			this.repaint();
		}
		catch(Exception exc)
		{}
	}
	
	public Enumeration<AbstractButton> getButtons()
	{
		return m_buttonGroup.getElements();
	}

	public void addActionListener(UnitFieldSelectionDialog fromDialog)
	{
		m_actionListeners.add(fromDialog);
		Enumeration<AbstractButton> buttons = m_buttonGroup.getElements();
		AbstractButton button = null;
		while(buttons.hasMoreElements())
		{
			button = buttons.nextElement();
			button.addActionListener(fromDialog);
		}
	}
}
