package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.MsoModelImpl;
import org.redcross.sar.util.except.IllegalOperationException;
import org.redcross.sar.util.except.MsoCastException;
import org.redcross.sar.util.mso.IGeodataIf;
import org.redcross.sar.util.mso.Selector;

import java.util.*;

/**
 * Strip of field to search
 */
public class AreaImpl extends AbstractMsoObject implements IAreaIf
{
// todo remove   private final AttributeImpl.MsoGeoList m_geodata = new AttributeImpl.MsoGeoList(this, "Geodata");
    private final AttributeImpl.MsoString m_remarks = new AttributeImpl.MsoString(this, "Remarks");
    private final POIListImpl m_areaPOIs = new POIListImpl(this, "AreaPOIs", false);
    private final MsoListImpl<IMsoObjectIf> m_areaGeodata = new MsoListImpl<IMsoObjectIf>(this, "AreaGeodata", false);

    public AreaImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    protected void defineAttributes()
    {
// todo remove        addAttribute(m_geodata);
        addAttribute(m_remarks);
    }

    protected void defineLists()
    {
        addList(m_areaPOIs);
        addList(m_areaGeodata);
    }

    protected void defineReferences()
    {
    }

    public void addObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
        if (anObject instanceof IRouteIf || anObject instanceof ITrackIf)
        {
            m_areaGeodata.add(anObject);
            return;
        }
        if (anObject instanceof IPOIIf)
        {
            if ("AreaPOIs".equals(aReferenceName))
            {
                m_areaPOIs.add((IPOIIf) anObject);
            } else if ("AreaGeodata".equals(aReferenceName))
            {
                m_areaGeodata.add(anObject);
            }
        }
    }

    public void removeObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
        if (anObject instanceof IRouteIf || anObject instanceof ITrackIf)
        {
            m_areaGeodata.removeReference(anObject);
            return;
        }
        if (anObject instanceof IPOIIf)
        {
            if ("AreaPOIs".equals(aReferenceName))
            {
                m_areaPOIs.removeReference((IPOIIf) anObject);
            } else if ("AreaGeodata".equals(aReferenceName))
            {
                m_areaGeodata.removeReference(anObject);
            }
        }
    }


    public static AreaImpl implementationOf(IAreaIf anInterface) throws MsoCastException
    {
        try
        {
            return (AreaImpl) anInterface;
        }
        catch (ClassCastException e)
        {
            throw new MsoCastException("Illegal cast to AreaImpl");
        }
    }

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_AREA;
    }


// todo remove   public void setGeodata(GeoList aGeodata)
//    {
//        m_geodata.setValue(aGeodata);
//    }
//
//    public GeoList getGeodata()
//    {
//        return m_geodata.getGeoList();
//    }
//
//    public IMsoModelIf.ModificationState getGeodataState()
//    {
//        return m_geodata.getState();
//    }
//
//    public IAttributeIf.IMsoGeoListIf getGeodataAttribute()
//    {
//        return m_geodata;
//    }

    public void setRemarks(String aRemarks)
    {
        m_remarks.setValue(aRemarks);
    }

    public String getRemarks()
    {
        return m_remarks.getString();
    }

    public IMsoModelIf.ModificationState getRemarksState()
    {
        return m_remarks.getState();
    }

    public IAttributeIf.IMsoStringIf getRemarksAttribute()
    {
        return m_remarks;
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for lists
    *-------------------------------------------------------------------------------------------*/

    public void addAreaPOI(IPOIIf anIPOIIf)
    {
        m_areaPOIs.add(anIPOIIf);
    }

    public IPOIListIf getAreaPOIs()
    {
        return m_areaPOIs;
    }

    public IMsoModelIf.ModificationState getAreaPOIsState(IPOIIf anIPOIIf)
    {
        return m_areaPOIs.getState(anIPOIIf);
    }

    public Collection<IPOIIf> getAreaPOIsItems()
    {
        return m_areaPOIs.getItems();
    }


    public void setAreaGeodataItem(int anIndex, IMsoObjectIf anMsoObjectIf)
    {
        IMsoObjectIf oldItem = getGeodataAt(anIndex);
        if (oldItem != null)
        {
            m_areaGeodata.removeReference(oldItem);
        }
        addAreaGeodata(anIndex, anMsoObjectIf);
    }

    public void addAreaGeodata(IMsoObjectIf anMsoObjectIf)
    {
        addAreaGeodata(getNextGeodataSequenceNumber(), anMsoObjectIf);
    }

    public void addAreaGeodata(int aNr, IMsoObjectIf anMsoObjectIf)
    {
        if (setAreaSequenceNumber(anMsoObjectIf, aNr))
        {
            m_areaGeodata.add(anMsoObjectIf);
        }
    }

    private boolean setAreaSequenceNumber(IMsoObjectIf anMsoObjectIf, int aNr)
    {
        if (anMsoObjectIf instanceof IPOIIf)
        {
            ((IPOIIf) anMsoObjectIf).setAreaSequenceNumber(aNr);
            return true;
        } else if (anMsoObjectIf instanceof ITrackIf)
        {
            ((ITrackIf) anMsoObjectIf).setAreaSequenceNumber(aNr);
            return true;
        } else if (anMsoObjectIf instanceof IRouteIf)
        {
            ((IRouteIf) anMsoObjectIf).setAreaSequenceNumber(aNr);
            return true;
        } else
        {
            return false;
        }
    }

    public IMsoListIf<IMsoObjectIf> getAreaGeodata()
    {
        return m_areaGeodata;
    }

    public IMsoModelIf.ModificationState getAreaGeodataState(IMsoObjectIf anMsoObjectIf)
    {
        return m_areaGeodata.getState(anMsoObjectIf);
    }

    public Collection<IMsoObjectIf> getAreaGeodataItems()
    {
        return m_areaGeodata.getItems();
    }

    /*-------------------------------------------------------------------------------------------
    * Other specified methods
    *-------------------------------------------------------------------------------------------*/

    private final SelfSelector<IAreaIf, IAssignmentIf> owningAssigmentSelector = new SelfSelector<IAreaIf, IAssignmentIf>(this)
    {
        public boolean select(IAssignmentIf anObject)
        {
            return (anObject.getPlannedArea() == m_object || anObject.getReportedArea() == m_object); // todo Check status as well?
        }
    };

    public IAssignmentIf getOwningAssignment()
    {
        return MsoModelImpl.getInstance().getMsoManager().getCmdPost().getAssignmentList().selectSingleItem(owningAssigmentSelector);
    }

    public void verifyAssignable(IAssignmentIf anAssignment) throws IllegalOperationException
    {
        IAssignmentIf asg = getOwningAssignment();
        if (asg != null && asg != anAssignment)
        {
            throw new IllegalOperationException("Area " + this + " is already assigned to an assigment.");
        }
    }

    private int getNextPOISequenceNumber()
    {
        int retVal = 0;
        for (IPOIIf ml : m_areaPOIs.getItems())
        {
            if (ml.getAreaSequenceNumber() > retVal)
            {
                retVal = ml.getAreaSequenceNumber();
            }
        }
        return retVal + 1;
    }

    private int getNextGeodataSequenceNumber()
    {
        int retVal = -1;
        for (IMsoObjectIf ml : m_areaGeodata.getItems())
        {
            int i = getAreaSequenceNumber(ml);

            if (i > retVal)
            {
                retVal = i;
            }
        }
        return retVal + 1;
    }

    private int getAreaSequenceNumber(IMsoObjectIf ml)
    {
        if (ml instanceof IPOIIf)                   // todo sjekk etter endring av GeoCollection
        {
            IPOIIf ml1 = (IPOIIf) ml;
            return ((IPOIIf) ml).getAreaSequenceNumber();
        }
        if (ml instanceof ITrackIf)
        {
            ITrackIf ml1 = (ITrackIf) ml;
            return ml1.getAreaSequenceNumber();
        }
        if (ml instanceof IRouteIf)
        {
            IRouteIf ml1 = (IRouteIf) ml;
            return ml1.getAreaSequenceNumber();
        }
        return 0;
    }

    public IMsoObjectIf getGeodataAt(int anIndex)
    {
        int i = 0;
        for (IMsoObjectIf ml : m_areaGeodata.getItems())
        {
            if (getAreaSequenceNumber(ml) == anIndex)
            {
                return ml;
            }
        }
        return null;
    }

    public Iterator<IGeodataIf> getAreaGeodataIterator()
    {
        List<IMsoObjectIf> x = m_areaGeodata.selectItems(new Selector<IMsoObjectIf>()
        {
            public boolean select(IMsoObjectIf anObject)
            {
                return true;
            }
        }, new Comparator<IMsoObjectIf>()
        {
            public int compare(IMsoObjectIf o1, IMsoObjectIf o2)
            {
                return getAreaSequenceNumber(o1) - getAreaSequenceNumber(o2);
            }
        });

        List<IGeodataIf> y = new ArrayList<IGeodataIf>();
        for (IMsoObjectIf m1 : x)
        {
            if (m1 instanceof IRouteIf)
            {
                y.add(((IRouteIf) m1).getGeodata());
            } else if (m1 instanceof ITrackIf)
            {
                y.add(((ITrackIf) m1).getGeodata());
            } else if (m1 instanceof IPOIIf)
            {
                y.add(((IPOIIf) m1).getPosition());
            }
        }
        return y.iterator();
    }

}