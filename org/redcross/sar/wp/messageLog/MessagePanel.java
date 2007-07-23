/**
 * @author Thomas Landvik
 * This class define the edit panel GUI for the message log
 */

package org.redcross.sar.wp.messageLog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import org.redcross.sar.app.Utils;
import org.redcross.sar.mso.data.IMessageIf;

public class MessagePanel extends JPanel
{
	private final static Dimension BUTTON_DIMENSION = new Dimension(60, 60);
	private final static int PANEL_WIDTH = BUTTON_DIMENSION.width * 8 + 22;
	
	private JLabel m_topLabel;
	
	// Panels
	private JPanel m_currentContentsPanel;
	private MessageTextPanel m_messageTextPanel;
	private MessagePositionPanel m_messagePositionPanel;
	private MessageListPanel m_messageListPanel;
	
	// Action buttons
	private JPanel m_buttonRow;
	private JButton m_textButton;
	private JButton m_positionButton;
	private JButton m_findingButton;
	private JButton m_assignedButton;
	private JButton m_startedButton;
	private JButton m_completedButton;
	private JButton m_listButton;
	private JButton m_deleteButton;
	
	public MessagePanel()
	{
		initPanels();
	}
	
	private void initPanels()
	{
		setMinimumSize(new Dimension(PANEL_WIDTH, MessageLogTopPanel.PANEL_HEIGHT));
		setPreferredSize(new Dimension(PANEL_WIDTH, MessageLogTopPanel.PANEL_HEIGHT));
		
		m_messageTextPanel = new MessageTextPanel();
		m_messageListPanel = new MessageListPanel();
		m_messagePositionPanel = new MessagePositionPanel();
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 0.5;
		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.BOTH;
		
		initLabel();
		this.add(m_topLabel, gbc);
		
		gbc.gridy++;
		this.add(new JSeparator(JSeparator.HORIZONTAL), gbc);
		
		initCurrentContents();
		gbc.gridy++;
		gbc.weighty = 1.0;
		this.add(m_currentContentsPanel, gbc);
		
		gbc.gridy++;
		gbc.weighty = 0;
		initButtons();
		this.add(m_buttonRow, gbc);
	}
	
	private void initCurrentContents()
	{
		m_currentContentsPanel = new JPanel(new BorderLayout());
		m_currentContentsPanel.add(m_messageTextPanel, BorderLayout.CENTER);
	}
	
	private void initLabel()
	{
		m_topLabel = new JLabel("Melding");
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
	
	private JButton createButton(String text, String iconPath)
	{
		JButton button = new JButton();
		if(iconPath != null)
		{
			button.setIcon(createImageIcon(iconPath));
		}
		else
		{
			button.setText(text);
		}
		button.setMinimumSize(BUTTON_DIMENSION);
		button.setPreferredSize(BUTTON_DIMENSION);
		button.setActionCommand(text);
		return button;
	}
	
	private void initButtons()
	{
		m_buttonRow = new JPanel(new FlowLayout(FlowLayout.LEADING, 4, 0));
		
		m_textButton = createButton("TEXT", "icons/60x60/text.gif");
		m_textButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				m_topLabel.setText("Melding");
				m_currentContentsPanel.removeAll();
				m_currentContentsPanel.add(m_messageTextPanel);
				//m_currentContentsPanel.repaint();
			}	
		});
		m_buttonRow.add(m_textButton);
		
		m_positionButton = createButton("POI", null);
		m_positionButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				m_topLabel.setText("Posisjon");
				m_currentContentsPanel.removeAll();
			}	
		});
		m_buttonRow.add(m_positionButton);
		
		m_findingButton = createButton("FINDING", "icons/60x60/discovery.gif");
		m_findingButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				m_topLabel.setText("Funn");
			}	
		});
		m_buttonRow.add(m_findingButton);
		
		m_assignedButton = createButton("ASSIGNED", null);
		m_assignedButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				m_topLabel.setText("Tildelt");
			}
			
		});
		m_buttonRow.add(m_assignedButton);
		
		m_startedButton = createButton("STARTED", null);
		m_startedButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				m_topLabel.setText("Startet");
			}
			
		});
		m_buttonRow.add(m_startedButton);
		
		m_completedButton = createButton("COMPLETED", null);
		m_completedButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				m_topLabel.setText("Ferdig");
				
			}
			
		});
		m_buttonRow.add(m_completedButton);
		
		m_listButton = createButton("LIST", "icons/60x60/list.gif");
		m_listButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				m_topLabel.setText("Liste");
				m_currentContentsPanel.removeAll();
				m_currentContentsPanel.add(m_messageListPanel);
			}
			
		});
		m_buttonRow.add(m_listButton);
		
		m_deleteButton = createButton("DELETE", "icons/60x60/delete.gif");
		m_deleteButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				m_topLabel.setText("Slett");
			}
			
		});
		m_buttonRow.add(m_deleteButton);
	}

	public void newMessageSelected(IMessageIf message) 
	{
		m_messageTextPanel.updateContents(message);
		m_messageListPanel.updateContents(message);
	}
}
