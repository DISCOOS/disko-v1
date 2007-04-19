package org.redcross.sar.util.mso;

import org.redcross.sar.util.except.IllegalMsoArgumentException;

import java.util.Calendar;

public abstract class DTG
{
    public static Calendar DTGToCal(String aDTG) throws IllegalMsoArgumentException
    {
        try
        {
            return DTGToCal(Long.parseLong(aDTG));
        }
        catch (NumberFormatException e)
        {
            throw (new IllegalMsoArgumentException("Illegal number format to DTG: " + aDTG));
        }
        catch (IllegalMsoArgumentException e)
        {
            throw (e);
        }
    }

    public static Calendar DTGToCal(long aDTG) throws IllegalMsoArgumentException
    {
        Calendar calendar = Calendar.getInstance();
        int day = (int) aDTG / 10000;
        int hour = (int) (aDTG % 10000) / 100;
        int minute = (int) aDTG % 100;

        if (minute >= 60)
        {
            throw new IllegalMsoArgumentException("Illegal DTG minute value in " + aDTG);
        }

        if (hour >= 24)
        {
            throw new IllegalMsoArgumentException("Illegal DTG hour value in " + aDTG);
        }

        if (day < 1 || !adjustToDay(calendar, day))
        {
            throw new IllegalMsoArgumentException("Illegal DTG day value in " + aDTG);
        }

        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
//        System.out.println(aDTG + "" + calendar.getTime());
        return calendar;
    }

    public static String CalToDTG(Calendar aCalendar)
    {
        return String.format("%1$td%1$tH%1$tM", aCalendar);
    }

    private static boolean adjustToDay(Calendar aCalendar, int aDay)
    {
        int today = aCalendar.get(Calendar.DAY_OF_MONTH);
        if (aDay < today - 20)
        { // Next month
            aCalendar.add(Calendar.MONTH, 1);
        } else if (today < aDay - 10)
        {
            aCalendar.add(Calendar.MONTH, -1);
        }
        return aDay <= aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static void main(String[] argv)
    {
        for (int year = 2007;year <= 2008; year ++)
        {
            for (int month = 0;month <= 2;month++)
            {
                for (int day = 1; day <= 32; day++)
                {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH, 15);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.YEAR, year);
                    System.out.print("Before: " + calendar.getTime() + ", setting day to " + day + ": ");
                    if (adjustToDay(calendar, day))
                    {
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        System.out.println(calendar.getTime());
                    } else
                    {
                        System.out.println("not legal!");
                    }
                }
            }
        }
    }
}