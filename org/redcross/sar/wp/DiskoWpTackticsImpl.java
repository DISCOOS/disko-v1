package org.redcross.sar.wp;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JToggleButton;

import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.app.Utils;
import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.ErrorDialog;
import org.redcross.sar.gui.NavBar;
import org.redcross.sar.gui.POIDialog;
import org.redcross.sar.gui.TextAreaDialog;
import org.redcross.sar.map.DiskoMap;
import org.redcross.sar.map.DrawTool;
import org.redcross.sar.map.MapUtil;
import org.redcross.sar.map.POITool;
import org.redcross.sar.map.PoiProperties;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.IOperationAreaIf;
import org.redcross.sar.mso.data.IOperationAreaListIf;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.mso.data.IPOIListIf;
import org.redcross.sar.mso.data.IRouteListIf;
import org.redcross.sar.mso.data.ISearchAreaIf;
import org.redcross.sar.mso.data.ISearchAreaListIf;
import org.redcross.sar.mso.data.IPOIIf.POIType;

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

	private IGraphicsContainer graphics = null;
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
	
	private String currentAction = null;
	private POITool poiTool = null;
	private DrawTool drawTool = null;
	private POIDialog poiDialog = null;
	private TextAreaDialog textAreaDialog = null;

	/**
	 * Constructs a DiskoApTaktikkImpl
	 * @param rolle A reference to the DiskoRolle
	 */
	public DiskoWpTackticsImpl(IDiskoRole rolle) {
		super(rolle);
		initialize();
	}
	
	private void initialize() {
		loadProperties("properties");
		DiskoMap map = (DiskoMap)getMap();
		layoutComponent(map);
		layoutButton(getElementToggleButton(), false);
		layoutButton(getListToggleButton(), true);
		layoutButton(getMissionToggleButton(), true);
		layoutButton(getHypotheseToggleButton(), true);
		layoutButton(getPriorityToggleButton(), true);
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
		graphics = map.getActiveView().getGraphicsContainer();
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
		
		currentAction = "ELEMENT_PATROL_SEARCH";
		drawTool = getApplication().getNavBar().getDrawTool();
		drawTool.setDrawMode(DrawTool.DRAW_MODE_POLYLINE);
		drawTool.setElementName(currentAction);
		
		poiTool = getApplication().getNavBar().getPOITool();
		poiTool.setElementName("POI");
		
		poiDialog = (POIDialog)poiTool.getDialog();
		POIType[] poiTypes = {POIType.START, POIType.VIA, POIType.STOP};
		poiDialog.setTypes(poiTypes);
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
		try {
			hideDialogs();
			graphics.deleteAllElements();
			getMap().partialRefreshGraphics(null);
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
		try {
			hideDialogs();
			getTextAreaDialog().setText(null);
			IEnvelope refreshEnv = new Envelope();
			ICmdPostIf cmdPost = getMsoManager().getCmdPost();
			
			// committing POIs
			IPOIListIf poiList = cmdPost.getPOIList();
			List poiGraphics = getMap().searchGraphics("POI");
			for (int i = 0; i < poiGraphics.size(); i++) {
				MarkerElement marker = (MarkerElement)poiGraphics.get(i);
				PoiProperties properties = (PoiProperties)marker.getCustomProperty();
				Point p = (Point)marker.getGeometry();
				IPOIIf poi = poiList.createPOI(properties.getType(), 
						MapUtil.getMsoPosistion(p));
				poi.setRemarks(properties.getDesrciption());
				refreshEnv.union(p.getEnvelope());
				graphics.deleteElement(marker);
			}
			if (currentAction.equals("ELEMENT_OPERATION_AREA")) {
				IOperationAreaListIf opAreaList = cmdPost.getOperationAreaList();
				List opAreaGraphics = getMap().searchGraphics(currentAction);
				if (opAreaGraphics.size() == 1) {
					PolygonElement pe = (PolygonElement)opAreaGraphics.get(0);
					Polygon polygon = (Polygon)pe.getGeometry();
					IOperationAreaIf opArea = opAreaList.createOperationArea();
					org.redcross.sar.util.mso.Polygon msoPolygon = 
						MapUtil.getMsoPolygon(polygon);
					opArea.setGeodata(msoPolygon);
					opArea.setRemarks(getTextAreaDialog().getText());
					refreshEnv.union(polygon.getEnvelope());
					graphics.deleteElement(pe);
					getMsoModel().commit();
				}
				else if (opAreaGraphics.size() == 0) {
					showError("Det er ikke tegnet operasjonsområde i kartet", null);
				}
				else {
					showError("Det er tegnet flere enn ett operasjonsområde i kartet", null);
				}
			}
			else if (currentAction.equals("ELEMENT_SEARCH_AREA")) {
				ISearchAreaListIf searchAreaList = cmdPost.getSearchAreaList();
				List searchAreaGraphics = getMap().searchGraphics(currentAction);
				for (int i = 0; i < searchAreaGraphics.size(); i++) {
					PolygonElement pe = (PolygonElement)searchAreaGraphics.get(i);
					Polygon polygon = (Polygon)pe.getGeometry();
					ISearchAreaIf searchArea = searchAreaList.createSearchArea();
					org.redcross.sar.util.mso.Polygon msoPolygon = 
						MapUtil.getMsoPolygon(polygon);
					searchArea.setGeodata(msoPolygon);
					refreshEnv.union(polygon.getEnvelope());
					graphics.deleteElement(pe);
					getMsoModel().commit();
				}
			}
			else { // patrol search
				IRouteListIf routeList = cmdPost.getRouteList();
				List routeGraphics = getMap().searchGraphics(currentAction);
				for (int i = 0; i < routeGraphics.size(); i++) {
					LineElement le = (LineElement)routeGraphics.get(i);
					Polyline polyline = (Polyline)le.getGeometry();
					routeList.createRoute(MapUtil.getMsoRoute(polyline));
					refreshEnv.union(polyline.getEnvelope());
					graphics.deleteElement(le);
					getMsoModel().commit();
				}
			}
			getMsoModel().commit();
			getMap().partialRefreshGraphics(refreshEnv);
		} catch (AutomationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void showError(String msg, String description) {
		ErrorDialog dialog = getApplication().getUIFactory().getErrorDialog();
		dialog.showError(msg, description);
		dialog.setLocationRelativeTo((JComponent)getMap());
	}
	
	private void hideDialogs() {
		getElementDialog().setVisible(false);
		getElementToggleButton().setSelected(false);
		getTextAreaDialog().setVisible(false);
	}
	
	private ElementDialog getElementDialog() {
		if (elementDialog == null) {
			elementDialog = new ElementDialog(getApplication().getFrame(), new ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					hideDialogs();
					getDummyToggleButton().doClick(); // HACK: unselect all toggle buttons
					getElementDialog().setVisible(false);
					getElementToggleButton().setSelected(false);
					String command = e.getActionCommand();
					currentAction = command;
					drawTool.setElementName(currentAction);
					if (command.equals("ELEMENT_OPERATION_AREA")) {
						showOperationAreaButtons();
						drawTool.setDrawMode(DrawTool.DRAW_MODE_POLYGON);
						POIType[] poiTypes = {POIType.INTELLIGENCE};
						poiDialog.setTypes(poiTypes);
					}
					else if (command.equals("ELEMENT_SEARCH_AREA")) {
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
		}
		return elementDialog;
	}
	
	public TextAreaDialog getTextAreaDialog() {
		if (textAreaDialog == null) {
			textAreaDialog = new TextAreaDialog(getApplication().getFrame());
			textAreaDialog.setIsToggable(false);
			textAreaDialog.setHeaderText("Aksjonens oppdrag:");
		}
		return textAreaDialog;
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
						if (elementToggleButton.isSelected()) {
							java.awt.Point p = elementToggleButton.getLocationOnScreen();
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
				Dimension size = getApplication().getUIFactory().getLargeButtonSize();
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
				Dimension size = getApplication().getUIFactory().getLargeButtonSize();
				String iconName = "hypothese.icon";
				Icon icon = Utils.createImageIcon(getApplication().getProperty(iconName),iconName);
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
				Dimension size = getApplication().getUIFactory().getLargeButtonSize();
				String iconName = "list.icon";
				Icon icon = Utils.createImageIcon(getApplication().getProperty(iconName),iconName);
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
				Dimension size = getApplication().getUIFactory().getLargeButtonSize();
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
				Dimension size = getApplication().getUIFactory().getLargeButtonSize();
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
