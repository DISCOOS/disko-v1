package org.redcross.sar.map;

import java.awt.Toolkit;
import java.io.IOException;
import com.esri.arcgis.carto.IActiveView;
import com.esri.arcgis.carto.IElement;
import com.esri.arcgis.carto.IGraphicsContainer;
import com.esri.arcgis.carto.LineElement;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.geometry.Polyline;
import com.esri.arcgis.interop.AutomationException;

/**
 * A custom draw tool.
 * @author geira
 *
 */
public class SplitTool extends AbstractCommandTool {

	private static final long serialVersionUID = 1L;
	private Point p = null;
	
	/**
	 * Constructs the DrawTool
	 */
	public SplitTool() throws IOException {
		p = new Point();
		p.setX(0);
		p.setY(0);
	}

	public void onCreate(Object obj) throws IOException, AutomationException {
		if (obj instanceof DiskoMap) {
			map = (DiskoMap)obj;
		}
	}

	public void onMouseDown(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		p.setX(x);
		p.setY(y); 
		transform(p);
		IElement elem = map.searchGraphics(p);
		if (elem != null && elem instanceof LineElement) {
			IActiveView av = map.getActiveView();
			IGraphicsContainer graphics = av.getGraphicsContainer();
			Polyline pl = (Polyline)elem.getGeometry();
			// splitting and adding new elements
			split(graphics, pl, p);
			// delete the orginal element
			graphics.deleteElement(elem);
		}
	}
	
	private void split(IGraphicsContainer graphics, Polyline orginal, Point nearPoint) 
			throws IOException, AutomationException {
		
		boolean[] splitHappened = new boolean[2];
		int[] newPartIndex = new int[2];
		int[] newSegmentIndex = new int[2];
		orginal.splitAtPoint(nearPoint, true, true, splitHappened, 
				newPartIndex, newSegmentIndex);
		// two new polylines
		Polyline pline1 = new Polyline();
		pline1.addGeometry(orginal.getGeometry(newPartIndex[0]), null, null);
		Polyline pline2 = new Polyline();
		pline2.addGeometry(orginal.getGeometry(newPartIndex[1]), null, null);
		
		LineElement le1 = new LineElement();
		le1.setGeometry(pline1);
		graphics.addElement(le1, 0);
		
		LineElement le2 = new LineElement();
		le2.setGeometry(pline2);
		graphics.addElement(le2, 0);
		
		Toolkit.getDefaultToolkit().beep();
	}
}
