package org.redcross.sar.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.map.layer.AbstractMsoLayer;
import org.redcross.sar.map.layer.AreaLayer;
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


	private EnumSet<IMsoManagerIf.MsoClassCode> myInterests = null;

	public DiskoMapManagerImpl(IDiskoApplication app) {
		this.app = app;
		maps = new ArrayList();
		myInterests = EnumSet.of(IMsoManagerIf.MsoClassCode.CLASSCODE_POI);
		myInterests.add(IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA);
		myInterests.add(IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA);
		myInterests.add(IMsoManagerIf.MsoClassCode.CLASSCODE_AREA);
		myInterests.add(IMsoManagerIf.MsoClassCode.CLASSCODE_TRACK);
		myInterests.add(IMsoManagerIf.MsoClassCode.CLASSCODE_SKETCH);
		myInterests.add(IMsoManagerIf.MsoClassCode.CLASSCODE_ROUTE);
		
		msoLayers = new ArrayList();
		msoLayers.add(new POILayer(app.getMsoModel()));
		msoLayers.add(new OperationAreaLayer(app.getMsoModel()));
		msoLayers.add(new SearchAreaLayer(app.getMsoModel()));
		msoLayers.add(new AreaLayer(app.getMsoModel()));
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
	
	public AbstractMsoLayer getMsoLayer(Enum classCode) {
		for (int i = 0; i < msoLayers.size(); i++) {
			AbstractMsoLayer customLayer = (AbstractMsoLayer)msoLayers.get(i);
			if (customLayer.getClassCode() == classCode) {
				return customLayer;
			}
		}
		return null;
	}
}
