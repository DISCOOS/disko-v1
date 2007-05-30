package org.redcross.sar.map.layer;

import java.io.IOException;
import java.net.UnknownHostException;

import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.POIFeatureClass;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import com.esri.arcgis.display.IDisplay;
import com.esri.arcgis.display.RgbColor;
import com.esri.arcgis.display.SimpleMarkerSymbol;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.system.ITrackCancel;

public class POILayer extends AbstractMsoFeatureLayer {

	private static final long serialVersionUID = 1L;
 	private SimpleMarkerSymbol symbol = null;
	private SimpleMarkerSymbol selectionSymbol = null;
 	
 	public POILayer(IMsoModelIf msoModel) {
 		classCode = IMsoManagerIf.MsoClassCode.CLASSCODE_POI;
		featureClass = new POIFeatureClass(IMsoManagerIf.MsoClassCode.CLASSCODE_POI, msoModel);
		try {
			createSymbols();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void draw(int drawPhase, IDisplay display, ITrackCancel trackCancel)
			throws IOException, AutomationException {
		try {
			if (display == null || !this.isVisible) {
				return;
			}
			for (int i = 0; i < featureClass.featureCount(null); i++) {
				IMsoFeature feature = (IMsoFeature)featureClass.getFeature(i);
				if (feature.isSelected()) {
					display.setSymbol(selectionSymbol);
				}
				else {
					display.setSymbol(symbol);
				}
				Point point = (Point)feature.getShape();
				if (point != null) {
					display.drawPoint(point);
				}
			}
			isDirty = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createSymbols() throws UnknownHostException, IOException {
		symbol = new SimpleMarkerSymbol();
		RgbColor c = new RgbColor();
		c.setRed(255);
		c.setBlue(255);
		symbol.setColor(c);
			
		selectionSymbol = new SimpleMarkerSymbol();
		c = new RgbColor();
		c.setBlue(255);
		c.setGreen(255);
		selectionSymbol.setColor(c);
	}
}
