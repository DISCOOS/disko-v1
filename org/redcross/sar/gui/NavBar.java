package org.redcross.sar.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.app.Utils;
import org.redcross.sar.map.DiskoMap;
import org.redcross.sar.map.DrawTool;
import org.redcross.sar.map.EraseTool;
import org.redcross.sar.map.FlankTool;
import org.redcross.sar.map.IDiskoTool;
import org.redcross.sar.map.PUITool;
import org.redcross.sar.map.SplitTool;

import com.esri.arcgis.controls.ControlsMapFullExtentCommand;
import com.esri.arcgis.controls.ControlsMapPanTool;
import com.esri.arcgis.controls.ControlsMapZoomInFixedCommand;
import com.esri.arcgis.controls.ControlsMapZoomInTool;
import com.esri.arcgis.controls.ControlsMapZoomOutFixedCommand;
import com.esri.arcgis.controls.ControlsMapZoomOutTool;
import com.esri.arcgis.controls.ControlsMapZoomToLastExtentBackCommand;
import com.esri.arcgis.controls.ControlsMapZoomToLastExtentForwardCommand;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.systemUI.ICommand;
import com.esri.arcgis.systemUI.ITool;

public class NavBar extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	public static final int FLANK_TOOL = 0;
	public static final int DRAW_TOOL = 1;
	public static final int ERASE_TOOL = 2;
	public static final int SPLIT_TOOL = 3;
	public static final int PUI_TOOL = 4;
	public static final int ZOOM_IN_TOOL = 5;
	public static final int ZOOM_OUT_TOOL = 6;
	public static final int PAN_TOOL = 7;
	public static final int ZOOM_IN_FIXED_COMMAND = 8;
	public static final int ZOOM_OUT_FIXED_COMMAND = 9;
	public static final int ZOOM_FULL_EXTENT_COMMAND = 10;
	public static final int ZOOM_TO_LAST_EXTENT_FORWARD_COMMAND = 11;
	public static final int ZOOM_TO_LAST_EXTENT_BACKWARD_COMMAND = 12;
	
	private IDiskoApplication app = null;
	private ButtonGroup bgroup  = null;
	private JToggleButton dummyToggleButton = null;
	private ArrayList<ICommand> commands  = null;
	private ArrayList<AbstractButton> buttons  = null;
	private JToggleButton flankToggleButton = null;
	private JToggleButton drawLineToggleButton = null;
	private JToggleButton eraseToggleButton = null;
	private JToggleButton splitToggleButton = null;
	private JToggleButton puiToggleButton = null;	
	private JToggleButton zoomInToggleButton = null;
	private JToggleButton zoomOutToggleButton = null;
	private JToggleButton panToggleButton = null;
	private JButton zoomInFixedButton = null;
	private JButton zoomOutFixedButton = null;
	private JButton fullExtentButton = null;
	private JButton zoomToLastExtentForwardButton = null;
	private JButton zoomToLastExtentBackwardButton = null;
	
	private DrawTool drawTool = null;
	private FlankTool flankTool = null;
	private EraseTool eraseTool = null;
	private SplitTool splitTool = null;
	private PUITool puiTool = null;
	private ControlsMapZoomInTool zoomInTool = null;
	private ControlsMapZoomOutTool zoomOutTool = null;
	private ControlsMapPanTool panTool = null;
	private ControlsMapZoomInFixedCommand zoomInFixedCommand = null;
	private ControlsMapZoomOutFixedCommand zoomOutFixedCommand = null;
	private ControlsMapFullExtentCommand fullExtentCommand = null;
	private ControlsMapZoomToLastExtentForwardCommand zoomToLastExtentForwardCommand = null;
	private ControlsMapZoomToLastExtentBackCommand zoomToLastExtentBackCommand = null;
	
	public NavBar() {
		this(null);
	}
	
	public NavBar(IDiskoApplication app) {
		this.app = app;
		initialize();
	}
	
	private void initialize() {
		commands = new ArrayList<ICommand>();
		buttons = new ArrayList<AbstractButton>();
		bgroup = new ButtonGroup();
			
		FlowLayout flowLayout = new FlowLayout();
		flowLayout.setHgap(0);
		flowLayout.setVgap(0);
		flowLayout.setAlignment(FlowLayout.RIGHT);
			setLayout(flowLayout);
			
		// Add a not visible dummy JToggleButton, used to unselect all
		// (visbible) JToggleButtons. This is a hack suggested by Java dev forum
		JToggleButton dummy = getDummyToggleButton();
		add(dummy);
		bgroup.add(dummy);
			
		addCommand(getFlankToggleButton(), getFlankTool());
		addCommand(getDrawLineToggleButton(), getDrawTool());
		addCommand(getEraseToggleButton(), getEraseTool());
		addCommand(getSplitToggleButton(), getSplitTool());
		addCommand(getPUIToggleButton(), getPUITool());
		addCommand(getZoomInToggleButton(), getZoomInTool());
		addCommand(getZoomOutToggleButton(), getZoomOutTool());
		addCommand(getPanToggleButton(), getPanTool());
		addCommand(getZoomInFixedButton(), getZoomInFixedCommand());
		addCommand(getZoomOutFixedButton(), getZoomOutFixedCommand());
		addCommand(getFullExtentButton(), getFullExtentCommand());
		addCommand(getZoomToLastExtentForwardButton(), getZoomToLastExtentForwardCommand());
		addCommand(getZoomToLastExtentBackwardButton(), getZoomToLastExtentBackCommand());	
	}
	
	
	private DrawTool getDrawTool() {
		if (drawTool == null) {
			try {
				drawTool = new DrawTool(app);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return drawTool;
	}
	
	private FlankTool getFlankTool() {
		if (flankTool == null) {
			try {
				String flayerName = app.getProperty("BufferPath.featureClass.Name");
				flankTool = new FlankTool(app.getFrame(), flayerName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return flankTool;
	}
	
	private EraseTool getEraseTool() {
		if (eraseTool == null) {
			try {
				eraseTool = new EraseTool();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return eraseTool;
	}
	
	private SplitTool getSplitTool() {
		if (splitTool == null) {
			try {
				splitTool = new SplitTool();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return splitTool;
	}
	
	private PUITool getPUITool() {
		if (puiTool == null) {
			try {
				String flayerName = app.getProperty("PUI.featureClass.Name");
				puiTool = new PUITool(app, flayerName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return puiTool;
	}
	
	private ControlsMapZoomInTool getZoomInTool() {
		if (zoomInTool == null) {
			try {
				zoomInTool = new ControlsMapZoomInTool();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return zoomInTool;
	}
	
	private ControlsMapZoomOutTool getZoomOutTool() {
		if (zoomOutTool == null) {
			try {
				zoomOutTool = new ControlsMapZoomOutTool();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return zoomOutTool;
	}
	
	private ControlsMapPanTool getPanTool() {
		if (panTool == null) {
			try {
				panTool = new ControlsMapPanTool();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return panTool;
		
	}
	
	private ControlsMapZoomInFixedCommand getZoomInFixedCommand() {
		if (zoomInFixedCommand == null) {
			try {
				zoomInFixedCommand = new ControlsMapZoomInFixedCommand();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return zoomInFixedCommand;
	}
	
	private ControlsMapZoomOutFixedCommand getZoomOutFixedCommand() {
		if (zoomOutFixedCommand == null) {
			try {
				zoomOutFixedCommand = new ControlsMapZoomOutFixedCommand();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return zoomOutFixedCommand;
		
	}
	
	private ControlsMapFullExtentCommand getFullExtentCommand() {
		if (fullExtentCommand == null) {
			try {
				fullExtentCommand = new ControlsMapFullExtentCommand();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fullExtentCommand;
		
	}
	
	private ControlsMapZoomToLastExtentForwardCommand getZoomToLastExtentForwardCommand() {
		if (zoomToLastExtentForwardCommand == null) {
			try {
				zoomToLastExtentForwardCommand = new ControlsMapZoomToLastExtentForwardCommand();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return zoomToLastExtentForwardCommand;
		
	}
	
	private ControlsMapZoomToLastExtentBackCommand getZoomToLastExtentBackCommand() {
		if (zoomToLastExtentBackCommand == null) {
			try {
				zoomToLastExtentBackCommand = new ControlsMapZoomToLastExtentBackCommand();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return zoomToLastExtentBackCommand;
	}
	
	private JToggleButton getDummyToggleButton() {
		if (dummyToggleButton == null) {
			dummyToggleButton = new JToggleButton();
			dummyToggleButton.setVisible(false);
		}
		return dummyToggleButton;
	}
	
	public JToggleButton getFlankToggleButton() {
		if (flankToggleButton == null) {
			try {
				Dimension size = app.getUIFactory().getSmallButtonSize();
				//String iconName = "MapDrawLineTool.icon";
				//Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				flankToggleButton = new JToggleButton("Fl");
				//flankToggleButton.setIcon(icon);
				flankToggleButton.setPreferredSize(size);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return flankToggleButton;
	}
	
	public JToggleButton getDrawLineToggleButton() {
		if (drawLineToggleButton == null) {
			try {
				Dimension size = app.getUIFactory().getSmallButtonSize();
				String iconName = "MapDrawLineTool.icon";
				Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				drawLineToggleButton = new JToggleButton();
				drawLineToggleButton.setIcon(icon);
				drawLineToggleButton.setPreferredSize(size);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return drawLineToggleButton;
	}
	
	public JToggleButton getSplitToggleButton() {
		if (splitToggleButton == null) {
			try {
				Dimension size = app.getUIFactory().getSmallButtonSize();
				String iconName = "MapSelectionTool.icon";
				Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				splitToggleButton = new JToggleButton();
				splitToggleButton.setIcon(icon);
				splitToggleButton.setPreferredSize(size);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return splitToggleButton;
	}
	
	public JToggleButton getEraseToggleButton() {
		if (eraseToggleButton == null) {
			try {
				Dimension size = app.getUIFactory().getSmallButtonSize();
				String iconName = "MapEraseTool.icon";
				Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				eraseToggleButton = new JToggleButton();
				eraseToggleButton.setIcon(icon);
				eraseToggleButton.setPreferredSize(size);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return eraseToggleButton;
	}
	
	public JToggleButton getPUIToggleButton() {
		if (puiToggleButton == null) {
			try {
				Dimension size = app.getUIFactory().getSmallButtonSize();
				String iconName = "MapDrawPointTool.icon";
				Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				puiToggleButton = new JToggleButton();
				puiToggleButton.setIcon(icon);
				puiToggleButton.setPreferredSize(size);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return puiToggleButton;
	}
	
	public JToggleButton getZoomInToggleButton() {
		if (zoomInToggleButton == null) {
			try {
				Dimension size = app.getUIFactory().getSmallButtonSize();
				String iconName = "MapZoomInTool.icon";
				Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				zoomInToggleButton = new JToggleButton();
				zoomInToggleButton.setIcon(icon);
				zoomInToggleButton.setPreferredSize(size);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return zoomInToggleButton;
	}
	
	public JToggleButton getZoomOutToggleButton() {
		if (zoomOutToggleButton == null) {
			try {
				Dimension size = app.getUIFactory().getSmallButtonSize();
				String iconName = "MapZoomOutTool.icon";
				Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				zoomOutToggleButton = new JToggleButton();
				zoomOutToggleButton.setIcon(icon);
				zoomOutToggleButton.setPreferredSize(size);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return zoomOutToggleButton;
	}
	
	public JToggleButton getPanToggleButton() {
		if (panToggleButton == null)  {
			try {
				Dimension size = app.getUIFactory().getSmallButtonSize();
				String iconName = "MapPanTool.icon";
				Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				panToggleButton = new JToggleButton();
				panToggleButton.setIcon(icon);
				panToggleButton.setPreferredSize(size);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return panToggleButton;
	}
	
	public JButton getZoomInFixedButton() {
		if (zoomInFixedButton == null) {
			try {
				Dimension size = app.getUIFactory().getSmallButtonSize();
				String iconName = "MapZoomInFixedCommand.icon";
				Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				zoomInFixedButton = new JButton();
				zoomInFixedButton.setIcon(icon);
				zoomInFixedButton.setPreferredSize(size);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return zoomInFixedButton;
	}
	
	public JButton getZoomOutFixedButton() {
		if (zoomOutFixedButton == null) {
			try {
				Dimension size = app.getUIFactory().getSmallButtonSize();
				String iconName = "MapZoomOutFixedCommand.icon";
				Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				zoomOutFixedButton = new JButton();
				zoomOutFixedButton.setIcon(icon);
				zoomOutFixedButton.setPreferredSize(size);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return zoomOutFixedButton;
	}
	
	
	public JButton getFullExtentButton() {
		if (fullExtentButton == null) {
			try {
				Dimension size = app.getUIFactory().getSmallButtonSize();
				String iconName = "MapFullExtentCommand.icon";
				Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				fullExtentButton = new JButton();
				fullExtentButton.setIcon(icon);
				fullExtentButton.setPreferredSize(size);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return fullExtentButton;
	}
	
	public JButton getZoomToLastExtentForwardButton() {
		if (zoomToLastExtentForwardButton == null) {
			try {
				Dimension size = app.getUIFactory().getSmallButtonSize();
				String iconName = "MapZoomToLastExtentForwardCommand.icon";
				Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				zoomToLastExtentForwardButton = new JButton();
				zoomToLastExtentForwardButton.setIcon(icon);
				zoomToLastExtentForwardButton.setPreferredSize(size);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return zoomToLastExtentForwardButton;
	}
	
	public JButton getZoomToLastExtentBackwardButton() {
		if (zoomToLastExtentBackwardButton == null) {
			try {
				Dimension size = app.getUIFactory().getSmallButtonSize();
				String iconName = "MapZoomToLastExtentBackCommand.icon";
				Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				zoomToLastExtentBackwardButton = new JButton();
				zoomToLastExtentBackwardButton.setIcon(icon);
				zoomToLastExtentBackwardButton.setPreferredSize(size);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return zoomToLastExtentBackwardButton;
	}
	
	public void taskChanged() {
		getDummyToggleButton().doClick(); // HACK: unselect all toggle buttons
		java.awt.EventQueue.invokeLater(
			new Runnable() {
				// wait for the map is loaded
				public void run() {
					try {
						DiskoMap map = app.getCurrentMap();
						for (int i = 0; i < commands.size(); i++) {
							ICommand command = (ICommand)commands.get(i);
							if (command != null) {
								AbstractButton b = (AbstractButton)buttons.get(i);
								if (map != null && b.isVisible() && b.isEnabled()) {
									command.onCreate(map);
								}
								else {
									if (command instanceof IDiskoTool) {
										((IDiskoTool)command).toolDeactivated();
									}
								}
							}
						}
						if (map != null) {
							map.getActiveView().refresh(); // refreshing the current map
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		);
	}
	
	public AbstractButton getButtonAt(int index) {
		return (AbstractButton)buttons.get(index);
	}
	
	public int addCommand(AbstractButton button, ICommand command) {
		add(button);
		if (button instanceof JToggleButton) {
			bgroup.add(button);
		}
		buttons.add(button);
		commands.add(command);
		if (command != null) {
			button.addActionListener(new NavActionListener(command));
		}
		return buttons.indexOf(button);
	}
	
	public int addButton(AbstractButton button) {
		return addCommand(button, null);
	}
	
	public void enableButtons(int[] indexes) {
		for (int i = 0; i < buttons.size(); i++) {
			getButtonAt(i).setEnabled(false);
		}
		if (indexes != null) {
			for (int i = 0; i < indexes.length; i++) {
				getButtonAt(indexes[i]).setEnabled(true);
			}
		}
	}
	
	public void showButtons(int[] indexes) {
		for (int i = 0; i < buttons.size(); i++) {
			getButtonAt(i).setVisible(false);
		}
		if (indexes != null) {
			for (int i = 0; i < indexes.length; i++) {
				getButtonAt(indexes[i]).setVisible(true);
			}
		}
	}
	
	class NavActionListener implements ActionListener {
		
		private ICommand command = null;
		
		NavActionListener(ICommand command) {
			this.command = command;
		}
		
		public void actionPerformed(java.awt.event.ActionEvent e) {
			try {
				DiskoMap map = app.getCurrentMap();
				if (command instanceof ITool && map != null) {
					map.setCurrentToolByRef((ITool)command);
				}
				else {
					command.onClick();
				}
			} catch (AutomationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
