package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.IllegalMsoArgumentException;
import org.redcross.sar.mso.IMsoModelIf;

import java.util.Calendar;

public interface ITimeItemIf extends Comparable<ITimeItemIf>, IMsoObjectIf
{
    public String getDTG();

    public Calendar getCalendar();

    public void setCalendar(Calendar aDTG);

    public IMsoModelIf.ModificationState getCalendarState();

    public IAttributeIf.IMsoCalendarIf getCalendarAttribute();

    public void setDTG(Long aDTG) throws IllegalMsoArgumentException;

    public void setDTG(String aDTG) throws IllegalMsoArgumentException;

    public String toString();

    public void setVisible(boolean aVisible);

    public boolean isVisible();

    public IMsoModelIf.ModificationState getVisibleState();

    public IAttributeIf.IMsoBooleanIf getVisibleAttribute();
}