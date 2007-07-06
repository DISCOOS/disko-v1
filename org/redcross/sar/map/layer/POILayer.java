package org.redcross.sar.map.layer;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Hashtable;

import org.redcross.sar.app.Utils;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.POIFeatureClass;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.mso.data.IPOIIf.POIType;

import com.esri.arcgis.display.IColor;
import com.esri.arcgis.display.IDisplay;
import com.esri.arcgis.display.IDisplayName;
import com.esri.arcgis.display.IMarkerSymbol;
import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.display.RgbColor;
import com.esri.arcgis.display.SimpleMarkerSymbol;
import com.esri.arcgis.display.TextSymbol;
import com.esri.arcgis.display.esriSimpleMarkerStyle;
import com.esri.arcgis.display.esriTextHorizontalAlignment;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.system.ITrackCancel;

public class POILayer extends AbstractMsoFeatureLayer {

	private static final long serialVersionUID = 1L;
	private RgbColor selectionColor = null;
	private TextSymbol textSymbol = null;
	private Hashtable<POIType, IDisplayName> symbols = null;
 	
 	public POILayer(IMsoModelIf msoModel) {
 		setClassCode(IMsoManagerIf.MsoClassCode.CLASSCODE_POI);
 		setLayerCode(LayerCode.POI_LAYER);
		featureClass = new POIFeatureClass(IMsoManagerIf.MsoClassCode.CLASSCODE_POI, msoModel);
		try {
			symbols = new Hashtable<POIType, IDisplayName>();
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
				Point point = (Point)feature.getShape();
				if (point != null) {
					IPOIIf poi = (IPOIIf)feature.getMsoObject();
					IMarkerSymbol markerSymbol = (IMarkerSymbol)symbols.get(poi.getType());
					IColor saveColor = markerSymbol.getColor();
					if (feature.isSelected()) {
						markerSymbol.setColor(selectionColor);
					}
					display.setSymbol((ISymbol)markerSymbol);
					display.drawPoint(point);
					markerSymbol.setColor(saveColor);
					
					//labels
					display.setSymbol(textSymbol);
					String text = Utils.translate(poi.getType());
					String remark = poi.getRemarks();
					if (remark != null) text += " "+remark;
					display.drawText(point, text);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createSymbols() throws UnknownHostException, IOException {
		// colors
		selectionColor = new RgbColor();
		selectionColor.setBlue(255);
		selectionColor.setGreen(255);
		
		RgbColor redColor = new RgbColor();
		redColor.setRed(255);
		RgbColor blueColor = new RgbColor();
		blueColor.setBlue(255);
		RgbColor greenColor = new RgbColor();
		greenColor.setGreen(255);
		
		SimpleMarkerSymbol redSquareSymbol = new SimpleMarkerSymbol();
		redSquareSymbol.setStyle(esriSimpleMarkerStyle.esriSMSSquare);
		redSquareSymbol.setColor(redColor);
		
		SimpleMarkerSymbol blueSquareSymbol = new SimpleMarkerSymbol();
		blueSquareSymbol.setStyle(esriSimpleMarkerStyle.esriSMSSquare);
		blueSquareSymbol.setColor(blueColor);
		
		SimpleMarkerSymbol redRoundSymbol = new SimpleMarkerSymbol();
		redRoundSymbol.setColor(redColor);
		
		SimpleMarkerSymbol blueRoundSymbol = new SimpleMarkerSymbol();
		blueRoundSymbol.setColor(blueColor);
		
		SimpleMarkerSymbol greenRoundSymbol = new SimpleMarkerSymbol();
		greenRoundSymbol.setColor(greenColor);
		
		SimpleMarkerSymbol blackDiamondSymbol = new SimpleMarkerSymbol();
		blackDiamondSymbol.setStyle(esriSimpleMarkerStyle.esriSMSDiamond);
		
		SimpleMarkerSymbol redDiamondSymbol = new SimpleMarkerSymbol();
		redDiamondSymbol.setStyle(esriSimpleMarkerStyle.esriSMSDiamond);
		redDiamondSymbol.setColor(redColor);
		
		SimpleMarkerSymbol greenDiamondSymbol = new SimpleMarkerSymbol();
		greenDiamondSymbol.setStyle(esriSimpleMarkerStyle.esriSMSDiamond);
		greenDiamondSymbol.setColor(greenColor);
		
		symbols.put(POIType.START, greenRoundSymbol);
		symbols.put(POIType.STOP, redRoundSymbol);
		symbols.put(POIType.VIA, blueRoundSymbol);
		symbols.put(POIType.OBSERVATION, blackDiamondSymbol);
		symbols.put(POIType.FINDING, blueSquareSymbol);
		symbols.put(POIType.GENERAL, blueSquareSymbol);
		symbols.put(POIType.SILENT_WITNESS, greenDiamondSymbol);
		symbols.put(POIType.INTELLIGENCE, redDiamondSymbol);
		
		textSymbol = new TextSymbol();
		textSymbol.setHorizontalAlignment(esriTextHorizontalAlignment.esriTHALeft);
		textSymbol.setVerticalAlignment(esriTextHorizontalAlignment.esriTHACenter);
		textSymbol.setXOffset(redRoundSymbol.getSize());
	}
}
