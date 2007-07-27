package org.redcross.sar.wp.messageLog;

import java.awt.Frame;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.Update;

/**
 * 
 * @author thomasl
 *
 * Dialog containing all units
 */

public class UnitListSelectionDialog extends DiskoDialog
{
	protected JPanel m_contentsPanel = null;
	protected JScrollPane  m_scrollPane = null;
	protected List<JButton> m_buttons = null;
	
	public UnitListSelectionDialog(IDiskoWpMessageLog wp)
	{
		super(wp.getApplication().getFrame());
		
		initContentsPanel();
	}

	private void initButtons()
	{
		// TODO Auto-generated method stub
		// Create a button for each unit in CP
	}

	private void initContentsPanel()
	{
		// TODO Auto-generated method stub
		m_contentsPanel = new JPanel();
		m_scrollPane = new JScrollPane();
		m_contentsPanel.add(m_scrollPane);
		this.add(m_contentsPanel);
		this.pack();
	}
}
