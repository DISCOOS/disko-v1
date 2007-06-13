package org.redcross.sar.map;

import org.redcross.sar.app.IDiskoApplication;

import java.io.File;

import com.esri.arcgis.datasourcesraster.RasterDataset;
import com.esri.arcgis.datasourcesraster.RasterWorkspaceFactory;
import com.esri.arcgis.datasourcesraster.RasterWorkspace;
import com.esri.arcgis.carto.RasterLayer;

import java.io.IOException;


import com.esri.arcgis.system.FileStream;
import com.esri.arcgis.system.IStream;

public class CustomMapData {
	
	
	public void AddCustomData(IDiskoApplication app, File f){
		String fname = f.getName();
		String path = f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-fname.length());
		boolean ok = false;
		
		
		DiskoMap map = (DiskoMap) app.getCurrentMap();		
		//System.out.println(path + " :_: " + fname +" " + f.getAbsolutePath());
		System.out.println("sjekker ext");
		//må sjekke fil type 
		int i = fname.lastIndexOf(".") + 1;
		String ext = fname.substring(i, fname.length());
		System.out.println("sjekker ext: " +  ext);
		
		
		if(ext.equalsIgnoreCase("tif") || ext.equalsIgnoreCase("tiff")){
			System.out.println("raster");
			ok = AddRasterFile(map, f.getAbsolutePath());			
		}
		else if (ext.equalsIgnoreCase("shp")){
			System.out.println("shp");
			ok = AddShapeFile(map, fname, path);
		}
						
		//lagre kartet med de nye dataene
		try{
			System.out.println("Ikke lagret");
			//FileStream fs = new FileStream();
			//fs.open("C:\\DISKO\\Disko.mxd", com.esri.arcgis.system.esriFilePermission.esriReadWrite);
			//map.save(fs, 0);
		}catch (Exception e){
			e.printStackTrace();
			System.out.println("FileStream feilet");
			
		}		
		
	}
	
	private boolean AddShapeFile(DiskoMap map, String fname, String path){
		try{
			map.addShapeFile(path, fname);	
			return true;
			//map.save(mxd, false);//må ha tak i mxd dokumentet
			//mapDocument.save(mapDocument.isUsesRelativePaths(), false);
		}
		catch(Exception e){			
			System.out.println("Add shape failed: " + e);
			return false;
		}
	}
		
	
	
	private boolean AddRasterFile(DiskoMap map, String fullpath){
//		RasterDataset rd = openRasterDataset(path, fname);
		try{			
			RasterLayer rl = new RasterLayer();
			rl.createFromFilePath(fullpath);			
			map.addLayer(rl, map.getLayerCount());
			return true;
			
		}catch(Exception e){
			System.out.println("Error new RasterLayer");
			return false;
		}
	}
	
	private RasterDataset openRasterDataset(String directoryName, String fileName) {
		RasterDataset rasterDataset = null;
		try {
			// Create a workspace factory which then opens a RasterWorkspace and
			// finally open the dataset
			RasterWorkspaceFactory rasterWorkspaceFactory = new RasterWorkspaceFactory();
			RasterWorkspace rasterWorkspace = (RasterWorkspace) rasterWorkspaceFactory
					.openFromFile(directoryName, 0);
			rasterDataset = (RasterDataset) rasterWorkspace
					.openRasterDataset(fileName);
		} catch (Exception e) {
			System.out.println("Threw an exception trying to open a rasterdataset");
			e.printStackTrace();
		}

		return rasterDataset;
	}
}
