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
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.app.Utils;
import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.NavBar;
import org.redcross.sar.gui.POIDialog;
import org.redcross.sar.gui.TextAreaDialog;
import org.redcross.sar.gui.UIFactory;
import org.redcross.sar.map.DiskoMap;
import org.redcross.sar.map.DrawTool;
import org.redcross.sar.map.EraseTool;
import org.redcross.sar.map.FlankTool;
import org.redcross.sar.map.IDiskoMapManager;
import org.redcross.sar.map.POITool;
import org.redcross.sar.map.SelectFeatureTool;
import org.redcross.sar.map.SplitTool;
import org.redcross.sar.map.feature.AreaFeatureClass;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.IMsoFeatureClass;
import org.redcross.sar.map.feature.OperationAreaFeatureClass;
import org.redcross.sar.map.feature.POIFeatureClass;
import org.redcross.sar.map.feature.SearchAreaFeatureClass;
import org.redcross.sar.map.layer.AreaLayer;
import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.map.layer.OperationAreaLayer;
import org.redcross.sar.map.layer.POILayer;
import org.redcross.sar.map.layer.SearchAreaLayer;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IAssignmentListIf;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.IHypothesisIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IOperationAreaIf;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.mso.data.ISearchAreaIf;
import org.redcross.sar.mso.data.ISearchIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IPOIIf.POIType;
import org.redcross.sar.mso.data.ISearchIf.SearchSubType;
import org.redcross.sar.util.except.IllegalOperationException;
import org.redcross.sar.wp.AbstractDiskoWpModule;
import org.redcross.sar.wp.TestData.BuildTestData;

import com.esri.arcgis.geodatabase.IFeatureClass;
import com.esri.arcgis.geometry.GeometryBag;
import com.esri.arcgis.geometry.IGeometry;
import com.esri.arcgis.geometry.Polygon;
import com.esri.arcgis.interop.AutomationException;

/**
 * Implements the DiskoApTaktikk interface
 * 
 * @author geira
 * 
 */
public class DiskoWpTacticsImpl extends AbstractDiskoWpModule 
		implements IDiskoWpTactics {

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
	private Enum currentElement = null;
	private POITool poiTool = null;
	private DrawTool drawTool = null;
	private EraseTool eraseTool = null;
	private FlankTool flankTool = null;
	private SplitTool splitTool = null;
	private POIDialog poiDialog = null;
	private SelectFeatureTool selectFeatureTool = null;
	private ArrayList<DiskoDialog> dialogs = null;
	private TextAreaDialog textAreaDialog = null;
	private HypothesesDialog hypothesesDialog = null;
	private PriorityDialog priorityDialog = null;
	private UnitSelectionDialog unitSelectionDialog = null;
	private SearchRequirementDialog searchRequirementDialog = null;
	private ListDialog listDialog = null;
	private ListSelectionListener elementListSelectionListener = null;
	private OperationAreaLayer opAreaLayer = null;
	private SearchAreaLayer searchAreaLayer = null;
	private POILayer poiLayer = null;
	private AreaLayer areaLayer = null;
	private IAssignmentIf currentAssignment = null;
	private IMsoFeature currentMsoFeature = null;
	private boolean isInitializing = true;

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
		
		BuildTestData.createCmdPost(getMsoModel());
	    BuildTestData.createUnitsAndAssignments(getMsoModel()); 
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
            	button.doClick();
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
				
				
				if (msoObject instanceof IPOIIf) {
					
				} else if (msoObject instanceof IAreaIf) {
					GeometryBag geomBag = (GeometryBag)msoFeature.getShape();
					for (int i = 0; i < geomBag.getGeometryCount(); i++) {
						IGeometry geom = geomBag.getGeometry(i);
						if (!isContaining(opAreaLayer.getFeatureClass(), geom)) {
							showWarning("Oppdraget er ikke innenfor operasjonsomr�de. Tegn p� nytt");
							msoFeature.removeGeodataAt(i);
					        return;
						}
					}
					IAreaIf area = (IAreaIf)msoObject;
					AreaFeatureClass areaFC = (AreaFeatureClass) areaLayer.getFeatureClass();
					poiTool.setArea(area, areaFC);
					showSearchButtons();
					currentAssignment = area.getOwningAssignment();
					if (currentAssignment instanceof ISearchIf) {
						ISearchIf search = (ISearchIf)currentAssignment;
						elementList.setSelectedValue(search.getSubType(), false);
						drawTool.setEditFeature(msoFeature);
						setFrameText(Utils.translate(search.getSubType()));
					}
				} else if (msoObject instanceof ISearchAreaIf) {
					elementList.setSelectedValue(msoObject.getMsoClassCode(), false);
					setFrameText(Utils.translate(msoObject.getMsoClassCode()));
					if (!isContaining(opAreaLayer.getFeatureClass(), msoFeature.getShape())) {
						showWarning("S�keomr�de er ikke innenfor operasjonsomr�de. Tegn p� nytt");
						msoFeature.delete();
						return;
					}
					showSearchAreaButtons();
				} else if (msoObject instanceof IOperationAreaIf) {
					elementList.setSelectedValue(msoObject.getMsoClassCode(), false);
					setFrameText(Utils.translate(msoObject.getMsoClassCode()));
					showOperationAreaButtons();
				}
				currentMsoFeature = msoFeature;
				super.fireTaskStarted();
			}
		} catch (RuntimeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
		myInterests.add(NavBar.ToolCommandType.SELECT_FEATURE_TOOL);
		myInterests.add(NavBar.ToolCommandType.ZOOM_FULL_EXTENT_COMMAND);
		myInterests.add(NavBar.ToolCommandType.ZOOM_TO_LAST_EXTENT_FORWARD_COMMAND);
		myInterests.add(NavBar.ToolCommandType.ZOOM_TO_LAST_EXTENT_BACKWARD_COMMAND);
		navBar.showButtons(myInterests);

		IDiskoMapManager mapManager = getMap().getMapManager();
		opAreaLayer = (OperationAreaLayer) mapManager.getMsoLayer(IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA);
		searchAreaLayer = (SearchAreaLayer) mapManager.getMsoLayer(IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA);
		poiLayer = (POILayer) mapManager.getMsoLayer(IMsoManagerIf.MsoClassCode.CLASSCODE_POI);
		areaLayer = (AreaLayer) mapManager.getMsoLayer(IMsoManagerIf.MsoClassCode.CLASSCODE_AREA);
		
		drawTool = navBar.getDrawTool();
		poiTool = navBar.getPOITool();
		eraseTool = navBar.getEraseTool();
		flankTool = navBar.getFlankTool();
		splitTool = navBar.getSplitTool();
		selectFeatureTool = navBar.getSelectFeatureTool();
		poiDialog = (POIDialog) poiTool.getDialog();
		
		//this will show the Nav bar
		UIFactory uiFactory = getApplication().getUIFactory();
		JToggleButton navToggle = uiFactory.getMainMenuPanel().getNavToggleButton();
		if (!navToggle.isSelected()) {
			navToggle.doClick();
		}
		selectElement();
		enableSelection(true);
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
			if (currentAssignment != null && currentAssignment instanceof ISearchIf) {
				ISearchIf search = (ISearchIf)currentAssignment;
				list.setSelectedValue(search.getSubType(), false);
			} else {
				list.setSelectedValue(ISearchIf.SearchSubType.LINE, false);
			}
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
		NavBar navBar = getApplication().getNavBar();
		navBar.getFlankToggleButton().setVisible(true);
		navBar.getSplitToggleButton().setVisible(true);
	}
	
	private boolean isContaining(IFeatureClass fc, IGeometry geom) 
			throws AutomationException, IOException {
		boolean flag = false;
		for (int i = 0; i < fc.featureCount(null); i++) {
			Polygon polygon = (Polygon)fc.getFeature(i).getShape();
			if (polygon.contains(geom)) {
				flag = true;
			}
		}
		return flag;
	}
	
	private void showWarning(final String msg) {
		Runnable r = new Runnable(){
            public void run() {
            	JOptionPane.showMessageDialog(getApplication().getFrame(), 
            		msg, Utils.translate(currentElement),
            		JOptionPane.WARNING_MESSAGE);
            }
        };
        SwingUtilities.invokeLater(r);
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
			hideDialogs(null);
			getTextAreaDialog().setText(null);
			
			if (currentElement == IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA) {
				IOperationAreaIf opArea = (IOperationAreaIf) currentMsoFeature.getMsoObject();
				opArea.setRemarks(getTextAreaDialog().getText());
			}
			else if (currentElement == IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA) {
				ISearchAreaIf searchArea = (ISearchAreaIf) currentMsoFeature.getMsoObject();
				IHypothesisIf hypo = getHypothesesDialog().getSelectedHypotheses();
				if (hypo == null) {
					showWarning("M� tilordne en hypotese til s�ksomr�de");
					getHypotheseToggleButton().doClick();
					return;
				}
				searchArea.setSearchAreaHypothesis(hypo);
				searchArea.setPriority(getPriorityDialog().getPriority());
			} 
			else { // search
				ICmdPostIf cmdPost = getMsoManager().getCmdPost();
				if (currentAssignment == null) { // must create new
					IAssignmentListIf assignmentList = cmdPost.getAssignmentList();
					currentAssignment = assignmentList.createSearch();
				}
				IAreaIf area = (IAreaIf) currentMsoFeature.getMsoObject();
				currentAssignment.setPlannedArea(area);
				SearchRequirementDialog dialog = getSearchRequirementDialog();
				currentAssignment.setPriority(dialog.getPriority());
				// moved VW currentAssignment.setStatus(dialog.getStatus());
				currentAssignment.setRemarks(getTextAreaDialog().getText());

				if (currentAssignment instanceof ISearchIf) {
					System.out.println("currentElement: "+currentElement);
					ISearchIf search = (ISearchIf) currentAssignment;
					search.setSubType((SearchSubType) currentElement);
					search.setPlannedAccuracy(dialog.getAccuracy());
					search.setPlannedPersonnel(dialog.getPersonelNeed());
					search.setPlannedProgress(dialog.getEstimatedProgress());
					IUnitIf unit = getUnitSelectionDialog().getSelectedUnit();
					if (unit != null) {
						unit.addUnitAssignment(search, dialog.getStatus()); // modified
						// VW
					} 
					else {
						//currentAssignment.setStatus(dialog.getStatus()); // todo
						// Test????
					}
				}
				currentAssignment.setPriority(dialog.getPriority());
				currentAssignment.setRemarks(getTextAreaDialog().getText());
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
		try {
			currentMsoFeature.setSelected(false);
			hideDialogs(null);
			getElementToggleButton().setEnabled(true);
			// clear fields in dialogs
			getTextAreaDialog().setText(null);
			getHypothesesDialog().reset();
			getSearchRequirementDialog().reset();
			getPriorityDialog().reset();
			poiDialog.reset();
			eraseTool.removeAll();
			
			//select next element
			selectElement();
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
		if (assignment instanceof ISearchIf) {
			ISearchIf search = (ISearchIf) assignment;
			if (makeCopy) {
				// clone() is not implemented
				// assignment = assignment.clone();
			} 
			SearchRequirementDialog reqDialog = getSearchRequirementDialog();
			UnitSelectionDialog unitDialog = getUnitSelectionDialog();
			JList elementList = getElementDialog().getElementList();
			elementList.clearSelection();
			elementList.setSelectedValue(search.getSubType(), false);

			unitDialog.selectedAssignedUnit(search);
			getTextAreaDialog().setText(search.getRemarks());
			reqDialog.setPriority(search.getPriority());
			reqDialog.setStatus(search.getStatus());
			reqDialog.setCriticalQuestions(search.getRemarks());

			reqDialog.setAccuracy(search.getPlannedAccuracy());
			reqDialog.setPersonelNeed(search.getPlannedPersonnel());
			reqDialog.setEstimatedProgress(search.getPlannedProgress());
			try {
				getMap().setSelected(search, true);
				getMap().zoomToMsoObject(search);
			} catch (AutomationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			/*IAreaIf area = search.getPlannedArea();
			if (area != null) {
				try {
					
					IMsoFeatureLayer msoLayer = getMap().getMapManager().getMsoLayer(
							IMsoManagerIf.MsoClassCode.CLASSCODE_AREA);
					IMsoFeatureClass msoFC = (IMsoFeatureClass)msoLayer.getFeatureClass();
					IMsoFeature msoFeature = msoFC.getFeature(area.getObjectId());
					msoFC.setSelected(msoFeature, true);
					
					getMap().setSelected(msoObject, true);
					getMap().zoomToFeature(msoFeature);
				} catch (AutomationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
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
		
		NavBar navBar = getApplication().getNavBar();
		navBar.getFlankToggleButton().setVisible(false);
		navBar.getSplitToggleButton().setVisible(false);
	}

	private void showSearchAreaButtons() {
		showElementListButton();
		getMissionToggleButton().setVisible(false);
		getHypotheseToggleButton().setVisible(true);
		getPriorityToggleButton().setVisible(true);
		getRequirementToggleButton().setVisible(false);
		getDescriptionToggleButton().setVisible(false);
		getUnitToggleButton().setVisible(false);
		
		NavBar navBar = getApplication().getNavBar();
		navBar.getFlankToggleButton().setVisible(false);
		navBar.getSplitToggleButton().setVisible(false);
	}

	private void showSearchButtons() {
		showElementListButton();
		getMissionToggleButton().setVisible(false);
		getHypotheseToggleButton().setVisible(false);
		getPriorityToggleButton().setVisible(false);
		getRequirementToggleButton().setVisible(true);
		getDescriptionToggleButton().setVisible(true);
		getUnitToggleButton().setVisible(true);
		
		NavBar navBar = getApplication().getNavBar();
		navBar.getFlankToggleButton().setVisible(true);
		navBar.getSplitToggleButton().setVisible(true);
	}

	private void showElementListButton() {
		ICmdPostIf cmdPost = getMsoManager().getCmdPost();
		getListToggleButton().setVisible(
				cmdPost.getOperationAreaListItems().size() > 0
				&& cmdPost.getSearchAreaListItems().size() > 0);

	}
	
	private void enableSelection(boolean enable) {
		try {
			OperationAreaFeatureClass opAreaFC  = (OperationAreaFeatureClass)opAreaLayer.getFeatureClass();
			SearchAreaFeatureClass searchAreaFC = (SearchAreaFeatureClass)searchAreaLayer.getFeatureClass();
			AreaFeatureClass areaFC = (AreaFeatureClass)areaLayer.getFeatureClass();
			POIFeatureClass poiFC = (POIFeatureClass)poiLayer.getFeatureClass();

			NavBar navBar = getApplication().getNavBar();
			if (enable) {
				opAreaFC.addDiskoMapEventListener(this);
				searchAreaFC.addDiskoMapEventListener(this);
				areaFC.addDiskoMapEventListener(this);
				poiFC.addDiskoMapEventListener(this);

				selectFeatureTool.addFeatureClass(poiFC);
				selectFeatureTool.addFeatureClass(areaFC);
				selectFeatureTool.addFeatureClass(searchAreaFC);
				selectFeatureTool.addFeatureClass(opAreaFC);
				navBar.getSelectFeatureToggleButton().setEnabled(true);
				navBar.getSelectFeatureToggleButton().doClick();
			} else {
				opAreaFC.removeDiskoMapEventListener(this);
				searchAreaFC.removeDiskoMapEventListener(this);
				areaFC.removeDiskoMapEventListener(this);
				poiFC.removeDiskoMapEventListener(this);
				selectFeatureTool.removeAll();
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
						TextAreaDialog dialog = getTextAreaDialog();
						dialog.setHeaderText("Beskrivelse av oppdrag:");
						hideDialogs(dialog);
						if (missionToggleButton.isSelected() && dialog.isVisible()) {
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
			hideDialogs(null);
			eraseTool.removeAll();
			try {
				JList list = (JList) e.getSource();
				currentElement = (Enum) list.getSelectedValue();
				if (currentElement == null) {
					return;
				}
				ICmdPostIf cmdPost = getMsoManager().getCmdPost();
				IMsoFeatureClass featureClass = null;
				if (currentElement == IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA) {
					showOperationAreaButtons();
					POIType[] poiTypes = { POIType.INTELLIGENCE };
					poiDialog.setTypes(poiTypes);
					featureClass = (IMsoFeatureClass) opAreaLayer.getFeatureClass();
				} 
				else if (currentElement == IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA) {
					if (cmdPost.getOperationAreaListItems().size() == 0) {
						showWarning("M� lage operasjonsomr�de f�rst");
						list.setSelectedValue(IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA, false);
						return;
					} 
					showSearchAreaButtons();
					POIType[] poiTypes = { POIType.INTELLIGENCE };
					poiDialog.setTypes(poiTypes);
					featureClass = (IMsoFeatureClass) searchAreaLayer.getFeatureClass();
				} 
				else {
					if (cmdPost.getOperationAreaListItems().size() == 0) {
						showWarning("M� lage operasjonsomr�de f�rst");
						list.setSelectedValue(IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA, false);
						return;
					} 
					else if (cmdPost.getSearchAreaListItems().size() == 0) {
						showWarning("M� lage s�keomr�de f�rst");
						list.setSelectedValue(IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA, false);
						return;
					} 
					showSearchButtons();
					POIType[] poiTypes = { POIType.START, POIType.VIA,POIType.STOP };
					poiDialog.setTypes(poiTypes);
					featureClass = (IMsoFeatureClass) areaLayer.getFeatureClass();
					flankTool.setFeatureClass(featureClass);
					splitTool.setFeatureClass(featureClass);
				}
				drawTool.setFeatureClass(featureClass);
				IMsoFeatureClass poiFc = (IMsoFeatureClass) poiLayer.getFeatureClass();
				poiTool.setFeatureClass(poiFc);
				eraseTool.addFeatureClass(poiFc);
				eraseTool.addFeatureClass(featureClass);
				setFrameText(Utils.translate(currentElement));
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
