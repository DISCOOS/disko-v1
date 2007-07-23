package org.redcross.sar.wp.messageLog;

import org.redcross.sar.gui.DiskoDialog;

public class ChangeToDialog extends DiskoDialog
{
	public ChangeToDialog(IDiskoWpMessageLog wp)
	{
		super(wp.getApplication().getFrame());
	}
}
