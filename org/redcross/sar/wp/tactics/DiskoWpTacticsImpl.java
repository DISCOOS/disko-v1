package org.redcross.sar.wp.tactics;

import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.app.Utils;
import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.NavBar;
import org.redcross.sar.gui.POIDialog;
import org.redcross.sar.gui.SubMenuPanel;
import org.redcross.sar.gui.TextAreaDialog;
import org.redcross.sar.map.DiskoMap;
import org.redcross.sar.map.DrawTool;
import org.redcross.sar.map.EraseTool;
import org.redcross.sar.map.FlankTool;
import org.redcross.sar.map.IDiskoMapManager;
import org.redcross.sar.map.IEditFeedback;
import org.redcross.sar.map.POITool;
import org.redcross.sar.map.SplitTool;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.IMsoFeatureClass;
import org.redcross.sar.map.layer.AreaLayer;
import org.redcross.sar.map.layer.OperationAreaLayer;
import org.redcross.sar.map.layer.POILayer;
import org.redcross.sar.map.layer.SearchAreaLayer;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IAssignmentListIf;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IOperationAreaIf;
import org.redcross.sar.mso.data.ISearchAreaIf;
import org.redcross.sar.mso.data.ISearchIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IPOIIf.POIType;
import org.redcross.sar.mso.data.ISearchIf.SearchSubType;
import org.redcross.sar.util.except.IllegalOperationException;
import org.redcross.sar.wp.AbstractDiskoWpModule;
import org.redcross.sar.wp.TestData.BuildTestData;

import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.interop.AutomationException;

/**
 * Implements the DiskoApTaktikk interface
 * 
 * @author geira
 * 
 */
public class DiskoWpTacticsImpl extends AbstractDiskoWpModule implements
IDiskoWpTactics, IEditFeedback {

	private ElementDialog elementDialog = null;

	private JToggleButton dummyToggleButton = null;

	private JToggleButton elementToggleButton = null;

	private JToggleButton listToggleButton = null;

	private JToggleButton missionToggleButton = null;

	private JToggleButton hypotheseToggleButton = null;

	private JToggleButton priorityToggleButton = null;

	private JToggleButton requirementToggleButton = null;

	private JToggleButton descriptionToggleButton = null;

	private JToggleButton unitToggleButton = null;

	private Enum currentAction = null;

	private POITool poiTool = null;

	private DrawTool drawTool = null;

	private EraseTool eraseTool = null;

	private FlankTool flankTool = null;
	
	private SplitTool splitTool = null;

	private POIDialog poiDialog = null;

	private ArrayList<DiskoDialog> dialogs = null;

	private TextAreaDialog textAreaDialog = null;

	private HypothesesDialog hypothesesDialog = null;

	private PriorityDialog priorityDialog = null;

	private UnitSelectionDialog unitSelectionDialog = null;

	private SearchRequirementDialog searchRequirementDialog = null;

	private ListDialog listDialog = null;

	private ListSelectionListener elementListSelectionListener = null;

	private SubMenuPanel subMenu = null;

	private OperationAreaLayer opAreaLayer = null;

	private SearchAreaLayer searchAreaLayer = null;

	private POILayer poiLayer = null;

	private AreaLayer areaLayer = null;

	private boolean workInProgress = false;

	private IAssignmentIf currentAssignment = null;

	private IMsoObjectIf currentMsoObject = null;

	/**
	 * Constructs a DiskoApTaktikkImpl
	 * 
	 * @param rolle
	 *            A reference to the DiskoRolle
	 */
	public DiskoWpTacticsImpl(IDiskoRole rolle) {
		super(rolle);
		dialogs = new ArrayList<DiskoDialog>();
		elementListSelectionListener = new ElementListSelectionListener();
		subMenu = getApplication().getUIFactory().getSubMenuPanel();
		
		 BuildTestData.createCmdPost(getMsoModel());
	     BuildTestData.createUnitsAndAssignments(getMsoModel());
	        
		initialize();
	}

	private void initialize() {
		loadProperties("properties");
		DiskoMap map = (DiskoMap) getMap();
		layoutComponent(map);
		layoutButton(getElementToggleButton(), true);
		layoutButton(getListToggleButton(), true);
		layoutButton(getMissionToggleButton(), true);
		layoutButton(getPriorityToggleButton(), true);
		layoutButton(getHypotheseToggleButton(), true);
		layoutButton(getRequirementToggleButton(), true);
		layoutButton(getDescriptionToggleButton(), true);
		layoutButton(getUnitToggleButton(), true);

		// Add a not visible dummy JToggleButton, used to unselect all
		// (visbible) JToggleButtons. This is a hack suggested by Java dev forum
		layoutButton(getDummyToggleButton(), true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.geodata.engine.disko.task.DiskoAp#getName()
	 */
	public String getName() {
		return "Taktikk";
	}

	public void onMapReplaced(DiskoMapEvent e) throws IOException {
		DiskoMap map = (DiskoMap) getMap();
		map.setName(getName() + "Map");
	}

	public void activated() {
		super.activated();
		NavBar navBar = getApplication().getNavBar();
		EnumSet<NavBar.ToolCommandType> myInterests = EnumSet.of(NavBar.ToolCommandType.FLANK_TOOL);
		myInterests.add(NavBar.ToolCommandType.DRAW_TOOL);
		myInterests.add(NavBar.ToolCommandType.ERASE_TOOL);
		myInterests.add(NavBar.ToolCommandType.SPLIT_TOOL);
		myInterests.add(NavBar.ToolCommandType.POI_TOOL);
		myInterests.add(NavBar.ToolCommandType.ZOOM_IN_TOOL);
		myInterests.add(NavBar.ToolCommandType.ZOOM_OUT_TOOL);
		myInterests.add(NavBar.ToolCommandType.PAN_TOOL);
		myInterests.add(NavBar.ToolCommandType.SELECT_FEATURES_TOOL);
		myInterests.add(NavBar.ToolCommandType.ZOOM_TO_LAST_EXTENT_FORWARD_COMMAND);
		myInterests.add(NavBar.ToolCommandType.ZOOM_TO_LAST_EXTENT_BACKWARD_COMMAND);
		navBar.showButtons(myInterests);

		IDiskoMapManager mapManager = getMap().getMapManager();
		opAreaLayer = (OperationAreaLayer) mapManager.getMsoLayer(IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA);
		searchAreaLayer = (SearchAreaLayer) mapManager.getMsoLayer(IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA);
		poiLayer = (POILayer) mapManager.getMsoLayer(IMsoManagerIf.MsoClassCode.CLASSCODE_POI);
		areaLayer = (AreaLayer) mapManager.getMsoLayer(IMsoManagerIf.MsoClassCode.CLASSCODE_AREA);

		drawTool = navBar.getDrawTool();
		drawTool.setEditFeedback(this);
		poiTool = navBar.getPOITool();
		eraseTool = navBar.getEraseTool();
		flankTool = navBar.getFlankTool();
		splitTool = navBar.getSplitTool();
		enableTools(false);
		poiDialog = (POIDialog) poiTool.getDialog();

		if (!workInProgress) {
			enableButtons(false);
		}
	}

	public void deactivated() {
		super.deactivated();
		hideDialogs(null);
		getDummyToggleButton().doClick();
	}

	public void editStarted(IFeature editFeature) {
	}

	public void editFinished(IFeature editFeature) {
		enableButtons(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.geodata.engine.disko.task.DiskoAp#cancel()
	 */
	public void cancel() {
		super.fireTaskCanceled();
		hideDialogs(null);
		getDummyToggleButton().doClick();
		doRollback();
		enableButtons(false);
		enableTools(false);
		getElementDialog().getElementList().clearSelection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.geodata.engine.disko.task.DiskoAp#finish()
	 */
	public void finish() {
		try {
			hideDialogs(null);
			getTextAreaDialog().setText(null);

			if (currentAction == IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA) {
				IOperationAreaIf opArea = (IOperationAreaIf) currentMsoObject;
				opArea.setRemarks(getTextAreaDialog().getText());
			} else if (currentAction == IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA) {
				ISearchAreaIf searchArea = (ISearchAreaIf) currentMsoObject;
				searchArea.setSearchAreaHypothesis(getHypothesesDialog()
						.getSelectedHypotheses());
				searchArea.setPriority(getPriorityDialog().getPriority());
			} else { // search
				ICmdPostIf cmdPost = getMsoManager().getCmdPost();
				if (currentAssignment == null) { // must create new
					IAssignmentListIf assignmentList = cmdPost
					.getAssignmentList();
					currentAssignment = assignmentList.createSearch();
				}
				IAreaIf area = (IAreaIf) currentMsoObject;
				currentAssignment.setPlannedArea(area);
				SearchRequirementDialog dialog = getSearchRequirementDialog();
				currentAssignment.setPriority(dialog.getPriority());
				// moved VW currentAssignment.setStatus(dialog.getStatus());
				currentAssignment.setRemarks(getTextAreaDialog().getText());

				if (currentAssignment instanceof ISearchIf) {
					ISearchIf search = (ISearchIf) currentAssignment;
					search.setSubType((SearchSubType) currentAction);
					search.setPlannedAccuracy(dialog.getAccuracy());
					search.setPlannedPersonnel(dialog.getPersonelNeed());
					search.setPlannedProgress(dialog.getEstimatedProgress());
					IUnitIf unit = getUnitSelectionDialog().getSelectedUnit();
					if (unit != null) {
						unit.addUnitAssignment(search, dialog.getStatus()); // modified
						// VW
					} else {
						currentAssignment.setStatus(dialog.getStatus()); // todo
						// Test????
					}
				}
				currentAssignment.setPriority(dialog.getPriority());
				currentAssignment.setRemarks(getTextAreaDialog().getText());
			}
		} catch (IllegalOperationException e) {
			doRollback();
			e.printStackTrace();
		} finally {
			// commit changes and clear graphics
			currentAssignment = null;
			workInProgress = false;
			enableButtons(false);
			doCommit();
		}
	}

	private void doCommit() {
		try {
			getMsoModel().commit();
			getMap().partialRefresh(null);
			enableTools(false);
			getElementDialog().getElementList().clearSelection();
		} catch (AutomationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doRollback() {
		try {
			getMsoModel().rollback();
			getMap().partialRefresh(null);
		} catch (AutomationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startEdit(IAssignmentIf assignment, boolean makeCopy) {
		if (makeCopy) {
			// clone() is not implemented
			// currentAssignment = assignment.clone();
			currentAssignment = assignment;
		} else {
			currentAssignment = assignment;
		}
		SearchRequirementDialog reqDialog = getSearchRequirementDialog();
		UnitSelectionDialog unitDialog = getUnitSelectionDialog();
		JList elementList = getElementDialog().getElementList();
		elementList.setSelectedValue(assignment.getType(), false);

		unitDialog.selectedAssignedUnit(assignment);
		getTextAreaDialog().setText(assignment.getRemarks());
		reqDialog.setPriority(assignment.getPriority());
		reqDialog.setStatus(assignment.getStatus());
		reqDialog.setCriticalQuestions(assignment.getRemarks());

		if (assignment instanceof ISearchIf) {
			ISearchIf search = (ISearchIf) assignment;
			reqDialog.setAccuracy(search.getPlannedAccuracy());
			reqDialog.setPersonelNeed(search.getPlannedPersonnel());
			reqDialog.setEstimatedProgress(search.getPlannedProgress());
		}
		IAreaIf area = assignment.getPlannedArea();
		if (area != null) {
		}
	}

	private void hideDialogs(JDialog notToHideDialog) {
		for (int i = 0; i < dialogs.size(); i++) {
			JDialog dialog = (JDialog) dialogs.get(i);
			if (dialog != notToHideDialog) {
				dialog.setVisible(false);
			}
		}
	}

	private ElementDialog getElementDialog() {
		if (elementDialog == null) {
			elementDialog = new ElementDialog(getApplication().getFrame(), null);
			elementDialog.setIsToggable(false);
			
			JList elementList = getElementDialog().getElementList();
			ISearchIf.SearchSubType[] values = ISearchIf.SearchSubType.values();
			Object[] listData = new Object[values.length + 2];
			listData[0] = IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA;
			listData[1] = IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA;
			for (int i = 0; i < values.length; i++) {
				listData[i + 2] = values[i];
			}
			elementList.setListData(listData);
			elementList.addListSelectionListener(elementListSelectionListener);
			dialogs.add(elementDialog);
		}
		return elementDialog;
	}

	private TextAreaDialog getTextAreaDialog() {
		if (textAreaDialog == null) {
			textAreaDialog = new TextAreaDialog(getApplication().getFrame());
			textAreaDialog.setIsToggable(false);
			dialogs.add(textAreaDialog);
		}
		return textAreaDialog;
	}

	private HypothesesDialog getHypothesesDialog() {
		if (hypothesesDialog == null) {
			hypothesesDialog = new HypothesesDialog(getApplication());
			hypothesesDialog.setIsToggable(false);
			dialogs.add(hypothesesDialog);
		}
		return hypothesesDialog;
	}

	private PriorityDialog getPriorityDialog() {
		if (priorityDialog == null) {
			priorityDialog = new PriorityDialog(getApplication().getFrame());
			priorityDialog.setIsToggable(false);
			dialogs.add(priorityDialog);
		}
		return priorityDialog;
	}

	private SearchRequirementDialog getSearchRequirementDialog() {
		if (searchRequirementDialog == null) {
			searchRequirementDialog = new SearchRequirementDialog(this);
			searchRequirementDialog.setIsToggable(false);
			dialogs.add(searchRequirementDialog);
		}
		return searchRequirementDialog;
	}

	private UnitSelectionDialog getUnitSelectionDialog() {
		if (unitSelectionDialog == null) {
			unitSelectionDialog = new UnitSelectionDialog(getApplication());
			unitSelectionDialog.setIsToggable(false);
			dialogs.add(unitSelectionDialog);
		}
		return unitSelectionDialog;
	}

	private ListDialog getListDialog() {
		if (listDialog == null) {
			listDialog = new ListDialog(this);
			listDialog.setIsToggable(false);
			dialogs.add(listDialog);
		}
		return listDialog;
	}

	private void showOperationAreaButtons() {
		showElementListButton();
		getMissionToggleButton().setVisible(true);
		getHypotheseToggleButton().setVisible(false);
		getPriorityToggleButton().setVisible(false);
		getRequirementToggleButton().setVisible(false);
		getDescriptionToggleButton().setVisible(false);
		getUnitToggleButton().setVisible(false);
	}

	private void showSearchAreaButtons() {
		showElementListButton();
		getMissionToggleButton().setVisible(false);
		getHypotheseToggleButton().setVisible(true);
		getPriorityToggleButton().setVisible(true);
		getRequirementToggleButton().setVisible(false);
		getDescriptionToggleButton().setVisible(false);
		getUnitToggleButton().setVisible(false);
	}

	private void showSearchButtons() {
		showElementListButton();
		getMissionToggleButton().setVisible(false);
		getHypotheseToggleButton().setVisible(false);
		getPriorityToggleButton().setVisible(false);
		getRequirementToggleButton().setVisible(true);
		getDescriptionToggleButton().setVisible(true);
		getUnitToggleButton().setVisible(true);
	}

	private void showElementListButton() {
		ICmdPostIf cmdPost = getMsoManager().getCmdPost();
		getListToggleButton().setVisible(
				cmdPost.getOperationAreaListItems().size() > 0
				&& cmdPost.getSearchAreaListItems().size() > 0);

	}

	private void enableButtons(boolean b) {
		getMissionToggleButton().setEnabled(b);
		getHypotheseToggleButton().setEnabled(b);
		getPriorityToggleButton().setEnabled(b);
		getRequirementToggleButton().setEnabled(b);
		getDescriptionToggleButton().setEnabled(b);
		getUnitToggleButton().setEnabled(b);
		subMenu.getFinishButton().setEnabled(b);
	}
	
	private void enableTools(boolean b) {
		NavBar navBar = getApplication().getNavBar();
		navBar.getDrawLineToggleButton().setEnabled(b);
		navBar.getSplitToggleButton().setEnabled(b);
		navBar.getEraseToggleButton().setEnabled(b);
		navBar.getFlankToggleButton().setEnabled(b);
		navBar.getPOIToggleButton().setEnabled(b);
		
		if (!b) {
			try {
				getMap().setCurrentToolByRef(null);
				navBar.unselectAll();
			} catch (AutomationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private JToggleButton getDummyToggleButton() {
		if (dummyToggleButton == null) {
			dummyToggleButton = new JToggleButton();
			dummyToggleButton.setVisible(false);
		}
		return dummyToggleButton;
	}

	private JToggleButton getElementToggleButton() {
		if (elementToggleButton == null) {
			try {
				Dimension size = getApplication().getUIFactory()
				.getLargeButtonSize();
				// String iconName = "MapZoomInTool.icon";
				// Icon icon =
				// Utils.createImageIcon(app.getProperty(iconName),iconName);
				elementToggleButton = new JToggleButton();
				elementToggleButton.setText("ELEMENT");
				// elementToggleButton.setIcon(icon);
				elementToggleButton.setPreferredSize(size);
				elementToggleButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							java.awt.event.ActionEvent e) {
						ElementDialog dialog = getElementDialog();
						hideDialogs(dialog);
						if (elementToggleButton.isSelected()
								&& dialog.isVisible()) {
							dialog.setVisible(false);
						} else {
							java.awt.Point p = elementToggleButton
							.getLocationOnScreen();
							p.setLocation(p.x - dialog.getWidth() - 2,
									p.y);
							dialog.setLocation(p);
							dialog.setVisible(true);
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
				Dimension size = getApplication().getUIFactory()
				.getLargeButtonSize();
				// String iconName = "MapZoomInTool.icon";
				// Icon icon =
				// Utils.createImageIcon(app.getProperty(iconName),iconName);
				descriptionToggleButton = new JToggleButton();
				descriptionToggleButton.setText("BESKRIVELSE");
				// descriptionToggleButton.setIcon(icon);
				descriptionToggleButton.setPreferredSize(size);
				descriptionToggleButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							java.awt.event.ActionEvent e) {
						TextAreaDialog dialog = getTextAreaDialog();
						dialog.setHeaderText("Beskrivelse av oppdrag:");
						hideDialogs(dialog);
						if (missionToggleButton.isSelected()
								&& dialog.isVisible()) {
							dialog.setVisible(false);
						} else {
							dialog.setLocationRelativeTo(
									(JComponent) getMap(),
									DiskoDialog.POS_SOUTH, true);
							dialog.setVisible(true);
						}
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
				Dimension size = getApplication().getUIFactory()
				.getLargeButtonSize();
				String iconName = "hypothese.icon";
				Icon icon = Utils.createImageIcon(getApplication().getProperty(
						iconName), iconName);
				hypotheseToggleButton = new JToggleButton();
				hypotheseToggleButton.setIcon(icon);
				hypotheseToggleButton.setPreferredSize(size);
				hypotheseToggleButton.setVisible(false);
				hypotheseToggleButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							java.awt.event.ActionEvent e) {
						HypothesesDialog dialog = getHypothesesDialog();
						hideDialogs(dialog);
						JComponent mapComp = (JComponent) getMap();
						if (hypotheseToggleButton.isSelected()
								& dialog.isVisible()) {
							dialog.setVisible(false);
						} else {
							dialog.setLocationRelativeTo(mapComp,
									DiskoDialog.POS_SOUTH, true);
							dialog.setVisible(true);
						}
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
				Dimension size = getApplication().getUIFactory()
				.getLargeButtonSize();
				String iconName = "list.icon";
				Icon icon = Utils.createImageIcon(getApplication().getProperty(
						iconName), iconName);
				listToggleButton = new JToggleButton();
				listToggleButton.setIcon(icon);
				listToggleButton.setPreferredSize(size);
				listToggleButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							java.awt.event.ActionEvent e) {
						ListDialog dialog = getListDialog();
						hideDialogs(dialog);
						JComponent mapComp = (JComponent) getMap();
						if (listToggleButton.isSelected()
								& dialog.isVisible()) {
							dialog.setVisible(false);
						} else {
							dialog.setLocationRelativeTo(mapComp);
							dialog.setVisible(true);
						}
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
				Dimension size = getApplication().getUIFactory()
				.getLargeButtonSize();
				String iconName = "mission.icon";
				Icon icon = Utils.createImageIcon(getApplication().getProperty(
						iconName), iconName);
				missionToggleButton = new JToggleButton();
				missionToggleButton.setIcon(icon);
				missionToggleButton.setPreferredSize(size);
				missionToggleButton.setVisible(false);
				missionToggleButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							java.awt.event.ActionEvent e) {
						TextAreaDialog dialog = getTextAreaDialog();
						dialog.setHeaderText("Aksjonens oppdrag:");
						hideDialogs(dialog);
						if (missionToggleButton.isSelected()
								&& dialog.isVisible()) {
							dialog.setVisible(false);
						} else {
							dialog.setLocationRelativeTo(
									(JComponent) getMap(),
									DiskoDialog.POS_SOUTH, true);
							dialog.setVisible(true);
						}
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
				Dimension size = getApplication().getUIFactory()
				.getLargeButtonSize();
				// String iconName = "MapZoomInTool.icon";
				// Icon icon =
				// Utils.createImageIcon(app.getProperty(iconName),iconName);
				priorityToggleButton = new JToggleButton();
				priorityToggleButton.setText("PRIORITET");
				// priorityToggleButton.setIcon(icon);
				priorityToggleButton.setPreferredSize(size);
				priorityToggleButton.setVisible(false);
				priorityToggleButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							java.awt.event.ActionEvent e) {
						PriorityDialog dialog = getPriorityDialog();
						hideDialogs(dialog);
						if (priorityToggleButton.isSelected()
								&& dialog.isVisible()) {
							dialog.setVisible(false);
						} else {
							java.awt.Point p = priorityToggleButton
							.getLocationOnScreen();
							p.setLocation(p.x - dialog.getWidth() - 2,
									p.y);
							dialog.setLocation(p);
							dialog.setVisible(true);
						}
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
				Dimension size = getApplication().getUIFactory()
				.getLargeButtonSize();
				// String iconName = "MapZoomInTool.icon";
				// Icon icon =
				// Utils.createImageIcon(app.getProperty(iconName),iconName);
				requirementToggleButton = new JToggleButton();
				requirementToggleButton.setText("KRAV");
				// requirementToggleButton.setIcon(icon);
				requirementToggleButton.setPreferredSize(size);
				requirementToggleButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							java.awt.event.ActionEvent e) {
						SearchRequirementDialog dialog = getSearchRequirementDialog();
						hideDialogs(dialog);
						if (requirementToggleButton.isSelected()
								&& dialog.isVisible()) {
							dialog.setVisible(false);
						} else {
							dialog.setLocationRelativeTo(
									(JComponent) getMap(),
									DiskoDialog.POS_SOUTH, true);
							dialog.setVisible(true);
						}
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return requirementToggleButton;
	}

	JToggleButton getUnitToggleButton() {
		if (unitToggleButton == null) {
			try {
				Dimension size = getApplication().getUIFactory().getLargeButtonSize();
				// String iconName = "MapZoomInTool.icon";
				// Icon icon =
				// Utils.createImageIcon(app.getProperty(iconName),iconName);
				unitToggleButton = new JToggleButton();
				unitToggleButton.setText("ENHET");
				// unitToggleButton.setIcon(icon);
				unitToggleButton.setPreferredSize(size);
				unitToggleButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							java.awt.event.ActionEvent e) {
						UnitSelectionDialog dialog = getUnitSelectionDialog();
						hideDialogs(dialog);
						if (unitToggleButton.isSelected()
								&& dialog.isVisible()) {
							dialog.setVisible(false);
						} else {
							java.awt.Point p = unitToggleButton.getLocationOnScreen();
							p.setLocation(p.x - dialog.getWidth() - 2,
									p.y);
							dialog.setLocation(p);
							dialog.setVisible(true);
						}
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return unitToggleButton;
	}

	class ElementListSelectionListener implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting()) {
				return;
			}
			hideDialogs(null);
			getDummyToggleButton().doClick(); // HACK: unselect all toggle
			// buttons
			getElementDialog().setVisible(false);
			getElementToggleButton().setSelected(false);
			eraseTool.removeAll();
			try {
				JList list = (JList) e.getSource();
				currentAction = (Enum) list.getSelectedValue();
				if (currentAction == null) {
					return;
				}
				ICmdPostIf cmdPost = getMsoManager().getCmdPost();
				IMsoFeatureClass featureClass = null;
				if (currentAction == IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA) {
					showOperationAreaButtons();
					POIType[] poiTypes = { POIType.INTELLIGENCE };
					poiDialog.setTypes(poiTypes);
					featureClass = (IMsoFeatureClass) opAreaLayer.getFeatureClass();
				} 
				else if (currentAction == IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA) {
					if (cmdPost.getOperationAreaListItems().size() == 0) {
						JOptionPane.showMessageDialog(getApplication().getFrame(),
							    "Må lage operasjonsområde først",
							    Utils.translate(currentAction),
							    JOptionPane.WARNING_MESSAGE);
						return;
					} 
					showSearchAreaButtons();
					POIType[] poiTypes = { POIType.INTELLIGENCE };
					poiDialog.setTypes(poiTypes);
					featureClass = (IMsoFeatureClass) searchAreaLayer.getFeatureClass();
				} 
				else {
					if (cmdPost.getOperationAreaListItems().size() == 0) {
						JOptionPane.showMessageDialog(getApplication().getFrame(),
							    "Må lage operasjonsområde først",
							    Utils.translate(currentAction),
							    JOptionPane.WARNING_MESSAGE);
						return;
					} 
					else if (cmdPost.getSearchAreaListItems().size() == 0) {
						JOptionPane.showMessageDialog(getApplication().getFrame(),
							    "Må lage søkeområde først",
							    Utils.translate(currentAction),
							    JOptionPane.WARNING_MESSAGE);
						return;
					} 
					showSearchButtons();
					POIType[] poiTypes = { POIType.START, POIType.VIA,POIType.STOP };
					poiDialog.setTypes(poiTypes);
					featureClass = (IMsoFeatureClass) areaLayer.getFeatureClass();
					flankTool.setFeatureClass(featureClass);
					splitTool.setFeatureClass(featureClass);
				}
				IMsoFeature editFeature = (IMsoFeature) featureClass.createFeature();
				drawTool.setFeatureClass(featureClass);
				IMsoFeatureClass poiFc = (IMsoFeatureClass) poiLayer.getFeatureClass();
				poiTool.setFeatureClass(poiFc);
				eraseTool.addFeatureClass(featureClass);
				eraseTool.addFeatureClass(poiFc);
				drawTool.setEditFeature(editFeature);
				currentMsoObject = editFeature.getMsoObject();
				setFrameText(Utils.translate(currentAction));
				enableTools(true);
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
