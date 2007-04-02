package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;

/**
 *
 */
public interface IBoatIf extends ITransportIf
{
    public enum BoatType
    {
        SEARCH_AND_RESCUE,
        MOTOR_BOAT,
        SPEED_BOAT,
        SAILBOAT,
        FREIGHTER,
        CRUISER,
        OTHER
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for ENUM attributes
    *-------------------------------------------------------------------------------------------*/

    public void setType(BoatType aType);

    public void setType(String aType);

    public BoatType getType();

    public IMsoModelIf.ModificationState getTypeState();

    public IAttributeIf.IMsoEnumIf<BoatType> getTypeAttribute();

    public String getTypeName();

    /*-------------------------------------------------------------------------------------------
    * Methods for attributes
    *-------------------------------------------------------------------------------------------*/

    public void setAverageSpeed(int anAverageSpeed);

    public int getAverageSpeed();

    public IMsoModelIf.ModificationState getAverageSpeedState();

    public IAttributeIf.IMsoIntegerIf getAverageSpeedAttribute();

    public void setCapacity(int aCapacity);

    public int getCapacity();

    public IMsoModelIf.ModificationState getCapacityState();

    public IAttributeIf.IMsoIntegerIf getCapacityAttribute();

    public void setDepth(int aDepth);

    public int getDepth();

    public IMsoModelIf.ModificationState getDepthState();

    public IAttributeIf.IMsoIntegerIf getDepthAttribute();

    public void setFreeboard(int aFreeboard);

    public int getFreeboard();

    public IMsoModelIf.ModificationState getFreeboardState();

    public IAttributeIf.IMsoIntegerIf getFreeboardAttribute();

    public void setHeight(int aHeight);

    public int getHeight();

    public IMsoModelIf.ModificationState getHeightState();

    public IAttributeIf.IMsoIntegerIf getHeightAttribute();

    public void setLength(int aLength);

    public int getLength();

    public IMsoModelIf.ModificationState getLengthState();

    public IAttributeIf.IMsoIntegerIf getLengthAttribute();

    public void setMaxSpeed(int aMaxSpeed);

    public int getMaxSpeed();

    public IMsoModelIf.ModificationState getMaxSpeedState();

    public IAttributeIf.IMsoIntegerIf getMaxSpeedAttribute();
}
