package org.redcross.sar.map;

import java.io.IOException;
import java.util.List;

import javax.swing.SwingUtilities;

import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.MsoFeatureClass;
import org.redcross.sar.map.layer.IMsoFeatureLayer;

import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.interop.AutomationException;

/**
 * A custom draw tool.
 * @author geira
 *
 */
public class SelectFeatureTool extends AbstractCommandTool {

	private static final long serialVersionUID = 1L;
	private Point p = null;
	
	/**
	 * Constructs the DrawTool
	 */
	public SelectFeatureTool() throws IOException {
		p = new Point();
		p.setX(0);
		p.setY(0);
	}

	public void onCreate(Object obj) throws IOException, AutomationException {
		if (obj instanceof IDiskoMap) {
			map = (DiskoMap)obj;
		}
	}

	public void onMouseDown(int button, int shift, int x, int y)
	throws IOException, AutomationException {
		p.setX(x);
		p.setY(y); 
		transform(p);

		List layers = map.getMapManager().getMsoLayers();
		for (int i = 0; i < layers.size(); i++) {
			IMsoFeatureLayer layer = (IMsoFeatureLayer)layers.get(i);
			if (layer.isSelectable()) {
				layer.clearSelected();
			}
		}
		for (int i = 0; i < layers.size(); i++) {
			IMsoFeatureLayer layer = (IMsoFeatureLayer)layers.get(i);
			if (layer.isSelectable()) {
				MsoFeatureClass fc = (MsoFeatureClass)layer.getFeatureClass();
				IFeature feature = search(fc, p);
				if (feature != null && feature instanceof IMsoFeature) {
					layer.setSelected((IMsoFeature)feature, true);
					map.partialRefresh(layer, null);
					break;
				}
			}
		}
	}
}
