package org.redcross.sar.map;

import java.io.IOException;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.POIDialog;
import com.esri.arcgis.display.IScreenDisplay;
import com.esri.arcgis.display.RgbColor;
import com.esri.arcgis.display.SimpleMarkerSymbol;
import com.esri.arcgis.display.esriScreenCache;
import com.esri.arcgis.carto.IElement;
import com.esri.arcgis.carto.IGraphicsContainer;
import com.esri.arcgis.carto.InvalidArea;
import com.esri.arcgis.carto.MarkerElement;
import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.interop.AutomationException;

/**
 * A custom draw tool.
 * @author geira
 *
 */
public class POITool extends AbstractCommandTool {

	private static final long serialVersionUID = 1L;
	
	private IGraphicsContainer graphics = null;
	private POIDialog poiDialog = null;
	private Point movePoint = null;
	private Envelope refreshEnvelope = null;
	private MarkerElement selectedElement = null;
	private SimpleMarkerSymbol symbol = null;
	private SimpleMarkerSymbol selectionSymbol = null;
		
	/**
	 * Constructs the DrawTool
	 */
	public POITool(IDiskoApplication app) throws IOException {
		dialog = new POIDialog(app, this);
		dialog.setIsToggable(false);
		
		symbol = new SimpleMarkerSymbol();
		RgbColor c = new RgbColor();
		c.setRed(255);
		c.setBlue(255);
		symbol.setColor(c);
		
		selectionSymbol = new SimpleMarkerSymbol();
		c = new RgbColor();
		c.setBlue(255);
		c.setGreen(255);
		selectionSymbol.setColor(c);
	}

	public void onCreate(Object obj) throws IOException, AutomationException {
		if (obj instanceof IDiskoMap) {
			map = (DiskoMap)obj;
			map.addDiskoMapEventListener(this);
			poiDialog = (POIDialog)dialog;
			poiDialog.setLocationRelativeTo(map, DiskoDialog.POS_WEST, false);
			graphics = map.getActiveView().getGraphicsContainer();
		}
	}

	public void onMouseDown(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		if (selectedElement != null) {
			selectedElement.setSymbol(symbol);
			map.partialRefreshGraphics(getEnvelope((Point)selectedElement.getGeometry()));
		}
		Point p = transform(x, y);
		refreshEnvelope = getEnvelope(p);
		IElement elem = map.searchGraphics(p);
		if (elem != null && elem instanceof MarkerElement) {
			selectedElement = (MarkerElement)elem;
		}
		else {
			selectedElement = new MarkerElement();
			PoiProperties props = new PoiProperties();
			props.setType(poiDialog.getSelectedType());
			selectedElement.setCustomProperty(props);
			selectedElement.setName(getElementName());
			graphics.addElement(selectedElement, 0);
		}
		selectedElement.setSymbol(selectionSymbol);
		map.partialRefreshGraphics(refreshEnvelope);
		movePoint = p;
	}	
	
	public void onMouseMove(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		if (movePoint != null) {
			movePoint.setX(x);
			movePoint.setY(y);
			transform(movePoint);
			refreshForeground();
			refreshEnvelope.centerAt(movePoint);
		}
	}

	public void onMouseUp(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		if (movePoint != null) {
			Point p = transform(x, y);
			p.setSpatialReferenceByRef(map.getSpatialReference());
			selectedElement.setGeometry(p);
			movePoint = null;
			refreshEnvelope = null;
			poiDialog.setPOI(selectedElement);
			map.partialRefreshGraphics(null);
		}
	}
	
	public void moveSelectedPOI(double x, double y) throws IOException, AutomationException {
		if (selectedElement != null) {
			Point p = new Point();
			p.setX(x);
			p.setY(y);
			p.setSpatialReferenceByRef(map.getSpatialReference());
			selectedElement.setGeometry(p);
			map.partialRefreshGraphics(null);
		}
	}
	
	public MarkerElement getSelectedPUI() {
		return selectedElement;
	}
	
	public void clearSelectedPOI() throws IOException, AutomationException {	
		if (selectedElement != null) {
			selectedElement.setSymbol(symbol);
			map.partialRefreshGraphics(getEnvelope((Point)selectedElement.getGeometry()));
			selectedElement = null;
		}
	}
	
	public void addPOIAt(double x, double y) throws IOException, AutomationException {
		Point p = new Point();
		p.setX(x);
		p.setY(y);
		p.setSpatialReferenceByRef(map.getSpatialReference());
		selectedElement = new MarkerElement();
		selectedElement.setGeometry(p);
		selectedElement.setSymbol(selectionSymbol);
		selectedElement.setName(getElementName());
		PoiProperties props = new PoiProperties();
		props.setType(poiDialog.getSelectedType());
		selectedElement.setCustomProperty(props);
		graphics.addElement(selectedElement, 0);
		map.partialRefreshGraphics(null);
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
	
	private void draw() throws IOException, AutomationException {	
		if (movePoint != null) {
			IScreenDisplay screenDisplay = map.getActiveView().getScreenDisplay();
			short esriNoScreenCache = (short) esriScreenCache.esriNoScreenCache;
			screenDisplay.startDrawing(0, esriNoScreenCache);
			screenDisplay.setSymbol(selectionSymbol);
			screenDisplay.drawPoint(movePoint);
			screenDisplay.finishDrawing();
		}
	}
	
	private void refreshForeground() throws IOException {
		if (refreshEnvelope != null) {
			InvalidArea invalidArea = new InvalidArea();
			invalidArea.setDisplayByRef(map.getActiveView().getScreenDisplay());
			invalidArea.add(refreshEnvelope);
			invalidArea.invalidate((short) esriScreenCache.esriNoScreenCache);
		}
	}
	
	public void refresh(int arg0) throws IOException, AutomationException {
		draw();
	}
	
	public void onAfterScreenDraw(DiskoMapEvent e) throws IOException {
		refresh(0);
	}
}
