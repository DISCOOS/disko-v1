package org.redcross.sar.wp.messageLog;

import org.redcross.sar.gui.DiskoButtonFactory;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.*;
import org.redcross.sar.mso.data.ICmdPostIf.CmdPostStatus;
import org.redcross.sar.mso.data.IUnitIf.UnitStatus;
import org.redcross.sar.mso.data.IUnitIf.UnitType;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.Update;
import org.redcross.sar.util.mso.Selector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Dialog containing a list of all units  in command post communicator list
 * 
 * @author thomasl
 */
public class SingleUnitListSelectionDialog extends DiskoDialog implements IEditMessageComponentIf, IMsoUpdateListenerIf, ActionListener
{
	private static final long serialVersionUID = 1L;
	
	protected JPanel m_contentsPanel = null;
	protected JScrollPane m_scrollPane = null;
	protected IDiskoWpMessageLog m_wpMessageLog;
	protected UnitType m_unitTypeFilter = null;
	protected IUnitListIf m_unitList = null;
	protected AbstractDerivedList<ICommunicatorIf> m_communicatorList;
	protected List<ActionListener> m_actionListeners;
	protected ButtonGroup m_buttonGroup = null;
	protected boolean m_senderList = true;
	protected HashMap<JToggleButton, ICommunicatorIf> m_buttonCommunicatorMap = null;
	protected HashMap<ICommunicatorIf, JToggleButton> m_communicatorButtonMap = null;
	protected JToggleButton m_currentButton = null;

	final private static Dimension BUTTON_SIZE = new Dimension(MessageLogPanel.SMALL_BUTTON_SIZE.width*3,
			MessageLogPanel.SMALL_BUTTON_SIZE.height);
	final public static int PANEL_WIDTH = BUTTON_SIZE.width * 5 ;
	private final int NUMBER_OF_ROWS = 7;

	/**
	 * @param wp Message log work process
	 * @param senderList Whether dialog is changing the sender field of a message or not
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

		m_buttonCommunicatorMap = new HashMap<JToggleButton, ICommunicatorIf>();
		m_communicatorButtonMap = new HashMap<ICommunicatorIf, JToggleButton>();

		m_buttonGroup = new ButtonGroup();

		initContentsPanel();
		this.pack();
	}

	/**
	 * Sorts units based on  number
	 */
	private Comparator<ICommunicatorIf> m_communicatorComparator = new Comparator<ICommunicatorIf>()
	{
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
	private final EnumSet<UnitStatus> m_activeUnitStatusSet = EnumSet.range(UnitStatus.READY, UnitStatus.PENDING);
	private final EnumSet<CmdPostStatus> m_activeCmdPostStatusSet = EnumSet.of(CmdPostStatus.IDLE, CmdPostStatus.OPERATING, CmdPostStatus.PAUSED);
	private Selector<ICommunicatorIf> m_communicatorSelector = new Selector<ICommunicatorIf>()
	{
		public boolean select(ICommunicatorIf communicator)
		{
			if(communicator instanceof ICmdPostIf )
			{
				// Command post should only be selected if the type filter is set to cp and the cp is active
				ICmdPostIf cmdPost = (ICmdPostIf)communicator;
				return m_activeCmdPostStatusSet.contains(cmdPost.getStatus()) &&
						(m_unitTypeFilter == UnitType.COMMAND_POST || m_unitTypeFilter == null);

			}
			else if(communicator instanceof IUnitIf)
			{
				// Unit should be selected if it is active, and the unit type filter match
				IUnitIf unit = (IUnitIf)communicator;
				return m_activeUnitStatusSet.contains(unit.getStatus()) &&
						(m_unitTypeFilter == unit.getType() || m_unitTypeFilter == null);
			}
			else
			{
				return false;
			}
		}
	};

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
		
		buildList();
		
		this.pack();
	}

	/**
	 * 
	 */
	public void clearContents()
	{
	}

	/**
	 *
	 */
	public void hideComponent()
	{
		this.setVisible(false);
	}

	/**
	 * Search communicator list and mark unit as selected
	 */
	public void newMessageSelected(IMessageIf message)
	{
		m_buttonGroup.clearSelection();
		m_unitTypeFilter = null;

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

		// Get communicator button and mark it
		JToggleButton button = m_communicatorButtonMap.get(communicator);

		if(button != null)
		{
			button.setSelected(true);
			m_currentButton = button;
		}
	}

	/**
	 * 
	 */
	public void showComponent()
	{
		this.setVisible(true);
	}

	/**
	 * Updates unit list based on MSO communicator events
	 * @see org.redcross.sar.mso.event.IMsoUpdateListenerIf#handleMsoUpdateEvent(org.redcross.sar.mso.event.MsoEvent.Update)
	 */
	public void handleMsoUpdateEvent(Update e)
	{
		buildList();
	}

	private final EnumSet<IMsoManagerIf.MsoClassCode> myInterests = EnumSet.of(IMsoManagerIf.MsoClassCode.CLASSCODE_MESSAGE, IMsoManagerIf.MsoClassCode.CLASSCODE_MESSAGELINE);
	/**
	 * Interested in message and message line updates
	 */
	public boolean hasInterestIn(IMsoObjectIf msoObject)
	{
		return myInterests.contains(msoObject.getMsoClassCode());
	}

	/**
	 * Gets which unit type the filter is set to
	 * @return Type
	 */
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

		m_buttonCommunicatorMap.clear();
		m_communicatorButtonMap.clear();

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
			this.addUnitButton(commnicator, panel);
			i++;
		}
	}

	private void addUnitButton(final ICommunicatorIf communicator, JPanel buttonPanel)
	{
		JToggleButton button = DiskoButtonFactory.createLargeToggleButton(communicator);

		m_buttonCommunicatorMap.put(button, communicator);
		m_communicatorButtonMap.put(communicator, button);

		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				JToggleButton sourceButton  = (JToggleButton)arg0.getSource();
				if(m_currentButton == sourceButton)
				{
					// Deselecting a button should result in standard sender value being used
					m_currentButton = null;
					ICommunicatorIf newCommunicator = m_wpMessageLog.getMsoManager().getCmdPostCommunicator();
					m_communicatorButtonMap.get(newCommunicator).doClick();
				}
				else
				{
					m_currentButton = sourceButton;
					// Select communicator
					IMessageIf message = MessageLogTopPanel.getCurrentMessage(true);
					if(m_senderList)
					{
						message.setSender(communicator);
					}
					else
					{
						message.setSingleReceiver(communicator);
					}
				}				
				
				fireDialogFinished();
			}
		});

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

	/**
	 * Registers a unit field dialog so it can listen to selection changes and update itself accordingly
	 * @param fromDialog
	 */
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

	/**
	 * Deselect buttons
	 */
	public void clearSelection()
	{
		m_buttonGroup.clearSelection();
	}
}
