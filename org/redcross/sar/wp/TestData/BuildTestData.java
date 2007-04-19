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
                testOperation = msoManager.createOperation("2007-TEST", "0001");
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
        IUnitIf unit;

        unit= unitList.createVehicle("St 39911");
        unit.setStatus(IUnitIf.UnitStatus.READY);

        unit = unitList.createVehicle("St 39912");
        unit.setStatus(IUnitIf.UnitStatus.READY);

        unit = unitList.createVehicle("St 39913");
        unit.setStatus(IUnitIf.UnitStatus.EMPTY);

        unit = unitList.createVehicle("St 39914");
        unit.setStatus(IUnitIf.UnitStatus.EMPTY);

        unit = unitList.createVehicle("St 39915");
        unit.setStatus(IUnitIf.UnitStatus.EMPTY);

        IAssignmentListIf asgList = cmdPost.getAssignmentList();
        try
        {
            IAssignmentIf asg;

            unit = unitList.getUnit(1);
            for (int i = 0; i < 10; i++)
            {
                asg = asgList.createSearch();
                asg.setStatus(IAssignmentIf.AssignmentStatus.DRAFT);
                asg.setStatus(IAssignmentIf.AssignmentStatus.READY);
                unit.addUnitAssignment(asg);
            }

            unit = unitList.getUnit(2);
            for (int i = 0; i < 8; i++)
            {
                asg = asgList.createSearch();
                asg.setStatus(IAssignmentIf.AssignmentStatus.DRAFT);
                asg.setStatus(IAssignmentIf.AssignmentStatus.READY);
                unit.addUnitAssignment(asg);
            }

            for (int i = 0; i < 12; i++)
            {
                asg = asgList.createSearch();
                asg.setStatus(IAssignmentIf.AssignmentStatus.DRAFT);
                asg.setStatus(IAssignmentIf.AssignmentStatus.READY);
            }

        }
        catch (MsoException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        aMsoModel.restoreUpdateMode();
    }

}
