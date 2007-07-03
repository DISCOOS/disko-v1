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
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.app.Utils;
import org.redcross.sar.event.DialogEvent;
import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.event.IDialogEventListener;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.NavBar;
import org.redcross.sar.gui.POIDialog;
import org.redcross.sar.gui.SubMenuPanel;
import org.redcross.sar.gui.TextAreaDialog;
import org.redcross.sar.gui.UIFactory;
import org.redcross.sar.map.DiskoMap;
import org.redcross.sar.map.DrawTool;
import org.redcross.sar.map.IDiskoMapManager;
import org.redcross.sar.map.POITool;
import org.redcross.sar.map.SelectFeatureTool;
import org.redcross.sar.map.feature.AreaFeatureClass;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.IMsoFeatureClass;
import org.redcross.sar.map.feature.OperationAreaFeatureClass;
import org.redcross.sar.map.feature.SearchAreaFeatureClass;
import org.redcross.sar.map.layer.AreaLayer;
import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.map.layer.OperationAreaLayer;
import org.redcross.sar.map.layer.POILayer;
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
import org.redcross.sar.mso.data.IAssignmentIf.AssignmentStatus;
import org.redcross.sar.mso.data.IPOIIf.POIType;
import org.redcross.sar.mso.data.ISearchIf.SearchSubType;
import org.redcross.sar.util.except.IllegalOperationException;
import org.redcross.sar.wp.AbstractDiskoWpModule;
import org.redcross.sar.wp.TestData.BuildTestData;

import com.esri.arcgis.interop.AutomationException;

/**
 * Implements the DiskoApTaktikk interface
 *
 * @author geira
 *
 */
public class DiskoWpTacticsImpl extends AbstractDiskoWpModule
		implements IDiskoWpTactics, IDialogEventListener {

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
	private SelectFeatureTool selectFeatureTool = null;
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
	private POILayer poiLayer = null;
	private AreaLayer areaLayer = null;
	private IMsoFeatureLayer currentLayer = null;
	private boolean isInitializing = true;

	/**
	 * Constructs a DiskoApTaktikkImpl
	 * @param rolle A reference to the DiskoRolle
	 */
	public DiskoWpTacticsImpl(IDiskoRole rolle) {
		super(rolle);
		dialogs = new ArrayList<DiskoDialog>();
		elementListSelectionListener = new ElementListSelectionListener();

		BuildTestData.createCmdPost(getMsoModel());
	    BuildTestData.createUnitsAndAssignments(getMsoModel());
        BuildTestData.createMessages(getMsoModel());
        buttonSize = getApplication().getUIFactory().getLargeButtonSize();
		initialize();
	}

	private void initialize() {
		loadProperties("properties");
		DiskoMap map = (DiskoMap) getMap();
		map.addDiskoMapEventListener(this);
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

	public void onMapReplaced(DiskoMapEvent e)
			throws IOException, AutomationException {
		//must be done here after the map has been loaded
		final JToggleButton button = getApplication().getNavBar().getDrawLineToggleButton();
		Runnable r = new Runnable(){
            public void run() {
            	if (!button.isSelected()) {
            		button.doClick();
            	}
            	isInitializing = false;
            }
        };
        SwingUtilities.invokeLater(r);
	}

	public void onSelectionChanged(DiskoMapEvent e)
			throws IOException, AutomationException {
		try {
			IMsoFeatureClass msoFC = (IMsoFeatureClass)e.getSource();
			List selection = msoFC.getSelected();
			if (selection != null && selection.size() > 0) {
				JList elementList = getElementDialog().getElementList();
				getElementToggleButton().setEnabled(false);
				IMsoFeature msoFeature = (IMsoFeature)selection.get(0);
				IMsoObjectIf msoObject = msoFeature.getMsoObject();

				if (msoObject instanceof IAreaIf) {
					IAreaIf area = (IAreaIf)msoObject;
					if (area.getOwningAssignment() instanceof ISearchIf) {
						ISearchIf search = (ISearchIf)area.getOwningAssignment();
						elementList.setSelectedValue(search.getSubType(), false);
					}
				}
				else {
					elementList.setSelectedValue(msoObject.getMsoClassCode(), false);
				}
				drawTool.setEditFeature(msoFeature);
			}
		} catch (RuntimeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void editLayerChanged(DiskoMapEvent e) {
		enableFinishCancel(true);
		fireTaskStarted();
		isEditing = true;
		
		IMsoFeature msoFeature = drawTool.getEditFeature();
		if (msoFeature.getMsoObject() instanceof IAreaIf) {
			IAreaIf area = (IAreaIf)msoFeature.getMsoObject();
			poiTool.setArea(area);
			getDescriptionDialog().setArea(area);
			
			NavBar navBar = getApplication().getNavBar();
			navBar.getPOIToggleButton().setEnabled(true);
		}
	}

	public void dialogCanceled(DialogEvent e) {
		// TODO Auto-generated method stub
	}

	public void dialogFinished(DialogEvent e) {
		// TODO Auto-generated method stub
	}

	public void dialogStateChanged(DialogEvent e) {
		enableFinishCancel(true);
		fireTaskStarted();
		isEditing = true;
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
		myTools.add(NavBar.ToolCommandType.TOC_TOOL);
		navBar.showButtons(myTools);

		IDiskoMapManager mapManager = getMap().getMapManager();
		opAreaLayer = (OperationAreaLayer) mapManager.getMsoLayer(IMsoFeatureLayer.LayerCode.OPERATION_AREA_LAYER);
		searchAreaLayer = (SearchAreaLayer) mapManager.getMsoLayer(IMsoFeatureLayer.LayerCode.SEARCH_AREA_LAYER);
		poiLayer = (POILayer) mapManager.getMsoLayer(IMsoFeatureLayer.LayerCode.POI_LAYER);
		areaLayer = (AreaLayer) mapManager.getMsoLayer(IMsoFeatureLayer.LayerCode.AREA_LAYER);

		drawTool = navBar.getDrawTool();
		poiTool = navBar.getPOITool();
		selectFeatureTool = navBar.getSelectFeatureTool();
		poiDialog = (POIDialog) poiTool.getDialog();

		//this will show the Nav bar
		UIFactory uiFactory = getApplication().getUIFactory();
		JToggleButton navToggle = uiFactory.getMainMenuPanel().getNavToggleButton();
		if (!navToggle.isSelected()) {
			navToggle.doClick();
		}
		if (!isEditing) {
			selectElement();
			enableSelection(true);
			enableFinishCancel(false);
		}
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

	public void deactivated() {
		super.deactivated();
		hideDialogs(null);
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

	/*
	 * (non-Javadoc)
	 *
	 * @see com.geodata.engine.disko.task.DiskoAp#finish()
	 */
	public void finish() {
		try {
			IMsoFeature msoFeature = drawTool.getEditFeature();
			Enum element = (Enum) getElementDialog().getElementList().getSelectedValue();
			if (element == IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA) {
				IOperationAreaIf opArea = (IOperationAreaIf) msoFeature.getMsoObject();
				opArea.setRemarks(getTextAreaDialog().getText());
			}
			else if (element == IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA) {
				ISearchAreaIf searchArea = (ISearchAreaIf) msoFeature.getMsoObject();
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
				IAreaIf area = (IAreaIf) msoFeature.getMsoObject();
				ISearchIf search = (ISearchIf)area.getOwningAssignment();
				if (area.getOwningAssignment() == null) {
					ICmdPostIf cmdPost = getMsoManager().getCmdPost();
					IAssignmentListIf assignmentList = cmdPost.getAssignmentList();
					search = assignmentList.createSearch();
					search.setPlannedArea(area);
				}
				SearchRequirementDialog dialog = getSearchRequirementDialog();
				search.setPriority(dialog.getPriority());
				search.setRemarks(dialog.getRemarks());
				search.setPriority(dialog.getPriority());
				search.setSubType((SearchSubType) element);
				search.setPlannedAccuracy(dialog.getAccuracy());
				search.setPlannedPersonnel(dialog.getPersonelNeed());
				search.setPlannedProgress(getEstimateDialog().getEstimatedTime());
				
				//unit
				//IUnitIf unit = getUnitSelectionDialog().getSelectedUnit();
				//unit.addUnitAssignment(search, search.getStatus());
					
				// dialog for setting status
				if (search.getStatus() != AssignmentStatus.READY) {
					SubMenuPanel subMenu = getApplication().getUIFactory().getSubMenuPanel();
					java.awt.Point p = subMenu.getFinishButton().getLocationOnScreen();
					p.setLocation(p.x - getAssignmentStatusDialog().getWidth() - 2, p.y-1);
					getAssignmentStatusDialog().setLocation(p);
					getAssignmentStatusDialog().setAssignment(search);
					getAssignmentStatusDialog().setVisible(true);
				}
			}
		}
		catch (IllegalOperationException e) {
			getMsoModel().rollback();
			fireTaskCanceled();
			e.printStackTrace();
		}
		finally {
			getMsoModel().commit();
			fireTaskFinished();
		}
		reset();
	}

	private void reset() {
		hideDialogs(null);
		getElementToggleButton().setEnabled(true);
		isEditing = false;
		enableFinishCancel(false);
		poiTool.setArea(null);
		
		callingWp = null;
		//Possible to go back ?
		/*if (callingWp != null) {
			String id = getDiskoRole().getName()+callingWp;
			getDiskoRole().selectDiskoWpModule(id);
			callingWp = null;
			return;
		}*/
		try {
			IMsoFeatureClass featureClass = (IMsoFeatureClass)currentLayer.getFeatureClass();
			featureClass.clearSelected();
			getMap().partialRefresh(currentLayer, null);
		} catch (AutomationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//select next element
		selectElement();
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
			textAreaDialog.addDialogListener(this);
			dialogs.add(textAreaDialog);
		}
		return textAreaDialog;
	}

	private HypothesesDialog getHypothesesDialog() {
		if (hypothesesDialog == null) {
			hypothesesDialog = new HypothesesDialog(getApplication());
			hypothesesDialog.setIsToggable(false);
			hypothesesDialog.addDialogListener(this);
			dialogs.add(hypothesesDialog);
		}
		return hypothesesDialog;
	}

	private PriorityDialog getPriorityDialog() {
		if (priorityDialog == null) {
			priorityDialog = new PriorityDialog(getApplication().getFrame());
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
			unitSelectionDialog = new UnitSelectionDialog(getApplication());
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
		navBar.getSelectFeatureToggleButton().setVisible(false);
		navBar.getEraseButton().setVisible(false);
		navBar.getPOIToggleButton().setVisible(true);
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

	private void enableFinishCancel(boolean b) {
		SubMenuPanel subMenu = getApplication().getUIFactory().getSubMenuPanel();
		subMenu.getFinishButton().setEnabled(b);
		subMenu.getCancelButton().setEnabled(b);
	}

	private void enableSelection(boolean enable) {
		try {
			OperationAreaFeatureClass opAreaFC  = (OperationAreaFeatureClass)opAreaLayer.getFeatureClass();
			SearchAreaFeatureClass searchAreaFC = (SearchAreaFeatureClass)searchAreaLayer.getFeatureClass();
			AreaFeatureClass areaFC = (AreaFeatureClass)areaLayer.getFeatureClass();
			//POIFeatureClass poiFC = (POIFeatureClass)poiLayer.getFeatureClass();

			selectFeatureTool.removeAll();
			NavBar navBar = getApplication().getNavBar();
			if (enable) {
				opAreaFC.addDiskoMapEventListener(this);
				searchAreaFC.addDiskoMapEventListener(this);
				areaFC.addDiskoMapEventListener(this);
				//poiFC.addDiskoMapEventListener(this);

				opAreaFC.addDiskoMapEventListener(getTextAreaDialog());
				searchAreaFC.addDiskoMapEventListener(getHypothesesDialog());
				searchAreaFC.addDiskoMapEventListener(getPriorityDialog());
				areaFC.addDiskoMapEventListener(getSearchRequirementDialog());
				areaFC.addDiskoMapEventListener(getEstimateDialog());
				areaFC.addDiskoMapEventListener(getDescriptionDialog());
				areaFC.addDiskoMapEventListener(getUnitSelectionDialog());

				selectFeatureTool.addSelectableLayer(poiLayer);
				selectFeatureTool.addSelectableLayer(areaLayer);
				selectFeatureTool.addSelectableLayer(searchAreaLayer);
				selectFeatureTool.addSelectableLayer(opAreaLayer);
				navBar.getSelectFeatureToggleButton().setEnabled(true);
				//navBar.getSelectFeatureToggleButton().doClick();
			} else {
				opAreaFC.removeDiskoMapEventListener(this);
				searchAreaFC.removeDiskoMapEventListener(this);
				areaFC.removeDiskoMapEventListener(this);
				//poiFC.removeDiskoMapEventListener(this);

				opAreaFC.removeDiskoMapEventListener(getTextAreaDialog());
				searchAreaFC.removeDiskoMapEventListener(getHypothesesDialog());
				searchAreaFC.removeDiskoMapEventListener(getPriorityDialog());
				areaFC.removeDiskoMapEventListener(getSearchRequirementDialog());
				areaFC.removeDiskoMapEventListener(getEstimateDialog());
				areaFC.removeDiskoMapEventListener(getDescriptionDialog());
				areaFC.removeDiskoMapEventListener(getUnitSelectionDialog());
				navBar.getSelectFeatureToggleButton().setEnabled(false);
			}
		} catch (AutomationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			ICmdPostIf cmdPost = getMsoManager().getCmdPost();
			if (element == IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA) {
				showOperationAreaButtons();
				POIType[] poiTypes = { POIType.INTELLIGENCE };
				poiDialog.setTypes(poiTypes);
				currentLayer = opAreaLayer;
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
				currentLayer = searchAreaLayer;
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
				POIType[] poiTypes = { POIType.VIA };
				poiDialog.setTypes(poiTypes);
				currentLayer = areaLayer;
			}
			drawTool.setEditLayer(currentLayer);
			poiTool.setEditLayer(poiLayer);
			setFrameText(Utils.translate(element));
		}
	}
}
