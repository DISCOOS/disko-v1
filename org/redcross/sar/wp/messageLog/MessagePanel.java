/**
 * @author Thomas Landvik
 * This class define the edit panel GUI for the message log
 */

package org.redcross.sar.wp.messageLog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

public class MessagePanel extends JPanel implements ActionListener
{
	private final static Dimension BUTTON_DIMENSION = new Dimension(60, 60);
	
	private final static int PANEL_WIDTH = 480;
	private final static int PANEL_HEIGHT = 180;
	
	private JLabel m_topLabel;
	
	private JPanel m_currentContentsPanel;
	
	// Text panel
	private JTable m_messageTextTable;
	private MessageTextTableModel m_messageTableModel;
	private JScrollPane m_textScrollPane;
	
	// Position panel
	// Finding panel
	// Assigned panel
	// Started panel
	// Completed panel
	// List panel
	// Delete panel
	
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
		super(new BorderLayout());
		setMinimumSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		//setBorder(BorderFactory.createLineBorder(Color.black));
		initLabel();
		initCurrentContents();
		initTextPane();
		initButtons();
	}
	
	private void initCurrentContents()
	{
		m_currentContentsPanel = new JPanel(new BorderLayout());
		//m_currentContentsPanel.setBorder(BorderFactory.createLineBorder(Color.red));
		this.add(m_currentContentsPanel, BorderLayout.CENTER);
	}
	
	private void initLabel()
	{
		m_topLabel = new JLabel("Melding");
		this.add(m_topLabel, BorderLayout.NORTH);
		this.add(new JSeparator(SwingConstants.HORIZONTAL));
	}
	
	private void initTextPane()
	{
		m_messageTableModel = new MessageTextTableModel();
		m_messageTextTable = new JTable(m_messageTableModel);
		m_messageTextTable.setBorder(BorderFactory.createLineBorder(Color.black));
		m_textScrollPane = new JScrollPane(m_messageTextTable);
		m_messageTextTable.setFillsViewportHeight(true);
		m_messageTextTable.setColumnSelectionAllowed(false);
		m_messageTextTable.setRowSelectionAllowed(false);
		m_currentContentsPanel.add(m_textScrollPane, BorderLayout.CENTER);
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
	private void initButtons()
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
		
		this.add(m_buttonRow, BorderLayout.SOUTH);
	}

	public void setText(String[] messageString) 
	{
		m_messageTableModel.setMessageLines(messageString);
	}

	/**
	 * Update message panel based on which buttons are pressed
	 * TODO: "internasjonaliser"
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
			m_currentContentsPanel.add(m_textScrollPane);
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
}
