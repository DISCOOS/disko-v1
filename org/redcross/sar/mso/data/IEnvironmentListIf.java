package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;
import org.redcross.sar.util.except.IllegalMsoArgumentException;

import java.util.Calendar;

public interface IEnvironmentListIf extends IMsoListIf<IEnvironmentIf>
{
    public IEnvironmentIf createEnvironment(Calendar aCalendar, String aText);

    public IEnvironmentIf createEnvironment(String aDTG, String aText) throws IllegalMsoArgumentException;

    public IEnvironmentIf createEnvironment(long aDTG, String aText) throws IllegalMsoArgumentException;

    public IEnvironmentIf createEnvironment(IMsoObjectIf.IObjectIdIf anObjectId);

    public IEnvironmentIf createEnvironment(IMsoObjectIf.IObjectIdIf anObjectId, String aDTG, String aText) throws IllegalMsoArgumentException;

    public IEnvironmentIf createEnvironment(IMsoObjectIf.IObjectIdIf anObjectId, long aDTG, String aText) throws IllegalMsoArgumentException;
}