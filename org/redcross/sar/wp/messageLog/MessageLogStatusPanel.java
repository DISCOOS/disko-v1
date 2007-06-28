/*
 * Author: Thomas Landvik
 * Created: 27.06.07
 */
package org.redcross.sar.wp.messageLog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.redcross.sar.app.Utils;
import org.redcross.sar.util.mso.DTG;

import com.sun.corba.se.impl.javax.rmi.CORBA.Util;

public class MessageLogStatusPanel extends JPanel
{
	private final static Dimension BUTTON_DIMENSION = new Dimension(60, 60);
	private final static int PANEL_HEIGHT = 180;
	private final static int SMALL_PANEL_WIDTH = 64;
	
	private JPanel m_nrPanel;
	private JLabel m_nrLabel;
	
	private JPanel m_dtgPanel;
	private JLabel m_dtgLabel;
    private JButton m_changeDTGButton;
    
    private JPanel m_fromPanel;
    private JLabel m_fromLabel;
    private JButton m_changeFromButton;
    
    private JPanel m_toPanel;
    private JLabel m_toLabel;
    private JButton m_changeToButton;
    
    private JPanel m_messagePanel;
    
    private JPanel m_taskPanel;
    private JLabel m_taskLabel;
    private JButton m_changeTaskButton;
    
    private JPanel m_statusPanel;
    private JButton m_cancelStatusButton;
    private JButton m_waitEndStatusButton;
    private JButton m_finishedStatusButton;
    
    public MessageLogStatusPanel()
    {
    	initPanels();
    }
    
    public void setNr(int nr)
    {
    	m_nrLabel.setText(Integer.toString(nr));
    }
    
    public void setDTG(DTG dtg)
    {
    	m_dtgLabel.setText(dtg.toString());
    }
    
    public void setFrom(String from)
    {
    	m_fromLabel.setText(from);
    }
    
    public void setTo(String to)
    {
    	m_toLabel.setText(to);
    }
    
    public void setTask(String task)
    {
    	m_taskLabel.setText(task);
    }
    
    private void createPanel(JPanel panel, int width, int height, String labelString, JLabel infoLabel, JButton button, GridBagConstraints gbc)
    {
    	panel = new JPanel();
    	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    	GridBagConstraints gbcLocal = new GridBagConstraints();
    	panel.setBorder(BorderFactory.createLineBorder(Color.black));
    	panel.setMinimumSize(new Dimension(width, height));
    	panel.setPreferredSize(new Dimension(width, height));
    	
    	// Top row label
        JLabel label = new JLabel(labelString);
        //label.setBorder(BorderFactory.createLineBorder(Color.black));
        label.setAlignmentY(Component.TOP_ALIGNMENT);
        panel.add(label);
        
        panel.add(Box.createVerticalGlue());
        
        // Info row label
        infoLabel = new JLabel();
        infoLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        panel.add(infoLabel, gbcLocal);
        
        panel.add(Box.createVerticalGlue());
        
        // Add button if not already created
        if(button == null)
        {
        	button = new JButton();
        	button.setIcon(createImageIcon("icons/60x60/change.gif"));
        	button.setMaximumSize(BUTTON_DIMENSION);
            button.setMinimumSize(BUTTON_DIMENSION);
            button.setPreferredSize(BUTTON_DIMENSION);
            button.setAlignmentY(Component.BOTTOM_ALIGNMENT);
            panel.add(button, gbcLocal);
        }
        
    	this.add(panel, gbc);
    }
    
    protected static ImageIcon createImageIcon(String path)
	{
		ImageIcon icon = null;
		try
		{
			icon = Utils.createImageIcon(path, null);
		}
		catch(Exception e)
		{
			System.err.println("Error loading icon: " + path);
		}
		
		return icon;
	}
    
    private void initPanels()
    {
    	this.setLayout(new GridBagLayout());
    	GridBagConstraints gbc = new GridBagConstraints();
    	gbc.fill = GridBagConstraints.BOTH;
    	gbc.weightx = 0.0;
    	gbc.weighty = 1.0;
    	
    	// Nr panel
    	gbc.gridx = 0;
    	gbc.gridy = 0;
        createPanel(m_nrPanel, SMALL_PANEL_WIDTH, PANEL_HEIGHT, "Nr", m_nrLabel, new JButton(), gbc);
       
        // DTG panel
        gbc.gridx = 1;
        createPanel(m_dtgPanel, SMALL_PANEL_WIDTH, PANEL_HEIGHT, "DTG", m_dtgLabel, m_changeDTGButton, gbc);
        
        // From panel
        gbc.gridx = 2;
        createPanel(m_fromPanel, SMALL_PANEL_WIDTH, PANEL_HEIGHT, "Fra", m_fromLabel, m_changeFromButton, gbc);

        // To panel
        gbc.gridx = 3;
        createPanel(m_toPanel, SMALL_PANEL_WIDTH, PANEL_HEIGHT, "Til", m_toLabel, m_changeToButton, gbc);
        
        // Message panel
        gbc.weightx = 1.0;
        gbc.gridx = 4;
        m_messagePanel = new MessageStatusPanel();
        this.add(m_messagePanel, gbc);
        
        // Task panel
        gbc.weightx = 0.0;
        gbc.gridx = 5;
        createPanel(m_taskPanel, 2*SMALL_PANEL_WIDTH, PANEL_HEIGHT, "Oppgave", m_taskLabel, m_changeTaskButton, gbc);
        
        // Status panel
        gbc.gridx = 6;
        gbc.weightx = 0.0;
        m_statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 1));
        m_statusPanel.setMinimumSize(new Dimension(SMALL_PANEL_WIDTH, PANEL_HEIGHT));
        m_statusPanel.setPreferredSize(new Dimension(SMALL_PANEL_WIDTH, PANEL_HEIGHT));
        m_statusPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        m_cancelStatusButton = new JButton();
        m_cancelStatusButton.setIcon(createImageIcon("icons/60x60/abort.gif"));
        m_cancelStatusButton.setMinimumSize(BUTTON_DIMENSION);
        m_cancelStatusButton.setPreferredSize(BUTTON_DIMENSION);
        m_statusPanel.add(m_cancelStatusButton, BorderLayout.NORTH);
        m_waitEndStatusButton = new JButton("Vent \nSlutt");
        m_waitEndStatusButton.setMaximumSize(BUTTON_DIMENSION);
        m_waitEndStatusButton.setMinimumSize(BUTTON_DIMENSION);
        m_waitEndStatusButton.setPreferredSize(BUTTON_DIMENSION);
        m_statusPanel.add(m_waitEndStatusButton, BorderLayout.CENTER);
        m_finishedStatusButton = new JButton();
        m_finishedStatusButton.setIcon(createImageIcon("icons/60x60/finish.gif"));
        m_finishedStatusButton.setMinimumSize(BUTTON_DIMENSION);
        m_finishedStatusButton.setPreferredSize(BUTTON_DIMENSION);
        m_statusPanel.add(m_finishedStatusButton, BorderLayout.SOUTH);
        this.add(m_statusPanel, gbc);
    }
}
