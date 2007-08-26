package org.redcross.sar.map.layer;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Hashtable;

import org.redcross.sar.app.Utils;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.PlannedAreaFeature;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
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
	private SimpleLineSymbol defaultLineSymbol = null;
	private TextSymbol textSymbol = null;
 	
 	public PlannedAreaLayer(IMsoModelIf msoModel) {
 		super(IMsoManagerIf.MsoClassCode.CLASSCODE_AREA,
 				LayerCode.AREA_LAYER, msoModel);
 		symbols = new Hashtable<SearchSubType, SimpleLineSymbol>();
 		createSymbols();
	}
 	
 	protected IMsoFeature createMsoFeature(IMsoObjectIf msoObject) 
 			throws IOException, AutomationException {
 		IMsoFeature msoFeature = new PlannedAreaFeature(msoModel);
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
				GeometryBag geomBag = (GeometryBag)feature.getShape();
				if (geomBag != null) {
					IAreaIf area = (IAreaIf)feature.getMsoObject();
					ISearchIf search = (ISearchIf)area.getOwningAssignment();
					String text = null;
					SimpleLineSymbol lineSymbol = null;
					if (search != null) {
						lineSymbol = (SimpleLineSymbol)symbols.get(search.getSubType());
						text = Utils.translate(search.getSubType())+" - "+
							Utils.translate(search.getStatus());
					} else {
						lineSymbol = defaultLineSymbol;
					}
					IColor saveColor = lineSymbol.getColor();
					if (feature.isSelected()) {
						lineSymbol.setColor(selectionColor);
					}
					for (int j = 0; j < geomBag.getGeometryCount(); j++) {
						IGeometry geom = geomBag.getGeometry(j);
						if (geom instanceof IPolyline) {
							display.setSymbol(lineSymbol);
							display.drawPolyline((IPolyline)geom);
							if (text != null) {
								display.setSymbol(textSymbol);
								display.drawText(geom, text);
							}
						}
					}
					lineSymbol.setColor(saveColor);
				}
			}
			isDirty = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createSymbols() {
		try {
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

			defaultLineSymbol = new SimpleLineSymbol();
			defaultLineSymbol.setWidth(2);
			defaultLineSymbol.setColor(blackColor);
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
