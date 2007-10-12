package org.redcross.sar.app;

import no.cmr.tools.Log;
import org.redcross.sar.gui.LoginDialog;
import org.redcross.sar.gui.NavBar;
import org.redcross.sar.gui.SysBar;
import org.redcross.sar.gui.UIFactory;
import org.redcross.sar.map.DiskoMapManagerImpl;
import org.redcross.sar.map.IDiskoMap;
import org.redcross.sar.map.IDiskoMapManager;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.MsoModelImpl;
import org.redcross.sar.output.DiskoReport;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Hashtable;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Implements the DiskoApplication interface. This class is responsible for connecting to the
 * ArcGIS Engine API and the DiskoModule API.
 *
 */

/**
 * @author geira
 */
public class DiskoApplicationImpl extends JFrame implements IDiskoApplication
{
   private static final ResourceBundle bundle = ResourceBundle.getBundle("org.redcross.sar.app.properties.DiskoApplication");
   private static final String CHOOSEOPDESC = "CHOOSE.OP.DESC";
   private static final String CHOOSETEXT = "CHOOSE.OP.TEXT";
   private static final String INIT_ERROR_TEXT = "INIT.ERROR.TEXT";
   private static final String INIT_ERROR_SHUTDOWN_TEXT = "INIT.ERROR.SHUTDOWN.TEXT";
   private static final String OPERATION_FINISHED_TITLE = "OPERATION.FINISHED.HEADER";
   private static final String OPERATION_FINISHED_TEXT ="OPERATION.FINISHED.TEXT";
    private static final String OPERATION_CREATED_TEXT = "OPERATION.CREATED.TEXT";
    private static final String OPERATION_CREATED_TITLE = "OPERATION.CREATED.HEADER";

   private static final long serialVersionUID = 1L;
   private IDiskoRole currentRole = null;
   private Hashtable<String, IDiskoRole> roles = null;
   private DiskoModuleLoader moduleLoader = null;
   private Properties properties = null;
   private UIFactory uiFactory = null;
   private IDiskoMapManager mapManager = null;
   private MsoModelImpl m_msoModel = null;
   private boolean vaitingForNewOp=false;
   private DiskoReport diskoReport = null;

    /**
    * The main method.
    *
    * @param args
    */
   public static void main(String[] args)
   {
      com.esri.arcgis.system.EngineInitializer.initializeVisualBeans();
      initLookAndFeel();

      // TODO Auto-generated method stub
      SwingUtilities.invokeLater(new Runnable()
      {
         public void run()
         {
            DiskoApplicationImpl thisClass = new DiskoApplicationImpl();
            thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         }
      });
   }

   private Object[] loggedin=new Object[3];
   private final static String DefaultFont = "Tahoma";
   private final static String DialogFont = "Dialog";

   private final static int MediumSize = 14;
   private final static int LargeSize = 16;

   private final static Font DEFAULT_BOLD_SMALL_FONT = new Font(DefaultFont, Font.PLAIN, 10);

   private final static Font DEFAULT_PLAIN_MEDIUM_FONT = new Font(DefaultFont, Font.PLAIN, MediumSize);
   //private final static Font DEFAULT_BOLD_MEDIUM_FONT = new Font(DefaultFont,Font.BOLD,MediumSize);
   private final static Font DEFAULT_PLAIN_LARGE_FONT = new Font(DefaultFont, Font.PLAIN, LargeSize);
   //private final static Font DEFAULT_BOLD_LARGE = new Font(DefaultFont,Font.BOLD,LargeSize);

   private final static Font DIALOG_PLAIN_MEDIUM_FONT = new Font(DialogFont, Font.PLAIN, MediumSize);

   private static void initLookAndFeel()
   {
      try
      {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

         //UIManager.put("Button.font", DEFAULT_PLAIN_MEDIUM_FONT);
          UIManager.put("Button.font", DEFAULT_BOLD_SMALL_FONT);
         UIManager.put("CheckBox.font", DEFAULT_PLAIN_LARGE_FONT);
         UIManager.put("CheckBoxMenuItem.acceleratorFont", DIALOG_PLAIN_MEDIUM_FONT);
         UIManager.put("CheckBoxMenuItem.font", DEFAULT_PLAIN_LARGE_FONT);
         UIManager.put("ColorChooser.font", DIALOG_PLAIN_MEDIUM_FONT);
         UIManager.put("ComboBox.font", DEFAULT_PLAIN_MEDIUM_FONT);
         UIManager.put("EditorPane.font", DEFAULT_PLAIN_MEDIUM_FONT);
         UIManager.put("FileChooser.listFont", DEFAULT_PLAIN_MEDIUM_FONT);
         UIManager.put("FormattedTextField.font", DEFAULT_PLAIN_MEDIUM_FONT);
//            UIManager.put("InternalFrame.titleFont",font Trebuchet MS,bold,13);
            UIManager.put("Label.font",DEFAULT_PLAIN_MEDIUM_FONT);
            UIManager.put("List.font",DEFAULT_PLAIN_MEDIUM_FONT);
            UIManager.put("Menu.acceleratorFont",DIALOG_PLAIN_MEDIUM_FONT);
            UIManager.put("Menu.font",DEFAULT_PLAIN_MEDIUM_FONT);
            UIManager.put("MenuBar.font",DEFAULT_PLAIN_MEDIUM_FONT);
            UIManager.put("MenuItem.acceleratorFont",DEFAULT_PLAIN_MEDIUM_FONT);
            UIManager.put("MenuItem.font",DEFAULT_PLAIN_LARGE_FONT);
            UIManager.put("OptionPane.buttonFont",DEFAULT_PLAIN_MEDIUM_FONT);
            UIManager.put("OptionPane.font",DEFAULT_PLAIN_MEDIUM_FONT);
            UIManager.put("OptionPane.messageFont",DEFAULT_PLAIN_MEDIUM_FONT);
            UIManager.put("Panel.font",DEFAULT_PLAIN_MEDIUM_FONT);
            UIManager.put("PasswordField.font",DEFAULT_PLAIN_MEDIUM_FONT);
            UIManager.put("PopupMenu.font",DEFAULT_PLAIN_MEDIUM_FONT);
            UIManager.put("ProgressBar.font",DEFAULT_PLAIN_MEDIUM_FONT);
            UIManager.put("RadioButton.font",DEFAULT_PLAIN_LARGE_FONT);
            UIManager.put("RadioButtonMenuItem.acceleratorFont",DIALOG_PLAIN_MEDIUM_FONT);
            UIManager.put("RadioButtonMenuItem.font", DEFAULT_PLAIN_LARGE_FONT);
            UIManager.put("ScrollPane.font",DEFAULT_PLAIN_MEDIUM_FONT);
            UIManager.put("Slider.font",DEFAULT_PLAIN_MEDIUM_FONT);
            UIManager.put("Spinner.font",DEFAULT_PLAIN_MEDIUM_FONT);
            UIManager.put("TabbedPane.font",DEFAULT_PLAIN_MEDIUM_FONT);
            UIManager.put("Table.font",DEFAULT_PLAIN_MEDIUM_FONT);
            UIManager.put("TableHeader.font",DEFAULT_PLAIN_MEDIUM_FONT);
  //          UIManager.put("TextArea.font",font Monospaced,plain,13);
            UIManager.put("TextField.font",DEFAULT_PLAIN_MEDIUM_FONT);
            UIManager.put("TextPane.font",DEFAULT_PLAIN_MEDIUM_FONT);
            UIManager.put("TitledBorder.font",DEFAULT_PLAIN_MEDIUM_FONT);
            UIManager.put("ToggleButton.font",DEFAULT_PLAIN_MEDIUM_FONT);
            UIManager.put("ToolBar.font",DEFAULT_PLAIN_MEDIUM_FONT);
            UIManager.put("ToolTip.font",DEFAULT_PLAIN_MEDIUM_FONT);
            UIManager.put("Tree.font",DEFAULT_PLAIN_MEDIUM_FONT);
            UIManager.put("Viewport.font",DEFAULT_PLAIN_MEDIUM_FONT);

//            UIManager.put("ScrollBar.width", 30);

      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   /**
    * This is the default constructor
    */
   public DiskoApplicationImpl()
   {
      super();
      initializeArcGISLicenses();
      initialize();
   }

   private void initialize()
   {
      try
      {
         this.setContentPane(getUIFactory().getContentPanel());
         this.pack();
         //getFrame().setExtendedState(Frame.MAXIMIZED_BOTH);
         Log.init("DISKO");
         // show me
         setVisible(true);
         //initiate modeldriver
         getMsoModel().getModelDriver().initiate();
         getMsoModel().getModelDriver().setDiskoApplication(this);
         // show the login dialog
         LoginDialog loginDialog = getUIFactory().getLoginDialog();
         loginDialog.setLocationRelativeTo((JComponent)getFrame().getContentPane(),LoginDialog.POS_CENTER,false);
         loginDialog.setVisible(true);
      }
      catch (Exception e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   void initializeArcGISLicenses()
   {
      try
      {
         com.esri.arcgis.system.AoInitialize ao = new com.esri.arcgis.system.AoInitialize();
         if (ao.isProductCodeAvailable(com.esri.arcgis.system.esriLicenseProductCode.esriLicenseProductCodeEngine) == com.esri.arcgis.system.esriLicenseStatus.esriLicenseAvailable)
         {
            ao.initialize(com.esri.arcgis.system.esriLicenseProductCode.esriLicenseProductCodeEngine);
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   /* (non-Javadoc)
   * @see org.redcross.sar.app.IDiskoApplication#getCurrentRole()
   */
   public IDiskoRole getCurrentRole()
   {
      return currentRole;
   }


   /* (non-Javadoc)
   * @see org.redcross.sar.app.IDiskoApplication#getCurrentMap()
   */
   public IDiskoMap getCurrentMap()
   {
      return getCurrentRole().getCurrentDiskoWpModule().getMap();
   }

   /* (non-Javadoc)
   * @see org.redcross.sar.app.IDiskoApplication#getFrame()
   */
   public JFrame getFrame()
   {
      return this;
   }

   /* (non-Javadoc)
   * @see org.redcross.sar.app.IDiskoApplication#getUIFactory()
   */
   public UIFactory getUIFactory()
   {
      if (uiFactory == null)
      {
         uiFactory = new UIFactory(this);
      }
      return uiFactory;
   }

   public NavBar getNavBar()
   {
      return getUIFactory().getMainPanel().getNavBar();
   }

   public SysBar getSysBar()
   {
      return getUIFactory().getMainPanel().getSysBar();
   }

   /* (non-Javadoc)
   * @see org.redcross.sar.app.IDiskoApplication#getDiskoMapManager()
   */
   public IDiskoMapManager getDiskoMapManager()
   {
      if (mapManager == null)
      {
         mapManager = new DiskoMapManagerImpl(this);
      }
      return mapManager;
   }

   public IMsoModelIf getMsoModel()
   {
      if (m_msoModel == null)
      {
         m_msoModel = MsoModelImpl.getInstance();
      }
      return m_msoModel;
   }
   
   public DiskoReport getDiskoReport(){
 	   return this.diskoReport;
   }

   /* (non-Javadoc)
   * @see org.redcross.sar.app.IDiskoApplication#chooseActiveOperation()
   */
   public void chooseActiveOperation()
   {
      java.util.List<String[]> opList = getMsoModel().getModelDriver().getActiveOperations();
       String opId=null;
      if (opList.size() > 1)
      {
         String[] options = new String[opList.size()];
         for (int i = 0; i < opList.size(); i++)
         {
            options[i] = opList.get(i)[0];
         }
         int ans = JOptionPane.showOptionDialog(null, bundle.getString(CHOOSEOPDESC), bundle.getString(CHOOSETEXT),
               JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
         if (ans >= 0)
         {
            opId = opList.get(ans)[1];
         }
      }
      else if(opList.size()==1)
      {
          opId = opList.get(0)[1];
      }

       setActiveOp(opId);
   }

    private void setActiveOp(String opId)
    {
        if(opId!=null)
       {
            getMsoModel().getModelDriver().setActiveOperation(opId);
            loadModules();
       }
    }

    /* (non-Javadoc)
    * @see org.redcross.sar.app.IDiskoApplication#finishOperation()
    */
   public void finishOperation()
   {
      getMsoModel().getModelDriver().finishActiveOperation();
   }

    public void operationFinished()
    {
        java.util.List<String[]> opList = getMsoModel().getModelDriver().getActiveOperations();

        String[] options = {bundle.getString("QUIT.TEXT"), bundle.getString("NEWACTION.TEXT")};
        int ans = JOptionPane.showOptionDialog(
                uiFactory.getContentPanel(),
                bundle.getString(OPERATION_FINISHED_TEXT),
                bundle.getString(OPERATION_FINISHED_TITLE),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if(ans==JOptionPane.YES_OPTION)
        {
            shutdown();
        }
        if(opList.size()>0)
        {
            chooseActiveOperation();
        }
        else
        {
            //Wait for new operation to occour
            getUIFactory().getContentPanel().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            SwingWorker sw = new SwingWorker<String, Object>()
            {
               @Override
               protected String doInBackground() throws Exception
               {
                  long maxTime = Long.parseLong(getProperty("max.wait.time", "" + 60 * 1000));
                  long currTime = System.currentTimeMillis();
                  long startTime = System.currentTimeMillis();
                  while ((currTime - startTime) < maxTime)
                  {
                     if (getMsoModel().getModelDriver().getActiveOperations().size()>0)
                     {
                        //Get back onto awt thread
                        return "true";
                     }
                     else
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
                     currTime = System.currentTimeMillis();
                  }
                  return "false";
               }

               @Override
               protected void done()
               {
                  try
                  {
                     getUIFactory().getContentPanel().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                     if (Boolean.parseBoolean(get()))
                     {
                        chooseActiveOperation();
                        return;
                     }
                      else
                     {
                         JOptionPane.showMessageDialog(uiFactory.getContentPanel(), bundle.getString(INIT_ERROR_SHUTDOWN_TEXT),
                        bundle.getString(INIT_ERROR_TEXT), JOptionPane.ERROR_MESSAGE);
                         shutdown();
                     }

                  }
                  catch (Exception e)
                  {
                     e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                  }
                  //modeldriver is not initiated display message to user and shut down
                  JOptionPane.showMessageDialog(uiFactory.getContentPanel(), bundle.getString(INIT_ERROR_SHUTDOWN_TEXT),
                        bundle.getString(INIT_ERROR_TEXT), JOptionPane.ERROR_MESSAGE);
                   shutdown();

               }

            };
             sw.execute();

        }


    }

    /* (non-Javadoc)
    * @see org.redcross.sar.app.IDiskoApplication#mergeOperations()
    */
   public void mergeOperations()
   {
      JOptionPane.showMessageDialog(null, "Merge not implemented yet", "Not implemented", JOptionPane.WARNING_MESSAGE);
   }

    public void newOperation()
    {
        vaitingForNewOp=true;
         getMsoModel().getModelDriver().createNewOperation();
    }

    public void operationAdded(String id)
    {
        if(vaitingForNewOp)
        {
            vaitingForNewOp=false;
            JOptionPane.showMessageDialog(uiFactory.getContentPanel(), bundle.getString(OPERATION_CREATED_TEXT),
                   bundle.getString(OPERATION_CREATED_TITLE), JOptionPane.INFORMATION_MESSAGE);

            setActiveOp(id);
        }
    }

    /* (non-Javadoc)
    * @see org.redcross.sar.app.IDiskoApplication#login(java.lang.String, java.lang.String, char[])
    */
   public void login(String roleName, String user, char[] password)
   {
       loggedin[0]=roleName;
       loggedin[1]=user;
       loggedin[2]=password;
       //loadModules();
      if (getMsoModel().getModelDriver().isInitiated())
      {
         chooseActiveOperation();
      }
      else
      {
         //wait for modeldriver to become initiated
         getUIFactory().getContentPanel().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
         SwingWorker sw = new SwingWorker<String, Object>()
         {
            @Override
            protected String doInBackground() throws Exception
            {
               long maxTime = Long.parseLong(getProperty("max.wait.time", "" + 60 * 1000));
               long currTime = System.currentTimeMillis();
               long startTime = System.currentTimeMillis();
               while ((currTime - startTime) < maxTime)
               {
                  if (getMsoModel().getModelDriver().isInitiated())
                  {
                     //Get back onto awt thread
                     return "true";
                  }
                  else
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
               }
               return "false";
            }

            @Override
            protected void done()
            {
               try
               {
                  getUIFactory().getContentPanel().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                  if (Boolean.parseBoolean(get()))
                  {
                     chooseActiveOperation();
                     return;
                  }
                   else
                  {
                      JOptionPane.showMessageDialog(uiFactory.getContentPanel(), bundle.getString(INIT_ERROR_SHUTDOWN_TEXT),
                     bundle.getString(INIT_ERROR_TEXT), JOptionPane.ERROR_MESSAGE);
                      shutdown();
                  }

               }
               catch (Exception e)
               {
                  e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
               }
               //modeldriver is not initiated display message to user and shut down
               JOptionPane.showMessageDialog(uiFactory.getContentPanel(), bundle.getString(INIT_ERROR_SHUTDOWN_TEXT),
                     bundle.getString(INIT_ERROR_TEXT), JOptionPane.ERROR_MESSAGE);
                shutdown();


            }

         };
          sw.execute();
      }
   }

    public void shutdown()
    {
        getMsoModel().getModelDriver().shutDown(); //Set last
        System.exit(0);

    }

    private void loadModules()
    {
        String roleName=(String)loggedin[0];
        if (roles == null)
        {
           roles = new Hashtable<String, IDiskoRole>();
        }
        IDiskoRole role = (IDiskoRole) roles.get(roleName);
        if (role == null)
        {
           try
           {
              role = getDiskoModuleLoader().parseRole(roleName);
              roles.put(roleName, role);
           }
           catch (Exception e1)
           {
              // TODO Auto-generated catch block
              e1.printStackTrace();
           }
        }
        currentRole = role;
        role.selectDiskoWpModule(0);
        getUIFactory().getMainMenuPanel().showMenu(role.getName());
        getUIFactory().getMenuPanel().setVisible(true);
    }

    /* (non-Javadoc)
    * @see org.redcross.sar.app.IDiskoApplication#getDiskoModuleLoader()
    */
   public DiskoModuleLoader getDiskoModuleLoader()
   {
      if (moduleLoader == null)
      {
         try
         {
            moduleLoader = new DiskoModuleLoader(this,
                  new File("DiskoModules.xml"));
         }
         catch (Exception e2)
         {
            // TODO Auto-generated catch block
            e2.printStackTrace();
         }
      }
      return moduleLoader;
   }

   public Properties getProperties()
   {
      if (properties == null)
      {
         try
         {
            properties = Utils.loadProperties("properties");
         }
         catch (Exception ex)
         {
            ex.printStackTrace();
         }
      }
      return properties;
   }

   /* (non-Javadoc)
   * @see org.redcross.sar.app.IDiskoApplication#getProperty(java.lang.String)
   */
   public String getProperty(String key)
   {
      return getProperties().getProperty(key);
   }

   /* (non-Javadoc)
   * @see org.redcross.sar.app.IDiskoApplication#getProperty(java.lang.String)
   */
   public String getProperty(String key, String defaultvalue)
   {
      return getProperties().getProperty(key, defaultvalue);
   }

}  // @jve:decl-index=0:visual-constraint="10,10"
