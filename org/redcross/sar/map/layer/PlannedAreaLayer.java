package org.redcross.sar.map.layer;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Hashtable;

import org.redcross.sar.app.Utils;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.PlannedAreaFeatureClass;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.mso.data.ISearchIf;
import org.redcross.sar.mso.data.ISearchIf.SearchSubType;

import com.esri.arcgis.display.IColor;
import com.esri.arcgis.display.IDisplay;
import com.esri.arcgis.display.RgbColor;
import com.esri.arcgis.display.SimpleLineSymbol;
import com.esri.arcgis.display.TextSymbol;
import com.esri.arcgis.display.esriSimpleLineStyle;
import com.esri.arcgis.geometry.GeometryBag;
import com.esri.arcgis.geometry.IGeometry;
import com.esri.arcgis.geometry.IPolyline;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.system.ITrackCancel;

public class PlannedAreaLayer extends AbstractMsoFeatureLayer {

	private static final long serialVersionUID = 1L;
	private RgbColor selectionColor = null;
	private Hashtable<SearchSubType, SimpleLineSymbol> symbols = null;
	private TextSymbol textSymbol = null;
 	
 	public PlannedAreaLayer(IMsoModelIf msoModel) {
 		setClassCode(IMsoManagerIf.MsoClassCode.CLASSCODE_AREA);
 		setLayerCode(LayerCode.AREA_LAYER);
 		featureClass = new PlannedAreaFeatureClass(IMsoManagerIf.MsoClassCode.CLASSCODE_AREA, msoModel);
		try {
			symbols = new Hashtable<SearchSubType, SimpleLineSymbol>();
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
				GeometryBag geomBag = (GeometryBag)feature.getShape();
				if (geomBag != null) {
					IAreaIf area = (IAreaIf)feature.getMsoObject();
					ISearchIf search = (ISearchIf)area.getOwningAssignment();
					String text = Utils.translate(search.getSubType())+" - "+
							Utils.translate(search.getStatus());
					SimpleLineSymbol lineSymbol = (SimpleLineSymbol)symbols.get(search.getSubType());
					IColor saveColor = lineSymbol.getColor();
					if (feature.isSelected()) {
						lineSymbol.setColor(selectionColor);
					}
					for (int j = 0; j < geomBag.getGeometryCount(); j++) {
						IGeometry geom = geomBag.getGeometry(j);
						if (geom instanceof IPolyline) {
							display.setSymbol(lineSymbol);
							display.drawPolyline((IPolyline)geom);
							display.setSymbol(textSymbol);
							display.drawText(geom, text);
						}
					}
					lineSymbol.setColor(saveColor);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void createSymbols() throws UnknownHostException, IOException {
		selectionColor = new RgbColor();
		selectionColor.setBlue(255);
		selectionColor.setGreen(255);
		
		RgbColor blackColor = new RgbColor();
		
		SimpleLineSymbol lineSymbol = new SimpleLineSymbol();
		lineSymbol.setStyle(esriSimpleLineStyle.esriSLSDash);
		lineSymbol.setWidth(2);
		lineSymbol.setColor(blackColor);
		
		symbols.put(ISearchIf.SearchSubType.LINE, lineSymbol);
		symbols.put(ISearchIf.SearchSubType.PATROL, lineSymbol);
		symbols.put(ISearchIf.SearchSubType.URBAN, lineSymbol);
		symbols.put(ISearchIf.SearchSubType.SHORELINE, lineSymbol);
		symbols.put(ISearchIf.SearchSubType.MARINE, lineSymbol);
		symbols.put(ISearchIf.SearchSubType.AIR, lineSymbol);
		symbols.put(ISearchIf.SearchSubType.DOG, lineSymbol);
		
		textSymbol = new TextSymbol();
		textSymbol.setYOffset(5);
	}
}
