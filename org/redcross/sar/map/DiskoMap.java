package org.redcross.sar.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.swing.border.SoftBevelBorder;

import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.event.IDiskoMapEventListener;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.IMsoFeatureClass;
import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.Update;

import com.esri.arcgis.beans.map.MapBean;
import com.esri.arcgis.carto.FeatureLayer;
import com.esri.arcgis.carto.IFeatureLayer;
import com.esri.arcgis.carto.ILayer;
import com.esri.arcgis.carto.IMap;
import com.esri.arcgis.controls.IMapControlEvents2Adapter;
import com.esri.arcgis.controls.IMapControlEvents2OnAfterDrawEvent;
import com.esri.arcgis.controls.IMapControlEvents2OnExtentUpdatedEvent;
import com.esri.arcgis.controls.IMapControlEvents2OnMapReplacedEvent;
import com.esri.arcgis.geodatabase.Feature;
import com.esri.arcgis.geodatabase.IEnumIDs;
import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geodatabase.QueryFilter;
import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.IEnvelope;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.systemUI.ITool;

/**
 * This calls extends AbstractDiskoApUi to provide userinterface for all map 
 * related tasks (arbeidsprosesser)
 * @author geira
 *
 */
public final class DiskoMap extends MapBean implements IDiskoMap, IMsoUpdateListenerIf {
	
	private static final long serialVersionUID = 1L;
	private String mxdDoc = null;
	private IDiskoMapManager mapManager = null;
	private SnapLayerSelectionModel snapLayerSelectionModel = null;
	private ClipLayerSelectionModel clipLayerSelectionModel = null;
	private IDiskoTool currentTool = null;
	private ArrayList<IDiskoMapEventListener> listeners = null;
	private DiskoMapEvent diskoMapEvent = null;
	protected EnumSet<IMsoManagerIf.MsoClassCode> myInterests = null;

	/**
	 * Default constructor
	 */
	public DiskoMap(String mxdDoc, IDiskoMapManager mapManager, IMsoModelIf msoModel)  
			throws IOException, AutomationException {
		this.mxdDoc = mxdDoc;
		this.mapManager = mapManager;
		
		listeners = new ArrayList<IDiskoMapEventListener>();
		diskoMapEvent = new DiskoMapEvent(this);
		
		myInterests = EnumSet.of(IMsoManagerIf.MsoClassCode.CLASSCODE_AREA);
		myInterests.add(IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA);
		myInterests.add(IMsoManagerIf.MsoClassCode.CLASSCODE_POI);
		IMsoEventManagerIf msoEventManager = msoModel.getEventManager();
		msoEventManager.addClientUpdateListener(this);
		initialize();
	}
	
	private void initialize() throws IOException, AutomationException {
		setName("diskoMap");			
		setShowScrollbars(false);
		setBorderStyle(com.esri.arcgis.controls.esriControlsBorderStyle.esriNoBorder);
		setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		setDocumentFilename(mxdDoc);
			
		// listen to do actions when the map is loaded
		addIMapControlEvents2Listener(new IMapControlEvents2Adapter() {
			private static final long serialVersionUID = 1L;
			public void onMapReplaced(IMapControlEvents2OnMapReplacedEvent e)
                   	throws java.io.IOException, AutomationException {
				mapLoaded();
				fireOnMapReplaced();
			}
			public void onAfterDraw(IMapControlEvents2OnAfterDrawEvent theEvent)
					throws IOException, AutomationException {
				fireOnAfterScreenDraw();
			}
			public void onExtentUpdated(IMapControlEvents2OnExtentUpdatedEvent theEvent) 
					throws IOException, AutomationException {
				fireOnExtentUpdated();
			}
		});
	}
	
	private void mapLoaded() throws java.io.IOException, AutomationException {
		// add custom layers
		IMap focusMap = getActiveView().getFocusMap();
		List customLayers = mapManager.getMsoLayers();
		for (int i = 0; i < customLayers.size(); i++) {
			IFeatureLayer layer = (IFeatureLayer)customLayers.get(i);
			layer.setSpatialReferenceByRef(getSpatialReference());
			focusMap.addLayer(layer);
		}
		
		// set all featurelayers not selectabel
		for (int i = 0; i < focusMap.getLayerCount(); i++) {
			ILayer layer = focusMap.getLayer(i);
			if (layer instanceof IFeatureLayer) {
				IFeatureLayer flayer = (IFeatureLayer)layer;
				if (!(flayer instanceof IMsoFeatureLayer)) {
					flayer.setSelectable(false);
				}
			}
		}
	}
	
	public void handleMsoUpdateEvent(Update e) {
		/*try {
			IMsoObjectIf msoObj = (IMsoObjectIf)e.getSource();
			IMsoFeatureLayer msoLayer = mapManager.getMsoLayer(msoObj.getMsoClassCode());
			IMsoFeatureClass fc = (IMsoFeatureClass)msoLayer.getFeatureClass();
			IMsoFeature feature = fc.getFeature(msoObj.getObjectId());
			if (feature != null && feature.getExtent() != null) {
				IEnvelope extent = feature.getExtent();
				partialRefresh(extent);
			}
			
		} catch (AutomationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
	}

	public boolean hasInterestIn(IMsoObjectIf aMsoObject) {
		return myInterests.contains(aMsoObject.getMsoClassCode());
	}
	
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#addDiskoMapEventListener(org.redcross.sar.event.IDiskoMapEventListener)
	 */
	public void addDiskoMapEventListener(IDiskoMapEventListener listener) {
		if (listeners.indexOf(listener) == -1) {
			listeners.add(listener);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#removeDiskoMapEventListener(org.redcross.sar.event.IDiskoMapEventListener)
	 */
	public void removeDiskoMapEventListener(IDiskoMapEventListener listener) {
		listeners.remove(listener);
	}
	
	protected void fireEditLayerChanged() {
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).editLayerChanged(diskoMapEvent);
		}
	}
	
	protected void fireOnMapReplaced() {
		for (int i = 0; i < listeners.size(); i++) {
			try {
				listeners.get(i).onMapReplaced(diskoMapEvent);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected void fireOnAfterScreenDraw() {
		for (int i = 0; i < listeners.size(); i++) {
			try {
				listeners.get(i).onAfterScreenDraw(diskoMapEvent);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected void fireOnExtentUpdated() {
		for (int i = 0; i < listeners.size(); i++) {
			try {
				listeners.get(i).onExtentUpdated(diskoMapEvent);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected void fireOnSelectionChanged() {
		for (int i = 0; i < listeners.size(); i++) {
			try {
				listeners.get(i).onSelectionChanged(diskoMapEvent);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#setCurrentToolByRef(com.esri.arcgis.systemUI.ITool)
	 */
	public void setCurrentToolByRef(ITool tool) 
			throws IOException, AutomationException {
		super.setCurrentToolByRef(tool);
		if (currentTool != null && currentTool instanceof IDiskoTool) {
			currentTool.toolDeactivated();
		}
		if (tool instanceof IDiskoTool) {
			currentTool = (IDiskoTool)tool;
			currentTool.toolActivated();
		}	
		else {
			currentTool = null;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#getSnapLayerSelectionModel()
	 */
	public SnapLayerSelectionModel getSnapLayerSelectionModel() 
			throws IOException, AutomationException {
		if (snapLayerSelectionModel == null) {
			snapLayerSelectionModel = new SnapLayerSelectionModel(this);
		}
		return snapLayerSelectionModel;
	}
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#getClipLayerSelectionModel()
	 */
	public ClipLayerSelectionModel getClipLayerSelectionModel() 
			throws IOException, AutomationException {
		if (clipLayerSelectionModel == null) {
			clipLayerSelectionModel = new ClipLayerSelectionModel(this);
		}
		return clipLayerSelectionModel;
	}
	
	public IDiskoMapManager getMapManager() {
		return mapManager;
	}

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#getSelection()
	 */
	public Feature[] getSelection() throws IOException, AutomationException {
		Feature[] selection = null;
		int count = getMap().getSelectionCount();
		if (count > 0) {
			selection = new Feature[count];
			int index = 0;
			for (int i = 0; i < getLayerCount(); i++) {
				ILayer layer = getLayer(i);
				if (layer instanceof FeatureLayer) {
					FeatureLayer flayer = (FeatureLayer)layer;
					IEnumIDs enumID = flayer.getSelectionSet().getIDs();
					int oid = enumID.next();
					while (oid != -1) {
						selection[index] = (Feature)flayer.getFeatureClass().getFeature(oid);
						index++;
						oid = enumID.next();
					}
				}
			}	
		}
		return selection;
	}
	
	public IEnvelope getSelectionExtent() throws IOException, AutomationException {
		Feature[] selection = getSelection();
		if (selection != null && selection.length > 0) {
			IEnvelope env = new Envelope();
			for (int i = 0; i < selection.length; i++) {
				env.union(selection[i].getExtent());
			}
			return env;
		}
		return null;
	}
	
	public void zoomToSelected() throws IOException, AutomationException {
		IEnvelope env = getSelectionExtent();
		if (env != null) {
			setExtent(env);
		}
	}
	
	public void zoomToFeature(IFeature feature) throws IOException, AutomationException {
		setExtent(feature.getExtent());
	}
	
	
	public void zoomToMsoObject(IMsoObjectIf msoObject) throws IOException, AutomationException {
		if (msoObject instanceof IAssignmentIf) {
			IAssignmentIf assignment = (IAssignmentIf)msoObject;
			msoObject = assignment.getPlannedArea();
		}
		if (msoObject != null) {
			IMsoFeatureLayer msoLayer = getMapManager().getMsoLayer(msoObject.getMsoClassCode());
			if (msoLayer != null)  {
				IMsoFeatureClass msoFC = (IMsoFeatureClass)msoLayer.getFeatureClass();
				IMsoFeature msoFeature = msoFC.getFeature(msoObject.getObjectId());
				zoomToFeature(msoFeature);
			}
		}
	}
	
	public void setSelected(IMsoObjectIf msoObject, boolean selected) 
			throws IOException, AutomationException {
		if (msoObject instanceof IAssignmentIf) {
			IAssignmentIf assignment = (IAssignmentIf)msoObject;
			msoObject = assignment.getPlannedArea();
		}
		if (msoObject != null) {
			IMsoFeatureLayer msoLayer = getMapManager().getMsoLayer(msoObject.getMsoClassCode());
			if (msoLayer != null)  {
				IMsoFeatureClass msoFC = (IMsoFeatureClass)msoLayer.getFeatureClass();
				msoFC.clearSelected();
				IMsoFeature msoFeature = msoFC.getFeature(msoObject.getObjectId());
				msoFC.setSelected(msoFeature, selected);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#setSelected(java.lang.String, java.lang.String, java.lang.Object)
	 */
	public void setSelected(String layerName, String fieldName, Object value) 
			throws IOException, AutomationException {
		setSelected(getFeatureLayer(layerName), fieldName, value);
	}
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#setSelected(com.esri.arcgis.carto.FeatureLayer, java.lang.String, java.lang.Object)
	 */
	public void setSelected(FeatureLayer layer, String fieldName, Object value) 
			throws IOException, AutomationException {
		String whereclause = "\""+fieldName+"\"=";
		if (value instanceof String) {
			whereclause += "'"+(String)value+"'";
		}
		else if (value instanceof Number) {
			whereclause += value.toString();
		}
		setSelected(layer, whereclause);
	}

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#setSelected(com.esri.arcgis.carto.FeatureLayer, java.lang.String)
	 */
	public void setSelected(FeatureLayer layer, String whereclause) 
			throws IOException, AutomationException {
		QueryFilter queryFilter = new QueryFilter();
		queryFilter.setWhereClause(whereclause);
		layer.selectFeatures(queryFilter,com.esri.arcgis.carto.
				esriSelectionResultEnum.esriSelectionResultNew, false);
	}
	
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#getFeatureLayer(java.lang.String)
	 */
	public FeatureLayer getFeatureLayer(String name) 
			throws IOException, AutomationException {
		for (int i = 0; i < getLayerCount(); i++) {
			ILayer layer = getLayer(i);
			if (layer instanceof FeatureLayer && layer.getName().equals(name)) {
				return (FeatureLayer)layer;
			}
		}	
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#deleteSelected()
	 */
	public void deleteSelected() throws IOException, AutomationException {
		Feature[] selection = getSelection();
		for (int i = 0; i < selection.length; i++) {
			selection[i].delete();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#partialRefresh(com.esri.arcgis.geometry.IEnvelope)
	 */
	public void partialRefresh(IEnvelope env) throws IOException, AutomationException {
		if (env != null) {
			getActiveView().partialRefresh(
					com.esri.arcgis.carto.esriViewDrawPhase.esriViewGeography, 
					null, env);
		}
		else {
			getActiveView().refresh();
		}
	}
}
