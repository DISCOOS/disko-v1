package org.redcross.sar.modelDriver;

import org.redcross.sar.mso.data.*;
import org.redcross.sar.mso.event.IMsoCommitListenerIf;
import org.redcross.sar.mso.event.MsoEvent;
import org.redcross.sar.mso.committer.ICommitWrapperIf;
import org.redcross.sar.mso.committer.ICommittableIf;
import org.redcross.sar.mso.CommitManager;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.MsoModelImpl;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.DuplicateIdException;
import org.redcross.sar.util.except.UnknownAttributeException;
import org.rescuenorway.saraccess.api.*;
import org.rescuenorway.saraccess.except.SaraException;
import org.rescuenorway.saraccess.model.*;

import java.util.Random;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Method;

import no.cmr.tools.Log;
import no.cmr.hrs.sar.model.Operation;
import no.cmr.hrs.sar.model.SarObjectImpl;
import no.cmr.hrs.sar.model.Fact;
import no.cmr.hrs.sar.tools.ChangeObject;

/**
 * For documentation, see {@link  IModelDriverIf}
 */
public class SarModelDriver implements IModelDriverIf, IMsoCommitListenerIf, SaraChangeListener {
    Random m_rand = new Random(89652467667623L);
    SarAccessService sarSvc;
    SarOperation sarOperation = null;
    Map<SarBaseObject, IMsoObjectIf> saraMsoMap = new HashMap();
    Map<IMsoObjectIf, SarBaseObject> msoSaraMap = new HashMap();

    boolean initiated = false;

    public SarModelDriver() {
        // setUpService();
    }

    public IMsoObjectIf.IObjectIdIf makeObjectId() {
        return new AbstractMsoObject.ObjectId(sarSvc.getSession().createInstanceId());
    }

    public SarAccessService getSarSvc() {
        return sarSvc;
    }

    public void initiate() {
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
    }

    private void createMsoOperation(SarOperation oper) {
        //Opprett alle Mso objekter fra hendelsens objekter
        IMsoManagerIf msoManager = MsoModelImpl.getInstance().getMsoManager();
        IOperationIf testOperation = msoManager.getOperation();
        if (testOperation == null) {
            try {
                testOperation = msoManager.createOperation("2007-TEST", oper.getID());
                sarOperation = oper;
                MsoModelImpl.getInstance().restoreUpdateMode();
                //TODO Opprett of map inn data

            }
            catch (DuplicateIdException e) // shall not happen
            {
                e.printStackTrace();
            }
        } else {
            //Hendelse er allerede opprettet, hva nå
        }
    }

    public void handleMsoCommitEvent(MsoEvent.Commit e) {
        //sarSvc.getSession().beginCommit(sarOperation.getID());
        //Iterer gjennom objektene, sjekk type og oppdater sara etter typen
        ICommitWrapperIf wrapper = (ICommitWrapperIf) e.getSource();
        List<ICommittableIf.ICommitObjectIf> objectList = wrapper.getObjects();
        for (ICommittableIf.ICommitObjectIf ico : objectList) {
            //IF created, create SARA object
            if (ico.getType().equals(CommitManager.CommitType.COMMIT_CREATED)) {
                SarObject so = createSaraObject(ico);
                so.createNewOut();
            } else if (ico.getType().equals(CommitManager.CommitType.COMMIT_MODIFIED)) {
                //if modified, modify Saraobject.
                updateSaraObject(ico);
            } else if (ico.getType().equals(CommitManager.CommitType.COMMIT_DELETED)) {
                //if deleted remove Sara object
                deleteSaraObject(ico);
            }
        }

        List<ICommittableIf.ICommitReferenceIf> attrList = wrapper.getListReferences();
        for (ICommittableIf.ICommitReferenceIf ico : attrList) {
            msoReferenceChanged(ico, false);
        }

        List<ICommittableIf.ICommitReferenceIf> listList = wrapper.getAttributeReferences();
        for (ICommittableIf.ICommitReferenceIf ico : listList) {
            msoReferenceChanged(ico, true);
        }
        sarSvc.getSession().commit(sarOperation.getID());

    }

    private void msoReferenceChanged(ICommittableIf.ICommitReferenceIf ico, boolean isNamedReference) {
        CommitManager.CommitType ct = ico.getType(); //CommitManager.CommitType.COMMIT_CREATED/CommitManager.CommitType.COMMIT_DELETED
        IMsoObjectIf owner = ico.getReferringObject();
        IMsoObjectIf ref = ico.getReferredObject();
        SarObject sourceObj = sarOperation.getSarObject(owner.getObjectId());
        SarObject relObj = sarOperation.getSarObject(ref.getObjectId());
        String refName = ico.getReferenceName();
        if (sourceObj == null || relObj == null) {
            Log.warning("Object not found " + owner.getObjectId() + " or " + ref.getObjectId());
        } else {
            if (ct.equals(CommitManager.CommitType.COMMIT_CREATED)) {
                if (isNamedReference) {
                    sourceObj.addNamedRelation(refName, relObj);
                } else {
                    sourceObj.addRelation(refName, relObj);
                }
            } else if (ct.equals(CommitManager.CommitType.COMMIT_MODIFIED)) {
                //TODO skal dette kunne skje??
                Log.warning("-------------Modify relation-----------");
            } else if (ct.equals(CommitManager.CommitType.COMMIT_DELETED)) {
                if (isNamedReference) {
                    sourceObj.removeNamedRelation(refName);
                } else {
                    sourceObj.removeRelation(refName, relObj);
                }
            }
        }
    }

    private SarObject createSaraObject(ICommittableIf.ICommitObjectIf commitObject) {
        IMsoObjectIf msoObj = commitObject.getObject();
        msoObj.getMsoClassCode();
        //Finn Saras mappede objekttype
        SarSession sarSess = sarSvc.getSession();
        String className = msoObj.getClass().getName();
        if (className.indexOf("Impl") > 0) {
            className = className.substring(26, className.indexOf("Impl"));
        }
        SarObject sbo = (SarObject) sarSess.createInstance(className, SarBaseObjectFactory.TYPE_OBJECT, msoObj.getObjectId());

        //TODO sett attributter og tilordne til hendelse
        sbo.setOperation(sarOperation);
        updateSaraObject(sbo, commitObject.getObject(), false);
        //Opprett instans av av denne og distribuer
        return sbo;
    }

    private void updateSaraObject(ICommittableIf.ICommitObjectIf commitObject) {
        SarObject soi = sarOperation.getSarObject(commitObject.getObject().getObjectId());
        updateSaraObject(soi, commitObject.getObject(), true);
    }

    private void updateSaraObject(SarObject sbo, IMsoObjectIf msoObj, boolean submitChanges) {
        SarSession sarSess = sarSvc.getSession();
        Map attrMap = msoObj.getAttributes();
        Map relMap = msoObj.getReferenceObjects();
        List<SarBaseObject> objs = sbo.getObjects();
        //TODO legg inn begincommit
        //sarSvc.getSession().beginCommit(sarOperation.getID());
        for (SarBaseObject so : objs) {
            try {
                if (so instanceof SarFact) {
                    //Map fact direkte mot attributtverdi
                    String attrName = ((SarFact) so).getLabel();
                    IAttributeIf msoAttr = (IAttributeIf) attrMap.get(attrName.toLowerCase());
                    SarMsoMapper.mapMsoAttrToSarFact((SarFact) so, msoAttr, submitChanges);
                } else if (so instanceof SarObject) {
                    String objName = ((SarObject) so).getName();
                    IMsoObjectIf refAttr = (IMsoObjectIf) relMap.get(objName);
                    if (refAttr != null) {
                        updateSaraObject((SarObject) so, refAttr, submitChanges);
                    }
                }
            }
            catch (Exception e) {
                Log.warning("Unable to map to Sara :(sarObj:" + so.getValueAsString() + ",msoObj:" + msoObj.getClass().getName() + e.getMessage());
            }
        }
        if (submitChanges) {
            sarSess.commit(sarOperation.getID());
        }

    }

    private void deleteSaraObject(ICommittableIf.ICommitObjectIf commitObject) {
        //Finn mappet objekt
        //Send slette melding
    }

    public void setUpService() {
        sarSvc = SarAccessService.getInstance();
        Credentials creds = new UserPasswordCredentials("Operatør", "Operatør");
        SarSession s = null;
        try {
            s = sarSvc.createSession(creds);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        sarSvc.getSession().AddChangeListener(this);

        try {
            sarSvc.startRecvMessages();
        }
        catch (Exception e) {
            Log.warning(e.getMessage());
        }
    }

    //---------------SaraChangeListener-----------------------------
    public void saraChanged(SaraChangeEvent change) {
        MsoModelImpl.getInstance().setRemoteUpdateMode();
        try {
//TODO: oppdater MsoModell
            if (change.getChangeType() == SaraChangeEvent.TYPE_ADD) {
                if (change.getSource() instanceof SarOperation) {
                    createMsoOperation((SarOperation) change.getSource());
                } else if (change.getSource() instanceof SarObject) {
                    addMsoObject((SarObject) change.getSource());
                }

                //TODO implementer for de andre objekttypene fact og object
            } else if (change.getChangeType() == SaraChangeEvent.TYPE_CHANGE) {
                changeMsoFromSara(change);

            } else if (change.getChangeType() == SaraChangeEvent.TYPE_REMOVE) {
                removeInMsoFromSara(change);
            }
        } catch (Exception e) {
            Log.warning("Unable to update msomodel " + e.getMessage());
        }
        MsoModelImpl.getInstance().restoreUpdateMode();
    }


    public void saraException(SaraException sce) {
        //TODO videresend exception
    }
//End---------------SaraChangeListener-----------------------------          

    private void removeInMsoFromSara(SaraChangeEvent change) {
        SarObjectImpl so = (SarObjectImpl) change.getParent();
        ChangeObject co = (ChangeObject) change.getSource();
        String relId = ((String[]) co.getToObject())[1];
        String relName = ((String[]) co.getToObj())[0];
        SarObjectImpl rel = (SarObjectImpl) sarOperation.getSarObject(relId);

        if (co.getFactType() == Fact.FACTTYPE_RELATION) {
            IMsoObjectIf source = saraMsoMap.get(so);
            IMsoObjectIf relObj = saraMsoMap.get(rel);
            //Change in relations
            //Get type relation change
            if (co.getFieldName().equalsIgnoreCase(SarBaseObjectImpl.REM_REL_FIELD)) {
                source.addObjectReference(relObj, null);
            } else if (co.getFieldName().equalsIgnoreCase(SarBaseObjectImpl.REM_NAMED_REL_FIELD)) {
                IMsoReferenceIf refObj = (IMsoReferenceIf) source.getReferenceObjects().get(relName);
                refObj.setReference(null);
            }
        } else {
            //Change of factvalue
            Log.warning("NOT IMPLEMENTED YET changeMsoFromSara field: " + co.getFieldType() + " ftype: " + co.getFactType());
        }
    }

    private void changeMsoFromSara(SaraChangeEvent change) {
        SarObjectImpl so = (SarObjectImpl) change.getParent();
        ChangeObject co = (ChangeObject) change.getSource();
        String relId = ((String[]) co.getToObject())[1];
        String relName = ((String[]) co.getToObj())[0];
        SarObjectImpl rel = (SarObjectImpl) sarOperation.getSarObject(relId);

        if (co.getFactType() == Fact.FACTTYPE_RELATION) {
            IMsoObjectIf source = saraMsoMap.get(so);
            IMsoObjectIf relObj = saraMsoMap.get(rel);
            //Change in relations
            //Get type relation change
            if (co.getFieldName().equalsIgnoreCase(SarBaseObjectImpl.ADD_REL_FIELD)) {
                source.addObjectReference(relObj, relName);
            } else if (co.getFieldName().equalsIgnoreCase(SarBaseObjectImpl.ADD_NAMED_REL_FIELD)) {

                IMsoReferenceIf refObj = (IMsoReferenceIf) source.getReferenceObjects().get(relName);
                refObj.setReference(relObj);

            }
        } else {
            //Change of factvalue
            Log.warning("NOT IMPLEMENTED YET changeMsoFromSara field: " + co.getFieldType() + " ftype: " + co.getFactType());
        }
    }

//    private IMsoObjectIf getMsoObject(SarObject sarObject)
//    {
//        IMsoObjectIf msoObj=null;
//        //Bruker reflection og navn til  å opprtette masomanagermetode ok kall createXXX foroppretting
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


    protected void addMsoObject(SarObject sarObject) {
        String name = "";
        //name=sarObject.getName();
        IMsoManagerIf msoMgr = MsoModelImpl.getInstance().getMsoManager();

        IMsoObjectIf msoObj = null;
        //Bruker reflection og navn til  å opprtette masomanagermetode ok kall createXXX foroppretting
        //bruk den med idparameter
        //tilordne msoObj fra createmetode
        String methodName = "create" + sarObject.getName();

        Method m = null;
        try {
            m = msoMgr.getClass().getMethod(methodName, IMsoObjectIf.IObjectIdIf.class);
            m.setAccessible(true);
            IMsoObjectIf.IObjectIdIf id = new AbstractMsoObject.ObjectId(sarObject.getID());
            msoObj = (IMsoObjectIf) m.invoke(msoMgr, new Object[]{id});

            // Handle any exceptions thrown by method to be invoked.
        }
        catch (Exception x) {
            Log.warning(x.getMessage());

        }

        setMsoObjectValues(sarObject, msoObj);
        saraMsoMap.put(sarObject, msoObj);
        msoSaraMap.put(msoObj, sarObject);

    }

    public static void setMsoObjectValues(SarObject sarObject, IMsoObjectIf msoObj) {
        //        MsoModelImpl.getInstance().setRemoteUpdateMode();

        for (SarBaseObject fact : sarObject.getObjects()) {
            if (fact instanceof SarFact) {
                try {
                    Map attrs = msoObj.getAttributes();
                    AttributeImpl attr = (AttributeImpl) attrs.get(((SarFact) fact).getLabel().toLowerCase());
                    if (attr != null) {
                        SarMsoMapper.mapSarFactToMsoAttr(attr, (SarFact) fact);
                    }
                    //msoObj.setAttribute(((SarFact)fact).getLabel().toLowerCase(),((SarFact)fact).getValue());
                }
//               catch (UnknownAttributeException e)
//               {
//                  Log.printStackTrace("Unable to map "+sarObject.getName());
//               }
                catch (Exception npe) {
                    Log.warning("Attr not found " + ((SarFact) fact).getLabel() + " for msoobj " + msoObj.getMsoClassCode() + "\n" + npe.getMessage());
                }
                //TODO implementer
            } else {
                //TODO handle internal object attributes
            }
        }
        //       MsoModelImpl.getInstance().commit();
//        MsoModelImpl.getInstance().restoreUpdateMode();
    }


}