package org.redcross.sar.map.layer;

import java.io.IOException;
import java.net.UnknownHostException;

import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.OperationAreaMaskFeatureClass;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;

import com.esri.arcgis.display.IDisplay;
import com.esri.arcgis.display.RgbColor;
import com.esri.arcgis.display.SimpleFillSymbol;
import com.esri.arcgis.display.TransparencyDisplayFilter;
import com.esri.arcgis.geometry.Polygon;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.system.ITrackCancel;

public class OperationAreaMaskLayer extends AbstractMsoFeatureLayer {

	private static final long serialVersionUID = 1L;
	private SimpleFillSymbol fill = null;
	
	public OperationAreaMaskLayer(IMsoModelIf msoModel) {
		setClassCode(IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA);
		setLayerCode(LayerCode.OPERATION_AREA_MASK_LAYER);
 		featureClass = new OperationAreaMaskFeatureClass(
 				IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA, msoModel);
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
 				Polygon polygon = (Polygon)feature.getShape();
 				if (polygon != null) {
 					TransparencyDisplayFilter filter = new TransparencyDisplayFilter();
 					filter.setTransparency((short)50);
 					display.setFilterByRef(filter);
 					display.setSymbol(fill);
 					display.drawPolygon(polygon);
 					display.setFilterByRef(null);
 				}
 			}
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
	}

	private void createSymbols() throws UnknownHostException, IOException {
		fill = new SimpleFillSymbol();
		RgbColor blueColor = new RgbColor();
		blueColor.setBlue(255);
		fill.setColor(blueColor);
	}
}
