package org.redcross.sar.modelDriver;

import no.cmr.hrs.sar.model.Fact;
import no.cmr.hrs.sar.model.SarObjectImpl;
import no.cmr.hrs.sar.tools.ChangeObject;
import no.cmr.hrs.sar.tools.IDHelper;
import no.cmr.tools.Log;
import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.mso.CommitManager;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.MsoModelImpl;
import org.redcross.sar.mso.committer.ICommitWrapperIf;
import org.redcross.sar.mso.committer.ICommittableIf;
import org.redcross.sar.mso.data.*;
import org.redcross.sar.mso.event.IMsoCommitListenerIf;
import org.redcross.sar.mso.event.MsoEvent;
import org.redcross.sar.util.except.DuplicateIdException;
import org.redcross.sar.util.except.MsoNullPointerException;
import org.rescuenorway.saraccess.api.*;
import org.rescuenorway.saraccess.except.SaraException;
import org.rescuenorway.saraccess.model.*;

import javax.swing.*;
import java.lang.reflect.Method;
import java.util.*;

/**
 * For documentation, see {@link  IModelDriverIf}
 */
public class SarModelDriver implements IModelDriverIf, IMsoCommitListenerIf, SaraChangeListener
{
    boolean loadingOperation = false;
    Random m_rand = new Random(89652467667623L);
    SarAccessService sarSvc;
    SarOperation sarOperation = null;
    Map<SarBaseObject, IMsoObjectIf> saraMsoMap = new HashMap();
    Map<IMsoObjectIf, SarBaseObject> msoSaraMap = new HashMap();

    boolean initiated = false;
    private IDiskoApplication diskoApp;

    public SarModelDriver()
    {
        // setUpService();
    }

    public IMsoObjectIf.IObjectIdIf makeObjectId()
    {
        return new AbstractMsoObject.ObjectId(sarOperation.getID() + "." + sarSvc.getSession().createInstanceId());
    }

    public SarAccessService getSarSvc()
    {
        return sarSvc;
    }

    public void initiate()
    {
        setUpService();
        IMsoModelIf imm = MsoModelImpl.getInstance();
        imm.getEventManager().addCommitListener(this);
        initiated = true;
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
        if (sarSvc.getSession().isFinishedLoading() && sarSvc.getSession().getOperations().getOperations().size() == 0)
        {
            loadingOperation = true;
            sarSvc.getSession().createNewOperation("MSO", true);
        }
    }

    public boolean isInitiated()
    {
        if (!loadingOperation && sarSvc.getSession().isFinishedLoading() && sarSvc.getSession().getOperations().getOperations().size() == 0)
        {
            loadingOperation = true;
            sarSvc.getSession().createNewOperation("MSO", true);
        }
        return sarSvc.getSession().isFinishedLoading() && !loadingOperation;
    }

    public List<String[]> getActiveOperations()
    {
        List<String[]> ops = new ArrayList();
        List<SarOperation> opers = sarSvc.getSession().getOperations().getOperations();
        for (SarOperation soper : opers)
        {
            String[] descr = {soper.getName(), soper.getID()};
            ops.add(descr);
        }
        return ops;
    }

    public boolean setActiveOperation(String operationid)
    {
        boolean result = true;
        SarOperation soper = sarSvc.getSession().getOperation(operationid);
        sarOperation = null;

        if (!clearMSO())
        {
            return false;
        }
        if (!setActiveOperation(soper))
        {
            return false;
        }

        sarOperation = soper;
        return true;
    }

    private boolean clearMSO()
    {
        return true;
    }

    boolean setActiveOperation(SarOperation soper)
    {
        MsoModelImpl.getInstance().setRemoteUpdateMode();
        //CREATE MSO operation
        createMsoOperation(soper);
        // ADD ALL CO
        for (SarObject so : soper.getObjectList())
        {
            if (so.getName().equals("CmdPost"))
            {
                addMsoObject(so);
            }
        }
        // ADD REST OF SARObjects
        for (SarObject so : soper.getObjectList())
        {
            if (!so.getName().equals("CmdPost"))
            {
                addMsoObject(so);
            }
        }
        //ADD RELATIONS
        for (SarObject so : soper.getObjectList())
        {
            //AddNamedrelations
            Iterator<Map.Entry<String, SarBaseObject>> table = so.getNamedRelations().entrySet().iterator();
            while (table.hasNext())
            {
                Map.Entry<String, SarBaseObject> entry = table.next();
                updateMsoReference(so, (SarObjectImpl) entry.getValue(), entry.getKey(), SarBaseObjectImpl.ADD_NAMED_REL_FIELD);
            }
            //Add rest
            Iterator<Map.Entry<String, List<SarBaseObject>>> rels = so.getRelations().entrySet().iterator();
            while (rels.hasNext())
            {
                Map.Entry<String, List<SarBaseObject>> entry = rels.next();
                for (int i = 0; i < entry.getValue().size(); i++)
                {
                    SarObjectImpl sarBaseObject = (SarObjectImpl) entry.getValue().get(i);
                    updateMsoReference(so, sarBaseObject, entry.getKey(), SarBaseObjectImpl.ADD_REL_FIELD);
                }

            }

        }
        MsoModelImpl.getInstance().restoreUpdateMode();

        return true;
    }

    public void finishActiveOperation()
    {
        //If this is the only operation, shutdown and create a new one
        boolean createNew = false;
        if (sarSvc.getSession().getOperations().getOperations().size() == 1)
        {
            createNew = true;
        }
        sarSvc.getSession().finishOperation(sarOperation.getID());
        if (createNew)
        {
            createNewOperation();
        }

    }

    public void createNewOperation()
    {
        loadingOperation = true;
        sarSvc.getSession().createNewOperation("MSO", true);
    }

    public void merge()
    {
        //To change body of implemented methods use File | Settings | File Templates.
        //TODO
    }

    public void setDiskoApplication(IDiskoApplication aDiskoApp)
    {
        diskoApp = aDiskoApp;
    }

    public void shutDown()
    {
        sarSvc.getSession().shutDown();
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
                String form = IDHelper.formatOperationID(oper.getID());
                String prefix = "";
                String number = "";
                if (form.indexOf("-") > 0)
                {
                    prefix = form.substring(0, form.lastIndexOf("-"));
                    number = form.substring(form.lastIndexOf("-") + 1);
                }
                IMsoObjectIf.IObjectIdIf operid = new AbstractMsoObject.ObjectId(oper.getID());
                testOperation = msoManager.createOperation(prefix, number, operid);
                sarOperation = oper;
//                MsoModelImpl.getInstance().restoreUpdateMode();
                //TODO Opprett of map inn data

            }
            catch (DuplicateIdException e) // shall not happen
            {
                e.printStackTrace();
            }
        } else
        {
            //Hendelse er allerede opprettet, hva n�
        }
    }

    private HashMap<String, SarObject> tmpObjects=new HashMap();
    public void handleMsoCommitEvent(MsoEvent.Commit e)
    {
        tmpObjects.clear();
        //Check that operation is added
        if (sarSvc.getSession().isFinishedLoading() && sarSvc.getSession().getOperations().getOperations().size() == 0)
        {
            sarSvc.getSession().createNewOperation("MSO", true);
        }
        //sarSvc.getSession().beginCommit(sarOperation.getID());
        //Iterer gjennom objektene, sjekk type og oppdater sara etter typen
        ICommitWrapperIf wrapper = (ICommitWrapperIf) e.getSource();
        List<ICommittableIf.ICommitObjectIf> objectList = new ArrayList<ICommittableIf.ICommitObjectIf>(wrapper.getObjects());
        for (ICommittableIf.ICommitObjectIf ico : objectList)
        {
            System.out.println("Commit Object " + ico.getType() + " " + ico.getObject());
            //IF created, create SARA object
            if (ico.getType().equals(CommitManager.CommitType.COMMIT_CREATED))
            {
                SarObject so = createSaraObject(ico);
                tmpObjects.put(so.getID(),so);
                so.createNewOut();
            } else if (ico.getType().equals(CommitManager.CommitType.COMMIT_MODIFIED))
            {
                //if modified, modify Saraobject.
                updateSaraObject(ico);
            } else if (ico.getType().equals(CommitManager.CommitType.COMMIT_DELETED))
            {
                //if deleted remove Sara object
                deleteSaraObject(ico);
            }
        }

        List<ICommittableIf.ICommitReferenceIf> attrList = new ArrayList<ICommittableIf.ICommitReferenceIf>(wrapper.getListReferences());
        System.out.println("Antall endrede referanser : "+attrList.size());
        for (ICommittableIf.ICommitReferenceIf ico : attrList)
        {
            System.out.println("Commit List " + ico.getType() + " " + ico.getReferenceName());
            msoReferenceChanged(ico, false);
        }

        List<ICommittableIf.ICommitReferenceIf> listList = new ArrayList<ICommittableIf.ICommitReferenceIf>(wrapper.getAttributeReferences());
        System.out.println("Antall endrede listereferanser : "+listList.size());
        for (ICommittableIf.ICommitReferenceIf ico : listList)
        {
//            System.out.println("Commit Attr " + ico.getType() + " " + ico.getReferenceName());
            msoReferenceChanged(ico, true);
        }
        sarSvc.getSession().commit(sarOperation.getID());

    }

    private void msoReferenceChanged(ICommittableIf.ICommitReferenceIf ico, boolean isNamedReference)
    {
        CommitManager.CommitType ct = ico.getType(); //CommitManager.CommitType.COMMIT_CREATED/CommitManager.CommitType.COMMIT_DELETED
        IMsoObjectIf owner = ico.getReferringObject();
        IMsoObjectIf ref = ico.getReferredObject();
        SarObject sourceObj = sarOperation.getSarObject(owner.getObjectId());
        SarObject relObj = sarOperation.getSarObject(ref.getObjectId());
        String refName = ico.getReferenceName();
            if(sourceObj==null)
            {
                sourceObj=tmpObjects.get(owner.getObjectId());
            }
            if(relObj==null)
            {
                relObj=tmpObjects.get(ref.getObjectId());
            }
        if (sourceObj == null || relObj == null)
        {
                Log.warning("Object not found " + owner.getObjectId() + " or " + ref.getObjectId());
    //            System.out.println("Object not found " + owner.getObjectId() + " or " + ref.getObjectId());
         }

         else
        {
            System.out.println("ChangeReference " + isNamedReference + " " +ct);
            if (ct.equals(CommitManager.CommitType.COMMIT_CREATED))
            {
                if (isNamedReference)
                {
                    sourceObj.addNamedRelation(refName, relObj);
                } else
                {
                    sourceObj.addRelation(refName, relObj);
                }
            } else if (ct.equals(CommitManager.CommitType.COMMIT_MODIFIED))
            {
                //TODO skal dette kunne skje??
                Log.warning("-------------Modify relation-----------");
            } else if (ct.equals(CommitManager.CommitType.COMMIT_DELETED))
            {
                if (isNamedReference)
                {
                    sourceObj.removeNamedRelation(refName);
                } else
                {
                    sourceObj.removeRelation(refName, relObj);
                }
            }
        }
    }

    private SarObject createSaraObject(ICommittableIf.ICommitObjectIf commitObject)
    {
        IMsoObjectIf msoObj = commitObject.getObject();
        msoObj.getMsoClassCode();
        //Finn Saras mappede objekttype
        SarSession sarSess = sarSvc.getSession();
        String className = msoObj.getClass().getName();
        if (className.indexOf("Impl") > 0)
        {
            className = className.substring(26, className.indexOf("Impl"));
        }
        String objId = msoObj.getObjectId().indexOf(".") > 0 ?
                msoObj.getObjectId().substring(msoObj.getObjectId().indexOf(".") + 1) :
                msoObj.getObjectId();
        SarObject sbo = (SarObject) sarSess.createInstance(className, SarBaseObjectFactory.TYPE_OBJECT, objId);

        //TODO sett attributter og tilordne til hendelse
        sbo.setOperation(sarOperation);
        updateSaraObject(sbo, commitObject.getObject(), false);
        //Opprett instans av av denne og distribuer
        return sbo;
    }

    private void updateSaraObject(ICommittableIf.ICommitObjectIf commitObject)
    {
        SarObject soi = sarOperation.getSarObject(commitObject.getObject().getObjectId());
        updateSaraObject(soi, commitObject.getObject(), true);
    }

    private void updateSaraObject(SarObject sbo, IMsoObjectIf msoObj, boolean submitChanges)
    {
        SarSession sarSess = sarSvc.getSession();
        Map attrMap = msoObj.getAttributes();
        Map relMap = msoObj.getReferenceObjects();
        List<SarBaseObject> objs = sbo.getObjects();
        //TODO legg inn begincommit
        //sarSvc.getSession().beginCommit(sarOperation.getID());
        for (SarBaseObject so : objs)
        {
            try
            {
                if (so instanceof SarFact)
                {
                    //Map fact direkte mot attributtverdi
                    String attrName = ((SarFact) so).getLabel();
                    IAttributeIf msoAttr = (IAttributeIf) attrMap.get(attrName.toLowerCase());
                    if (msoAttr != null)
                    {
                        SarMsoMapper.mapMsoAttrToSarFact((SarFact) so, msoAttr, submitChanges);
                    } else if (!attrName.equalsIgnoreCase("Objektnavn"))
                    {
                        Log.warning("Attribute " + attrName + " not found for " + sbo.getName());
                    }

                } else if (so instanceof SarObject)
                {
                    String objName = ((SarObject) so).getName();
                    IMsoObjectIf refAttr = (IMsoObjectIf) relMap.get(objName);
                    if (refAttr != null)
                    {
                        updateSaraObject((SarObject) so, refAttr, submitChanges);
                    }
                }
            }
            catch (Exception e)
            {
                Log.error("Unable to map to Sara :(sarObj:" + so.getValueAsString() + ",msoObj:" + msoObj.getClass().getName() + e.getMessage());
            }
        }
        if (submitChanges)
        {
            sarSess.commit(sarOperation.getID());
        }

    }

    private void deleteSaraObject(ICommittableIf.ICommitObjectIf commitObject)
    {
        //Finn mappet objekt
        //Send slette melding
    }

    public void setUpService()
    {
        sarSvc = SarAccessService.getInstance();
        Credentials creds = new UserPasswordCredentials("Operat�r", "Operat�r");
        SarSession s = null;
        try
        {
            s = sarSvc.createSession(creds);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        sarSvc.getSession().AddChangeListener(this);

        try
        {
            sarSvc.startRecvMessages();
        }
        catch (Exception e)
        {
            Log.printStackTrace(e);
            //Log.warning(e.getMessage());
        }
    }

    //---------------SaraChangeListener-----------------------------
    public void saraChanged(final SaraChangeEvent change)
    {
        if (loadingOperation && change.getSource() instanceof SarOperation)
        {
            if (change.getChangeType() == SaraChangeEvent.TYPE_ADD)
            {
                loadingOperation = false;
                diskoApp.operationAdded(((SarOperation) change.getSource()).getID());
            }
        }
        if (sarOperation != null && change.getSarOp() == sarOperation)
        {
            SwingUtilities.invokeLater(new Runnable()
            {

                public void run()
                {
                    //To change body of implemented methods use File | Settings | File Templates.
                    System.out.println("saraChanged");

                    MsoModelImpl.getInstance().setRemoteUpdateMode();
                    try
                    {
                        if (change.getChangeType() == SaraChangeEvent.TYPE_ADD)
                        {
                            if (change.getSource() instanceof SarOperation)
                            {
                                createMsoOperation((SarOperation) change.getSource());
                            } else if (change.getSource() instanceof SarObject)
                            {
                                addMsoObject((SarObject) change.getSource());
                            } else
                            {
                                Log.warning("SaraChange not handled for objectType " + change.getSource().getClass().getName());
                            }

                            //TODO implementer for de andre objekttypene fact og object
                        } else if (change.getChangeType() == SaraChangeEvent.TYPE_CHANGE)
                        {
                            changeMsoFromSara(change);

                        } else if (change.getChangeType() == SaraChangeEvent.TYPE_REMOVE)
                        {
                            removeInMsoFromSara(change);
                        }
                    }
                    catch (Exception e)
                    {
                        Log.printStackTrace("Unable to update msomodel " + e.getMessage(), e);
                    }
                    MsoModelImpl.getInstance().restoreUpdateMode();
                }
            });
        }
    }


    public void saraException(SaraException sce)
    {
        //TODO videresend exception
    }
//End---------------SaraChangeListener-----------------------------

    private void removeInMsoFromSara(SaraChangeEvent change)
    {

        Object co = change.getSource();
        IMsoManagerIf msoManager = MsoModelImpl.getInstance().getMsoManager();

        IOperationIf testOperation = msoManager.getOperation();
        try
        {

            if (co instanceof SarOperation && co == sarOperation)
            {
                msoManager.remove(testOperation);
                diskoApp.operationFinished();
            } else
            {
                IMsoObjectIf source = saraMsoMap.get(co);
                if (source != null)
                {
                    msoManager.remove(source);
                }
            }
        }
        catch (MsoNullPointerException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

//      if (co.getFactType() == Fact.FACTTYPE_RELATION)
//      {
//         SarObjectImpl so = (SarObjectImpl) change.getParent();
//         String relId = ((String[]) co.getToObject())[1];
//         String relName = ((String[]) co.getToObj())[0];
//         SarObjectImpl rel = (SarObjectImpl) sarOperation.getSarObject(relId);
//
//         IMsoObjectIf source = saraMsoMap.get(so);
//         IMsoObjectIf relObj = saraMsoMap.get(rel);
//         //Change in relations
//         //Get type relation change
//         if (co.getFieldName().equalsIgnoreCase(SarBaseObjectImpl.REM_REL_FIELD))
//         {
//            source.addObjectReference(relObj, null);
//         }
//         else if (co.getFieldName().equalsIgnoreCase(SarBaseObjectImpl.REM_NAMED_REL_FIELD))
//         {
//            IMsoReferenceIf refObj = (IMsoReferenceIf) source.getReferenceObjects().get(relName);
//            refObj.setReference(null);
//         }
//      }
//      else
//      {
//         SarBaseObject so = change.getParent();
//         if (so instanceof SarObjectImpl)
//         {
//            //Find object and remove in mso
//            IMsoManagerIf msoMgr = MsoModelImpl.getInstance().getMsoManager();
//
//            IMsoObjectIf msoObj = saraMsoMap.get(so);
//            try
//            {
//               boolean result = msoMgr.remove(msoObj);
//               //Vinjar: Hva dersom remove ikke ok??
//            }
//            catch (MsoNullPointerException e)
//            {
//               e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
//            //TODO avsjekk med vinjar om dette er nok
//
//         }
//         else
//         {
//            //Change of factvalue
//            Log.warning("NOT IMPLEMENTED YET deleteMsoFromSara field: " + co.getFieldType() + " ftype: " + co.getFactType());
//
//         }

//      }
    }

    private void changeMsoFromSara(SaraChangeEvent change)
    {
        SarBaseObject so = change.getParent();
        ChangeObject co = (ChangeObject) change.getSource();

        if (co.getFactType() == Fact.FACTTYPE_RELATION)
        {
            String relId = ((String[]) co.getToObject())[1];
            String relName = ((String[]) co.getToObj())[0];
            SarObjectImpl rel = (SarObjectImpl) sarOperation.getSarObject(relId);

            updateMsoReference(so, rel, relName, co.getFieldName());
        } else
        {
            //Change of factvalue
            //Find object containing the fact
            SarObject parentObject = getParentObject(so);
            // Use object to find msoobject
            if (parentObject != null)
            {
                IMsoObjectIf msoObj = saraMsoMap.get(parentObject);
                Map attrs = msoObj.getAttributes();
                AttributeImpl attr = (AttributeImpl) attrs.get(((SarFact) so).getLabel().toLowerCase());
                if (attr != null)
                {
                    SarMsoMapper.mapSarFactToMsoAttr(attr, (SarFact) so);
                }

            }
            //Update msoobject
            Log.warning("NOT IMPLEMENTED YET changeMsoFromSara field: " + co.getFieldType() + " ftype: " + co.getFactType());
        }
    }

    private void updateMsoReference(SarBaseObject so, SarObjectImpl rel, String relName, String fieldName)
    {
        IMsoObjectIf source = saraMsoMap.get(so);
        IMsoObjectIf relObj = saraMsoMap.get(rel);
        //Change in relations
        //Get type relation change
        if (fieldName.equalsIgnoreCase(SarBaseObjectImpl.ADD_REL_FIELD))
        {
            source.addObjectReference(relObj, relName);
        } else if (fieldName.equalsIgnoreCase(SarBaseObjectImpl.ADD_NAMED_REL_FIELD))
        {

            IMsoReferenceIf refObj = (IMsoReferenceIf) source.getReferenceObjects().get(relName);
            if (refObj == null)
            {
                System.out.println(relName);
            }
            refObj.setReference(relObj);

        }
    }

    private SarObject getParentObject(SarBaseObject so)
    {
        //Use id rules to find object id  (
        SarObject soi = null;
        try
        {
            String parentid = so.getID().substring(0, so.getID().lastIndexOf("."));
            String operid = so.getID().substring(0, so.getID().indexOf("."));
            SarOperation soper = sarSvc.getSession().getOperation(operid);
            soi = soper.getSarObject(parentid);
            return soi;
        }
        catch (Exception e)
        {
            Log.printStackTrace("Unable to find parent", e);
            return null;
        }

    }

//    private IMsoObjectIf getMsoObject(SarObject sarObject)
//    {
//        IMsoObjectIf msoObj=null;
//        //Bruker reflection og navn til  � opprtette masomanagermetode ok kall createXXX foroppretting
//        //bruk den med idparameter
//        //tilordne msoObj fra createmetode
//        String methodName="get"+sarObject.getName()+"List";
//        IMsoManagerIf msoMgr = MsoModelImpl.getInstance().getMsoManager();
//        if(sarObject.getName().equals("CmdPost"))
//        {
//            return msoMgr.getCmdPost();
//        }
//
//        Method m=null;
//        try {
//
//            m=msoMgr.getCmdPost().getClass().getMethod(methodName,IMsoObjectIf.IObjectIdIf.class);
//            m.setAccessible(true);
//            IMsoObjectIf.IObjectIdIf id=new AbstractMsoObject.ObjectId(sarObject.getID());
//            msoObj =((IMsoListIf) m.invoke(msoMgr.getCmdPost(), new Object[]{id})).getItem(sarObject.getID());
//
//            // Handle any exceptions thrown by method to be invoked.
//        }
//        catch (Exception x)
//        {
//            Log.warning(x.getMessage());
//        }
//        return msoObj;
//
//    }


    protected void addMsoObject(SarObject sarObject)
    {
        String name = "";
        //name=sarObject.getName();
        IMsoManagerIf msoMgr = MsoModelImpl.getInstance().getMsoManager();

        IMsoObjectIf msoObj = null;
        //Bruker reflection og navn til  � opprtette masomanagermetode ok kall createXXX foroppretting
        //bruk den med idparameter
        //tilordne msoObj fra createmetode
        String methodName = "create" + sarObject.getName();

        Method m = null;
        try
        {
            m = msoMgr.getClass().getMethod(methodName, IMsoObjectIf.IObjectIdIf.class);
            m.setAccessible(true);
            IMsoObjectIf.IObjectIdIf id = new AbstractMsoObject.ObjectId(sarObject.getID());
            msoObj = (IMsoObjectIf) m.invoke(msoMgr, new Object[]{id});

            // Handle any exceptions thrown by method to be invoked.
        }
        catch (Exception x)
        {
            Log.warning(x.getMessage());

        }

        setMsoObjectValues(sarObject, msoObj);
        saraMsoMap.put(sarObject, msoObj);
        msoSaraMap.put(msoObj, sarObject);

    }

    public static void setMsoObjectValues(SarObject sarObject, IMsoObjectIf msoObj)
    {
        System.out.println("setMsoObjectValues");
        //MsoModelImpl.getInstance().setRemoteUpdateMode();

        for (SarBaseObject fact : sarObject.getObjects())
        {
            if (fact instanceof SarFact)
            {
                try
                {
                    Map attrs = msoObj.getAttributes();
                    AttributeImpl attr = (AttributeImpl) attrs.get(((SarFact) fact).getLabel().toLowerCase());
                    if (attr != null)
                    {
                        SarMsoMapper.mapSarFactToMsoAttr(attr, (SarFact) fact);
                    }
                    //msoObj.setAttribute(((SarFact)fact).getLabel().toLowerCase(),((SarFact)fact).getValue());
                }
//               catch (UnknownAttributeException e)
//               {
//                  Log.printStackTrace("Unable to map "+sarObject.getName());
//               }
                catch (Exception npe)
                {
                    try
                    {
                        Log.warning("Attr not found " + ((SarFact) fact).getLabel() + " for msoobj " + msoObj.getMsoClassCode() + "\n" + npe.getMessage());
                    }
                    catch (Exception e)
                    {
                        Log.printStackTrace(e);
                    }
                }
                //TODO implementer
            } else
            {
                //TODO handle internal object attributes
            }
        }
        //       MsoModelImpl.getInstance().commit();
        //MsoModelImpl.getInstance().restoreUpdateMode();
    }


}