package org.redcross.sar.map;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.EventType;
import org.redcross.sar.mso.event.MsoEvent.Update;

import com.esri.arcgis.carto.FeatureLayer;
import com.esri.arcgis.carto.IFeatureLayer;
import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geometry.IEnvelope;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.interop.AutomationException;

public class DiskoMapManagerImpl implements IDiskoMapManager, IMsoUpdateListenerIf {
	
	private IDiskoApplication app = null;
	private ArrayList maps = null;
	private FeatureLayer poiFeatureLayer = null;
	private FeatureLayer basicLineFeatureLayer = null;
	private FeatureLayer flankFeatureLayer = null;
	private EnumSet<IMsoManagerIf.MsoClassCode> myInterests = null;

	public DiskoMapManagerImpl(IDiskoApplication app) {
		this.app = app;
		maps = new ArrayList();
		IMsoEventManagerIf msoEventManager = app.getMsoModel().getEventManager();
		msoEventManager.addClientUpdateListener(this);
		myInterests = EnumSet.of(IMsoManagerIf.MsoClassCode.CLASSCODE_POI);
		myInterests.add(IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA);
		myInterests.add(IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA);
		myInterests.add(IMsoManagerIf.MsoClassCode.CLASSCODE_TRACK);
		myInterests.add(IMsoManagerIf.MsoClassCode.CLASSCODE_SKETCH);
		myInterests.add(IMsoManagerIf.MsoClassCode.CLASSCODE_ROUTE);
	}
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMapManager#getMapInstance()
	 */
	public IDiskoMap getMapInstance() {
		DiskoMap map = null;
        try {
			String mxdDoc = app.getProperty("MxdDocument.path");
			map = new DiskoMap(mxdDoc);
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
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMapManager#getMaps()
	 */
	public List getMaps() {
		return maps;
	}
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMapManager#refreshAllMaps(com.esri.arcgis.geometry.IEnvelope)
	 */
	public void refreshAllMaps(IEnvelope env) {
		try {
			for (int i = 0; i < maps.size(); i++) {
				DiskoMap map = (DiskoMap)maps.get(i);
				if (map != null) {
					if (env != null) {
						map.getActiveView().partialRefresh(
							com.esri.arcgis.carto.esriViewDrawPhase.esriViewGeography, 
							null, env);
					}
					else {
						map.getActiveView().refresh();
					}
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
	
	private DiskoMap getDefaultMap() {
		if (maps.size() > 0) {
			return (DiskoMap)maps.get(0);
		}
		return null;
	}
	
	public IFeatureLayer getPoiFeatureLayer() {
		DiskoMap defaultMap = getDefaultMap();
		if (poiFeatureLayer == null && defaultMap != null) {
			try {
				poiFeatureLayer = defaultMap.getFeatureLayer(app.getProperty("PUI.featureClass.Name"));
			} catch (AutomationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return poiFeatureLayer;
	}
	
	public FeatureLayer getBasicLineFeatureLayer() {
		DiskoMap defaultMap = getDefaultMap();
		if (basicLineFeatureLayer == null && defaultMap != null) {
			try {
				basicLineFeatureLayer= defaultMap.getFeatureLayer(
						app.getProperty("BasicLine.featureClass.Name"));
			} catch (AutomationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return basicLineFeatureLayer;
	}

	public FeatureLayer getFlankFeatureLayer() {
		DiskoMap defaultMap = getDefaultMap();
		if (flankFeatureLayer == null && defaultMap != null) {
			try {
				flankFeatureLayer = defaultMap.getFeatureLayer(
						app.getProperty("BufferPath.featureClass.Name"));
			} catch (AutomationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return flankFeatureLayer;
	}

	public void handleMsoUpdateEvent(Update e) {
		try {
			if (e.getEventTypeMask() == EventType.ADDED_REFERENCE_EVENT.maskValue()) {
				Object source = e.getSource();
				if (source instanceof IPOIIf) {
					IPOIIf poi = (IPOIIf)source;
					Point p = MapUtil.getPoint(poi.getPosition());
					IFeatureLayer poiFL = getPoiFeatureLayer();
					if (poiFL != null) {
						IFeature feature = getPoiFeatureLayer().getFeatureClass().createFeature();
						feature.setShapeByRef(p);
						feature.setValue(4, poi.getRemarks());
						feature.store();
						refreshAllMaps(MapUtil.getEnvelope(p, 50));
					}
				}
			}
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public boolean hasInterestIn(IMsoObjectIf aMsoObject) {
		return myInterests.contains(aMsoObject.getMsoClassCode());
	}
}
