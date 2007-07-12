package org.redcross.sar.modelDriver;

import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.app.IDiskoApplication;

/**
 *  Specification of the Model Driver API.
 */
public interface IModelDriverIf
{
    /**
     * Make an assumably unique Object Id
     * @return  The Object ID
     */
    public IMsoObjectIf.IObjectIdIf makeObjectId();

   /**
    * This method checks to see if a SaraOperation (Hendelse) is running and initiates a mapping
    * between SaraOperation (Hendelse) and a running MsoModel
    */
   public void initiate();

   /**
    *
    * @return true if integration is loaded otherwise false
    */
   public boolean isInitiated();

   /**
    * Fetch a list of currently active operations with name and id
    * @return a List of the currentlig active operations as String[2]:{Name,Id} pairs
    */
   public java.util.List<String[]> getActiveOperations();

   /**
    *   Sets an operation as active using the id of the operation
    * @param operationid id of the operation to use
    * @return value indicating if the operation is set as active
    */
   public boolean setActiveOperation(String operationid);

   /**
    * Finish the currently active operation
    */
   public void finishActiveOperation();

   /**
    * Initiate a new operation, and set it as active
    */
   public void createNewOperation();

   /**
    * Merge a other operation with the currently active one and finishes the other when merge is completed
    */
   public void merge();

    /**
     * Sets the DiscoApplication
     */
    public void setDiskoApplication(IDiskoApplication diskoApp);

    void shutDown();
}
