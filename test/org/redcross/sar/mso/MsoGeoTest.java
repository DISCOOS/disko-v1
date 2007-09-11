package org.redcross.sar.mso;

import static org.junit.Assert.*;
import org.junit.Test;
import org.redcross.sar.util.mso.*;

import java.util.Calendar;
/**
 * Created by IntelliJ IDEA.
 * User: vinjar
 * Date: 05.sep.2007
 * To change this template use File | Settings | File Templates.
 */

/**
 *
 */
public class MsoGeoTest
{
    @Test
    public void testTrack()
    {
        Track t = new Track("t1","Track1");
        try
        {
            Track tc = (Track)t.clone();
            assertEquals(t,tc);
            assertNotSame(t,tc);
            TimePos tp1 = new TimePos(1,1, Calendar.getInstance());
            TimePos tp2 = new TimePos(2,2, Calendar.getInstance());
            t.add(tp1);
            tc.add(tp1);
            assertEquals(t,tc);
            assertEquals(t.hashCode(),tc.hashCode());
            t.add(tp2);
            assertFalse(t.equals(tc));
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void testRoute()
    {
        Route r = new Route("t1","Route1");
        try
        {
            Route rc = (Route)r.clone();
            assertEquals(r,rc);
            assertNotSame(r,rc);
            GeoPos tp1 = new GeoPos(1,1);
            GeoPos tp2 = new GeoPos(2,2);
            r.add(tp1);
            rc.add(tp1);
            assertEquals(r,rc);
            assertEquals(r.hashCode(),rc.hashCode());
            r.add(tp2);
            assertFalse(r.equals(rc));
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void testPolygon()
    {
        Polygon p = new Polygon("p1","Polygon1");
        try
        {
            Polygon pc = (Polygon)p.clone();
            Polygon pa = (Polygon)p.clone();
            assertEquals(p,pc);
            assertNotSame(p,pc);
            GeoPos tp1 = new GeoPos(1,1);
            GeoPos tp2 = new GeoPos(2,2);
            GeoPos tp1a = new GeoPos(1,1);
            p.add(tp1);
            pa.add(tp1a);
            pc.add(tp1);
            assertEquals(p,pc);
            assertEquals(p,pa);
            assertEquals(p.hashCode(),pc.hashCode());
            p.add(tp2);
            assertFalse(p.equals(pc));
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
