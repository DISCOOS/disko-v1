package org.redcross.sar.map;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.FlankDialog;
import org.redcross.sar.map.feature.AreaFeature;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.util.mso.GeoCollection;
import org.redcross.sar.util.mso.IGeodataIf;
import org.redcross.sar.util.mso.Route;

import com.esri.arcgis.carto.IFeatureLayer;
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

		IFeature feature = search(featureClass, p);
		if (feature != null && feature instanceof AreaFeature) {
			AreaFeature areaFeature = (AreaFeature)feature;
			if (areaFeature.isEditable()) {
				GeometryBag geomBag = (GeometryBag)areaFeature.getShape();
				int index = getGeomIndex(geomBag, p);
				if (index > -1) {
					IAreaIf area = (IAreaIf)areaFeature.getMsoObject();
					GeoCollection geoColl = area.getGeodata();
					Route route = getRouteAt(geoColl, index);
					if (route != null) {
						route.setLayout(getLayout());
						//HACK: To force firing events.
						area.setGeodata(cloneGeoCollection(geoColl));
						map.getActiveView().refresh();
					}
				}
			}
		}
	}
	
	private GeoCollection cloneGeoCollection(GeoCollection oldColl) {
		GeoCollection newColl = new GeoCollection(null);
		Iterator iter = oldColl.getPositions().iterator();
		while (iter.hasNext()) {
			newColl.add((IGeodataIf)iter.next());
		}
		return newColl;
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
		List clipLayers = ((FlankDialog)dialog).getClipModel().getSelected();
		if (clipLayers.size() > 0) {
			layout += "ClipFeatures=";
			for (int i = 0; i < clipLayers.size(); i++) {
				IFeatureLayer flayer = (IFeatureLayer)clipLayers.get(i);
				layout += flayer.getFeatureClass().getAliasName()+",";
			}
		}
		System.out.println(layout);
		return layout;
	}
}
