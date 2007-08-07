package org.redcross.sar.wp.messageLog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import org.redcross.sar.app.Utils;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.IMessageLineListIf;
import org.redcross.sar.mso.data.IMessageLineIf.MessageLineType;
import org.redcross.sar.util.mso.Selector;

/**
 * @author thomasl
 *
 * Displays the message text in the top panel in the message log
 */
public class TextDialog extends DiskoDialog implements IEditMessageDialogIf
{
	private JScrollPane m_textScroll;
	private JTextArea m_textArea;
	private JButton m_cancelButton;
	private JButton m_okButton;
	
	public TextDialog(IDiskoWpMessageLog wp) 
	{
		super(wp.getApplication().getFrame());
		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(600, 120));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		initTextArea(gbc);
		initButtons(gbc);
		this.pack();
	}

	private void initTextArea(GridBagConstraints gbc)
	{
		gbc.gridheight = 2;
		gbc.weightx = 1.0;
		m_textArea = new JTextArea();
		m_textScroll = new JScrollPane(m_textArea);
		this.add(m_textScroll, gbc);
	}
	
	private void initButtons(GridBagConstraints gbc)
	{
		gbc.gridheight = 1;
		gbc.gridx++;
		gbc.weightx = 0.0;
		
		m_cancelButton = new JButton();
		m_cancelButton.setMinimumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		m_cancelButton.setPreferredSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		m_cancelButton.setMaximumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		try
		{
			m_cancelButton.setIcon(Utils.createImageIcon("icons/60x60/abort.gif", "ABORT"));
		}
		catch(Exception e)
		{
			m_cancelButton.setText("Cancel");
			System.err.println("Error loading cancel button icon in message text panel");
		}
		m_cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				fireDialogCanceled();
			}
		});
		this.add(m_cancelButton, gbc);
		
		gbc.gridy = 1;
		m_okButton = new JButton();
		m_okButton.setMinimumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		m_okButton.setPreferredSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		m_okButton.setMaximumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		/*try
		{
			m_okButton.setIcon(Utils.createImageIcon("icons/60x60/ok.gif", "OK"));
		}
		catch(Exception e)
		{
			m_okButton.setText("OK");
		}*/
		m_okButton.setText("OK");
		m_okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				fireDialogFinished();
			}	
		});
		this.add(m_okButton, gbc);
	}
	
	private final Comparator<IMessageLineIf> m_lineComparator = new Comparator<IMessageLineIf>()
	{
		public int compare(IMessageLineIf o1, IMessageLineIf o2)
		{
			// TODO Auto-generated method stub
			return o1.getLineNumber() - o2.getLineNumber();
		}	
	};
	
	private final Selector<IMessageLineIf> m_textLineSelector = new Selector<IMessageLineIf>()
	{
		public boolean select(IMessageLineIf messageLine)
		{
			if(messageLine.getLineType() == IMessageLineIf.MessageLineType.TEXT)
			{
				return true;
			}
			else
			{
				return false;
			}
		}	
	};

	public void hideDialog()
	{
		this.setVisible(false);
	}

	public void newMessageSelected(IMessageIf message)
	{
		IMessageLineIf textMessageLine = message.findMessageLine(MessageLineType.TEXT, true);
		m_textArea.setText(textMessageLine.getLineText());
	}

	public void showDialog()
	{
		this.setVisible(true);
	}

	public void clearContents()
	{
		m_textArea.setText("");
	}

	public String getText()
	{
		return m_textArea.getText();
	}
}
