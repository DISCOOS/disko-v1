package org.redcross.sar.wp.tactics;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JToggleButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.app.Utils;
import org.redcross.sar.event.DialogEvent;
import org.redcross.sar.event.IDialogEventListener;
import org.redcross.sar.event.IMsoLayerEventListener;
import org.redcross.sar.event.MsoLayerEvent;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.NavBar;
import org.redcross.sar.gui.POIDialog;
import org.redcross.sar.gui.SubMenuPanel;
import org.redcross.sar.gui.UIFactory;
import org.redcross.sar.map.DiskoMap;
import org.redcross.sar.map.DrawTool;
import org.redcross.sar.map.IDiskoMapManager;
import org.redcross.sar.map.POITool;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.map.layer.OperationAreaLayer;
import org.redcross.sar.map.layer.PlannedAreaLayer;
import org.redcross.sar.map.layer.SearchAreaLayer;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.mso.data.IAssignmentListIf;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.IHypothesisIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IOperationAreaIf;
import org.redcross.sar.mso.data.ISearchAreaIf;
import org.redcross.sar.mso.data.ISearchIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IAssignmentIf.AssignmentStatus;
import org.redcross.sar.mso.data.IPOIIf.POIType;
import org.redcross.sar.util.except.IllegalOperationException;
import org.redcross.sar.wp.AbstractDiskoWpModule;

import com.esri.arcgis.interop.AutomationException;

/**
 * Implements the DiskoApTaktikk interface
 *
 * @author geira
 *
 */
public class DiskoWpTacticsImpl extends AbstractDiskoWpModule
		implements IDiskoWpTactics, IDialogEventListener, IMsoLayerEventListener  {

	private ElementDialog elementDialog = null;
	private Dimension buttonSize = null;
	private JToggleButton elementToggleButton = null;
	private JToggleButton listToggleButton = null;
	private JToggleButton missionToggleButton = null;
	private JToggleButton hypotheseToggleButton = null;
	private JToggleButton priorityToggleButton = null;
	private JToggleButton requirementToggleButton = null;
	private JToggleButton descriptionToggleButton = null;
	private JToggleButton unitToggleButton = null;
	private JToggleButton estimateToggleButton = null;
	private POITool poiTool = null;
	private DrawTool drawTool = null;
	private POIDialog poiDialog = null;
	private ArrayList<DiskoDialog> dialogs = null;
	private TextAreaDialog textAreaDialog = null;
	private HypothesesDialog hypothesesDialog = null;
	private PriorityDialog priorityDialog = null;
	private UnitSelectionDialog unitSelectionDialog = null;
	private SearchRequirementDialog searchRequirementDialog = null;
	private EstimateDialog estimateDialog = null;
	private ListDialog listDialog = null;
	private AssignmentStatusDialog assignmentStatusDialog = null;
	private ListSelectionListener elementListSelectionListener = null;
	private OperationAreaLayer opAreaLayer = null;
	private SearchAreaLayer searchAreaLayer = null;
	private DescriptionDialog descriptionDialog = null;
	private PlannedAreaLayer plannedAreaLayer = null;
	private boolean isInitializing = true;
	private IMsoObjectIf currentMsoObj = null;

	/**
	 * Constructs a DiskoApTaktikkImpl
	 * @param rolle A reference to the DiskoRolle
	 */
	public DiskoWpTacticsImpl(IDiskoRole rolle) {
		super(rolle);
		dialogs = new ArrayList<DiskoDialog>();
		elementListSelectionListener = new ElementListSelectionListener();
        
        buttonSize = getApplication().getUIFactory().getLargeButtonSize();
        IDiskoMapManager mapManager = getMap().getMapManager();
		opAreaLayer = (OperationAreaLayer) mapManager.getMsoLayer(IMsoFeatureLayer.LayerCode.OPERATION_AREA_LAYER);
		searchAreaLayer = (SearchAreaLayer) mapManager.getMsoLayer(IMsoFeatureLayer.LayerCode.SEARCH_AREA_LAYER);
		plannedAreaLayer = (PlannedAreaLayer) mapManager.getMsoLayer(IMsoFeatureLayer.LayerCode.AREA_LAYER);
		//listeners
		opAreaLayer.addDiskoLayerEventListener(this);
		searchAreaLayer.addDiskoLayerEventListener(this);
		plannedAreaLayer.addDiskoLayerEventListener(this);
		
		NavBar navBar = getApplication().getNavBar();
		drawTool = navBar.getDrawTool();
		poiTool = navBar.getPOITool();
		poiDialog = (POIDialog) poiTool.getDialog();
		poiDialog.addDialogListener(this);
		// init GUI
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
		layoutButton(getEstimateToggleButton(), true);
		layoutButton(getDescriptionToggleButton(), true);
		layoutButton(getUnitToggleButton(), true);
	}
	
	/*
	 * (non-Javadoc)
	 *
	 * @see com.geodata.engine.disko.task.DiskoAp#getName()
	 */
	public String getName() {
		return "Taktikk";
	}
	
	public void activated() {
		super.activated();
		NavBar navBar = getApplication().getNavBar();
		EnumSet<NavBar.ToolCommandType> myTools = EnumSet.of(NavBar.ToolCommandType.DRAW_TOOL);
		myTools.add(NavBar.ToolCommandType.POI_TOOL);
		myTools.add(NavBar.ToolCommandType.ERASE_COMMAND);
		
		myTools.add(NavBar.ToolCommandType.ZOOM_IN_TOOL);
		myTools.add(NavBar.ToolCommandType.ZOOM_OUT_TOOL);
		myTools.add(NavBar.ToolCommandType.PAN_TOOL);
		myTools.add(NavBar.ToolCommandType.SELECT_FEATURE_TOOL);
		myTools.add(NavBar.ToolCommandType.ZOOM_FULL_EXTENT_COMMAND);
		myTools.add(NavBar.ToolCommandType.ZOOM_TO_LAST_EXTENT_FORWARD_COMMAND);
		myTools.add(NavBar.ToolCommandType.ZOOM_TO_LAST_EXTENT_BACKWARD_COMMAND);
		myTools.add(NavBar.ToolCommandType.MAP_TOGGLE_COMMAND);
		myTools.add(NavBar.ToolCommandType.TOC_COMMAND);
		navBar.showButtons(myTools);

		//this will show the Navbar
		UIFactory uiFactory = getApplication().getUIFactory();
		JToggleButton navToggle = uiFactory.getMainMenuPanel().getNavToggleButton();
		if (!navToggle.isSelected()) {
			navToggle.doClick();
		}
		if (!isEditing) {
			selectElement();
		}
	}

	public void deactivated() {
		super.deactivated();
		hideDialogs(null);
		NavBar navBar = getApplication().getNavBar();
		navBar.hideDialogs();
	}
	
	private void selectElement() {
		ICmdPostIf cmdPost = getMsoManager().getCmdPost();
		JList list = getElementDialog().getElementList();
		list.clearSelection();
		if (cmdPost.getOperationAreaListItems().size() == 0) {
			list.setSelectedValue(IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA, false);
		}
		else if (cmdPost.getSearchAreaListItems().size() == 0) {
			list.setSelectedValue(IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA, false);
		}
		else {
			list.setSelectedValue(ISearchIf.SearchSubType.LINE, false);
		}
		NavBar navBar = getApplication().getNavBar();
		JToggleButton drawButton = navBar.getDrawLineToggleButton();
		if (!isInitializing && !drawButton.isSelected()) {
			drawButton.doClick();
		}
	}
	
	class ElementListSelectionListener implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting()) {
				return;
			}
			getElementDialog().setVisible(false);
			JList list = (JList) e.getSource();
			Enum element = (Enum) list.getSelectedValue();
			if (element == null) {
				return;
			}
			hideDialogs(null);
			ICmdPostIf cmdPost = getMsoManager().getCmdPost();
			if (element == IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA) {
				showOperationAreaButtons();
				POIType[] poiTypes = { POIType.INTELLIGENCE };
				poiDialog.setTypes(poiTypes);
				drawTool.setMsoClassCode(IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA);
			}
			else if (element == IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA) {
				if (cmdPost.getOperationAreaListItems().size() == 0) {
					showWarning("Må lage operasjonsområde først");
					list.setSelectedValue(IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA, false);
					return;
				}
				showSearchAreaButtons();
				POIType[] poiTypes = { POIType.INTELLIGENCE };
				poiDialog.setTypes(poiTypes);
				drawTool.setMsoClassCode(IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA);
			}
			else {
				if (cmdPost.getOperationAreaListItems().size() == 0) {
					showWarning("Må lage operasjonsområde først");
					list.setSelectedValue(IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA, false);
					return;
				}
				else if (cmdPost.getSearchAreaListItems().size() == 0) {
					showWarning("Må lage søkeområde først");
					list.setSelectedValue(IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA, false);
					return;
				}
				showSearchButtons();
				POIType[] poiTypes = { POIType.VIA, POIType.START, POIType.STOP };
				poiDialog.setTypes(poiTypes);
				drawTool.setMsoClassCode(IMsoManagerIf.MsoClassCode.CLASSCODE_AREA);
			}
			setFrameText(Utils.translate(element));
		}
	}

	public void onSelectionChanged(MsoLayerEvent e)
			throws IOException, AutomationException {
		try {
			IMsoFeatureLayer msoLayer = (IMsoFeatureLayer)e.getSource();
			List selection = msoLayer.getSelected();
			if (selection != null && selection.size() > 0) {
				JList elementList = getElementDialog().getElementList();
				IMsoFeature msoFeature = (IMsoFeature)selection.get(0);
				currentMsoObj = msoFeature.getMsoObject();
				if (currentMsoObj == null) {
					return;
				}
				if (currentMsoObj instanceof IAreaIf) {
					IAreaIf area = (IAreaIf)currentMsoObj;
					if (area.getOwningAssignment() instanceof ISearchIf) {
						ISearchIf search = (ISearchIf)area.getOwningAssignment();
						elementList.setSelectedValue(search.getSubType(), false);
					}
					drawTool.setArea(area);
					NavBar navBar = getApplication().getNavBar();
					navBar.getPOIToggleButton().setEnabled(true);
				}
				else {
					elementList.setSelectedValue(currentMsoObj.getMsoClassCode(), false);
				}
			}
		} catch (RuntimeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void dialogCanceled(DialogEvent e) {
		// TODO Auto-generated method stub
	}

	public void dialogFinished(DialogEvent e) {
		// TODO Auto-generated method stub
	}

	public void dialogStateChanged(DialogEvent e) {
		fireTaskStarted();
		isEditing = true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.geodata.engine.disko.task.DiskoAp#finish()
	 */
	public void finish() {
		if (currentMsoObj != null) {
			if (currentMsoObj instanceof IOperationAreaIf) {
				IOperationAreaIf opArea = (IOperationAreaIf) currentMsoObj;
				opArea.setRemarks(getTextAreaDialog().getText());
			}
			else if (currentMsoObj instanceof ISearchAreaIf) {
				ISearchAreaIf searchArea = (ISearchAreaIf) currentMsoObj;
				IHypothesisIf hypo = getHypothesesDialog().getSelectedHypotheses();
				if (hypo == null) {
					showWarning("Må tilordne en hypotese til søksområde");
					getHypotheseToggleButton().doClick();
					return;
				}
				searchArea.setSearchAreaHypothesis(hypo);
				searchArea.setPriority(getPriorityDialog().getPriority());
			}
			else {
				IAreaIf area = (IAreaIf) currentMsoObj;
				ISearchIf search = (ISearchIf)area.getOwningAssignment();
				if (search == null) {
					ICmdPostIf cmdPost = getMsoManager().getCmdPost();
					IAssignmentListIf assignmentList = cmdPost.getAssignmentList();
					search = assignmentList.createSearch();
					try {
						search.setPlannedArea(area);
					} catch (IllegalOperationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				JList elementList = getElementDialog().getElementList();
				search.setSubType((ISearchIf.SearchSubType)elementList.getSelectedValue());
				SearchRequirementDialog dialog = getSearchRequirementDialog();
				search.setPriority(dialog.getPriority());
				search.setRemarks(dialog.getRemarks());
				search.setPriority(dialog.getPriority());
				search.setPlannedAccuracy(dialog.getAccuracy());
				search.setPlannedPersonnel(dialog.getPersonelNeed());
				search.setPlannedProgress(getEstimateDialog().getEstimatedTime());

				// dialog for setting status
				if (search.getStatus() != AssignmentStatus.READY) {
					IUnitIf unit = getUnitSelectionDialog().getSelectedUnit();
					SubMenuPanel subMenu = getApplication().getUIFactory().getSubMenuPanel();
					java.awt.Point p = subMenu.getFinishButton().getLocationOnScreen();
					p.setLocation(p.x - getAssignmentStatusDialog().getWidth() - 2, p.y-1);
					getAssignmentStatusDialog().setLocation(p);
					getAssignmentStatusDialog().setAssignment(search, unit);
					getAssignmentStatusDialog().setVisible(true);
				}
			}
		}
		getMsoModel().commit();
		fireTaskFinished();
		reset();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.geodata.engine.disko.task.DiskoAp#cancel()
	 */
	public void cancel() {
		fireTaskCanceled();
		getMsoModel().rollback();
		reset();
	}

	private void reset() {
		currentMsoObj = null;
		hideDialogs(null);
		isEditing = false;
		drawTool.setMsoClassCode(null);
		drawTool.setArea(null);
		setLayersSelectable(true);
		
		callingWp = null;
		//Possible to go back ?
		/*if (callingWp != null) {
			String id = getDiskoRole().getName()+callingWp;
			getDiskoRole().selectDiskoWpModule(id);
			callingWp = null;
			return;
		}*/
		clearSelected();
		//select next element
		selectElement();
	}
	
	private void clearSelected() {
		try {
			IDiskoMapManager mapManager = getMap().getMapManager();
			List msoLayers = mapManager.getMsoLayers();
			for (int i = 0; i < msoLayers.size(); i++) {
				IMsoFeatureLayer msoLayer = (IMsoFeatureLayer)msoLayers.get(i);
				if (msoLayer.getSelected().size() > 0) {
					msoLayer.clearSelected();
					getMap().partialRefresh(msoLayer, null);
				}
			}
		} catch (AutomationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			textAreaDialog = new TextAreaDialog(this);
			textAreaDialog.setIsToggable(false);
			textAreaDialog.addDialogListener(this);
			dialogs.add(textAreaDialog);
		}
		return textAreaDialog;
	}

	private HypothesesDialog getHypothesesDialog() {
		if (hypothesesDialog == null) {
			hypothesesDialog = new HypothesesDialog(this);
			hypothesesDialog.setIsToggable(false);
			hypothesesDialog.addDialogListener(this);
			dialogs.add(hypothesesDialog);
		}
		return hypothesesDialog;
	}

	private PriorityDialog getPriorityDialog() {
		if (priorityDialog == null) {
			priorityDialog = new PriorityDialog(this);
			priorityDialog.setIsToggable(false);
			priorityDialog.addDialogListener(this);
			dialogs.add(priorityDialog);
		}
		return priorityDialog;
	}

	private SearchRequirementDialog getSearchRequirementDialog() {
		if (searchRequirementDialog == null) {
			searchRequirementDialog = new SearchRequirementDialog(this);
			searchRequirementDialog.setIsToggable(false);
			searchRequirementDialog.addDialogListener(this);
			dialogs.add(searchRequirementDialog);
		}
		return searchRequirementDialog;
	}
	
	private EstimateDialog getEstimateDialog() {
		if (estimateDialog == null) {
			estimateDialog = new EstimateDialog(this);
			estimateDialog.setIsToggable(false);
			estimateDialog.addDialogListener(this);
			dialogs.add(estimateDialog);
		}
		return estimateDialog;
	}
	
	private DescriptionDialog getDescriptionDialog() {
		if (descriptionDialog == null) {
			descriptionDialog = new DescriptionDialog(this);
			descriptionDialog.setIsToggable(false);
			descriptionDialog.addDialogListener(this);
			dialogs.add(descriptionDialog);
		}
		return descriptionDialog;
	}

	private UnitSelectionDialog getUnitSelectionDialog() {
		if (unitSelectionDialog == null) {
			unitSelectionDialog = new UnitSelectionDialog(this);
			unitSelectionDialog.setIsToggable(false);
			unitSelectionDialog.addDialogListener(this);
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

	private AssignmentStatusDialog getAssignmentStatusDialog() {
		if (assignmentStatusDialog == null) {
			assignmentStatusDialog = new AssignmentStatusDialog(this);
			assignmentStatusDialog.setIsToggable(false);
		}
		return assignmentStatusDialog;
	}

	private void showOperationAreaButtons() {
		showElementListButton();
		getMissionToggleButton().setVisible(true);
		getHypotheseToggleButton().setVisible(false);
		getPriorityToggleButton().setVisible(false);
		getRequirementToggleButton().setVisible(false);
		getEstimateToggleButton().setVisible(false);
		getDescriptionToggleButton().setVisible(false);
		getUnitToggleButton().setVisible(false);

		NavBar navBar = getApplication().getNavBar();
		navBar.getSelectFeatureToggleButton().setVisible(true);
		navBar.getEraseButton().setVisible(true);
		navBar.getPOIToggleButton().setVisible(true);
		navBar.getPOIToggleButton().setEnabled(true);
	}

	private void showSearchAreaButtons() {
		showElementListButton();
		getMissionToggleButton().setVisible(false);
		getHypotheseToggleButton().setVisible(true);
		getPriorityToggleButton().setVisible(true);
		getRequirementToggleButton().setVisible(false);
		getEstimateToggleButton().setVisible(false);
		getDescriptionToggleButton().setVisible(false);
		getUnitToggleButton().setVisible(false);

		NavBar navBar = getApplication().getNavBar();
		navBar.getPOIToggleButton().setVisible(true);
		navBar.getPOIToggleButton().setEnabled(true);
		navBar.getSelectFeatureToggleButton().setVisible(true);
		navBar.getEraseButton().setVisible(true);
	}

	private void showSearchButtons() {
		showElementListButton();
		getMissionToggleButton().setVisible(false);
		getHypotheseToggleButton().setVisible(false);
		getPriorityToggleButton().setVisible(false);
		getRequirementToggleButton().setVisible(true);
		getEstimateToggleButton().setVisible(true);
		getDescriptionToggleButton().setVisible(true);
		getUnitToggleButton().setVisible(true);

		NavBar navBar = getApplication().getNavBar();
		navBar.getPOIToggleButton().setVisible(true);
		navBar.getPOIToggleButton().setEnabled(false);
		navBar.getSelectFeatureToggleButton().setVisible(true);
		navBar.getEraseButton().setVisible(true);
	}

	private void showElementListButton() {
		ICmdPostIf cmdPost = getMsoManager().getCmdPost();
		getListToggleButton().setVisible(
				cmdPost.getOperationAreaListItems().size() > 0
				&& cmdPost.getSearchAreaListItems().size() > 0);

	}
	
	private void setLayersSelectable(boolean isSelectable) {
		try {
			opAreaLayer.setSelectable(isSelectable);
			searchAreaLayer.setSelectable(isSelectable);
			plannedAreaLayer.setSelectable(isSelectable);
		} catch (AutomationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private JToggleButton getElementToggleButton() {
		if (elementToggleButton == null) {
			try {
				elementToggleButton = new JToggleButton();
				Enum key = TacticsTaskType.ELEMENT_TASK;
				ImageIcon icon = Utils.getIcon(key);
				if (icon != null) {
					elementToggleButton.setIcon(icon);
				} else {
					elementToggleButton.setText(key.name());
				}
				elementToggleButton.setPreferredSize(buttonSize);
				elementToggleButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (isEditing) {
							Enum element = (Enum) getElementDialog().getElementList().getSelectedValue();
							showWarning("Du må avslutte "+Utils.translate(element)+" først");
							return;
						}
						ElementDialog dialog = getElementDialog();
						hideDialogs(dialog);
						if (elementToggleButton.isSelected() && dialog.isVisible()) {
							dialog.setVisible(false);
						}
						else {
							java.awt.Point p = elementToggleButton.getLocationOnScreen();
							p.setLocation(p.x - dialog.getWidth() - 2, p.y);
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
				descriptionToggleButton = new JToggleButton();
				Enum key = TacticsTaskType.DESCRIPTION_TASK;
				ImageIcon icon = Utils.getIcon(key);
				if (icon != null) {
					descriptionToggleButton.setIcon(icon);
				} else {
					descriptionToggleButton.setText(key.name());
				}
				descriptionToggleButton.setPreferredSize(buttonSize);
				descriptionToggleButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						DescriptionDialog dialog = getDescriptionDialog();
						hideDialogs(dialog);
						if (descriptionToggleButton.isSelected() && dialog.isVisible()) {
							dialog.setVisible(false);
						} else {
							dialog.setLocationRelativeTo((JComponent) getMap(),
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
				hypotheseToggleButton = new JToggleButton();
				Enum key = TacticsTaskType.HYPOTHESIS_TASK;
				ImageIcon icon = Utils.getIcon(key);
				if (icon != null) {
					hypotheseToggleButton.setIcon(icon);
				} else {
					hypotheseToggleButton.setText(key.name());
				}
				hypotheseToggleButton.setPreferredSize(buttonSize);
				hypotheseToggleButton.setVisible(false);
				hypotheseToggleButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						HypothesesDialog dialog = getHypothesesDialog();
						hideDialogs(dialog);
						JComponent mapComp = (JComponent) getMap();
						if (hypotheseToggleButton.isSelected() && dialog.isVisible()) {
							dialog.setVisible(false);
						}
						else {
							dialog.setLocationRelativeTo(mapComp,DiskoDialog.POS_SOUTH, true);
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
				listToggleButton = new JToggleButton();
				Enum key = TacticsTaskType.LIST_TASK;
				ImageIcon icon = Utils.getIcon(key);
				if (icon != null) {
					listToggleButton.setIcon(icon);
				} else {
					listToggleButton.setText(key.name());
				}
				listToggleButton.setPreferredSize(buttonSize);
				listToggleButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						ListDialog dialog = getListDialog();
						hideDialogs(dialog);
						JComponent mapComp = (JComponent) getMap();
						if (listToggleButton.isSelected() && dialog.isVisible()) {
							dialog.setVisible(false);
						}
						else {
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
				missionToggleButton = new JToggleButton();
				Enum key = TacticsTaskType.MISSON_TASK;
				ImageIcon icon = Utils.getIcon(key);
				if (icon != null) {
					missionToggleButton.setIcon(icon);
				} else {
					missionToggleButton.setText(key.name());
				}
				missionToggleButton.setPreferredSize(buttonSize);
				missionToggleButton.setVisible(false);
				missionToggleButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						TextAreaDialog dialog = getTextAreaDialog();
						dialog.setHeaderText("Aksjonens oppdrag:");
						hideDialogs(dialog);
						if (missionToggleButton.isSelected() && dialog.isVisible()) {
							dialog.setVisible(false);
						}
						else {
							dialog.setLocationRelativeTo((JComponent) getMap(),
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
				priorityToggleButton = new JToggleButton();
				Enum key = TacticsTaskType.PRIORITY_TASK;
				ImageIcon icon = Utils.getIcon(key);
				if (icon != null) {
					priorityToggleButton.setIcon(icon);
				} else {
					priorityToggleButton.setText(key.name());
				}
				priorityToggleButton.setPreferredSize(buttonSize);
				priorityToggleButton.setVisible(false);
				priorityToggleButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						PriorityDialog dialog = getPriorityDialog();
						hideDialogs(dialog);
						if (priorityToggleButton.isSelected() && dialog.isVisible()) {
							dialog.setVisible(false);
						}
						else {
							java.awt.Point p = priorityToggleButton.getLocationOnScreen();
							p.setLocation(p.x - dialog.getWidth() - 2, p.y);
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
				requirementToggleButton = new JToggleButton();
				Enum key = TacticsTaskType.REQUIREMENT_TASK;
				ImageIcon icon = Utils.getIcon(key);
				if (icon != null) {
					requirementToggleButton.setIcon(icon);
				} else {
					requirementToggleButton.setText(key.name());
				}
				requirementToggleButton.setPreferredSize(buttonSize);
				requirementToggleButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						SearchRequirementDialog dialog = getSearchRequirementDialog();
						hideDialogs(dialog);
						if (requirementToggleButton.isSelected() && dialog.isVisible()) {
							dialog.setVisible(false);
						}
						else {
							dialog.setLocationRelativeTo((JComponent) getMap(),
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
	
	private JToggleButton getEstimateToggleButton() {
		if (estimateToggleButton == null) {
			try {
				estimateToggleButton = new JToggleButton();
				Enum key = TacticsTaskType.ESTIMATE_TASK;
				ImageIcon icon = Utils.getIcon(key);
				if (icon != null) {
					estimateToggleButton.setIcon(icon);
				} else {
					estimateToggleButton.setText(key.name());
				}
				estimateToggleButton.setPreferredSize(buttonSize);
				estimateToggleButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						EstimateDialog dialog = getEstimateDialog();
						hideDialogs(dialog);
						if (estimateToggleButton.isSelected() && dialog.isVisible()) {
							dialog.setVisible(false);
						} 
						else {
							dialog.setLocationRelativeTo((JComponent) getMap(), 
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
		return estimateToggleButton;
	}

	JToggleButton getUnitToggleButton() {
		if (unitToggleButton == null) {
			try {
				unitToggleButton = new JToggleButton();
				Enum key = TacticsTaskType.UNIT_TASK;
				ImageIcon icon = Utils.getIcon(key);
				if (icon != null) {
					unitToggleButton.setIcon(icon);
				} else {
					unitToggleButton.setText(key.name());
				}
				unitToggleButton.setPreferredSize(buttonSize);
				unitToggleButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						UnitSelectionDialog dialog = getUnitSelectionDialog();
						hideDialogs(dialog);
						if (unitToggleButton.isSelected() && dialog.isVisible()) {
							dialog.setVisible(false);
						}
						else {
							java.awt.Point p = unitToggleButton.getLocationOnScreen();
							p.setLocation(p.x - dialog.getWidth() - 2, p.y);
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

}
