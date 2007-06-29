package org.redcross.sar.util;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.wp.AbstractDiskoWpModule;

import java.util.Calendar;
import java.util.EnumSet;
/**
 * Created by IntelliJ IDEA.
 * User: vinjar
 * Date: 19.jun.2007
 * To change this template use File | Settings | File Templates.
 */

/**
 *
 */
public class AssignmentTransferUtilities
{
    final static EnumSet<IAssignmentIf.AssignmentStatus> acceptedStatuses = EnumSet.range(IAssignmentIf.AssignmentStatus.ASSIGNED, IAssignmentIf.AssignmentStatus.REPORTED);

    public static void createCommunicationMessage(AbstractDiskoWpModule aWpModule, IUnitIf aUnit, IAssignmentIf anAssignment)
    {

        IMessageLineIf.MessageLineType lineType;
        switch (anAssignment.getStatus())
        {
            case ASSIGNED:
                lineType = IMessageLineIf.MessageLineType.ASSIGNED;
                break;
            case EXECUTING:
                lineType = IMessageLineIf.MessageLineType.STARTED;
                break;
            case ABORTED:
            case FINISHED:
            case REPORTED:
                lineType = IMessageLineIf.MessageLineType.COMPLETE;
                break;
            default:
                return;
        }
        IMsoManagerIf manager = aWpModule.getMsoManager();
        IMessageIf message = manager.createMessage();
        message.setCreated(Calendar.getInstance());
        message.setCalendar(Calendar.getInstance());
        message.setStatus(IMessageIf.MessageStatus.UNCONFIRMED);
        IMessageLineIf l0 = message.findMessageLine(lineType, true);
        IMessageLineIf l1 = message.findMessageLine(IMessageLineIf.MessageLineType.TEXT, true);
        l1.setText(anAssignment.getStatus().name());
    }

    public static boolean unitCanAccept(IUnitIf aUnit, IAssignmentIf.AssignmentStatus aStatus)
    {
        switch (aUnit.getStatus())
        {
            case READY:
            case PAUSED:
            case WORKING:
                if (aStatus == IAssignmentIf.AssignmentStatus.ALLOCATED)
                {
                    return true;
                } else if (aStatus == IAssignmentIf.AssignmentStatus.ASSIGNED)
                {
                    return (aUnit.getAssignedAssignments().size() == 0);
                } else if (aStatus == IAssignmentIf.AssignmentStatus.EXECUTING)
                {
                    return (aUnit.getExecutingAssigment().size() == 0);
                } else
                {
                    return true;
                }
            case RELEASED:
                return aStatus == IAssignmentIf.AssignmentStatus.REPORTED;
            default:
                return false;
        }
    }

    /**
     * Test if an assignment can change satus and owner.
     * @param anAssignment The assignment to change
     * @param newStatus The new status
     * @param newUnit The new owner
     * @return <code>true</code> if the change is legal, <code>false</code> otherwise.
     */
    public static boolean assignmentCanChangeToStatus(IAssignmentIf anAssignment, String newStatus, IUnitIf newUnit)
    {
        IAssignmentIf.AssignmentStatus status = anAssignment.getStatusAttribute().enumValue(newStatus);
        if (status == null)
        {
            return false;
        }
        return assignmentCanChangeToStatus(anAssignment,status, newUnit);
    }

    public static boolean assignmentCanChangeToStatus(IAssignmentIf anAssignment, IAssignmentIf.AssignmentStatus newStatus, IUnitIf newUnit)
    {
        IUnitIf currentUnit = anAssignment.getOwningUnit();
        IAssignmentIf.AssignmentStatus currentStatus = anAssignment.getStatus();

        if (newStatus == currentStatus && newUnit == currentUnit)
        {
            return newStatus == IAssignmentIf.AssignmentStatus.ALLOCATED;     // Can drop on the same in order to change priority
        }

        switch (currentStatus)
        {
            case EMPTY:
                return newUnit == null && (newStatus == IAssignmentIf.AssignmentStatus.DRAFT || newStatus == IAssignmentIf.AssignmentStatus.READY);
            case DRAFT:
                return newUnit == null && newStatus == IAssignmentIf.AssignmentStatus.READY;
            case READY:
                return newUnit != null && IAssignmentIf.ACTIVE_SET.contains(newStatus) && AssignmentTransferUtilities.unitCanAccept(newUnit,newStatus);
            case ALLOCATED:
            case ASSIGNED:
                return newUnit == null ? newStatus == IAssignmentIf.AssignmentStatus.READY : (IAssignmentIf.ACTIVE_SET.contains(newStatus)  && AssignmentTransferUtilities.unitCanAccept(newUnit,newStatus));
            case EXECUTING:
                return newUnit == currentUnit && IAssignmentIf.FINISHED_AND_REPORTED_SET.contains(newStatus);
            case ABORTED:
            case FINISHED:
                return newUnit == currentUnit && newStatus == IAssignmentIf.AssignmentStatus.REPORTED;
        }
        return false;
    }



}
