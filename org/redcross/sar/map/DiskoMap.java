package org.redcross.sar.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.border.SoftBevelBorder;

import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.event.IDiskoMapEventListener;
import org.redcross.sar.gui.ClipLayerSelectionModel;
import org.redcross.sar.gui.SnapLayerSelectionModel;

import com.esri.arcgis.beans.map.MapBean;
import com.esri.arcgis.carto.FeatureLayer;
import com.esri.arcgis.carto.IActiveView;
import com.esri.arcgis.carto.IElement;
import com.esri.arcgis.carto.IElementProperties;
import com.esri.arcgis.carto.IEnumElement;
import com.esri.arcgis.carto.IFeatureLayerSelectionEventsAdapter;
import com.esri.arcgis.carto.IFeatureLayerSelectionEventsFeatureLayerSelectionChangedEvent;
import com.esri.arcgis.carto.IGraphicsContainer;
import com.esri.arcgis.carto.ILayer;
import com.esri.arcgis.controls.IMapControlEvents2Adapter;
import com.esri.arcgis.controls.IMapControlEvents2OnAfterDrawEvent;
import com.esri.arcgis.controls.IMapControlEvents2OnExtentUpdatedEvent;
import com.esri.arcgis.controls.IMapControlEvents2OnMapReplacedEvent;
import com.esri.arcgis.geodatabase.Feature;
import com.esri.arcgis.geodatabase.IEnumIDs;
import com.esri.arcgis.geodatabase.QueryFilter;
import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.IEnvelope;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.systemUI.ITool;

/**
 * This calls extends AbstractDiskoApUi to provide userinterface for all map 
 * related tasks (arbeidsprosesser)
 * @author geira
 *
 */
public final class DiskoMap extends MapBean implements IDiskoMap {
	
	private static final long serialVersionUID = 1L;
	private String mxdDoc = null;
	private IDiskoMapManager mapManager = null;
	private SnapLayerSelectionModel snapLayerSelectionModel = null;
	private ClipLayerSelectionModel clipLayerSelectionModel = null;
	private IDiskoTool currentTool = null;
	private ArrayList<IDiskoMapEventListener> listeners = null;
	private DiskoMapEvent diskoMapEvent = null;

	/**
	 * Default constructor
	 */
	public DiskoMap(String mxdDoc, IDiskoMapManager mapManager)  
			throws IOException, AutomationException {
		this.mxdDoc = mxdDoc;
		this.mapManager = mapManager;
		listeners = new ArrayList<IDiskoMapEventListener>();
		diskoMapEvent = new DiskoMapEvent(this);
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
		// set all featurelayers not selectabel
		for (int i = 0; i < getLayerCount(); i++) {
			ILayer layer = getLayer(i);
			if (layer instanceof FeatureLayer) {
				FeatureLayer flayer = (FeatureLayer)layer;
				// implementers of subclasses must explesit set layers selectable
				flayer.setSelectable(false);
				// add layer selection listener, forward to DiskoMapEvent
				flayer.addIFeatureLayerSelectionEventsListener(new IFeatureLayerSelectionEventsAdapter() {
					private static final long serialVersionUID = 1L;
					public void featureLayerSelectionChanged(
							IFeatureLayerSelectionEventsFeatureLayerSelectionChangedEvent theEvent)
				    		throws java.io.IOException, AutomationException {
						fireOnSelectionChanged();
					}
				});
			}
		}	
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
	
	protected void fireGraphicsAdded() {
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).graphicsAdded(diskoMapEvent);
		}
	}
	
	protected void fireGraphicsDeleted() {
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).graphicsDeleted(diskoMapEvent);
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
	
	public void zoomToSelected() throws IOException, AutomationException {
		Feature[] selection = getSelection();
		if (selection != null && selection.length > 0) {
			IEnvelope env = new Envelope();
			for (int i = 0; i < selection.length; i++) {
				env.union(selection[i].getExtent());
			}
			setExtent(env);
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
	
	public void addGraphics(IElement elem) throws IOException, AutomationException {
		IGraphicsContainer graphics = getActiveView().getGraphicsContainer();
		graphics.addElement(elem, 0);
		fireGraphicsAdded();
	}

	public void deleteGraphics(IElement elem) throws IOException, AutomationException {
		IGraphicsContainer graphics = getActiveView().getGraphicsContainer();
		graphics.deleteElement(elem);
		fireGraphicsDeleted();
	}
	

	public void deleteAllGraphics() throws IOException, AutomationException {
		IGraphicsContainer graphics = getActiveView().getGraphicsContainer();
		graphics.deleteAllElements();
		fireGraphicsDeleted();
	}
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#searchGraphics(com.esri.arcgis.geometry.Point)
	 */
	public IElement searchGraphics(Point p) 
			throws IOException, AutomationException {

		IActiveView av = getActiveView();
		double tolerance = av.getExtent().getWidth()/50;
		IGraphicsContainer graphics = av.getGraphicsContainer();
		graphics.reset();
		IEnumElement enumElement = graphics.locateElements(p, tolerance);
		if (enumElement != null) {
			return enumElement.next();
		}
		return null;
	}
	
	public List searchGraphics(String name) 
			throws IOException, AutomationException {
		ArrayList result = new ArrayList();
		IGraphicsContainer graphics = getActiveView().getGraphicsContainer();
		graphics.reset();
		IElement elem = null;
		while ((elem = graphics.next()) != null) {
			if (elem instanceof IElementProperties && 
					((IElementProperties)elem).getName().equals(name)) {
				result.add(elem);
			}
		}
		return result;
	}
	
	public boolean hasGraphics(String name) 
			throws IOException, AutomationException {
		IGraphicsContainer graphics = getActiveView().getGraphicsContainer();
		graphics.reset();
		IElement elem = null;
		while ((elem = graphics.next()) != null) {
			if (elem instanceof IElementProperties && 
					((IElementProperties)elem).getName().equals(name)) {
				return true;
			}
		}
		return false;
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

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#partialRefreshGraphics(com.esri.arcgis.geometry.IEnvelope)
	 */
	public void partialRefreshGraphics(IEnvelope env) throws IOException, AutomationException {
		if (env == null) {
			env = getActiveView().getExtent();
		}
		getActiveView().partialRefresh(
				com.esri.arcgis.carto.esriViewDrawPhase.esriViewGraphics, 
				null, env);
	}
}
