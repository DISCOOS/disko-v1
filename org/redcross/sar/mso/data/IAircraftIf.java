package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;

/**
 *
 */
public interface IAircraftIf extends ITransportIf
{
    public enum AircraftType
    {
        LIGHT_AIRCRAFT,
        AIRPLANE,
        AIRLINER,
        HELICOPTER,
        MICROPLANE,
        HANGGLIDER,
        AIRBALLOON,
        OTHER
    }

    public void setAverageSpeed(int anAverageSpeed);

    public int getAverageSpeed();

    public IMsoModelIf.ModificationState getAverageSpeedState();

    public IAttributeIf.IMsoIntegerIf getAverageSpeedAttribute();

    public void setFlightTime(int aFlightTime);

    public int getFlightTime();

    public IMsoModelIf.ModificationState getFlightTimeState();

    public IAttributeIf.IMsoIntegerIf getFlightTimeAttribute();

    public void setInfrared(boolean hasInfrared);

    public boolean hasInfrared();

    public IMsoModelIf.ModificationState getInfraredState();

    public IAttributeIf.IMsoBooleanIf getInfraredAttribute();

    public void setMaxSpeed(int aMaxSpeed);

    public int getMaxSpeed();

    public IMsoModelIf.ModificationState getMaxSpeedState();

    public IAttributeIf.IMsoIntegerIf getMaxSpeedAttribute();

    public void setNightvision(boolean hasNightvision);

    public boolean hasNightvision();

    public IMsoModelIf.ModificationState getNightvisionState();

    public IAttributeIf.IMsoBooleanIf getNightvisionAttribute();

    public void setPhoto(boolean hasPhoto);

    public boolean hasPhoto();

    public IMsoModelIf.ModificationState getPhotoState();

    public IAttributeIf.IMsoBooleanIf getPhotoAttribute();

    public void setRange(int aRange);

    public int getRange();

    public IMsoModelIf.ModificationState getRangeState();

    public IAttributeIf.IMsoIntegerIf getRangeAttribute();

    public void setSeats(int aSeats);

    public int getSeats();

    public IMsoModelIf.ModificationState getSeatsState();

    public IAttributeIf.IMsoIntegerIf getSeatsAttribute();

    public void setVideo(boolean hasVideo);

    public boolean hasVideo();

    public IMsoModelIf.ModificationState getVideoState();

    public IAttributeIf.IMsoBooleanIf getVideoAttribute();

    public void setVisibility(int aVisibility);

    public int getVisibility();

    public IMsoModelIf.ModificationState getVisibilityState();

    public IAttributeIf.IMsoIntegerIf getVisibilityAttribute();

    public String getTypeName();

    public void setType(int aType);

    public int getType();

    public IMsoModelIf.ModificationState getTypeState();

    public IAttributeIf.IMsoIntegerIf getTypeAttribute();
}
