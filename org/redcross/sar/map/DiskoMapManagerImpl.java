package org.redcross.sar.map;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.filechooser.FileSystemView;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.map.layer.AbstractMsoFeatureLayer;
import org.redcross.sar.map.layer.PlannedAreaLayer;
import org.redcross.sar.map.layer.DiskoWMSLayer;
import org.redcross.sar.map.layer.FlankLayer;
import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.map.layer.OperationAreaLayer;
import org.redcross.sar.map.layer.OperationAreaMaskLayer;
import org.redcross.sar.map.layer.POILayer;
import org.redcross.sar.map.layer.SearchAreaLayer;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.util.MapInfoComparator;

import com.esri.arcgis.carto.ILayer;
import com.esri.arcgis.carto.IMap;
import com.esri.arcgis.interop.AutomationException;

public class DiskoMapManagerImpl implements IDiskoMapManager {

	private IDiskoApplication app = null;
	private ArrayList<DiskoMap> maps = null;
	private List<AbstractMsoFeatureLayer> msoLayers = null;
	private String primarMxdDoc = null;
	private String secondaryMxdDoc = null;
	private boolean primarActive = true;
	private ArrayList<String> sMxdPaths = new ArrayList<String>();

	
	public DiskoMapManagerImpl(IDiskoApplication app) {
		this.app = app;
		maps = new ArrayList<DiskoMap>();
		
		msoLayers = new ArrayList<AbstractMsoFeatureLayer>();
		msoLayers.add(new POILayer(app.getMsoModel()));
		msoLayers.add(new PlannedAreaLayer(app.getMsoModel()));
		msoLayers.add(new FlankLayer(app.getMsoModel()));
		msoLayers.add(new SearchAreaLayer(app.getMsoModel()));
		msoLayers.add(new OperationAreaLayer(app.getMsoModel()));
		msoLayers.add(new OperationAreaMaskLayer(app.getMsoModel()));
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
		} catch (AutomationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
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
		

		//initWMSLayers();
		
		
		DiskoMap map = (DiskoMap) app.getCurrentMap();
		String mapname = map.getDocumentFilename();		
		if(mapname.equalsIgnoreCase(this.primarMxdDoc)){ 
			map.loadMxFile(this.secondaryMxdDoc, null, null); //sets 2.map active
			primarActive = false;
		}
		else {
			map.loadMxFile(this.primarMxdDoc, null, null);//sets 1.map active
			primarActive = true;
		}		
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
	
	public List getMsoLayers() {
		return msoLayers;
	}
	
	public List getMsoLayers(IMsoManagerIf.MsoClassCode classCode) {
		ArrayList<IMsoFeatureLayer> result = new ArrayList<IMsoFeatureLayer>();
		for (int i = 0; i < msoLayers.size(); i++) {
			IMsoFeatureLayer msoFeatureLayer = (IMsoFeatureLayer)msoLayers.get(i);
			if (msoFeatureLayer.getClassCode() == classCode) {
				result.add(msoFeatureLayer);
			}
		}
		return result;
	}
	
	public IMsoFeatureLayer getMsoLayer(IMsoFeatureLayer.LayerCode layerCode) {
		for (int i = 0; i < msoLayers.size(); i++) {
			IMsoFeatureLayer msoFeatureLayer = (IMsoFeatureLayer)msoLayers.get(i);
			if (msoFeatureLayer.getLayerCode() == layerCode) {
				return msoFeatureLayer;
			}
		}
		return null;
	}
	
	public void initWMSLayers() throws IOException{
		System.out.println("initWMSLayers");
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
