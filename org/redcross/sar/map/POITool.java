package org.redcross.sar.map;

import java.io.IOException;
import java.util.Hashtable;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.PUIDialog;
import com.esri.arcgis.display.IDraw;
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
	private PUIDialog puiDialog = null;
	private Point movePoint = null;
	private Envelope refreshEnvelope = null;
	private MarkerElement selectedElement = null;
	private SimpleMarkerSymbol symbol = null;
	private SimpleMarkerSymbol selectionSymbol = null;
		
	/**
	 * Constructs the DrawTool
	 */
	public POITool(IDiskoApplication app) throws IOException {
		dialog = new PUIDialog(app, this);
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
		if (obj instanceof DiskoMap) {
			map = (DiskoMap)obj;
			map.addDiskoMapEventListener(this);
			puiDialog = (PUIDialog)dialog;
			puiDialog.setLocationRelativeTo(map, DiskoDialog.POS_WEST, false);
			graphics = map.getActiveView().getGraphicsContainer();
		}
	}

	public void onMouseDown(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		if (selectedElement != null) {
			selectedElement.setSymbol(symbol);
			partialRefreshGraphics(getEnvelope((Point)selectedElement.getGeometry()));
		}
		Point p = transform(x, y);
		refreshEnvelope = getEnvelope(p);
		IElement elem = searchGraphics(p);
		if (elem != null && elem instanceof MarkerElement) {
			selectedElement = (MarkerElement)elem;
		}
		else {
			selectedElement = new MarkerElement();
			selectedElement.setCustomProperty(new Hashtable());
			graphics.addElement(selectedElement, 0);
		}
		selectedElement.setSymbol(selectionSymbol);
		partialRefreshGraphics(refreshEnvelope);
		movePoint = p;
	}	
	
	public void onMouseMove(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		if (movePoint != null) {
			movePoint.setX(x);
			movePoint.setY(y);
			transform(movePoint);
			refreshForeground();
			refresh(0);
			refreshEnvelope.centerAt(movePoint);
		}
	}

	public void onMouseUp(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		if (movePoint != null) {
			Point p = transform(x, y);
			selectedElement.setGeometry(p);
			movePoint = null;
			refreshEnvelope = null;
			puiDialog.setPUI(selectedElement);
			partialRefreshGraphics(map.getActiveView().getExtent());
		}
	}
	
	public void moveSelectedPUI(double x, double y) throws IOException, AutomationException {
		if (selectedElement != null) {
			Point p = new Point();
			p.setX(x);
			p.setY(y);
			selectedElement.setGeometry(p);
			partialRefreshGraphics(getEnvelope(p));
		}
	}
	
	public MarkerElement getSelectedPUI() {
		return selectedElement;
	}
	
	public void clearSelectedPUI() throws IOException, AutomationException {	
		if (selectedElement != null) {
			selectedElement.setSymbol(symbol);
			partialRefreshGraphics(getEnvelope((Point)selectedElement.getGeometry()));
			selectedElement = null;
		}
	}
	
	public void addPUIAt(double x, double y) throws IOException, AutomationException {
		Point p = new Point();
		p.setX(x);
		p.setY(y);
		selectedElement = new MarkerElement();
		selectedElement.setGeometry(p);
		selectedElement.setSymbol(selectionSymbol);
		selectedElement.setCustomProperty(new Hashtable());
		graphics.addElement(selectedElement, 0);
		partialRefreshGraphics(map.getActiveView().getExtent());
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
			screenDisplay.startDrawing(0,(short) esriScreenCache.esriNoScreenCache);
			IDraw draw = (IDraw) screenDisplay;
			draw.setSymbol(selectionSymbol);
			draw.draw(movePoint);
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
