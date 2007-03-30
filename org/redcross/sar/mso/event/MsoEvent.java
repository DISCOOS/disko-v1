package org.redcross.sar.mso.event;

import org.redcross.sar.mso.committer.ICommitWrapperIf;
import org.redcross.sar.mso.data.IAttributeIf;
import org.redcross.sar.mso.data.IMsoObjectIf;

/**
 * Class for event objects in a observer/observable pattern for the MSO model.
 * <p/>
 * The event object is passed from updating (observable) to updated (observer) objects.
 * <p/>
 * The MSO model requires three sets of listeners: Client Update Listeners handle updates sent to the client, either by the user or the server.
 * The Server Update Listener shall handle update that shall be sent to the server at a (later) commit. Commit Listeners handle the Commit / Rollback events.
 */
public class MsoEvent extends java.util.EventObject
{
    /**
     * EventImpl( type with maskValue values.
     */
    public enum EventType
    {
        EMPTY_EVENT(0),
        ADDED_REFERENCE_EVENT(1),
        REMOVED_REFERENCE_EVENT(2),
        MODIFIED_REFERENCE_EVENT(4),
        CREATED_OBJECT_EVENT(8),
        DELETED_OBJECT_EVENT(16),
        MODIFIED_DATA_EVENT(32),
        COMMIT_EVENT(64);

        private final int m_maskValue;

        /**
         * Constructof of Enum members
         *
         * @param value Value relatee to member.
         */
        EventType(int value)
        {
            this.m_maskValue = value;
        }

        /**
         * Get mask value for this Enum member
         *
         * @return The mask value
         */
        public int maskValue()
        {
            return m_maskValue;
        }
    }

    private final int m_eventTypeMask;

    /**
     * Create event for a specific {@link org.redcross.sar.mso.event.MsoEvent.EventType}
     *
     * @param aSource         The source object
     * @param anEventTypeMask See {@link org.redcross.sar.mso.event.MsoEvent.EventType}
     */
    public MsoEvent(Object aSource, int anEventTypeMask)
    {
        super(aSource);
        m_eventTypeMask = anEventTypeMask;
    }

    /**
     * Get EventImpl type maskValue
     *
     * @return The sum of {@link org.redcross.sar.mso.event.MsoEvent.EventType} values for this event.
     */
    public int getEventTypeMask()
    {
        return m_eventTypeMask;
    }

    public static class Update extends MsoEvent
    {
        public Update(Object aSource, int anEventTypeMask)
        {
            super(aSource, anEventTypeMask);
        }
    }

    public static class Commit extends MsoEvent
    {
        public Commit(ICommitWrapperIf aSource, int anEventTypeMask)
        {
            super(aSource, anEventTypeMask);
        }
    }

    public static class Gis extends MsoEvent
    {
        public Gis(IAttributeIf aSource)
        {
            super(aSource, 0);
        }
    }

    public static class Item extends MsoEvent
    {
        public Item(IMsoObjectIf aSource, int anEventTypeMask)
        {
            super(aSource, anEventTypeMask);
        }
    }

}