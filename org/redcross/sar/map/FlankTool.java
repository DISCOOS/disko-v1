package org.redcross.sar.map;

import java.awt.Frame;
import java.io.IOException;
import java.util.List;

import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.FlankDialog;

import com.esri.arcgis.carto.FeatureLayer;
import com.esri.arcgis.carto.ILayer;
import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geodatabase.IFeatureClass;
import com.esri.arcgis.geodatabase.IFeatureCursor;
import com.esri.arcgis.geodatabase.SpatialFilter;
import com.esri.arcgis.geodatabase.esriSpatialRelEnum;
import com.esri.arcgis.geometry.Envelope;
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
	private Envelope searchEnvelope = null;
	private String flankeLayerName = null;
	private FeatureLayer flankeFL = null;
	
	private static final int LEFT_SIDE_FLANK  = 1;
	private static final int RIGHT_SIDE_FLANK = 2;
	
	
	/**
	 * Constructs the DrawTool
	 */
	public FlankTool(Frame frame, String flankeLayerName) throws IOException {
		this.flankeLayerName = flankeLayerName;
		searchEnvelope = new Envelope();
		searchEnvelope.putCoords(0, 0, 0, 0);
		searchEnvelope.setHeight(100);
		searchEnvelope.setWidth(100);
		
		// the dialog
		dialog = new FlankDialog(frame, this);
		dialog.setIsToggable(false);
	}

	public void onCreate(Object obj) throws IOException, AutomationException {
		if (obj instanceof DiskoMap) {
			map = (DiskoMap)obj;
			flankeFL = map.getFeatureLayer(flankeLayerName);
			FlankDialog flankDialog = (FlankDialog)dialog;
			flankDialog.onLoad(map);
			flankDialog.setLocationRelativeTo(map, DiskoDialog.POS_WEST, false);
		}
	}

	public void onMouseDown(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		Point p = transform(x,y);
		searchEnvelope.centerAt(p);
		for (int i = 0; i < map.getLayerCount(); i++) {
			ILayer layer = map.getLayer(i);
			if (layer instanceof FeatureLayer) {
				FeatureLayer flayer = (FeatureLayer)layer;
				if (flayer.isSelectable()) {
					IFeature feature = search(flayer, searchEnvelope);
					if (feature != null) {
						createFlankes(feature);
						break;
					}
				}
			}
		}	
	}
	
	public void setTolerance(double tolerance) throws IOException {
		searchEnvelope.setHeight(tolerance);
		searchEnvelope.setWidth(tolerance);
	}
	
	private void createFlankes(IFeature feature) throws IOException, AutomationException {
		FlankDialog flankDialog = (FlankDialog)dialog;
		int geomType = feature.getShape().getGeometryType();
		if (geomType == com.esri.arcgis.geometry.esriGeometryType.esriGeometryPolyline) {
			// left side
			if (flankDialog.getLeftCheckBox().isSelected()) {
				double value = (double)((Integer)flankDialog.getLeftSpinner().getValue()).intValue();
				createBuffer(feature, value, LEFT_SIDE_FLANK);
			}
			// right side
			if (flankDialog.getRightCheckBox().isSelected()) {
				double value = (double)((Integer)flankDialog.getRightSpinner().getValue()).intValue();
				createBuffer(feature, value, RIGHT_SIDE_FLANK);
			}
		}
		// clear selection and refresh
		map.getMap().clearSelection();
		map.getActiveView().refresh();
	}
	
	private void createBuffer(IFeature feature, double dist, int side) 
			throws IOException, AutomationException {
		Line n1 = new Line();
		Line n2 = new Line();
		Polyline pl = new Polyline();
		IGeometryCollection coll = null;

		Polyline pathGeom = (Polyline) feature.getShape();
		Polygon bufferGeom = (Polygon) pathGeom.buffer(dist);
			
		pathGeom.queryNormal(3, 0, false, dist * 2, n1);
		pathGeom.queryNormal(3, 0, false, dist * -2, n2);
		pl.addPoint(n1.getToPoint(), null, null);
		pl.addPoint(n2.getToPoint(), null, null);
		coll = bufferGeom.cut2(pl);
			
		double d = pathGeom.getLength();
		pathGeom.queryNormal(12, d, false, dist * 2, n1);
		pathGeom.queryNormal(12, d, false, dist * -2, n2);
		pl.setFromPoint(n2.getToPoint());
		pl.setToPoint(n1.getToPoint());
		coll = ((Polygon) coll.getGeometry(1)).cut2(pl);
			
		IGeometry[] leftGeom = new IGeometry[2];
		IGeometry[] rightGeom = new IGeometry[2];
		((Polygon) coll.getGeometry(1)).cut(pathGeom,leftGeom,rightGeom);

		IFeatureClass fclass = flankeFL.getFeatureClass();
		int field = fclass.findField("Side");

		if (side == LEFT_SIDE_FLANK) {
			IFeature leftFlank = flankeFL.getFeatureClass().createFeature();
			leftFlank.setValue(field, "venstre");
			Polygon leftPoly = clip((Polygon) leftGeom[0]);
			leftFlank.setShapeByRef(leftPoly);
			leftFlank.store();
		}
		else if (side == RIGHT_SIDE_FLANK) {
			IFeature rightFlank = flankeFL.getFeatureClass().createFeature();
			rightFlank.setValue(field, "hoyre");
			Polygon rightPoly = clip((Polygon) rightGeom[0]);
			rightFlank.setShapeByRef(rightPoly);
			rightFlank.store();
		}
	}
	
	private Polygon clip(Polygon flank) throws IOException {
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
