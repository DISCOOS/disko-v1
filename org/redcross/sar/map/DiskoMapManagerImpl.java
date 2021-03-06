package org.redcross.sar.map;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.filechooser.FileSystemView;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.map.layer.AbstractMsoFeatureLayer;
import org.redcross.sar.map.layer.DiskoWMSLayer;
import org.redcross.sar.map.layer.FlankLayer;
import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.map.layer.OperationAreaLayer;
import org.redcross.sar.map.layer.OperationAreaMaskLayer;
import org.redcross.sar.map.layer.POILayer;
import org.redcross.sar.map.layer.PlannedAreaLayer;
import org.redcross.sar.map.layer.SearchAreaLayer;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.util.MapInfoComparator;

import com.esri.arcgis.carto.ILayer;
import com.esri.arcgis.carto.IMap;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.geometry.Envelope;

public class DiskoMapManagerImpl implements IDiskoMapManager {

	private IDiskoApplication app = null;
	private ArrayList<DiskoMap> maps = null;
	private IDiskoMap printMap = null;
	private String primarMxdDoc = null;
	private String secondaryMxdDoc = null;
	private boolean primarActive = true;
	private ArrayList<String> sMxdPaths = new ArrayList<String>();
	
	public DiskoMapManagerImpl(IDiskoApplication app) {
		this.app = app;
		maps = new ArrayList<DiskoMap>();
		setInitMxdPaths();		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.redcross.sar.map.IDiskoMapManager#getMapInstance()
	 */
	public IDiskoMap getMapInstance() {
		DiskoMap map = null;
		try {
			String mxdDoc = app.getProperty("MxdDocument.path");
			map = new DiskoMap(mxdDoc, this, app.getMsoModel());
			maps.add(map);
			if (printMap == null){
				printMap = new DiskoMap(mxdDoc, this, app.getMsoModel());
			}
		} catch (AutomationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	public IDiskoMap getPrintMap(){
		try {
			if (printMap == null){
				String mxdDoc = app.getProperty("MxdDocument.path");
				printMap = new DiskoMap(mxdDoc, this, app.getMsoModel());
			}
		} catch (AutomationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return printMap;
	}
	
	/**
	 * Reads primary and secondary mxddoc path from properties. Also reads custom mxddocs in given mxddoc catalog into an arraylist.
	 *
	 */
	private void setInitMxdPaths(){
		System.out.println("setInitMxdPaths()");
		this.primarMxdDoc = app.getProperty("MxdDocument.path");
		this.secondaryMxdDoc = app.getProperty("MxdDocument.secondary.path");
		if (secondaryMxdDoc != null) {
		}
		
		String catalogPath = app.getProperty("MxdDocument.catalog.path");	
		File f = FileSystemView.getFileSystemView().createFileObject(catalogPath);
		File[] files = FileSystemView.getFileSystemView().getFiles(f, true);
		String fname;
		for (int i=0; i < files.length; i++){			
			fname = files[i].getName();
			if (fname.contains(".")){
				try{
					if (fname.substring(fname.lastIndexOf(".")).equalsIgnoreCase(".mxd")){
						sMxdPaths.add(fname);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}			
		}
		System.out.println("setInitMxdPaths(), antall mxd'er: " + sMxdPaths.size());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.redcross.sar.map.IDiskoMapManager#getMaps()
	 */
	public List getMaps() {
		return maps;
	}
	
	public String getCurrentMxd() {
		return this.primarMxdDoc;
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	public void toggleMap() throws IOException{		
		// toogle
		primarActive = !primarActive;
		// forward
		setMap();
	}	
	
	/**
	 * 
	 * @throws IOException
	 */
	public void setMap() throws IOException{
		
		DiskoMap map = (DiskoMap) app.getCurrentMap();
		String mapname = map.getDocumentFilename();
		
		//Envelope extent = (Envelope)map.getExtent();
		
		// set invisible
		map.setVisible(false);
		
		if(primarActive){ 
			map.loadMxFile(this.primarMxdDoc, null, null); //sets 1.map active
		}
		else {
			map.loadMxFile(this.secondaryMxdDoc, null, null);//sets 2.map active
		}
		/*/ set previous extent?
		if (!mapname.equalsIgnoreCase(map.getDocumentFilename())) {
			map.setExtent(extent);
		}*/
		
		// set visible
		map.setVisible(true);
		
		//toggle icon
		app.getNavBar().switchIcon("maptoggle", primarActive);				
	}	
	
	public void setPrimarMxdDoc(String mxddoc){
		this.primarMxdDoc = mxddoc;
	}
	
	public void setSecondaryMxdDoc(String mxddoc){
		this.secondaryMxdDoc = mxddoc;		
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList getMapsTable(){
		ArrayList<MapSourceInfo> maps = new ArrayList<MapSourceInfo>();
		String sPath = new String();
		MapSourceInfo mapInfo;
		for (int i=0; i<sMxdPaths.size(); i++){
			mapInfo = new MapSourceInfo();
			sPath = app.getProperty("MxdDocument.catalog.path") + this.sMxdPaths.get(i);
			mapInfo.setMxdPath(sPath);			
			DiskoMapManagerImpl manager = (DiskoMapManagerImpl) app.getDiskoMapManager();
			if(sPath.equalsIgnoreCase(manager.primarMxdDoc)){
				mapInfo.setPrimarMap(true);
			}
			else 
				mapInfo.setPrimarMap(false);
			
			if(sPath.equalsIgnoreCase(manager.secondaryMxdDoc)){
				mapInfo.setSecondaryMap(true);
			}
			else 
				mapInfo.setSecondaryMap(false);
			
			if(sPath.equalsIgnoreCase(app.getProperty("MxdDocument.path")) || sPath.equalsIgnoreCase(app.getProperty("MxdDocument.secondary.path"))){
				mapInfo.setType("Sentral");
			}
			else 
				mapInfo.setType("Lokal");			
			//hardkodet i pilot
			mapInfo.setStatus("ok");			
			
			System.out.println("fest: " + mapInfo.getMxdPath() + ", " + mapInfo.getType()+ ", " + mapInfo.getStatus() +", " + mapInfo.getPrimarMap() + ", " + mapInfo.getSecondaryMap());
			
			maps.add(mapInfo);
		}	
		
		//sort list		
		Collections.sort(maps, new MapInfoComparator()); 
		return maps;
	}
	
	public void initWMSLayers() throws IOException{
		DiskoWMSLayer wms = new DiskoWMSLayer();
		DiskoMap map = (DiskoMap) app.getCurrentMap();
		IMap focusMap = map.getActiveView().getFocusMap();
		System.out.println(map.getLayerCount());
		try{
			ILayer wmsLayer = (ILayer) wms.createWMSLayer();
			wmsLayer.setVisible(true);
			focusMap.addLayer(wmsLayer);			
			System.out.println("har lagt til et layer");
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		map.getActiveView().refresh();
		
		System.out.println(map.getLayerCount());
		
		for (int i = 0; i < focusMap.getLayerCount(); i++){
			ILayer lay = focusMap.getLayer(i);
			System.out.println(lay.getName());
			
		}
		
	}
}
