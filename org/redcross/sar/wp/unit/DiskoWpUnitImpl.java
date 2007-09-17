package org.redcross.sar.wp.unit;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.JTableHeader;

import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.gui.DiskoButtonFactory;
import org.redcross.sar.wp.AbstractDiskoWpModule;

/**
 * Implementation of the Unit work process
 * 
 * @author thomasl
 */
public class DiskoWpUnitImpl extends AbstractDiskoWpModule implements IDiskoWpUnit
{
	private JPanel m_contentsPanel;
	
	private PersonnelTransferHandler m_personnelTransferHandler;
	
	private JTabbedPane m_overviewTabPane;
	private JTable m_personnelOverviewTable;
	private JTable m_unitOverviewTable;
	private JTable m_calloutOverviewTable;
	
	private JPanel m_detailsPanel;
	private PersonnelDetailsPanel m_personnelDetailsPanel;
	private UnitDetailsPanel m_unitDetailsPanel;
	private CalloutDetailsPanel m_calloutDetailsPanel;
	
	private JPanel m_bottomPanel;
	
	private JButton m_newPersonnelButton;
	private JButton m_newUnitButton;
	private JButton m_importCalloutButton;
	private JButton m_deleteButton;
	
	private static final String PERSONNEL_VIEW_ID = "PERSONNEL_VIEW";
	private static final String UNIT_VIEW_ID = "UNIT_VIEW";
	private static final String CALLOUT_VIEW_ID = "CALLOUT_VIEW";

	public DiskoWpUnitImpl(IDiskoRole role)
	{
		super(role);
		
		// Initialize transfer handler
		try
		{
			m_personnelTransferHandler = new PersonnelTransferHandler();
		} 
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
		initialize();
		initButtons();
		initTables();
	}
	
	private void initialize()
	{
		// Properties
		loadProperties("properties");
		assignWpBundle("org.redcross.sar.wp.unit.unit");
		
		// Main panels
		m_contentsPanel = new JPanel(new BorderLayout());
		layoutComponent(m_contentsPanel);
		
		// Detail panels
		m_detailsPanel = new JPanel(new CardLayout());
		m_detailsPanel.setPreferredSize(new Dimension(350, 500));
		m_personnelDetailsPanel = new PersonnelDetailsPanel(this);
		m_detailsPanel.add(m_personnelDetailsPanel, PERSONNEL_VIEW_ID);
		m_unitDetailsPanel = new UnitDetailsPanel();
		m_detailsPanel.add(m_unitDetailsPanel, UNIT_VIEW_ID);
		m_calloutDetailsPanel = new CalloutDetailsPanel();
		m_detailsPanel.add(m_calloutDetailsPanel, CALLOUT_VIEW_ID);
		
		// Bottom panels
		m_bottomPanel = new JPanel(new CardLayout());
		
		// Overview panels
		m_overviewTabPane = new JTabbedPane();
		m_overviewTabPane.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent arg0)
			{
				int selectedIndex = m_overviewTabPane.getSelectedIndex();
				
				String componentId = null;
				switch(selectedIndex)
				{
				case 0:
					componentId = PERSONNEL_VIEW_ID;
					break;
				case 1:
					componentId = UNIT_VIEW_ID;
					break;
				case 2:
					componentId = CALLOUT_VIEW_ID;
					break;
				default:
					componentId = null;
					
				}
				CardLayout detailsLayout = (CardLayout)m_detailsPanel.getLayout();
				detailsLayout.show(m_detailsPanel, componentId);
				CardLayout bottomLayout = (CardLayout)m_bottomPanel.getLayout();
				bottomLayout.show(m_bottomPanel, componentId);
				
			}
		});
			
		// Set up splitters
		JSplitPane horSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		horSplit.setDividerLocation(0.4);
		horSplit.setLeftComponent(m_detailsPanel);
		horSplit.setRightComponent(m_overviewTabPane);
		JSplitPane vertSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		vertSplit.setLeftComponent(horSplit);
		vertSplit.setRightComponent(m_bottomPanel);
		vertSplit.setDividerLocation(0.75);
		m_contentsPanel.add(vertSplit, BorderLayout.CENTER);
	}

	private void initTables()
	{
		// Personnel
		PersonnelOverviewTableModel personnelModel = new PersonnelOverviewTableModel(this);
		m_personnelOverviewTable = new JTable(personnelModel);
		m_personnelOverviewTable.setTransferHandler(m_personnelTransferHandler);
		m_personnelOverviewTable.setDragEnabled(true);
		
		JTableHeader tableHeader = m_personnelOverviewTable.getTableHeader();
        tableHeader.setResizingAllowed(false);
        tableHeader.setReorderingAllowed(false);
		
		JScrollPane personnelOverviewScrollPane = new JScrollPane(m_personnelOverviewTable);
		m_overviewTabPane.addTab(getText("Personnel.text"), personnelOverviewScrollPane);
		
		// Unit
		UnitOverviewTableModel unitModel = new UnitOverviewTableModel(this);
		m_unitOverviewTable = new JTable(unitModel);
		
		tableHeader = m_unitOverviewTable.getTableHeader();
        tableHeader.setResizingAllowed(false);
        tableHeader.setReorderingAllowed(false);
        
		JScrollPane unitOverviewScrollPane = new JScrollPane(m_unitOverviewTable); 
		m_overviewTabPane.addTab(getText("Unit.text"), unitOverviewScrollPane);
		
		// Call-out
		CalloutOverviewTableModel calloutModel = new CalloutOverviewTableModel(this);
		m_calloutOverviewTable = new JTable(calloutModel);
		
		tableHeader = m_calloutOverviewTable.getTableHeader();
        tableHeader.setResizingAllowed(false);
        tableHeader.setReorderingAllowed(false);
		
		JScrollPane OverviewScrollPane = new JScrollPane(m_calloutOverviewTable);
		m_overviewTabPane.addTab(getText("Callout.text"), OverviewScrollPane);
	}
	
	private void initButtons()
	{
		m_newPersonnelButton = DiskoButtonFactory.createSmallButton(getText("NewPersonnelButton.text")/*, 
				getText("NewPersonnelButton.icon")*/);
		m_newPersonnelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				newPersonnel();
			}
		});
		layoutButton(m_newPersonnelButton);
		
		m_newUnitButton = DiskoButtonFactory.createSmallButton(getText("NewUnitButton.text")/*, 
				getText("NewUnitButton.icon")*/);
		layoutButton(m_newUnitButton);
		
		m_importCalloutButton = DiskoButtonFactory.createSmallButton(getText("ImportAlertButton.text")/*, 
			getText("ImportAlertButton.icon")*/);
		layoutButton(m_importCalloutButton);
		
		m_deleteButton = DiskoButtonFactory.createSmallButton("", getText("DeleteButton.icon"));
		layoutButton(m_deleteButton);
	}

	@Override
	public String getName()
	{
		return this.getText("Enhet");
	}
	
	@Override
	public void activated()
	{
		super.activated();
	}
		@Override
	public void deactivated()
	{
		super.deactivated();
	}

	public void cancel()
	{
		m_personnelDetailsPanel.setPersonnel(null);
		this.getMsoModel().rollback();
	}

	public void finish()
	{
		m_personnelDetailsPanel.savePersonnel();
		this.getMsoModel().commit();
	}
	
	private void newPersonnel()
	{
		// View personnel details
		CardLayout layout = (CardLayout)m_detailsPanel.getLayout();
		layout.show(m_detailsPanel, PERSONNEL_VIEW_ID);
		
		m_personnelDetailsPanel.setPersonnel(null);
	}
}
