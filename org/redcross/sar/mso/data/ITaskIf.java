package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;

import java.util.Calendar;

public interface ITaskIf extends ITimeItemIf
{
    /*-------------------------------------------------------------------------------------------
    * Other specified methods
    *-------------------------------------------------------------------------------------------*/

    public Calendar getDueTime();

    public void setDueTime(Calendar aCalendar);

    public IMsoModelIf.ModificationState getDueTimeState();
}