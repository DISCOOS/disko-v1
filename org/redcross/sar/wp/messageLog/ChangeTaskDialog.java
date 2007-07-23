package org.redcross.sar.wp.messageLog;

import org.redcross.sar.gui.DiskoDialog;

public class ChangeTaskDialog extends DiskoDialog
{
	public ChangeTaskDialog(IDiskoWpMessageLog wp)
	{
		super(wp.getApplication().getFrame());
	}
}
