package org.redcross.sar.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.app.Utils;


public class SysBar extends JPanel {

	private static final long serialVersionUID = 1L;
	private IDiskoApplication app = null;
	private ButtonGroup bgroup = null;
	private JButton changeRolleButton = null;
	private JButton mapOptionButton = null;

	/**
	 * This is the default constructor
	 */
	public SysBar() {
		super(null);
	}
	
	public SysBar(IDiskoApplication app) {
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
		bgroup = new ButtonGroup();
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setHgap(0);
		flowLayout.setVgap(0);
		flowLayout.setAlignment(FlowLayout.RIGHT);
		this.setLayout(flowLayout);
		addButton(getChangeRolleButton());
		addButton(getMapOptionButton());
	}
	
	private JButton getChangeRolleButton() {
		if (changeRolleButton == null) {
			try {
				changeRolleButton = new JButton();
				String iconName = "SwitchRole.icon";
				Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				changeRolleButton.setIcon(icon);
				Dimension size = app.getUIFactory().getSmallButtonSize();
				changeRolleButton.setPreferredSize(size);
				changeRolleButton.setIcon(icon);
				changeRolleButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						LoginDialog loginDialog = app.getUIFactory().getLoginDialog();
						loginDialog.setVisible(true);
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return changeRolleButton;
	}
	
	private JButton getMapOptionButton() {
		
		if (mapOptionButton == null) {
			try {
				mapOptionButton = new JButton();
				String iconName = "Kart.icon";
				Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				mapOptionButton.setIcon(icon);
				Dimension size = app.getUIFactory().getSmallButtonSize();
				mapOptionButton.setPreferredSize(size);
				mapOptionButton.setIcon(icon);
				mapOptionButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						MapOptionDialog mapOptionDialog = app.getUIFactory().getMapOptionDialog();
						mapOptionDialog.setVisible(true);
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return mapOptionButton;
	}
	
	public void addButton(AbstractButton button) {
		add(button);
		if (button instanceof JToggleButton) {
			bgroup.add(button);
		}
	}
	
	public void removeButton(AbstractButton button) {
		remove(button);
		if (button instanceof JToggleButton) {
			bgroup.remove(button);
		}
	}
}
