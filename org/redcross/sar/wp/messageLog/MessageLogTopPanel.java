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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import org.redcross.sar.app.Utils;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLogIf;
import org.redcross.sar.util.mso.DTG;
import org.redcross.sar.util.mso.Selector;

import com.sun.corba.se.impl.javax.rmi.CORBA.Util;

public class MessageLogTopPanel extends JPanel implements ActionListener
{
	private final static Dimension BUTTON_DIMENSION = new Dimension(60, 60);
	public final static int PANEL_HEIGHT = (BUTTON_DIMENSION.height) * 3 + 42;
	public final static int SMALL_PANEL_WIDTH = 64;
	
	IMessageLogIf m_messageLog;
	private int m_currentMessage;
	
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
    
    private MessagePanel m_messagePanel;
    
    private JPanel m_taskPanel;
    private JLabel m_taskLabel;
    private JButton m_changeTaskButton;
    
    private JPanel m_statusPanel;
    private JButton m_cancelStatusButton;
    private JButton m_waitEndStatusButton;
    private JButton m_finishedStatusButton;
    
    public MessageLogTopPanel(IMessageLogIf messageLog)
    {
    	m_messageLog = messageLog;
    	initPanels();
    }
    
    private JPanel createPanel(int width, int height, String labelString, GridBagConstraints gbc)
    {
    	JPanel panel = new JPanel();
    	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    	panel.setMinimumSize(new Dimension(width-1, height-1));
    	panel.setPreferredSize(new Dimension(width, height));
    	panel.setMaximumSize(new Dimension(width+1, height+1));
    	
    	// Top row label
        JLabel label = new JLabel(labelString);

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
        m_nrPanel = createPanel(SMALL_PANEL_WIDTH/2, PANEL_HEIGHT, "Nr", gbc);
        m_nrPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        m_nrLabel = new JLabel();
        m_nrPanel.add(m_nrLabel);
        m_nrPanel.add(Box.createVerticalGlue());
        m_nrPanel.add(Box.createRigidArea(BUTTON_DIMENSION));
        gbc.gridx++;
        this.add(new JSeparator(SwingConstants.VERTICAL), gbc);
       
        // DTG panel
        gbc.gridx++;
        m_dtgPanel = createPanel(SMALL_PANEL_WIDTH, PANEL_HEIGHT, "DTG", gbc);
        m_dtgPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        m_dtgLabel = new JLabel();
        m_dtgPanel.add(m_dtgLabel);
        m_changeDTGButton = createChangeButton();
        m_changeDTGButton.addActionListener(this);
        m_changeDTGButton.setActionCommand("CHANGE_DTG");
        m_dtgPanel.add(Box.createVerticalGlue());
        m_dtgPanel.add(m_changeDTGButton);
        gbc.gridx++;
        this.add(new JSeparator(SwingConstants.VERTICAL), gbc);
        
        // From panel
        gbc.gridx++;
        m_fromPanel = createPanel(SMALL_PANEL_WIDTH, PANEL_HEIGHT, "Fra", gbc);
        m_fromPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        m_fromLabel = new JLabel();
        m_fromPanel.add(m_fromLabel);
        m_changeFromButton = createChangeButton();
        m_changeFromButton.addActionListener(this);
        m_changeFromButton.setActionCommand("CHANGE_FROM");
        m_fromPanel.add(Box.createVerticalGlue());
        m_fromPanel.add(m_changeFromButton);
        gbc.gridx++;
        this.add(new JSeparator(SwingConstants.VERTICAL), gbc);

        // To panel
        gbc.gridx++;
        m_toPanel = createPanel(SMALL_PANEL_WIDTH, PANEL_HEIGHT, "Til", gbc);
        m_toPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        m_toLabel = new JLabel();
        m_toPanel.add(m_toLabel);
        m_changeToButton = createChangeButton();
        m_changeToButton.addActionListener(this);
        m_changeToButton.setActionCommand("CHANGE_TO");
        m_toPanel.add(Box.createVerticalGlue());
        m_toPanel.add(m_changeToButton);
        gbc.gridx++;
        this.add(new JSeparator(SwingConstants.VERTICAL), gbc);
        
        // Message panel
        gbc.gridx++;
        gbc.weightx = 1.0;
        m_messagePanel = new MessagePanel();
        this.add(m_messagePanel, gbc);
        gbc.gridx++;
        this.add(new JSeparator(SwingConstants.VERTICAL), gbc);
        
        // Task panel
        gbc.gridx++;
        gbc.weightx = 0.0;
        m_taskPanel = createPanel(2*SMALL_PANEL_WIDTH, PANEL_HEIGHT, "Oppgave", gbc);
        m_taskPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        m_taskLabel = new JLabel();
        m_taskPanel.add(m_taskLabel);
        m_changeTaskButton = createChangeButton();
        m_changeTaskButton.addActionListener(this);
        m_changeTaskButton.setActionCommand("CHANGE_TASK");
        m_taskPanel.add(m_changeTaskButton);
        gbc.gridx++;
        this.add(new JSeparator(SwingConstants.VERTICAL), gbc);
        
        // Status panel
        gbc.gridx++;
        m_statusPanel = new JPanel();
        m_statusPanel.setMinimumSize(new Dimension(SMALL_PANEL_WIDTH, PANEL_HEIGHT));
        m_statusPanel.setPreferredSize(new Dimension(SMALL_PANEL_WIDTH, PANEL_HEIGHT));
        m_statusPanel.setMaximumSize(new Dimension(SMALL_PANEL_WIDTH, PANEL_HEIGHT));
        m_statusPanel.add(new JLabel(" "));
        m_statusPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        m_statusPanel.add(Box.createVerticalGlue());
        
        m_cancelStatusButton = new JButton();
        m_cancelStatusButton.setIcon(createImageIcon("icons/60x60/abort.gif"));
        m_cancelStatusButton.setMinimumSize(BUTTON_DIMENSION);
        m_cancelStatusButton.setPreferredSize(BUTTON_DIMENSION);
        m_statusPanel.add(m_cancelStatusButton);
        m_waitEndStatusButton = new JButton("Vent \nSlutt");
        m_waitEndStatusButton.setMaximumSize(BUTTON_DIMENSION);
        m_waitEndStatusButton.setMinimumSize(BUTTON_DIMENSION);
        m_waitEndStatusButton.setPreferredSize(BUTTON_DIMENSION);
        m_statusPanel.add(m_waitEndStatusButton);
        m_finishedStatusButton = new JButton();
        m_finishedStatusButton.setIcon(createImageIcon("icons/60x60/finish.gif"));
        m_finishedStatusButton.setMinimumSize(BUTTON_DIMENSION);
        m_finishedStatusButton.setPreferredSize(BUTTON_DIMENSION);
        m_statusPanel.add(m_finishedStatusButton);
        this.add(m_statusPanel, gbc);
    }

	public void newMessageSelected(int messageNr) 
	{
		m_currentMessage = messageNr;
		
		// Get the message
		List<IMessageIf> messages = m_messageLog.selectItems(m_messageSelector, m_lineNumberComparator);
		IMessageIf message = messages.get(0);
		
		// Update contents
		m_nrLabel.setText(Integer.toString(message.getNumber()));
		m_dtgLabel.setText(message.getDTG());
		//m_fromLabel.setText(message.get); 
		//m_toLabel.setText(message.get); //
		m_messagePanel.newMessageSelected(message);
		//m_taskLabel.setText(message.get);
	}
	
	private final Selector<IMessageIf> m_messageSelector = new Selector<IMessageIf>()
    {
        public boolean select(IMessageIf aMessage)
        {
        	if(aMessage.getNumber() == m_currentMessage)
        	{
        		return true;
        	}
        	else
        	{
        		return false;
        	}
        }
    };
    private final static Comparator<IMessageIf> m_lineNumberComparator = new Comparator<IMessageIf>()
    {
        public int compare(IMessageIf m1, IMessageIf m2)
        {
            return m1.getNumber() - m2.getNumber();
        }
    };

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// TODO Auto-generated method stub
		
	}
}
