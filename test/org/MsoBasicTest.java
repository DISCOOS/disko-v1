package org;

import static org.junit.Assert.*;
import org.junit.Test;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.MsoModelImpl;
import org.redcross.sar.mso.data.*;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.util.except.MsoException;
import org.redcross.sar.util.mso.Selector;

import java.util.ArrayList;
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
        m_msoModel.setRemoteUpdateMode();
        msoModelCreate();
        m_msoModel.restoreUpdateMode();
        msoUpdateModes();
        msoRollback();
        m_msoModel.rollback();
        msoUnits();
        m_msoModel.rollback();
        msoLists();
        m_msoModel.rollback();
        msoSubLists();
        m_msoModel.rollback();
        msoReferences();
        m_msoModel.rollback();
        msoTimeLine();
    }

    public void msoModelCreate()
    {
        try
        {
            IOperationIf operationA = m_msoManager.createOperation();
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
            String statname = cmdstat.name();
            cmdPostB.setStatus(ICmdPostIf.CmdPostStatus.RELEASED);
            cmdstat = Enum.valueOf(ICmdPostIf.CmdPostStatus.class, statname);
            cmdPostA.setStatus("OPERATING");
            cmdstat = cmdPostB.getStatus();
            cmdPostA.setStatus(cmdstat);

        }
        catch (MsoException e)
        {
            assertTrue(false);
        }
    }

    public void msoUpdateModes()
    {
        assertEquals(m_msoModel.getUpdateMode(), IMsoModelIf.UpdateMode.LOCAL_UPDATE_MODE);
        m_msoModel.setRemoteUpdateMode();
        m_msoModel.restoreUpdateMode();
        assertEquals(m_msoModel.getUpdateMode(), IMsoModelIf.UpdateMode.LOCAL_UPDATE_MODE);
    }

    public void msoRollback()
    {
        try
        {
            ICmdPostIf cmdPost = m_msoManager.getCmdPost();
            System.out.println(cmdPost.toString());

            m_msoModel.setRemoteUpdateMode();
            IAssignmentListIf miList = cmdPost.getAssignmentList();
            System.out.println("MissionListSize: " + miList.size());

            IPOIListIf puiList = cmdPost.getPOIList();
            System.out.println("PUIListSize: " + puiList.size());

            IBriefingListIf _5poList = cmdPost.getBriefingList();
            System.out.println("5POListSize: " + _5poList.size());

            IAssignmentIf assignment = miList.createAssignment();
            System.out.println("MissionListSize: " + miList.size());

            m_msoModel.restoreUpdateMode();

            IPOIIf pui = puiList.createPOI();
            System.out.println("PUIListSize: " + puiList.size());

            IBriefingIf _5po = _5poList.createBriefing();
            System.out.println("5POListSize: " + _5poList.size());

            System.out.println("Add Pui");
            assignment.addAssignmentFindings(pui);

            System.out.println("Set 5PO");
            assignment.setAssignmentBriefing(_5po);

            System.out.println("Rollback");
            m_msoModel.rollback();

            System.out.println("MissionListSize: " + miList.size());
            System.out.println("PUIListSize: " + puiList.size());
            System.out.println("5POListSize: " + _5poList.size());

        }
        catch (MsoException e)
        {
            e.printStackTrace();
            assertTrue(false);
        }
        finally
        {
            m_msoModel.restoreUpdateMode();
        }
    }


    public void msoUnits()
    {
        ICmdPostIf cmdPost = m_msoManager.getCmdPost();
        IHierarchicalUnitIf koUnit = m_msoManager.getCmdPostUnit();
        IUnitListIf unitList = cmdPost.getUnitList();
        UnitListImpl unitListImpl = (UnitListImpl) unitList;
        assertNotNull(unitList);
        m_msoModel.setRemoteUpdateMode();

        unitListImpl.clear();

        IUnitIf vUnit1 = unitList.createVehicle("V1");
        assertNotNull(vUnit1);
        assertTrue(koUnit.setSuperiorUnit(null));
        assertTrue(vUnit1.setSuperiorUnit(koUnit));
        assertFalse(koUnit.setSuperiorUnit(vUnit1));

        IUnitIf vUnit2 = unitList.createVehicle("V2");
        assertNotNull(vUnit2);
        assertTrue(vUnit2.setSuperiorUnit(koUnit));

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
        m_msoModel.restoreUpdateMode();
    }

    public void msoLists()
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
            System.out.println("Test Create vehicle 5");
            IUnitIf vUnit5 = unitList.createVehicle("V5");
            mainCount++;
            int v5Count = 1;
            assertEquals(unitList.size(), mainCount);
            assertEquals(((AbstractMsoObject) vUnit3).listenerCount(), v3Count);
            assertEquals(((AbstractMsoObject) vUnit4).listenerCount(), v4Count);

            System.out.println("Test Add vehicle 1");
            myUnitList.add(vUnit1);
            myCount++;
            v1Count++;
            System.out.println("Test Add vehicle 3");
            myUnitList.add(vUnit3);
            myCount++;
            v3Count++;
            myUnitList.add(vUnit4);
            myCount++;
            v4Count++;
            assertEquals(myUnitList.size(), myCount);
            assertEquals(((AbstractMsoObject) vUnit3).listenerCount(), v3Count);
            assertEquals(((AbstractMsoObject) vUnit4).listenerCount(), v4Count);

            System.out.println("Test Remove vehicle 3 from a list");
            myUnitList.removeReference(vUnit3);
            myCount--;
            v3Count--;
            assertEquals(myUnitList.size(), myCount);
            assertEquals(((AbstractMsoObject) vUnit3).listenerCount(), v3Count);

            System.out.println("Test Remove vehicle 3 from a list once again");
            myUnitList.removeReference(vUnit3);
            assertEquals(myUnitList.size(), myCount);
            assertEquals(((AbstractMsoObject) vUnit3).listenerCount(), v3Count);

            unitListImpl.print();
            myUnitList.print();

            System.out.println("Test Remove completely vehicle 1");
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

            unitListImpl.print();
            myUnitList.print();

            assertEquals(unitList.size(), mainCount);
            assertEquals(myUnitList.size(), myCount);
            assertEquals(((AbstractMsoObject) vUnit3).listenerCount(), v3Count);
            assertEquals(((AbstractMsoObject) vUnit5).listenerCount(), v5Count);

        }
        catch (MsoException e)
        {
            e.printStackTrace();
        }
    }

    public void msoSubLists()
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
        int mainMissionCount = 0;
        int mainPUICount = 0;
        int myCount = 0;

        try
        {
            m_msoModel.setRemoteUpdateMode();
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

            IAssignmentIf mis11 = assignmentList.createAssignment();
            vUnit1.addUnitAssignments(mis11);
            int vmCount1 = 1;
            IAssignmentIf mis12 = assignmentList.createAssignment();
            vUnit1.addUnitAssignments(mis12);
            vmCount1++;
            assertEquals(aUnit1.getUnitAssignmentsItems().size(), vmCount1);

            assertTrue(assignmentList.removeReference(mis12));
            vmCount1--;
            mis12 = null;
            assertEquals(aUnit1.getUnitAssignmentsItems().size(), vmCount1);

            IAssignmentIf mis13 = assignmentList.createAssignment();
            AssignmentImpl misImpl13 = (AssignmentImpl) mis13;
            vUnit1.addUnitAssignments(mis13);
            vmCount1++;
            assertEquals(aUnit1.getUnitAssignmentsItems().size(), vmCount1);
            assertEquals(misImpl13.listenerCount(), 2);

            IPOIIf PUI1 = puiList.createPOI();
            mis13.addAssignmentFindings(PUI1);
            int mpCount3 = 1;
            IPOIIf PUI2 = puiList.createPOI();
            mis13.addAssignmentFindings(PUI2);
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
        finally
        {
            m_msoModel.restoreUpdateMode();
        }
    }

    public void msoReferences()
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

        IBriefingListIf _5poList = cmdPost.getBriefingList();
        assertNotNull(_5poList);
        BriefingListImpl _5poListImpl = (BriefingListImpl) _5poList;
        _5poListImpl.clear();

        int mainUnitCount = 0;
        int mainMissionCount = 0;
        int main5POCount = 0;

        m_msoModel.setRemoteUpdateMode();
        IUnitIf vUnit1 = unitList.createVehicle("V1");
        mainUnitCount++;
        int v1Count = 1;
        IUnitIf vUnit2 = unitList.createVehicle("V2");
        mainUnitCount++;
        int v2Count = 1;
        IUnitIf vUnit3 = unitList.createVehicle("V3");
        mainUnitCount++;
        assertEquals(unitList.size(), mainUnitCount);
        int v3Count = 1;
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

        assertTrue(unitList.removeReference(vUnit1));
        mainUnitCount--;
        assertEquals(unitList.size(), mainUnitCount);

        IAssignmentIf mis11 = assignmentList.createAssignment();
        assertEquals(assignmentList.size(), 1);
        IBriefingIf _5po1 = _5poList.createBriefing();
        assertEquals(_5poList.size(), 1);
        assertEquals(((BriefingImpl) _5po1).listenerCount(), 1);
        mis11.setAssignmentBriefing(_5po1);
        assertEquals(((BriefingImpl) _5po1).listenerCount(), 2);
        assignmentListImpl.removeReference(mis11);
        assertEquals(((BriefingImpl) _5po1).listenerCount(), 1);
        m_msoModel.restoreUpdateMode();
    }


    public void msoTimeLine()
    {
        ICmdPostIf cmdPost = m_msoManager.getCmdPost();
        IOperationIf operation = m_msoManager.getOperation();

        IEventLogIf eventLog = cmdPost.getEventLog();
        EventLogImpl eventLogImpl = (EventLogImpl) eventLog;
        eventLogImpl.clear();

        ITaskListIf taskList = cmdPost.getTaskList();
        TaskListImpl taskListImpl = (TaskListImpl) taskList;
        taskListImpl.clear();

        ITimeLineIf timeLine = cmdPost.getTimeLine();

        try
        {
            m_msoModel.setRemoteUpdateMode();
            IEventIf event1 = eventLog.createEvent(Calendar.getInstance());
            event1.setDTG(121212L);
            assertEquals(timeLine.size(),1);
            IEventIf event2 = eventLog.createEvent(Calendar.getInstance());
            event2.setDTG(131313L);
            assertEquals(timeLine.size(),2);
            ITaskIf task1 = taskListImpl.createTask(null);
            task1.setDTG(121313L);
            assertEquals(timeLine.size(),3);

            timeLine.print();
            event1.setDTG(151212L);
            timeLine.print();

            m_msoModel.restoreUpdateMode();

            eventLog.removeReference(event2);
            assertEquals(timeLine.size(),2);

            timeLine.print();

            task1.setDTG(151213L);
            timeLine.print();


            task1.setDTG(151213L);
            timeLine.print();

            eventLog.removeReference(event1);
            assertEquals(timeLine.size(),1);

            timeLine.print();

            taskList.removeReference(task1);
            assertEquals(timeLine.size(),0);

            timeLine.print();
            System.out.println("Commit");
            m_msoModel.commit();
            timeLine.print();
            assertEquals(timeLine.size(),0);



        }
        catch (MsoException e)
        {
            e.printStackTrace();
        }
        finally
        {
            m_msoModel.restoreUpdateMode();
        }
    }

}
