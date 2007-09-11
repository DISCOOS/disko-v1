package org.redcross.sar.mso.data;

import org.redcross.sar.mso.CommitManager;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.MsoModelImpl;
import org.redcross.sar.mso.committer.CommittableImpl;
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
        m_items = new LinkedHashMap<String, M>(aSize);
        m_added = new LinkedHashMap<String, M>();
        m_deleted = new LinkedHashMap<String, M>();
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
        verifyMainOperation("Cannot create object in a non-main list");
    }

    public void verifyMainOperation(String aMessage)
    {
        if (!m_isMain)
        {
            //throw new MsoRuntimeException(aMessage);
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
        HashSet<M> retVal = new HashSet<M>(size());
        retVal.addAll(m_items.values());
        retVal.addAll(m_added.values());
        return retVal;
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

    public M getItem(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        return getItem(anObjectId.getId());
    }

    public M getItem(String anObjectId)
    {
        M retVal = m_items.get(anObjectId);
        if (retVal == null)
        {
            retVal = m_added.get(anObjectId);
        }
        return retVal;
    }

    public void add(M anObject)
    {
        if (anObject == null)
        {
            //throw new MsoRuntimeException(getName() + ": Cannot add null object");
        }
        if (m_items.containsKey(anObject.getObjectId()) || m_added.containsKey(anObject.getObjectId()) || m_deleted.containsKey(anObject.getObjectId()))
        {
            throw new DuplicateIdException("ObjectId already added to list");
        }
        if (!((AbstractMsoObject) anObject).isSetup())
        {
            //throw new MsoRuntimeException(getName() + ": Cannot add uninitialized object");
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

    private void clearList(HashMap<String, M> aList, boolean updateServer)      // todo Denne er feil, slettete elementer blir ikke dereferert.
    {
        for (M refObj : aList.values())
        {
            AbstractMsoObject abstrObj = (AbstractMsoObject) refObj;
            if (abstrObj != null)
            {
                abstrObj.removeDeleteListener(this);
                if (m_isMain)
                {
                    abstrObj.doDelete();
                } else
                {
                    abstrObj.registerRemovedReference(updateServer);
                }
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

    public boolean doDeleteReference(M anObject)
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
            return true;
        }
        return false;
    }

    public boolean removeReference(M anObject)
    {
        if (isMain())
        {
            return anObject.deleteObject();
        } else
        {
            return doDeleteReference(anObject);
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
            return IMsoModelIf.ModificationState.STATE_SERVER;
        }
        if (m_added.containsKey(aReference.getObjectId()))
        {
            return IMsoModelIf.ModificationState.STATE_LOCAL;
        }
        return IMsoModelIf.ModificationState.STATE_UNDEFINED;
    }

    public boolean rollback()
    {
        boolean retVal = m_added.size() > 0 || m_deleted.size() > 0;
        clearList(m_added, false);
        undeleteAll();
        if (m_isMain)
        {
            for (M object : m_items.values())
            {
                ((AbstractMsoObject) object).rollback();
            }
        }
        return retVal;
    }

    /**
     * Move from m_added to m_items without changing any listeners etc
     */
    private void commitAddedLocal()
    {
        m_items.putAll(m_added);
        m_added.clear();
    }

    public boolean postProcessCommit()
    {
        boolean retVal = m_added.size() > 0;
        clearDeleted(m_deleted);
        commitAddedLocal();
        if (m_isMain)
        {
            for (M object : m_items.values())
            {
                ((AbstractMsoObject) object).postProcessCommit();
            }
        }
        return retVal;
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
        ArrayList<M> retVal = new ArrayList<M>();
        for (M item : getItems())
        {
            if (aSelector.select(item))
            {
                retVal.add(item);
            }
        }
        if (aComparator != null)
        {
            Collections.sort(retVal, aComparator);
        }
        return retVal;
    }

    public M selectSingleItem(Selector<M> aSelector)
    {
        for (M item : getItems())
        {
            if (aSelector.select(item))
            {
                return item;
            }
        }
        return null;
    }


    protected M createdItem(M anObject)
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
            //throw new MsoRuntimeException("Duplicate object id, should be unique: " + anObject.getObjectId());
        }
        return null;
    }

    protected IMsoObjectIf.IObjectIdIf makeUniqueId()
    {
        IMsoObjectIf.IObjectIdIf retVal;
        do
        {
            retVal = MsoModelImpl.getInstance().getModelDriver().makeObjectId();
        }
        while (m_items.get(retVal.getId()) != null || m_added.get(retVal.getId()) != null || m_deleted.get(retVal.getId()) != null);
        return retVal;
    }

    public Collection<CommittableImpl.CommitReference> getCommittableRelations()
    {
        Vector<CommittableImpl.CommitReference> retVal = new Vector<CommittableImpl.CommitReference>();
        for (M item : m_added.values())
        {
            retVal.add(new CommittableImpl.CommitReference(m_name, m_owner, item, CommitManager.CommitType.COMMIT_CREATED));
        }
        for (M item : m_deleted.values())
        {
            retVal.add(new CommittableImpl.CommitReference(m_name, m_owner, item, CommitManager.CommitType.COMMIT_DELETED));
        }
        return retVal;
    }

    protected int makeSerialNumber()
    {
        int retVal = 0;
        for (M item : getItems())
        {
            try
            {
                ISerialNumberedIf serialItem = (ISerialNumberedIf) item;
                if (serialItem.getNumber() > retVal)
                {
                    retVal = serialItem.getNumber();
                }
            }
            catch (ClassCastException e)
            {
                //throw new MsoRuntimeException("Object " + item + " is not implementing ISerialNumberedIf");
            }
        }
        return retVal + 1;
    }

    public boolean contains(M anObject)
    {
        return (m_items.containsKey(anObject.getObjectId()) || m_added.containsKey(anObject.getObjectId()));
    }

    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        MsoListImpl<M> list;
        try
        {
            list = (MsoListImpl<M>) o;
        }
        catch (Exception e)
        {
            return false;
        }

        if (m_owner != null ? !m_owner.equals(list.m_owner) : list.m_owner != null)
        {
            return false;
        }
        if (m_name != null ? !m_name.equals(list.m_name) : list.m_name != null)
        {
            return false;
        }
        if (m_isMain != list.m_isMain)
        {
            return false;
        }
        if (m_items != null ? !m_items.equals(list.m_items) : list.m_items != null)
        {
            return false;
        }
        if (m_added!= null ? !m_added.equals(list.m_added) : list.m_added != null)
        {
            return false;
        }
        if (m_deleted != null ? !m_deleted.equals(list.m_deleted) : list.m_deleted != null)
        {
            return false;
        }

        return true;
    }

}