package org.redcross.sar.map;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.IMsoFeatureClass;
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
	private ArrayList<IMsoFeatureLayer> selectableLayers = null;
	
	/**
	 * Constructs the DrawTool
	 */
	public SelectFeatureTool() throws IOException {
		p = new Point();
		p.setX(0);
		p.setY(0);
		selectableLayers = new ArrayList<IMsoFeatureLayer>();
	}

	public void onCreate(Object obj) throws IOException, AutomationException {
		if (obj instanceof IDiskoMap) {
			map = (DiskoMap)obj;
		}
	}
	
	public void addSelectableLayer(IMsoFeatureLayer layer) {
		if (selectableLayers.indexOf(layer) == -1) {
			selectableLayers.add(layer);
		}
	}
	
	public void removeAll() {
		selectableLayers.clear();
	}

	public void onMouseDown(int button, int shift, int x, int y)
	throws IOException, AutomationException {
		p.setX(x);
		p.setY(y); 
		transform(p);
		Runnable r = new Runnable() {
			public void run() {
				try {
					for (int i = 0; i < selectableLayers.size(); i++) {
						IMsoFeatureLayer layer = (IMsoFeatureLayer)selectableLayers.get(i);
						IMsoFeatureClass fc = (IMsoFeatureClass)layer.getFeatureClass();
						fc.clearSelected();
					}
					for (int i = 0; i < selectableLayers.size(); i++) {
						IMsoFeatureLayer layer = (IMsoFeatureLayer)selectableLayers.get(i);
						IMsoFeatureClass fc = (IMsoFeatureClass)layer.getFeatureClass();
						IFeature feature = search(fc, p);
						if (feature != null && feature instanceof IMsoFeature) {
							editFeature = (IMsoFeature)feature;
							fc.setSelected(editFeature, true);
							map.partialRefresh(layer, null);
							break;
						}
					}
				} catch (AutomationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		SwingUtilities.invokeLater(r);
	}
}
