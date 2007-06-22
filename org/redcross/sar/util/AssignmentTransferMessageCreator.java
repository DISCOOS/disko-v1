package org.redcross.sar.util;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.wp.AbstractDiskoWpModule;

import java.util.Calendar;
/**
 * Created by IntelliJ IDEA.
 * User: vinjar
 * Date: 19.jun.2007
 * To change this template use File | Settings | File Templates.
 */

/**
 *
 */
public class AssignmentTransferMessageCreator
{
    public static void createMessage(AbstractDiskoWpModule aWpModule, IUnitIf aUnit, IAssignmentIf anAssignment)
    {
        IMsoManagerIf manager = aWpModule.getMsoManager();
        IMessageIf message = manager.createMessage();
        message.setCreated(Calendar.getInstance());
//System.out.println(MessageFormat.format("{0} transferred to status {1} and unit {2} at {3}.",anAssignment.getNumber(),anAssignment.getStatus(),aUnit,message.getCreated())) ;
    }
}
