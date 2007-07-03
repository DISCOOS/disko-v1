package org.redcross.sar.map;

import java.io.IOException;
import java.util.List;

import org.redcross.sar.map.feature.AreaFeatureClass;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.IMsoFeatureClass;
import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.mso.data.IMsoObjectIf;

import com.esri.arcgis.interop.AutomationException;

/**
 * A custom draw tool.
 * @author geira
 *
 */
public class EraseCommand extends AbstractCommandTool {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs the DrawTool
	 */
	public EraseCommand() throws IOException {
	}
	
	public void onCreate(Object obj) throws IOException, AutomationException {
		if (obj instanceof IDiskoMap) {
			map = (DiskoMap)obj;
		}
	}

	public void onClick() throws IOException, AutomationException {
		List layers = map.getMapManager().getMsoLayers();
		for (int i = 0; i < layers.size(); i++) {
			IMsoFeatureLayer layer = (IMsoFeatureLayer)layers.get(i);
			IMsoFeatureClass msoFC = (IMsoFeatureClass)layer.getFeatureClass();
			List selected = msoFC.getSelected();
			for (int j = 0; j < selected.size(); j++) {
				IMsoFeature msoFeature = (IMsoFeature)selected.get(j);
				if (msoFeature.isSelected())  {
					IMsoObjectIf msoObject = msoFeature.getMsoObject();
					if (msoObject instanceof IAreaIf) {
						IAreaIf area = (IAreaIf)msoObject;
						((AreaFeatureClass)msoFC).deleteAreaPOIs(area);
					}
					msoFeature.getMsoObject().deleteObject();
					map.fireEditLayerChanged();
					return;
				}
			}
		}
	}
}
