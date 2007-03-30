package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.MsoCastException;

public class IntelligenceImpl extends AbstractMsoObject implements IIntelligenceIf
{
    private final MsoReferenceImpl<IPOIIf> m_poi = new MsoReferenceImpl<IPOIIf>(this, "IntelligencePoi",true);
    private final MsoReferenceImpl<ISubjectIf> m_subject = new MsoReferenceImpl<ISubjectIf>(this, "IntelligenceSubject",true);
    private final MsoReferenceImpl<IRouteIf> m_route = new MsoReferenceImpl<IRouteIf>(this, "IntelligenceRoute",true);
    private final MsoReferenceImpl<ITrackIf> m_track = new MsoReferenceImpl<ITrackIf>(this, "IntelligenceTrack",true);

    public IntelligenceImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    public IntelligenceImpl(IMsoObjectIf.IObjectIdIf anObjectId,IPOIIf aPoi, ISubjectIf aSubject, IRouteIf aRoute, ITrackIf aTrack) throws MsoCastException
    {
        super(anObjectId);
        setPOI(aPoi);
        setSubject(aSubject);
        setRoute(aRoute);
        setTrack(aTrack);
    }

    protected void defineAttributes()
    {
    }

    protected void defineLists()
    {
    }

    protected void defineReferences()
   {
       addReference(m_poi);
       addReference(m_subject);
       addReference(m_route);
       addReference(m_track);
    }

    public static IntelligenceImpl implementationOf(IIntelligenceIf anInterface) throws MsoCastException
    {
        try
        {
            return (IntelligenceImpl) anInterface;
        }
        catch (ClassCastException e)
        {
            throw new MsoCastException("Illegal cast to IntelligenceImpl");
        }
    }

    public void setPOI(IPOIIf aPoi) throws MsoCastException
    {
        m_poi.setReference(aPoi);
    }

    public IMsoReferenceIf<IPOIIf> getPOIReference()
    {
        return m_poi;
    }

    public IMsoModelIf.ModificationState getPOIState()
    {
        return m_poi.getState();
    }

    public IPOIIf getPOI()
    {
        return m_poi.getReference();
    }

    public void setSubject(ISubjectIf aSubject) throws MsoCastException
    {
        m_subject.setReference(aSubject);
    }

    public IMsoReferenceIf<ISubjectIf> getSubjectReference()
    {
        return m_subject;
    }

    public IMsoModelIf.ModificationState getSubjectState()
    {
        return m_subject.getState();
    }

    public ISubjectIf getSubject()
    {
        return m_subject.getReference();
    }

    public void setRoute(IRouteIf aRoute) throws MsoCastException
    {
        m_route.setReference(aRoute);
    }

    public IMsoReferenceIf<IRouteIf> getRouteReference()
    {
        return m_route;
    }

    public IMsoModelIf.ModificationState getRouteState()
    {
        return m_subject.getState();
    }

    public IRouteIf getRoute()
    {
        return m_route.getReference();
    }

    public void setTrack(ITrackIf aTrack) throws MsoCastException
    {
        m_track.setReference(aTrack);
    }

    public IMsoReferenceIf<ITrackIf> getTrackReference()
    {
        return m_track;
    }

    public IMsoModelIf.ModificationState getTrackState()
    {
        return m_track.getState();
    }

    public ITrackIf getTrack()
    {
        return m_track.getReference();
    }

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_INTELLIGENCE;
    }


}