package org.redcross.sar.wp.messageLog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.redcross.sar.app.Utils;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.data.ICommunicatorIf;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IUnitListIf;
import org.redcross.sar.mso.data.IUnitIf.UnitType;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.Update;
import org.redcross.sar.util.mso.Selector;

/**
 * 
 * @author thomasl
 *
 * Dialog containing all units
 */
public class UnitListSelectionDialog extends DiskoDialog implements IEditMessageDialogIf, IMsoUpdateListenerIf, ActionListener
{
	protected JPanel m_contentsPanel = null;
	protected JScrollPane m_scrollPane = null;
	protected IDiskoWpMessageLog m_wpMessageLog;
	protected UnitType m_unitTypeFilter = null;
	protected IUnitListIf m_unitList = null;
	
	private final Dimension BUTTON_SIZE = new Dimension(MessageLogPanel.SMALL_BUTTON_SIZE.width*3, 
			MessageLogPanel.SMALL_BUTTON_SIZE.height);
	private final int NUMBER_OF_ROWS = 7;
	
	/**
	 * Sorts units based on  number
	 */
	private Comparator<IUnitIf> m_unitComparator = new Comparator<IUnitIf>()
	{
		public int compare(IUnitIf o1, IUnitIf o2)
		{
			return o1.getNumber() - o2.getNumber();
		}	
	};
	
	/**
	 * Selects unit based on the unit type filter
	 */
	private Selector<IUnitIf> m_unitSelector = new Selector<IUnitIf>()
	{
		public boolean select(IUnitIf anObject)
		{
			if(m_unitTypeFilter == null)
			{
				return true;
			}
			else
			{
				if(anObject.getType() == m_unitTypeFilter)
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}	
	};
	
	public UnitListSelectionDialog(IDiskoWpMessageLog wp)
	{
		super(wp.getApplication().getFrame());
		
		m_wpMessageLog = wp;
		m_wpMessageLog.getMmsoEventManager().addClientUpdateListener(this);
		m_unitList = wp.getMsoManager().getCmdPost().getUnitList();
		
		initContentsPanel();
		this.pack();
	}
	
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
		m_contentsPanel.setPreferredSize(new Dimension(BUTTON_SIZE.width*5 + 6, 
				MessageLogPanel.SMALL_BUTTON_SIZE.height*NUMBER_OF_ROWS + 6));
		this.add(m_scrollPane);
		this.pack();
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
		
		// Set contents panel size in order to enable scroll pane
		int numberOfColumns = m_unitList.getItems().size() / NUMBER_OF_ROWS + 1;
		m_contentsPanel.setMinimumSize(new Dimension(numberOfColumns * BUTTON_SIZE.width, 
				NUMBER_OF_ROWS * BUTTON_SIZE.height));
		m_contentsPanel.setPreferredSize(new Dimension(numberOfColumns * BUTTON_SIZE.width, 
				NUMBER_OF_ROWS * BUTTON_SIZE.height));
		m_contentsPanel.setMaximumSize(new Dimension(numberOfColumns * BUTTON_SIZE.width, 
				NUMBER_OF_ROWS * BUTTON_SIZE.height));
		
		JPanel panel = null;
		int i = 0;
		for(IUnitIf unit : m_unitList.selectItems(m_unitSelector , m_unitComparator))
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
			addUnitButton(unit, panel);
			i++;
		}
	}

	private void addUnitButton(IUnitIf unit, JPanel buttonPanel)
	{
		JButton button = new JButton();
		
		button.setMinimumSize(BUTTON_SIZE);
		button.setPreferredSize(BUTTON_SIZE);
		button.setMaximumSize(BUTTON_SIZE);
		
		button.setIcon(getUnitIcon(unit.getType()));
		button.setText(unit.getNumber() + " " + unit.getCallSign());
		
		// TODO add action listener
		
		buttonPanel.add(button);
	}

	
	public void actionPerformed(ActionEvent e)
	{
		// TODO Auto-generated method stub
		
	}
}
