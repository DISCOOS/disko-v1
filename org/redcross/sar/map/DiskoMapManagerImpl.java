package org.redcross.sar.map;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.committer.ICommitWrapperIf;
import org.redcross.sar.mso.committer.ICommittableIf.ICommitObjectIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IOperationAreaIf;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.mso.data.IRouteIf;
import org.redcross.sar.mso.data.ISearchAreaIf;
import org.redcross.sar.mso.event.IMsoCommitListenerIf;
import org.redcross.sar.mso.event.IMsoDerivedUpdateListenerIf;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.IMsoGisListenerIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.Commit;
import org.redcross.sar.mso.event.MsoEvent.DerivedUpdate;
import org.redcross.sar.mso.event.MsoEvent.Gis;
import org.redcross.sar.mso.event.MsoEvent.Update;
import org.redcross.sar.util.mso.Route;

import com.esri.arcgis.carto.FeatureLayer;
import com.esri.arcgis.carto.IFeatureLayer;
import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.IEnvelope;
import com.esri.arcgis.geometry.ISpatialReference;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.geometry.Polygon;
import com.esri.arcgis.geometry.Polyline;
import com.esri.arcgis.interop.AutomationException;

public class DiskoMapManagerImpl implements IDiskoMapManager, 
		IMsoCommitListenerIf, IMsoUpdateListenerIf, 
		IMsoGisListenerIf, IMsoDerivedUpdateListenerIf {
	
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
		msoEventManager.addCommitListener(this);
		msoEventManager.addClientUpdateListener(this);
		msoEventManager.addGisUpdateListener(this);
		msoEventManager.addDerivedUpdateListener(this);
		
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
			env.expand(50, 50, false);
			for (int i = 0; i < maps.size(); i++) {
				DiskoMap map = (DiskoMap)maps.get(i);
				if (map.isShowing()) {
					if (env != null) {
						map.getActiveView().partialRefresh(
								com.esri.arcgis.carto.esriViewDrawPhase.
								esriViewGeography, null, env);
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
				poiFeatureLayer = defaultMap.getFeatureLayer(
						app.getProperty("PUI.featureClass.Name"));
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
	
	public void handleMsoCommitEvent(Commit e) {
		IEnvelope refreshEnv = getRefreshEnvelope();
		ICommitWrapperIf commitWrapper = (ICommitWrapperIf) e.getSource();
		List commitObjects = commitWrapper.getObjects();
		for (int i = 0; i < commitObjects.size(); i++) {
			ICommitObjectIf commitable = (ICommitObjectIf) commitObjects.get(i);
			Object commitObject = commitable.getObject();
			if (commitObject instanceof IPOIIf) {
				IPOIIf poi = (IPOIIf) commitObject;
				createPoiFeature(poi, refreshEnv);
			} 
			else if (commitObject instanceof IOperationAreaIf) {
				IOperationAreaIf opArea = (IOperationAreaIf) commitObject;
				createOperationAreaFeature(opArea, refreshEnv);
			}
			else if (commitObject instanceof ISearchAreaIf) {
				ISearchAreaIf searchArea = (ISearchAreaIf) commitObject;
				createSearchAreaFeature(searchArea, refreshEnv);
			}
			else if (commitObject instanceof IRouteIf) {
				IRouteIf route = (IRouteIf) commitObject;
				createRouteFeature(route, refreshEnv);
			}
		}
		refreshAllMaps(refreshEnv);
	}
	
	private IEnvelope getRefreshEnvelope() {
		try {
			return new Envelope();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void createPoiFeature(IPOIIf poi, IEnvelope refreshEnv) {
		try {
			ISpatialReference srs = getDefaultMap().getSpatialReference();
			Point p = MapUtil.getEsriPoint(poi.getPosition(), srs);
			IFeatureLayer poiFL = getPoiFeatureLayer();
			if (poiFL != null) {
				IFeature feature = poiFL.getFeatureClass().createFeature();
				feature.setShapeByRef(p);
				feature.setValue(4, poi.getRemarks());
				feature.store();
				refreshEnv.union(MapUtil.getEnvelope(p, 50));
			}
		} catch (AutomationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createOperationAreaFeature(
			IOperationAreaIf opArea, IEnvelope refreshEnv) {
		try {
			ISpatialReference srs = getDefaultMap().getSpatialReference();
			IFeatureLayer flankFL = getFlankFeatureLayer();
			org.redcross.sar.util.mso.Polygon msoPolygon = opArea.getGeodata();
			if (flankFL != null && msoPolygon != null) {
				Polygon polygon = MapUtil.getEsriPolygon(msoPolygon, srs);
				IFeature feature = flankFL.getFeatureClass().createFeature();
				feature.setShapeByRef(polygon);
				int field = flankFL.getFeatureClass().findField("Side");
				feature.setValue(field, "venstre");
				feature.store();
				refreshEnv.union(polygon.getEnvelope());
			}
		} catch (AutomationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createSearchAreaFeature(
			ISearchAreaIf searchArea, IEnvelope refreshEnv) {
		try {
			ISpatialReference srs = getDefaultMap().getSpatialReference();
			IFeatureLayer flankFL = getFlankFeatureLayer();
			org.redcross.sar.util.mso.Polygon msoPolygon = searchArea.getGeodata();
			if (flankFL != null && msoPolygon != null) {
				Polygon polygon = MapUtil.getEsriPolygon(msoPolygon, srs);
				IFeature feature = flankFL.getFeatureClass().createFeature();
				feature.setShapeByRef(polygon);
				int field = flankFL.getFeatureClass().findField("Side");
				feature.setValue(field, "hoyre");
				feature.store();
				refreshEnv.union(polygon.getEnvelope());
			}
		} catch (AutomationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createRouteFeature(
			IRouteIf msoRoute, IEnvelope refreshEnv) {
		try {
			ISpatialReference srs = getDefaultMap().getSpatialReference();
			IFeatureLayer basicLineFL = getBasicLineFeatureLayer();
			Route route = msoRoute.getGeodata();
			if (basicLineFL != null && route != null) {
				Polyline polyline = MapUtil.getEsriPolyline(route, srs);
				IFeature feature = basicLineFL.getFeatureClass().createFeature();
				feature.setShapeByRef(polyline);
				feature.store();
				refreshEnv.union(polyline.getEnvelope());
			}
		} catch (AutomationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void handleMsoGisEvent(Gis e) {
		//System.out.println(e.getEventTypeMask()+" "+e.getClass());
	}

	public void handleMsoDerivedUpdateEvent(DerivedUpdate e) {
		//System.out.println(e.getEventTypeMask()+" "+e.getClass());
	}

	public void handleMsoUpdateEvent(Update e) {
		//System.out.println(e.getEventTypeMask()+" "+e.getClass());
	}

	public boolean hasInterestIn(IMsoObjectIf aMsoObject) {
		return myInterests.contains(aMsoObject.getMsoClassCode());
	}
}
