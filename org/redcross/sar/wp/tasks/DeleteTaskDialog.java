package org.redcross.sar.wp.tasks;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.redcross.sar.gui.DiskoDialog;

public class DeleteTaskDialog extends DiskoDialog
{
	private static final long serialVersionUID = 1L;
	
	protected IDiskoWpTasks m_wpTasks;
	
	protected JPanel m_contentsPanel;
	
	public DeleteTaskDialog(IDiskoWpTasks wp)
	{
		super(wp.getApplication().getFrame());
		m_wpTasks = wp;
		
		initialize();
	}
	
	private void initialize()
	{
		m_contentsPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		JLabel headerLabel = new JLabel(m_wpTasks.getText("DeleteTask.text"));
		
		this.add(m_contentsPanel);
	}
}
