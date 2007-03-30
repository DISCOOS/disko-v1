package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.committer.CommittableImpl;

import java.util.Collection;

/**
 * Interface for MSO object
 */
public interface IMsoObjectIf
{
    /**
     * Get Object ID
     *
     * @return The Object ID
     */
    public String getObjectId();

    public IMsoManagerIf.MsoClassCode getMsoClassCode();

    /**
     * Suspend notification of listeners.
     * <p/>
     * Is used when several updates of an object shall be sent to listeners as one event.
     */
//    public void suspendNotify();

    /**
     * Add a "listener" to MsoObject deleteObject.
     *
     * @param aHolder Listener to add.
     */
//    public void addDeleteListener(IMsoObjectHolderIf aHolder);

    /**
     * Remove a "listener" to MsoObject deleteObject.
     *
     * @param aHolder Listener to remove.
     */
//    public void removeDeleteListener(IMsoObjectHolderIf aHolder);

    /**
     * Delete this object from the data structures
     *
     * @return <code>true</code> if object has been deleted, <code>false</code> otherwise.
     */
    public boolean deleteObject();

    /**
     * Resume notification of listeners.
     * <p/>
     * Is used if notification has been suspended by {@link #suspendNotify()}.
     */
//    public void resumeNotify();

//    public void resumeNotifications();

//    public void registerAddedReference();

//    public void registerRemovedReference();

//    public void registerRemovedReference(boolean updateServer);

    /**
     * Register modified reference.
     * Can fire a {@link org.redcross.sar.mso.event.MsoEvent}
     */
//    public void registerModifiedReference();

    /**
     * Register modified reference.
     * Can fire a {@link org.redcross.sar.mso.event.MsoEvent}
     */
//    public void registerModifiedReference(boolean updateServer);

//    public void registerCreatedObject();

//    public void registerDeletedObject();

    /**
     * Register modified data.
     * Can fire a {@link org.redcross.sar.mso.event.MsoEvent}
     */
//    public void registerModifiedData();


    /**
     * Rollback local changes.
     */
//    public void rollback();

    /**
     * Commit local changes.
     */
//    public void commitLocal();

    /**
     * Get a Boolean attribute with the given index.
     *
     * @param anIndex Attribute index.
     * @return The attribute, if it exists a and is of the right type, otherwise null.
     */
    public IAttributeIf.IMsoBooleanIf getBooleanAttribute(int anIndex);

    /**
     * Get a Boolean attribute with the given name.
     *
     * @param aName Attribute name.
     * @return The attribute, if it exists a and is of the right type, otherwise null.
     */
    public IAttributeIf.IMsoBooleanIf getBooleanAttribute(String aName);

    /**
     * Get an Integer attribute with the given index.
     *
     * @param anIndex Attribute index.
     * @return The attribute, if it exists a and is of the right type, otherwise null.
     */
    public IAttributeIf.IMsoIntegerIf getIntegerAttribute(int anIndex);

    /**
     * Get a Boolean attribute with the given name.
     *
     * @param aName Attribute name.
     * @return The attribute, if it exists a and is of the right type, otherwise null.
     */
    public IAttributeIf.IMsoIntegerIf getIntegerAttribute(String aName);

    /**
     * Get a Long attribute with the given index.
     *
     * @param anIndex Attribute index.
     * @return The attribute, if it exists a and is of the right type, otherwise null.
     */
    public IAttributeIf.IMsoLongIf getLongAttribute(int anIndex);

    /**
     * Get a Boolean attribute with the given name.
     *
     * @param aName Attribute name.
     * @return The attribute, if it exists a and is of the right type, otherwise null.
     */
    public IAttributeIf.IMsoLongIf getLongAttribute(String aName);

    /**
     * Get a Double attribute with the given index.
     *
     * @param anIndex Attribute index.
     * @return The attribute, if it exists a and is of the right type, otherwise null.
     */
    public IAttributeIf.IMsoDoubleIf getDoubleAttribute(int anIndex);

    /**
     * Get a Boolean attribute with the given name.
     *
     * @param aName Attribute name.
     * @return The attribute, if it exists a and is of the right type, otherwise null.
     */
    public IAttributeIf.IMsoDoubleIf getDoubleAttribute(String aName);

    /**
     * Get a String attribute with the given index.
     *
     * @param anIndex Attribute index.
     * @return The attribute, if it exists a and is of the right type, otherwise null.
     */
    public IAttributeIf.IMsoStringIf getStringAttribute(int anIndex);

    /**
     * Get a Boolean attribute with the given name.
     *
     * @param aName Attribute name.
     * @return The attribute, if it exists a and is of the right type, otherwise null.
     */
    public IAttributeIf.IMsoStringIf getStringAttribute(String aName);

    /**
     * Get a Calendar attribute with the given index.
     *
     * @param anIndex Attribute index.
     * @return The attribute, if it exists a and is of the right type, otherwise null.
     */
    public IAttributeIf.IMsoCalendarIf getCalendarAttribute(int anIndex);

    /**
     * Get a Boolean attribute with the given name.
     *
     * @param aName Attribute name.
     * @return The attribute, if it exists a and is of the right type, otherwise null.
     */
    public IAttributeIf.IMsoCalendarIf getCalendarAttribute(String aName);

    /**
     * Get a {@link org.redcross.sar.mso.data.IAttributeIf.IMsoPositionIf} attribute with the given index.
     *
     * @param anIndex Attribute index.
     * @return The attribute, if it exists a and is of the right type, otherwise null.
     */
    public IAttributeIf.IMsoPositionIf getPositionAttribute(int anIndex);

    /**
     * Get a {@link org.redcross.sar.mso.data.IAttributeIf.IMsoPositionIf}  attribute with the given name.
     *
     * @param aName Attribute name.
     * @return The attribute, if it exists a and is of the right type, otherwise null.
     */
    public IAttributeIf.IMsoPositionIf getPositionAttribute(String aName);

    /**
     * Get a {@link org.redcross.sar.mso.data.IAttributeIf.IMsoTimePosIf} attribute with the given index.
     *
     * @param anIndex Attribute index.
     * @return The attribute, if it exists a and is of the right type, otherwise null.
     */
    public IAttributeIf.IMsoTimePosIf getTimePosAttribute(int anIndex);

    /**
     * Get a {@link org.redcross.sar.mso.data.IAttributeIf.IMsoTimePosIf}  attribute with the given name.
     *
     * @param aName Attribute name.
     * @return The attribute, if it exists a and is of the right type, otherwise null.
     */
    public IAttributeIf.IMsoTimePosIf getTimePosAttribute(String aName);

    /**
     * Get a {@link org.redcross.sar.mso.data.IAttributeIf.IMsoTrackIf} attribute with the given index.
     *
     * @param anIndex Attribute index.
     * @return The attribute, if it exists a and is of the right type, otherwise null.
     */
    public IAttributeIf.IMsoTrackIf getTrackAttribute(int anIndex);

    /**
     * Get a {@link org.redcross.sar.mso.data.IAttributeIf.IMsoTrackIf}  attribute with the given name.
     *
     * @param aName Attribute name.
     * @return The attribute, if it exists a and is of the right type, otherwise null.
     */
    public IAttributeIf.IMsoTrackIf getTrackAttribute(String aName);
    /**
     * Get a {@link org.redcross.sar.mso.data.IAttributeIf.IMsoRouteIf} attribute with the given index.
     *
     * @param anIndex Attribute index.
     * @return The attribute, if it exists a and is of the right type, otherwise null.
     */
    public IAttributeIf.IMsoRouteIf getRouteAttribute(int anIndex);

    /**
     * Get a {@link org.redcross.sar.mso.data.IAttributeIf.IMsoRouteIf}  attribute with the given name.
     *
     * @param aName Attribute name.
     * @return The attribute, if it exists a and is of the right type, otherwise null.
     */
    public IAttributeIf.IMsoRouteIf getRouteAttribute(String aName);

    /**
     * Get a {@link org.redcross.sar.mso.data.IAttributeIf.IMsoPolygonIf} attribute with the given index.
     *
     * @param anIndex Attribute index.
     * @return The attribute, if it exists a and is of the right type, otherwise null.
     */
    public IAttributeIf.IMsoPolygonIf getPolygonAttribute(int anIndex);

    /**
     * Get a {@link org.redcross.sar.mso.data.IAttributeIf.IMsoPolygonIf}  attribute with the given name.
     *
     * @param aName Attribute name.
     * @return The attribute, if it exists a and is of the right type, otherwise null.
     */
    public IAttributeIf.IMsoPolygonIf getPolygonAttribute(String aName);

    /**
     * Get a {@link org.redcross.sar.mso.data.IAttributeIf.IMsoPolygonIf} attribute with the given index.
     *
     * @param anIndex Attribute index.
     * @return The attribute, if it exists a and is of the right type, otherwise null.
     */
    public IAttributeIf.IMsoEnumIf getEnumAttribute(int anIndex);

    /**
     * Get a {@link org.redcross.sar.mso.data.IAttributeIf.IMsoPolygonIf}  attribute with the given name.
     *
     * @param aName Attribute name.
     * @return The attribute, if it exists a and is of the right type, otherwise null.
     */
    public IAttributeIf.IMsoEnumIf getEnumAttribute(String aName);

    public Collection<CommittableImpl.CommitReference> getCommittableRelations();

    public interface IObjectIdIf
    {
        public String getId();
    }
}
