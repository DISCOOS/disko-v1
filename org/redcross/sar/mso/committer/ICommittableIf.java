package org.redcross.sar.mso.committer;

import org.redcross.sar.mso.CommitManager;
import org.redcross.sar.mso.data.IMsoObjectIf;

/**
 *
 */
public interface ICommittableIf
{
    /**
     * Get type of commit
     */
    public CommitManager.CommitType getType();

    public interface ICommitObjectIf extends ICommittableIf
    {
        public IMsoObjectIf getObject();
    }

    public interface ICommitReferenceIf extends ICommittableIf
    {
        public String getReferenceName();

        public IMsoObjectIf getReferringObject();

        public IMsoObjectIf getReferredObject();
    }

}
