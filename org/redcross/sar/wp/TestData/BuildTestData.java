package org.redcross.sar.wp.TestData;

import org.redcross.sar.mso.data.*;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.MsoException;
import org.redcross.sar.util.except.DuplicateIdException;
import org.redcross.sar.util.except.IllegalOperationException;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
/**
 * Created by IntelliJ IDEA.
 * User: vinjar
 * Date: 16.apr.2007
 * To change this template use File | Settings | File Templates.
 */

/**
 *
 */
public class BuildTestData
{
   public static void createCmdPost(IMsoModelIf aMsoModel)
    {
        aMsoModel.setRemoteUpdateMode();
        IMsoManagerIf msoManager = aMsoModel.getMsoManager();
        IOperationIf testOperation = msoManager.getOperation();
        if (testOperation == null)
        {
            try
            {
                testOperation = msoManager.createOperation("2007-TEST","0001");
            }
            catch (DuplicateIdException e) // shall not happen
            {
                e.printStackTrace();
            }
        }
        ICmdPostIf cmdPost = msoManager.getCmdPost();
        if (cmdPost == null)
        {
            try
            {
                cmdPost = msoManager.createCmdPost();
            }
            catch (DuplicateIdException e) // shall not happen
            {
                e.printStackTrace();
            }
        }
        if (cmdPost != null)
        {
            cmdPost.setStatus(ICmdPostIf.CmdPostStatus.OPERATING);
        }
        aMsoModel.restoreUpdateMode();
    }

    public static void createUnitsAndAssignments(IMsoModelIf aMsoModel)
    {
        ICmdPostIf cmdPost = aMsoModel.getMsoManager().getCmdPost();
        aMsoModel.setRemoteUpdateMode();
        IUnitListIf unitList = cmdPost.getUnitList();
        IUnitIf unit1 = unitList.createVehicle("St 39911");
        unit1.setStatus(IUnitIf.UnitStatus.READY);

        IUnitIf unit2 = unitList.createVehicle("St 39912");
        unit2.setStatus(IUnitIf.UnitStatus.READY);

        IAssignmentListIf asgList = cmdPost.getAssignmentList();
        IAssignmentIf asg1 = asgList.createSearch();
        IAssignmentIf asg2 = asgList.createSearch();
        IAssignmentIf asg3 = asgList.createSearch();
        IAssignmentIf asg4 = asgList.createSearch();
        IAssignmentIf asg5 = asgList.createSearch();
        try
        {
            asg1.setStatus(IAssignmentIf.AssignmentStatus.DRAFT);
            asg1.setStatus(IAssignmentIf.AssignmentStatus.READY);
            unit1.addUnitAssignment(asg1);

            asg2.setStatus(IAssignmentIf.AssignmentStatus.DRAFT);
            asg2.setStatus(IAssignmentIf.AssignmentStatus.READY);
            unit1.addUnitAssignment(asg2);

            asg3.setStatus(IAssignmentIf.AssignmentStatus.DRAFT);
            asg3.setStatus(IAssignmentIf.AssignmentStatus.READY);
            unit1.addUnitAssignment(asg3);
            asg3.setStatus(IAssignmentIf.AssignmentStatus.PAUSED);

            asg4.setStatus(IAssignmentIf.AssignmentStatus.DRAFT);
            asg4.setStatus(IAssignmentIf.AssignmentStatus.READY);
            unit2.addUnitAssignment(asg4);
            asg4.setStatus(IAssignmentIf.AssignmentStatus.ASSIGNED);

            asg5.setStatus(IAssignmentIf.AssignmentStatus.DRAFT);
            asg5.setStatus(IAssignmentIf.AssignmentStatus.READY);
            unit2.addUnitAssignment(asg5);
            asg5.setStatus(IAssignmentIf.AssignmentStatus.FINISHED);

        }
        catch (MsoException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        aMsoModel.restoreUpdateMode();
    }

}
