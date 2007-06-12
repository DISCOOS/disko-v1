package org.redcross.sar.app;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JToggleButton;
import javax.swing.border.Border;

import org.redcross.sar.event.DiskoWpEvent;
import org.redcross.sar.event.IDiskoWpEventListener;
import org.redcross.sar.gui.MainMenuPanel;
import org.redcross.sar.gui.MainPanel;
import org.redcross.sar.gui.NavBar;
import org.redcross.sar.gui.SubMenuPanel;
import org.redcross.sar.wp.IDiskoWpModule;

import com.esri.arcgis.interop.AutomationException;


/**
 * Implements the DiskoRolle interface.
 * @author geira
 */
/**
 * @author geira
 *
 */
public class DiskoRoleImpl implements IDiskoRole, IDiskoWpEventListener {
	
	private IDiskoApplication app = null;
	private String name = null;
	private String title = null;
	private String description = null;
	private IDiskoWpModule currentModule = null;
	private ArrayList<IDiskoWpModule> modules = null;
	private Border bussyBorder = null;
	private Border normalBorder = null;
	
	/**
	 * Constructs a DiskoRolleImpl
	 * @param app A reference to DiskoApplication
	 */
	public DiskoRoleImpl(IDiskoApplication app) {
		this(app, null, null, null);
	}
	
	/**
	 * Constructs a DiskoRoleImpl
	 * @param app A reference to DiskoApplication
	 * @param name The name of this 
	 * @param title The title
	 * @param description The description
	 */
	public DiskoRoleImpl(IDiskoApplication app, String name, String title, String description) {
		this.app = app;
		this.name = name;
		this.title = title;
		this.description = description;
		this.modules = new ArrayList<IDiskoWpModule>();
		this.bussyBorder = BorderFactory.createLineBorder(Color.red, 2);
	}
	
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.app.IDiskoRole#addDiskoWpModule(org.redcross.sar.wp.IDiskoWpModule)
	 */
	public void addDiskoWpModule(final IDiskoWpModule module) {
		try {
			final String id = getName()+module.getName();
			module.addDiskoWpEventListener(this);
			JToggleButton tbutton = new JToggleButton();
			normalBorder = tbutton.getBorder();
			String iconName = module.getName()+".icon";
			Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
			tbutton.setIcon(icon);
			Dimension size = app.getUIFactory().getLargeButtonSize();
			tbutton.setPreferredSize(size);
			tbutton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					selectDiskoWpModule(id);
				}
			});
			MainMenuPanel mainMenuPanel = app.getUIFactory().getMainMenuPanel();
			mainMenuPanel.addItem(tbutton, getName());
			modules.add(module);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private int searchModule(String id) {
		for (int i = 0; i < modules.size(); i++) {
			if ((getName()+modules.get(i).getName()).equals(id)) {
				return i;
			}
		}
		return -1;
	}
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.app.IDiskoRole#selectDiskoWpModule(java.lang.String)
	 */
	public void selectDiskoWpModule(String id)  {
		int index = searchModule(id);
		if (index > -1) {
			selectDiskoWpModule(index);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.app.IDiskoRole#selectDiskoWpModule(int)
	 */
	public void selectDiskoWpModule(int index) {
		if (index < modules.size()) {
			if (currentModule != null) {
				// deactiat previous module
				try {
					currentModule.getMap().setCurrentToolByRef(null);
				} catch (AutomationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				currentModule.deactivated();
			}
			IDiskoWpModule module = modules.get(index);
			String id = getName()+module.getName();
			MainMenuPanel mainMenu = app.getUIFactory().getMainMenuPanel();
			SubMenuPanel subMenu   = app.getUIFactory().getSubMenuPanel();
			NavBar navBar = app.getUIFactory().getMainPanel().getNavBar();
			MainPanel mainPanel = app.getUIFactory().getMainPanel();
			navBar.setVisible(false);
			subMenu.setVisible(false);
			mainPanel.showComponent(id);
		
			if (module.hasSubMenu()) {
				subMenu.setVisible(true);
				subMenu.showMenu(id);
			}
			if (mainMenu.getNavToggleButton().isSelected()) {
				navBar.setVisible(true);
			}
			currentModule = module;
			module.activated();
			navBar.taskChanged();
			// set the button selected in the main menu
			AbstractButton button = mainMenu.getButton(getName(), index);
			if (button != null) {
				button.setSelected(true);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.app.IDiskoRole#getCurrentDiskoWpModule()
	 */
	public IDiskoWpModule getCurrentDiskoWpModule() {
		return currentModule;
	}
	
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.app.IDiskoRole#getDiskoWpModules()
	 */
	public List getDiskoWpModules() {
		return modules;
	}
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.app.IDiskoRole#getName()
	 */
	public String getName() {
		return name;
	}
	
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.app.IDiskoRole#getTitle()
	 */
	public String getTitle() {
		return title;
	}
	
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.app.IDiskoRole#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/* (non-Javadoc)
	 * @see org.redcross.sar.app.IDiskoRole#getApplication()
	 */
	public IDiskoApplication getApplication() {
		return app;
	}

	public void taskCanceled(DiskoWpEvent e) {
		setBorder(normalBorder);
	}

	public void taskFinished(DiskoWpEvent e) {
		setBorder(normalBorder);
	}

	public void taskStarted(DiskoWpEvent e) {
		setBorder(bussyBorder);
	}
	
	private void setBorder(Border border) {
		MainMenuPanel mainMenu = app.getUIFactory().getMainMenuPanel();
		String id = getName()+currentModule.getName();
		int index = searchModule(id);
		if (index > -1) {
			AbstractButton button = mainMenu.getButton(getName() ,index);
			button.setBorder(border);
		}
	}
}
