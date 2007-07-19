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

public class MessagePanel extends JPanel implements ActionListener
{
	private final static Dimension BUTTON_DIMENSION = new Dimension(60, 60);
	private final static int PANEL_WIDTH = BUTTON_DIMENSION.width * 8 + 22;
	
	private JLabel m_topLabel;
	
	// Panels
	private JPanel m_currentContentsPanel;
	private MessageTextPanel m_messageTextPanel;
	private MessagePositionPanel m_messagePositionPanel;
	
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
		super(new GridBagLayout());
		setMinimumSize(new Dimension(PANEL_WIDTH, MessageLogTopPanel.PANEL_HEIGHT));
		setPreferredSize(new Dimension(PANEL_WIDTH, MessageLogTopPanel.PANEL_HEIGHT));
		
		// Init panels
		m_messageTextPanel = new MessageTextPanel();
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 0;
		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.BOTH;
		initLabel(gbc);
		
		gbc.gridy++;
		this.add(new JSeparator(JSeparator.HORIZONTAL), gbc);
		
		gbc.gridy++;
		gbc.weighty = 1.0;
		initCurrentContents(gbc);
		
		gbc.gridy++;
		gbc.weighty = 0;
		initButtons(gbc);
	}
	
	private void initCurrentContents(GridBagConstraints gbc)
	{
		m_currentContentsPanel = new JPanel(new BorderLayout());
		m_currentContentsPanel.add(m_messageTextPanel, BorderLayout.CENTER);
		this.add(m_currentContentsPanel, gbc);
	}
	
	private void initLabel(GridBagConstraints gbc)
	{
		m_topLabel = new JLabel("Melding");
		this.add(m_topLabel, gbc);
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
	
	private void initButton(JButton button, String text, String iconPath)
	{
		button = new JButton();
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
		button.addActionListener(this);
		button.setActionCommand(text);
		m_buttonRow.add(button);
	}
	private void initButtons(GridBagConstraints gbc)
	{
		m_buttonRow = new JPanel(new FlowLayout(FlowLayout.LEADING, 4, 0));
		
		initButton(m_textButton, "TEXT", "icons/60x60/text.gif");
		initButton(m_positionButton, "POI", null);
		initButton(m_findingButton, "FINDING", "icons/60x60/discovery.gif");
		initButton(m_assignedButton, "ASSIGNED", null);
		initButton(m_startedButton, "STARTED", null);
		initButton(m_completedButton, "COMPLETED", null);
		initButton(m_listButton, "LIST", "icons/60x60/list.gif");
		initButton(m_deleteButton, "DELETE", "icons/60x60/delete.gif");
		
		this.add(m_buttonRow, gbc);
	}

	/**
	 * Update message panel based on which buttons are pressed
	 * TODO "multi language"
	 */
	@Override
	public void actionPerformed(ActionEvent ae) 
	{
		String command = ae.getActionCommand();
		
		// Remove panels from message panel
		m_currentContentsPanel.removeAll();
	
		if(command.equals("TEXT"))
		{
			m_topLabel.setText("Melding");
			m_currentContentsPanel.add(m_messageTextPanel, BorderLayout.CENTER);
		}
		else if(command.equals("POI"))
		{
			m_topLabel.setText("Posisjon");
		}
		else if(command.equals("FINDING"))
		{
			m_topLabel.setText("Funn");
		}
		else if(command.equals("ASSIGNED"))
		{
			m_topLabel.setText("Tildelt");
		}
		else if(command.equals("STARTED"))
		{
			m_topLabel.setText("Startet");
		}
		else if(command.equals("COMPLETED"))
		{
			m_topLabel.setText("Utført");
		}
		else if(command.equals("LIST"))
		{
			m_topLabel.setText("Liste");
		}
		else if(command.equals("DELETE"))
		{
			m_topLabel.setText("Slett");
		}
		
		// Update panel
		m_currentContentsPanel.repaint();
	}

	public void newMessageSelected(IMessageIf message) 
	{
		m_messageTextPanel.updateContents(message);
	}
}
