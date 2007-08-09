package org.redcross.sar.map.layer;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.redcross.sar.map.feature.FlankFeature;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.MsoFeatureClass;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.IMsoObjectIf;

import com.esri.arcgis.display.IDisplay;
import com.esri.arcgis.display.LineFillSymbol;
import com.esri.arcgis.display.RgbColor;
import com.esri.arcgis.display.SimpleLineSymbol;
import com.esri.arcgis.geometry.IPolygon;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.system.ITrackCancel;

public class FlankLayer extends AbstractMsoFeatureLayer {

	private static final long serialVersionUID = 1L;
	private LineFillSymbol blueFill = null;
	private LineFillSymbol redFill  = null;
	private boolean isInitiated = false;
	
	public FlankLayer(IMsoModelIf msoModel) {
		super(IMsoManagerIf.MsoClassCode.CLASSCODE_AREA,
				LayerCode.FLANK_LAYER, msoModel);
	}
	
	private void initiate() throws IOException, AutomationException {
 		createSymbols();
 		MsoFeatureClass msoFC = (MsoFeatureClass)featureClass;
 		ICmdPostIf cmdPost = msoModel.getMsoManager().getCmdPost();
 		Iterator iter = cmdPost.getAreaListItems().iterator();
 		while(iter.hasNext()) {
 			IMsoObjectIf msoObj = (IMsoObjectIf)iter.next();
 			IMsoFeature msoFeature = createMsoFeature(msoObj);
 			msoFC.addFeature(msoFeature);
 		}
 		isInitiated = true;
 	}
	
	protected IMsoFeature createMsoFeature(IMsoObjectIf msoObject) 
			throws IOException, AutomationException {
		IMsoFeature msoFeature = new FlankFeature();
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
			if (!isInitiated) initiate();
			for (int i = 0; i < featureClass.featureCount(null); i++) {
				FlankFeature feature = (FlankFeature)featureClass.getFeature(i);
				List leftFlanks  = feature.getLeftFlanks();
				for (int j = 0; j < leftFlanks.size(); j++) {
					display.setSymbol(redFill);
					display.drawPolygon((IPolygon)leftFlanks.get(j));
				}
				List rightFlanks  = feature.getRightFlanks();
				for (int j = 0; j < rightFlanks.size(); j++) {
					display.setSymbol(blueFill);
					display.drawPolygon((IPolygon)rightFlanks.get(j));
				}
			}
			isDirty = false;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void createSymbols() throws IOException, AutomationException {
		// fill symbols
		redFill = new LineFillSymbol();
		RgbColor redColor = new RgbColor();
		redColor.setRed(255);
		redFill.setColor(redColor);
		redFill.setAngle(45);
		SimpleLineSymbol leftOutline = new SimpleLineSymbol();
		leftOutline.setColor(redColor);
		redFill.setOutline(leftOutline);

		blueFill = new LineFillSymbol();
		RgbColor blueColor = new RgbColor();
		blueColor.setBlue(255);
		blueFill.setColor(blueColor);
		blueFill.setAngle(45);
		SimpleLineSymbol rightOutline = new SimpleLineSymbol();
		rightOutline.setColor(blueColor);
		blueFill.setOutline(rightOutline);
	}
}
