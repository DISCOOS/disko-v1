package org.redcross.sar.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.Hashtable;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.app.Utils;
import org.redcross.sar.wp.IDiskoWp;

import com.borland.jbcl.layout.VerticalFlowLayout;

public class SubMenuPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private IDiskoApplication app = null;
	private JPanel menuPanel = null;
	private ButtonGroup bgroup = new ButtonGroup();  //  @jve:decl-index=0:
	private Hashtable<String, JPanel> panels = null;
	private JButton cancelButton = null;
	private JButton finishButton = null;
	/**
	 * This is the default constructor
	 */
	public SubMenuPanel() {
		this(null);
	}
	
	public SubMenuPanel(IDiskoApplication app) {
		super();
		this.app = app;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		panels = new Hashtable<String, JPanel>();
		this.setLayout(new BorderLayout());
		this.add(getCancelButton(), BorderLayout.NORTH);
		this.add(getMenuPanel(), BorderLayout.CENTER);
		this.add(getFinishButton(), BorderLayout.SOUTH);
	}
	
	/**
	 * Add a new Button to this MainMenuPanel. The button will added to a panel
	 * in the CardLayout with the given name. If this panel does not exists, 
	 * a new panel will be created.
	 * 
	 * @param button The button to add
	 * @param menuName A name to identify a panel (menu) in the CardLayout
	 */
	public void addItem(AbstractButton button, String menuName, boolean addToGroup) {
		JPanel panel = (JPanel)panels.get(menuName);
		if (panel == null) {
			panel = new JPanel();
			VerticalFlowLayout vfl = new VerticalFlowLayout();
			vfl.setVgap(0);
			vfl.setHgap(0);
			vfl.setAlignment(VerticalFlowLayout.TOP);
			panel.setLayout(vfl);
			getMenuPanel().add(panel, menuName);
			panels.put(menuName, panel);
		}
		panel.add(button);
		if (button instanceof JToggleButton && addToGroup) {
			bgroup.add(button);
		}
	}
	
	public void addItem(AbstractButton button, String menuName) {
		addItem(button,menuName, false);
	}
	
	/**
	 * Show a panel in the CardLayout with the given name.
	 * @param menuName The name of the panel (menu).
	 */
	public void showMenu(String menuName) {
		CardLayout cl = (CardLayout)getMenuPanel().getLayout();
		cl.show(getMenuPanel(), menuName);
	}

	/**
	 * This method initializes menuPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getMenuPanel() {
		if (menuPanel == null) {
			try {
				menuPanel = new JPanel();
				menuPanel.setLayout(new CardLayout());
				menuPanel.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return menuPanel;
	}
	
	public JButton getCancelButton() {
		if (cancelButton == null) {
			try {
				cancelButton = new JButton();
				String iconName = "cancel.icon";
				Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				cancelButton.setIcon(icon);
				Dimension size = app.getUIFactory().getLargeButtonSize();
				cancelButton.setPreferredSize(size);
				cancelButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						/*String msg = "Dette vil slette allt som er gjort til nå."+
						"Vil du allikevel avbryte?";
						if (showYesNoDialog(msg) == 0) {*/
							IDiskoWp wp = (IDiskoWp)app.getCurrentRole().getCurrentDiskoWpModule();
							wp.cancel();
						//}
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return cancelButton;
	}

	/**
	 * This method initializes sysToggleButton	
	 * 	
	 * @return javax.swing.JToggleButton	
	 */
	public JButton getFinishButton() {
		if (finishButton == null) {
			try {
				finishButton = new JButton();
				String iconName = "finish.icon";
				Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				finishButton.setIcon(icon);
				Dimension size = app.getUIFactory().getLargeButtonSize();
				finishButton.setPreferredSize(size);
				finishButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						IDiskoWp wp = (IDiskoWp)app.getCurrentRole().getCurrentDiskoWpModule();
						wp.finish();
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return finishButton;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
