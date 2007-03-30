package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.util.except.MsoCastException;
import org.redcross.sar.util.mso.Track;

public class TrackImpl extends AbstractMsoObject implements ITrackIf
{
    private final AttributeImpl.MsoTrack m_track = new AttributeImpl.MsoTrack(this, "track");

    public TrackImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    public TrackImpl(IMsoObjectIf.IObjectIdIf anObjectId, Track aTrack)
    {
        super(anObjectId);
        m_track.setValue(aTrack);
    }

    protected void defineAttributes()
    {
        addAttribute(m_track);
    }

    protected void defineLists()
    {
    }

    protected void defineReferences()
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

}