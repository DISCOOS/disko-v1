package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.MsoModelImpl;
import org.redcross.sar.mso.committer.ICommittableIf;
import org.redcross.sar.mso.committer.CommittableImpl;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.MsoEvent;
import org.redcross.sar.util.error.MsoError;
import org.redcross.sar.util.except.IllegalDeleteException;
import org.redcross.sar.util.except.MsoNullPointerException;
import org.redcross.sar.util.mso.Selector;

import java.util.*;

/**
 * Base class for all data objects in the MSO data model.
 * Has double bookkeeping of all attributes, both in a HashMap (for name lookup) and an ArrayList (for indexing)
 */
public abstract class AbstractMsoObject implements IMsoObjectIf
{
    /**
     * The Object ID, must exist for all MSO Objects .
     */
    private IObjectIdIf m_MsoObjectId;

    /**
     * Reference to common EventImpl Manager.
     */
    protected final IMsoEventManagerIf m_eventManager;

    /**
     * HashMap of attributes for name lookup.
     */
    private final HashMap<String, AttributeImpl> m_attributeMap = new LinkedHashMap<String, AttributeImpl>();

    /**
     * ArrayList of attributes for index lookup.
     */
    private final ArrayList<AttributeImpl> m_attributeList = new ArrayList<AttributeImpl>();

    /**
     * Set of reference lists.
     */
    private final Set<MsoListImpl> m_referenceLists = new HashSet<MsoListImpl>();

    /**
     * Set of reference objects.
     */
    private final Set<MsoReferenceImpl> m_referenceObjects = new HashSet<MsoReferenceImpl>();

    /**
     * Mask for update events for client
     */
    private int m_clientUpdateMask = 0;

    /**
     * Mask for update events for server
     */
    private int m_serverUpdateMask = 0;

    /**
     * Inticator that tells if {@link #m_attributeList} is sorted
     */
    private boolean m_listSorted = true;

    /**
     * Suspend event flag
     */
    private boolean m_suspendNotify = false;

    /**
     * Set of object holders, used when deleting object
     */
    private final Set<IMsoObjectHolderIf> m_objectHolders = new HashSet<IMsoObjectHolderIf>();

//    private boolean m_isCommitted = false;

    private boolean m_isSetup = false;


    /**
     * Constructor
     *
     * @param anObjectId The Object Id
     */
    public AbstractMsoObject(IObjectIdIf anObjectId)
    {
        if (anObjectId == null || anObjectId.getId() == null || anObjectId.getId().length() == 0)
        {
            throw new MsoError("Try to create object with no well defined object id.");
        }
        m_MsoObjectId = anObjectId;
        m_eventManager = MsoModelImpl.getInstance().getEventManager();
        suspendNotify();
        registerCreatedObject();
    }

    public void setupReferences()
    {
        if (m_isSetup)
        {
            System.out.println("Error in setup: " + this + " is setup already");
        }
        m_isSetup = true;
        defineAttributes();
        defineLists();
        defineReferences();
        resumeNotify();
    }

    protected abstract void defineAttributes();

    protected abstract void defineLists();

    protected abstract void defineReferences();

    public String getObjectId()
    {
        return m_MsoObjectId.getId();
    }

    public void addDeleteListener(IMsoObjectHolderIf aHolder)
    {
        m_objectHolders.add(aHolder);
    }

    public void removeDeleteListener(IMsoObjectHolderIf aHolder)
    {
        m_objectHolders.remove(aHolder);
    }

    public int listenerCount()
    {
        return m_objectHolders.size();
    }

    public boolean deleteObject()
    {
        if (canDelete())
        {
            for (MsoListImpl list : m_referenceLists)
            {
                list.deleteAll();
            }

            for (MsoReferenceImpl reference : m_referenceObjects)
            {
                reference.setReference(null);
            }

            doDelete();
            return true;
        }
        return false;
    }

    private boolean canDelete()
    {
        try
        {
            for (IMsoObjectHolderIf holder : this.m_objectHolders)
            {
                holder.canDeleteReference(this);
            }
        }
        catch (IllegalDeleteException e)
        {
            System.out.println(e.getMessage());     // todo Erstatt med logging
            return false;
        }
        return true;
    }

    public void doDelete()
    {
        while (m_objectHolders.size() > 0)
        {
            Iterator<IMsoObjectHolderIf> iterator = m_objectHolders.iterator();
            IMsoObjectHolderIf myHolder = iterator.next();
            myHolder.doDeleteReference(this);
        }
        registerDeletedObject();
    }

    public boolean isSetup()
    {
        return m_isSetup;
    }

    /**
     * Add a new attribute.
     * Maintains the double bookkeeping under the following conditions: Do not accept duplicated names, duplicated numbers are accepted.
     *
     * @param anAttribute Attribute to add
     */
    public void addAttribute(AttributeImpl anAttribute)
    {
        if (anAttribute != null)
        {
            String attrName = anAttribute.getName().toLowerCase();
            if (m_attributeMap.containsKey(attrName))
            {
                AttributeImpl attr = m_attributeMap.remove(attrName);
                m_attributeList.remove(attr);
            }
            m_attributeMap.put(attrName, anAttribute);
            m_attributeList.add(anAttribute);
            m_listSorted = false;
        } else
        {
            System.out.println("Error in setup: " + this + ": Try to add null Attribute");
        }
    }

    protected void addList(MsoListImpl aList)
    {
        if (aList != null)
        {
            m_referenceLists.add(aList);
        } else
        {
            System.out.println("Error in setup: " + this + ": Try to add null list");
        }
    }

    protected void addReference(MsoReferenceImpl aReference)
    {
        if (aReference != null)
        {
            m_referenceObjects.add(aReference);
        } else
        {
            System.out.println("Error in setup: " + this + ": Try to add null reference");
        }
    }

    private AttributeImpl getAttribute(String aName)
    {
        return m_attributeMap.get(aName.toLowerCase());
    }

    private AttributeImpl getAttribute(String aName, Class aClass)
    {
        return m_attributeMap.get(aName.toLowerCase());
    }

    private AttributeImpl getAttribute(int anIndex)
    {
        if (!m_listSorted)
        {
            arrangeList();
        }
        if (m_attributeList.size() > anIndex && anIndex >= 0)
        {
            return m_attributeList.get(anIndex);
        }
        return null;
    }


    public IAttributeIf.IMsoBooleanIf getBooleanAttribute(int anIndex)
    {
        try
        {
            return (IAttributeIf.IMsoBooleanIf) getAttribute(anIndex);
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    public IAttributeIf.IMsoBooleanIf getBooleanAttribute(String aName)
    {
        try
        {
            return (IAttributeIf.IMsoBooleanIf) getAttribute(aName);
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    public IAttributeIf.IMsoIntegerIf getIntegerAttribute(int anIndex)
    {
        try
        {
            return (IAttributeIf.IMsoIntegerIf) getAttribute(anIndex);
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    public IAttributeIf.IMsoIntegerIf getIntegerAttribute(String aName)
    {
        try
        {
            return (IAttributeIf.IMsoIntegerIf) getAttribute(aName);
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    public IAttributeIf.IMsoLongIf getLongAttribute(int anIndex)
    {
        try
        {
            return (IAttributeIf.IMsoLongIf) getAttribute(anIndex);
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    public IAttributeIf.IMsoLongIf getLongAttribute(String aName)
    {
        try
        {
            return (IAttributeIf.IMsoLongIf) getAttribute(aName);
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    public IAttributeIf.IMsoDoubleIf getDoubleAttribute(int anIndex)
    {
        try
        {
            return (IAttributeIf.IMsoDoubleIf) getAttribute(anIndex);
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    public IAttributeIf.IMsoDoubleIf getDoubleAttribute(String aName)
    {
        try
        {
            return (IAttributeIf.IMsoDoubleIf) getAttribute(aName);
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    public IAttributeIf.IMsoStringIf getStringAttribute(int anIndex)
    {
        try
        {
            return (IAttributeIf.IMsoStringIf) getAttribute(anIndex);
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    public IAttributeIf.IMsoStringIf getStringAttribute(String aName)
    {
        try
        {
            return (IAttributeIf.IMsoStringIf) getAttribute(aName);
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    public IAttributeIf.IMsoCalendarIf getCalendarAttribute(int anIndex)
    {
        try
        {
            return (IAttributeIf.IMsoCalendarIf) getAttribute(anIndex);
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    public IAttributeIf.IMsoCalendarIf getCalendarAttribute(String aName)
    {
        try
        {
            return (IAttributeIf.IMsoCalendarIf) getAttribute(aName);
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    public IAttributeIf.IMsoPositionIf getPositionAttribute(int anIndex)
    {
        try
        {
            return (IAttributeIf.IMsoPositionIf) getAttribute(anIndex);
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    public IAttributeIf.IMsoPositionIf getPositionAttribute(String aName)
    {
        try
        {
            return (IAttributeIf.IMsoPositionIf) getAttribute(aName);
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    public IAttributeIf.IMsoTimePosIf getTimePosAttribute(int anIndex)
    {
        try
        {
            return (IAttributeIf.IMsoTimePosIf) getAttribute(anIndex);
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    public IAttributeIf.IMsoTimePosIf getTimePosAttribute(String aName)
    {
        try
        {
            return (IAttributeIf.IMsoTimePosIf) getAttribute(aName);
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    public IAttributeIf.IMsoTrackIf getTrackAttribute(int anIndex)
    {
        try
        {
            return (IAttributeIf.IMsoTrackIf) getAttribute(anIndex);
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    public IAttributeIf.IMsoTrackIf getTrackAttribute(String aName)
    {
        try
        {
            return (IAttributeIf.IMsoTrackIf) getAttribute(aName);
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    public IAttributeIf.IMsoRouteIf getRouteAttribute(int anIndex)
    {
        try
        {
            return (IAttributeIf.IMsoRouteIf) getAttribute(anIndex);
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    public IAttributeIf.IMsoRouteIf getRouteAttribute(String aName)
    {
        try
        {
            return (IAttributeIf.IMsoRouteIf) getAttribute(aName);
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    public IAttributeIf.IMsoPolygonIf getPolygonAttribute(int anIndex)
    {
        try
        {
            return (IAttributeIf.IMsoPolygonIf) getAttribute(anIndex);
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    public IAttributeIf.IMsoPolygonIf getPolygonAttribute(String aName)
    {
        try
        {
            return (IAttributeIf.IMsoPolygonIf) getAttribute(aName);
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    public IAttributeIf.IMsoEnumIf getEnumAttribute(int anIndex)
    {
        try
        {
            return (IAttributeIf.IMsoEnumIf) getAttribute(anIndex);
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    public IAttributeIf.IMsoEnumIf getEnumAttribute(String aName)
    {
        try
        {
            return (IAttributeIf.IMsoEnumIf) getAttribute(aName);
        }
        catch (ClassCastException e)
        {
            return null;
        }
    }

    private void arrangeList()
    {
        Collections.sort(m_attributeList);
        int i = 0;
        for (AttributeImpl attr : m_attributeList)
        {
            attr.renumber(i++);
        }
        m_listSorted = true;
    }

    public void rearrangeAttribute(AttributeImpl anAttr, int anIndexNo)
    {
        if (!m_listSorted)
        {
            arrangeList();
        }
        int attrIndexNo = anAttr.getIndexNo();
        anIndexNo = Math.max(0, anIndexNo);
        anIndexNo = Math.min(m_attributeList.size(), anIndexNo);
        if (attrIndexNo == anIndexNo)
        {
            return;
        }
        if (anIndexNo < attrIndexNo)
        {
            for (int i = anIndexNo; i < attrIndexNo; i++)
            {
                m_attributeList.get(i).renumber(i + 1);
            }
            anAttr.renumber(anIndexNo);
        } else
        {
            for (int i = attrIndexNo; i < anIndexNo; i++)
            {
                m_attributeList.get(i + 1).renumber(i);
            }
            anAttr.renumber(anIndexNo);
        }
        arrangeList();
    }

    private void setAttributeValue(AttributeImpl anAttr, Object aValue) throws MsoNullPointerException
    {
        if (anAttr != null)
        {
            anAttr.setAttrValue(aValue); // todo Check class before assignment
        } else
        {
            throw new MsoNullPointerException("Trying to assign value to null attribute");
        }
    }

    public void setAttribute(String aName, Object aValue) throws MsoNullPointerException
    {

        setAttributeValue(getAttribute(aName), aValue);
    }

    public void setAttribute(int anIndex, Object aValue) throws MsoNullPointerException
    {
        setAttributeValue(getAttribute(anIndex), aValue);
    }

    private void registerUpdate(MsoEvent.EventType anEventType, boolean updateServer)
    {
        IMsoModelIf.UpdateMode anUpdateMode = MsoModelImpl.getInstance().getUpdateMode();
        int clientEventTypeMask = anEventType.maskValue();
        int serverEventTypeMask = (updateServer && anUpdateMode == IMsoModelIf.UpdateMode.LOCAL_UPDATE_MODE) ? clientEventTypeMask : 0;
        if (!m_suspendNotify)
        {
            notifyUpdate(clientEventTypeMask, serverEventTypeMask);
        } else
        {
            m_clientUpdateMask = m_clientUpdateMask | clientEventTypeMask;
            m_serverUpdateMask = m_serverUpdateMask | serverEventTypeMask;
        }
    }

    public void registerAddedReference()
    {
        registerUpdate(MsoEvent.EventType.ADDED_REFERENCE_EVENT, true);
    }

    public void registerRemovedReference()
    {
        registerUpdate(MsoEvent.EventType.REMOVED_REFERENCE_EVENT, true);
    }

    public void registerRemovedReference(boolean updateServer)
    {
        registerUpdate(MsoEvent.EventType.REMOVED_REFERENCE_EVENT, updateServer);
    }

    public void registerModifiedReference()
    {
        registerUpdate(MsoEvent.EventType.MODIFIED_REFERENCE_EVENT, true);
    }

    public void registerModifiedReference(boolean updateServer)
    {
        registerUpdate(MsoEvent.EventType.MODIFIED_REFERENCE_EVENT, updateServer);
    }

    public void registerCreatedObject()
    {
        registerUpdate(MsoEvent.EventType.CREATED_OBJECT_EVENT, true);
    }

    public void registerDeletedObject()
    {
        registerUpdate(MsoEvent.EventType.DELETED_OBJECT_EVENT, true);
    }

    public void registerModifiedData()
    {
        registerUpdate(MsoEvent.EventType.MODIFIED_DATA_EVENT, true);
    }

    /**
     * Rollback local changes.
     * Generates client update event.
     */
    public void rollback()
    {
        suspendNotify();
        boolean dataModified = false;
        for (AttributeImpl attr : m_attributeList)
        {
            dataModified |= attr.rollback();
        }
        if (dataModified)
        {
            registerModifiedData();
        }

        for (MsoListImpl list : m_referenceLists)
        {
            list.rollback();
        }

        for (MsoReferenceImpl reference : m_referenceObjects)
        {
            reference.rollback();
        }
        resumeNotify();
    }

    /**
     * Commit local changes.
     * Generates client update event.
     */
    public void commitLocal()
    {
        suspendNotify();
        boolean dataModified = false;
        for (AttributeImpl attr : m_attributeList)
        {
            dataModified |= attr.commitLocal();
        }
        if (dataModified)
        {
            registerModifiedData();
        }

        for (MsoListImpl list : m_referenceLists)
        {
            list.commitLocal();
        }

        for (MsoReferenceImpl reference : m_referenceObjects)
        {
            reference.commitLocal();
        }
        resumeNotify();
    }


    /**
     * Notify listeners (both server and clients);
     *
     * @param aClientEventTypeMask Mask for Type of event for client
     * @param aServerEventTypeMask Mask for Type of event for server
     */
    protected void notifyUpdate(int aClientEventTypeMask, int aServerEventTypeMask)
    {
        if (aClientEventTypeMask != 0)
        {
            m_eventManager.notifyDerivedUpdate(this, aClientEventTypeMask);
            m_eventManager.notifyClientUpdate(this, aClientEventTypeMask);
        }

        if (aServerEventTypeMask != 0)
        {
            m_eventManager.notifyServerUpdate(this, aServerEventTypeMask);
        }
    }

    /**
     * Suspend notification of listeners.
     * <p/>
     * Sets suspend mode, all notificatioan are suspended until #resumeNotify is called.
     */
    public void suspendNotify()
    {
        m_suspendNotify = true;
        m_clientUpdateMask = 0;
        m_serverUpdateMask = 0;
    }

    /**
     * Resume notification of listeners.
     * <p/>
     * Sends notifications to listeners and clears suspend mode
     */
    public void resumeNotify()
    {
        m_suspendNotify = false;
        notifyUpdate(m_clientUpdateMask, m_serverUpdateMask);
        m_clientUpdateMask = 0;
        m_serverUpdateMask = 0;
    }

    /**
     * Resume notification of listeners in all lists.
     * <p/>
     * Calls {@link MsoListImpl#resumeNotifications} for all defined lists.
     */
    public void resumeNotifications()
    {
        for (MsoListImpl list : m_referenceLists)
        {
            list.resumeNotifications();
        }
    }

    public Collection<ICommittableIf.ICommitReferenceIf> getCommittableAttributeRelations()
    {
        Vector<ICommittableIf.ICommitReferenceIf> retVal = new Vector<ICommittableIf.ICommitReferenceIf>();
        for (MsoReferenceImpl reference : m_referenceObjects)
        {
            retVal.addAll(reference.getCommittableRelations());
        }
        return retVal;
    }

    public Collection<ICommittableIf.ICommitReferenceIf> getCommittableListRelations()
    {
        Vector<ICommittableIf.ICommitReferenceIf> retVal = new Vector<ICommittableIf.ICommitReferenceIf>();
        for (MsoListImpl list : m_referenceLists)
        {
            retVal.addAll(list.getCommittableRelations());
        }
        return retVal;
    }


    /**
     * Class for holding Object ID Strings
     * <p/>
     * Is merely a wrapper around a String, used in order not to mismatch with other String objects.
     */
    public static class ObjectId implements IObjectIdIf
    {
        private final String m_id;

        public ObjectId(String anId)
        {
            m_id = anId;
        }

        public String getId()
        {
            return m_id;
        }
    }

    public abstract static class SelfSelector<T extends IMsoObjectIf, M extends IMsoObjectIf> implements Selector<M>
    {
        T m_object;

        public SelfSelector(T myObject)
        {
            m_object = myObject;
        }
    }
    
    /**
     * Selector used for selecting assignments with a given status.
     */
    public static class EnumSelector<T extends Enum, M extends IMsoObjectIf> implements Selector<M>
    {
        T m_selectValue;
        String m_attributeName;
        
        /**
         * Construct a Selector object
         * @param aStatus  The status to test against
         * @param anAttributName Name of Enum attribute
         */
        public EnumSelector(T aStatus, String anAttributName)
        {
            m_selectValue = aStatus;
            m_attributeName = anAttributName;
        }

        public boolean select(M anObject)
        {
            return anObject.getEnumAttribute(m_attributeName) == m_selectValue;
        }
    }
    
}