package org.redcross.sar.modelDriver;

import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.committer.ICommitWrapperIf;
import org.redcross.sar.mso.committer.ICommittableIf;
import org.redcross.sar.mso.event.IMsoCommitListenerIf;
import org.redcross.sar.mso.event.MsoEvent;

import java.util.List;

/**
 * A commit handler is handling {@link org.redcross.sar.mso.event.MsoEvent.Commit} events in order to perform commits against a server.
 * <p/>
 * The loopback commit handler merely generates server feedbacks, without transmitting to server, and is used for test purposes.
 * At the moment, it is not implemented, commit is provided by temporary functionality in the MSO model (commitLocal()).
 */
public class LoopbackCommitHandler implements IMsoCommitListenerIf
{
    private final IMsoModelIf m_ownerModel;

    /**
     * Constructor.
     *
     * @param theModel Reference to the calling {@link org.redcross.sar.mso.IMsoModelIf} singleton object.
     */
    public LoopbackCommitHandler(IMsoModelIf theModel)
    {
        m_ownerModel = theModel;
        m_ownerModel.getEventManager().addCommitListener(this);
    }

    /**
     * Eventhandler
     *
     * @param e The {@link org.redcross.sar.mso.event.MsoEvent.Commit} event.
     */
    public void handleMsoCommitEvent(MsoEvent.Commit e)
    {
        ICommitWrapperIf wrapper = (ICommitWrapperIf) e.getSource();
        List<ICommittableIf.ICommitObjectIf> objectList = wrapper.getObjects();
    }
}
