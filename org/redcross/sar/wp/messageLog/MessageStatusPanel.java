/*
 * Author: Thomas Landvik
 * Created: 27.06.07
 */
package org.redcross.sar.wp.messageLog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.redcross.sar.app.Utils;

public class MessageStatusPanel extends JPanel
{
	private final static Dimension BUTTON_DIMENSION = new Dimension(60, 60);
	
	private JLabel m_messageLabel;
	
	private JScrollPane m_textScrollPane;
	
	private JPanel m_buttonRow;
	private JButton m_textButton;
	private JButton m_positionButton;
	private JButton m_findingButton;
	private JButton m_assignedButton;
	private JButton m_startedButton;
	private JButton m_completedButton;
	private JButton m_listButton;
	private JButton m_deleteButton;
	
	public MessageStatusPanel()
	{
		super(new BorderLayout());
		setMinimumSize(new Dimension(480, 180));
		setPreferredSize(new Dimension(480, 180));
		setBorder(BorderFactory.createLineBorder(Color.black));
		initLabel();
		initTextPane();
		initButtons();
	}
	
	private void initLabel()
	{
		m_messageLabel = new JLabel("Melding");
		this.add(m_messageLabel, BorderLayout.NORTH);
	}
	
	private void initTextPane()
	{
		m_textScrollPane = new JScrollPane();
		this.add(m_textScrollPane, BorderLayout.CENTER);
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
		m_buttonRow.add(button);
	}
	private void initButtons()
	{
		m_buttonRow = new JPanel(new FlowLayout(FlowLayout.LEADING, 4, 0));
		
		initButton(m_textButton, "Tekst", "icons/60x60/text.gif");
		initButton(m_positionButton, "Posisjon", null);
		initButton(m_findingButton, "Funn", "icons/60x60/discovery.gif");
		initButton(m_assignedButton, "Tildelt", null);
		initButton(m_startedButton, "Startet", null);
		initButton(m_completedButton, "Utf�rt", null);
		initButton(m_listButton, "Liste", "icons/60x60/list.gif");
		initButton(m_deleteButton, "Slett", "icons/60x60/delete.gif");
		
		this.add(m_buttonRow, BorderLayout.SOUTH);
	}
}
