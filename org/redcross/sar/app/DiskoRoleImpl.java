package org.redcross.sar.app;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.JToggleButton;

import org.redcross.sar.gui.MainMenuPanel;
import org.redcross.sar.gui.MainPanel;
import org.redcross.sar.gui.NavBar;
import org.redcross.sar.gui.SubMenuPanel;
import org.redcross.sar.wp.IDiskoWpModule;


/**
 * Implements the DiskoRolle interface.
 * @author geira
 */
public class DiskoRoleImpl implements IDiskoRole {
	
	private IDiskoApplication app = null;
	private String name = null;
	private String title = null;
	private String description = null;
	private IDiskoWpModule currentModule = null;
	private ArrayList<IDiskoWpModule> modules = null;
	
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
	}
	
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.app.IDiskoRole#addDiskoWpModule(org.redcross.sar.wp.IDiskoWpModule)
	 */
	public void addDiskoWpModule(final IDiskoWpModule module) {
		final String id = getName()+module.getName();
		JToggleButton tbutton = new JToggleButton();
		tbutton.setText(module.getName());
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
	}
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.app.IDiskoRole#selectDiskoWpModule(java.lang.String)
	 */
	public void selectDiskoWpModule(String id)  {
		for (int i = 0; i < modules.size(); i++) {
			if ((getName()+modules.get(i).getName()).equals(id)) {
				selectDiskoWpModule(i);
				return;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.app.IDiskoRole#selectDiskoWpModule(int)
	 */
	public void selectDiskoWpModule(int index) {
		if (index < modules.size()) {
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
			app.getFrame().setTitle("DISKO "+getTitle()+" "+
					currentModule.getName());
		}
	}
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.app.IDiskoRole#getCurrentDiskoWpModule()
	 */
	public IDiskoWpModule getCurrentDiskoWpModule() {
		return currentModule;
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
}
