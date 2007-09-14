package org.redcross.sar.wp.unit;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
	
	private JTabbedPane m_overviewTabPane;
	
	private JPanel m_detailsPanel;
	private TeamDetailsPanel m_teamDetailsPanel;
	private UnitDetailsPanel m_unitDetailsPanel;
	private AlertDetailsPanel m_alertDetailsPanel;
	
	private JPanel m_bottomPanel;
	
	private JButton m_newTeamButton;
	private JButton m_newUnitButton;
	private JButton m_importAlertButton;
	private JButton m_deleteButton;
	
	private static final String TEAM_VIEW_ID = "TEAM_VIEW";
	private static final String UNIT_VIEW_ID = "UNIT_VIEW";
	private static final String ALERT_VIEW_ID = "ALERT_VIEW";

	public DiskoWpUnitImpl(IDiskoRole role)
	{
		super(role);
		
		initialize();
		
		initButtons();
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
		m_teamDetailsPanel = new TeamDetailsPanel();
		m_detailsPanel.add(m_teamDetailsPanel, TEAM_VIEW_ID);
		m_unitDetailsPanel = new UnitDetailsPanel();
		m_detailsPanel.add(m_unitDetailsPanel, UNIT_VIEW_ID);
		m_alertDetailsPanel = new AlertDetailsPanel();
		m_detailsPanel.add(m_alertDetailsPanel, ALERT_VIEW_ID);
		
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
					componentId = TEAM_VIEW_ID;
					break;
				case 1:
					componentId = UNIT_VIEW_ID;
					break;
				case 2:
					componentId = ALERT_VIEW_ID;
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
		JScrollPane teamOverviewScrollPane = new JScrollPane();
		m_overviewTabPane.addTab(getText("Team.text"), teamOverviewScrollPane);
		JScrollPane unitOverviewScrollPane = new JScrollPane(); 
		m_overviewTabPane.addTab(getText("Unit.text"), unitOverviewScrollPane);
		JScrollPane alertOverviewScrollPane = new JScrollPane();
		m_overviewTabPane.addTab(getText("Alert.text"), alertOverviewScrollPane);
			
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
	
	private void initButtons()
	{
		m_newTeamButton = DiskoButtonFactory.createSmallButton(getText("NewTeamButton.text")/*, 
				getText("NewTeamButton.icon")*/);
		layoutButton(m_newTeamButton);
		
		m_newUnitButton = DiskoButtonFactory.createSmallButton(getText("NewUnitButton.text")/*, 
				getText("NewUnitButton.icon")*/);
		layoutButton(m_newUnitButton);
		
		m_importAlertButton = DiskoButtonFactory.createSmallButton(getText("ImportAlertButton.text")/*, 
			getText("ImportAlertButton.icon")*/);
		layoutButton(m_importAlertButton);
		
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
		// TODO Auto-generated method stub
		
	}

	public void finish()
	{
		// TODO Auto-generated method stub
		
	}
}
