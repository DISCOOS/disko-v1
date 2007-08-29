package org.redcross.sar.wp.messageLog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.IMessageLineIf.MessageLineType;

/**
 * @author thomasl
 *
 * Displays the message text in the top panel in the message log
 */
public class TextPanel extends JPanel implements IEditMessageComponentIf
{
	private static final long serialVersionUID = 1L;
	
	private JScrollPane m_textScroll;
	private JTextArea m_textArea;
	private JButton m_cancelButton;
	private JButton m_okButton;
	
	public TextPanel(IDiskoWpMessageLog wp) 
	{
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		initTextArea(gbc);
		initButtons(gbc);
	}

	private void initTextArea(GridBagConstraints gbc)
	{
		gbc.gridheight = 2;
		gbc.weightx = 1.0;
		m_textArea = new JTextArea();
		m_textArea.setLineWrap(true);
		m_textArea.setWrapStyleWord(true);
		m_textScroll = new JScrollPane(m_textArea);
		this.add(m_textScroll, gbc);
	}
	
	private void initButtons(GridBagConstraints gbc)
	{
		gbc.gridheight = 1;
		gbc.gridx++;
		gbc.weightx = 0.0;
		
		m_cancelButton = DiskoButtonFactory.createSmallCancelButton();
		
		m_cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
//				fireDialogCanceled();
			}
		});
		this.add(m_cancelButton, gbc);
		
		gbc.gridy = 1;
		m_okButton = DiskoButtonFactory.createSmallOKButton();
		m_okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// Store text in current message
				IMessageIf message = MessageLogTopPanel.getCurrentMessage();
				IMessageLineIf textLine = message.findMessageLine(MessageLineType.TEXT, true);
				textLine.setLineText(m_textArea.getText());
			}	
		});
		this.add(m_okButton, gbc);
	}

	public void hideComponent()
	{
		this.setVisible(false);
	}

	public void newMessageSelected(IMessageIf message)
	{
		IMessageLineIf textMessageLine = message.findMessageLine(MessageLineType.TEXT, false);
		if(textMessageLine != null)
		{
			m_textArea.setText(textMessageLine.getLineText());
		}
		else
		{
			m_textArea.setText("");
		}
	}

	public void showComponent()
	{
		this.setVisible(true);
	}

	public void clearContents()
	{
		m_textArea.setText("");
	}
}
