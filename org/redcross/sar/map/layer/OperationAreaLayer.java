package org.redcross.sar.map.layer;

import java.io.IOException;
import java.net.UnknownHostException;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import com.esri.arcgis.display.IDisplay;
import com.esri.arcgis.display.RgbColor;
import com.esri.arcgis.display.SimpleFillSymbol;
import com.esri.arcgis.display.SimpleLineSymbol;
import com.esri.arcgis.geometry.Polygon;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.system.ITrackCancel;

public class OperationAreaLayer extends AbstractMsoLayer {

	private static final long serialVersionUID = 1L;
 	private SimpleFillSymbol symbol = null;
	private SimpleFillSymbol selectionSymbol = null;
 	
 	public OperationAreaLayer(IMsoModelIf msoModel) {
 		classCode = IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA;
 		featureClass = new MsoFeatureClass(IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA, msoModel);
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
				MsoFeature msoFeature = (MsoFeature)featureClass.getFeature(i);
				if (msoFeature.isSelected()) {
					display.setSymbol(selectionSymbol);
				}
				else {
					display.setSymbol(symbol);
				}
				Polygon polygon = (Polygon)msoFeature.getShape();
				if (polygon != null) {
					display.drawPolygon(polygon);
				}
			}
			isDirty = false;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void createSymbols() throws UnknownHostException, IOException {
		symbol = new SimpleFillSymbol();
		symbol.setStyle(com.esri.arcgis.display.esriSimpleFillStyle.esriSFSNull);
		
		RgbColor c = new RgbColor();
		c.setRed(255);
		c.setBlue(255);
		
		SimpleLineSymbol outlineSymbol = new SimpleLineSymbol();	
		outlineSymbol.setWidth(4);
		outlineSymbol.setColor(c);
		symbol.setOutline(outlineSymbol);
			
		selectionSymbol = new SimpleFillSymbol();
		selectionSymbol.setStyle(com.esri.arcgis.display.esriSimpleFillStyle.esriSFSNull);
		c = new RgbColor();
		c.setBlue(255);
		c.setGreen(255);
		
		SimpleLineSymbol selectedOutlineSymbol = new SimpleLineSymbol();	
		selectedOutlineSymbol.setWidth(4);
		selectedOutlineSymbol.setColor(c);
		selectionSymbol.setOutline(selectedOutlineSymbol);
	}
}
