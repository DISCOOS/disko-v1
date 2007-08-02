package org.redcross.sar.wp.messageLog;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.data.IMessageIf;

/**
 * Dialog shown when setting the to field of a message in broadcast mode
 * 
 * @author thomasl
 *
 */
public class BroadcastToDialog extends DiskoDialog implements IEditMessageDialogIf
{
	protected JPanel m_contentsPanel = null;
	protected JPanel m_buttonRowPanel = null;
	
	protected JButton m_selectionButton = null;
	protected JButton m_confirmButton = null;
	protected JButton m_allButton = null;
	protected JButton m_noneButton = null;
	
	protected JPanel m_unitTypePanel = null;
	protected JButton m_teamButton = null;
	protected JButton m_dogButton = null;
	protected JButton m_vehicelButton = null;
	protected JButton m_aircraftButton = null;
	protected JButton m_boatButton = null;
	protected JButton m_commandPostButton = null;
	
	protected JPanel m_listArea = null;
	//protected HashMap<ICommunicatorIf, boolean> m_selectedCommuicators;
	//protected HashMap<ICommunicatorIf, boolean> m_confirmedCommunicators;
	
	public BroadcastToDialog(IDiskoWpMessageLog wp)
	{
		super(wp.getApplication().getFrame());
		
		initContentsPanel();
		initButtons();
		
		this.pack();
	}

	private void initButtons()
	{
		m_buttonRowPanel = new JPanel();
		m_buttonRowPanel.setLayout(new BoxLayout(m_buttonRowPanel, BoxLayout.LINE_AXIS));
		
		m_selectionButton = new JButton();
		m_buttonRowPanel.add(m_selectionButton);
		
		m_confirmButton = new JButton();
		m_buttonRowPanel.add(m_confirmButton);
		
		m_buttonRowPanel.add(new JSeparator(JSeparator.VERTICAL));
		
		m_allButton = new JButton();
		m_buttonRowPanel.add(m_allButton);
		
		m_noneButton = new JButton();
		m_buttonRowPanel.add(m_noneButton);
		
		m_buttonRowPanel.add(new JSeparator(JSeparator.VERTICAL));
		
		m_unitTypePanel = new JPanel();
		m_unitTypePanel.setLayout(new BoxLayout(m_unitTypePanel, BoxLayout.LINE_AXIS));
		
		m_teamButton = new JButton();
		m_unitTypePanel.add(m_teamButton);
		
		m_dogButton = new JButton();
		m_unitTypePanel.add(m_dogButton);
		
		m_vehicelButton = new JButton();
		m_unitTypePanel.add(m_vehicelButton);
		
		m_aircraftButton = new JButton();
		m_unitTypePanel.add(m_aircraftButton);
		
		m_boatButton = new JButton();
		m_unitTypePanel.add(m_boatButton);
		
		m_commandPostButton = new JButton();
		m_unitTypePanel.add(m_commandPostButton);
		
		m_buttonRowPanel.add(m_unitTypePanel);
		
		m_contentsPanel.add(m_buttonRowPanel);
	}

	private void initContentsPanel()
	{
		m_contentsPanel = new JPanel();
		this.add(m_contentsPanel);
	}

	public void clearContents()
	{
		// TODO Auto-generated method stub
		
	}

	public void hideDialog()
	{
		// TODO Auto-generated method stub
		
	}

	public void newMessageSelected(IMessageIf message)
	{
		// TODO Auto-generated method stub
		
	}

	public void showDialog()
	{
		// TODO Auto-generated method stub
		
	}
}
