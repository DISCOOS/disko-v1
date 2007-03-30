package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.MsoCastException;

public interface IIntelligenceIf extends IMsoObjectIf
{

    public void setPOI(IPOIIf aPoi) throws MsoCastException;
    public IMsoReferenceIf<IPOIIf> getPOIReference();
    public IMsoModelIf.ModificationState getPOIState();
    public IPOIIf getPOI();

    public void setSubject(ISubjectIf aSubject) throws MsoCastException;
    public IMsoReferenceIf<ISubjectIf> getSubjectReference();
    public IMsoModelIf.ModificationState getSubjectState();
    public ISubjectIf getSubject();

    public void setRoute(IRouteIf aRoute) throws MsoCastException;
    public IMsoReferenceIf<IRouteIf>getRouteReference();
    public IMsoModelIf.ModificationState getRouteState();
    public IRouteIf getRoute();

    public void setTrack(ITrackIf aTrack) throws MsoCastException;
    public IMsoReferenceIf<ITrackIf>getTrackReference();
    public IMsoModelIf.ModificationState getTrackState();
    public ITrackIf getTrack();
}
