package org.redcross.sar.mso;

import org.redcross.sar.mso.data.*;
import org.redcross.sar.util.except.DuplicateIdException;
import org.redcross.sar.util.except.MsoNullPointerException;
import org.redcross.sar.util.except.IllegalMsoArgumentException;
import org.redcross.sar.util.mso.*;


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
     * Create a new {@link org.redcross.sar.mso.data.IOperationIf} object.
     * The Big Bang that creates the Universe of MSO Structures (Galaxes) and Objects (stars).
     *
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if an Operation object (Universe) already exists.
     */
    public IOperationIf createOperation() throws DuplicateIdException;

    /**
     * Get the {@link org.redcross.sar.mso.data.IOperationIf} object
     *
     * @return The Operation object, null if no object is defined
     */
    public IOperationIf getOperation();

    /**
     * Create a new {@link org.redcross.sar.mso.data.ICmdPostIf} object and add it to the collection of CmdPost objects.
     *
     * @return The created object, with an empty MSO structure.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if a CmdPost object already exists.
     */
    public ICmdPostIf createCmdPost() throws DuplicateIdException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.ICmdPostIf} object and add it to the collection of CmdPost objects.
     *
     * @param anObjectId The Object id
     * @return The created object, with an empty MSO structure.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if a CmdPost object already exists.
     */
    public ICmdPostIf createCmdPost(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    /**
     * Get (Unique) {@link org.redcross.sar.mso.data.ICmdPostIf} object
     *
     * @return The {@link org.redcross.sar.mso.data.ICmdPostIf} object
     */
    public ICmdPostIf getCmdPost();

    /**
     * Get (Unique) {@link org.redcross.sar.mso.data.ICmdPostIf} object as an {@link IHierarchicalUnitIf} object
     *
     * @return The {@link IHierarchicalUnitIf} object
     */
    public IHierarchicalUnitIf getCmdPostUnit();

    /**
     * Remove an object from the data structure.
     *
     * @param aMsoObject The object to remove.
     * @return <code>true</code> if the object could be deleted, <code>false</code> otherwise
     * @throws org.redcross.sar.util.except.MsoNullPointerException
     *          if parameter is null.
     */
    public boolean remove(IMsoObjectIf aMsoObject) throws MsoNullPointerException;

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

    /**
     * Create a new {@link org.redcross.sar.mso.data.IAreaIf} object and add it to the collection of Area objects.
     *
     * @return The created object.
     */
    public IAreaIf createArea();

    /**
     * Create a new {@link org.redcross.sar.mso.data.IAreaIf} object and add it to the collection of Area objects.
     *
     * @param anObjectId The Object id
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if a Area object already exists with the same ObjectId value.
     */
    public IAreaIf createArea(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.IAssignmentIf} object and add it to the collection of Assignment objects.
     *
     * @return The created object.
     */
    public IAssignmentIf createAssignment();

    /**
     * Create a new {@link org.redcross.sar.mso.data.IAssignmentIf} object and add it to the collection of Assignment objects.
     *
     * @param anObjectId The Object id
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if an Assignment object already exists with the same ObjectId value.
     */
    public IAssignmentIf createAssignment(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.IBriefingIf} object and add it to the collection of Briefing objects.
     *
     * @return The created object.
     */
    public IBriefingIf createBriefing();

    /**
     * Create a new {@link org.redcross.sar.mso.data.IBriefingIf} object and add it to the collection of Briefing objects.
     *
     * @param anObjectId The Object id
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if a Briefing object already exists with the same ObjectId value.
     */
    public IBriefingIf createBriefing(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.ICalloutIf} object and add it to the collection of Callout objects.
     *
     * @return The created object.
     */
    public ICalloutIf createCallout();

    /**
     * Create a new {@link org.redcross.sar.mso.data.ICalloutIf} object and add it to the collection of Callout objects.
     *
     * @param anObjectId The Object id
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if a Callout object already exists with the same ObjectId value.
     */
    public ICalloutIf createCallout(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.ICheckpointIf} object and add it to the collection of Checkpoint objects.
     *
     * @return The created object.
     */
    public ICheckpointIf createCheckpoint();

    /**
     * Create a new {@link org.redcross.sar.mso.data.ICheckpointIf} object and add it to the collection of Checkpoint objects.
     *
     * @param anObjectId The Object id
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if a Checkpoint object already exists with the same ObjectId value.
     */
    public ICheckpointIf createCheckpoint(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.IDataSourceIf} object and add it to the collection of DataSource objects.
     *
     * @return The created object.
     */
    public IDataSourceIf createDataSource();

    /**
     * Create a new {@link org.redcross.sar.mso.data.IDataSourceIf} object and add it to the collection of DataSource objects.
     *
     * @param anObjectId The Object id
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if a DataSource object already exists with the same ObjectId value.
     */
    public IDataSourceIf createDataSource(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.IEnvironmentIf} object and add it to the collection of Environment objects.
     *
     * @param aCalendar The time from which the information is valid.
     * @return The created object.
     */
    public IEnvironmentIf createEnvironment(Calendar aCalendar, String aText);

    /**
     * Create a new {@link org.redcross.sar.mso.data.IEnvironmentIf} object and add it to the collection of Environment objects.
     *
     * @param aDTG The time (in DateTimeGroup format) from which the information is valid.
     * @return The created object.
     * @throws org.redcross.sar.util.except.IllegalMsoArgumentException
     *          if paramter aDTG holds an illegal DateTimeGroup value.
     */
    public IEnvironmentIf createEnvironment(String aDTG, String aText) throws IllegalMsoArgumentException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.IEnvironmentIf} object and add it to the collection of Environment objects.
     *
     * @return The created object.
     * @throws org.redcross.sar.util.except.IllegalMsoArgumentException
     *          if paramter aDTG holds an illegal DateTimeGroup value.
     */
    public IEnvironmentIf createEnvironment(long aDTG, String aText) throws IllegalMsoArgumentException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.IEnvironmentIf} object and add it to the collection of Environment objects.
     *
     * @param anObjectId The Object id
     * @param aCalendar The time from which the information is valid.
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if an Environment object already exists with the same ObjectId value.
     */
    public IEnvironmentIf createEnvironment(IMsoObjectIf.IObjectIdIf anObjectId, Calendar aCalendar, String aText) throws DuplicateIdException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.IEnvironmentIf} object and add it to the collection of Environment objects.
     *
     * @param aDTG The time (in DateTimeGroup format) from which the information is valid.
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if an Environment object already exists with the same ObjectId value.
     * @throws org.redcross.sar.util.except.IllegalMsoArgumentException
     *          if paramter aDTG holds an illegal DateTimeGroup value.
     */
    public IEnvironmentIf createEnvironment(IMsoObjectIf.IObjectIdIf anObjectId, String aDTG, String aText) throws DuplicateIdException, IllegalMsoArgumentException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.IEnvironmentIf} object and add it to the collection of Environment objects.
     *
     * @param aDTG The time (in DateTimeGroup format) from which the information is valid.
     * @param anObjectId The Object id
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if an Environment object already exists with the same ObjectId value.
     * @throws org.redcross.sar.util.except.IllegalMsoArgumentException
     *          if paramter aDTG holds an illegal DateTimeGroup value.
     */
    public IEnvironmentIf createEnvironment(IMsoObjectIf.IObjectIdIf anObjectId, long aDTG, String aText) throws DuplicateIdException, IllegalMsoArgumentException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.IEquipmentIf} object and add it to the collection of Equipment objects.
     *
     * @return The created object.
     */
    public IEquipmentIf createEquipment();

    /**
     * Create a new {@link org.redcross.sar.mso.data.IEquipmentIf} object and add it to the collection of Equipment objects.
     *
     * @param anObjectId The Object id
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if an Equipment object already exists with the same ObjectId value.
     */
    public IEquipmentIf createEquipment(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.IForecastIf} object and add it to the collection of Forecast objects.
     *
     * @param aCalendar The time from which the information is valid.
     * @param aText A forecast text
     * @return The created object.
     */
    public IForecastIf createForecast(Calendar aCalendar, String aText);

    /**
     * Create a new {@link org.redcross.sar.mso.data.IForecastIf} object and add it to the collection of Forecast objects.
     *
     * @param aDTG The time (in DateTimeGroup format) from which the information is valid.
     * @param aText A forecast text
     * @return The created object.
     * @throws org.redcross.sar.util.except.IllegalMsoArgumentException
     *          if paramter aDTG holds an illegal DateTimeGroup value.
     */
    public IForecastIf createForecast(String aDTG, String aText) throws IllegalMsoArgumentException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.IForecastIf} object and add it to the collection of Forecast objects.
     *
     * @param aDTG The time (in DateTimeGroup format) from which the information is valid.
     * @param aText A forecast text
     * @return The created object.
     * @throws org.redcross.sar.util.except.IllegalMsoArgumentException
     *          if paramter aDTG holds an illegal DateTimeGroup value.
     */
    public IForecastIf createForecast(long aDTG, String aText) throws IllegalMsoArgumentException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.IForecastIf} object and add it to the collection of Forecast objects.
     *
     * @param anObjectId The Object id
     * @param aText A forecast text
     * @param aCalendar The time from which the information is valid.
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if a Forecast object already exists with the same ObjectId value.
     */
    public IForecastIf createForecast(IMsoObjectIf.IObjectIdIf anObjectId, Calendar aCalendar, String aText) throws DuplicateIdException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.IForecastIf} object and add it to the collection of Forecast objects.
     *
     * @param aDTG The time (in DateTimeGroup format) from which the information is valid.
     * @param aText A forecast text
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if a Forecast object already exists with the same ObjectId value.
     * @throws org.redcross.sar.util.except.IllegalMsoArgumentException
     *          if paramter aDTG holds an illegal DateTimeGroup value.
     */
    public IForecastIf createForecast(IMsoObjectIf.IObjectIdIf anObjectId, String aDTG, String aText) throws DuplicateIdException, IllegalMsoArgumentException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.IForecastIf} object and add it to the collection of Forecast objects.
     *
     * @param anObjectId The Object id
     * @param aDTG The time (in DateTimeGroup format) from which the information is valid.
     * @param aText A forecast text
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if a Forecast object already exists with the same ObjectId value.
     * @throws org.redcross.sar.util.except.IllegalMsoArgumentException
     *          if paramter aDTG holds an illegal DateTimeGroup value.
     */
    public IForecastIf createForecast(IMsoObjectIf.IObjectIdIf anObjectId, long aDTG, String aText) throws DuplicateIdException, IllegalMsoArgumentException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.IHypothesisIf} object and add it to the collection of Hypothesis objects.
     *
     * @return The created object.
     */
    public IHypothesisIf createHypothesis();

    /**
     * Create a new {@link org.redcross.sar.mso.data.IHypothesisIf} object and add it to the collection of Hypothesis objects.
     *
     * @param anObjectId The Object id
     * @param aNumber The serial number
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if a Hypothesis object already exists with the same ObjectId value.
     */
    public IHypothesisIf createHypothesis(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber) throws DuplicateIdException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.IIntelligenceIf} object and add it to the collection of Intelligence objects.
     *
     * @return The created object.
     */
    public IIntelligenceIf createIntelligence();

    /**
     * Create a new {@link org.redcross.sar.mso.data.IIntelligenceIf} object and add it to the collection of Intelligence objects.
     *
     * @param anObjectId The Object id
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if an Intelligence object already exists with the same ObjectId value.
     */
    public IIntelligenceIf createIntelligence(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.IOperationAreaIf} object and add it to the collection of OperationArea objects.
     *
     * @return The created object.
     */
    public IOperationAreaIf createOperationArea();

    /**
     * Create a new {@link org.redcross.sar.mso.data.IOperationAreaIf} object and add it to the collection of OperationArea objects.
     *
     * @param anObjectId The Object id
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if an OperationArea object already exists with the same ObjectId value.
     */
    public IOperationAreaIf createOperationArea(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.IPersonnelIf} object and add it to the collection of Personnel objects.
     *
     * @return The created object.
     */
    public IPersonnelIf createPersonnel(String aPersonellId);

    /**
     * Create a new {@link org.redcross.sar.mso.data.IPersonnelIf} object and add it to the collection of Personnel objects.
     *
     * @param anObjectId The Object id
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if a Personnel object already exists with the same ObjectId value.
     */
    public IPersonnelIf createPersonnel(IMsoObjectIf.IObjectIdIf anObjectId, String aPersonellId) throws DuplicateIdException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.IPOIIf} object and add it to the collection of POI objects.
     *
     * @return The created object.
     */
    public IPOIIf createPOI();

    /**
     * Create a new {@link org.redcross.sar.mso.data.IPOIIf} object and add it to the collection of POI objects.
     *
     * @param anObjectId The Object id
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if a POI object already exists with the same ObjectId value.
     */
    public IPOIIf createPOI(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.IPOIIf} object and add it to the collection of POI objects.
     *
     * @param aType The POI type
     * @param aPosition The POI position
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if a POI object already exists with the same ObjectId value.
     */
    public IPOIIf createPOI(IPOIIf.POIType aType, Position aPosition);

    /**
     * Create a new {@link org.redcross.sar.mso.data.IPOIIf} object and add it to the collection of POI objects.
     *
     * @param anObjectId The Object id
     * @param aType The POI type
     * @param aPosition The POI position
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if a POI object already exists with the same ObjectId value.
     */
    public IPOIIf createPOI(IMsoObjectIf.IObjectIdIf anObjectId, IPOIIf.POIType aType, Position aPosition) throws DuplicateIdException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.IRouteIf} object and add it to the collection of Route objects.
     *
     * @return The created object.
     */
    public IRouteIf createRoute(Route aRoute);

    /**
     * Create a new {@link org.redcross.sar.mso.data.IRouteIf} object and add it to the collection of Route objects.
     *
     * @param anObjectId The Object id
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if a Route object already exists with the same ObjectId value.
     */
    public IRouteIf createRoute(IMsoObjectIf.IObjectIdIf anObjectId, Route aRoute) throws DuplicateIdException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.ISearchAreaIf} object and add it to the collection of SearchArea objects.
     *
     * @return The created object.
     */
    public ISearchAreaIf createSearchArea();

    /**
     * Create a new {@link org.redcross.sar.mso.data.ISearchAreaIf} object and add it to the collection of SearchArea objects.
     *
     * @param anObjectId The Object id
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if a SearchArea object already exists with the same ObjectId value.
     */
    public ISearchAreaIf createSearchArea(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.ISketchIf} object and add it to the collection of Sketch objects.
     *
     * @return The created object.
     */
    public ISketchIf createSketch();

    /**
     * Create a new {@link org.redcross.sar.mso.data.ISketchIf} object and add it to the collection of Sketch objects.
     *
     * @param anObjectId The Object id
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if a Sketch object already exists with the same ObjectId value.
     */
    public ISketchIf createSketch(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.ISubjectIf} object and add it to the collection of Subject objects.
     *
     * @return The created object.
     */
    public ISubjectIf createSubject(String aName, String aDescription);

    /**
     * Create a new {@link org.redcross.sar.mso.data.ISubjectIf} object and add it to the collection of Subject objects.
     *
     * @param anObjectId The Object id
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if a Subject object already exists with the same ObjectId value.
     */
    public ISubjectIf createSubject(IMsoObjectIf.IObjectIdIf anObjectId, String aName, String aDescription) throws DuplicateIdException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.ITaskIf} object and add it to the collection of Task objects.
     *
     * @param aCalendar Task due time
     * @return The created object.
     */
    public ITaskIf createTask(Calendar aCalendar);

    /**
     * Create a new {@link org.redcross.sar.mso.data.ITaskIf} object and add it to the collection of Task objects.
     *
     * @param anObjectId The Object id
     * @param aCalendar Task due time
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if a Task object already exists with the same ObjectId value.
     */
    public ITaskIf createTask(IMsoObjectIf.IObjectIdIf anObjectId, Calendar aCalendar) throws DuplicateIdException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.ITrackIf} object and add it to the collection of Track objects.
     *
     * @return The created object.
     */
    public ITrackIf createTrack();

    /**
     * Create a new {@link org.redcross.sar.mso.data.ITrackIf} object and add it to the collection of Track objects.
     *
     * @param anObjectId The Object id
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if a Track object already exists with the same ObjectId value.
     */
    public ITrackIf createTrack(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    /**
     * Create a new {@link org.redcross.sar.mso.data.IVehicleIf} object and add it to the collection of Unit objects.
     *
     * @param anIdentifier The unit's identifier (if needed here??)
     * @return The created object.
     */
    public IVehicleIf createVehicle(String anIdentifier);

    /**
     * Create a new {@link org.redcross.sar.mso.data.IVehicleIf} object and add it to the collection of Unit objects.
     *
     * @param anObjectId The Object id
     * @param aNumber The unit number
     * @param anIdentifier The unit's identifier (if needed here??)
     * @return The created object.
     * @throws org.redcross.sar.util.except.DuplicateIdException
     *          if a Unit object already exists with the same ObjectId value.
     */
    public IVehicleIf createVehicle(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber, String anIdentifier) throws DuplicateIdException;
}
