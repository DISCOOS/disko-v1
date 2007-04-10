package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.MsoModelImpl;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.DuplicateIdException;
import org.redcross.sar.util.mso.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

/**
 * Search or rescue unit.
 */
public abstract class AbstractUnit extends AbstractMsoObject implements IUnitIf, ISerialNumberedIf
{

    private final AttributeImpl.MsoInteger m_averageSpeed = new AttributeImpl.MsoInteger(this, "AverageSpeed");
    private final AttributeImpl.MsoInteger m_bearing = new AttributeImpl.MsoInteger(this, "Bearing");
    private final AttributeImpl.MsoString m_callSign = new AttributeImpl.MsoString(this, "CallSign");
    private final AttributeImpl.MsoInteger m_maxSpeed = new AttributeImpl.MsoInteger(this, "MaxSpeed");
    private final AttributeImpl.MsoInteger m_number = new AttributeImpl.MsoInteger(this, "Number");
    private final AttributeImpl.MsoPosition m_position = new AttributeImpl.MsoPosition(this, "Position");
    private final AttributeImpl.MsoString m_remarks = new AttributeImpl.MsoString(this, "Remarks");
    private final AttributeImpl.MsoInteger m_speed = new AttributeImpl.MsoInteger(this, "Speed");
    private final AttributeImpl.MsoEnum<UnitType> m_type = new AttributeImpl.MsoEnum<UnitType>(this, "Type", UnitType.COMMAND_POST);

    private final AssignmentListImpl m_unitAssignments = new AssignmentListImpl(this, "UnitAssignments", false);
    private final PersonnelListImpl m_unitPersonnel = new PersonnelListImpl(this, "UnitPersonnel", false);

    private final MsoReferenceImpl<IHierarchicalUnitIf> m_superiorUnit = new MsoReferenceImpl<IHierarchicalUnitIf>(this, "SuperiorUnit", false);
    private final MsoReferenceImpl<IPersonnelIf> m_unitLeader = new MsoReferenceImpl<IPersonnelIf>(this, "UnitLeader", true);


    public AbstractUnit(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber)
    {
        super(anObjectId);
        m_number.setValue(aNumber);
        setType(getTypeBySubclass());
    }


    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_UNIT;
    }


    protected void defineAttributes()
    {
        addAttribute(m_averageSpeed);
        addAttribute(m_bearing);
        addAttribute(m_callSign);
        addAttribute(m_maxSpeed);
        addAttribute(m_number);
        addAttribute(m_position);
        addAttribute(m_remarks);
        addAttribute(m_speed);
        addAttribute(m_type);
    }

    protected void defineLists()
    {
        addList(m_unitAssignments);
        addList(m_unitPersonnel);
    }

    protected void defineReferences()
    {
        addReference(m_superiorUnit);
        addReference(m_unitLeader);
    }


    protected abstract UnitType getTypeBySubclass();

    /**
     * Local implementation of {@link AbstractMsoObject#registerModifiedData()}
     * Resets correct subclass in case of incorrect changes by application or others.
     */
    public void registerModifiedData()
    {
        if (getType() != getTypeBySubclass())
        {
            setType(getTypeBySubclass());
        }
        super.registerModifiedData();
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for ENUM attributes
    *-------------------------------------------------------------------------------------------*/

    protected void setType(UnitType aType)
    {
        m_type.setValue(aType);
    }

    public UnitType getType()
    {
        return m_type.getValue();
    }

    public IMsoModelIf.ModificationState getTypeState()
    {
        return m_type.getState();
    }

    public IAttributeIf.IMsoEnumIf<UnitType> getTypeAttribute()
    {
        return m_type;
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for attributes
    *-------------------------------------------------------------------------------------------*/

    public void setAverageSpeed(int anAverageSpeed)
    {
        m_averageSpeed.setValue(anAverageSpeed);
    }

    public int getAverageSpeed()
    {
        return m_averageSpeed.intValue();
    }

    public IMsoModelIf.ModificationState getAverageSpeedState()
    {
        return m_averageSpeed.getState();
    }

    public IAttributeIf.IMsoIntegerIf getAverageSpeedAttribute()
    {
        return m_averageSpeed;
    }

    public void setBearing(int aBearing)
    {
        m_bearing.setValue(aBearing);
    }

    public int getBearing()
    {
        return m_bearing.intValue();
    }

    public IMsoModelIf.ModificationState getBearingState()
    {
        return m_bearing.getState();
    }

    public IAttributeIf.IMsoIntegerIf getBearingAttribute()
    {
        return m_bearing;
    }

    public void setCallSign(String aCallSign)
    {
        m_callSign.setValue(aCallSign);
    }

    public String getCallSign()
    {
        return m_callSign.getString();
    }

    public IMsoModelIf.ModificationState getCallSignState()
    {
        return m_callSign.getState();
    }

    public IAttributeIf.IMsoStringIf getCallSignAttribute()
    {
        return m_callSign;
    }

    public void setMaxSpeed(int aMaxSpeed)
    {
        m_maxSpeed.setValue(aMaxSpeed);
    }

    public int getMaxSpeed()
    {
        return m_maxSpeed.intValue();
    }

    public IMsoModelIf.ModificationState getMaxSpeedState()
    {
        return m_maxSpeed.getState();
    }

    public IAttributeIf.IMsoIntegerIf getMaxSpeedAttribute()
    {
        return m_maxSpeed;
    }

    public void setPosition(Position aPosition)
    {
        m_position.setValue(aPosition);
    }

    public Position getPosition()
    {
        return m_position.getPosition();
    }

    public IMsoModelIf.ModificationState getPositionState()
    {
        return m_position.getState();
    }

    public IAttributeIf.IMsoPositionIf getPositionAttribute()
    {
        return m_position;
    }

    public void setRemarks(String aRemarks)
    {
        m_remarks.setValue(aRemarks);
    }

    public String getRemarks()
    {
        return m_remarks.getString();
    }

    public IMsoModelIf.ModificationState getRemarksState()
    {
        return m_remarks.getState();
    }

    public IAttributeIf.IMsoStringIf getRemarksAttribute()
    {
        return m_remarks;
    }

    public void setSpeed(int aSpeed)
    {
        m_speed.setValue(aSpeed);
    }

    public int getSpeed()
    {
        return m_speed.intValue();
    }

    public IMsoModelIf.ModificationState getSpeedState()
    {
        return m_speed.getState();
    }

    public IAttributeIf.IMsoIntegerIf getSpeedAttribute()
    {
        return m_speed;
    }

    // From ISerialNumberedIf
    public void setNumber(int aNumber)
    {
        m_number.setValue(aNumber);
    }

    public int getNumber()
    {
        return m_number.intValue();
    }

    public IMsoModelIf.ModificationState getNumberState()
    {
        return m_number.getState();
    }

    public IAttributeIf.IMsoIntegerIf getNumberAttribute()
    {
        return m_number;
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for lists
    *-------------------------------------------------------------------------------------------*/

    public void addUnitAssignments(IAssignmentIf anIAssignmentIf) throws DuplicateIdException
    {
        m_unitAssignments.add(anIAssignmentIf);
    }

    public IAssignmentListIf getUnitAssignments()
    {
        return m_unitAssignments;
    }

    public IMsoModelIf.ModificationState getUnitAssignmentsState(IAssignmentIf anIAssignmentIf)
    {
        return m_unitAssignments.getState(anIAssignmentIf);
    }

    public Collection<IAssignmentIf> getUnitAssignmentsItems()
    {
        return m_unitAssignments.getItems();
    }

    public void addUnitPersonnel(IPersonnelIf anIPersonnelIf) throws DuplicateIdException
    {
        m_unitPersonnel.add(anIPersonnelIf);
    }

    public IPersonnelListIf getUnitPersonnel()
    {
        return m_unitPersonnel;
    }

    public IMsoModelIf.ModificationState getUnitPersonnelState(IPersonnelIf anIPersonnelIf)
    {
        return m_unitPersonnel.getState(anIPersonnelIf);
    }

    public Collection<IPersonnelIf> getUnitPersonnelItems()
    {
        return m_unitPersonnel.getItems();
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for references
    *-------------------------------------------------------------------------------------------*/


    public boolean setSuperiorUnit(IHierarchicalUnitIf aSuperior)
    {
        IHierarchicalUnitIf tu = aSuperior;
        while (tu != null && tu != this)
        {
            tu = tu.getSuperiorUnit();
        }
        if (tu != null)
        {
            return false;
        }
        m_superiorUnit.setReference(aSuperior);
        return true;
    }

    public IHierarchicalUnitIf getSuperiorUnit()
    {
        return m_superiorUnit.getReference();
    }

    public IMsoModelIf.ModificationState getSuperiorUnitState()
    {
        return m_superiorUnit.getState();
    }

    public IMsoReferenceIf<IHierarchicalUnitIf> getSuperiorUnitAttribute()
    {
        return m_superiorUnit;
    }

    public void setUnitLeader(IPersonnelIf aPersonnel)
    {
        m_unitLeader.setReference(aPersonnel);
    }

    public IPersonnelIf getUnitLeader()
    {
        return m_unitLeader.getReference();
    }

    public IMsoModelIf.ModificationState getUnitLeaderState()
    {
        return m_unitLeader.getState();
    }

    public IMsoReferenceIf<IPersonnelIf> getUnitLeaderAttribute()
    {
        return m_unitLeader;
    }

    /**
     * Generate list of subordinates for this unit
     *
     * @return The list
     */
    public List<IHierarchicalUnitIf> getSubOrdinates()
    {
        return getSubOrdinates(this);
    }

    /**
     * Generate list of subordinates for a {@link IHierarchicalUnitIf} unit.
     *
     * @param aUnit The unit that has subordinates.
     * @return The list.
     */
    public static List<IHierarchicalUnitIf> getSubOrdinates(IHierarchicalUnitIf aUnit)
    {
        ArrayList<IHierarchicalUnitIf> resultList = new ArrayList<IHierarchicalUnitIf>();
        IUnitListIf mainList = MsoModelImpl.getInstance().getMsoManager().getCmdPost().getUnitList();
        for (IUnitIf u : mainList.getItems())
        {
            if (u.getSuperiorUnit() == aUnit) // todo Test !!!!!!!!!!!!!!!!
            {
                resultList.add(u);
            }
        }
        return resultList;
    }

    public String toString()
    {
        return "AbstractUnit" + " " + getObjectId();
    }

}
