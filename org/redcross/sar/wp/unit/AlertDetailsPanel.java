package org.redcross.sar.wp.unit;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * JPanel displaying alert details
 * 
 * @author thomasl
 */
public class AlertDetailsPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	public AlertDetailsPanel()
	{
		this.add(new JLabel("Alert Details"));
	}
}
