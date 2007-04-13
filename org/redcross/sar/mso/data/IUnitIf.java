package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.MsoCastException;
import org.redcross.sar.util.except.MsoException;
import org.redcross.sar.util.except.DuplicateIdException;
import org.redcross.sar.util.except.IllegalOperationException;
import org.redcross.sar.util.mso.*;

import java.util.Collection;
import java.util.List;

/**
 *
 */
public interface IUnitIf extends IHierarchicalUnitIf, ICommunicatorIf
{
    public enum UnitType
    {
        COMMAND_POST,
        TEAM,
        DOG,
        AIRCRAFT,
        BOAT,
        VEHICLE
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for ENUM attributes
    *-------------------------------------------------------------------------------------------*/

    public UnitType getType();

    public IMsoModelIf.ModificationState getTypeState();

    public IAttributeIf.IMsoEnumIf<UnitType> getTypeAttribute();

    /*-------------------------------------------------------------------------------------------
    * Methods for attributes
    *-------------------------------------------------------------------------------------------*/

    public void setAverageSpeed(int anAverageSpeed);

    public int getAverageSpeed();

    public IMsoModelIf.ModificationState getAverageSpeedState();

    public IAttributeIf.IMsoIntegerIf getAverageSpeedAttribute();

    public void setBearing(int aBearing);

    public int getBearing();

    public IMsoModelIf.ModificationState getBearingState();

    public IAttributeIf.IMsoIntegerIf getBearingAttribute();

    public void setCallSign(String aCallSign);

    public String getCallSign();

    public IMsoModelIf.ModificationState getCallSignState();

    public IAttributeIf.IMsoStringIf getCallSignAttribute();

    public void setMaxSpeed(int aMaxSpeed);

    public int getMaxSpeed();

    public IMsoModelIf.ModificationState getMaxSpeedState();

    public IAttributeIf.IMsoIntegerIf getMaxSpeedAttribute();

    public void setPosition(Position aPosition);

    public Position getPosition();

    public IMsoModelIf.ModificationState getPositionState();

    public IAttributeIf.IMsoPositionIf getPositionAttribute();

    public void setRemarks(String aRemarks);

    public String getRemarks();

    public IMsoModelIf.ModificationState getRemarksState();

    public IAttributeIf.IMsoStringIf getRemarksAttribute();

    public void setSpeed(int aSpeed);

    public int getSpeed();

    public IMsoModelIf.ModificationState getSpeedState();

    public IAttributeIf.IMsoIntegerIf getSpeedAttribute();

    /*-------------------------------------------------------------------------------------------
    * Methods for lists
    *-------------------------------------------------------------------------------------------*/

    /**
     * @param anIAssignmentIf The assigment to add
     * @throws DuplicateIdException if the list already contains an object with the same object ID.
     * @throws org.redcross.sar.util.except.IllegalOperationException if the assignment cannot be assigned.
     */
    public void addUnitAssignment(IAssignmentIf anIAssignmentIf) throws DuplicateIdException, IllegalOperationException;

    public void removeUnitAssignment(IAssignmentIf anIAssignmentIf, IAssignmentIf.AssignmentStatus newStatus) throws IllegalOperationException;

    public IAssignmentListIf getUnitAssignments();

    public IMsoModelIf.ModificationState getUnitAssignmentsState(IAssignmentIf anIAssignmentIf);

    public Collection<IAssignmentIf> getUnitAssignmentsItems();

    public void addUnitPersonnel(IPersonnelIf anIPersonnelIf) throws DuplicateIdException;

    public IPersonnelListIf getUnitPersonnel();

    public IMsoModelIf.ModificationState getUnitPersonnelState(IPersonnelIf anIPersonnelIf);

    public Collection<IPersonnelIf> getUnitPersonnelItems();

    /*-------------------------------------------------------------------------------------------
    * Methods for references
    *-------------------------------------------------------------------------------------------*/

    public void setUnitLeader(IPersonnelIf aPersonnel);

    public IPersonnelIf getUnitLeader();

    public IMsoModelIf.ModificationState getUnitLeaderState();

    public IMsoReferenceIf<IPersonnelIf> getUnitLeaderAttribute();

    /*-------------------------------------------------------------------------------------------
    * Other methods
    *-------------------------------------------------------------------------------------------*/

    public List<IAssignmentIf> assignmentsByPriority();

}
