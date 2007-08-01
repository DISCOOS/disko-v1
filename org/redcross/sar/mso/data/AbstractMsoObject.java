package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.MsoModelImpl;
import org.redcross.sar.mso.committer.ICommittableIf;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.MsoEvent;
import org.redcross.sar.util.except.IllegalDeleteException;
import org.redcross.sar.util.except.MsoNullPointerException;
import org.redcross.sar.util.except.UnknownAttributeException;
import org.redcross.sar.util.mso.*;

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
     * Map of attributes for name lookup.
     */
    private final Map<String, AttributeImpl> m_attributeMap = new LinkedHashMap<String, AttributeImpl>();

    /**
     * ArrayList of attributes for index lookup.
     */
    private final ArrayList<AttributeImpl> m_attributeList = new ArrayList<AttributeImpl>();

    /**
     * Set of reference lists.
     */
    private final Map<String, MsoListImpl> m_referenceLists = new LinkedHashMap<String, MsoListImpl>();

    /**
     * Set of reference objects.
     */
    private final Map<String, MsoReferenceImpl> m_referenceObjects = new LinkedHashMap<String, MsoReferenceImpl>();

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
            //throw new MsoException("Try to create object with no well defined object id.");
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
            for (MsoListImpl list : m_referenceLists.values())
            {
                list.deleteAll();
            }

            for (MsoReferenceImpl reference : m_referenceObjects.values())
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
            m_referenceLists.put(aList.getName().toLowerCase(), aList);
        } else
        {
            System.out.println("Error in setup: " + this + ": Try to add null list");
        }
    }

    protected void addReference(MsoReferenceImpl aReference)
    {
        if (aReference != null)
        {
            m_referenceObjects.put(aReference.getName().toLowerCase(), aReference);
        } else
        {
            System.out.println("Error in setup: " + this + ": Try to add null reference");
        }
    }

    private AttributeImpl getAttribute(String aName) throws UnknownAttributeException
    {
        AttributeImpl retVal = m_attributeMap.get(aName.toLowerCase());
        if (retVal == null)
        {
            throw new UnknownAttributeException("Unkbown attribute name '" + aName + "' in " + this.getClass().toString());
        }
        return retVal;
    }

    UnknownAttributeException attributeCast(String aName, Class aClass)
    {
        return new UnknownAttributeException("Unknown attribute name '" + aName + "' of class " + aClass.toString() + " in " + this.getClass().toString());
    }

    UnknownAttributeException attributeCast(int anIndex, Class aClass)
    {
        return new UnknownAttributeException("Unknown attribute index '" + anIndex + "' of class " + aClass.toString() + " in " + this.getClass().toString());
    }

    private AttributeImpl getAttribute(String aName, Class aClass) throws UnknownAttributeException
    {

        AttributeImpl retVal = getAttribute(aName);
        if (!retVal.getAttributeClass().isAssignableFrom(aClass))
        {
            throw attributeCast(aName, aClass);
        }


        return m_attributeMap.get(aName.toLowerCase());
    }

    private AttributeImpl getAttribute(int anIndex) throws UnknownAttributeException
    {
        if (!m_listSorted)
        {
            arrangeList();
        }
        if (m_attributeList.size() > anIndex && anIndex >= 0)
        {
            return m_attributeList.get(anIndex);
        }
        throw new UnknownAttributeException("Unkbown attribute index " + anIndex + " in " + this.getClass().toString());
    }

    public Map getAttributes()
    {
        return m_attributeMap;
    }

    public Map getReferenceObjects()
    {
        return m_referenceObjects;
    }

    public Map getReferenceLists()
    {
        return m_referenceLists;
    }

    public abstract void addObjectReference(IMsoObjectIf anObject, String aReferenceName);

    public abstract void removeObjectReference(IMsoObjectIf anObject, String aReferenceName);

    public IAttributeIf.IMsoBooleanIf getBooleanAttribute(int anIndex) throws UnknownAttributeException
    {
        try
        {
            return (IAttributeIf.IMsoBooleanIf) getAttribute(anIndex);
        }
        catch (ClassCastException e)
        {
            throw attributeCast(anIndex, Boolean.class);
        }
    }

    public IAttributeIf.IMsoBooleanIf getBooleanAttribute(String aName) throws UnknownAttributeException
    {
        try
        {
            return (IAttributeIf.IMsoBooleanIf) getAttribute(aName);
        }
        catch (ClassCastException e)
        {
            throw attributeCast(aName, Boolean.class);
        }
    }

    public IAttributeIf.IMsoIntegerIf getIntegerAttribute(int anIndex) throws UnknownAttributeException
    {
        try
        {
            return (IAttributeIf.IMsoIntegerIf) getAttribute(anIndex);
        }
        catch (ClassCastException e)
        {
            throw attributeCast(anIndex, Integer.class);
        }
    }

    public IAttributeIf.IMsoIntegerIf getIntegerAttribute(String aName) throws UnknownAttributeException
    {
        try
        {
            return (IAttributeIf.IMsoIntegerIf) getAttribute(aName);
        }
        catch (ClassCastException e)
        {
            throw attributeCast(aName, Integer.class);
        }
    }

//    public IAttributeIf.IMsoLongIf getLongAttribute(int anIndex) throws UnknownAttributeException
//    {
//        try
//        {
//            return (IAttributeIf.IMsoLongIf) getAttribute(anIndex);
//        }
//        catch (ClassCastException e)
//        {
//            throw attributeCast(anIndex, Long.class);
//        }
//    }
//
//    public IAttributeIf.IMsoLongIf getLongAttribute(String aName) throws UnknownAttributeException
//    {
//        try
//        {
//            return (IAttributeIf.IMsoLongIf) getAttribute(aName);
//        }
//        catch (ClassCastException e)
//        {
//            throw attributeCast(aName, Long.class);
//        }
//    }

    public IAttributeIf.IMsoDoubleIf getDoubleAttribute(int anIndex) throws UnknownAttributeException
    {
        try
        {
            return (IAttributeIf.IMsoDoubleIf) getAttribute(anIndex);
        }
        catch (ClassCastException e)
        {
            throw attributeCast(anIndex, Double.class);
        }
    }

    public IAttributeIf.IMsoDoubleIf getDoubleAttribute(String aName) throws UnknownAttributeException
    {
        try
        {
            return (IAttributeIf.IMsoDoubleIf) getAttribute(aName);
        }
        catch (ClassCastException e)
        {
            throw attributeCast(aName, Double.class);
        }
    }

    public IAttributeIf.IMsoStringIf getStringAttribute(int anIndex) throws UnknownAttributeException
    {
        try
        {
            return (IAttributeIf.IMsoStringIf) getAttribute(anIndex);
        }
        catch (ClassCastException e)
        {
            throw attributeCast(anIndex, String.class);
        }
    }

    public IAttributeIf.IMsoStringIf getStringAttribute(String aName) throws UnknownAttributeException
    {
        try
        {
            return (IAttributeIf.IMsoStringIf) getAttribute(aName);
        }
        catch (ClassCastException e)
        {
            throw attributeCast(aName, String.class);
        }
    }

    public IAttributeIf.IMsoCalendarIf getCalendarAttribute(int anIndex) throws UnknownAttributeException
    {
        try
        {
            return (IAttributeIf.IMsoCalendarIf) getAttribute(anIndex);
        }
        catch (ClassCastException e)
        {
            throw attributeCast(anIndex, Calendar.class);
        }
    }

    public IAttributeIf.IMsoCalendarIf getCalendarAttribute(String aName) throws UnknownAttributeException
    {
        try
        {
            return (IAttributeIf.IMsoCalendarIf) getAttribute(aName);
        }
        catch (ClassCastException e)
        {
            throw attributeCast(aName, Calendar.class);
        }
    }

    public IAttributeIf.IMsoPositionIf getPositionAttribute(int anIndex) throws UnknownAttributeException
    {
        try
        {
            return (IAttributeIf.IMsoPositionIf) getAttribute(anIndex);
        }
        catch (ClassCastException e)
        {
            throw attributeCast(anIndex, Position.class);
        }
    }

    public IAttributeIf.IMsoPositionIf getPositionAttribute(String aName) throws UnknownAttributeException
    {
        try
        {
            return (IAttributeIf.IMsoPositionIf) getAttribute(aName);
        }
        catch (ClassCastException e)
        {
            throw attributeCast(aName, Position.class);
        }
    }

    public IAttributeIf.IMsoTimePosIf getTimePosAttribute(int anIndex) throws UnknownAttributeException
    {
        try
        {
            return (IAttributeIf.IMsoTimePosIf) getAttribute(anIndex);
        }
        catch (ClassCastException e)
        {
            throw attributeCast(anIndex, TimePos.class);
        }
    }

    public IAttributeIf.IMsoTimePosIf getTimePosAttribute(String aName) throws UnknownAttributeException
    {
        try
        {
            return (IAttributeIf.IMsoTimePosIf) getAttribute(aName);
        }
        catch (ClassCastException e)
        {
            throw attributeCast(aName, TimePos.class);
        }
    }

    public IAttributeIf.IMsoTrackIf getTrackAttribute(int anIndex) throws UnknownAttributeException
    {
        try
        {
            return (IAttributeIf.IMsoTrackIf) getAttribute(anIndex);
        }
        catch (ClassCastException e)
        {
            throw attributeCast(anIndex, Track.class);
        }
    }

    public IAttributeIf.IMsoTrackIf getTrackAttribute(String aName) throws UnknownAttributeException
    {
        try
        {
            return (IAttributeIf.IMsoTrackIf) getAttribute(aName);
        }
        catch (ClassCastException e)
        {
            throw attributeCast(aName, Track.class);
        }
    }

    public IAttributeIf.IMsoRouteIf getRouteAttribute(int anIndex) throws UnknownAttributeException
    {
        try
        {
            return (IAttributeIf.IMsoRouteIf) getAttribute(anIndex);
        }
        catch (ClassCastException e)
        {
            throw attributeCast(anIndex, Route.class);
        }
    }

    public IAttributeIf.IMsoRouteIf getRouteAttribute(String aName) throws UnknownAttributeException
    {
        try
        {
            return (IAttributeIf.IMsoRouteIf) getAttribute(aName);
        }
        catch (ClassCastException e)
        {
            throw attributeCast(aName, Route.class);
        }
    }

    public IAttributeIf.IMsoPolygonIf getPolygonAttribute(int anIndex) throws UnknownAttributeException
    {
        try
        {
            return (IAttributeIf.IMsoPolygonIf) getAttribute(anIndex);
        }
        catch (ClassCastException e)
        {
            throw attributeCast(anIndex, Polygon.class);
        }
    }

    public IAttributeIf.IMsoPolygonIf getPolygonAttribute(String aName) throws UnknownAttributeException
    {
        try
        {
            return (IAttributeIf.IMsoPolygonIf) getAttribute(aName);
        }
        catch (ClassCastException e)
        {
            throw attributeCast(aName, Polygon.class);
        }
    }

    public IAttributeIf.IMsoEnumIf getEnumAttribute(int anIndex) throws UnknownAttributeException
    {
        try
        {
            return (IAttributeIf.IMsoEnumIf) getAttribute(anIndex);
        }
        catch (ClassCastException e)
        {
            throw attributeCast(anIndex, Enum.class);
        }
    }

    public IAttributeIf.IMsoEnumIf getEnumAttribute(String aName) throws UnknownAttributeException
    {
        try
        {
            return (IAttributeIf.IMsoEnumIf) getAttribute(aName);
        }
        catch (ClassCastException e)
        {
            throw attributeCast(aName, Enum.class);
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
            anAttr.set(aValue);
        } else
        {
            throw new MsoNullPointerException("Trying to assign value to null attribute");
        }
    }

    public void setAttribute(String aName, Object aValue) throws UnknownAttributeException
    {
        AttributeImpl attr = getAttribute(aName);
        attr.set(aValue);
    }

    public void setAttribute(int anIndex, Object aValue) throws UnknownAttributeException
    {
        AttributeImpl attr = getAttribute(anIndex);
        attr.set(aValue);
    }

    private void registerUpdate(MsoEvent.EventType anEventType, boolean updateServer)
    {
        IMsoModelIf.UpdateMode anUpdateMode = MsoModelImpl.getInstance().getUpdateMode();
        int clientEventTypeMask = anEventType.maskValue();
        int serverEventTypeMask = (updateServer && anUpdateMode == IMsoModelIf.UpdateMode.LOCAL_UPDATE_MODE) ? clientEventTypeMask : 0;
        System.out.println("RegisterUpdate:  " + getObjectId() + " " + getMsoClassCode() + " Mask: " + clientEventTypeMask + " " + anUpdateMode);

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

        for (MsoListImpl list : m_referenceLists.values())
        {
            list.rollback();
        }

        for (MsoReferenceImpl reference : m_referenceObjects.values())
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

        for (MsoListImpl list : m_referenceLists.values())
        {
            list.commitLocal();
        }

        for (MsoReferenceImpl reference : m_referenceObjects.values())
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
        for (MsoListImpl list : m_referenceLists.values())
        {
            list.resumeNotifications();
        }
    }

    public static String getBundleText(ResourceBundle aBundle, String aKey)
    {
        if (aBundle == null)
        {
            return aKey;
        }
        try
        {
            String retVal = aBundle.getString(aKey);
            return retVal != null ? retVal : aKey;
        }
        catch (Exception e)
        {
            return aKey;
        }
    }

    public Collection<ICommittableIf.ICommitReferenceIf> getCommittableAttributeRelations()
    {
        Vector<ICommittableIf.ICommitReferenceIf> retVal = new Vector<ICommittableIf.ICommitReferenceIf>();
        for (MsoReferenceImpl reference : m_referenceObjects.values())
        {
            retVal.addAll(reference.getCommittableRelations());
        }
        return retVal;
    }

    public Collection<ICommittableIf.ICommitReferenceIf> getCommittableListRelations()
    {
        Vector<ICommittableIf.ICommitReferenceIf> retVal = new Vector<ICommittableIf.ICommitReferenceIf>();
        for (MsoListImpl list : m_referenceLists.values())
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
         *
         * @param aStatus        The status to test against
         * @param anAttributName Name of Enum attribute
         */
        public EnumSelector(T aStatus, String anAttributName)
        {
            m_selectValue = aStatus;
            m_attributeName = anAttributName;
        }

        public boolean select(M anObject)
        {
            try
            {
                return anObject.getEnumAttribute(m_attributeName) == m_selectValue;
            }
            catch (UnknownAttributeException e)
            {
                return false;
            }
        }
    }

    public static class StatusSelector<T extends IEnumStatusHolder, E extends Enum> implements Selector<T>
    {
        E m_selectValue;

        /**
         * Construct a Selector object
         *
         * @param aStatus The status to test against
         */
        public StatusSelector(E aStatus)
        {
            m_selectValue = aStatus;
        }

        public boolean select(T anObject)
        {
            return anObject.getStatus() == m_selectValue;
        }
    }

    /**
     * Selector used for selecting assignments with status in a given set.
     */
    public static class StatusSetSelector<T extends IEnumStatusHolder<E>, E extends Enum> implements Selector<T>
    {
        EnumSet<? extends E> m_valueSet;

        /**
         * Construct a Selector object
         *
         * @param aValueSet The status set to test against
         */
        public StatusSetSelector(EnumSet<? extends E> aValueSet)
        {
            m_valueSet = aValueSet;
        }

        public StatusSetSelector()
        {
            //To change body of created methods use File | Settings | File Templates.
        }

        public boolean select(T anObject)
        {
            return m_valueSet.contains(anObject.getStatus());
        }
    }


}