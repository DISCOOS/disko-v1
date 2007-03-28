package org.redcross.sar.map;

import java.io.IOException;
import java.util.ArrayList;
import javax.swing.border.SoftBevelBorder;

import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.event.IDiskoMapEventListener;
import org.redcross.sar.gui.ClipLayerSelectionModel;
import org.redcross.sar.gui.SnapLayerSelectionModel;

import com.esri.arcgis.beans.map.MapBean;
import com.esri.arcgis.carto.FeatureLayer;
import com.esri.arcgis.carto.IFeatureLayerSelectionEventsAdapter;
import com.esri.arcgis.carto.IFeatureLayerSelectionEventsFeatureLayerSelectionChangedEvent;
import com.esri.arcgis.carto.ILayer;
import com.esri.arcgis.controls.IMapControlEvents2Adapter;
import com.esri.arcgis.controls.IMapControlEvents2OnAfterDrawEvent;
import com.esri.arcgis.controls.IMapControlEvents2OnExtentUpdatedEvent;
import com.esri.arcgis.controls.IMapControlEvents2OnMapReplacedEvent;
import com.esri.arcgis.geodatabase.Feature;
import com.esri.arcgis.geodatabase.IEnumIDs;
import com.esri.arcgis.geodatabase.QueryFilter;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.systemUI.ITool;

/**
 * This calls extends AbstractDiskoApUi to provide userinterface for all map 
 * related tasks (arbeidsprosesser)
 * @author geira
 *
 */
public final class DiskoMap extends MapBean {
	
	private static final long serialVersionUID = 1L;
	private String mxdDoc = null;
	private boolean editable = false;
	private FeatureLayer editLayer = null;
	private SnapLayerSelectionModel snapLayerSelectionModel = null;
	private ClipLayerSelectionModel clipLayerSelectionModel = null;
	private IDiskoTool currentTool = null;
	private ArrayList<IDiskoMapEventListener> listeners = null;
	private DiskoMapEvent diskoMapEvent = null;

	/**
	 * Default constructor
	 */
	public DiskoMap(String mxdDoc)  throws IOException {
		this.mxdDoc = mxdDoc;
		listeners = new ArrayList<IDiskoMapEventListener>();
		diskoMapEvent = new DiskoMapEvent(this);
		initialize();
	}
	
	private void initialize() throws IOException {
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
	
	private void mapLoaded() throws java.io.IOException {
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
	
	
	
	public void addDiskoMapEventListener(IDiskoMapEventListener listener) {
		if (listeners.indexOf(listener) == -1) {
			listeners.add(listener);
		}
	}
	
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
	
	public void setCurrentToolByRef(ITool tool) 
			throws IOException, AutomationException{
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
	
	public SnapLayerSelectionModel getSnapLayerSelectionModel() throws IOException {
		if (snapLayerSelectionModel == null) {
			snapLayerSelectionModel = new SnapLayerSelectionModel(this);
		}
		return snapLayerSelectionModel;
	}
	
	public ClipLayerSelectionModel getClipLayerSelectionModel() throws IOException {
		if (clipLayerSelectionModel == null) {
			clipLayerSelectionModel = new ClipLayerSelectionModel(this);
		}
		return clipLayerSelectionModel;
	}
	
	
	public void setEditLayer(FeatureLayer editLayer) {
		this.editLayer = editLayer;
		fireEditLayerChanged();
	}
	
	public FeatureLayer getEditLayer() {
		return editLayer;
	}
	
	public void setIsEditable(boolean editable) {
		this.editable = editable;
	}
	
	public boolean isEditable() {
		return editable;
	}

	public Feature[] getSelection() throws IOException {
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
	
	public void setSelected(String layerName, String fieldName, Object value) throws IOException {
		setSelected(getFeatureLayer(layerName), fieldName, value);
	}
	
	
	public void setSelected(FeatureLayer layer, String fieldName, Object value) throws IOException {
		String whereclause = "["+fieldName+"] =";
		if (value instanceof String) {
			whereclause += "'"+(String)value+"'";
		}
		else if (value instanceof Number) {
			whereclause += value.toString();
		}
		setSelected(layer, whereclause);
	}

	public void setSelected(FeatureLayer layer, String whereclause) throws IOException {
		QueryFilter queryFilter = new QueryFilter();
		queryFilter.setWhereClause(whereclause);
		layer.selectFeatures(queryFilter,com.esri.arcgis.carto.
				esriSelectionResultEnum.esriSelectionResultNew, false);
	}
	
	
	/**
	 * Gets a FeatureLayer with the given name
	 * @param name The name of a FeatureLayer
	 * @return A FeatureLayer
	 */
	public FeatureLayer getFeatureLayer(String name) throws IOException {
		for (int i = 0; i < getLayerCount(); i++) {
			ILayer layer = getLayer(i);
			if (layer instanceof FeatureLayer && layer.getName().equals(name)) {
				return (FeatureLayer)layer;
			}
		}	
		return null;
	}
	
	public void deleteSelected() throws IOException {
		Feature[] selection = getSelection();
		for (int i = 0; i < selection.length; i++) {
			selection[i].delete();
		}
	}
}
