package org.redcross.sar.wp.tacktics;

import java.awt.Dimension;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JToggleButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.app.Utils;
import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.NavBar;
import org.redcross.sar.gui.POIDialog;
import org.redcross.sar.gui.TextAreaDialog;
import org.redcross.sar.map.DiskoMap;
import org.redcross.sar.map.DrawTool;
import org.redcross.sar.map.IDiskoMap;
import org.redcross.sar.map.MapUtil;
import org.redcross.sar.map.POITool;
import org.redcross.sar.map.PoiProperties;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.AssignmentImpl;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.mso.data.IAreaListIf;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IAssignmentListIf;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.IOperationAreaIf;
import org.redcross.sar.mso.data.IOperationAreaListIf;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.mso.data.IPOIListIf;
import org.redcross.sar.mso.data.ISearchAreaIf;
import org.redcross.sar.mso.data.ISearchAreaListIf;
import org.redcross.sar.mso.data.ISearchIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IPOIIf.POIType;
import org.redcross.sar.mso.data.ISearchIf.SearchSubType;
import org.redcross.sar.util.except.IllegalOperationException;
import org.redcross.sar.util.mso.GeoCollection;
import org.redcross.sar.wp.AbstractDiskoWpModule;

import com.esri.arcgis.carto.IElement;
import com.esri.arcgis.carto.IGraphicsContainer;
import com.esri.arcgis.carto.LineElement;
import com.esri.arcgis.carto.MarkerElement;
import com.esri.arcgis.carto.PolygonElement;
import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.IEnvelope;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.geometry.Polygon;
import com.esri.arcgis.geometry.Polyline;
import com.esri.arcgis.interop.AutomationException;

/**
 * Implements the DiskoApTaktikk interface
 * @author geira
 *
 */
public class DiskoWpTackticsImpl extends AbstractDiskoWpModule implements IDiskoWpTacktics {

	private IGraphicsContainer graphicsContainer = null;
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
	private POIDialog poiDialog = null;
	private ArrayList dialogs = null;
	private TextAreaDialog textAreaDialog = null;
	private HypothesesDialog hypothesesDialog = null;
	private PriorityDialog priorityDialog = null;
	private UnitSelectionDialog unitSelectionDialog = null;
	private SearchRequirementDialog searchRequirementDialog = null;
	private ListDialog listDialog = null;

	/**
	 * Constructs a DiskoApTaktikkImpl
	 * @param rolle A reference to the DiskoRolle
	 */
	public DiskoWpTackticsImpl(IDiskoRole rolle) {
		super(rolle);
		dialogs = new ArrayList();
		initialize();
	}
	
	private void initialize() {
		loadProperties("properties");
		DiskoMap map = (DiskoMap)getMap();
		layoutComponent(map);
		layoutButton(getElementToggleButton(), true);
		layoutButton(getListToggleButton(), true);
		layoutButton(getMissionToggleButton(), true);
		layoutButton(getPriorityToggleButton(), true);
		layoutButton(getHypotheseToggleButton(), true);
		layoutButton(getRequirementToggleButton(), true);
		layoutButton(getDescriptionToggleButton(), true);
		layoutButton(getUnitToggleButton(), true);
		
		//Add a not visible dummy JToggleButton, used to unselect all
		//(visbible) JToggleButtons. This is a hack suggested by Java dev forum
		layoutButton(getDummyToggleButton(), true);
	}
	
	public void onMapReplaced(DiskoMapEvent e) throws IOException {
		DiskoMap map = (DiskoMap)getMap();
		map.setName(getName()+"Map");	
		graphicsContainer = map.getActiveView().getGraphicsContainer();
	}
	
	public void activated() {
		NavBar navBar = getApplication().getNavBar();
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
		
		currentAction = SearchSubType.PATROL;
		drawTool = getApplication().getNavBar().getDrawTool();
		drawTool.setDrawMode(DrawTool.DRAW_MODE_POLYLINE);
		drawTool.setElementName(currentAction.name());
		
		poiTool = getApplication().getNavBar().getPOITool();
		poiTool.setElementName(currentAction.name());
		
		poiDialog = (POIDialog)poiTool.getDialog();
		POIType[] poiTypes = {POIType.START, POIType.VIA, POIType.STOP};
		poiDialog.setTypes(poiTypes);
	}
	
	public void deactivated() {
		hideDialogs(null);
		getDummyToggleButton().doClick();
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
		try {
			hideDialogs(null);
			getDummyToggleButton().doClick();
			graphicsContainer.deleteAllElements();
			getMap().partialRefreshGraphics(null);
			getMsoModel().rollback();
		} catch (AutomationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.geodata.engine.disko.task.DiskoAp#finish()
	 */
	public void finish() {
		List graphics = null;
		try {
			hideDialogs(null);
			getTextAreaDialog().setText(null);
			ICmdPostIf cmdPost = getMsoManager().getCmdPost();
			graphics = getMap().searchGraphics(currentAction.name());
			
			if (currentAction == IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA) {
				IOperationAreaListIf opAreaList = cmdPost.getOperationAreaList();
				for (int i = 0; i < graphics.size(); i++) {
					IElement elem = (IElement)graphics.get(i);
					if (elem instanceof PolygonElement) {
						PolygonElement pe = (PolygonElement)elem;
						Polygon polygon = (Polygon)pe.getGeometry();
						IOperationAreaIf opArea = opAreaList.createOperationArea();
						org.redcross.sar.util.mso.Polygon msoPolygon = MapUtil.getMsoPolygon(polygon);
						opArea.setGeodata(msoPolygon);
						opArea.setRemarks(getTextAreaDialog().getText());
						getTextAreaDialog().setText(null);
					}
					else if (elem instanceof MarkerElement) { // POI
						createPOI((MarkerElement)elem);
					}
				}
			}
			else if (currentAction == IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA) {
				ISearchAreaListIf searchAreaList = cmdPost.getSearchAreaList();
				for (int i = 0; i < graphics.size(); i++) {
					IElement elem = (IElement)graphics.get(i);
					if (elem instanceof PolygonElement) {
						PolygonElement pe = (PolygonElement)elem;
						Polygon polygon = (Polygon)pe.getGeometry();
						ISearchAreaIf searchArea = searchAreaList.createSearchArea();
						org.redcross.sar.util.mso.Polygon msoPolygon = MapUtil.getMsoPolygon(polygon);
						searchArea.setGeodata(msoPolygon);
						searchArea.setSearchAreaHypothesis(getHypothesesDialog().getSelectedHypotheses());
						((AssignmentImpl)searchArea).setPriority(getPriorityDialog().getPriority());
					}
					else if (elem instanceof MarkerElement) { // POI
						createPOI((MarkerElement)elem);
					}
				}
			}
			else { // search
				int size = graphics.size();
				IAssignmentListIf assignmentList = cmdPost.getAssignmentList();
				ISearchIf search = assignmentList.createSearch();
				search.setSubType((SearchSubType)currentAction);
				IAreaListIf areaList = cmdPost.getAreaList();
				IAreaIf area = areaList.createArea();
				GeoCollection geoCollection = new GeoCollection(null, null, size);
				
				for (int i = 0; i < size; i++) {
					IElement elem = (IElement)graphics.get(i);
					if (elem instanceof LineElement) {
						Polyline polyline = (Polyline)elem.getGeometry();
						geoCollection.add(MapUtil.getMsoRoute(polyline));
					}
					else if (elem instanceof PolygonElement) {
						Polygon polygon = (Polygon)elem.getGeometry();
						geoCollection.add(MapUtil.getMsoPolygon(polygon));
					}
					else if (elem instanceof MarkerElement) { // POI
						area.addAreaPOI(createPOI((MarkerElement)elem));
					}
				}
				area.setGeodata(geoCollection);
				search.setPlannedArea(area);
				SearchRequirementDialog dialog = getSearchRequirementDialog();
				search.setPlannedAccuracy(dialog.getAccuracy());
				search.setPriority(dialog.getPriority());
				search.setPlannedPersonnel(dialog.getPersonelNeed());
				search.setPlannedProgress(dialog.getEstimatedProgress());
				search.setStatus(dialog.getStatus());
				search.setRemarks(getTextAreaDialog().getText());
				
				IUnitIf unit = getUnitSelectionDialog().getSelectedUnit();
				if (unit != null) {
					unit.addUnitAssignment(search);
				}
				getTextAreaDialog().setText(null);
			}
		} catch (AutomationException e) {
			getMsoModel().rollback();
			e.printStackTrace();
		} catch (IOException e) {
			getMsoModel().rollback();
			e.printStackTrace();
		} catch (IllegalOperationException e) {
			getMsoModel().rollback();
			e.printStackTrace();
		} finally {
			// commit changes and clear graphics
			clearGraphics(graphics);
			getMsoModel().commit();
		}
	}
	
	private void clearGraphics(List graphics) {
		if (graphics == null) {
			return;
		}
		try {
			IEnvelope refreshEnv = new Envelope();
			refreshEnv.putCoords(0, 0, 0, 0);
			for (int i = 0; i < graphics.size(); i++) {
				IElement elem = (IElement)graphics.get(i);
				refreshEnv.union(elem.getGeometry().getEnvelope());
				graphicsContainer.deleteElement(elem);
			}
			//refreshing the map
			getMap().partialRefreshGraphics(refreshEnv);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AutomationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void startEdit(IAssignmentIf assignment, boolean makeCopy) {
		try {
			IDiskoMap map = getMap();
			IAreaIf area = assignment.getPlannedArea();
			if (area != null) {
				String layerName = getProperty("BasicLine.featureClass.Name");
				String msoid = area.getObjectId();
				map.setSelected(layerName, "MSOID", msoid);
				map.zoomToSelected();
			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AutomationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private IPOIIf createPOI(MarkerElement marker) 
			throws AutomationException, IOException {
		ICmdPostIf cmdPost = getMsoManager().getCmdPost();
		IPOIListIf poiList = cmdPost.getPOIList();
		PoiProperties properties = (PoiProperties)marker.getCustomProperty();
		Point p = (Point)marker.getGeometry();
		IPOIIf poi = poiList.createPOI(properties.getType(), 
				MapUtil.getMsoPosistion(p));
		poi.setRemarks(properties.getDesrciption());
		return poi;
	}
	
	private void hideDialogs(JDialog notToHideDialog) {
		for (int i = 0; i < dialogs.size(); i++) {
			JDialog dialog = (JDialog)dialogs.get(i);
			if (dialog != notToHideDialog) {
				dialog.setVisible(false);
			}
		}
	}
	
	private ElementDialog getElementDialog() {
		if (elementDialog == null) {
			elementDialog = new ElementDialog(getApplication().getFrame(), new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					hideDialogs(null);
					getDummyToggleButton().doClick(); // HACK: unselect all toggle buttons
					getElementDialog().setVisible(false);
					getElementToggleButton().setSelected(false);
					
					JList list = (JList)e.getSource();
					currentAction = (Enum)list.getSelectedValue();
					drawTool.setElementName(currentAction.name());
					poiTool.setElementName(currentAction.name());
					if (currentAction == IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA) {
						showOperationAreaButtons();
						drawTool.setDrawMode(DrawTool.DRAW_MODE_POLYGON);
						POIType[] poiTypes = {POIType.INTELLIGENCE};
						poiDialog.setTypes(poiTypes);
					}
					else if (currentAction == IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA) {
						showSearchAreaButtons();
						drawTool.setDrawMode(DrawTool.DRAW_MODE_POLYGON);
						POIType[] poiTypes = {POIType.INTELLIGENCE};
						poiDialog.setTypes(poiTypes);
					}
					else {
						showSearchButtons();
						drawTool.setDrawMode(DrawTool.DRAW_MODE_POLYLINE);
						POIType[] poiTypes = {POIType.START, POIType.VIA, POIType.STOP};
						poiDialog.setTypes(poiTypes);
					}
				}
			});
			elementDialog.setIsToggable(false);
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
			searchRequirementDialog = new SearchRequirementDialog(getApplication());
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
				Dimension size = getApplication().getUIFactory().getLargeButtonSize();
				//String iconName = "MapZoomInTool.icon";
				//Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				elementToggleButton = new JToggleButton();
				elementToggleButton.setText("ELEMENT");
				//elementToggleButton.setIcon(icon);
				elementToggleButton.setPreferredSize(size);
				elementToggleButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						ElementDialog dialog = getElementDialog();
						hideDialogs(dialog);
						if (elementToggleButton.isSelected() && dialog.isVisible()) {
							dialog.setVisible(false);
						}
						else {
							java.awt.Point p = elementToggleButton.getLocationOnScreen();
							p.setLocation(p.x - dialog.getWidth()-2, p.y);
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
				Dimension size = getApplication().getUIFactory().getLargeButtonSize();
				//String iconName = "MapZoomInTool.icon";
				//Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				descriptionToggleButton = new JToggleButton();
				descriptionToggleButton.setText("BESKRIVELSE");
				//descriptionToggleButton.setIcon(icon);
				descriptionToggleButton.setPreferredSize(size);
				descriptionToggleButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						TextAreaDialog dialog = getTextAreaDialog();
						dialog.setHeaderText("Beskrivelse av oppdrag:");
						hideDialogs(dialog);
						if (missionToggleButton.isSelected() && dialog.isVisible()) {
							dialog.setVisible(false);
						}
						else {
							dialog.setLocationRelativeTo((JComponent)getMap(), 
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
				Dimension size = getApplication().getUIFactory().getLargeButtonSize();
				String iconName = "hypothese.icon";
				Icon icon = Utils.createImageIcon(getApplication().getProperty(iconName),iconName);
				hypotheseToggleButton = new JToggleButton();
				hypotheseToggleButton.setIcon(icon);
				hypotheseToggleButton.setPreferredSize(size);
				hypotheseToggleButton.setVisible(false);
				hypotheseToggleButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						HypothesesDialog dialog = getHypothesesDialog();
						hideDialogs(dialog);
						JComponent mapComp = (JComponent)getMap();
						if (hypotheseToggleButton.isSelected() & dialog.isVisible()) {
							dialog.setVisible(false);
						}
						else {
							dialog.setLocationRelativeTo(mapComp, DiskoDialog.POS_SOUTH, true);
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
				Dimension size = getApplication().getUIFactory().getLargeButtonSize();
				String iconName = "list.icon";
				Icon icon = Utils.createImageIcon(getApplication().getProperty(iconName),iconName);
				listToggleButton = new JToggleButton();
				listToggleButton.setIcon(icon);
				listToggleButton.setPreferredSize(size);
				listToggleButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						ListDialog dialog = getListDialog();
						hideDialogs(dialog);
						JComponent mapComp = (JComponent)getMap();
						if (listToggleButton.isSelected() & dialog.isVisible()) {
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
				Dimension size = getApplication().getUIFactory().getLargeButtonSize();
				String iconName = "mission.icon";
				Icon icon = Utils.createImageIcon(getApplication().getProperty(iconName),iconName);
				missionToggleButton = new JToggleButton();
				missionToggleButton.setIcon(icon);
				missionToggleButton.setPreferredSize(size);
				missionToggleButton.setVisible(false);
				missionToggleButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						TextAreaDialog dialog = getTextAreaDialog();
						dialog.setHeaderText("Aksjonens oppdrag:");
						hideDialogs(dialog);
						if (missionToggleButton.isSelected() && dialog.isVisible()) {
							dialog.setVisible(false);
						}
						else {
							dialog.setLocationRelativeTo((JComponent)getMap(), 
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
				Dimension size = getApplication().getUIFactory().getLargeButtonSize();
				//String iconName = "MapZoomInTool.icon";
				//Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				priorityToggleButton = new JToggleButton();
				priorityToggleButton.setText("PRIORITET");
				//priorityToggleButton.setIcon(icon);
				priorityToggleButton.setPreferredSize(size);
				priorityToggleButton.setVisible(false);
				priorityToggleButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						PriorityDialog dialog = getPriorityDialog();
						hideDialogs(dialog);
						if (priorityToggleButton.isSelected() && dialog.isVisible()) {
							dialog.setVisible(false);
						}
						else {
							java.awt.Point p = priorityToggleButton.getLocationOnScreen();
							p.setLocation(p.x - dialog.getWidth()-2, p.y);
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
				Dimension size = getApplication().getUIFactory().getLargeButtonSize();
				//String iconName = "MapZoomInTool.icon";
				//Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				requirementToggleButton = new JToggleButton();
				requirementToggleButton.setText("KRAV");
				//requirementToggleButton.setIcon(icon);
				requirementToggleButton.setPreferredSize(size);
				requirementToggleButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						SearchRequirementDialog dialog = getSearchRequirementDialog();
						hideDialogs(dialog);
						if (requirementToggleButton.isSelected() && dialog.isVisible()) {
							dialog.setVisible(false);
						}
						else {
							dialog.setLocationRelativeTo((JComponent)getMap(), 
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

	private JToggleButton getUnitToggleButton() {
		if (unitToggleButton == null) {
			try {
				Dimension size = getApplication().getUIFactory().getLargeButtonSize();
				//String iconName = "MapZoomInTool.icon";
				//Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				unitToggleButton = new JToggleButton();
				unitToggleButton.setText("ENHET");
				//unitToggleButton.setIcon(icon);
				unitToggleButton.setPreferredSize(size);
				unitToggleButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						UnitSelectionDialog dialog = getUnitSelectionDialog();
						hideDialogs(dialog);
						if (unitToggleButton.isSelected() && dialog.isVisible()) {
							dialog.setVisible(false);
						}
						else {
							java.awt.Point p = unitToggleButton.getLocationOnScreen();
							p.setLocation(p.x - dialog.getWidth()-2, p.y);
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
