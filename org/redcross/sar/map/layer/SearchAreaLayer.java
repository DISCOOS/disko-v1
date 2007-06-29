package org.redcross.sar.map.layer;

import java.io.IOException;
import java.net.UnknownHostException;

import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.SearchAreaFeatureClass;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;

import com.esri.arcgis.display.IDisplay;
import com.esri.arcgis.display.RgbColor;
import com.esri.arcgis.display.SimpleFillSymbol;
import com.esri.arcgis.display.SimpleLineSymbol;
import com.esri.arcgis.geometry.Polygon;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.system.ITrackCancel;

public class SearchAreaLayer extends AbstractMsoFeatureLayer {

	private static final long serialVersionUID = 1L;
 	private SimpleFillSymbol symbol = null;
	private SimpleFillSymbol selectionSymbol = null;
 	
 	public SearchAreaLayer(IMsoModelIf msoModel) {
 		setClassCode(IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA);
 		setLayerCode(LayerCode.SEARCH_AREA_LAYER);
 		featureClass = new SearchAreaFeatureClass(IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA, msoModel);
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
					if (feature.isSelected()) 
						display.setSymbol(selectionSymbol);
					else display.setSymbol(symbol);
					display.drawPolygon(polygon);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void createSymbols() throws UnknownHostException, IOException {
		symbol = new SimpleFillSymbol();
		symbol.setStyle(com.esri.arcgis.display.esriSimpleFillStyle.esriSFSNull);
		
		RgbColor color = new RgbColor();
		color.setBlue(255);
		
		SimpleLineSymbol outlineSymbol = new SimpleLineSymbol();	
		outlineSymbol.setWidth(3);
		outlineSymbol.setColor(color);
		symbol.setOutline(outlineSymbol);
			
		selectionSymbol = new SimpleFillSymbol();
		selectionSymbol.setStyle(com.esri.arcgis.display.esriSimpleFillStyle.esriSFSNull);
		RgbColor selectionColor = new RgbColor();
		selectionColor.setBlue(255);
		selectionColor.setGreen(255);
		
		SimpleLineSymbol selectedOutlineSymbol = new SimpleLineSymbol();	
		selectedOutlineSymbol.setWidth(3);
		selectedOutlineSymbol.setColor(selectionColor);
		selectionSymbol.setOutline(selectedOutlineSymbol);
	}
}
