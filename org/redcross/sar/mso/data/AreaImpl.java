package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.MsoModelImpl;
import org.redcross.sar.util.except.DuplicateIdException;
import org.redcross.sar.util.except.MsoCastException;
import org.redcross.sar.util.except.IllegalOperationException;
import org.redcross.sar.util.mso.*;

import java.util.Collection;
import java.util.List;

/**
 * Strip of field to search
 */
public class AreaImpl extends AbstractMsoObject implements IAreaIf
{
    private final AttributeImpl.MsoGeoCollection m_geodata = new AttributeImpl.MsoGeoCollection(this, "Geodata");
    private final AttributeImpl.MsoString m_remarks = new AttributeImpl.MsoString(this, "Remarks");
    private final POIListImpl m_areaPOIs = new POIListImpl(this, "AreaPOIs", false);

    public AreaImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    protected void defineAttributes()
    {
        addAttribute(m_geodata);
        addAttribute(m_remarks);
    }

    protected void defineLists()
    {
        addList(m_areaPOIs);
    }

    protected void defineReferences()
    {
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


    public void setGeodata(GeoCollection aGeodata)
    {
        m_geodata.setValue(aGeodata);
    }

    public GeoCollection getGeodata()
    {
        return m_geodata.getGeoCollection();
    }

    public IMsoModelIf.ModificationState getGeodataState()
    {
        return m_geodata.getState();
    }

    public IAttributeIf.IMsoGeoCollectionIf getGeodataAttribute()
    {
        return m_geodata;
    }

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

    public void addAreaPOI(IPOIIf anIPOIIf) throws DuplicateIdException
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

    /*-------------------------------------------------------------------------------------------
    * Other specified methods
    *-------------------------------------------------------------------------------------------*/

    public IAssignmentIf getOwningAssignment()
    {
        List<IAssignmentIf> retVal = MsoModelImpl.getInstance().getMsoManager().getCmdPost().getAssignmentList().selectItems(
                new SelfSelector<IAreaIf, IAssignmentIf>(this)
                {
                    public boolean select(IAssignmentIf anObject)
                    {
                        return (anObject.getPlannedArea() == m_object || anObject.getReportedArea() == m_object); // todo Check status as well?
                    }
                }, null);
        return (retVal.size() == 0) ? null : retVal.get(0);
    }

    public void verifyAssignable(IAssignmentIf anAssignment) throws IllegalOperationException
        {
        IAssignmentIf asg = getOwningAssignment();
        if (asg != null && asg != anAssignment)
        {
            throw new IllegalOperationException("Area " + this + " is already assigned to an assigment.");
        }
    }


    public IMsoReferenceIf<IHypothesisIf> getHypotesisAttribute()
    {
        return getOwningAssignment().getAssignmentHypothesisAttribute();
    }

    public void setHypothesis(IHypothesisIf anHypothesis)
    {
        getOwningAssignment().setAssignmentHypothesis(anHypothesis);
    }

}