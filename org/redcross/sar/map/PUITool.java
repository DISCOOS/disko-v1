package org.redcross.sar.map;

import java.io.IOException;
import java.util.ArrayList;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.PUIDialog;

import com.esri.arcgis.display.IDraw;
import com.esri.arcgis.display.IScreenDisplay;
import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.display.esriScreenCache;
import com.esri.arcgis.carto.FeatureLayer;
import com.esri.arcgis.carto.InvalidArea;
import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geodatabase.IFeatureCursor;
import com.esri.arcgis.geodatabase.QueryFilter;
import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.interop.AutomationException;

/**
 * A custom draw tool.
 * @author geira
 *
 */
public class PUITool extends AbstractCommandTool {

	private static final long serialVersionUID = 1L;
	
	private String puiLayerName = null;
	private FeatureLayer puiLayer = null;
	private PUIDialog puiDialog = null;
	private ArrayList<IFeature> features = null;
	private IFeature selectedPUI = null;
	private Point movePoint = null;
	private Envelope refreshEnvelope = null;
	private boolean edit = false;
	
	private static final Short EDIT_CODE_UNCHANGED = new Short((short)0);
	private static final Short EDIT_CODE_ADDED     = new Short((short)1);
	private static final Short EDIT_CODE_DELETED   = new Short((short)2);
	private static final Short EDIT_CODE_CHANGED   = new Short((short)3);
		
	/**
	 * Constructs the DrawTool
	 */
	public PUITool(IDiskoApplication app, String puiLayerName) throws IOException {
		this.puiLayerName = puiLayerName;
		dialog = new PUIDialog(app, this);
		dialog.setIsToggable(false);
		features = new ArrayList<IFeature>();
		movePoint = new Point();
		movePoint.setX(0);
		movePoint.setY(0);
	}

	public void onCreate(Object obj) throws IOException, AutomationException {
		if (obj instanceof DiskoMap) {
			map = (DiskoMap)obj;
			puiLayer = map.getFeatureLayer(puiLayerName);
			map.addDiskoMapEventListener(this);
			puiDialog = (PUIDialog)dialog;
			puiDialog.setLocationRelativeTo(map, DiskoDialog.POS_WEST, false);
			refreshEnvelope = (Envelope)map.getActiveView().getExtent();
		}
	}
	
	public void toolActivated() throws IOException, AutomationException {
		loadPUIs();
		super.toolActivated();
		
	}
	
	public void toolDeactivated() throws IOException, AutomationException {
		finish();
		super.toolDeactivated();
	}
	
	private void loadPUIs() throws IOException, AutomationException {
		System.out.println("loading ....");
		//TODO: velg all PUI i samme aksjon ?
		features.clear();
		QueryFilter filter = new QueryFilter();
		//filter.setWhereClause();
		IFeatureCursor featureCursor = puiLayer.search(filter,false);
		
		IFeature feature = featureCursor.nextFeature();
		while (feature != null) {
			features.add(feature);
			feature.setValue(3, EDIT_CODE_UNCHANGED);
			feature = featureCursor.nextFeature();
		}
		puiLayer.setVisible(false);
		draw();
	}

	public void onMouseDown(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		Point p = transform(x,y);
		Envelope env = getEnvelope(p);
		IFeature pui = search(env);
		if (pui != null) {
			edit = true;
			setSelectedPUI(pui);
		}
		else {
			addPUIAt(p);
		}
	}	
	
	public void onMouseMove(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		if (edit) {
			movePUI(x, y);
		}
	}
	
	private void movePUI(int x, int y) throws IOException, AutomationException {
		Point p = (Point)selectedPUI.getShape();
		refreshEnvelope.centerAt(p);
		refreshForegroundPartial();
		movePoint.setX(x);
		movePoint.setY(y);
		transform(movePoint);
		p.setX(movePoint.getX());
		p.setY(movePoint.getY());
		draw(selectedPUI);
	}
	
	public void moveSelectedPUI(double x, double y) throws IOException, AutomationException {
		Point p = (Point)selectedPUI.getShape();
		refreshEnvelope.centerAt(p);
		p.setX(0);
		p.setY(0);
		partialRefresh(refreshEnvelope);
		p.setX(x);
		p.setY(y);
		draw(selectedPUI);
	}

	public void onMouseUp(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		if (edit) {
			movePUI(x, y);
			puiDialog.setPUI(selectedPUI);
			edit = false;
			selectedPUI.setValue(3, EDIT_CODE_CHANGED);
		}
		refreshEnvelope = (Envelope)map.getActiveView().getExtent();
	}
	
	private IFeature search(Envelope env) throws IOException, AutomationException {
		for (int i = 0; i < features.size(); i++) {
			IFeature pui = (IFeature)features.get(i);
			Point p = (Point)pui.getShape();
			if (!env.disjoint(p)) {
				p.setX(0);
				p.setY(0);
				partialRefresh(env);
				return pui;
			}
		}
		return null;
	}
	
	public IFeature getSelectedPUI() {
		return selectedPUI;
	}
	
	public void clearSelectedPUI() throws IOException, AutomationException {	
		if (selectedPUI != null) {
			refreshEnvelope = getEnvelope((Point)selectedPUI.getShape());
			selectedPUI = null;
			refreshForegroundPartial();
		}
	}
	
	public void deleteSelectedPUI() throws IOException, AutomationException {	
		if (selectedPUI != null) {
			selectedPUI.setValue(3, EDIT_CODE_DELETED);
			clearSelectedPUI();
			puiDialog.clearFields();
		}
	}
	
	private Envelope getEnvelope(Point p) throws IOException, AutomationException {
		Envelope env = new Envelope();
		double size = map.getActiveView().getExtent().getWidth()/50;
		double xmin = p.getX()-size/2;
		double ymin = p.getY()-size/2;
		double xmax = p.getX()+size/2;
		double ymax = p.getY()+size/2;
		env.putCoords(xmin, ymin, xmax, ymax);
		return env;
	}
	
	public void setSelectedPUI(IFeature pui) throws IOException, AutomationException {
		clearSelectedPUI();
		selectedPUI = pui;
		draw(pui);
	}
	
	public void addPUIAt(double x, double y) throws IOException, AutomationException {	
		IFeature pui = puiLayer.getFeatureClass().createFeature();
		Point p = new Point();
		p.setX(x);
		p.setY(y);
		pui.setShapeByRef(p);
		pui.setValue(3, EDIT_CODE_ADDED);
		features.add(pui);
		setSelectedPUI(pui);
	}
	
	private void addPUIAt(Point p) throws IOException, AutomationException {	
		IFeature pui = puiLayer.getFeatureClass().createFeature();
		pui.setShapeByRef(p);
		pui.setValue(3, EDIT_CODE_ADDED);
		features.add(pui);
		puiDialog.setPUI(pui);
		setSelectedPUI(pui);
	}
	
	private void draw() throws IOException, AutomationException {	
		for (int i = 0; i < features.size(); i++) {
			IFeature pui = (IFeature)features.get(i);
			if (!refreshEnvelope.disjoint(pui.getShape())) {
				draw(pui);
			}
		}
	}
	
	private void draw(IFeature pui) throws IOException, AutomationException {
		short editCode = ((Short)pui.getValue(3)).shortValue();
		if (editCode != EDIT_CODE_DELETED.shortValue()) {
			ISymbol symbol = pui == selectedPUI ? puiLayer.getSelectionSymbol() :
				puiLayer.getRenderer().getSymbolByFeature(pui);
			IScreenDisplay screenDisplay = map.getActiveView().getScreenDisplay();
			screenDisplay.startDrawing(0,(short) esriScreenCache.esriNoScreenCache);
			IDraw draw = (IDraw) screenDisplay;
		
			draw.setSymbol(symbol);
			draw.draw(pui.getShape());
			screenDisplay.finishDrawing();
		}
	}
	
	private void refreshForegroundPartial() throws IOException {
		InvalidArea invalidArea = new InvalidArea();
		invalidArea.add(refreshEnvelope);
		invalidArea.setDisplayByRef(map.getActiveView().getScreenDisplay());
		invalidArea.invalidate((short) esriScreenCache.esriNoScreenCache);
	}
	
	public void commit() throws IOException, AutomationException {
		for (int i = 0; i < features.size(); i++) {
			IFeature pui = (IFeature)features.get(i);
			short editCode = ((Short)pui.getValue(3)).shortValue();
			if (editCode == EDIT_CODE_ADDED.shortValue() ||
				editCode == EDIT_CODE_CHANGED.shortValue()) {
				pui.setValue(3, EDIT_CODE_UNCHANGED);
				pui.store();
				System.out.println("Added or changed");			}
			else if (editCode == EDIT_CODE_DELETED.shortValue()) {
				features.remove(pui);
				pui.delete();
				System.out.println("Deleted");	
			}
		}
		System.out.println(" ---- ");	
		clear();
	}
	
	private void finish() throws IOException, AutomationException {
		System.out.println("finish");
		commit();
		features.clear();
		puiLayer.setVisible(true);
		partialRefresh(null);
	}
	
	private void clear() throws IOException, AutomationException {
		clearSelectedPUI();
		refreshEnvelope = (Envelope)map.getActiveView().getExtent();
		partialRefresh(refreshEnvelope);
		puiDialog.clearFields();
	}
	
	public IFeature getLastPUI() {
		if (features.size() == 0) {
			return null;
		}
		return (IFeature)features.get(features.size()-1);
	}
	
	public void refresh(int arg0) throws IOException, AutomationException {
		draw();
	}
	
	public void onAfterScreenDraw(DiskoMapEvent e) throws IOException {
		refresh(0);
	}
}
