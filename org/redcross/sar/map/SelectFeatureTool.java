package org.redcross.sar.map;

import java.io.IOException;
import java.util.ArrayList;

import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.IMsoFeatureClass;

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
	private ArrayList<IMsoFeatureClass> featureClasses = null;
	
	/**
	 * Constructs the DrawTool
	 */
	public SelectFeatureTool() throws IOException {
		p = new Point();
		p.setX(0);
		p.setY(0);
		featureClasses = new ArrayList<IMsoFeatureClass>();
	}

	public void onCreate(Object obj) throws IOException, AutomationException {
		if (obj instanceof IDiskoMap) {
			map = (DiskoMap)obj;
		}
	}
	
	public void addFeatureClass(IMsoFeatureClass fc) {
		featureClasses.add(fc);
	}
	
	public void removeAll() {
		featureClasses.clear();
	}

	public void onMouseDown(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		p.setX(x);
		p.setY(y); 
		transform(p);

		for (int i = 0; i < featureClasses.size(); i++) {
			IMsoFeatureClass fc = (IMsoFeatureClass)featureClasses.get(i);
			IFeature feature = search(fc, p);
			if (feature != null && feature instanceof IMsoFeature) {
				editFeature = (IMsoFeature)feature;
				if (editFeature.isEditable()) {
					fc.clearSelected();
					fc.setSelected(editFeature, true);
					map.partialRefresh(null);
					break;
				}
			}
		}
	}
}
