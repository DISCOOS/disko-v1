package org.redcross.sar.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.EnumSet;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.app.Utils;
import org.redcross.sar.map.DrawTool;
import org.redcross.sar.map.EraseTool;
import org.redcross.sar.map.FlankTool;
import org.redcross.sar.map.IDiskoMap;
import org.redcross.sar.map.IDiskoTool;
import org.redcross.sar.map.POITool;
import org.redcross.sar.map.SelectFeatureTool;
import org.redcross.sar.map.SplitTool;
import org.redcross.sar.map.MapToggleCommand;

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
	
	public enum ToolCommandType {
		POI_TOOL,
		FLANK_TOOL,
		DRAW_TOOL,
		ERASE_TOOL,
		SPLIT_TOOL,
		SELECT_FEATURE_TOOL,
		ZOOM_IN_TOOL,
		ZOOM_OUT_TOOL,
		PAN_TOOL,
		ZOOM_IN_FIXED_COMMAND,
		ZOOM_OUT_FIXED_COMMAND,
		ZOOM_FULL_EXTENT_COMMAND,
		ZOOM_TO_LAST_EXTENT_FORWARD_COMMAND,
		ZOOM_TO_LAST_EXTENT_BACKWARD_COMMAND,
		MAP_TOGGLE_COMMAND
    }
	
	public enum ButtonPlacement {
		LEFT,
		RIGHT
    }
	
	private IDiskoApplication app = null;
	private ButtonGroup bgroup  = null;
	private JToggleButton dummyToggleButton = null;
	private Hashtable<Enum<?>, ICommand> commands  = null;
	private Hashtable<Enum<?>, AbstractButton> buttons  = null;
	private JPanel leftPanel  = null;
	private JPanel rightPanel = null;
	private JToggleButton flankToggleButton = null;
	private JToggleButton drawLineToggleButton = null;
	private JToggleButton eraseToggleButton = null;
	private JToggleButton splitToggleButton = null;
	private JToggleButton poiToggleButton = null;	
	private JToggleButton zoomInToggleButton = null;
	private JToggleButton zoomOutToggleButton = null;
	private JToggleButton panToggleButton = null;
	private JToggleButton selectFeatureToggleButton = null;
	private JButton zoomInFixedButton = null;
	private JButton zoomOutFixedButton = null;
	private JButton fullExtentButton = null;
	private JButton zoomToLastExtentForwardButton = null;
	private JButton zoomToLastExtentBackwardButton = null;	
	private JButton mapToggleButton = null;
	
	private DrawTool drawTool = null;
	private FlankTool flankTool = null;
	private EraseTool eraseTool = null;
	private SplitTool splitTool = null;
	private POITool puiTool = null;
	private SelectFeatureTool selectFeatureTool = null;
	private ControlsMapZoomInTool zoomInTool = null;
	private ControlsMapZoomOutTool zoomOutTool = null;
	private ControlsMapPanTool panTool = null;
	private ControlsMapZoomInFixedCommand zoomInFixedCommand = null;
	private ControlsMapZoomOutFixedCommand zoomOutFixedCommand = null;
	private ControlsMapFullExtentCommand fullExtentCommand = null;
	private ControlsMapZoomToLastExtentForwardCommand zoomToLastExtentForwardCommand = null;
	private ControlsMapZoomToLastExtentBackCommand zoomToLastExtentBackCommand = null;
	private MapToggleCommand mapToggle = null;
	
	public NavBar() {
		this(null);
	}
	
	public NavBar(IDiskoApplication app) {
		this.app = app;
		initialize();
	}
	
	private void initialize() {
		commands = new Hashtable<Enum<?>, ICommand>();
		buttons = new Hashtable<Enum<?>, AbstractButton>();
		bgroup = new ButtonGroup();
			
		setLayout(new BorderLayout());
		add(getLeftPanel(), BorderLayout.WEST);
		add(getRightPanel(), BorderLayout.EAST);
			
		// Add a not visible dummy JToggleButton, used to unselect all
		// (visbible) JToggleButtons. This is a hack suggested by Java dev forum
		bgroup.add(getDummyToggleButton());
			
		addCommand(getPOIToggleButton(), getPOITool(), 
				ToolCommandType.POI_TOOL, ButtonPlacement.LEFT);
		addCommand(getFlankToggleButton(), getFlankTool(), 
				ToolCommandType.FLANK_TOOL, ButtonPlacement.LEFT);
		addCommand(getDrawLineToggleButton(), getDrawTool(), 
				ToolCommandType.DRAW_TOOL, ButtonPlacement.LEFT);
		addCommand(getEraseToggleButton(), getEraseTool(), 
				ToolCommandType.ERASE_TOOL, ButtonPlacement.LEFT);
		addCommand(getSplitToggleButton(), getSplitTool(), 
				ToolCommandType.SPLIT_TOOL, ButtonPlacement.LEFT);
		addCommand(getSelectFeatureToggleButton(), getSelectFeatureTool(), 
				ToolCommandType.SELECT_FEATURE_TOOL, ButtonPlacement.LEFT);
		addCommand(getZoomInToggleButton(), getZoomInTool(), 
				ToolCommandType.ZOOM_IN_TOOL, ButtonPlacement.RIGHT);
		addCommand(getZoomOutToggleButton(), getZoomOutTool(), 
				ToolCommandType.ZOOM_OUT_TOOL, ButtonPlacement.RIGHT);
		addCommand(getPanToggleButton(), getPanTool(), 
				ToolCommandType.PAN_TOOL, ButtonPlacement.RIGHT);
		addCommand(getZoomInFixedButton(), getZoomInFixedCommand(),
				ToolCommandType.ZOOM_IN_FIXED_COMMAND, ButtonPlacement.RIGHT);
		addCommand(getZoomOutFixedButton(), getZoomOutFixedCommand(),
				ToolCommandType.ZOOM_OUT_FIXED_COMMAND, ButtonPlacement.RIGHT);
		addCommand(getFullExtentButton(), getFullExtentCommand(), 
				ToolCommandType.ZOOM_FULL_EXTENT_COMMAND, ButtonPlacement.RIGHT);
		addCommand(getZoomToLastExtentForwardButton(), getZoomToLastExtentForwardCommand(), 
				ToolCommandType.ZOOM_TO_LAST_EXTENT_FORWARD_COMMAND, ButtonPlacement.RIGHT);
		addCommand(getZoomToLastExtentBackwardButton(), getZoomToLastExtentBackCommand(), 
				ToolCommandType.ZOOM_TO_LAST_EXTENT_BACKWARD_COMMAND, ButtonPlacement.RIGHT);	
		addCommand(getMapToggleButton(), getMapToggleCommand(), 
				ToolCommandType.MAP_TOGGLE_COMMAND, ButtonPlacement.RIGHT);
	}
	
	private JPanel getLeftPanel() {
		if (leftPanel == null) {
			try {
				leftPanel = new JPanel();
				FlowLayout fl = new FlowLayout();
				fl.setHgap(0);
				fl.setVgap(0);
				fl.setAlignment(FlowLayout.LEFT);
				leftPanel.setLayout(fl);
				
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return leftPanel;
	}
	
	private JPanel getRightPanel() {
		if (rightPanel == null) {
			try {
				rightPanel = new JPanel();
				FlowLayout fl = new FlowLayout();
				fl.setHgap(0);
				fl.setVgap(0);
				fl.setAlignment(FlowLayout.RIGHT);
				rightPanel.setLayout(fl);
				
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return rightPanel;
	}
	
	
	public DrawTool getDrawTool() {
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
	
	public FlankTool getFlankTool() {
		if (flankTool == null) {
			try {
				flankTool = new FlankTool(app);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return flankTool;
	}
	
	public EraseTool getEraseTool() {
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
	
	public SplitTool getSplitTool() {
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
	
	public POITool getPOITool() {
		if (puiTool == null) {
			try {
				puiTool = new POITool(app);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return puiTool;
	}
	
	public SelectFeatureTool getSelectFeatureTool() {
		if (selectFeatureTool == null) {
			try {
				selectFeatureTool = new SelectFeatureTool();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return selectFeatureTool;
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
	
	
	private MapToggleCommand getMapToggleCommand() {		
		if (mapToggle == null) {
			try {
				mapToggle = new MapToggleCommand();			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mapToggle;
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
				flankToggleButton = new JToggleButton();
				flankToggleButton.setPreferredSize(size);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return flankToggleButton;
	}
	
	public JToggleButton getSelectFeatureToggleButton() {
		if (selectFeatureToggleButton == null)  {
			try {
				Dimension size = app.getUIFactory().getSmallButtonSize();
				selectFeatureToggleButton = new JToggleButton();
				selectFeatureToggleButton.setPreferredSize(size);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return selectFeatureToggleButton;
	}
	
	public JToggleButton getDrawLineToggleButton() {
		if (drawLineToggleButton == null) {
			try {
				Dimension size = app.getUIFactory().getSmallButtonSize();
				drawLineToggleButton = new JToggleButton();
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
				splitToggleButton = new JToggleButton();
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
				eraseToggleButton = new JToggleButton();
				eraseToggleButton.setPreferredSize(size);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return eraseToggleButton;
	}
	
	public JToggleButton getPOIToggleButton() {
		if (poiToggleButton == null) {
			try {
				Dimension size = app.getUIFactory().getSmallButtonSize();
				poiToggleButton = new JToggleButton();
				poiToggleButton.setPreferredSize(size);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return poiToggleButton;
	}
	
	public JToggleButton getZoomInToggleButton() {
		if (zoomInToggleButton == null) {
			try {
				Dimension size = app.getUIFactory().getSmallButtonSize();
				zoomInToggleButton = new JToggleButton();
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
				zoomOutToggleButton = new JToggleButton();
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
				panToggleButton = new JToggleButton();
				panToggleButton.setPreferredSize(size);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return panToggleButton;
	}
	
	public JButton getMapToggleButton(){
		if (mapToggleButton == null) {
			try {
				Dimension size = app.getUIFactory().getSmallButtonSize();
				mapToggleButton = new JButton();
				mapToggleButton.setPreferredSize(size);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		return mapToggleButton;
	}
	
	public JButton getZoomInFixedButton() {
		if (zoomInFixedButton == null) {
			try {
				Dimension size = app.getUIFactory().getSmallButtonSize();
				zoomInFixedButton = new JButton();
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
				zoomOutFixedButton = new JButton();
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
				fullExtentButton = new JButton();
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
				zoomToLastExtentForwardButton = new JButton();
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
				zoomToLastExtentBackwardButton = new JButton();
				zoomToLastExtentBackwardButton.setPreferredSize(size);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return zoomToLastExtentBackwardButton;
	}
		
	public void taskChanged() {
		//unselectAll();
		java.awt.EventQueue.invokeLater(
			new Runnable() {
				// wait for the map is loaded
				public void run() {
					try {
						IDiskoMap map = app.getCurrentMap();
						Iterator commandIter = commands.values().iterator();
						Iterator buttonIter  = buttons.values().iterator();
						while (commandIter.hasNext() && buttonIter.hasNext()) {
							ICommand command = (ICommand)commandIter.next();
							if (command != null) {
								//AbstractButton b = (AbstractButton)buttonIter.next();
								if (map != null) {
									command.onCreate(map);
								}
								else {
									if (command instanceof IDiskoTool) {
										((IDiskoTool)command).toolDeactivated();
									}
								}
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		);
	}
	
	public void unselectAll() {
		getDummyToggleButton().doClick(); // HACK: unselect all toggle buttons
	}
	
	public AbstractButton getButton(Enum key) {
		return (AbstractButton)buttons.get(key);
	}
	
	public void addCommand(AbstractButton button, ICommand command, Enum key, Enum buttonPlacement) {
		if (buttonPlacement == ButtonPlacement.LEFT) {
			getLeftPanel().add(button);
		} else {
			getRightPanel().add(button);
		}
		if (button instanceof JToggleButton) {
			bgroup.add(button);
		}
		buttons.put(key, button);
		commands.put(key, command);
		if (command != null) {
			button.addActionListener(new NavActionListener(command));
		}
		ImageIcon icon = Utils.getIcon(key);
		if (icon != null) {
			button.setIcon(icon);
		}
		else {
			button.setText(key.name());
		}
	}
	
	
	public void enableButtons(EnumSet<NavBar.ToolCommandType> myInterests) {
		Iterator buttonIter = buttons.values().iterator();
		while (buttonIter.hasNext()) {
			((AbstractButton)buttonIter.next()).setEnabled(false);
		}
		Iterator enmuIter = myInterests.iterator();
		while (enmuIter.hasNext()) {
			Enum key = (Enum)enmuIter.next();
			AbstractButton button = (AbstractButton)buttons.get(key);
			if (button != null) {
				button.setEnabled(true);
			}
		}
	}
	
	public void showButtons(EnumSet<NavBar.ToolCommandType> myInterests) {
		Iterator buttonIter = buttons.values().iterator();
		while (buttonIter.hasNext()) {
			((AbstractButton)buttonIter.next()).setVisible(false);
		}
		Iterator enmuIter = myInterests.iterator();
		while (enmuIter.hasNext()) {
			Enum key = (Enum)enmuIter.next();
			AbstractButton button = (AbstractButton)buttons.get(key);
			if (button != null) {
				button.setVisible(true);
			}
		}
	}
	
	public void switchIcon(String command, boolean first){
		if(command.equalsIgnoreCase("maptoggle")){
			System.out.println("spennende...");
			AbstractButton ab = this.getButton(ToolCommandType.MAP_TOGGLE_COMMAND);
			ImageIcon icon;
			if(first)
				icon = new ImageIcon(app.getProperty("ToolCommandType.MAP_TOGGLE_COMMAND.icon"));
			else 
				icon = new ImageIcon(app.getProperty("ToolCommandType.MAP_TOGGLE_COMMAND_2.icon"));
			ab.setIcon(icon);			
		}
	}
	
	class NavActionListener implements ActionListener {
		
		private ICommand command = null;
		
		NavActionListener(ICommand command) {
			this.command = command;
		}
		
		public void actionPerformed(java.awt.event.ActionEvent e) {
			try {
				AbstractButton button = (AbstractButton)e.getSource();
				IDiskoMap map = app.getCurrentMap();
				if (command instanceof IDiskoTool) {
					DiskoDialog dialog = ((IDiskoTool)command).getDialog();
					if (button.isSelected() && dialog != null && dialog.isVisible()) {
						dialog.setVisible(false);
						return;
					}
				}
				if (command instanceof ITool && map != null) {
					map.setCurrentToolByRef((ITool)command);
				}
				else if (command instanceof MapToggleCommand){					
					mapToggle = (MapToggleCommand) command;
					mapToggle.onClick(app);
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
