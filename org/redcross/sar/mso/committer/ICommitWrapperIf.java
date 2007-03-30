package org.redcross.sar.mso.committer;

import java.util.List;

/**
 *
 */
public interface ICommitWrapperIf
{
    /**
     * Get a list of committable objects.
     * @return The list
     */
    public List<ICommittableIf.ICommitObjectIf> getObjects();

    /**
     * Get a list of committable relations.
     * @return The list
     */
    public List<ICommittableIf.ICommitReferenceIf> getReferences();

}
