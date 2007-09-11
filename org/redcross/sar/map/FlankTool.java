package org.redcross.sar.map;

import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geometry.GeometryBag;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.interop.AutomationException;
import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.FlankDialog;
import org.redcross.sar.map.feature.FlankFeature;
import org.redcross.sar.map.feature.MsoFeatureClass;
import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IRouteIf;
import org.redcross.sar.util.mso.Route;

import java.io.IOException;

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
//CMR			GeoList geoList = area.getGeodata();
//CMR			Route route = getRouteAt(geoList, index);
				IMsoObjectIf msoObject = area.getGeodataAt(index);         // todo sjekk etter endring av GeoCollection
                if (msoObject != null && msoObject instanceof IRouteIf)
                {
                    Route route = ((IRouteIf)msoObject).getGeodata();
                    if (route != null) {
	    				route.setLayout(getLayout());
		    			flankFeature.msoGeometryChanged();
			    		map.partialRefresh(editLayer, null);
				    }
                }
            }
		}
	}

//CMR	private Route getRouteAt(GeoList geoList, int index) { // todo (vw) Use direct lookup in list in stead
//CMR        Iterator iter = geoList.getPositions().iterator();
//CMR		int i = 0;
//CMR		while (iter.hasNext()) {
//CMR			IGeodataIf geodata = (IGeodataIf) iter.next();
//CMR			if (geodata instanceof Route && index == i) {
//CMR				return (Route)geodata;
//CMR			}
//CMR			i++;
//CMR		}
//CMR		return null;
//CMR	}

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
