package org.redcross.sar.mso.event;

import org.redcross.sar.mso.committer.ICommitWrapperIf;

/**
 *
 */
public class MsoCommitAdapter implements IMsoCommitListenerIf
{
    /**
     * Handle an update event sent to server
     */
    public void handleMsoCommitEvent(MsoEvent.Commit e)
    {
        int mask = e.getEventTypeMask();
        ICommitWrapperIf sourceWrapper = (ICommitWrapperIf)e.getSource();

        if ((mask & MsoEvent.EventType.COMMIT_EVENT.maskValue()) != 0)
        {
            // Possible handling of COMMIT_EVENT

        }
    }

}
