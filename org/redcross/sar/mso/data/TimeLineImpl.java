package org.redcross.sar.mso.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimeLineImpl extends AbstractDerivedList<ITimeItemIf> implements ITimeLineIf
{
    final List<ITimeItemIf> m_timeItems = new ArrayList<ITimeItemIf>();

    // todo Sorting can be optimized by using a sorting algorithm that takes into account that only one object has been added/changed 

    public TimeLineImpl()
    {
        super();
    }

    public List<ITimeItemIf> getTimeItems()
    {
        return m_timeItems;
    }

    public boolean hasInterestIn(Object anObject)
    {
        return anObject instanceof AbstractTimeItem;
    }

    public void handleItemCreate(Object anObject)
    {
        ITimeItemIf item = (ITimeItemIf) anObject;
        m_items.put(item.getObjectId(),item);
        m_timeItems.add(item);
        Collections.sort(m_timeItems);
    }

    public void handleItemDelete(Object anObject)
    {
        ITimeItemIf item = (ITimeItemIf) anObject;
        m_items.remove(item.getObjectId());
        m_timeItems.remove(item);
//        Collections.sort(m_timeItems);
    }

    public void handleItemModify(Object anObject)
    {
        Collections.sort(m_timeItems);
    }

    public void print()
    {
        for (ITimeItemIf to : m_timeItems)
        {
            System.out.println("Time item: " + to.toString());
        }
    }

}