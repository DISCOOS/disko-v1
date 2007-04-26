package org.redcross.sar.map;

import java.io.IOException;

import com.esri.arcgis.carto.IActiveView;
import com.esri.arcgis.carto.IElement;
import com.esri.arcgis.carto.IGraphicsContainer;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.interop.AutomationException;

/**
 * A custom draw tool.
 * @author geira
 *
 */
public class EraseTool extends AbstractCommandTool {

	private static final long serialVersionUID = 1L;
	private Point p = null;
	
	/**
	 * Constructs the DrawTool
	 */
	public EraseTool() throws IOException {
		p = new Point();
		p.setX(0);
		p.setY(0);
	}

	public void onCreate(Object obj) throws IOException, AutomationException {
		if (obj instanceof IDiskoMap) {
			map = (DiskoMap)obj;
		}
	}

	public void onMouseDown(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		p.setX(x);
		p.setY(y); 
		transform(p);
		IElement elem = map.searchGraphics(p);
		if (elem != null) {
			IActiveView av = map.getActiveView();
			IGraphicsContainer graphics = av.getGraphicsContainer();
			graphics.deleteElement(elem);
			map.partialRefreshGraphics(map.getActiveView().getExtent());
		}
	}
}
