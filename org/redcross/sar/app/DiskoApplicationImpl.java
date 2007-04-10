package org.redcross.sar.app;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.UIManager;

import org.redcross.sar.gui.LoginDialog;
import org.redcross.sar.gui.UIFactory;
import org.redcross.sar.map.DiskoMap;

import java.io.File;
import java.util.Hashtable;
import java.util.Properties;


/**
 * Implements the DiskoApplication interface. This class is responsible for connecting to the 
 * ArcGIS Engine API and the DiskoModule API.
 * 
 */
public class DiskoApplicationImpl extends JFrame implements IDiskoApplication {

	private static final long serialVersionUID = 1L;
	private IDiskoRole currentRole = null;
	private Hashtable<String, IDiskoRole> roller = null;
	private DiskoModuleLoader moduleLoader = null;
	private Properties properties = null;
	private UIFactory uiFactory = null;
	
	/**
	 * The main method.
	 * @param args
	 */
	public static void main(String[] args) {
		com.esri.arcgis.system.EngineInitializer.initializeVisualBeans();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				DiskoApplicationImpl thisClass = new DiskoApplicationImpl();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}

	/**
	 * This is the default constructor
	 */
	public DiskoApplicationImpl() {
		super();
		initializeArcGISLicenses();
		initialize();
	}

	private void initialize() {
		try {
			this.setContentPane(getUIFactory().getContentPanel());
			this.pack();
			// show the login dialog
			LoginDialog loginDialog = getUIFactory().getLoginDialog();
			loginDialog.setVisible(true);
			loginDialog.setLocation(300,300);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void initializeArcGISLicenses() {
		try {
			com.esri.arcgis.system.AoInitialize ao = new com.esri.arcgis.system.AoInitialize();
			if (ao.isProductCodeAvailable(com.esri.arcgis.system.esriLicenseProductCode.esriLicenseProductCodeEngine) == com.esri.arcgis.system.esriLicenseStatus.esriLicenseAvailable) {
				ao.initialize(com.esri.arcgis.system.esriLicenseProductCode.esriLicenseProductCodeEngine);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.app.IDiskoApplication#getCurrentRole()
	 */
	public IDiskoRole getCurrentRole() {
		return currentRole;
	}
	
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.app.IDiskoApplication#getCurrentMap()
	 */
	public DiskoMap getCurrentMap() {
		return getCurrentRole().getCurrentDiskoWpModule().getMap();
	}
	
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.app.IDiskoApplication#getFrame()
	 */
	public JFrame getFrame() {
		return this;
	}
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.app.IDiskoApplication#getUIFactory()
	 */
	public UIFactory getUIFactory() {
		if (uiFactory == null) {
			uiFactory = new UIFactory(this);
		}
		return uiFactory;
	}
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.app.IDiskoApplication#login(java.lang.String, java.lang.String, char[])
	 */
	public void login(String rolleName, String user, char[] password) {
		if (roller == null) {
			roller = new Hashtable<String,IDiskoRole>();
		}
		IDiskoRole role = (IDiskoRole) roller.get(rolleName);
		if (role == null) {
			try {
				role = getDiskoModuleLoader().parseRole(rolleName);
				roller.put(rolleName, role);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		currentRole = role;
		System.out.println(role.getName());
		role.selectDiskoWpModule(0);
		getUIFactory().getMainMenuPanel().showMenu(role.getName());
		getUIFactory().getMenuPanel().setVisible(true);
		pack();
	}
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.app.IDiskoApplication#getDiskoModuleLoader()
	 */
	public DiskoModuleLoader getDiskoModuleLoader() {
		if (moduleLoader == null) {
			try {
				moduleLoader = new DiskoModuleLoader(this, 
						new File("DiskoModules.xml"));
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
		return moduleLoader;
	}
	
	private Properties getProperties() {
		if (properties == null) {
			try {
				properties = Utils.getProperties("properties");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return properties;
	}
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.app.IDiskoApplication#getProperty(java.lang.String)
	 */
	public String getProperty(String key) {
		return getProperties().getProperty(key);
	}
	
}  // @jve:decl-index=0:visual-constraint="10,10"
