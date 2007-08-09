package org.redcross.sar.map.layer;

import java.io.IOException;
import java.util.Iterator;

import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.MsoFeatureClass;
import org.redcross.sar.map.feature.OperationAreaMaskFeature;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.IMsoObjectIf;

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
	private TransparencyDisplayFilter filter = null;
	private boolean isInitiated = false;
	
	public OperationAreaMaskLayer(IMsoModelIf msoModel) {
		super(IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA,
				LayerCode.OPERATION_AREA_MASK_LAYER, msoModel);
	}
	
	private void initiate() throws IOException, AutomationException {
 		createSymbols();
 		MsoFeatureClass msoFC = (MsoFeatureClass)featureClass;
 		ICmdPostIf cmdPost = msoModel.getMsoManager().getCmdPost();
 		Iterator iter = cmdPost.getOperationAreaListItems().iterator();
 		while(iter.hasNext()) {
 			IMsoObjectIf msoObj = (IMsoObjectIf)iter.next();
 			IMsoFeature msoFeature = createMsoFeature(msoObj);
 			msoFC.addFeature(msoFeature);
 		}
 		isInitiated = true;
 	}
	
	protected IMsoFeature createMsoFeature(IMsoObjectIf msoObject) 
			throws IOException, AutomationException {
		IMsoFeature msoFeature = new OperationAreaMaskFeature();
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
 				IMsoFeature feature = (IMsoFeature)featureClass.getFeature(i);
 				Polygon polygon = (Polygon)feature.getShape();
 				if (polygon != null) {
 					display.setFilterByRef(filter);
 					display.setSymbol(fill);
 					display.drawPolygon(polygon);
 					display.setFilterByRef(null);
 				}
 			}
 			isDirty = false;
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
	}

	private void createSymbols() throws IOException, AutomationException {
		filter = new TransparencyDisplayFilter();
		filter.setTransparency((short)50);

		fill = new SimpleFillSymbol();
		RgbColor blueColor = new RgbColor();
		blueColor.setBlue(255);
	}
}
