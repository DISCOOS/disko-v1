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
import javax.swing.SwingConstants;

import org.redcross.sar.app.Utils;
import org.redcross.sar.util.mso.DTG;

import com.sun.corba.se.impl.javax.rmi.CORBA.Util;

public class MessageEditPanel extends JPanel
{
	private final static Dimension BUTTON_DIMENSION = new Dimension(60, 60);
	private final static int PANEL_HEIGHT = 180;
	public final static int SMALL_PANEL_WIDTH = 64;
	
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
    
    private MessageEditStatusPanel m_messagePanel;
    
    private JPanel m_taskPanel;
    private JLabel m_taskLabel;
    private JButton m_changeTaskButton;
    
    private JPanel m_statusPanel;
    private JButton m_cancelStatusButton;
    private JButton m_waitEndStatusButton;
    private JButton m_finishedStatusButton;
    
    public MessageEditPanel()
    {
    	initPanels();
    }
    
    public void setNr(int nr)
    {
    	m_nrLabel.setText(Integer.toString(nr));
    }
    
    public void setDTG(String dtg)
    {
    	m_dtgLabel.setText(dtg);
    }
    
    public void setFrom(String from)
    {
    	m_fromLabel.setText(from);
    }
    
    public void setTo(String to)
    {
    	m_toLabel.setText(to);
    }
    
    public void setMessage(String[] messageString)
    {
    	m_messagePanel.setText(messageString);
    }
    
    public void setTask(String task)
    {
    	m_taskLabel.setText(task);
    }
    
    private JPanel createPanel(int width, int height, String labelString, GridBagConstraints gbc)
    {
    	JPanel panel = new JPanel();
    	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    	panel.setBorder(BorderFactory.createLineBorder(Color.black));
    	panel.setMinimumSize(new Dimension(width, height));
    	panel.setPreferredSize(new Dimension(width, height));
    	
    	// Top row label
        JLabel label = new JLabel(labelString);
        //label.setBorder(BorderFactory.createLineBorder(Color.black));
        label.setAlignmentY(Component.TOP_ALIGNMENT);
        panel.add(label);
        
    	this.add(panel, gbc);
    	return panel;
    }
    
    private JButton createChangeButton()
    {
    	JButton button = new JButton();
    	button.setIcon(createImageIcon("icons/60x60/change.gif"));
    	button.setMaximumSize(BUTTON_DIMENSION);
        button.setMinimumSize(BUTTON_DIMENSION);
        button.setPreferredSize(BUTTON_DIMENSION);
        button.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        return button;
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
        m_nrPanel = createPanel(SMALL_PANEL_WIDTH/2, PANEL_HEIGHT, "Nr", gbc);
        m_nrLabel = new JLabel();
        m_nrLabel.setAlignmentY(Component.TOP_ALIGNMENT);
        m_nrPanel.add(m_nrLabel);
        m_nrPanel.add(Box.createVerticalGlue());
       
        // DTG panel
        gbc.gridx = 1;
        m_dtgPanel = createPanel(SMALL_PANEL_WIDTH, PANEL_HEIGHT, "DTG", gbc);
        m_dtgLabel = new JLabel();
        m_dtgLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        m_dtgPanel.add(m_dtgLabel);
        m_changeDTGButton = createChangeButton();
        m_dtgPanel.add(Box.createVerticalGlue());
        m_dtgPanel.add(m_changeDTGButton);
        
        // From panel
        gbc.gridx = 2;
        m_fromPanel = createPanel(SMALL_PANEL_WIDTH, PANEL_HEIGHT, "Fra", gbc);
        m_fromLabel = new JLabel();
        m_fromPanel.add(m_fromLabel);
        m_changeFromButton = createChangeButton();
        m_fromPanel.add(Box.createVerticalGlue());
        m_fromPanel.add(m_changeFromButton);

        // To panel
        gbc.gridx = 3;
        m_toPanel = createPanel(SMALL_PANEL_WIDTH, PANEL_HEIGHT, "Til", gbc);
        m_toLabel = new JLabel();
        m_toPanel.add(m_toLabel);
        m_changeToButton = createChangeButton();
        m_toPanel.add(Box.createVerticalGlue());
        m_toPanel.add(m_changeToButton);
        
        // Message panel
        gbc.weightx = 1.0;
        gbc.gridx = 4;
        m_messagePanel = new MessageEditStatusPanel();
        this.add(m_messagePanel, gbc);
        
        // Task panel
        gbc.weightx = 0.0;
        gbc.gridx = 5;
        m_taskPanel = createPanel(2*SMALL_PANEL_WIDTH, PANEL_HEIGHT, "Oppgave", gbc);
        m_taskLabel = new JLabel();
        m_taskLabel.setAlignmentY(Component.TOP_ALIGNMENT);
        m_taskPanel.add(m_taskLabel);
        m_changeTaskButton = createChangeButton();
        m_taskPanel.add(Box.createVerticalGlue());
        m_taskPanel.add(m_changeTaskButton);
        
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