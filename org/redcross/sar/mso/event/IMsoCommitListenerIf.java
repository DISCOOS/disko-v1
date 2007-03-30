package org.redcross.sar.mso.event;

/**
 *
 */
public interface IMsoCommitListenerIf extends java.util.EventListener
{
    /**
     * Handle a commit event.
     * <p/>
     * Listener method(s) that handle MSO Commit events.
     * @param e The {@link MsoEvent event} that shall be handled.
     */

    public void handleMsoCommitEvent(MsoEvent.Commit e);
}
