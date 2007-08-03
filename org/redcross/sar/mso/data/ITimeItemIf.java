package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;

import java.util.Calendar;

public interface ITimeItemIf extends Comparable<ITimeItemIf>, IMsoObjectIf
{
    public Calendar getCalendar();

    public void setCalendar(Calendar aDTG);

    public IMsoModelIf.ModificationState getCalendarState();

    public IAttributeIf.IMsoCalendarIf getCalendarAttribute();

    public String toString();

    public void setVisible(boolean aVisible);

    public boolean isVisible();

    public IMsoModelIf.ModificationState getVisibleState();

    public IAttributeIf.IMsoBooleanIf getVisibleAttribute();
}