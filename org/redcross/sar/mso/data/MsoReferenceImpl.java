package org.redcross.sar.mso.data;

import org.redcross.sar.mso.CommitManager;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.MsoModelImpl;
import org.redcross.sar.mso.committer.CommittableImpl;
import org.redcross.sar.util.except.IllegalDeleteException;

import java.util.Collection;
import java.util.Vector;

/**
 *
 */
public class MsoReferenceImpl<T extends IMsoObjectIf> implements IMsoReferenceIf<T>, IMsoObjectHolderIf<T>
{
    private final AbstractMsoObject m_owner;
    private final String m_name;
    private boolean m_canDelete = true;
    protected T m_localValue = null;
    protected T m_serverValue = null;
    protected IMsoModelIf.ModificationState m_state = IMsoModelIf.ModificationState.STATE_UNDEFINED;

    public MsoReferenceImpl(AbstractMsoObject theOwner, String theName, boolean canDelete)
    {
        m_owner = theOwner;
        m_name = theName;
        m_canDelete = canDelete;
    }

    public String getName()
    {
        return m_name;
    }

    public T getReference()
    {
        return m_state == IMsoModelIf.ModificationState.STATE_LOCAL ? m_localValue : m_serverValue;
    }

    public void setCanDelete(boolean canDelete)
    {
        m_canDelete = canDelete;
    }

    public boolean canDelete()
    {
        return m_canDelete;
    }

    public IMsoModelIf.ModificationState getState()
    {
        return m_state;
    }

    private void registerDeletedReference(T anObject)
    {
        if (anObject != null)
        {
            ((AbstractMsoObject)anObject).registerRemovedReference();
        }
    }

    private void registerAddedReference(T anObject)
    {
        if (anObject != null)
        {
            ((AbstractMsoObject)anObject).registerAddedReference();
        }
    }

    public void setReference(T aReference)
    {
        IMsoModelIf.UpdateMode updateMode = MsoModelImpl.getInstance().getUpdateMode();
        T oldReference = getReference();
        IMsoModelIf.ModificationState newState;
        boolean valueChanged = false;

        if (m_state != IMsoModelIf.ModificationState.STATE_LOCAL)
        {
            if (m_serverValue != null)
            {
                ((AbstractMsoObject)m_serverValue).removeDeleteListener(this);
                registerDeletedReference(m_serverValue);
            }
        } else
        {
            if (m_localValue != null)
            {
                ((AbstractMsoObject)m_localValue).removeDeleteListener(this);
                registerDeletedReference(m_localValue);
            }
        }

        switch (updateMode)
        {
            case LOOPBACK_UPDATE_MODE:
            {
                newState = IMsoModelIf.ModificationState.STATE_SERVER_ORIGINAL;
                m_serverValue = aReference;
                if (m_serverValue != null)
                {
                    ((AbstractMsoObject)m_serverValue).addDeleteListener(this);
                    ((AbstractMsoObject)m_serverValue).registerAddedReference();
                }
                valueChanged = true;
                break;
            }
            case REMOTE_UPDATE_MODE:
            {
                newState = m_state == IMsoModelIf.ModificationState.STATE_LOCAL ? IMsoModelIf.ModificationState.STATE_CONFLICTING : IMsoModelIf.ModificationState.STATE_SERVER_MODIFIED;
                m_serverValue = aReference;
                if (m_serverValue != null)
                {
                    ((AbstractMsoObject)m_serverValue).addDeleteListener(this);
                    ((AbstractMsoObject)m_serverValue).registerAddedReference();
                }
                break;
            }
            default:
            {
                newState = IMsoModelIf.ModificationState.STATE_LOCAL;
                m_localValue = aReference;
                if (m_localValue != null)
                {
                    ((AbstractMsoObject)m_localValue).addDeleteListener(this);
                    registerAddedReference(m_localValue);
                }
            }
        }
        if (m_state != newState)
        {
            m_state = newState;
            valueChanged = true;
        }

        if (valueChanged)
        {
            registerReferenceChange(aReference, oldReference);
        }
    }

    public Vector<T> getConflictingValues()
    {
        if (m_state == IMsoModelIf.ModificationState.STATE_CONFLICTING)
        {
            Vector<T> result = new Vector<T>(2);
            result.add(m_serverValue);
            result.add(m_localValue);
            return result;
        }
        return null;
    }

    /**
     * Perform rollback
     */
    public void rollback()
    {
        T oldLocalValue = m_localValue;
        m_localValue = null;
        boolean isChanged = m_state == IMsoModelIf.ModificationState.STATE_LOCAL || m_state == IMsoModelIf.ModificationState.STATE_CONFLICTING;
        m_state = IMsoModelIf.ModificationState.STATE_SERVER_ORIGINAL;
        if (isChanged)
        {
            if (m_serverValue != null)
            {
                ((AbstractMsoObject)m_serverValue).addDeleteListener(this);
            }

            registerAddedReference(m_serverValue);
            registerDeletedReference(oldLocalValue);
            registerReferenceChange(m_serverValue, oldLocalValue);
        }
    }

    /**
     * Perform local commit
     * Change values without changing listeners etc.
     */
    public void commitLocal()
    {
        boolean isChanged = m_state == IMsoModelIf.ModificationState.STATE_LOCAL || m_state == IMsoModelIf.ModificationState.STATE_CONFLICTING;
        m_state = IMsoModelIf.ModificationState.STATE_SERVER_ORIGINAL;
        if (isChanged)
        {
            m_serverValue = m_localValue;
            m_localValue = null;
            registerReferenceChange(m_serverValue, m_serverValue);
        }
    }

    private void registerReferenceChange(T newRef, T oldRef)
    {
        if (newRef != null || oldRef != null)
        {
            if (newRef != null && oldRef != null)
            {
                m_owner.registerModifiedReference();
            } else if (newRef != null)
            {
                m_owner.registerAddedReference();
            } else
            {
                m_owner.registerRemovedReference();
            }
        }
    }

    private boolean acceptConflicting(IMsoModelIf.ModificationState aState)
    {
        if (m_state == IMsoModelIf.ModificationState.STATE_CONFLICTING)
        {
            m_state = aState;
            T accepted;
            T rejected;
            if (aState == IMsoModelIf.ModificationState.STATE_LOCAL)
            {
                accepted = m_localValue;
                rejected = m_serverValue;
            } else
            {
                accepted = m_serverValue;
                rejected = m_localValue;
            }
            registerAddedReference(accepted);
            registerDeletedReference(rejected);
            m_owner.registerModifiedReference();
            return true;
        }
        return false;
    }

    public boolean acceptLocal()
    {
        MsoModelImpl.getInstance().setLocalUpdateMode();
        boolean retval = acceptConflicting(IMsoModelIf.ModificationState.STATE_LOCAL);
        MsoModelImpl.getInstance().restoreUpdateMode();
        return retval;
    }

    public boolean acceptServer()
    {
        MsoModelImpl.getInstance().setRemoteUpdateMode();
        boolean retval = acceptConflicting(IMsoModelIf.ModificationState.STATE_SERVER_ORIGINAL);
        MsoModelImpl.getInstance().restoreUpdateMode();
        return retval;
    }


    public boolean isUncommitted()
    {
        return m_state == IMsoModelIf.ModificationState.STATE_LOCAL;
    }

    public void canDeleteReference(T anObject) throws IllegalDeleteException
    {
        if (getReference() == anObject && !canDelete())
        {
            throw new IllegalDeleteException("Cannot deleteObject object " + anObject.getObjectId() + " from reference " + getName() + " referred by " + m_owner.toString());
        }
    }

    public void doDeleteReference(T anObject)
    {
        if (getReference() == anObject)
        {
            /*
            Remove reference the "ordinary" way. Will create event for modified reference and state in this object.
             */
            setReference(null);
        }
    }

    public Collection<CommittableImpl.CommitReference> getCommittableRelations()
    {
        Vector<CommittableImpl.CommitReference> result = new Vector<CommittableImpl.CommitReference>();
        if (m_state == IMsoModelIf.ModificationState.STATE_LOCAL)
        {
            if (m_serverValue != null)
            {
                result.add(new CommittableImpl.CommitReference(m_name, m_owner, m_serverValue, CommitManager.CommitType.COMMIT_DELETED));
            }
            if (m_localValue != null)
            {
                result.add(new CommittableImpl.CommitReference(m_name, m_owner, m_localValue, CommitManager.CommitType.COMMIT_CREATED));
            }

        }
        return result;
    }

}
