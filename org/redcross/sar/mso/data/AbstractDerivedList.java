package org.redcross.sar.mso.data;

import org.redcross.sar.mso.MsoModelImpl;
import org.redcross.sar.mso.event.IMsoDerivedUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent;
import org.redcross.sar.util.mso.Selector;

import java.util.*;

/**
 *
 */
public abstract class AbstractDerivedList<M extends IMsoObjectIf> implements IMsoDerivedListIf<M>, IMsoDerivedUpdateListenerIf
{
    protected final HashMap<String, M> m_items;

    public AbstractDerivedList()
    {
        this(50);
    }

    public AbstractDerivedList(int aSize)
    {
        m_items = new LinkedHashMap<String, M>(aSize);
        MsoModelImpl.getInstance().getEventManager().addDerivedUpdateListener(this);
    }

    public Collection<M> getItems()
    {
        return m_items.values();
    }

    public int size()
    {
        return m_items.size();
    }

    public List<M> selectItems(Selector<M> aSelector, Comparator<M> aComparator)
    {
        return MsoListImpl.selectItemsInCollection(aSelector,aComparator,getItems());
    }

    public M selectSingleItem(Selector<M> aSelector)
    {
        return MsoListImpl.selectSingleItem(aSelector,getItems());
    }

    static final int mask = MsoEvent.EventType.CREATED_OBJECT_EVENT.maskValue()
                | MsoEvent.EventType.DELETED_OBJECT_EVENT.maskValue()
                | MsoEvent.EventType.MODIFIED_DATA_EVENT.maskValue();


    public void handleMsoDerivedUpdateEvent(MsoEvent.DerivedUpdate e)
    {
        if (!hasInterestIn(e.getSource()))
        {
            return;
        }

        if ((e.getEventTypeMask() & mask) != 0)
        {
            if (e.isCreateObjectEvent())
            {
                handleItemCreate(e.getSource());
            } else if (e.isDeleteObjectEvent())
            {
                handleItemDelete(e.getSource());
            } else if (e.isModifyObjectEvent())
            {
                handleItemModify(e.getSource());
            }
        }
    }

    public abstract boolean hasInterestIn(Object anObject);

    public abstract void handleItemCreate(Object anObject);

    public abstract void handleItemDelete(Object anObject);

    public abstract void handleItemModify(Object anObject);

    }
