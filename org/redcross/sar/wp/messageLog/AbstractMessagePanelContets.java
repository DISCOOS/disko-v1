package org.redcross.sar.wp.messageLog;

import javax.swing.JPanel;

import org.redcross.sar.mso.data.IMessageIf;

public abstract class AbstractMessagePanelContets extends JPanel
{
	public abstract void updateContents(IMessageIf message);
}
