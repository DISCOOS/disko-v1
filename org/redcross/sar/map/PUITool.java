package org.redcross.sar.map;

import java.io.IOException;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.PUIDialog;

import com.esri.arcgis.display.IDraw;
import com.esri.arcgis.display.IScreenDisplay;
import com.esri.arcgis.display.RgbColor;
import com.esri.arcgis.display.SimpleMarkerSymbol;
import com.esri.arcgis.display.esriScreenCache;
import com.esri.arcgis.carto.FeatureLayer;
import com.esri.arcgis.carto.esriSelectionResultEnum;
import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geodatabase.QueryFilter;
import com.esri.arcgis.geometry.Point;
import com.esri.arcgis.interop.AutomationException;

/**
 * A custom draw tool.
 * @author geira
 *
 */
public class PUITool extends AbstractCommandTool {

	private static final long serialVersionUID = 1L;
	
	private Point p1 = null;
	private FeatureLayer editLayer = null;
	private SimpleMarkerSymbol drawSymbol = null;
	private PUIDialog puiDialog = null;
		
	/**
	 * Constructs the DrawTool
	 */
	public PUITool(IDiskoApplication app) throws IOException {
		//symbol to draw with
		//drawSymbol
		drawSymbol = new SimpleMarkerSymbol();	
		RgbColor drawColor = new RgbColor();
		drawColor.setRed(255);
		drawSymbol.setColor(drawColor);
		
		dialog = new PUIDialog(app);
		dialog.setIsToggable(true);
		//this.drawSymbol = 
	}

	public void onCreate(Object obj) throws IOException, AutomationException {
		if (obj instanceof DiskoMap) {
			map = (DiskoMap)obj;
			map.addDiskoMapEventListener(this);
			puiDialog = (PUIDialog)dialog;
			puiDialog.setLocationRelativeTo(map, DiskoDialog.POS_WEST, true);
			setEditLayer(map.getEditLayer());
		}
	}

	public void setEditLayer(FeatureLayer editLayer) {
		this.editLayer = editLayer;
	}
	
	

	public void onMouseDown(int button, int shift, int x, int y)
			throws IOException, AutomationException {
		//sjekker om det allerede finnes et tegnet punkt. 
		//sletter evt dette fra 
		
		//IMap map = hookHelper.getFocusMap();
		p1 = transform(x,y);
		
		//System.out.println("Aktivt editLayer: " + editLayer.getName());
		
		//save p to editlayer		
		IFeature digFeature = editLayer.getFeatureClass().createFeature();
		digFeature.setShapeByRef(p1);
		
		digFeature.store();
					
		//draw this feature
		draw();
		
		//sets x and y value in PUI dialog
		puiDialog.setYCoordFieldText(Double.toString(p1.getY()));
		puiDialog.setXCoordFieldText(Double.toString(p1.getX()));
		
		//set this feature selected
		QueryFilter qfilter = new QueryFilter();
		qfilter.setWhereClause("[OBJECTID] ="+digFeature.getOID());
		editLayer.selectFeatures(qfilter, 
				esriSelectionResultEnum.esriSelectionResultNew, false);
		
		//refresh map
		map.getActiveView().refresh();
		//System.out.println(p1);
	}	
	
	private void draw() throws IOException {
		IScreenDisplay screenDisplay = map.getActiveView().getScreenDisplay();
		screenDisplay.startDrawing(0,(short) esriScreenCache.esriNoScreenCache);
		IDraw draw = (IDraw) screenDisplay;
		
		draw.setSymbol(drawSymbol);
		draw.draw(p1);
		//screenDisplay.g
		screenDisplay.finishDrawing();
	}
	
	//***** DiskoMapEventListener implementations *****
	public void editLayerChanged(DiskoMapEvent e) {
		setEditLayer(map.getEditLayer());
	}
}
