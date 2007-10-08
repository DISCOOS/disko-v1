package org.redcross.sar.map;

import com.esri.arcgis.beans.map.MapBean;
import com.esri.arcgis.carto.*;
import com.esri.arcgis.controls.IMapControlEvents2Adapter;
import com.esri.arcgis.controls.IMapControlEvents2OnMapReplacedEvent;
import com.esri.arcgis.geodatabase.Feature;
import com.esri.arcgis.geodatabase.IEnumIDs;
import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geodatabase.QueryFilter;
import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.IEnvelope;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.systemUI.ITool;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.MsoFeatureClass;
import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.Update;

import javax.swing.*;
import javax.swing.border.SoftBevelBorder;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

/**
 * This calls extends AbstractDiskoApUi to provide userinterface for all map
 * related tasks (arbeidsprosesser)
 * @author geira
 *
 */
public final class DiskoMap extends MapBean implements IDiskoMap, IMsoUpdateListenerIf {

	private static final long serialVersionUID = 1L;
	private static final double pixelSize = 0.000377;//meter
	private String mxdDoc = null;
	private IDiskoMapManager mapManager = null;
	private MsoLayerSelectionModel msoLayerSelectionModel = null;
	private WMSLayerSelectionModel wmsLayerSelectionModel = null;
	private DefaultMapLayerSelectionModel defaultMapLayerSelectionModel = null;
	private IDiskoTool currentTool = null;
	private boolean supressDrawing = false;
	protected EnumSet<IMsoManagerIf.MsoClassCode> myInterests = null;

	/**
	 * Default constructor
	 */
	public DiskoMap(String mxdDoc, IDiskoMapManager mapManager, IMsoModelIf msoModel)
			throws IOException, AutomationException {
		this.mxdDoc = mxdDoc;
		this.mapManager = mapManager;

		myInterests = EnumSet.of(IMsoManagerIf.MsoClassCode.CLASSCODE_AREA);
		myInterests.add(IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA);
		myInterests.add(IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA);
		myInterests.add(IMsoManagerIf.MsoClassCode.CLASSCODE_POI);
        myInterests.add(IMsoManagerIf.MsoClassCode.CLASSCODE_ROUTE);
        myInterests.add(IMsoManagerIf.MsoClassCode.CLASSCODE_TRACK);
		IMsoEventManagerIf msoEventManager = msoModel.getEventManager();
		msoEventManager.addClientUpdateListener(this);
		initialize();
	}
	
	/**
	 * Default empty constructor
	 */
	public DiskoMap(){
		
	}

	private void initialize() throws IOException, AutomationException {
		setName("diskoMap");
		setShowScrollbars(false);
		setBorderStyle(com.esri.arcgis.controls.esriControlsBorderStyle.esriNoBorder);
		setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));

        //setDocumentFilename(mxdDoc);
		loadMxFile(mxdDoc, null, null);
		initLayers();

		// listen to do actions when the map is loaded
		addIMapControlEvents2Listener(new IMapControlEvents2Adapter() {
			private static final long serialVersionUID = 1L;
			public void onMapReplaced(IMapControlEvents2OnMapReplacedEvent e)
                   	throws java.io.IOException, AutomationException {
				initLayers();
			}
		});
	}

	private void initLayers() throws java.io.IOException, AutomationException {
		// add custom layers
		IMap focusMap = getActiveView().getFocusMap();
		List customLayers = mapManager.getMsoLayers();
		for (int i = 0; i < customLayers.size(); i++) {
			IFeatureLayer layer = (IFeatureLayer)customLayers.get(i);
			layer.setSpatialReferenceByRef(getSpatialReference());
			focusMap.addLayer(layer);
			layer.setCached(true);
		}
		// reactivate
		/*getActiveView().deactivate();
				getActiveView().activate(getHWnd());
				getActiveView().refresh();*/

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
		if (supressDrawing) {
			return;
		}
		IMsoObjectIf msoObj = (IMsoObjectIf)e.getSource();
		List msoLayers = mapManager.getMsoLayers(msoObj.getMsoClassCode());
		IMsoFeatureLayer flayer = (IMsoFeatureLayer)msoLayers.get(0);
		if (flayer.isDirty()) {
			refreshLayer(flayer, null);
		}
	}

	public void refreshMsoLayers() {
		List msoLayers = mapManager.getMsoLayers();
		for (int i = 0; i < msoLayers.size(); i++) {
			IMsoFeatureLayer flayer = (IMsoFeatureLayer)msoLayers.get(i);
			if (flayer.isDirty()) {
				refreshLayer(flayer, null);
			}
		}
	}

	public void refreshMsoLayers(IMsoManagerIf.MsoClassCode classCode) {
		List msoLayers = mapManager.getMsoLayers(classCode);
		for (int i = 0; i < msoLayers.size(); i++) {
			IMsoFeatureLayer flayer = (IMsoFeatureLayer)msoLayers.get(i);
			if (flayer.isDirty()) {
				refreshLayer(flayer, null);
			}
		}
	}

	public void setSupressDrawing(boolean supress) {
		supressDrawing = supress;
	}

	public boolean hasInterestIn(IMsoObjectIf aMsoObject) {
		return myInterests.contains(aMsoObject.getMsoClassCode());
	}

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#setCurrentToolByRef(com.esri.arcgis.systemUI.ITool)
	 */
	public void setCurrentToolByRef(ITool tool)
			throws IOException, AutomationException {
		super.setCurrentToolByRef(tool);
		setActiveTool(tool);
	}

	public void setActiveTool(ITool tool)
			throws IOException, AutomationException {
		if (currentTool instanceof IDiskoTool &&
				currentTool != null && tool != currentTool) {
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
	 * @see org.redcross.sar.map.IDiskoMap#getClipLayerSelectionModel()
	 */
	public void setMsoLayerSelectionModel()
		throws IOException, AutomationException{
		msoLayerSelectionModel = new MsoLayerSelectionModel(this);
	}

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#getClipLayerSelectionModel()
	 */
	public MsoLayerSelectionModel getMsoLayerSelectionModel()
			throws IOException, AutomationException {
		if (msoLayerSelectionModel == null) {
			msoLayerSelectionModel = new MsoLayerSelectionModel(this);
		}
		return msoLayerSelectionModel;
	}

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#getClipLayerSelectionModel()
	 */
	public void setWMSLayerSelectionModel()
		throws IOException, AutomationException {
		wmsLayerSelectionModel = new WMSLayerSelectionModel(this);
	}

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#getClipLayerSelectionModel()
	 */
	public WMSLayerSelectionModel getWMSLayerSelectionModel()
			throws IOException, AutomationException {
		if (wmsLayerSelectionModel == null) {
			wmsLayerSelectionModel = new WMSLayerSelectionModel(this);
		}
		return wmsLayerSelectionModel;
	}

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#getClipLayerSelectionModel()
	 */
	public void setDefaultMapLayerSelectionModel()
		throws IOException, AutomationException {
		defaultMapLayerSelectionModel = new DefaultMapLayerSelectionModel(this);
	}

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#getClipLayerSelectionModel()
	 */
	public DefaultMapLayerSelectionModel getDefaultMapLayerSelectionModel()
			throws IOException, AutomationException {
		if (defaultMapLayerSelectionModel == null) {
			defaultMapLayerSelectionModel = new DefaultMapLayerSelectionModel(this);
		}
		return defaultMapLayerSelectionModel;
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
			IEnvelope env = null;
			List msoLayers = mapManager.getMsoLayers(msoObject.getMsoClassCode());
			for (int i = 0; i < msoLayers.size(); i++) {
				IFeatureLayer flayer = (IFeatureLayer)msoLayers.get(i);
				MsoFeatureClass msoFC = (MsoFeatureClass)flayer.getFeatureClass();
				IMsoFeature msoFeature = msoFC.getFeature(msoObject.getObjectId());
				IEnvelope extent = msoFeature.getExtent();
				if (extent != null) {
					//her bør det legges inn en extent forstørrer slik at det blir litt luft/kart rundt objectet
					if (env == null)
						env = extent.getEnvelope();
					else env.union(extent);
				}
			}
			if (env != null) {
				setExtent(env);
			}
		}
	}
	
	public void zoomToPrintMapExtent(IMsoObjectIf msoObject, double scale, int pixHeigth, int pixWidth) throws IOException, AutomationException {
		if (msoObject instanceof IAssignmentIf) {
			IAssignmentIf assignment = (IAssignmentIf)msoObject;
			msoObject = assignment.getPlannedArea();			
		}
		if (msoObject != null) {
			IEnvelope env = null;
			List msoLayers = mapManager.getMsoLayers(msoObject.getMsoClassCode());
			for (int i = 0; i < msoLayers.size(); i++) {
				IFeatureLayer flayer = (IFeatureLayer)msoLayers.get(i);
				MsoFeatureClass msoFC = (MsoFeatureClass)flayer.getFeatureClass();
				IMsoFeature msoFeature = msoFC.getFeature(msoObject.getObjectId());
				IEnvelope extent = msoFeature.getExtent();
				//må kalkulere et extent som gir gitt skala
				double xMax = extent.getXMax();
				double yMax = extent.getYMax();
				double xMin = extent.getXMin();
				double yMin = extent.getYMin();
				System.out.println("looper "+i+", extent.getXMax(): "+extent.getXMax() + ", extent.getYMax(): "+extent.getYMax() + ", extent.getXMin(): "+extent.getXMin() + "extent.getYMin(): "+extent.getYMin());
				double deltaX = (pixWidth*pixelSize)*scale;
				double deltaY = (pixWidth*pixelSize)*scale;
				double centerX = xMin + (xMax-xMin)/2;
				double centerY = yMin + (yMax-yMin)/2;
				System.out.println("deltaX: "+deltaX + ", deltaY: "+deltaY);		
				extent.setXMax(centerX+deltaX/2);
				extent.setXMin(centerX-deltaX/2);
				extent.setYMax(centerY+deltaY/2);
				extent.setYMin(centerY-deltaY/2);
				System.out.println("extent.getXMax(): "+extent.getXMax() + ", extent.getYMax(): "+extent.getYMax() + ", extent.getXMin(): "+extent.getXMin() + "extent.getYMin(): "+extent.getYMin());
				
				if (extent != null) {
					
					if (env == null)
						env = extent.getEnvelope();
					else env.union(extent);
				}
			}
			if (env != null) {
				setExtent(env);
			}
		}
	}
		
	public void zoomToPrintMapExtent(IMsoObjectIf msoObject, double scale, double mapPrintHeigthSize, double mapPrintWidthSize) throws IOException, AutomationException {
		if (msoObject instanceof IAssignmentIf) {
			IAssignmentIf assignment = (IAssignmentIf)msoObject;
			msoObject = assignment.getPlannedArea();
		}
		if (msoObject != null) {
			IEnvelope env = null;
			List msoLayers = mapManager.getMsoLayers(msoObject.getMsoClassCode());
			
			for (int i = 0; i < msoLayers.size(); i++) {
				IFeatureLayer flayer = (IFeatureLayer)msoLayers.get(i);
				MsoFeatureClass msoFC = (MsoFeatureClass)flayer.getFeatureClass();
				IMsoFeature msoFeature = msoFC.getFeature(msoObject.getObjectId());
				IEnvelope extent = msoFeature.getExtent();
				
				//må kalkulere et extent som gir gitt skala				
				System.out.println("looper "+i+", extent.getXMax(): "+extent.getXMax() + ", extent.getYMax(): "+extent.getYMax() + ", extent.getXMin(): "+extent.getXMin() + "extent.getYMin(): "+extent.getYMin());				
				double centerX = extent.getXMin() + (extent.getXMax()-extent.getXMin())/2;
				double centerY = extent.getYMin() + (extent.getYMax()-extent.getYMin())/2;
				
				
				double deltaX = mapPrintWidthSize * scale;
				double deltaY = mapPrintHeigthSize * scale;
				
				System.out.println("deltaX: "+deltaX + ", deltaY: "+deltaY);		
				extent.setXMax(centerX+deltaX/2);
				extent.setXMin(centerX-deltaX/2);
				extent.setYMax(centerY+deltaY/2);
				extent.setYMin(centerY-deltaY/2);
				System.out.println("extent.getXMax(): "+extent.getXMax() + ", extent.getYMax(): "+extent.getYMax() + ", extent.getXMin(): "+extent.getXMin() + "extent.getYMin(): "+extent.getYMin());
				
				if (extent != null) {
					
					if (env == null)
						env = extent.getEnvelope();
					else env.union(extent);
				}
			}
			if (env != null) {
				setExtent(env);
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
			List msoLayers = mapManager.getMsoLayers(msoObject.getMsoClassCode());
			for (int i = 0; i < msoLayers.size(); i++) {
				IMsoFeatureLayer flayer = (IMsoFeatureLayer)msoLayers.get(i);
				MsoFeatureClass msoFC = (MsoFeatureClass)flayer.getFeatureClass();
				flayer.clearSelected();
				IMsoFeature msoFeature = msoFC.getFeature(msoObject.getObjectId());
				flayer.setSelected(msoFeature, selected);
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
		// get map in focus
		IMap m = getActiveView().getFocusMap();
		for (int i = 0; i < m.getLayerCount(); i++) {
			ILayer layer = m.getLayer(i);
			if (layer instanceof FeatureLayer && layer.getName().equalsIgnoreCase(name)) {
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
	 * @see org.redcross.sar.map.IDiskoMap#refreshLayer(com.esri.arcgis.geometry.IEnvelope)
	 */
	public void refreshLayer(final Object obj, final IEnvelope env) {
		Runnable r = new Runnable() {
			public void run() {
				try {
					getActiveView().partialRefresh(
							esriViewDrawPhase.esriViewGeography, obj, env);
				} catch (AutomationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		SwingUtilities.invokeLater(r);
	}

	public String getMxdDoc() {
		return mxdDoc;
	}
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#refreshSelection(com.esri.arcgis.geometry.IEnvelope)
	 */
	public void refreshSelection(final Object obj, final IEnvelope env) {
		Runnable r = new Runnable() {
			public void run() {
				try {
					getActiveView().partialRefresh(
							esriViewDrawPhase.esriViewGraphics, obj, env);
				} catch (AutomationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		SwingUtilities.invokeLater(r);
	}
}
