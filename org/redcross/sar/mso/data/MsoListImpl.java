package org.redcross.sar.mso.data;

import org.redcross.sar.mso.CommitManager;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.MsoModelImpl;
import org.redcross.sar.mso.committer.CommittableImpl;
import org.redcross.sar.util.error.MsoError;
import org.redcross.sar.util.except.DuplicateIdException;
import org.redcross.sar.util.mso.Selector;

import java.util.*;

/**
 *
 */
public class MsoListImpl<M extends IMsoObjectIf> implements IMsoListIf<M>, IMsoObjectHolderIf<M>
{
    protected final IMsoObjectIf m_owner;
    protected String m_name;
    protected final HashMap<String, M> m_items;
    protected final HashMap<String, M> m_added;
    protected final HashMap<String, M> m_deleted;
    protected final boolean m_isMain;

    public MsoListImpl(IMsoObjectIf anOwner)
    {
        this(anOwner, "");
    }

    public MsoListImpl(IMsoObjectIf anOwner, String theName)
    {
        this(anOwner, theName, false);
    }

    public MsoListImpl(IMsoObjectIf anOwner, String theName, boolean isMain)
    {
        this(anOwner, theName, isMain, 50);
    }

    public MsoListImpl(IMsoObjectIf anOwner, String theName, boolean isMain, int aSize)
    {
        m_owner = anOwner;
        m_name = theName;
        m_isMain = isMain;
        m_items = new HashMap<String, M>(aSize);
        m_added = new HashMap<String, M>();
        m_deleted = new HashMap<String, M>();
    }

    protected void setName(String aName)
    {
        m_name = aName.toLowerCase();
    }

    public String getName()
    {
        return m_name;
    }

    public void checkCreateOp()
    {
        verifyMainOperation("Cannot create object in non-main lists");
    }

    public void verifyMainOperation(String aMessage)
    {
        if (!m_isMain)
        {
            throw new MsoError(aMessage);
        }
    }

    public IMsoObjectIf getOwner()
    {
        return m_owner;
    }

    public boolean isMain()
    {
        return m_isMain;
    }

    public Collection<M> getItems()
    {
        HashSet<M> result = new HashSet<M>(size());
        result.addAll(m_items.values());
        result.addAll(m_added.values());
        return result;
    }

    public M getItem()
    {
        Iterator<M> iterator = getItems().iterator();
        if (iterator.hasNext())
        {
            return iterator.next();
        }
        return null;
    }

    public M getItem(String anObjectId)
    {
        M result = m_items.get(anObjectId);
        if (result == null)
        {
            result = m_added.get(anObjectId);
        }
        return result;
    }

    public void add(M anObject) throws DuplicateIdException
    {
        if (anObject == null)
        {
            throw new MsoError(getName() + ": Cannot add null object");
        }
        if (m_items.containsKey(anObject.getObjectId()) || m_added.containsKey(anObject.getObjectId()) || m_deleted.containsKey(anObject.getObjectId()))
        {
            throw new DuplicateIdException("ObjectId already added to list");
        }
        if (!((AbstractMsoObject) anObject).isSetup())
        {
            throw new MsoError(getName() + ": Cannot add uninitialized object");
        }


        if (MsoModelImpl.getInstance().getUpdateMode() == MsoModelImpl.UpdateMode.LOCAL_UPDATE_MODE)
        {
            m_added.put(anObject.getObjectId(), anObject);
        } else
        {
            m_items.put(anObject.getObjectId(), anObject);
        }
        ((AbstractMsoObject) anObject).registerAddedReference();
        ((AbstractMsoObject) anObject).addDeleteListener(this);
        if (m_owner != null)
        {
            ((AbstractMsoObject) m_owner).registerAddedReference();
        }
    }

    public void clear()
    {
        clearList(m_items, true);
        clearList(m_added, false);
        clearDeleted(m_deleted);
    }

    private void clearList(HashMap<String, M> aList, boolean updateServer)
    {
        for (M refObj : aList.values())
        {
            AbstractMsoObject abstrObj = (AbstractMsoObject) refObj;
            if (abstrObj != null)
            {
                if (m_isMain)
                {
                    abstrObj.registerDeletedObject();
                } else
                {
                    abstrObj.registerRemovedReference(updateServer);
                }
                abstrObj.removeDeleteListener(this);
            }
        }
        aList.clear();
    }

    private void clearDeleted(HashMap<String, M> aList)
    {
        aList.clear();
    }

    public int size()
    {
        return m_items.size() + m_added.size();
    }

    public void print()
    {
        System.out.println("List:    " + this.m_name);
        for (M o : m_items.values())
        {
            System.out.println("Item:    " + o.toString());
        }
        for (M o : m_added.values())
        {
            System.out.println("Added:   " + o.toString());
        }
        for (M o : m_deleted.values())
        {
            System.out.println("Deleted: " + o.toString());
        }
    }

    /**
     * Can always deleteObject.
     *
     * @param anObject See implemented method {@link IMsoObjectHolderIf#canDeleteReference(IMsoObjectIf)}.
     */
    public void canDeleteReference(M anObject)
    {
    }

    public void doDeleteReference(M anObject)
    {
        boolean localUpdateMode = MsoModelImpl.getInstance().getUpdateMode() == MsoModelImpl.UpdateMode.LOCAL_UPDATE_MODE;
        boolean updateServer = localUpdateMode;

        String key = anObject.getObjectId();

        M refObj;
        refObj = m_items.remove(key);
        if (refObj != null)
        {
            if (localUpdateMode)
            {
                m_deleted.put(key, anObject);
            }
        } else
        {
            refObj = m_added.remove(key);
            updateServer = false;
        }
        if (refObj != null)
        {
            ((AbstractMsoObject) refObj).removeDeleteListener(this);
            if (m_owner != null)
            {
                ((AbstractMsoObject) m_owner).registerRemovedReference(updateServer);
            }
        }
    }

    public boolean removeReference(M anObject)
    {
        if (isMain())
        {
            return anObject.deleteObject();
        } else
        {
            doDeleteReference(anObject);
            return true;
        }
    }

    /**
     * Re-insert an object in items.
     *
     * @param refObj The object to undelete.
     */
    protected void reInsert(M refObj)
    {
        if (refObj != null)
        {
            m_items.put(refObj.getObjectId(), refObj);
            ((AbstractMsoObject) refObj).addDeleteListener(this);
            ((AbstractMsoObject) refObj).registerCreatedObject();
            if (m_owner != null)
            {
                ((AbstractMsoObject) m_owner).registerAddedReference();
            }
        }
    }

    /**
     * Undelete all deleted objects
     */
    private void undeleteAll()
    {
        for (M refObj : m_deleted.values())
        {
            reInsert(refObj);
        }
        m_deleted.clear();
    }


    /**
     * Delete all objects
     * <p/>
     * todo Can be optimized, but has probably very little effect, as the list normally will be quite short.
     */
    public void deleteAll()
    {
        M refObj = getItem();
        while (refObj != null)
        {
            doDeleteReference(refObj);
            refObj = getItem();
        }
    }

    public IMsoModelIf.ModificationState getState(M aReference)
    {
        if (m_items.containsKey(aReference.getObjectId()))
        {
            return IMsoModelIf.ModificationState.STATE_SERVER_ORIGINAL;
        }
        if (m_added.containsKey(aReference.getObjectId()))
        {
            return IMsoModelIf.ModificationState.STATE_LOCAL;
        }
        return IMsoModelIf.ModificationState.STATE_UNDEFINED;
    }

    public boolean rollback()
    {
        boolean result = m_added.size() > 0 || m_deleted.size() > 0;
        clearList(m_added, false);
        undeleteAll();
        if (m_isMain)
        {
            for (M object : m_items.values())
            {
                ((AbstractMsoObject) object).rollback();
            }
        }
        return result;
    }

    /**
     * Move from m_added to m_items without changing any listeners etc
     */
    private void commitAddedLocal()
    {
        m_items.putAll(m_added);
        m_added.clear();
    }

    public boolean commitLocal()
    {
        boolean result = m_added.size() > 0;
        clearDeleted(m_deleted);
        commitAddedLocal();
        if (m_isMain)
        {
            for (M object : m_items.values())
            {
                ((AbstractMsoObject) object).commitLocal();
            }
        }
        return result;
    }


    public void resumeNotifications()
    {
        if (m_isMain)
        {
            for (M object : m_items.values())
            {
                ((AbstractMsoObject) object).resumeNotify();
            }
        }
    }

    public List<M> selectItems(Selector<M> aSelector, Comparator<M> aComparator)
    {
        ArrayList<M> result = new ArrayList<M>();
        for (M item : getItems())
        {
            if (aSelector.select(item))
            {
                result.add(item);
            }
        }
        if (aComparator != null)
        {
            Collections.sort(result, aComparator);
        }
        return result;
    }

    protected M createdItem(M anObject) throws DuplicateIdException
    {
        ((AbstractMsoObject) anObject).setupReferences();
        add(anObject);
        return anObject;
    }

    protected M createdUniqueItem(M anObject)
    {
        try
        {
            return createdItem(anObject);
        }
        catch (DuplicateIdException e)
        {
            throw new MsoError("Duplicate object id, should be unique: " + anObject.getObjectId());
        }
    }

    protected IMsoObjectIf.IObjectIdIf makeUniqueId()
    {
        IMsoObjectIf.IObjectIdIf retval;
        do
        {
            retval = MsoModelImpl.getInstance().getModelDriver().makeObjectId();
        }
        while (m_items.get(retval.getId()) != null || m_added.get(retval.getId()) != null || m_deleted.get(retval.getId()) != null);
        return retval;
    }

    public Collection<CommittableImpl.CommitReference> getCommittableRelations()
    {
        Vector<CommittableImpl.CommitReference> result = new Vector<CommittableImpl.CommitReference>();
        for (M item : m_added.values())
        {
            result.add(new CommittableImpl.CommitReference(m_name, m_owner, item, CommitManager.CommitType.COMMIT_CREATED));
        }
        for (M item : m_deleted.values())
        {
            result.add(new CommittableImpl.CommitReference(m_name, m_owner, item, CommitManager.CommitType.COMMIT_DELETED));
        }
        return result;
    }

    protected int makeSerialNumber()
    {
        int retval = 0;
        for (M item : getItems())
        {
            try
            {
                ISerialNumberedIf serialItem = (ISerialNumberedIf) item;
                if (serialItem.getNumber() > retval)
                {
                    retval = serialItem.getNumber();
                }
            }
            catch (ClassCastException e)
            {
                throw new MsoError("Object " + item + " is not implementing ISerialNumberedIf");
            }
        }
        return retval + 1;
    }
}