package org.redcross.sar.mso;

import org.redcross.sar.mso.data.*;
import org.redcross.sar.util.except.DuplicateIdException;
import org.redcross.sar.util.except.IllegalOperationException;
import org.redcross.sar.util.except.MsoException;
import org.redcross.sar.util.except.IllegalMsoArgumentException;

import java.util.Calendar;

/**
 * Interface for managing Mso objects
 */
public interface IMsoManagerIf
{
    /**
     * ClassCodes .
     */
    enum MsoClassCode
    {
        CLASSCODE_NOCLASS,
        CLASSCODE_OPERATION,
        CLASSCODE_CMDPOST,
        CLASSCODE_HYPOTHESIS,
        CLASSCODE_BRIEFING,
        CLASSCODE_SUBJECT,
        CLASSCODE_INTELLIGENCE,
        CLASSCODE_POI,
        CLASSCODE_TRACK,
        CLASSCODE_ROUTE,
        CLASSCODE_CALLOUT,
        CLASSCODE_PERSONNEL,
        CLASSCODE_UNIT,
        CLASSCODE_SKETCH,
        CLASSCODE_EQUIPMENT,
        CLASSCODE_ASSIGNMENT,
        CLASSCODE_AREA,
        CLASSCODE_SEARCHAREA,
        CLASSCODE_OPERATIONAREA,
        CLASSCODE_TASK,
        CLASSCODE_CHECKPOINT,
        CLASSCODE_EVENT,
        CLASSCODE_MESSAGE,
        CLASSCODE_FORECAST,
        CLASSCODE_ENVIRONMENT,
        CLASSCODE_DATASOURCE
    }

    /**
     * Create a new {@link org.redcross.sar.mso.data.OperationImpl OperationImpl} object.
     * The Big Bang that creates the Universe of MSO Structures (Galaxes) and Objects (stars).
     *
     * @return The created OperationImpl object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if an Operation object (Universe) already exists.
     */
    public OperationImpl createOperation() throws DuplicateIdException;

    /**
     * Get the OperationImpl object
     *
     * @return The Operation object, null if no object is defined
     */
    public OperationImpl getOperation();

    /**
     * Create a new {@link org.redcross.sar.mso.data.ICmdPostIf ICmdPostIf} object and add it to the collection of OperationImpl objects.
     *
     * @return The created CmdPost object, with an empty MSO structure.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if a CmdPost object already exists.
     */
    public ICmdPostIf createCmdPost() throws DuplicateIdException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.ICmdPostIf ICmdPostIf} object and add it to the collection of OperationImpl objects.
     *
     * @param anObjectId The Object id
     * @return The created CmdPost object, with an empty MSO structure.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if a CmdPost object already exists.
     */
    public ICmdPostIf createCmdPost(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    /**
     * Get (Unique) KO object
     *
     * @return The KO object
     */
    public ICmdPostIf getCmdPost();

    /**
     * Get (Unique) KO object as an {@link IHierarchicalUnitIf} object
     *
     * @return The KO object
     */
    public IHierarchicalUnitIf getCmdPostUnit();

    /**
     * Remove an object from the data structure.
     *
     * @param aMsoObject The object to remove.
     * @return <code>true</code> if the object could be deleted, <code>false</code> oterhwise
     * @throws org.redcross.sar.util.except.MsoNullPointerException
     *          if parameter is null.
     */
    public boolean remove(IMsoObjectIf aMsoObject) throws MsoException;

    /**
     * Might not be needed.
     */
    public void commit();

    /**
     * Perform local commit on all objects in the model.
     */
    public void commitLocal();

    /**
     * Perform rollback on all objects in the model.
     */
    public void rollback();

    public IAreaIf createArea();

    public IAreaIf createArea(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    public IAssignmentIf createAssignment();

    public IAssignmentIf createAssignment(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    public IBriefingIf createBriefing();

    public IBriefingIf createBriefing(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    public ICalloutIf createCallout();

    public ICalloutIf createCallout(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    public ICheckpointIf createCheckPoint();

    public ICheckpointIf createCheckPoint(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    public IDataSourceIf createDataSource();

    public IDataSourceIf createDataSource(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    public IEnvironmentIf createEnvironment(Calendar aCalendar, String aText);

    public IEnvironmentIf createEnvironment(String aDTG, String aText) throws IllegalMsoArgumentException;

    public IEnvironmentIf createEnvironment(long aDTG, String aText) throws IllegalMsoArgumentException;

    public IEnvironmentIf createEnvironment(IMsoObjectIf.IObjectIdIf anObjectId, Calendar aCalendar, String aText) throws DuplicateIdException;

    public IEnvironmentIf createEnvironment(IMsoObjectIf.IObjectIdIf anObjectId, String aDTG, String aText) throws DuplicateIdException, IllegalMsoArgumentException;

    public IEnvironmentIf createEnvironment(IMsoObjectIf.IObjectIdIf anObjectId, long aDTG, String aText) throws DuplicateIdException, IllegalMsoArgumentException;

    public IEquipmentIf createEquipment();

    public IEquipmentIf createEquipment(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    public IForecastIf createForecast(String aText);

    public IForecastIf createForecast(Calendar aCalendar, String aText);

    public IForecastIf createForecast(String aDTG, String aText) throws IllegalMsoArgumentException;

    public IForecastIf createForecast(long aDTG, String aText) throws IllegalMsoArgumentException;

    public IForecastIf createForecast(IMsoObjectIf.IObjectIdIf anObjectId, String aText) throws DuplicateIdException;

    public IForecastIf createForecast(IMsoObjectIf.IObjectIdIf anObjectId, Calendar aCalendar, String aText) throws DuplicateIdException;

    public IForecastIf createForecast(IMsoObjectIf.IObjectIdIf anObjectId, String aDTG, String aText) throws DuplicateIdException, IllegalMsoArgumentException;

    public IForecastIf createForecast(IMsoObjectIf.IObjectIdIf anObjectId, long aDTG, String aText) throws DuplicateIdException, IllegalMsoArgumentException;

    public IHypothesisIf createHypothesis();

    public IHypothesisIf createHypothesis(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    public IIntelligenceIf createIntelligence();

    public IIntelligenceIf createIntelligence(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    public IOperationAreaIf createOperationArea();

    public IOperationAreaIf createOperationArea(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    public IPersonnelIf createPersonnel(String aPersonellId);

    public IPersonnelIf createPersonnel(IMsoObjectIf.IObjectIdIf anObjectId, String aPersonellId) throws DuplicateIdException;

    public IPOIIf createPOI();

    public IPOIIf createPOI(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    public IRouteIf createRoute();

    public IRouteIf createRoute(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    public ISearchAreaIf createSearchArea();

    public ISearchAreaIf createSearchArea(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    public ISketchIf createSketch();

    public ISketchIf createSketch(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    public ISubjectIf createSubject(String aName, String aDescription);

    public ISubjectIf createSubject(IMsoObjectIf.IObjectIdIf anObjectId, String aName, String aDescription) throws DuplicateIdException;

    public ITaskIf createTask(Calendar aCalendar);

    public ITaskIf createTask(IMsoObjectIf.IObjectIdIf anObjectId, Calendar aCalendar) throws DuplicateIdException;

    public ITrackIf createTrack();

    public ITrackIf createTrack(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    public VehicleImpl createVehicle(long aNumber, String aKjennetegn, int aSpeed);

    public VehicleImpl createVehicle(IMsoObjectIf.IObjectIdIf anObjectId, long aNumber, String aKjennetegn, int aSpeed) throws DuplicateIdException;
}
