package org.redcross.sar.gui;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.BorderFactory;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;

import org.redcross.sar.event.DialogEvent;
import org.redcross.sar.event.IDialogEventListener;

public class DiskoDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	public static final int POS_WEST   = 1;
	public static final int POS_NORTH  = 2;
	public static final int POS_EAST   = 3;
	public static final int POS_SOUTH  = 4;
	public static final int POS_CENTER = 5;
	
	private ArrayList<IDialogEventListener> listeners = null;  //  @jve:decl-index=0:
	
	private JToggleButton toggleButton = null;
	private JDialog navDialogToggle = null;
	private JComponent positionComp = null;
	private int positionPolicy = POS_CENTER;
	private boolean sizeToFit = false;
	private boolean isToggable = false;
	private int width  = -1;
	private int height = -1;

	/**
	 * @param owner
	 */
	public DiskoDialog(Frame owner) {
		super(owner);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setUndecorated(true);
		listeners = new ArrayList<IDialogEventListener>();
	}
	
	public void addDialogListener(IDialogEventListener listener) {
		listeners.add(listener);
	}
	
	public void removeDialogListener(IDialogEventListener listener) {
		listeners.remove(listener);
	}
	
	protected void fireDialogFinished() {
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).dialogFinished(new DialogEvent(this));
		}
	}
	
	protected void fireDialogCanceled() {
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).dialogCanceled(new DialogEvent(this));
		}
	}
	
	protected void fireDialogStateChanged() {
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).dialogStateChanged(new DialogEvent(this));
		}
	}
	
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (isToggable) {
			if (visible) {
				getNavDialogToggle().setVisible(true);
				getToggleButton().setSelected(true);
				getToggleButton().setText("<<");
			}
			else {
				getNavDialogToggle().setVisible(false);
			}
		}
		updatePosition();
	}
	
	public void setLocationRelativeTo(JComponent comp, int posPolicy, boolean sizeToFit) {
		this.positionComp = comp;
		this.positionPolicy = posPolicy;
		this.sizeToFit = sizeToFit;
		
		positionComp.addComponentListener(new ComponentListener() {
			public void componentHidden(ComponentEvent arg0) {
			}
			public void componentMoved(ComponentEvent arg0) {
			}
			public void componentResized(ComponentEvent arg0) {
				updatePosition();
			}
			public void componentShown(ComponentEvent arg0) {
				updatePosition();
			}
		});
		updatePosition();
	}
	
	public void setIsToggable(boolean isToggable) {
		this.isToggable = isToggable;
		updatePosition();
	}
	
	private void updatePosition() {
		if (positionComp == null || !positionComp.isShowing()) {
			return;
		}
		if (width == -1 && height == -1) {
			width  = getWidth();
			height = getHeight();
		}
		int offset = 2;
		int x = positionComp.getLocationOnScreen().x;
		int y = positionComp.getLocationOnScreen().y;
		int w = 0;
		int h = 0;
		switch (positionPolicy) {
			case POS_WEST:
				w = width;
				h = positionComp.getHeight();
				x += offset;
				y += offset;
				break;
			case POS_NORTH:
				w = positionComp.getWidth();
				h = height;
				x += offset;
				y += offset;
				break;
			case POS_EAST:
				w = width;
				h = positionComp.getHeight();
				x += (positionComp.getWidth() - w + offset);
				y += offset;
				break;
			case POS_SOUTH:
				w = positionComp.getWidth();
				h = height;
				x += offset;
				y += (positionComp.getHeight()- h + offset);
				break;
			case POS_CENTER:
				w = width;
				h = height;
				x += (positionComp.getWidth() /2)-(w/2);
				y += (positionComp.getHeight()/2)-(h/2);
				break;
		}
		if (sizeToFit && positionPolicy != POS_CENTER) {
			setSize(w-offset*2, h-offset*2);
		}
		setLocation(x, y);
		validate();
		if (isToggable) {
			int yy = sizeToFit ? y+getHeight()-getNavDialogToggle().getHeight() : y;
			getNavDialogToggle().setLocation(x, yy);
		}
	}
	
	private JDialog getNavDialogToggle() {
		if (navDialogToggle == null) {
			navDialogToggle = new JDialog((Frame)getOwner());
			navDialogToggle.setAlwaysOnTop(true);
			JPanel contenPane = (JPanel)navDialogToggle.getContentPane();
			contenPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			contenPane.add(getToggleButton());
			navDialogToggle.setUndecorated(true);
			navDialogToggle.pack();
		}
		return navDialogToggle;
	}
	
	public JToggleButton getToggleButton() {
		if (toggleButton == null) {
			try {
				toggleButton = new JToggleButton("<<");
				toggleButton.setSelected(true);
				toggleButton.setPreferredSize(new Dimension(50, 50));
				toggleButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						if (toggleButton.isSelected()) {
							toggleButton.setText("<<");
							setVisible(true);
						}
						else {
							toggleButton.setText(">>");
							setVisible(false);
							getNavDialogToggle().setVisible(true);
						}				
					}
				});
				
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return toggleButton;
	}
}
