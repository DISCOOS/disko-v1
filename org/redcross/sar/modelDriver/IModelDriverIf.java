package org.redcross.sar.modelDriver;

import org.redcross.sar.mso.data.IMsoObjectIf;

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
}
