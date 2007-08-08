package org.redcross.sar.map;

import java.io.IOException;
import java.util.Iterator;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.FlankDialog;
import org.redcross.sar.map.feature.FlankFeature;
import org.redcross.sar.map.feature.MsoFeatureClass;
import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.util.mso.GeoCollection;
import org.redcross.sar.util.mso.IGeodataIf;
import org.redcross.sar.util.mso.Route;

import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geometry.GeometryBag;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.interop.AutomationException;

/**
 * A custom draw tool.
 * @author geira
 *
 */
public class FlankTool extends AbstractCommandTool {

	private static final long serialVersionUID = 1L;
	private Point p = null;
	
	/**
	 * Constructs the DrawTool
	 */
	public FlankTool(IDiskoApplication app) throws IOException, AutomationException {
		dialog = new FlankDialog(app, this);
		dialog.setIsToggable(false);
		p = new Point();
	}

	public void onCreate(Object obj) throws IOException, AutomationException {
		if (obj instanceof IDiskoMap) {
			map = (DiskoMap)obj;
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

		IMsoFeatureLayer editLayer = map.getMapManager().getMsoLayer(
				IMsoFeatureLayer.LayerCode.FLANK_LAYER);
		MsoFeatureClass featureClass = (MsoFeatureClass)editLayer.getFeatureClass();
		IFeature feature = search(featureClass, p);
		if (feature != null && feature instanceof FlankFeature) {
			FlankFeature flankFeature = (FlankFeature)feature;
			GeometryBag geomBag = (GeometryBag)flankFeature.getShape();
			int index = getGeomIndex(geomBag, p);
			if (index > -1) {
				IAreaIf area = (IAreaIf)flankFeature.getMsoObject();
				GeoCollection geoColl = area.getGeodata();
				Route route = getRouteAt(geoColl, index);
				if (route != null) {
					route.setLayout(getLayout());
					flankFeature.msoGeometryChanged();
					map.partialRefresh(editLayer, null);
				}
			}
		}
	}
	
	private Route getRouteAt(GeoCollection geoColl, int index) {
		Iterator iter = geoColl.getPositions().iterator();
		int i = 0;
		while (iter.hasNext()) {
			IGeodataIf geodata = (IGeodataIf) iter.next();
			if (geodata instanceof Route && index == i) {
				return (Route)geodata;
			}
			i++;
		}
		return null;
	}
	
	private String getLayout() throws AutomationException, IOException {
		FlankDialog flankDialog = (FlankDialog)dialog;
		String layout = "";
		if (flankDialog.getLeftCheckBox().isSelected()) {
			layout += "LeftDist="+flankDialog.getLeftSpinner().getValue()+"&";
		}
		if (flankDialog.getRightCheckBox().isSelected()) {
			layout += "RightDist="+flankDialog.getRightSpinner().getValue()+"&";
		}
		/*List clipLayers = ((FlankDialog)dialog).getClipModel().getSelected();
		if (clipLayers.size() > 0) {
			layout += "ClipFeatures=";
			for (int i = 0; i < clipLayers.size(); i++) {
				IFeatureLayer flayer = (IFeatureLayer)clipLayers.get(i);
				layout += flayer.getFeatureClass().getAliasName()+",";
			}
		}*/
		return layout;
	}
}
