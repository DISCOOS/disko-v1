package org.redcross.sar.map;

import java.io.IOException;
import java.util.List;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.ErrorDialog;
import org.redcross.sar.gui.FlankDialog;

import com.esri.arcgis.carto.FeatureLayer;
import com.esri.arcgis.carto.IElement;
import com.esri.arcgis.carto.IGraphicsContainer;
import com.esri.arcgis.carto.LineElement;
import com.esri.arcgis.carto.PolygonElement;
import com.esri.arcgis.display.LineFillSymbol;
import com.esri.arcgis.display.RgbColor;
import com.esri.arcgis.display.SimpleLineSymbol;
import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geodatabase.IFeatureClass;
import com.esri.arcgis.geodatabase.IFeatureCursor;
import com.esri.arcgis.geodatabase.SpatialFilter;
import com.esri.arcgis.geodatabase.esriSpatialRelEnum;
import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.IEnvelope;
import com.esri.arcgis.geometry.IGeometry;
import com.esri.arcgis.geometry.IGeometryCollection;
import com.esri.arcgis.geometry.Line;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.geometry.Polygon;
import com.esri.arcgis.geometry.Polyline;
import com.esri.arcgis.interop.AutomationException;

/**
 * A custom draw tool.
 * @author geira
 *
 */
public class FlankTool extends AbstractCommandTool {

	private static final long serialVersionUID = 1L;
	private IDiskoApplication app = null;
	private IGraphicsContainer graphics = null;
	private Point p = null;
	private IEnvelope refreshEnvelope = null;
	private LineFillSymbol redFill  = null;
	private LineFillSymbol blueFill = null;
	
	
	private static final int LEFT_SIDE_FLANK  = 1;
	private static final int RIGHT_SIDE_FLANK = 2;
	
	
	/**
	 * Constructs the DrawTool
	 */
	public FlankTool(IDiskoApplication app) throws IOException, AutomationException {
		this.app = app;
		p = new Point();
		p.setX(0);
		p.setY(0);
		refreshEnvelope = new Envelope();
		refreshEnvelope.putCoords(0, 0, 0, 0);
		
		// fill symbols
		redFill = new LineFillSymbol();
		RgbColor redColor = new RgbColor();
		redColor.setRed(255);
		redFill.setColor(redColor);
		redFill.setAngle(45);
		SimpleLineSymbol leftOutline = new SimpleLineSymbol();
		leftOutline.setColor(redColor);
		redFill.setOutline(leftOutline);
	
		blueFill = new LineFillSymbol();
		RgbColor blueColor = new RgbColor();
		blueColor.setBlue(255);
		blueFill.setColor(blueColor);
		blueFill.setAngle(45);
		SimpleLineSymbol rightOutline = new SimpleLineSymbol();
		rightOutline.setColor(blueColor);
		blueFill.setOutline(rightOutline);
		
		// the dialog
		dialog = new FlankDialog(app, this);
		dialog.setIsToggable(false);
	}

	public void onCreate(Object obj) throws IOException, AutomationException {
		if (obj instanceof DiskoMap) {
			map = (DiskoMap)obj;
			graphics = map.getActiveView().getGraphicsContainer();
			FlankDialog flankDialog = (FlankDialog)dialog;
			flankDialog.onLoad(map);
			flankDialog.setLocationRelativeTo(map, DiskoDialog.POS_WEST, false);
		}
	}

	public void onMouseDown(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		p.setX(x);
		p.setY(y); 
		transform(p);
		IElement elem = map.searchGraphics(p);
		if (elem != null && elem instanceof LineElement) {
			Polyline pl = (Polyline)elem.getGeometry();
			createFlankes(pl);
		}
	}
	
	private void createFlankes(Polyline pl)  throws IOException {
		refreshEnvelope.putCoords(0, 0, 0, 0);
		FlankDialog flankDialog = (FlankDialog)dialog;
		// left side
		if (flankDialog.getLeftCheckBox().isSelected()) {
			double value = (double)((Integer)flankDialog.getLeftSpinner().getValue()).intValue();
			try {
				createFlanke(pl, value, LEFT_SIDE_FLANK);
			} catch (AutomationException e) {
				showError("Kan ikke lage venstre flanke. Ugyldig geometri. Tegn på nytt", e.getDescription());
			}
		}
		// right side
		if (flankDialog.getRightCheckBox().isSelected()) {
			double value = (double)((Integer)flankDialog.getRightSpinner().getValue()).intValue();
			try {
				createFlanke(pl, value, RIGHT_SIDE_FLANK);
			} catch (AutomationException e) {
				showError("Kan ikke lage høyre flanke. Ugyldig geometri. Tegn på nytt", e.getDescription());
			}
		}
		map.partialRefreshGraphics(refreshEnvelope);
	}
	
	private void showError(String msg, String description) {
		ErrorDialog dialog = app.getUIFactory().getErrorDialog();
		dialog.showError(msg, description);
		dialog.setLocationRelativeTo(map);
	}
	
	private void createFlanke(Polyline path, double dist, int side) 
			throws IOException, AutomationException {
		Line n1 = new Line();
		Line n2 = new Line();
		Polyline pl = new Polyline();
		IGeometryCollection coll = null;
		Polygon buffer = (Polygon) path.buffer(dist);
		
		path.queryNormal(3, 0, false, dist *  1, n1);
		path.queryNormal(3, 0, false, dist * -1, n2);
		pl.addPoint(n1.getToPoint(), null, null);
		pl.addPoint(n2.getToPoint(), null, null);
		coll = buffer.cut2(pl);
			
		double d = path.getLength();
		path.queryNormal(12, d, false, dist *  1, n1);
		path.queryNormal(12, d, false, dist * -1, n2);
		pl.setFromPoint(n2.getToPoint());
		pl.setToPoint(n1.getToPoint());
		Polygon rest = (Polygon) coll.getGeometry(1);
		coll = rest.cut2(pl);
			
		IGeometry[] leftGeom = new IGeometry[2];
		IGeometry[] rightGeom = new IGeometry[2];
		
		((Polygon) coll.getGeometry(1)).cut(path,leftGeom,rightGeom);

		FlankProperties properties = new FlankProperties();
		if (side == LEFT_SIDE_FLANK) {
			properties.setSide("venstre");
			Polygon leftPoly = clip((Polygon) leftGeom[0]);
			PolygonElement pe = new PolygonElement();
			pe.setGeometry(leftPoly);
			pe.setSymbol(redFill);
			pe.setCustomProperty(properties);
			graphics.addElement(pe, 0);
			refreshEnvelope.union(leftPoly.getEnvelope());
		}
		else if (side == RIGHT_SIDE_FLANK) {
			properties.setSide("hoyre");
			Polygon rightPoly = clip((Polygon) rightGeom[0]);
			PolygonElement pe = new PolygonElement();
			pe.setGeometry(rightPoly);
			pe.setSymbol(blueFill);
			pe.setCustomProperty(properties);
			graphics.addElement(pe, 0);
			refreshEnvelope.union(rightPoly.getEnvelope());
		}
	}
	
	private Polygon clip(Polygon flank) throws IOException, AutomationException {
		List clipLayers = ((FlankDialog)dialog).getClipModel().getSelected();
		if (clipLayers == null || clipLayers.size() < 1) {
			return flank;
		}
		Polygon result = flank;
		for (int i = 0; i < clipLayers.size(); i++) {
			FeatureLayer flayer = (FeatureLayer)clipLayers.get(i);
			IFeatureClass fclass = flayer.getFeatureClass();
			SpatialFilter spatialFilter = new SpatialFilter();
			spatialFilter.setGeometryByRef(flank.getEnvelope());
			spatialFilter.setGeometryField(fclass.getShapeFieldName());
			spatialFilter.setSpatialRel(esriSpatialRelEnum.esriSpatialRelIntersects);
			IFeatureCursor featureCursor = fclass.search(spatialFilter,false);
			
			IFeature feature = featureCursor.nextFeature();
			while (feature != null) {
				result = (Polygon)result.difference(feature.getShape());
				feature = featureCursor.nextFeature();
			}
		}
		return result;
	}
}
