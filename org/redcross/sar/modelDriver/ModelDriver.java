package org.redcross.sar.modelDriver;

import org.redcross.sar.mso.CommitManager;
import org.redcross.sar.mso.committer.ICommitWrapperIf;
import org.redcross.sar.mso.committer.ICommittableIf;
import org.redcross.sar.mso.data.AbstractMsoObject;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.event.IMsoCommitListenerIf;
import org.redcross.sar.mso.event.MsoEvent;
import org.redcross.sar.app.IDiskoApplication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 *   For documentation, see {@link  org.redcross.sar.modelDriver.IModelDriverIf}
 */
public class ModelDriver implements IModelDriverIf, IMsoCommitListenerIf
{
    Random m_rand = new Random(89652467667623L);

    public IMsoObjectIf.IObjectIdIf makeObjectId()
    {
        int rand = m_rand.nextInt(1000);
        long time = new Date().getTime();
        return new AbstractMsoObject.ObjectId(Long.toString(time)+"."+Integer.toString(rand));

    }

   public void initiate()
   {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   public boolean isInitiated()
   {
      return true;
   }

   public List<String[]> getActiveOperations()
   {
      java.util.List<String[]> list=new ArrayList();
      list.add(new String[]{"2","2"});
      return list;
   }

   public boolean setActiveOperation(String operationid)
   {
      return true;
   }

   public void finishActiveOperation()
   {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   public void createNewOperation()
   {
      //To change body of implemented methods use File | Settings | File Templates.
   }

   public void merge()
   {
      //To change body of implemented methods use File | Settings | File Templates.
   }

    public void setDiskoApplication(IDiskoApplication diskoApp)
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void shutDown()
    {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void handleMsoCommitEvent(MsoEvent.Commit e)
   {
      //Iterer gjennom objektene
        ICommitWrapperIf wrapper = (ICommitWrapperIf) e.getSource();
        List<ICommittableIf.ICommitObjectIf> objectList = wrapper.getObjects();
      for(ICommittableIf.ICommitObjectIf ico:objectList)
      {
         //IF created, create SARA object
         //if modified, modify Saraobject.
         //if deleted remove Sara object
      }

      List<ICommittableIf.ICommitReferenceIf> attrList = wrapper.getAttributeReferences();
    for(ICommittableIf.ICommitReferenceIf ico:attrList)
    {
       CommitManager.CommitType ct=ico.getType(); //CommitManager.CommitType.COMMIT_CREATED/CommitManager.CommitType.COMMIT_DELETED
       String  attName=ico.getReferenceName();
       IMsoObjectIf owner= ico.getReferringObject();
       IMsoObjectIf attribute= ico.getReferredObject();
    }

    List<ICommittableIf.ICommitReferenceIf> listList = wrapper.getListReferences();
    for(ICommittableIf.ICommitReferenceIf ico:listList)
    {
       CommitManager.CommitType ct=ico.getType(); //CommitManager.CommitType.COMMIT_CREATED/CommitManager.CommitType.COMMIT_DELETED
       String  refName=ico.getReferenceName();
       IMsoObjectIf owner= ico.getReferringObject();
       IMsoObjectIf ref= ico.getReferredObject();
    }

   }
}
