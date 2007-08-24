package org.redcross.sar.map.layer;

import java.io.IOException;
import java.net.UnknownHostException;

import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.OperationAreaFeature;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.IMsoObjectIf;

import com.esri.arcgis.display.IDisplay;
import com.esri.arcgis.display.RgbColor;
import com.esri.arcgis.display.SimpleFillSymbol;
import com.esri.arcgis.display.SimpleLineSymbol;
import com.esri.arcgis.geometry.ISpatialReference;
import com.esri.arcgis.geometry.Polygon;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.system.ITrackCancel;

public class OperationAreaLayer extends AbstractMsoFeatureLayer {

	private static final long serialVersionUID = 1L;
 	private SimpleFillSymbol symbol = null;
	private SimpleFillSymbol selectionSymbol = null;
 	
 	public OperationAreaLayer(IMsoModelIf msoModel, ISpatialReference srs) {
 		super(IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA, 
 				LayerCode.OPERATION_AREA_LAYER, msoModel, srs);
 		createSymbols();
	}
 	
 	protected IMsoFeature createMsoFeature(IMsoObjectIf msoObject) 
 			throws IOException, AutomationException {
 		IMsoFeature msoFeature = new OperationAreaFeature();
 		msoFeature.setSpatialReference(srs);
		msoFeature.setMsoObject(msoObject);
		return msoFeature;
	}
	
 	public void draw(int drawPhase, IDisplay display, ITrackCancel trackCancel)
 			throws IOException, AutomationException {
 		try {
 			if (display == null || !this.isVisible) {
 				return;
 			}
 			for (int i = 0; i < featureClass.featureCount(null); i++) {
 				IMsoFeature feature = (IMsoFeature)featureClass.getFeature(i);
 				Polygon polygon = (Polygon)feature.getShape();
 				if (polygon != null) {
 					if (feature.isSelected()) 
 						display.setSymbol(selectionSymbol);
 					else display.setSymbol(symbol);
 					display.drawPolygon(polygon);
 				}
 			}
 			isDirty = false;
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 	}

 	private void createSymbols() {
 		try {
			symbol = new SimpleFillSymbol();
			symbol.setStyle(com.esri.arcgis.display.esriSimpleFillStyle.esriSFSNull);

			RgbColor c = new RgbColor();
			c.setRed(255);
			c.setBlue(255);

			SimpleLineSymbol outlineSymbol = new SimpleLineSymbol();	
			outlineSymbol.setWidth(1.5);
			outlineSymbol.setColor(c);
			symbol.setOutline(outlineSymbol);

			selectionSymbol = new SimpleFillSymbol();
			selectionSymbol.setStyle(com.esri.arcgis.display.esriSimpleFillStyle.esriSFSNull);
			c = new RgbColor();
			c.setBlue(255);
			c.setGreen(255);

			SimpleLineSymbol selectedOutlineSymbol = new SimpleLineSymbol();	
			selectedOutlineSymbol.setWidth(1.5);
			selectedOutlineSymbol.setColor(c);
			selectionSymbol.setOutline(selectedOutlineSymbol);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AutomationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 	}
}
