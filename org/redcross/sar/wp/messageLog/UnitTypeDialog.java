package org.redcross.sar.wp.messageLog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.redcross.sar.gui.DiskoDialog;

public class UnitTypeDialog extends DiskoDialog
{
	private IDiskoWpMessageLog m_wp;
	private JPanel m_contentsPanel = null;
	
	
	private JButton m_planeButton = null;
	private JButton m_boatButton = null;
	private JButton m_dogButton = null;
	private JButton m_carButton = null;
	private JButton m_manButton = null;
	private JButton m_koButton = null;
	
	public UnitTypeDialog(IDiskoWpMessageLog wp)
	{
		super(wp.getApplication().getFrame());
		m_wp = wp;
		
		m_contentsPanel = new JPanel(new BorderLayout(3, 2));
		m_contentsPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
		
		initButtons();
		
		this.add(m_contentsPanel);
		this.pack();
	}
	
	private void initButtons()
	{
		m_planeButton = new JButton("Plane");
		m_planeButton.setMinimumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		m_contentsPanel.add(m_planeButton);
		
		m_boatButton = new JButton("Boat");
		m_boatButton.setMinimumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		m_contentsPanel.add(m_boatButton);
		
		m_dogButton = new JButton("Dog");
		m_dogButton.setMinimumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		m_contentsPanel.add(m_dogButton);
		
		m_carButton = new JButton("Car");
		m_carButton.setMinimumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		m_contentsPanel.add(m_carButton);
		
		m_manButton = new JButton("Man");
		m_manButton.setMinimumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		m_contentsPanel.add(m_manButton);
		
		m_koButton = new JButton("KO");
		m_koButton.setMinimumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		m_contentsPanel.add(m_koButton);
	}

}
