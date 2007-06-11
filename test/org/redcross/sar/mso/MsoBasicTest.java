package org.redcross.sar.mso;

import static org.junit.Assert.*;
import org.junit.Test;
import org.redcross.sar.mso.data.*;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.util.except.DuplicateIdException;
import org.redcross.sar.util.except.IllegalOperationException;
import org.redcross.sar.util.except.MsoException;
import org.redcross.sar.util.except.MsoRuntimeException;
import org.redcross.sar.util.mso.Selector;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

/**
 *
 */
public class MsoBasicTest
{

    IMsoModelIf m_msoModel;
    IMsoManagerIf m_msoManager;
    IMsoEventManagerIf m_eventManager;

    private void connectToModel()
    {
        m_msoModel = MsoModelImpl.getInstance();
    }

    private void getModelDependencies()
    {
        m_msoManager = m_msoModel.getMsoManager();
        m_eventManager = m_msoModel.getEventManager();
    }

    @Test
    public void msoModelConnect()
    {
        connectToModel();
        assertNotNull(m_msoModel);
        getModelDependencies();
        assertNotNull(m_msoManager);
        assertNotNull(m_eventManager);
        msoTestUpdateModes();
        m_msoModel.setRemoteUpdateMode();
        msoModelTestCreate();
        msoTestUnits();
        msoTestLists();
        msoTestSubLists();
        msoTestReferences();
        msoTestTimeLine();
        msoTestAssignments();
        msoTestRollbackOrCommit(true);
        msoTestRollbackOrCommit(false);
        m_msoModel.restoreUpdateMode();
    }

    public void msoModelTestCreate()
    {
        try
        {
            IOperationIf operationA = m_msoManager.createOperation("2007-TEST","0001");
            assertNotNull(operationA);
            IOperationIf operationB = m_msoManager.getOperation();
            assertEquals(operationA, operationB);
            assertTrue(operationA.getCmdPostList().getItems().isEmpty());

            ICmdPostIf cmdPostA = m_msoManager.createCmdPost();
            assertNotNull(cmdPostA);
            ICmdPostIf cmdPostB = m_msoManager.getCmdPost();
            assertEquals(cmdPostA, cmdPostB);
            cmdPostB.setStatus(ICmdPostIf.CmdPostStatus.OPERATING);
            ICmdPostIf.CmdPostStatus cmdstat = cmdPostA.getStatus();
            assertEquals(cmdstat, ICmdPostIf.CmdPostStatus.OPERATING);

            String statname = cmdstat.name();
            assertEquals(statname, "OPERATING");

            cmdPostB.setStatus(ICmdPostIf.CmdPostStatus.RELEASED);
            cmdstat = cmdPostA.getStatus();
            statname = cmdstat.name();
            assertEquals(statname, "RELEASED");

            cmdstat = Enum.valueOf(ICmdPostIf.CmdPostStatus.class, statname);
            cmdPostA.setStatus(cmdstat);
            assertEquals(cmdPostB.getStatus().name(), "RELEASED");
        }
        catch (MsoRuntimeException e)
        {
            assertTrue(false);
        }
    }

    public void msoTestUpdateModes()
    {
        assertEquals(m_msoModel.getUpdateMode(), IMsoModelIf.UpdateMode.LOCAL_UPDATE_MODE);
        m_msoModel.setRemoteUpdateMode();
        assertEquals(m_msoModel.getUpdateMode(), IMsoModelIf.UpdateMode.REMOTE_UPDATE_MODE);
        m_msoModel.setLoopbackUpdateMode();
        assertEquals(m_msoModel.getUpdateMode(), IMsoModelIf.UpdateMode.LOOPBACK_UPDATE_MODE);
        m_msoModel.restoreUpdateMode();
        assertEquals(m_msoModel.getUpdateMode(), IMsoModelIf.UpdateMode.REMOTE_UPDATE_MODE);
        m_msoModel.restoreUpdateMode();
        assertEquals(m_msoModel.getUpdateMode(), IMsoModelIf.UpdateMode.LOCAL_UPDATE_MODE);
    }


    public void msoTestUnits()
    {
        ICmdPostIf cmdPost = m_msoManager.getCmdPost();
        IHierarchicalUnitIf koUnit = m_msoManager.getCmdPostUnit();
        IUnitListIf unitList = cmdPost.getUnitList();
        UnitListImpl unitListImpl = (UnitListImpl) unitList;
        assertNotNull(unitList);

        unitListImpl.clear();

        IUnitIf vUnit1 = unitList.createVehicle("V1");
        assertNotNull(vUnit1);
        assertTrue(koUnit.setSuperiorUnit(null));
        assertTrue(vUnit1.setSuperiorUnit(koUnit));
        assertFalse(koUnit.setSuperiorUnit(vUnit1));

        IUnitIf vUnit2 = unitList.createVehicle("V2");
        assertNotNull(vUnit2);
        assertTrue(vUnit2.setSuperiorUnit(koUnit));

        IUnitIf bUnit1 = unitList.createBoat("B1");

        List<IUnitIf> units = unitList.selectItems(new Selector<IUnitIf>()
        {
            public boolean select(IUnitIf anObject)
            {
                return anObject.getSuperiorUnit() != null;
            }
        }, new Comparator<IUnitIf>()
        { // Note: this comparator imposes orderings that are inconsistent with equals.

            public int compare(IUnitIf o1, IUnitIf o2)
            {
                return o1.getObjectId().compareTo(o2.getObjectId());
            }
        });
        assertNotNull(units);
        assertEquals(units.size(), 2);


        List<IHierarchicalUnitIf> koSubOrdList;
        koSubOrdList = koUnit.getSubOrdinates();
        assertNotNull(koSubOrdList);
        assertEquals(koSubOrdList.size(), 2);

        koSubOrdList = vUnit1.getSubOrdinates();
        assertEquals(koSubOrdList.size(), 0);
        assertTrue(vUnit2.setSuperiorUnit(vUnit1));

        koSubOrdList = koUnit.getSubOrdinates();
        assertEquals(koSubOrdList.size(), 1);
        assertTrue(koSubOrdList.contains(vUnit1));
        assertFalse(koSubOrdList.contains(vUnit2));

        koSubOrdList = vUnit1.getSubOrdinates();
        assertEquals(koSubOrdList.size(), 1);
        assertTrue(koSubOrdList.contains(vUnit2));

        koSubOrdList = vUnit2.getSubOrdinates();
        assertEquals(koSubOrdList.size(), 0);
        unitListImpl.clear();
        assertEquals(unitList.size(), 0);
    }

    public void msoTestLists()
    {
        ICmdPostIf cmdPost = m_msoManager.getCmdPost();
        IUnitListIf unitList = cmdPost.getUnitList();

        UnitListImpl unitListImpl = (UnitListImpl) unitList;
        unitListImpl.clear();

        UnitListImpl myUnitList = new UnitListImpl(null, "MyTestList", false);

        int mainCount = 0;
        int myCount = 0;

        try
        {
            IUnitIf vUnit1 = unitList.createVehicle("V1");
            mainCount++;
            int v1Count = 1;
            IUnitIf vUnit2 = unitList.createVehicle("V2");
            mainCount++;
            int v2Count = 1;
            IUnitIf vUnit3 = unitList.createVehicle("V3");
            mainCount++;
            int v3Count = 1;
            myUnitList.add(vUnit2);
            myCount++;
            assertEquals(unitList.size(), mainCount);
            assertEquals(myUnitList.size(), myCount);
            IUnitIf vUnit4 = unitList.createVehicle("V4");
            mainCount++;
            int v4Count = 1;
            IUnitIf vUnit5 = unitList.createVehicle("V5");
            mainCount++;
            int v5Count = 1;
            assertEquals(unitList.size(), mainCount);
            assertEquals(((AbstractMsoObject) vUnit3).listenerCount(), v3Count);
            assertEquals(((AbstractMsoObject) vUnit4).listenerCount(), v4Count);

//            System.out.println("Test Add vehicle 1");
            myUnitList.add(vUnit1);
            myCount++;
            v1Count++;
//            System.out.println("Test Add vehicle 3");
            myUnitList.add(vUnit3);
            myCount++;
            v3Count++;
            myUnitList.add(vUnit4);
            myCount++;
            v4Count++;
            assertEquals(myUnitList.size(), myCount);
            assertEquals(((AbstractMsoObject) vUnit3).listenerCount(), v3Count);
            assertEquals(((AbstractMsoObject) vUnit4).listenerCount(), v4Count);

//            System.out.println("Test Remove vehicle 3 from a list");
            myUnitList.removeReference(vUnit3);
            myCount--;
            v3Count--;
            assertEquals(myUnitList.size(), myCount);
            assertEquals(((AbstractMsoObject) vUnit3).listenerCount(), v3Count);

//            System.out.println("Test Remove vehicle 3 from a list once again");
            myUnitList.removeReference(vUnit3);
            assertEquals(myUnitList.size(), myCount);
            assertEquals(((AbstractMsoObject) vUnit3).listenerCount(), v3Count);

//            unitListImpl.print();
//            myUnitList.print();

//            System.out.println("Test Remove completely vehicle 1");
            assertTrue(unitList.removeReference(vUnit1));
            vUnit1 = null;
            v1Count = 0;
            myCount--;
            mainCount--;
            assertEquals(unitList.size(), mainCount);
            assertEquals(myUnitList.size(), myCount);

            assertTrue(vUnit2.deleteObject());
            vUnit2 = null;
            v2Count = 0;
            myCount--;
            mainCount--;

            assertTrue(vUnit4.deleteObject());
            vUnit4 = null;
            v4Count = 0;
            myCount--;
            mainCount--;

//            unitListImpl.print();
//            myUnitList.print();

            assertEquals(unitList.size(), mainCount);
            assertEquals(myUnitList.size(), myCount);
            assertEquals(((AbstractMsoObject) vUnit3).listenerCount(), v3Count);
            assertEquals(((AbstractMsoObject) vUnit5).listenerCount(), v5Count);
            unitListImpl.clear();
            assertEquals(unitList.size(), 0);
            assertEquals(myUnitList.size(), 0);
            assertEquals(((AbstractMsoObject) vUnit3).listenerCount(), 0);
            assertEquals(((AbstractMsoObject) vUnit5).listenerCount(), 0);
        }
        catch (MsoRuntimeException e)
        {
            e.printStackTrace();
        }
        unitListImpl.clear();
    }

    public void msoTestSubLists()
    {
        ICmdPostIf cmdPost = m_msoManager.getCmdPost();
        IUnitListIf unitList = cmdPost.getUnitList();

        UnitListImpl unitListImpl = (UnitListImpl) unitList;
        unitListImpl.clear();

        UnitListImpl myUnitList = new UnitListImpl(null, "MyTestList", false);

        IAssignmentListIf assignmentList = cmdPost.getAssignmentList();
        assertNotNull(assignmentList);
        AssignmentListImpl assignmentListImpl = (AssignmentListImpl) assignmentList;
        assignmentListImpl.clear();

        IPOIListIf puiList = cmdPost.getPOIList();
        assertNotNull(puiList);
        POIListImpl puiListImpl = (POIListImpl) puiList;
        puiListImpl.clear();

        int mainUnitCount = 0;
        int myCount = 0;

        try
        {
            IUnitIf vUnit1 = unitList.createVehicle("V1");
            mainUnitCount++;
            int v1Count = 1;
            IUnitIf vUnit2 = unitList.createVehicle("V2");
            mainUnitCount++;
            int v2Count = 1;
            IUnitIf vUnit3 = unitList.createVehicle("V3");
            mainUnitCount++;
            int v3Count = 1;
            myUnitList.add(vUnit1);
            myCount++;

            AbstractUnit aUnit1 = (AbstractUnit) vUnit1;

            IAssignmentIf assign11 = assignmentList.createAssignment();
            assign11.setStatusAndOwner(IAssignmentIf.AssignmentStatus.READY,null);
            vUnit1.addUnitAssignment(assign11,IAssignmentIf.AssignmentStatus.ALLOCATED);
            assertEquals(assign11.getOwningUnit(), vUnit1);
            int vmCount1 = 1;
            IAssignmentIf assign12 = assignmentList.createAssignment();
            assign12.setStatusAndOwner(IAssignmentIf.AssignmentStatus.READY,null);
            assertNull(assign12.getOwningUnit());
            vUnit1.addUnitAssignment(assign12,IAssignmentIf.AssignmentStatus.ALLOCATED);
            assertEquals(assign12.getOwningUnit(), vUnit1);
            vmCount1++;
            assertEquals(aUnit1.getUnitAssignmentsItems().size(), vmCount1);

            assertTrue(assignmentList.removeReference(assign12));
            vmCount1--;
            assign12 = null;
            assertEquals(aUnit1.getUnitAssignmentsItems().size(), vmCount1);

            IAssignmentIf assign13 = assignmentList.createAssignment();
            AssignmentImpl misImpl13 = (AssignmentImpl) assign13;
            assign13.setStatusAndOwner(IAssignmentIf.AssignmentStatus.READY,null);
            vUnit1.addUnitAssignment(assign13,IAssignmentIf.AssignmentStatus.ALLOCATED);
            vmCount1++;
            assertEquals(aUnit1.getUnitAssignmentsItems().size(), vmCount1);
            assertEquals(misImpl13.listenerCount(), 2);

            List l1 = vUnit1.getAllocatedAssignments();

            IPOIIf PUI1 = puiList.createPOI();
            assign13.addAssignmentFinding(PUI1);
            int mpCount3 = 1;
            IPOIIf PUI2 = puiList.createPOI();
            assign13.addAssignmentFinding(PUI2);
            mpCount3++;
            assertEquals(misImpl13.POICount(), mpCount3);
            assertEquals(((POIImpl) PUI1).listenerCount(), 2);
            assertEquals(((POIImpl) PUI2).listenerCount(), 2);
            assertEquals(puiList.size(), 2);
            assertEquals(unitList.size(), mainUnitCount);
            assertEquals(myUnitList.size(), myCount);

            assertTrue(unitList.removeReference(vUnit1));
            mainUnitCount--;
            myCount--;
            assertEquals(unitList.size(), mainUnitCount);
            assertEquals(myUnitList.size(), myCount);
            assertEquals(puiList.size(), 2);

        }
        catch (MsoException e)
        {
            e.printStackTrace();
        }
        unitListImpl.clear();
        assignmentListImpl.clear();
        puiListImpl.clear();
    }

    public void msoTestReferences()
    {
        ICmdPostIf cmdPost = m_msoManager.getCmdPost();
        IUnitListIf unitList = cmdPost.getUnitList();

        UnitListImpl unitListImpl = (UnitListImpl) unitList;
        unitListImpl.clear();

        IAssignmentListIf assignmentList = cmdPost.getAssignmentList();
        assertNotNull(assignmentList);
        AssignmentListImpl assignmentListImpl = (AssignmentListImpl) assignmentList;
        assignmentListImpl.clear();

        IBriefingListIf briefingListIf = cmdPost.getBriefingList();
        assertNotNull(briefingListIf);
        BriefingListImpl briefingListImpl = (BriefingListImpl) briefingListIf;
        briefingListImpl.clear();

        int mainUnitCount = 0;

        IUnitIf vUnit1 = unitList.createVehicle("V1");
        mainUnitCount++;
        IUnitIf vUnit2 = unitList.createVehicle("V2");
        mainUnitCount++;
        IUnitIf vUnit3 = unitList.createVehicle("V3");
        mainUnitCount++;
        assertEquals(unitList.size(), mainUnitCount);
        vUnit2.setSuperiorUnit(vUnit1);
        vUnit3.setSuperiorUnit(vUnit2);
        assertFalse(unitList.removeReference(vUnit1));
        assertFalse(unitList.removeReference(vUnit2));

        assertTrue(unitList.removeReference(vUnit3));
        mainUnitCount--;
        assertEquals(unitList.size(), mainUnitCount);

        assertTrue(unitList.removeReference(vUnit2));
        mainUnitCount--;
        assertEquals(unitList.size(), mainUnitCount);

        IAssignmentIf assign11 = assignmentList.createAssignment();
        assertEquals(assignmentList.size(), 1);

        IBriefingIf briefing1 = briefingListIf.createBriefing();
        assertEquals(briefingListIf.size(), 1);
        assertEquals(((BriefingImpl) briefing1).listenerCount(), 1);

        assign11.setAssignmentBriefing(briefing1);
        assertEquals(((BriefingImpl) briefing1).listenerCount(), 2);

        int assign11ListenerCount = ((AssignmentImpl) assign11).listenerCount();

        try
        {
            assign11.setStatusAndOwner(IAssignmentIf.AssignmentStatus.READY,null);
            vUnit1.addUnitAssignment(assign11,IAssignmentIf.AssignmentStatus.ALLOCATED);
        }
        catch (DuplicateIdException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch (IllegalOperationException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        assign11ListenerCount++;
        assertEquals(vUnit1.getUnitAssignmentsItems().size(), 1);
        assertEquals(((AssignmentImpl) assign11).listenerCount(), assign11ListenerCount);

        assertTrue(assignmentListImpl.removeReference(assign11));
        assertEquals(((BriefingImpl) briefing1).listenerCount(), 1);
        assertEquals(vUnit1.getUnitAssignmentsItems().size(), 0);
        assertEquals(((AssignmentImpl) assign11).listenerCount(), 0);

        briefingListImpl.clear();
        assertEquals(((BriefingImpl) briefing1).listenerCount(), 0);

        assertTrue(unitList.removeReference(vUnit1));
        mainUnitCount--;
        assertEquals(unitList.size(), mainUnitCount);
        assertEquals(vUnit1.getUnitAssignmentsItems().size(), 0);


        assignmentListImpl.clear();
        unitListImpl.clear();
    }


    public void msoTestTimeLine()
    {
        ICmdPostIf cmdPost = m_msoManager.getCmdPost();

        IEventLogIf eventLog = cmdPost.getEventLog();
        EventLogImpl eventLogImpl = (EventLogImpl) eventLog;
        eventLogImpl.clear();

        ITaskListIf taskList = cmdPost.getTaskList();
        TaskListImpl taskListImpl = (TaskListImpl) taskList;
        taskListImpl.clear();

        ITimeLineIf timeLine = cmdPost.getTimeLine();

        try
        {
            IEventIf event1 = eventLog.createEvent(Calendar.getInstance());
            event1.setDTG(121212L);
            assertEquals(timeLine.size(), 1);
            IEventIf event2 = eventLog.createEvent(Calendar.getInstance());
            event2.setDTG(131313L);
            assertEquals(timeLine.size(), 2);
            ITaskIf task1 = taskListImpl.createTask(Calendar.getInstance());
            task1.setDTG(121313L);
            assertEquals(timeLine.size(), 3);
            assertTrue(isSorted(timeLine));

            event1.setDTG(151212L);
            assertTrue(isSorted(timeLine));

            eventLog.removeReference(event2);
            assertEquals(timeLine.size(), 2);
            assertTrue(isSorted(timeLine));

            task1.setDTG(151213L);
            assertTrue(isSorted(timeLine));

            event1.setDTG(151213L);
            assertTrue(isSorted(timeLine));

            eventLog.removeReference(event1);
            assertEquals(timeLine.size(), 1);

            taskList.removeReference(task1);
            assertEquals(timeLine.size(), 0);
        }
        catch (MsoException e)
        {
            e.printStackTrace();
        }
    }

    public boolean isSorted(ITimeLineIf aTimeLine)
    {
        Calendar cal = null;
        for (ITimeItemIf ti : aTimeLine.getTimeItems())
        {
            if (cal != null)
            {
                if (cal.compareTo(ti.getCalendar()) > 0)
                {
                    return false;
                }
            }
            cal = ti.getCalendar();
        }
        return true;
    }


    public void msoTestAssignments()
    {
        ICmdPostIf cmdPost = m_msoManager.getCmdPost();

        IAssignmentListIf assignmentList = cmdPost.getAssignmentList();
        assertNotNull(assignmentList);
        AssignmentListImpl assignmentListImpl = (AssignmentListImpl) assignmentList;
        assignmentListImpl.clear();

        IAreaListIf areaList = cmdPost.getAreaList();
        assertNotNull(areaList);
        AreaListImpl areaListImpl = (AreaListImpl) areaList;
        areaListImpl.clear();

        IAssignmentIf assign11 = assignmentList.createAssignment();
        IAssignmentIf assign12 = assignmentList.createAssignment();

        IAreaIf area11 = areaList.createArea();
        IAreaIf area12 = areaList.createArea();
        assertNull(area11.getOwningAssignment());
        assertNull(area12.getOwningAssignment());


        IUnitListIf unitList = cmdPost.getUnitList();
        UnitListImpl unitListImpl = (UnitListImpl) unitList;
        assertNotNull(unitList);

        unitListImpl.clear();

        IUnitIf vUnit1 = unitList.createVehicle("V1");
        IUnitIf vUnit2 = unitList.createVehicle("V2");



        try
        {
            assign11.setPlannedArea(area11);
            assign12.setReportedArea(area12);
        }
        catch (IllegalOperationException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        assertEquals(assign11, area11.getOwningAssignment());
        assertEquals(assign12, area12.getOwningAssignment());

        try
        {
            assign11.setStatusAndOwner("DRAFT",null);
            assign11.setStatusAndOwner("READY",null);
            vUnit1.addUnitAssignment(assign11,IAssignmentIf.AssignmentStatus.ALLOCATED);
            vUnit2.addUnitAssignment(assign11,IAssignmentIf.AssignmentStatus.ASSIGNED);
        }
        catch (IllegalOperationException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        assertEquals(assign11.getStatus(), IAssignmentIf.AssignmentStatus.ASSIGNED);
        assertTrue(assign11.hasBeenAssigned());
        assertFalse(assign11.hasBeenFinished());
        try
        {
            assign11.setStatusAndOwner("EXECUTING",vUnit1);
            assign11.setStatusAndOwner("FINISHED",vUnit1);
            assign11.setStatusAndOwner("REPORTED",vUnit1);
        }
        catch (IllegalOperationException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        assertTrue(assign11.hasBeenFinished());

        assertNotNull(assign11.getPlannedArea());
        areaListImpl.clear();
        assertNull(assign11.getPlannedArea());
        assignmentListImpl.clear();
    }


    public void msoTestRollbackOrCommit(boolean commit)
    {
        ICmdPostIf cmdPost = m_msoManager.getCmdPost();
        IAssignmentListIf assignmentList = cmdPost.getAssignmentList();
        ((AssignmentListImpl) assignmentList).clear();
        assertEquals(assignmentList.size(), 0);

        IPOIListIf poiList = cmdPost.getPOIList();
        ((POIListImpl) poiList).clear();
        IBriefingListIf briefingList = cmdPost.getBriefingList();
        ((BriefingListImpl) briefingList).clear();
        IAssignmentIf assignment = assignmentList.createAssignment();

        assertEquals(poiList.size(), 0);
        assertEquals(briefingList.size(), 0);
        assertEquals(assignmentList.size(), 1);
        assertEquals(assignment.getAssignmentFindingsItems().size(), 0);
        assertNull(assignment.getAssignmentBriefing());

        IEventLogIf eventLog = cmdPost.getEventLog();
        EventLogImpl eventLogImpl = (EventLogImpl) eventLog;
        eventLogImpl.clear();

        ITaskListIf taskList = cmdPost.getTaskList();
        TaskListImpl taskListImpl = (TaskListImpl) taskList;
        taskListImpl.clear();

        ITimeLineIf timeLine = cmdPost.getTimeLine();


        boolean updateModeSet = false;
        try
        {
            IEventIf event1 = eventLog.createEvent(Calendar.getInstance());
            event1.setDTG(121212L);
            assertEquals(timeLine.size(), 1);
            assertEquals(event1.getCalendarState(), IMsoModelIf.ModificationState.STATE_SERVER_MODIFIED);

            m_msoModel.setLocalUpdateMode();
            updateModeSet = true;
            event1.setDTG(121215L);
            IPOIIf pui = poiList.createPOI();
            assertEquals(poiList.size(), 1);

            IBriefingIf briefing = briefingList.createBriefing();
            assertEquals(briefingList.size(), 1);

            assignment.addAssignmentFinding(pui);
            assertEquals(assignment.getAssignmentFindingsItems().size(), 1);

            assignment.setAssignmentBriefing(briefing);
            assertNotNull(assignment.getAssignmentBriefing());

            IEventIf event2 = eventLog.createEvent(Calendar.getInstance());
            event2.setDTG(131313L);
            assertEquals(timeLine.size(), 2);
            ITaskIf task1 = taskListImpl.createTask(Calendar.getInstance());
            task1.setDTG(121313L);
            assertEquals(timeLine.size(), 3);
            assertTrue(isSorted(timeLine));

            assertEquals(event1.getCalendarState(), IMsoModelIf.ModificationState.STATE_LOCAL);

            if (commit)
            {
                m_msoModel.commit();

                assertEquals(poiList.size(), 1);
                assertEquals(briefingList.size(), 1);
                assertEquals(assignmentList.size(), 1);
                assertEquals(assignment.getAssignmentFindingsItems().size(), 1);
                assertNotNull(assignment.getAssignmentBriefing());
                assertEquals(timeLine.size(), 3);
                assertTrue(isSorted(timeLine));
                assertEquals(event1.getDTG(), "121215");
                assertEquals(event1.getCalendarState(), IMsoModelIf.ModificationState.STATE_SERVER_ORIGINAL);
                m_msoModel.rollback();        // to assert thar rollback has no effect
                assertEquals(poiList.size(), 1);
                assertEquals(briefingList.size(), 1);
                assertEquals(assignmentList.size(), 1);
                assertEquals(assignment.getAssignmentFindingsItems().size(), 1);
                assertNotNull(assignment.getAssignmentBriefing());
                assertEquals(timeLine.size(), 3);
                assertTrue(isSorted(timeLine));
                assertEquals(event1.getDTG(), "121215");
                assertEquals(event1.getCalendarState(), IMsoModelIf.ModificationState.STATE_SERVER_ORIGINAL);
            } else
            {
                m_msoModel.rollback();

                assertEquals(poiList.size(), 0);
                assertEquals(briefingList.size(), 0);
                assertEquals(assignmentList.size(), 1);
                assertEquals(assignment.getAssignmentFindingsItems().size(), 0);
                assertNull(assignment.getAssignmentBriefing());
                assertEquals(timeLine.size(), 1);
                assertEquals(event1.getDTG(), "121212");
                assertEquals(event1.getCalendarState(), IMsoModelIf.ModificationState.STATE_SERVER_ORIGINAL);
            }
        }
        catch (MsoException e)
        {
            e.printStackTrace();
            assertTrue(false);
        }
        finally
        {
            if (updateModeSet)
            {
                m_msoModel.restoreUpdateMode();
            }
        }
    }
}
