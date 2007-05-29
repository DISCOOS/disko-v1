package org.redcross.sar.modelDriver;

import org.rescuenorway.saraccess.model.*;
import org.rescuenorway.saraccess.model.TimePos;
import org.redcross.sar.mso.data.AttributeImpl;
import org.redcross.sar.mso.data.IAttributeIf;
import org.redcross.sar.util.mso.*;
import no.cmr.hrs.sar.model.FactNumerical;
import no.cmr.hrs.sar.model.FactLocation;
import no.cmr.geo.PositionOccurrence;
import no.cmr.tools.Log;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Helper class userd to map between MSO object attributes and Sara facts
 * User: Stian
 * Date: 08.mai.2007
 * Time: 13:54:03
 * To change this template use File | Settings | File Templates.
 */
public class SarMsoMapper {

    public static void mapMsoAttrToSarFact(SarFact sarFact, IAttributeIf msoAttr, boolean distribute) {
        if (msoAttr instanceof AttributeImpl.MsoBoolean) {
            AttributeImpl.MsoBoolean lAttr = (AttributeImpl.MsoBoolean) msoAttr;
            ((SarFactNumerical) sarFact).setNumValue(lAttr.booleanValue() ? 1 : 0, distribute);
        }
        if (msoAttr instanceof AttributeImpl.MsoInteger) {
            AttributeImpl.MsoInteger lAttr = (AttributeImpl.MsoInteger) msoAttr;
            ((SarFactNumerical) sarFact).setNumValue(lAttr.intValue(), distribute);
        }
        if (msoAttr instanceof AttributeImpl.MsoDouble) {
            AttributeImpl.MsoDouble lAttr = (AttributeImpl.MsoDouble) msoAttr;
            ((SarFactNumerical) sarFact).setNumValue(lAttr.doubleValue(), distribute);
        }
        if (msoAttr instanceof AttributeImpl.MsoString) {
            AttributeImpl.MsoString lAttr = (AttributeImpl.MsoString) msoAttr;
            ((SarFactString) sarFact).setStringValue(lAttr.getString(), distribute);
        }
        if (msoAttr instanceof AttributeImpl.MsoCalendar) {
            AttributeImpl.MsoCalendar lAttr = (AttributeImpl.MsoCalendar) msoAttr;
            ((SarFactDate) sarFact).setDate(lAttr.getCalendar(), distribute);
        }
        if (msoAttr instanceof AttributeImpl.MsoPosition) {

            AttributeImpl.MsoPosition lAttr = (AttributeImpl.MsoPosition) msoAttr;

            ((SarFactLocation) sarFact).updateLocation((float) lAttr.getPosition().getPosition().getY(),
                    (float) lAttr.getPosition().getPosition().getX(), null, null, null, "", distribute);

        }
        if (msoAttr instanceof AttributeImpl.MsoTimePos) {
            AttributeImpl.MsoTimePos lAttr = (AttributeImpl.MsoTimePos) msoAttr;
            ((SarFactLocation) sarFact).updateLocation((float) lAttr.getTimePos().getPosition().getY(),
                    (float) lAttr.getTimePos().getPosition().getX(), lAttr.getTimePos().getTime(), null, null, "", distribute);
        }
        if (msoAttr instanceof AttributeImpl.MsoPolygon) {
            AttributeImpl.MsoPolygon lAttr = (AttributeImpl.MsoPolygon) msoAttr;
            List<TimePos> posList = mapGeoPosListToSaraTimePos(lAttr.getPolygon().getVertices());
            ((SarFactArea) sarFact).setArea(posList, lAttr.getPolygon().getName(), lAttr.getPolygon().getLayout(), distribute);//TODO gjennoms�kt eller ei??
        }
        if (msoAttr instanceof AttributeImpl.MsoRoute) {
            AttributeImpl.MsoRoute lAttr = (AttributeImpl.MsoRoute) msoAttr;
            List<TimePos> posList = mapGeoPosListToSaraTimePos(lAttr.getRoute().getPositions());
            ((SarFactTrack) sarFact).setTrack(posList, lAttr.getRoute().getName(), lAttr.getRoute().getLayout(), distribute);
        }
        if (msoAttr instanceof AttributeImpl.MsoTrack) {
            AttributeImpl.MsoTrack lAttr = (AttributeImpl.MsoTrack) msoAttr;
            List<TimePos> posList = mapTimePosListToSaraTimePos(lAttr.getTrack().getTrackTimePos());
            ((SarFactTrack) sarFact).setTrack(posList, lAttr.getTrack().getName(), lAttr.getTrack().getLayout(), distribute);
        }
        if (msoAttr instanceof AttributeImpl.MsoGeoCollection) {
            throw new ClassCastException("MsoGeoCollection not supported by mapper");
        }
        if (msoAttr instanceof AttributeImpl.MsoEnum) {
            AttributeImpl.MsoEnum lAttr = (AttributeImpl.MsoEnum) msoAttr;
            ((SarFactString) sarFact).setStringValue(lAttr.getValueName(), distribute);
        }
    }

    public static void mapSarFactToMsoAttr(Object msoAttr, SarFact sarFact) {
        if (msoAttr instanceof AttributeImpl.MsoBoolean) {
            AttributeImpl.MsoBoolean lAttr = (AttributeImpl.MsoBoolean) msoAttr;
            int factVal = (int) ((SarFactNumerical) sarFact).getIntegerValue();
            lAttr.setValue(factVal == 1 ? true : false);
        } else if (msoAttr instanceof AttributeImpl.MsoInteger) {
            AttributeImpl.MsoInteger lAttr = (AttributeImpl.MsoInteger) msoAttr;
            int factVal = (int) ((SarFactNumerical) sarFact).getIntegerValue();
            lAttr.setValue(factVal);

        } else if (msoAttr instanceof AttributeImpl.MsoDouble) {
            AttributeImpl.MsoDouble lAttr = (AttributeImpl.MsoDouble) msoAttr;
            double factVal = ((SarFactNumerical) sarFact).getDoubleValue();
            lAttr.setValue(factVal);

        } else if (msoAttr instanceof AttributeImpl.MsoString) {
            AttributeImpl.MsoString lAttr = (AttributeImpl.MsoString) msoAttr;
            lAttr.setValue(((SarFactString) sarFact).getStringValue());
        } else if (msoAttr instanceof AttributeImpl.MsoCalendar) {
            AttributeImpl.MsoCalendar lAttr = (AttributeImpl.MsoCalendar) msoAttr;
            lAttr.setValue(((SarFactDate) sarFact).getDate());
        } else if (msoAttr instanceof AttributeImpl.MsoPosition) {
            AttributeImpl.MsoPosition lAttr = (AttributeImpl.MsoPosition) msoAttr;
            SarFactLocation lFact = (SarFactLocation) sarFact;    //2Sjekk med vinjar
            lAttr.setValue(new Position(lFact.getID(), lFact.getLongValue(), lFact.getLatValue()));
        } else if (msoAttr instanceof AttributeImpl.MsoTimePos) {
            AttributeImpl.MsoTimePos lAttr = (AttributeImpl.MsoTimePos) msoAttr;
            SarFactLocation lFact = (SarFactLocation) sarFact;
            lAttr.setValue(new org.redcross.sar.util.mso.TimePos(lFact.getLongValue(), lFact.getLatValue(), lFact.getTimeAtPos()));
        } else if (msoAttr instanceof AttributeImpl.MsoPolygon) {
            AttributeImpl.MsoPolygon lAttr = (AttributeImpl.MsoPolygon) msoAttr;

            SarFactArea aFact = (SarFactArea) sarFact;
            Polygon lPoly = new Polygon(lAttr.getPolygon().getId(), aFact.getName());
            lPoly.setLayout(aFact.getStyle());
            for (TimePos tp : aFact.getArea()) {
                lPoly.add(tp.getLongitude(), tp.getLatitude());
            }
            lAttr.setValue(lPoly);
        } else if (msoAttr instanceof AttributeImpl.MsoRoute) {
            AttributeImpl.MsoRoute lAttr = (AttributeImpl.MsoRoute) msoAttr;
            SarFactTrack aFact = (SarFactTrack) sarFact;

            Route lPoly = new Route(lAttr.getRoute().getId(), aFact.getName());//TODO avsjekk
            lPoly.setLayout(aFact.getStyle());
            for (TimePos tp : aFact.getTrack()) {
                lPoly.add(tp.getLongitude(), tp.getLatitude());
            }
            lAttr.setValue(lPoly);


        } else if (msoAttr instanceof AttributeImpl.MsoTrack) {

            AttributeImpl.MsoTrack lAttr = (AttributeImpl.MsoTrack) msoAttr;
            SarFactTrack aFact = (SarFactTrack) sarFact;
            Track lPoly = new Track(lAttr.getTrack().getId(), aFact.getName());

            lPoly.setLayout(aFact.getStyle());
            for (TimePos tp : aFact.getTrack()) {
                lPoly.add(tp.getLongitude(), tp.getLatitude(), tp.getTimeInPos());
            }
            lAttr.setValue(lPoly);

        } else if (msoAttr instanceof AttributeImpl.MsoGeoCollection) {
            throw new ClassCastException("MsoGeoCollection not supported by mapper");
        } else if (msoAttr instanceof AttributeImpl.MsoEnum) {
            AttributeImpl.MsoEnum lAttr = (AttributeImpl.MsoEnum) msoAttr;
            lAttr.setValue(((SarFactString) sarFact).getStringValue());
        } else {
            Log.warning("Unknown mapping type" + msoAttr.getClass().getName());
        }

    }


    private static List<TimePos> mapGeoPosListToSaraTimePos(Collection<GeoPos> route) {
        List<TimePos> list = new ArrayList();
        for (GeoPos geo : route) {
            TimePos lPos = new PositionOccurrence((float) geo.getPosition().getY(), (float) geo.getPosition().getX(), "");
            list.add(lPos);
        }
        return list;
    }

    private static List<TimePos> mapTimePosListToSaraTimePos(Collection<org.redcross.sar.util.mso.TimePos> track) {
        List<TimePos> list = new ArrayList();
        for (org.redcross.sar.util.mso.TimePos geo : track) {
            TimePos lPos = new PositionOccurrence((float) geo.getPosition().getY(), (float) geo.getPosition().getX(), geo.getTime().getTimeInMillis(), "");
            list.add(lPos);
        }
        return list;
    }

}
