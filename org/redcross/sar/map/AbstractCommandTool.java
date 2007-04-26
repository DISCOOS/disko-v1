package org.redcross.sar.map;

import java.io.IOException;
import java.util.Properties;
import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.event.IDiskoMapEventListener;
import org.redcross.sar.gui.DiskoDialog;
import com.esri.arcgis.display.IDisplayTransformation;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.systemUI.ICommand;
import com.esri.arcgis.systemUI.ITool;

public abstract class AbstractCommandTool implements ICommand, ITool, IDiskoTool, IDiskoMapEventListener {
	
	protected DiskoMap map = null;
	protected DiskoDialog dialog = null;
	protected Properties properties = null;
	protected IDisplayTransformation transform = null;
	
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

	public DiskoMap getMap() {
		return map;
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
