package org.redcross.sar.modelDriver;

import org.junit.Before; 
import org.junit.After;
import org.junit.Ignore; 
import org.junit.Test; 
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import org.redcross.sar.mso.MsoModelImpl;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.MsoEvent;
import org.redcross.sar.mso.data.*;
import org.redcross.sar.util.mso.Position;
import org.rescuenorway.saraccess.api.*;
import org.rescuenorway.saraccess.model.SarOperation;
import org.rescuenorway.saraccess.model.SarObject;
import org.rescuenorway.saraccess.model.SarBaseObject;
import org.rescuenorway.saraccess.except.SaraException;
import no.cmr.tools.Log;
import no.cmr.hrs.sar.model.Operation;

import java.util.Calendar;

/**
 * ModelDriver Tester.
 *
 * @author Stian Anfinsen
 * @since <pre>04/30/2007</pre>
 * @version 1.0
 */
public class ModelDriverTest
{
    @Before
    public void setUp() throws Exception {
       Log.init("ModelDriverTest");
    }
    @After
    public void tearDown() throws Exception {

    }

//    @Test
//    public void testHandleMsoCommitEvent()
//    {
//      IMsoModelIf imm= MsoModelImpl.getInstance();
//   }

   @Test
   public void testCreateMsoObject()
   {
//      IOperationIf operationA = m_msoManager.createOperation("2007-TEST","0001");
//      ICmdPostIf cmdPostA = m_msoManager.createCmdPost();

     IMsoModelIf imm= MsoModelImpl.getInstance();
     SarModelDriver smd=new SarModelDriver();
      SarSession sarSess=smd.getSarSvc().getSession();
        imm.getEventManager().addCommitListener(smd);
     smd.initiate();
       IMsoManagerIf msoMgr = MsoModelImpl.getInstance().getMsoManager();

       IMsoObjectIf msoObj=null;
      imm.setLocalUpdateMode();
       VehicleImpl ve=(VehicleImpl)msoMgr.createVehicle("Testing");
       ve.setAverageSpeed(50);
       ve.setBearing(90);
       ve.setCallSign("123456753");
       ve.setNumber(5);
       ve.setPosition(new Position("testing",5d,12d));
       ve.setRemarks("Test vehicle");
        ve.setMaxSpeed(90);
       ve.setSpeed(21);
       ve.setStatus(IUnitIf.UnitStatus.EMPTY);


     //SarObject sbo= (SarObject)sarSess.createInstance("Vehicle",SarBaseObjectFactory.TYPE_OBJECT,sarSess.createInstanceId());
      imm.commit();
      //smd.addMsoObject(sbo);

      //MsoEvent.Commit e=new MsoEvent.Commit(ve,0);

      
  }

    @Test
    public void testCreateSaraObject()
    {

      IMsoModelIf imm= MsoModelImpl.getInstance();
      SarModelDriver smd=new SarModelDriver();

      SarSession sarSess=smd.getSarSvc().getSession();
        smd.initiate();
      
      SarObject sbo= (SarObject)sarSess.createInstance("Vehicle",SarBaseObjectFactory.TYPE_OBJECT,sarSess.createInstanceId());

       smd.addMsoObject(sbo);

       assertEquals(true,true);

   }

   @Test
   public void testLoadModelObject()
   {

     IMsoModelIf imm= MsoModelImpl.getInstance();
     SarModelDriver smd=new SarModelDriver();

     SarSession sarSess=smd.getSarSvc().getSession();
       smd.initiate();

     SarObject sbo= (SarObject)sarSess.createInstance("Vehicle",SarBaseObjectFactory.TYPE_OBJECT,sarSess.createInstanceId());

      //smd.addMsoObject(sbo);
     
      while (!sarSess.isFinishedLoading())
      {
         try
         {
            Thread.currentThread().sleep(200);
         }
         catch (InterruptedException e)
         {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
         }
      }
      System.out.println(imm.getMsoManager().getOperation().getOperationNumber());
      System.out.println(imm.getMsoManager().getCmdPost().getStatus());

      assertEquals(true,true);

  }


}
