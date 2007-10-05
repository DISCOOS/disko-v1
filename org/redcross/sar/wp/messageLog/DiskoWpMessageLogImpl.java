package org.redcross.sar.wp.messageLog;

import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.gui.NavBar;
import org.redcross.sar.wp.AbstractDiskoWpModule;

import java.util.EnumSet;

import javax.swing.JOptionPane;

/**
 *
 */
public class DiskoWpMessageLogImpl extends AbstractDiskoWpModule implements IDiskoWpMessageLog
{
    MessageLogPanel m_logPanel;

    public DiskoWpMessageLogImpl(IDiskoRole role)
    {
        super(role);
        initialize();
    }

    private void initialize()
    {
        loadProperties("properties");
        assignWpBundle("org.redcross.sar.wp.messageLog.messageLog");

        m_logPanel = new MessageLogPanel(this);
        layoutComponent(m_logPanel.getPanel());
    }

    @Override
	public void activated()
    {
        super.activated();
        NavBar navBar = getApplication().getNavBar();
        EnumSet<NavBar.ToolCommandType> myInterests =
                EnumSet.of(NavBar.ToolCommandType.ZOOM_IN_TOOL);
        myInterests.add(NavBar.ToolCommandType.ZOOM_OUT_TOOL);
        myInterests.add(NavBar.ToolCommandType.PAN_TOOL);
        myInterests.add(NavBar.ToolCommandType.ZOOM_FULL_EXTENT_COMMAND);
        myInterests.add(NavBar.ToolCommandType.ZOOM_TO_LAST_EXTENT_FORWARD_COMMAND);
        myInterests.add(NavBar.ToolCommandType.ZOOM_TO_LAST_EXTENT_BACKWARD_COMMAND);
        myInterests.add(NavBar.ToolCommandType.MAP_TOGGLE_COMMAND);
        navBar.showButtons(myInterests);
    }

    @Override 
	public void deactivated()
    {
    	// Warn user that it work processes can't be changed if message is not committed
    	if(MessageLogBottomPanel.isMessageDirty())
    	{
    		Object[] dialogOptions = {getText("yes.text"), getText("no.text")};
    		int n = JOptionPane.showOptionDialog(this.getApplication().getFrame(), 
    				getText("DirtyMessageWarning.text"), 
    				getText("DirtyMessageWarning.header"), 
    				JOptionPane.YES_NO_OPTION, 
    				JOptionPane.QUESTION_MESSAGE, 
    				null, 
    				dialogOptions, 
    				dialogOptions[0]);
    		boolean changeWP = (n == JOptionPane.YES_OPTION);
    		if(changeWP)
    		{
    			// Delete current message, change work process
    			MessageLogBottomPanel.clearCurrentMessage();
    		}
    		else
    		{
    			// Don't change WP
//    			getDiskoRole().selectDiskoWpModule(this);
    			// TODO Change selectDiskoWpModule/deactivated to allow cancellation of wp change
    			return;
    		}
    	}
    	
    	super.deactivated();
    	m_logPanel.hidePanels();
    	m_logPanel.clearSelection();
    }

    /* (non-Javadoc)
    * @see com.geodata.engine.disko.task.DiskoAp#getName()
    */
    @Override
	public String getName()
    {
        return "Sambandslogg";
    }

    /* (non-Javadoc)
     * @see com.geodata.engine.disko.task.DiskoAp#cancel()
     */
    public void cancel()
    {
    }

    /* (non-Javadoc)
     * @see com.geodata.engine.disko.task.DiskoAp#finish()
     */
    public void finish()
    {
    }

}
