package org.redcross.sar.map;

import java.io.IOException;
import java.util.List;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.DrawDialog;
import org.redcross.sar.map.index.IndexedGeometry;

import com.esri.arcgis.carto.InvalidArea;
import com.esri.arcgis.display.IScreenDisplay;
import com.esri.arcgis.display.RgbColor;
import com.esri.arcgis.display.SimpleLineSymbol;
import com.esri.arcgis.display.esriScreenCache;
import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.GeometryBag;
import com.esri.arcgis.geometry.IGeometry;
import com.esri.arcgis.geometry.IPoint;
import com.esri.arcgis.geometry.ISegmentGraphCursor;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.geometry.Polygon;
import com.esri.arcgis.geometry.Polyline;
import com.esri.arcgis.geometry.SegmentGraph;
import com.esri.arcgis.geometry.esriGeometryType;
import com.esri.arcgis.interop.AutomationException;

/**
 * A custom draw tool.
 * @author geira
 *
 */
public class DrawTool extends AbstractCommandTool {

	private static final long serialVersionUID = 1L;
	
	private List snapLayers = null;
	private boolean isActive = false;
	private boolean isDirty = false;
	
	// holds path geometry
	private Polyline pathGeometry = null;
	private Polyline rubberBand = null;
	private Envelope searchEnvelope = null;
	
	private Point p  = null;
	private Point p1 = null;
	private Point p2 = null;
	
	private IndexedGeometry indexedGeometry = null;
	private IGeometry snapGeometry = null;
	private int moveCounter = 0;
	private boolean isMoving = false;
	
	private SimpleLineSymbol drawSymbol = null;	
	private SimpleLineSymbol flashSymbol = null;
	
	/**
	 * Constructs the DrawTool
	 */
	public DrawTool(IDiskoApplication app) throws IOException {
		//symbol to draw with
		drawSymbol = new SimpleLineSymbol();	
		RgbColor drawColor = new RgbColor();
		drawColor.setRed(255);
		drawSymbol.setColor(drawColor);
		drawSymbol.setWidth(1);
			
		flashSymbol = new SimpleLineSymbol();	
		RgbColor flashColor = new RgbColor();
		flashColor.setGreen(255);
		flashColor.setBlue(255);
		flashSymbol.setColor(flashColor);
		flashSymbol.setWidth(2);
			
		// a serach envelope
		searchEnvelope = new Envelope();
		searchEnvelope.putCoords(0, 0, 0, 0);
		searchEnvelope.setHeight(100);
		searchEnvelope.setWidth(100);
		
		p = new Point();
		p.setX(0);
		p.setY(0);
		p1 = new Point();
		p1.setX(0);
		p1.setY(0);
		indexedGeometry = new IndexedGeometry();
		// dialog
		dialog = new DrawDialog(app, this);
		dialog.setIsToggable(false);
	}

	public void onCreate(Object obj) throws IOException, AutomationException {
		if (obj instanceof IDiskoMap) {
			map = (DiskoMap)obj;
			map.addDiskoMapEventListener(this);
			DrawDialog drawDialog = (DrawDialog)dialog;
			drawDialog.onLoad(map);
			drawDialog.setLocationRelativeTo(map, DiskoDialog.POS_WEST, false);
			setSnapableLayers(drawDialog.getSnapModel().getSelected());
			setSnapTolerance(map.getActiveView().getExtent().getWidth()/50);
			drawDialog.setSnapTolerance(getSnapTolerance());
		}
	}
	
	public void setSnapableLayers(List snapLayers) throws IOException {
		if (snapLayers != this.snapLayers) {
			isDirty = true;
			this.snapLayers = snapLayers;
			updateIndexedGeometry();
		}
	}
	
	public void setSnapTolerance(double tolerance) throws IOException {
		searchEnvelope.setHeight(tolerance);
		searchEnvelope.setWidth(tolerance);
	}
	
	public double getSnapTolerance() throws IOException {
		return searchEnvelope.getHeight();
	}
	
	public void onClick() throws IOException, AutomationException {
		isActive = true;
		updateIndexedGeometry();
	}

	public boolean deactivate() throws IOException, AutomationException {
		isActive = false;
		return true;
	}

	public void onDblClick() throws IOException, AutomationException {
		pathGeometry.simplify();
		pathGeometry.setSpatialReferenceByRef(map.getSpatialReference());
		
		if (featureClass.getShapeType() == esriGeometryType.esriGeometryPolyline) {
		}
		else if (featureClass.getShapeType() == esriGeometryType.esriGeometryPolygon) {
			Polygon polygon = getPolygon(pathGeometry);
			polygon.setSpatialReferenceByRef(map.getSpatialReference());
			editFeature.setGeodata(MapUtil.getMsoPolygon(polygon));
		}
		else if (featureClass.getShapeType() == esriGeometryType.esriGeometryBag) {
			editFeature.addGeodataToCollection(MapUtil.getMsoRoute(pathGeometry));
		}
		reset();
		if (editFeedback != null) {
			editFeedback.editFinished(editFeature);
		}
	}
	
	private Polygon getPolygon(Polyline pline) throws IOException, AutomationException {
		// closing
		pline.addPoint(pathGeometry.getFromPoint(), null, null);
		Polygon polygon = new Polygon();
		for (int i = 0; i < pline.getPointCount(); i++) {
			polygon.addPoint(pline.getPoint(i), null, null);
		}
		polygon.simplify();
		return polygon;
	}

	public void onMouseDown(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		if (editFeedback != null) {
			editFeedback.editStarted(editFeature);
		}
		p2 = transform(x,y);
		//try snapping
		searchEnvelope.centerAt(p2);
		Polyline pline = (Polyline)indexedGeometry.search(searchEnvelope);
		if (pline != null) {
			p2 = getNearestPoint(pline, p2);
		}
		if (pathGeometry == null) {
			pathGeometry = new Polyline();
			pathGeometry.addPoint(p2, null, null);
			rubberBand = new Polyline();
			rubberBand.addPoint(p2, null, null);
			rubberBand.addPoint(p2, null, null);
		}
		refreshForegroundPartial();
		updatePath();
		rubberBand.updatePoint(0,p2);
		draw();
		p1 = p2;
	}
	
	public void onMouseMove(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		//Only create the trace every other time the mouse moves
		moveCounter++;
		if(moveCounter < 2) {
		    return;
		}
		moveCounter = 0;
		isMoving = true;
		refreshForegroundPartial();
		p.setX(x);
		p.setY(y);
		transform(p);
		
		searchEnvelope.centerAt(p);
		snapGeometry = indexedGeometry.search(searchEnvelope);
		if (rubberBand != null) {
			rubberBand.updatePoint(1, p);
		}
		draw();
		isMoving = false;
	}

	public void refresh(int arg0) throws IOException, AutomationException {
		draw();
	}

	public void reset() throws IOException {
		pathGeometry = null;
		rubberBand = null;
		snapGeometry = null;
		p1 = new Point();
		p1.setX(0);
		p1.setY(0);
		refreshForegroundPartial();
	}
	
	private void updatePath() throws IOException {
		if (p1.returnDistance(p2) == 0) {
			return;
		}
		searchEnvelope.centerAt(p1);
		Polyline pline1 = (Polyline)indexedGeometry.search(searchEnvelope);
		Polyline pline2 = (Polyline)snapGeometry;
		
		if (pline1 == null || pline2 == null) {
			pathGeometry.addPoint(p2, null, null);
			return;
		}
		// densify rubberband, use vertices as input to segment graph
		Polyline copy = (Polyline)rubberBand.esri_clone();
		copy.densify(getSnapTolerance(), -1);
		// greate a geometry bag to hold selected polylines
		GeometryBag gb = new GeometryBag();
		if (pline1 != null) {
			gb.addGeometry(pline1, null, null);
		}
		if (pline2 != null) {
			gb.addGeometry(pline2, null, null);
		}
		// create the segment graph
		SegmentGraph segmentGraph = new SegmentGraph();
		segmentGraph.load(gb, false, true);
		ISegmentGraphCursor segmentGraphCursor = segmentGraph.getCursor(p1);
		
		// tracing the segmnet graph
		for (int i = 0; i < copy.getPointCount(); i++) {
			IPoint p = copy.getPoint(i);
			if (!segmentGraphCursor.moveTo(p)) {
				segmentGraphCursor.finishMoveTo(p);
			}
		}
		Polyline trace = (Polyline)segmentGraphCursor.getCurrentTrace();
		if (trace != null && trace.getPointCount() > 0) {
			// add tracepoints to path
			for (int i = 0; i < trace.getPointCount(); i++ ) {
				pathGeometry.addPoint(trace.getPoint(i), null, null);
			}
		}
		else {
			pathGeometry.addPoint(p2, null, null);
		}
		// reset
		segmentGraphCursor = null;
	}
	
	private void draw() throws IOException {
		IScreenDisplay screenDisplay = map.getActiveView().getScreenDisplay();
		screenDisplay.startDrawing(screenDisplay.getHDC(),(short) esriScreenCache.esriNoScreenCache);
		
		if (pathGeometry != null && !isMoving) {
			screenDisplay.setSymbol(drawSymbol);
			screenDisplay.drawPolyline(pathGeometry);
		}
		if (rubberBand != null) {
			screenDisplay.setSymbol(drawSymbol);
			screenDisplay.drawPolyline(rubberBand);
		}
		if (snapGeometry != null) {
			screenDisplay.setSymbol(flashSymbol);
			screenDisplay.drawPolyline(snapGeometry);
		}
		screenDisplay.finishDrawing();
	}
	
	private void refreshForegroundPartial() throws IOException {
		InvalidArea invalidArea = new InvalidArea();
		if (pathGeometry != null && !isMoving) {
			invalidArea.add(pathGeometry);
		}
		if (rubberBand != null) {
			invalidArea.add(rubberBand);
		}
		if (snapGeometry != null) {
			invalidArea.add(snapGeometry);
		}
		invalidArea.setDisplayByRef(map.getActiveView().getScreenDisplay());
		//invalidArea.invalidateEx((short) esriScreenCache.esriNoScreenCache,2);
		invalidArea.invalidate((short) esriScreenCache.esriNoScreenCache);
	}
	
	private Point getNearestPoint(Polyline pline, Point point) throws IOException {
		double minDist = Double.MAX_VALUE;
		Point nearestPoint = null;
		for (int i = 0; i < pline.getPointCount(); i++) {
			Point p = (Point)pline.getPoint(i);
			double dist = point.returnDistance(p);
			if (dist < minDist) {
				minDist = dist;
				nearestPoint = p;
			}
		}
		return nearestPoint;
	}
	
	private void updateIndexedGeometry() throws IOException {
		if (!isActive) {
			isDirty = true;
			return;
		}
		if (isDirty) {
			if (indexedGeometry != null) {
				Envelope extent = (Envelope)map.getActiveView().getExtent();
				indexedGeometry.update(extent, snapLayers);
			}
			isDirty = false;
		}
	}

	//***** DiskoMapEventListener implementations *****
	public void onAfterScreenDraw(DiskoMapEvent e) throws IOException {
		refresh(0);
	}

	public void onExtentUpdated(DiskoMapEvent e) throws IOException {
		isDirty = true;
		updateIndexedGeometry();
		
	}

	public void onMapReplaced(DiskoMapEvent e) throws IOException{
		isDirty = true;
		updateIndexedGeometry();
		setSnapTolerance(map.getActiveView().getExtent().getWidth()/50);
		((DrawDialog)dialog).setSnapTolerance(getSnapTolerance());
	}
}
