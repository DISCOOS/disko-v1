package org.redcross.sar.map.layer;

import java.io.IOException;
import java.net.UnknownHostException;

import org.redcross.sar.map.feature.AreaFeatureClass;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;

import com.esri.arcgis.display.IDisplay;
import com.esri.arcgis.display.RgbColor;
import com.esri.arcgis.display.SimpleLineSymbol;
import com.esri.arcgis.geometry.GeometryBag;
import com.esri.arcgis.geometry.IGeometry;
import com.esri.arcgis.geometry.IPolyline;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.system.ITrackCancel;

public class AreaLayer extends AbstractMsoFeatureLayer {

	private static final long serialVersionUID = 1L;
 	private SimpleLineSymbol symbol = null;
	private SimpleLineSymbol selectionSymbol = null;
 	
 	public AreaLayer(IMsoModelIf msoModel) {
 		classCode = IMsoManagerIf.MsoClassCode.CLASSCODE_AREA;
 		featureClass = new AreaFeatureClass(IMsoManagerIf.MsoClassCode.CLASSCODE_AREA, msoModel);
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
				GeometryBag geomBag = (GeometryBag)feature.getShape();
				if (geomBag != null) {
					for (int j = 0; j < geomBag.getGeometryCount(); j++) {
						IGeometry geom = geomBag.getGeometry(j);
						if (geom instanceof IPolyline) {
							display.drawPolyline((IPolyline)geom);
						}
					}
				}
			}
			isDirty = false;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void createSymbols() throws UnknownHostException, IOException {
		symbol = new SimpleLineSymbol();
		RgbColor c = new RgbColor();
		//c.setGreen(255);
		symbol.setColor(c);
		symbol.setWidth(3);
			
		selectionSymbol = new SimpleLineSymbol();
		c = new RgbColor();
		c.setBlue(255);
		c.setGreen(255);
		selectionSymbol.setColor(c);
		selectionSymbol.setWidth(3);
	}
}
