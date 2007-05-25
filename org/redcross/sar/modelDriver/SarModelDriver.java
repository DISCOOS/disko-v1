package org.redcross.sar.modelDriver;

import org.redcross.sar.mso.data.*;
import org.redcross.sar.mso.event.IMsoCommitListenerIf;
import org.redcross.sar.mso.event.MsoEvent;
import org.redcross.sar.mso.committer.ICommitWrapperIf;
import org.redcross.sar.mso.committer.ICommittableIf;
import org.redcross.sar.mso.CommitManager;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.MsoModelImpl;
import org.redcross.sar.util.except.DuplicateIdException;
import org.redcross.sar.util.except.UnknownAttributeException;
import org.rescuenorway.saraccess.api.*;
import org.rescuenorway.saraccess.except.SaraException;
import org.rescuenorway.saraccess.model.*;

import java.util.Random;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Method;

import no.cmr.tools.Log;
import no.cmr.hrs.sar.model.Operation;
import no.cmr.hrs.sar.model.SarObjectImpl;

/**
 *   For documentation, see {@link  IModelDriverIf}
 */
public class SarModelDriver implements IModelDriverIf, IMsoCommitListenerIf
{
   Random m_rand = new Random(89652467667623L);
   SarAccessService sarSvc;
   SarOperation sarOperation=null;


    public SarModelDriver() {
        setUpService();
    }

    public IMsoObjectIf.IObjectIdIf makeObjectId()
   {
      return new AbstractMsoObject.ObjectId(sarSvc.getSession().createInstanceId());
   }

    public SarAccessService getSarSvc() {
        return sarSvc;
    }

    public void initiate()
   {

//      //Hent ut hendelse fra Sara dersom slik eksisterer og map denne mot ny modell instans
//      SarOperations sarOpers=sarSvc.getSession().getOperations();
//      SarOperation oper=null;
//      if(sarOpers.getOperations().size()==0)
//      {
//         //Opprett ny hendelse
//         //sarSvc.getSession().createNewOperation()
//          //TODO avklar
//           //oper=new Operation("TestBus","12121212",Operation.STATE_ACTIVE, System.currentTimeMillis());
//          //createMsoOperation(oper);
//         sarSvc.getSession().createNewOperation("MSO",true);
//      }
//      else
//      {
//         //TODO avklar ang. hendelser
//         oper=sarOpers.getOperations().get(0);
//         createMsoOperation(oper);
//      }
//      sarOperation=oper;
   }

   private void createMsoOperation(SarOperation oper)
   {
      //Opprett alle Mso objekter fra hendelsens objekter
      IMsoManagerIf msoManager = MsoModelImpl.getInstance().getMsoManager();
      IOperationIf testOperation = msoManager.getOperation();
      if (testOperation == null)
      {
         try
         {
            testOperation = msoManager.createOperation("2007-TEST", oper.getID());
             sarOperation=oper;
            MsoModelImpl.getInstance().restoreUpdateMode();
             //TODO Opprett of map inn data

         }
         catch (DuplicateIdException e) // shall not happen
         {
            e.printStackTrace();
         }
      }
      else
      {
         //Hendelse er allerede opprettet, hva nå
      }
   }

   public void handleMsoCommitEvent(MsoEvent.Commit e)
   {
      //Iterer gjennom objektene, sjekk type og oppdater sara etter typen
      ICommitWrapperIf wrapper = (ICommitWrapperIf) e.getSource();
      List<ICommittableIf.ICommitObjectIf> objectList = wrapper.getObjects();
      for(ICommittableIf.ICommitObjectIf ico:objectList)
      {
         //IF created, create SARA object
         if(ico.getType().equals(CommitManager.CommitType.COMMIT_CREATED))
         {
            SarObject so=createSaraObject(ico);
             so.createNewOut();
         }
         else if(ico.getType().equals(CommitManager.CommitType.COMMIT_MODIFIED))
         {
            //if modified, modify Saraobject.
            updateSaraObject(ico);
         }
         else if(ico.getType().equals(CommitManager.CommitType.COMMIT_DELETED))
         {
            //if deleted remove Sara object
            deleteSaraObject(ico);
         }
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

   private SarObject createSaraObject(ICommittableIf.ICommitObjectIf commitObject)
   {
      IMsoObjectIf msoObj=commitObject.getObject();
      msoObj.getMsoClassCode();
      //Finn Saras mappede objekttype
      SarSession sarSess=sarSvc.getSession();
      String className=msoObj.getClass().getName();
      if(className.indexOf("Impl")>0)
      {
         className=className.substring(26,className.indexOf("Impl"));
      }
      SarObject sbo= (SarObject)sarSess.createInstance(className,SarBaseObjectFactory.TYPE_OBJECT,msoObj.getObjectId());

      //TODO sett attributter og tilordne til hendelse
      sbo.setOperation(sarOperation);
       updateSaraObject(sbo, commitObject.getObject(), false);
      //Opprett instans av av denne og distribuer
       return sbo;
   }

   private void updateSaraObject(ICommittableIf.ICommitObjectIf commitObject)
   {
       SarObject soi=sarOperation.getSarObject(commitObject.getObject().getObjectId());
       updateSaraObject(soi, commitObject.getObject(), true);
   }

   private void updateSaraObject(SarObject sbo, IMsoObjectIf msoObj, boolean submitChanges)
   {
      SarSession sarSess=sarSvc.getSession();
       Map attrMap=msoObj.getAttributes();
       Map relMap=msoObj.getReferenceObjects();
       List<SarBaseObject> objs=sbo.getObjects();
       //TODO legg inn begincommit
       sarSvc.getSession().beginCommit(sarOperation.getID());
       for(SarBaseObject so:objs)
       {
          try
          {
             if(so instanceof SarFact)
             {
                //Map fact direkte mot attributtverdi
                String attrName=((SarFact)so).getLabel();
                IAttributeIf msoAttr=(IAttributeIf)attrMap.get(attrName.toLowerCase());
                SarMsoMapper.mapMsoAttrToSarFact((SarFact)so,msoAttr,submitChanges);
             }
             else if(so instanceof SarObject)
             {
                String objName=((SarObject)so).getName();
                IMsoObjectIf refAttr=(IMsoObjectIf)relMap.get(objName);
                if(refAttr!=null)
                {
                   updateSaraObject((SarObject)so,refAttr,submitChanges);
                }
             }
          }
          catch (Exception e)
          {
             Log.warning("Unable to map to Sara :(sarObj:"+so.getValueAsString()+",msoObj:"+msoObj.getClass().getName() +e.getMessage());
          }
       }
       if(submitChanges)
       {
            sarSess.commit(sarOperation.getID());
       }

   }

   private void deleteSaraObject(ICommittableIf.ICommitObjectIf commitObject)
   {
      //Finn mappet objekt
      //Send slette melding
   }

   private void setUpService()
   {
      sarSvc = SarAccessService.getInstance();
      Credentials creds = new UserPasswordCredentials("Operatør", "Operatør");
      SarSession s = null;
      try
      {
         s = sarSvc.createSession(creds);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }

      sarSvc.getSession().AddChangeListener(new SaraChangeListener()
      {

         public void saraChanged(SaraChangeEvent change)
         {
            //TODO: oppdater MsoModell
            if(change.getChangeType()==SaraChangeEvent.TYPE_ADD)
            {
               if(change.getSource() instanceof SarOperation)
               {
                  createMsoOperation((SarOperation)change.getSource());
               }
               else if (change.getSource() instanceof SarObject)
               {
                  addMsoObject((SarObject)change.getSource());
               }

               //TODO implementer for de andre objekttypene fact og object
            }
         }

         public void saraException(SaraException sce)
         {
            //TODO videresend exception
         }
      });
      try
      {
         sarSvc.startRecvMessages();
      }
      catch (Exception e)
      {
        Log.warning(e.getMessage());
      }
   }

    protected void addMsoObject(SarObject sarObject)
    {
        String name="";
        //name=sarObject.getName();
        IMsoManagerIf msoMgr = MsoModelImpl.getInstance().getMsoManager();

        IMsoObjectIf msoObj=null;
        //Bruker reflection og navn til  å opprtette masomanagermetode ok kall createXXX foroppretting
        //bruk den med idparameter
        //tilordne msoObj fra createmetode
        String methodName="create"+sarObject.getName();

        Method m=null;
        try {
            m=msoMgr.getClass().getMethod(methodName,IMsoObjectIf.IObjectIdIf.class);
            m.setAccessible(true);
            IMsoObjectIf.IObjectIdIf id=new AbstractMsoObject.ObjectId(sarObject.getID());
            msoObj =(IMsoObjectIf) m.invoke(msoMgr, new Object[]{id});

            // Handle any exceptions thrown by method to be invoked.
            }
            catch (Exception x)
            {
            Log.warning(x.getMessage());

        }

            setMsoObjectValues(sarObject, msoObj);


        }

      public static void setMsoObjectValues(SarObject sarObject, IMsoObjectIf msoObj)
      {
         for(SarBaseObject fact:sarObject.getObjects())
         {
            if(fact instanceof SarFact)
            {
               try
               {
                  Map attrs=msoObj.getAttributes();
                   AttributeImpl attr= (AttributeImpl) attrs.get(((SarFact)fact).getLabel().toLowerCase());
                  if(attr!=null)
                  {
                     SarMsoMapper.mapSarFactToMsoAttr(attr,(SarFact)fact);
                  }
                  //msoObj.setAttribute(((SarFact)fact).getLabel().toLowerCase(),((SarFact)fact).getValue());
               }
//               catch (UnknownAttributeException e)
//               {
//                  Log.printStackTrace("Unable to map "+sarObject.getName());
//               }
               catch(Exception npe)
               {
                   Log.warning("Attr not found "+((SarFact)fact).getLabel()+" for msoobj "+msoObj.getMsoClassCode()+"\n"+npe.getMessage());
               }
               //TODO implementer
            }
            else
            {
               //TODO handle internal object attributes
            }
         }

      }

   }