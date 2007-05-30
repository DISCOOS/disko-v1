package org.redcross.sar.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.map.layer.AreaLayer;
import org.redcross.sar.map.layer.FlankLayer;
import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.map.layer.OperationAreaLayer;
import org.redcross.sar.map.layer.POILayer;
import org.redcross.sar.map.layer.SearchAreaLayer;
import org.redcross.sar.mso.IMsoManagerIf;

import com.esri.arcgis.geometry.IEnvelope;
import com.esri.arcgis.interop.AutomationException;

public class DiskoMapManagerImpl implements IDiskoMapManager {

	private IDiskoApplication app = null;
	private ArrayList maps = null;
	private List msoLayers = null;

	public DiskoMapManagerImpl(IDiskoApplication app) {
		this.app = app;
		maps = new ArrayList();
		
		msoLayers = new ArrayList();
		msoLayers.add(new POILayer(app.getMsoModel()));
		msoLayers.add(new OperationAreaLayer(app.getMsoModel()));
		msoLayers.add(new SearchAreaLayer(app.getMsoModel()));
		msoLayers.add(new AreaLayer(app.getMsoModel()));
		msoLayers.add(new FlankLayer(app.getMsoModel()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.redcross.sar.map.IDiskoMapManager#getMapInstance()
	 */
	public IDiskoMap getMapInstance() {
		DiskoMap map = null;
		try {
			String mxdDoc = app.getProperty("MxdDocument.path");
			map = new DiskoMap(mxdDoc, this, app.getMsoModel());
			maps.add(map);
		} catch (AutomationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.redcross.sar.map.IDiskoMapManager#getMaps()
	 */
	public List getMaps() {
		return maps;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.redcross.sar.map.IDiskoMapManager#refreshAllMaps(com.esri.arcgis.geometry.IEnvelope)
	 */
	public void refreshAllMaps(IEnvelope env) throws AutomationException,
			IOException {
		if (env != null) {
			env.expand(50, 50, false);
		}
		for (int i = 0; i < maps.size(); i++) {
			DiskoMap map = (DiskoMap) maps.get(i);
			if (map.isShowing()) {
				if (env != null) {
					map.getActiveView().partialRefresh(
							com.esri.arcgis.carto.esriViewDrawPhase.esriViewGeography,null, env);
				} else {
					map.getActiveView().refresh();
				}
			}
		}
	}
	
	public List getMsoLayers() {
		return msoLayers;
	}
	
	public IMsoFeatureLayer getMsoLayer(IMsoManagerIf.MsoClassCode classCode) {
		for (int i = 0; i < msoLayers.size(); i++) {
			IMsoFeatureLayer msoFeatureLayer = (IMsoFeatureLayer)msoLayers.get(i);
			if (msoFeatureLayer.getClassCode() == classCode) {
				return msoFeatureLayer;
			}
		}
		return null;
	}
}
