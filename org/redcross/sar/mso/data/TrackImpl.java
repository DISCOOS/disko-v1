package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.MsoCastException;
import org.redcross.sar.util.mso.Track;

/**
 * Recorded route.
 */
public class TrackImpl extends AbstractMsoObject implements ITrackIf
{
    private final AttributeImpl.MsoTrack m_geodata = new AttributeImpl.MsoTrack(this, "Geodata");
    private final AttributeImpl.MsoString m_remarks = new AttributeImpl.MsoString(this, "Remarks");

    public TrackImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    public TrackImpl(IMsoObjectIf.IObjectIdIf anObjectId, Track aTrack)
    {
        super(anObjectId);
        setGeodata(aTrack);
    }

    protected void defineAttributes()
    {
        addAttribute(m_geodata);
        addAttribute(m_remarks);
    }

    protected void defineLists()
    {
    }

    protected void defineReferences()
    {
    }

    public void addObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
    }

    public void removeObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
    }

    public static TrackImpl implementationOf(ITrackIf anInterface) throws MsoCastException
    {
        try
        {
            return (TrackImpl) anInterface;
        }
        catch (ClassCastException e)
        {
            throw new MsoCastException("Illegal cast to TrackImpl");
        }
    }

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_TRACK;
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for attributes
    *-------------------------------------------------------------------------------------------*/

    public void setGeodata(Track aGeodata)
    {
        m_geodata.setValue(aGeodata);
    }

    public Track getGeodata()
    {
        return m_geodata.getTrack();
    }

    public IMsoModelIf.ModificationState getGeodataState()
    {
        return m_geodata.getState();
    }

    public IAttributeIf.IMsoTrackIf getGeodataAttribute()
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


    public void setAreaSequenceNumber(int aNumber)
    {
        //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public int getAreaSequenceNumber()
    {
        return 0;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public IMsoModelIf.ModificationState getAreaSequenceNumberState()
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public IAttributeIf.IMsoIntegerIf getAreaSequenceNumberAttribute()
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }
}