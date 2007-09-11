package org.redcross.sar.map;

import com.esri.arcgis.display.IDisplayTransformation;
import com.esri.arcgis.geodatabase.*;
import com.esri.arcgis.geometry.*;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.systemUI.ICommand;
import com.esri.arcgis.systemUI.ITool;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.map.layer.OperationAreaLayer;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Properties;

public abstract class AbstractCommandTool implements ICommand, ITool, IDiskoTool {

	protected DiskoMap map = null;
	protected DiskoDialog dialog = null;
	protected Properties properties = null;
	protected IDisplayTransformation transform = null;
	protected OperationAreaLayer opAreaLayer = null;

	protected IDisplayTransformation getTransform()
			throws IOException, AutomationException {
		if (transform == null) {
			transform = map.getActiveView().getScreenDisplay().
				getDisplayTransformation();
		}
		return transform;
	}

	protected Point transform(int x, int y) throws IOException, AutomationException {
		return (Point)getTransform().toMapPoint(x,y);
	}

	protected void transform(Point p) throws IOException, AutomationException {
		p.transform(com.esri.arcgis.geometry.esriTransformDirection.esriTransformReverse, getTransform());
	}

	public void toolActivated() throws IOException, AutomationException {
		if (dialog != null) {
			dialog.setVisible(!dialog.isVisible());
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

	public DiskoMap getMap() {
		return map;
	}

	protected IFeature search(IFeatureClass fc, IPoint p) throws UnknownHostException, IOException {
		IEnvelope env = MapUtil.getEnvelope(p, map.getActiveView().getExtent().getWidth()/50);
		ISpatialFilter filter = new SpatialFilter();
		filter.setGeometryByRef(env);
		IFeatureCursor cursor = fc.search(filter, false);
		IFeature feature = cursor.nextFeature();
		// return last feature
		/*while (feature != null) {
			feature = cursor.nextFeature();
		}*/
		return feature;
	}

	protected int getGeomIndex(GeometryBag geomBag, IPoint p) throws AutomationException, IOException {
		IEnvelope env = MapUtil.getEnvelope(p, map.getActiveView().getExtent().getWidth()/50);
		for (int i = 0; i < geomBag.getGeometryCount(); i++) {
			IRelationalOperator relOp = (IRelationalOperator)geomBag.getGeometry(i);
			if (!relOp.disjoint(env)) {
				return i;
			}
		}
		return -1;
	}

//CMR	protected GeoList cloneGeoList(GeoList oldList) {
//CMR		GeoList newColl = new GeoList(null);
//CMR		if (oldList != null) {
//CMR			Iterator iter = oldList.getPositions().iterator();
//CMR			while (iter.hasNext()) {
//CMR				newColl.add((IGeodataIf)iter.next());
//CMR			}
//CMR		}
//CMR		return newColl;
//CMR	}

	protected boolean insideOpArea(IPoint point) throws AutomationException, IOException {
		boolean flag = false;
		IFeatureClass fc = opAreaLayer.getFeatureClass();
		for (int i = 0; i < fc.featureCount(null); i++) {
			Polygon polygon = (Polygon)fc.getFeature(i).getShape();
			if (polygon.contains(point)) {
				flag = true;
			}
		}
		return flag;
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
}
