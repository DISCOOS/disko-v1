package org.redcross.sar.mso.data;

import org.redcross.sar.mso.MsoModelImpl;
import org.redcross.sar.mso.event.IMsoItemListenerIf;
import org.redcross.sar.mso.event.MsoEvent;
import org.redcross.sar.util.mso.Selector;

import java.util.*;

/**
 *
 */
public abstract class AbstractDerivedList<M extends IMsoObjectIf> implements IMsoDerivedListIf<M>, IMsoItemListenerIf
{
    protected final HashMap<String, M> m_items;

    public AbstractDerivedList()
    {
        this(50);
    }

    public AbstractDerivedList(int aSize)
    {
        m_items = new HashMap<String, M>(aSize);
        MsoModelImpl.getInstance().getEventManager().addItemListener(this);
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

    public void handleMsoItemEvent(MsoEvent.Item e)
    {
        int mask = MsoEvent.EventType.CREATED_OBJECT_EVENT.maskValue()
                | MsoEvent.EventType.DELETED_OBJECT_EVENT.maskValue()
                | MsoEvent.EventType.MODIFIED_DATA_EVENT.maskValue();

        if (!hasInterestIn(e.getSource()))
        {
            return;
        }

        if ((e.getEventTypeMask() & mask) != 0)
        {
            if ((e.getEventTypeMask() & MsoEvent.EventType.CREATED_OBJECT_EVENT.maskValue()) != 0)
            {
                handleItemCreate(e.getSource());
            } else if ((e.getEventTypeMask() & MsoEvent.EventType.DELETED_OBJECT_EVENT.maskValue()) != 0)
            {
                handleItemDelete(e.getSource());
            } else if ((e.getEventTypeMask() & MsoEvent.EventType.MODIFIED_DATA_EVENT.maskValue()) != 0)
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
