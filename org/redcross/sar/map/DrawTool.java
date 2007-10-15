package org.redcross.sar.map;

import com.esri.arcgis.carto.*;
import com.esri.arcgis.controls.IMapControlEvents2Adapter;
import com.esri.arcgis.controls.IMapControlEvents2OnAfterScreenDrawEvent;
import com.esri.arcgis.controls.IMapControlEvents2OnExtentUpdatedEvent;
import com.esri.arcgis.controls.IMapControlEvents2OnMapReplacedEvent;
import com.esri.arcgis.display.IScreenDisplay;
import com.esri.arcgis.display.RgbColor;
import com.esri.arcgis.display.SimpleLineSymbol;
import com.esri.arcgis.display.esriScreenCache;
import com.esri.arcgis.geometry.*;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.geometry.Polygon;
import com.esri.arcgis.interop.AutomationException;
import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.DrawDialog;
import org.redcross.sar.gui.FreeHandDialog;
import org.redcross.sar.map.index.IndexedGeometry;
import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.map.layer.OperationAreaLayer;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A custom draw tool.
 * @author geira
 *
 */
public class DrawTool extends AbstractCommandTool {

	private static final long serialVersionUID = 1L;

	private boolean isActive = false;
	private boolean isDirty = false;
	private IDiskoApplication app = null;

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
	private static final int SNAP_TOL_FACTOR = 200;
	private IAreaIf area = null;
	private IMsoManagerIf.MsoClassCode msoClassCode = null;

	/**
	 * Constructs the DrawTool
	 */
	public DrawTool(IDiskoApplication app) throws IOException {
		this.app = app;
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
			DrawDialog drawDialog = (DrawDialog)dialog;
			drawDialog.onLoad(map);
			drawDialog.setLocationRelativeTo(map, DiskoDialog.POS_WEST, true);
			setSnapTolerance(map.getActiveView().getExtent().getWidth()/SNAP_TOL_FACTOR);
			drawDialog.setSnapTolerance(getSnapTolerance());
			updateSnapLayers();
			map.addIMapControlEvents2Listener(new MapControlAdapter());

			// getting operation areas
			opAreaLayer = (OperationAreaLayer) map.getMsoLayer(
					IMsoFeatureLayer.LayerCode.OPERATION_AREA_LAYER);
		}
	}

	public void setSnapTolerance(double tolerance) throws IOException, AutomationException {
		searchEnvelope.setHeight(tolerance);
		searchEnvelope.setWidth(tolerance);
	}

	public double getSnapTolerance() throws IOException {
		return searchEnvelope.getHeight();
	}

	public void setMsoClassCode(IMsoManagerIf.MsoClassCode msoClassCode) {
		this.msoClassCode = msoClassCode;
	}

	public void setArea(IAreaIf area) {
		this.area = area;
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
		Polyline polyline = pathGeometry;
		polyline.setSpatialReferenceByRef(map.getSpatialReference());
		/*
		// get snappeable layers
		ArrayList<IFeatureLayer> snapTo =((DrawDialog)dialog).getSnapableLayers();
		// snapping?
		if (snapTo.size()>0) {
			ArrayList<IIdentify> l = new ArrayList<IIdentify>(snapTo.size()); 
			for (int i=0;i<snapTo.size();i++) {
				l.add((IIdentify)snapTo.get(i));
			}
			MapUtil.snapPolyLineTo(polyline,l,50);
		}*/
		// get command post
		ICmdPostIf cmdPost = app.getMsoModel().getMsoManager().getCmdPost();
		IMsoObjectIf msoObj = null;

		if (msoClassCode == IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA) {
			Polygon polygon = getPolygon(polyline);
			polygon.setSpatialReferenceByRef(map.getSpatialReference());
			IOperationAreaListIf opAreaList = cmdPost.getOperationAreaList();
			IOperationAreaIf opArea = opAreaList.createOperationArea();
			opArea.setGeodata(MapUtil.getMsoPolygon(polygon));
			msoObj = opArea;
		}
		else if (msoClassCode == IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA) {
			Polygon polygon = getPolygon(polyline);
			polygon.setSpatialReferenceByRef(map.getSpatialReference());
			ISearchAreaListIf searchAreaList = cmdPost.getSearchAreaList();
			ISearchAreaIf searchArea = searchAreaList.createSearchArea();
			searchArea.setGeodata(MapUtil.getMsoPolygon(polygon));
			msoObj = searchArea;
		}
		else if (msoClassCode == IMsoManagerIf.MsoClassCode.CLASSCODE_AREA) {
			map.setSupressDrawing(true);
			if (area == null) {
				IAreaListIf areaList = cmdPost.getAreaList();
				area = areaList.createArea();
			}
            IRouteIf route = cmdPost.getRouteList().createRoute(MapUtil.getMsoRoute(polyline));
            area.addAreaGeodata(route);
			msoObj = area;
			map.setSupressDrawing(false);
			map.refreshLayer(map.getMsoLayer(IMsoFeatureLayer.LayerCode.AREA_LAYER), null);
		}
		map.setSelected(msoObj, true);
		reset();
	}

	private Polygon getPolygon(Polyline pline) throws IOException, AutomationException {
		// closing
		pline.addPoint(pline.getFromPoint(), null, null);
		Polygon polygon = new Polygon();
		for (int i = 0; i < pline.getPointCount(); i++) {
			polygon.addPoint(pline.getPoint(i), null, null);
		}
		polygon.simplify();
		return polygon;
	}

	public void onMouseDown(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		p2 = transform(x,y);
		// check if point inside operation area
		if (msoClassCode != IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA &&
				!insideOpArea(p2)) {
			Toolkit.getDefaultToolkit().beep();
			return;
		}
		//try snapping
		searchEnvelope.centerAt(p2);
		Polyline pline = (Polyline)indexedGeometry.search(searchEnvelope);
		if (pline != null) {
			pline.densify(getSnapTolerance()/10, -1);
			pline.generalize(1);
			p2 = getNearestPoint(pline, p2);
		}
		if (pathGeometry == null) {
			pathGeometry = new Polyline();
			pathGeometry.addPoint(p2, null, null);
			rubberBand = new Polyline();
			rubberBand.addPoint(p2, null, null);
			rubberBand.addPoint(p2, null, null);
		}
		refresh();
		updatePath();
		rubberBand.updatePoint(0,p2);
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
		refresh();
		p.setX(x);
		p.setY(y);
		transform(p);
		searchEnvelope.centerAt(p);
		snapGeometry = indexedGeometry.search(searchEnvelope);
		if (rubberBand != null) {
			rubberBand.updatePoint(1, p);
		}
		isMoving = false;
	}

	public void reset() throws IOException {
		refresh();
		pathGeometry = null;
		rubberBand = null;
		snapGeometry = null;
		p1 = new Point();
		p1.setX(0);
		p1.setY(0);
	}

	private void updatePath() throws IOException, AutomationException {
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
		copy.densify(getSnapTolerance()/10, -1);
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

	private void draw() throws IOException, AutomationException {
		
		if (isActive) {
			IScreenDisplay screenDisplay = map.getActiveView().getScreenDisplay();
			screenDisplay.startDrawing(screenDisplay.getHDC(),(short) esriScreenCache.esriAllScreenCaches);
	
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
	}

	private void refresh() throws IOException, AutomationException {
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
		invalidArea.invalidate((short) esriScreenCache.esriNoScreenCache);
	}

	private Point getNearestPoint(Polyline pline, Point point)
			throws IOException, AutomationException {
		//double minDist = Double.MAX_VALUE;
		IPoint nearestPoint = pline.returnNearestPoint(point, esriSegmentExtension.esriNoExtension);
		/*for (int i = 0; i < pline.getPointCount(); i++) {
			Point p = (Point)pline.getPoint(i);
			double dist = point.returnDistance(p);
			if (dist < minDist) {
				minDist = dist;
				nearestPoint = p;
			}
		}*/
		return (Point)nearestPoint;
	}

	public void setIsDirty() {
		isDirty = true;
		try {
			updateIndexedGeometry();
		} catch (AutomationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void updateIndexedGeometry() throws IOException, AutomationException {
		if (!isActive) {
			isDirty = true;
			return;
		}
		if (isDirty) {
			if (indexedGeometry != null) {
				updateSnapLayers();
				Envelope extent = (Envelope)map.getActiveView().getExtent();
				DrawDialog drawDialog = (DrawDialog)dialog;
				indexedGeometry.update(extent, drawDialog.getSnapableLayers());
			}
			isDirty = false;
		}
	}

	private void updateSnapLayers() throws IOException, AutomationException {
		ArrayList<IFeatureLayer> layers = new ArrayList<IFeatureLayer>();
		IMap focusMap = map.getActiveView().getFocusMap();
		for (int i = 0; i < focusMap.getLayerCount(); i++) {
			ILayer layer = focusMap.getLayer(i);
			if (layer instanceof IFeatureLayer) {
				IFeatureLayer flayer = (IFeatureLayer) layer;
				int shapeType = flayer.getFeatureClass().getShapeType();
				if (shapeType == com.esri.arcgis.geometry.esriGeometryType.esriGeometryPolyline ||
						shapeType == com.esri.arcgis.geometry.esriGeometryType.esriGeometryPolygon  ||
						shapeType == com.esri.arcgis.geometry.esriGeometryType.esriGeometryBag) {
					double scale = map.getScale((IBasicMap)focusMap);
					if (flayer.isVisible() && scale > flayer.getMaximumScale() &&
							scale < flayer.getMinimumScale()) {
						layers.add(flayer);
					}
				}
			}
		}
		DrawDialog drawDialog = (DrawDialog)dialog;
		drawDialog.updateLayerSelection(layers);
		setSnapTolerance(map.getActiveView().getExtent().getWidth()/SNAP_TOL_FACTOR);
		drawDialog.setSnapTolerance(getSnapTolerance());
	}

	class MapControlAdapter extends IMapControlEvents2Adapter {

		private static final long serialVersionUID = 1L;

		@Override
		public void onAfterScreenDraw(IMapControlEvents2OnAfterScreenDrawEvent arg0)
				throws IOException, AutomationException {
			draw();
		}

		@Override
		public void onExtentUpdated(IMapControlEvents2OnExtentUpdatedEvent arg0)
				throws IOException, AutomationException {
			isDirty = true;
			updateIndexedGeometry();
		}

		@Override
		public void onMapReplaced(IMapControlEvents2OnMapReplacedEvent arg0)
				throws IOException, AutomationException {
			isDirty = true;
			updateIndexedGeometry();
		}
	}
}
