package org.redcross.sar.map;

import java.io.IOException;
import java.util.List;

import org.redcross.sar.event.IDiskoMapEventListener;
import org.redcross.sar.gui.ClipLayerSelectionModel;
import org.redcross.sar.gui.SnapLayerSelectionModel;
import com.esri.arcgis.carto.FeatureLayer;
import com.esri.arcgis.carto.IElement;
import com.esri.arcgis.geodatabase.Feature;
import com.esri.arcgis.geometry.IEnvelope;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.systemUI.ITool;

public interface IDiskoMap {

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#addDiskoMapEventListener(org.redcross.sar.event.IDiskoMapEventListener)
	 */
	public void addDiskoMapEventListener(IDiskoMapEventListener listener);

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#removeDiskoMapEventListener(org.redcross.sar.event.IDiskoMapEventListener)
	 */
	public void removeDiskoMapEventListener(IDiskoMapEventListener listener);

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#setCurrentToolByRef(com.esri.arcgis.systemUI.ITool)
	 */
	public void setCurrentToolByRef(ITool tool) throws IOException,
			AutomationException;

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#getSnapLayerSelectionModel()
	 */
	public SnapLayerSelectionModel getSnapLayerSelectionModel()
			throws IOException, AutomationException;

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#getClipLayerSelectionModel()
	 */
	public ClipLayerSelectionModel getClipLayerSelectionModel()
			throws IOException, AutomationException;

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#getSelection()
	 */
	public Feature[] getSelection() throws IOException, AutomationException;

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
	 * @see org.redcross.sar.map.IDiskoMap#searchGraphics(com.esri.arcgis.geometry.Point)
	 */
	public IElement searchGraphics(Point p) throws IOException,
			AutomationException;
	
	public List searchGraphics(String name) 
			throws IOException, AutomationException;

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#partialRefresh(com.esri.arcgis.geometry.IEnvelope)
	 */
	public void partialRefresh(IEnvelope env) throws IOException,
			AutomationException;

	/* (non-Javadoc)
	 * @see org.redcross.sar.map.IDiskoMap#partialRefreshGraphics(com.esri.arcgis.geometry.IEnvelope)
	 */
	public void partialRefreshGraphics(IEnvelope env) throws IOException,
			AutomationException;

}