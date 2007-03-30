package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

public interface ITrackListIf extends IMsoListIf<ITrackIf>
{
    public ITrackIf createTrack();

    public ITrackIf createTrack(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;
}