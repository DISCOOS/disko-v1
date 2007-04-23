package org.redcross.sar.wp;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JToggleButton;
import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.app.Utils;
import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.gui.NavBar;
import org.redcross.sar.gui.UIFactory;
import org.redcross.sar.map.DiskoMap;

import com.esri.arcgis.carto.FeatureLayer;

/**
 * Implements the DiskoApTaktikk interface
 * @author geira
 *
 */
public class DiskoWpTackticsImpl extends AbstractDiskoWpModule implements IDiskoWpTacktics {

	private IDiskoApplication app = null;
	private FeatureLayer flankeFL = null;
	private ElementDialog elementDialog = null;
	private JToggleButton elementToggleButton = null;
	private JToggleButton listToggleButton = null;
	private JToggleButton missionToggleButton = null;
	private JToggleButton hypotheseToggleButton = null;
	private JToggleButton priorityToggleButton = null;
	private JToggleButton requirementToggleButton = null;
	private JToggleButton descriptionToggleButton = null;
	private JToggleButton unitToggleButton = null;
	
	
	/**
	 * Constructs a DiskoApTaktikkImpl
	 * @param rolle A reference to the DiskoRolle
	 */
	public DiskoWpTackticsImpl(IDiskoRole rolle) {
		super(rolle);
		app = getDiskoRole().getApplication();
		initialize();
	}
	
	private void initialize() {
		loadProperties("properties");
		DiskoMap map = getMap();
		layoutComponent(map);
		layoutButton(getElementToggleButton(), false);
		layoutButton(getListToggleButton(), true);
		layoutButton(getMissionToggleButton(), true);
		layoutButton(getHypotheseToggleButton(), true);
		layoutButton(getPriorityToggleButton(), true);
		layoutButton(getRequirementToggleButton(), true);
		layoutButton(getDescriptionToggleButton(), true);
		layoutButton(getUnitToggleButton(), true);
	}
	
	public void onMapReplaced(DiskoMapEvent e) throws IOException {
		DiskoMap map = getMap();
		map.setName(getName()+"Map");	
		flankeFL = map.getFeatureLayer(getProperty("BufferPath.featureClass.Name"));
		flankeFL.setSelectable(true);
	}
	
	public void activated() {
		UIFactory uiFactory = getDiskoRole().getApplication().getUIFactory();
		NavBar navBar = uiFactory.getMainPanel().getNavBar();
		int[] buttonIndexes = {
				NavBar.FLANK_TOOL,
				NavBar.DRAW_TOOL,
				NavBar.ERASE_TOOL,
				NavBar.SPLIT_TOOL,
				NavBar.PUI_TOOL,
				NavBar.ZOOM_IN_TOOL,
				NavBar.ZOOM_OUT_TOOL,
				NavBar.PAN_TOOL,
				NavBar.SELECT_FEATURES_TOOL,
				NavBar.ZOOM_FULL_EXTENT_COMMAND,
				NavBar.ZOOM_TO_LAST_EXTENT_FORWARD_COMMAND,
				NavBar.ZOOM_TO_LAST_EXTENT_BACKWARD_COMMAND
		};
		navBar.showButtons(buttonIndexes);
	}
	
	public void deactivated() {
		hideDialogs();
    }
	
	/* (non-Javadoc)
	 * @see com.geodata.engine.disko.task.DiskoAp#getName()
	 */
	public String getName() {
		return "Taktikk";
	}

	/* (non-Javadoc)
	 * @see com.geodata.engine.disko.task.DiskoAp#cancel()
	 */
	public void cancel() {
		hideDialogs();
	}
	
	/* (non-Javadoc)
	 * @see com.geodata.engine.disko.task.DiskoAp#finish()
	 */
	public void finish() {
		hideDialogs();
	}
	
	private void hideDialogs() {
		getElementDialog().setVisible(false);
		getElementToggleButton().setSelected(false);
	}
	
	private ElementDialog getElementDialog() {
		if (elementDialog == null) {
			elementDialog = new ElementDialog(app.getFrame(), new ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getElementDialog().setVisible(false);
					getElementToggleButton().setSelected(false);
					String command = e.getActionCommand();
					if (command.equals("ELEMENT_OPERATION_AREA")) {
						showOperationAreaButtons();
					}
					else if (command.equals("ELEMENT_SEARCH_AREA")) {
						showSearchAreaButtons();
					}
					else if (command.equals("ELEMENT_PATROL_SEARCH")) {
						showSearchButtons();
					}
					else if (command.equals("ELEMENT_LINE_SEARCH")) {
						showSearchButtons();
					}
					else if (command.equals("ELEMENT_RODE_SEARCH")) {
						showSearchButtons();
					}
					else if (command.equals("ELEMENT_BEACH_SEARCH")) {
						showSearchButtons();
					}
					else if (command.equals("ELEMENT_MARITIME_SEARCH")) {
						showSearchButtons();
					}
					else if (command.equals("ELEMENT_DOG_SEARCH")) {
						showSearchButtons();
					}
					else if (command.equals("ELEMENT_AIR_SEARCH")) {
						showSearchButtons();
					}
					else if (command.equals("ELEMENT_GENERAL_TASK")) {
						showSearchButtons();
					}
				}
			});
			elementDialog.setIsToggable(false);
		}
		return elementDialog;
	}
	
	private void showOperationAreaButtons() {
		getMissionToggleButton().setVisible(true);
		getHypotheseToggleButton().setVisible(false);
		getPriorityToggleButton().setVisible(false);
		getRequirementToggleButton().setVisible(false);
		getDescriptionToggleButton().setVisible(false);
		getUnitToggleButton().setVisible(false);
	}
	
	private void showSearchAreaButtons() {
		getMissionToggleButton().setVisible(false);
		getHypotheseToggleButton().setVisible(true);
		getPriorityToggleButton().setVisible(true);
		getRequirementToggleButton().setVisible(false);
		getDescriptionToggleButton().setVisible(false);
		getUnitToggleButton().setVisible(false);
	}
	
	private void showSearchButtons() {
		getMissionToggleButton().setVisible(false);
		getHypotheseToggleButton().setVisible(false);
		getPriorityToggleButton().setVisible(false);
		getRequirementToggleButton().setVisible(true);
		getDescriptionToggleButton().setVisible(true);
		getUnitToggleButton().setVisible(true);
	}

	private JToggleButton getElementToggleButton() {
		if (elementToggleButton == null) {
			try {
				Dimension size = app.getUIFactory().getLargeButtonSize();
				//String iconName = "MapZoomInTool.icon";
				//Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				elementToggleButton = new JToggleButton();
				elementToggleButton.setText("ELEMENT");
				//elementToggleButton.setIcon(icon);
				elementToggleButton.setPreferredSize(size);
				elementToggleButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						ElementDialog dialog = getElementDialog();
						if (elementToggleButton.isSelected()) {
							Point p = elementToggleButton.getLocationOnScreen();
							p.setLocation(p.x - dialog.getWidth()-2, p.y);
							dialog.setLocation(p);
							dialog.setVisible(true);
						}
						else {
							dialog.setVisible(false);
						}
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return elementToggleButton;
	}

	private JToggleButton getDescriptionToggleButton() {
		if (descriptionToggleButton == null) {
			try {
				Dimension size = app.getUIFactory().getLargeButtonSize();
				//String iconName = "MapZoomInTool.icon";
				//Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				descriptionToggleButton = new JToggleButton();
				descriptionToggleButton.setText("BESKRIVELSE");
				//descriptionToggleButton.setIcon(icon);
				descriptionToggleButton.setPreferredSize(size);
				descriptionToggleButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						hideDialogs();
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return descriptionToggleButton;
	}

	private JToggleButton getHypotheseToggleButton() {
		if (hypotheseToggleButton == null) {
			try {
				Dimension size = app.getUIFactory().getLargeButtonSize();
				String iconName = "hypothese.icon";
				Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				hypotheseToggleButton = new JToggleButton();
				hypotheseToggleButton.setIcon(icon);
				hypotheseToggleButton.setPreferredSize(size);
				hypotheseToggleButton.setVisible(false);
				hypotheseToggleButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						hideDialogs();
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return hypotheseToggleButton;
	}

	private JToggleButton getListToggleButton() {
		if (listToggleButton == null) {
			try {
				Dimension size = app.getUIFactory().getLargeButtonSize();
				String iconName = "list.icon";
				Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				listToggleButton = new JToggleButton();
				listToggleButton.setIcon(icon);
				listToggleButton.setPreferredSize(size);
				listToggleButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						hideDialogs();
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return listToggleButton;
	}

	private JToggleButton getMissionToggleButton() {
		if (missionToggleButton == null) {
			try {
				Dimension size = app.getUIFactory().getLargeButtonSize();
				String iconName = "mission.icon";
				Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				missionToggleButton = new JToggleButton();
				missionToggleButton.setIcon(icon);
				missionToggleButton.setPreferredSize(size);
				missionToggleButton.setVisible(false);
				missionToggleButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						hideDialogs();
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return missionToggleButton;
	}

	private JToggleButton getPriorityToggleButton() {
		if (priorityToggleButton == null) {
			try {
				Dimension size = app.getUIFactory().getLargeButtonSize();
				//String iconName = "MapZoomInTool.icon";
				//Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				priorityToggleButton = new JToggleButton();
				priorityToggleButton.setText("PRIORITET");
				//priorityToggleButton.setIcon(icon);
				priorityToggleButton.setPreferredSize(size);
				priorityToggleButton.setVisible(false);
				priorityToggleButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						hideDialogs();
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return priorityToggleButton;
	}

	private JToggleButton getRequirementToggleButton() {
		if (requirementToggleButton == null) {
			try {
				Dimension size = app.getUIFactory().getLargeButtonSize();
				//String iconName = "MapZoomInTool.icon";
				//Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				requirementToggleButton = new JToggleButton();
				requirementToggleButton.setText("KRAV");
				//requirementToggleButton.setIcon(icon);
				requirementToggleButton.setPreferredSize(size);
				requirementToggleButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						hideDialogs();
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return requirementToggleButton;
	}

	private JToggleButton getUnitToggleButton() {
		if (unitToggleButton == null) {
			try {
				Dimension size = app.getUIFactory().getLargeButtonSize();
				//String iconName = "MapZoomInTool.icon";
				//Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				unitToggleButton = new JToggleButton();
				unitToggleButton.setText("ENHET");
				//unitToggleButton.setIcon(icon);
				unitToggleButton.setPreferredSize(size);
				unitToggleButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						hideDialogs();
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return unitToggleButton;
	}
}
