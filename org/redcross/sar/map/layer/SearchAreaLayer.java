package org.redcross.sar.map.layer;

import java.io.IOException;
import java.net.UnknownHostException;

import org.redcross.sar.app.Utils;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.SearchAreaFeature;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.ISearchAreaIf;

import com.esri.arcgis.display.IDisplay;
import com.esri.arcgis.display.RgbColor;
import com.esri.arcgis.display.SimpleFillSymbol;
import com.esri.arcgis.display.SimpleLineSymbol;
import com.esri.arcgis.display.TextSymbol;
import com.esri.arcgis.geometry.ISpatialReference;
import com.esri.arcgis.geometry.Polygon;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.system.ITrackCancel;

public class SearchAreaLayer extends AbstractMsoFeatureLayer {

	private static final long serialVersionUID = 1L;
 	private SimpleFillSymbol symbol = null;
	private SimpleFillSymbol selectionSymbol = null;
	private TextSymbol textSymbol = null;
	private String[] labels = null;
 	
 	public SearchAreaLayer(IMsoModelIf msoModel,  ISpatialReference srs) {
 		super(IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA, 
 				LayerCode.SEARCH_AREA_LAYER, msoModel, srs);
 		createlabels();
 		createSymbols();
	}
 	
 	protected IMsoFeature createMsoFeature(IMsoObjectIf msoObject) 
 			throws IOException, AutomationException {
 		IMsoFeature msoFeature = new SearchAreaFeature();
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
				ISearchAreaIf searchArea = (ISearchAreaIf)feature.getMsoObject();
				Polygon polygon = (Polygon)feature.getShape();
				if (polygon != null) {
					if (feature.isSelected()) 
						display.setSymbol(selectionSymbol);
					else display.setSymbol(symbol);
					display.drawPolygon(polygon);
					
					// labels
					display.setSymbol(textSymbol);
					int pri = searchArea.getPriority();
					if (pri > 0) {
						display.drawText(polygon.getCentroid(), labels[pri-1]);
					}
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

			RgbColor color = new RgbColor();
			color.setBlue(255);

			SimpleLineSymbol outlineSymbol = new SimpleLineSymbol();	
			outlineSymbol.setWidth(1.5);
			outlineSymbol.setColor(color);
			symbol.setOutline(outlineSymbol);

			selectionSymbol = new SimpleFillSymbol();
			selectionSymbol.setStyle(com.esri.arcgis.display.esriSimpleFillStyle.esriSFSNull);
			RgbColor selectionColor = new RgbColor();
			selectionColor.setBlue(255);
			selectionColor.setGreen(255);

			SimpleLineSymbol selectedOutlineSymbol = new SimpleLineSymbol();	
			selectedOutlineSymbol.setWidth(1.5);
			selectedOutlineSymbol.setColor(selectionColor);
			selectionSymbol.setOutline(selectedOutlineSymbol);

			textSymbol = new TextSymbol();
			textSymbol.setSize(14);
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
	
	private void createlabels() {
		labels = new String[5];
		labels[0] = Utils.translate("PRIMARY_SEARCH_AREA");
		labels[1] = Utils.translate("SECONDARY_SEARCH_AREA");
		labels[2] = Utils.translate("PRIORITY3_SEARCH_AREA");
		labels[3] = Utils.translate("PRIORITY4_SEARCH_AREA");
		labels[4] = Utils.translate("PRIORITY5_SEARCH_AREA");
	}
}
