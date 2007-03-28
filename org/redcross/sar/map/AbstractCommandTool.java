package org.redcross.sar.map;

import java.io.IOException;

import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.event.IDiskoMapEventListener;
import org.redcross.sar.gui.DiskoDialog;

import com.esri.arcgis.carto.FeatureLayer;
import com.esri.arcgis.display.IDisplayTransformation;
import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geodatabase.IFeatureCursor;
import com.esri.arcgis.geodatabase.SpatialFilter;
import com.esri.arcgis.geodatabase.esriSpatialRelEnum;
import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.systemUI.ICommand;
import com.esri.arcgis.systemUI.ITool;

public abstract class AbstractCommandTool implements ICommand, ITool, IDiskoTool, IDiskoMapEventListener {
	
	protected DiskoMap map = null;
	protected DiskoDialog dialog = null;
	
	protected Point transform(int x, int y) throws IOException {
		IDisplayTransformation transform = map.getActiveView().
			getScreenDisplay().getDisplayTransformation();
		return (Point)transform.toMapPoint(x,y);
	}
	
	protected IFeature search(FeatureLayer flayer, Envelope env) throws IOException {
		SpatialFilter spatialFilter = new SpatialFilter();
		spatialFilter.setGeometryByRef(env);
		spatialFilter.setGeometryField(flayer.getFeatureClass().getShapeFieldName());
		spatialFilter.setSpatialRel(esriSpatialRelEnum.esriSpatialRelIntersects);
		IFeatureCursor featureCursor = flayer.search(spatialFilter,false);
		// first hit will be returned
		return featureCursor.nextFeature();
	}
	
	public void toolActivated() throws IOException, AutomationException {
		if (dialog != null) {
			dialog.setVisible(true);
		}
	}
	
	public void toolDeactivated() throws IOException, AutomationException {
		if (dialog != null) {
			dialog.setVisible(false);
		}
	}
	
	public DiskoDialog getDialog() {
		return dialog;
	}

	public int getBitmap() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getCaption() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCategory() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getHelpContextID() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getHelpFile() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getMessage() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTooltip() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isChecked() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEnabled() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return false;
	}

	public void onClick() throws IOException, AutomationException {
		// TODO Auto-generated method stub

	}

	public void onCreate(Object arg0) throws IOException, AutomationException {
		// TODO Auto-generated method stub

	}

	public boolean deactivate() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return true;
	}

	public int getCursor() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean onContextMenu(int arg0, int arg1) throws IOException,
			AutomationException {
		// TODO Auto-generated method stub
		return false;
	}

	public void onDblClick() throws IOException, AutomationException {
		// TODO Auto-generated method stub

	}

	public void onKeyDown(int arg0, int arg1) throws IOException,
			AutomationException {
		// TODO Auto-generated method stub

	}

	public void onKeyUp(int arg0, int arg1) throws IOException,
			AutomationException {
		// TODO Auto-generated method stub

	}

	public void onMouseDown(int arg0, int arg1, int arg2, int arg3)
			throws IOException, AutomationException {
		// TODO Auto-generated method stub

	}

	public void onMouseMove(int arg0, int arg1, int arg2, int arg3)
			throws IOException, AutomationException {
		// TODO Auto-generated method stub

	}

	public void onMouseUp(int arg0, int arg1, int arg2, int arg3)
			throws IOException, AutomationException {
		// TODO Auto-generated method stub

	}

	public void refresh(int arg0) throws IOException, AutomationException {
		// TODO Auto-generated method stub

	}

	public void editLayerChanged(DiskoMapEvent e) {
		// TODO Auto-generated method stub

	}

	public void onAfterScreenDraw(DiskoMapEvent e) throws IOException {
		// TODO Auto-generated method stub

	}

	public void onExtentUpdated(DiskoMapEvent e) throws IOException {
		// TODO Auto-generated method stub

	}

	public void onMapReplaced(DiskoMapEvent e) throws IOException {
		// TODO Auto-generated method stub

	}
	
	public void onSelectionChanged(DiskoMapEvent e) throws IOException {
		// TODO Auto-generated method stub
		
	}
}
