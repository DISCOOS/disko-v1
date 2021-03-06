package org.redcross.sar.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.IMsoObjectIf;

import com.esri.arcgis.carto.FeatureLayer;
import com.esri.arcgis.geodatabase.Feature;
import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geometry.IEnvelope;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.systemUI.ITool;

public interface IDiskoMap {

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#setCurrentToolByRef(com.esri.arcgis.systemUI.ITool)
	 */
	public void setCurrentToolByRef(ITool tool) throws IOException,
			AutomationException;
	
	public void setActiveTool(ITool tool) throws IOException, AutomationException;
	
	public List getMsoLayers();

	
	public List getMsoLayers(IMsoManagerIf.MsoClassCode classCode);
	
	public IMsoFeatureLayer getMsoLayer(IMsoFeatureLayer.LayerCode layerCode);
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#getClipLayerSelectionModel()
	 */
	public MsoLayerSelectionModel getMsoLayerSelectionModel()
			throws IOException, AutomationException;
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#getClipLayerSelectionModel()
	 */
	public void setMsoLayerSelectionModel()
			throws IOException, AutomationException;
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#getClipLayerSelectionModel()
	 */
	public WMSLayerSelectionModel getWMSLayerSelectionModel()
			throws IOException, AutomationException;
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#getClipLayerSelectionModel()
	 */
	public void setWMSLayerSelectionModel()
			throws IOException, AutomationException;
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#getClipLayerSelectionModel()
	 */
	public DefaultMapLayerSelectionModel getDefaultMapLayerSelectionModel()
			throws IOException, AutomationException;
	
	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#getClipLayerSelectionModel()
	 */
	public void setDefaultMapLayerSelectionModel()
			throws IOException, AutomationException;
	
	public IDiskoMapManager getMapManager();

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#getSelection()
	 */
	public Feature[] getSelection() throws IOException, AutomationException;
	
	public IEnvelope getSelectionExtent() throws IOException, AutomationException;

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#setSelected(java.lang.String, java.lang.String, java.lang.Object)
	 */
	public void setSelected(String layerName, String fieldName, Object value)
			throws IOException, AutomationException;

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#setSelected(com.esri.arcgis.carto.FeatureLayer, java.lang.String, java.lang.Object)
	 */
	public void setSelected(FeatureLayer layer, String fieldName, Object value)
			throws IOException, AutomationException;

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#setSelected(com.esri.arcgis.carto.FeatureLayer, java.lang.String)
	 */
	public void setSelected(FeatureLayer layer, String whereclause)
			throws IOException, AutomationException;
	
	public void zoomToSelected () throws IOException, AutomationException;
	
	public void zoomToFeature(IFeature feature) throws IOException, AutomationException;
	
	public void zoomToMsoObject(IMsoObjectIf msoObject) throws IOException, AutomationException;
	
	public void setSelected(IMsoObjectIf msoObject, boolean selected) throws IOException, AutomationException;

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#getFeatureLayer(java.lang.String)
	 */
	public FeatureLayer getFeatureLayer(String name) throws IOException,
			AutomationException;

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#deleteSelected()
	 */
	public void deleteSelected() throws IOException;

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#refreshLayer(com.esri.arcgis.geometry.IEnvelope)
	 */
	public void refreshLayer(Object obj, IEnvelope env) throws IOException,
			AutomationException;

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#refreshSelection(com.esri.arcgis.geometry.IEnvelope)
	 */
	public void refreshSelection(Object obj, IEnvelope env) throws IOException,
			AutomationException;
	
}