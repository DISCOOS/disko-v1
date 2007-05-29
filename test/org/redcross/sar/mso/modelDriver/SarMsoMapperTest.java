package org.redcross.sar.modelDriver;

import org.junit.Before;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import org.rescuenorway.saraccess.model.SarFact;
import org.rescuenorway.saraccess.model.SarFactLocation;
import org.rescuenorway.saraccess.model.SarFactTrack;
import org.rescuenorway.saraccess.model.SarFactArea;
import org.redcross.sar.mso.data.AttributeImpl;
import org.redcross.sar.mso.data.VehicleImpl;
import org.redcross.sar.mso.data.AbstractMsoObject;
import org.redcross.sar.util.mso.*;
import no.cmr.hrs.sar.model.*;
import no.cmr.geo.PositionOccurrence;
import junit.framework.Assert;
import com.sun.corba.se.impl.ior.ObjectIdImpl;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ArrayList;
import java.awt.geom.Point2D;

/**
 * Created by Christian Michelsen Research AS
 * User: Stian
 * Date: 09.mai.2007
 * Time: 15:05:08
 */
public class SarMsoMapperTest {


    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testBooleanAttMapping() {
        VehicleImpl testowner = new VehicleImpl(new AbstractMsoObject.ObjectId("Testvehicle"), 1);

        String id = "123456";
        String label = "TestNavn";
        String bus = "Bus";
        SarFact lFact = new FactNumerical(bus, id, 0, 0, label, false, null, true, id, 1, 0, 1, 0, 0);
        SarFact copyFact = new FactNumerical(bus, id, 0, 0, label, false, null, true, id, 1, 0, 1, 0, 0);

        AttributeImpl.MsoBoolean att1 = new AttributeImpl.MsoBoolean(testowner, label);
        att1.setValue(true);
        AttributeImpl.MsoBoolean attCopy = new AttributeImpl.MsoBoolean(testowner, label);
        attCopy.setValue(false);
        testowner.addAttribute(att1);

        testowner.addAttribute(attCopy);
        SarMsoMapper.mapSarFactToMsoAttr(att1, lFact);
        ((FactNumerical) lFact).setNumValue(-4, false);
        SarMsoMapper.mapMsoAttrToSarFact(lFact, att1, false);

        assertEquals(lFact, copyFact);
        assertEquals(att1.booleanValue(), attCopy.booleanValue());

    }

    @Test
    public void testIntegerAttMapping() {
        VehicleImpl testowner = new VehicleImpl(new AbstractMsoObject.ObjectId("Testvehicle"), 1);

        String id = "123456";
        String label = "TestNavn";
        String bus = "Bus";
        SarFact lFact = new FactNumerical(bus, id, 0, 0, label, false, null, true, id, 1, 0, 3264121, 0, 17);
        SarFact copyFact = new FactNumerical(bus, id, 0, 0, label, false, null, true, id, 1, 0, 3264121, 0, 17);

        AttributeImpl.MsoInteger att1 = new AttributeImpl.MsoInteger(testowner, label);
        att1.setValue(12);
        AttributeImpl.MsoInteger attCopy = new AttributeImpl.MsoInteger(testowner, label);
        attCopy.setValue(17);
        testowner.addAttribute(att1);

        testowner.addAttribute(attCopy);
        SarMsoMapper.mapSarFactToMsoAttr(att1, lFact);
        ((FactNumerical) lFact).setNumValue(-4, false);
        SarMsoMapper.mapMsoAttrToSarFact(lFact, att1, false);

        assertEquals(lFact, copyFact);
        assertEquals(att1.intValue(), attCopy.intValue());

    }

    @Test
    public void testStringAttMapping() {
        VehicleImpl testowner = new VehicleImpl(new AbstractMsoObject.ObjectId("Testvehicle"), 1);

        String id = "123456";
        String label = "TestNavn";
        String bus = "Bus";
        SarFact lFact = new FactString(bus, id, 0, 0, label, false, null, true, id, 1, "Test");
        SarFact copyFact = new FactString(bus, id, 0, 0, label, false, null, true, id, 1, "Test");

        AttributeImpl.MsoString att1 = new AttributeImpl.MsoString(testowner, label);
        att1.setValue("Elvis");
        AttributeImpl.MsoString attCopy = new AttributeImpl.MsoString(testowner, label);
        attCopy.setValue("Test");
        testowner.addAttribute(att1);

        testowner.addAttribute(attCopy);
        SarMsoMapper.mapSarFactToMsoAttr(att1, lFact);
        ((FactString) lFact).setStringValue("Ugyldig", false);
        SarMsoMapper.mapMsoAttrToSarFact(lFact, att1, false);

        assertEquals(lFact, copyFact);
        assertEquals(att1.getString(), attCopy.getString());

    }

    @Test
    public void testCalendarAttMapping() {
        VehicleImpl testowner = new VehicleImpl(new AbstractMsoObject.ObjectId("Testvehicle"), 1);

        String id = "123456";
        String label = "TestNavn";
        String bus = "Bus";
        Calendar lCal = Calendar.getInstance();
        SarFact lFact = new FactDate(bus, id, 0, 0, label, false, null, true, id, 1, lCal.getTimeInMillis());
        SarFact copyFact = new FactDate(bus, id, 0, 0, label, false, null, true, id, 1, lCal.getTimeInMillis());

        Calendar old = new GregorianCalendar(1999, 12, 0);
        AttributeImpl.MsoCalendar att1 = new AttributeImpl.MsoCalendar(testowner, label);
        att1.setValue(old);
        AttributeImpl.MsoCalendar attCopy = new AttributeImpl.MsoCalendar(testowner, label);
        attCopy.setValue(lCal);
        testowner.addAttribute(att1);
        testowner.addAttribute(attCopy);

        SarMsoMapper.mapSarFactToMsoAttr(att1, lFact);
        ((FactDate) lFact).setDate(old, false);
        SarMsoMapper.mapMsoAttrToSarFact(lFact, att1, false);

        assertEquals(lFact, copyFact);
        assertEquals(att1.getCalendar(), attCopy.getCalendar());

    }

    @Test
    public void testPositionAttMapping() {
        VehicleImpl testowner = new VehicleImpl(new AbstractMsoObject.ObjectId("Testvehicle"), 1);

        String id = "123456";
        String label = "TestNavn";
        String bus = "Bus";
        Calendar lCal = Calendar.getInstance();
        SarFact lFact = new FactLocation(bus, id, 0, 0, label, false, null, true, id, 1, 60, 10);
        SarFact copyFact = new FactLocation(bus, id, 0, 0, label, false, null, true, id, 1, 60, 10);

        Point2D.Double pos1 = new Point2D.Double(10, 60);
        Point2D.Double pos2 = new Point2D.Double(20, 30);
        AttributeImpl.MsoPosition att1 = new AttributeImpl.MsoPosition(testowner, label);
        att1.setValue(new Position("12", pos2));
        AttributeImpl.MsoPosition attCopy = new AttributeImpl.MsoPosition(testowner, label);
        attCopy.setValue(new Position("12", pos1));
        testowner.addAttribute(att1);
        testowner.addAttribute(attCopy);

        SarMsoMapper.mapSarFactToMsoAttr(att1, lFact);
        ((SarFactLocation) lFact).updateLocation((float) pos2.getY(), (float) pos2.getX(), null, false);
        SarMsoMapper.mapMsoAttrToSarFact(lFact, att1, false);

        assertEquals(lFact, copyFact);
        assertEquals(attCopy.getPosition().getPosition(), att1.getPosition().getPosition());

    }

    @Test
    public void testTimePositionAttMapping() {
        VehicleImpl testowner = new VehicleImpl(new AbstractMsoObject.ObjectId("Testvehicle"), 1);

        String id = "123456";
        String label = "TestNavn";
        String bus = "Bus";
        Calendar lCal = Calendar.getInstance();
        Calendar old = new GregorianCalendar(1999, 12, 0);
        SarFact lFact = new FactLocation(bus, id, 0, 0, label, false, null, true, id, 1, 1, 60, 10, lCal);
        SarFact copyFact = new FactLocation(bus, id, 0, 0, label, false, null, true, id, 1, 1, 60, 10, lCal);

        Point2D.Double pos1 = new Point2D.Double(10, 60);
        TimePos tp1 = new TimePos(pos1, lCal);
        Point2D.Double pos2 = new Point2D.Double(20, 30);
        TimePos tp2 = new TimePos(pos2, old);
        AttributeImpl.MsoTimePos att1 = new AttributeImpl.MsoTimePos(testowner, label);
        att1.setValue(tp2);
        AttributeImpl.MsoTimePos attCopy = new AttributeImpl.MsoTimePos(testowner, label);
        attCopy.setValue(tp1);
        testowner.addAttribute(att1);
        testowner.addAttribute(attCopy);

        SarMsoMapper.mapSarFactToMsoAttr(att1, lFact);
        ((SarFactLocation) lFact).updateLocation((float) pos2.getY(), (float) pos2.getX(), null, false);
        SarMsoMapper.mapMsoAttrToSarFact(lFact, att1, false);

        assertEquals(lFact, copyFact);
        assertEquals(attCopy.getTimePos().getPosition(), att1.getTimePos().getPosition());
        assertEquals(attCopy.getTimePos().getDTG(), att1.getTimePos().getDTG());

    }

    @Test
    public void testRouteAttMapping() {
        VehicleImpl testowner = new VehicleImpl(new AbstractMsoObject.ObjectId("Testvehicle"), 1);

        String id = "123456";
        String label = "TestNavn";
        String bus = "Bus";
        Calendar lCal = Calendar.getInstance();
        Calendar old = new GregorianCalendar(1999, 12, 0);

        List list1 = getSaraPosList();
        List list2 = new ArrayList(list1);
        SarFact lFact = new FactTrack(bus, id, 0, 0, label, false, null, true, id, 1, list1, 1, "TestLayout", null, null);

        SarFact copyFact = new FactTrack(bus, id, 0, 0, label, false, null, true, id, 1, list2, 1, "TestLayout", null, null);
        Route tp1 = new Route("r2");
        tp1.add(10, 60);
        tp1.add(20, 60);
        tp1.add(20, 70);
        tp1.add(10, 70);
        tp1.add(10, 60);
        tp1.setLayout("TestLayout");
        Route tp2 = new Route("r2");
        tp2.add(20, 60);
        tp2.add(20, 60);
        tp2.add(20, 70);
        tp2.add(80, 70);
        tp2.add(10, 60);
        tp2.setLayout("dsføjngdfkgndfælgædfæsdgmaædfkg");
        AttributeImpl.MsoRoute att1 = new AttributeImpl.MsoRoute(testowner, label);
        att1.setValue(tp2);
        AttributeImpl.MsoRoute attCopy = new AttributeImpl.MsoRoute(testowner, label);
        attCopy.setValue(tp1);
        testowner.addAttribute(att1);
        testowner.addAttribute(attCopy);

        SarMsoMapper.mapSarFactToMsoAttr(att1, lFact);
        ((SarFactTrack) lFact).setTrack(getSaraTimePosList(), false);
        SarMsoMapper.mapMsoAttrToSarFact(lFact, att1, false);

        assertEquals(lFact, copyFact);
        assertEquals(attCopy.getRoute().getLayout(), att1.getRoute().getLayout());
        assertEquals(attCopy.getRoute(), att1.getRoute());

    }

    @Test
    public void testTrackAttMapping() {
        VehicleImpl testowner = new VehicleImpl(new AbstractMsoObject.ObjectId("Testvehicle"), 1);

        String id = "123456";
        String label = "TestNavn";
        String bus = "Bus";
        Calendar lCal = Calendar.getInstance();
        Calendar old = new GregorianCalendar(1999, 12, 0);

        List list1 = getSaraTimePosList(old);
        List list2 = new ArrayList(list1);
        SarFact lFact = new FactTrack(bus, id, 0, 0, label, false, null, true, id, 1, list1, 1, "TestLayout", null, null);

        SarFact copyFact = new FactTrack(bus, id, 0, 0, label, false, null, true, id, 1, list2, 1, "TestLayout", null, null);
        Track tp1 = new Track("r2");
        tp1.add(10, 60, old);
        tp1.add(20, 60, old);
        tp1.add(20, 70, old);
        tp1.add(10, 70, old);
        tp1.add(10, 60, old);
        tp1.setLayout("TestLayout");
        Track tp2 = new Track("r2");
        tp2.add(20, 60, Calendar.getInstance());
        tp2.add(20, 60, Calendar.getInstance());
        tp2.add(20, 70, Calendar.getInstance());
        tp2.add(80, 70, Calendar.getInstance());
        tp2.add(10, 60, Calendar.getInstance());
        tp2.setLayout("dsføjngdfkgndfælgædfæsdgmaædfkg");
        AttributeImpl.MsoTrack att1 = new AttributeImpl.MsoTrack(testowner, label);
        att1.setValue(tp2);
        AttributeImpl.MsoTrack attCopy = new AttributeImpl.MsoTrack(testowner, label);
        attCopy.setValue(tp1);
        testowner.addAttribute(att1);
        testowner.addAttribute(attCopy);

        SarMsoMapper.mapSarFactToMsoAttr(att1, lFact);
        ((SarFactTrack) lFact).setTrack(getSaraTimePosList(), false);
        SarMsoMapper.mapMsoAttrToSarFact(lFact, att1, false);

        assertEquals(lFact, copyFact);
        assertEquals(attCopy.getTrack().getLayout(), att1.getTrack().getLayout());
        assertEquals(attCopy.getTrack(), att1.getTrack());

    }

    @Test
    public void testPolygonAttMapping() {
        VehicleImpl testowner = new VehicleImpl(new AbstractMsoObject.ObjectId("Testvehicle"), 1);

        String id = "123456";
        String label = "TestNavn";
        String bus = "Bus";
        Calendar lCal = Calendar.getInstance();
        Calendar old = new GregorianCalendar(1999, 12, 0);

        List list1 = getSaraTimePosList(old);
        List list2 = new ArrayList(list1);
        SarFact lFact = new FactArea(bus, id, 0, 0, label, false, null, true, id, 1, list1, "TestPoly", 1, "TestLayout");

        SarFact copyFact = new FactArea(bus, id, 0, 0, label, false, null, true, id, 1, list2, "TestPoly", 1, "TestLayout");
        Polygon tp1 = new Polygon("r2", "TestPoly");
        tp1.add(10, 60);
        tp1.add(20, 60);
        tp1.add(20, 70);
        tp1.add(10, 70);
        tp1.add(10, 60);
        tp1.setLayout("TestLayout");
        Polygon tp2 = new Polygon("r2");
        tp2.add(20, 60);
        tp2.add(20, 60);
        tp2.add(20, 70);
        tp2.add(80, 70);
        tp2.add(10, 60);
        tp2.setLayout("dsføjngdfkgndfælgædfæsdgmaædfkg");
        AttributeImpl.MsoPolygon att1 = new AttributeImpl.MsoPolygon(testowner, label);
        att1.setValue(tp2);
        AttributeImpl.MsoPolygon attCopy = new AttributeImpl.MsoPolygon(testowner, label);
        attCopy.setValue(tp1);
        testowner.addAttribute(att1);
        testowner.addAttribute(attCopy);

        SarMsoMapper.mapSarFactToMsoAttr(att1, lFact);
        ((SarFactArea) lFact).setArea(getSaraTimePosList(), "testnavn", false);
        SarMsoMapper.mapMsoAttrToSarFact(lFact, att1, false);


        assertEquals(copyFact, lFact);
        assertEquals(attCopy.getPolygon(), att1.getPolygon());

    }


    public static List<org.rescuenorway.saraccess.model.TimePos> getSaraPosList() {
        List<org.rescuenorway.saraccess.model.TimePos> lList = new ArrayList();
        Calendar lCal = null;
        lList.add(new PositionOccurrence(60, 10, ""));
        lList.add(new PositionOccurrence(60, 20, ""));
        lList.add(new PositionOccurrence(70, 20, ""));
        lList.add(new PositionOccurrence(70, 10, ""));
        lList.add(new PositionOccurrence(60, 10, ""));
        return lList;
    }


    public static List<org.rescuenorway.saraccess.model.TimePos> getSaraTimePosList() {
        return getSaraTimePosList(Calendar.getInstance());
    }

    public static List<org.rescuenorway.saraccess.model.TimePos> getSaraTimePosList(Calendar lCal) {
        List<org.rescuenorway.saraccess.model.TimePos> lList = new ArrayList();

        lList.add(new PositionOccurrence(60, 10, lCal.getTimeInMillis(), ""));
        lList.add(new PositionOccurrence(60, 20, lCal.getTimeInMillis(), ""));
        lList.add(new PositionOccurrence(70, 20, lCal.getTimeInMillis(), ""));
        lList.add(new PositionOccurrence(70, 10, lCal.getTimeInMillis(), ""));
        lList.add(new PositionOccurrence(60, 10, lCal.getTimeInMillis(), ""));
        return lList;
    }

}
