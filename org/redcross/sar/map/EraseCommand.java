package org.redcross.sar.map;

import java.io.IOException;
import java.util.List;

import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.layer.IMsoFeatureLayer;

import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.systemUI.ICommand;

/**
 * A custom draw tool.
 * @author geira
 *
 */
public class EraseCommand implements ICommand {

	private static final long serialVersionUID = 1L;
	private DiskoMap map = null;
	
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
		// TODO: prompt confirmation
		List layers = map.getMsoLayers();
		for (int i = 0; i < layers.size(); i++) {
			IMsoFeatureLayer layer = (IMsoFeatureLayer)layers.get(i);
			List selected = layer.getSelected();
			for (int j = 0; j < selected.size(); j++) {
				IMsoFeature msoFeature = (IMsoFeature)selected.get(j);
				if (msoFeature.isSelected())  {
					msoFeature.getMsoObject().deleteObject();
					return;
				}
			}
		}
	}

	public int getBitmap() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getCaption() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCategory() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getHelpContextID() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getHelpFile() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getMessage() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTooltip() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isChecked() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEnabled() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return false;
	}
}
