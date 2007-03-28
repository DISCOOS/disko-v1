package org.redcross.sar.map.index;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.esri.arcgis.carto.FeatureLayer;
import com.esri.arcgis.geodatabase.IFeature;
import com.esri.arcgis.geodatabase.IFeatureCursor;
import com.esri.arcgis.geodatabase.SpatialFilter;
import com.esri.arcgis.geodatabase.esriSpatialRelEnum;
import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.IGeometry;
import com.esri.arcgis.geometry.IGeometryCollection;
import com.esri.arcgis.geometry.IPointCollection;
import com.esri.arcgis.geometry.IRing;
import com.esri.arcgis.geometry.Path;
import com.esri.arcgis.geometry.Polygon;
import com.esri.arcgis.geometry.Polyline;

public class IndexedGeometry  {
	
	//Logger for this object
	 private static final Logger LOGGER = Logger.getLogger(
	      "om.geodata.engine.disko.geom.IndexedGeometryBag");
	
	private static final long serialVersionUID = 1L;
	private static final double MAX_TILE_SIZE = 1000;
	private QuadTree quadTree = null;
	private ArrayList<Tile> tiles = null;
	private ArrayList<IGeometry> geometries = null;

	public IndexedGeometry() throws IOException {
		super();
		
		tiles = new ArrayList<Tile>(); // contains searched tiles
		geometries = new ArrayList<IGeometry>();
	}
	
	public void update(Envelope extent, List snapLayers) throws IOException {
		quadTree = null; // invalidate the quadtree
		geometries.clear(); // remove all geometries
		
		if (extent == null || snapLayers == null) {
			return;
		}
		if (Math.min(extent.getWidth(), extent.getHeight()) > 20000) {
			return;
		}
		//long startTime = System.currentTimeMillis();
		Polyline temp = new Polyline();
		
		for (int i = 0; i < snapLayers.size(); i++) {
			FeatureLayer flayer = (FeatureLayer)snapLayers.get(i);
			SpatialFilter spatialFilter = new SpatialFilter();
			spatialFilter.setGeometryByRef(extent);
			spatialFilter.setGeometryField(flayer.getFeatureClass().getShapeFieldName());
			spatialFilter.setSpatialRel(esriSpatialRelEnum.esriSpatialRelIntersects);
			IFeatureCursor featureCursor = flayer.search(spatialFilter,false);
			
			IFeature feature = featureCursor.nextFeature();
			while (feature != null) {
				IGeometry geom = feature.getShapeCopy();
				if (geom instanceof Polyline) {
					// converts polyline to collection of paths
					temp.addGeometryCollection((IGeometryCollection)geom);
				}
				else if (geom instanceof Polygon) {
					Polygon polygon = (Polygon)geom;
					// must convert polygons to paths
					for (int j = 0; j < polygon.getGeometryCount(); j++) {
						IRing ring = (IRing)polygon.getGeometry(j);
						Path path = new Path();
						path.addPointCollection((IPointCollection)ring);
						temp.addGeometry(path, null, null);
					}
				}
				feature = featureCursor.nextFeature();
			}
		}
		temp.simplifyNetwork();
		for (int j = 0; j < temp.getGeometryCount(); j++) {
			Polyline pline = new Polyline();
			pline.addGeometry(temp.getGeometry(j), null, null);
			geometries.add(pline);
		}
		build(extent); // build the quadtree
		//long endTime = System.currentTimeMillis();
		//System.out.println("Time used: "+(endTime-startTime));
	}
	
	private void build(Envelope extent) throws IOException {
		// estimate number of levels in cache quad tree
		int numLevels = 1;
		double size = 0;
		for (int n = 0; n < 9; n++) {
			double k = Math.pow(2, n);
			size = Math.max(extent.getWidth() / k, extent.getHeight() / k);
			if (size <= MAX_TILE_SIZE) {
				numLevels = n + 1;
				break;
			}
		}
		LOGGER.info("Creating quad tree with " + numLevels
				+ " levels and tile size " + Math.round(size));
		quadTree = new QuadTree(extent, numLevels);
	}
	
	public IGeometry search(Envelope env) throws IOException {
		if (quadTree == null) {
			return null;
		}
		tiles.clear();
		quadTree.getTiles(tiles, env);
		LOGGER.fine("Number of tiles: " + tiles.size());

		// query tiles
		for (int i = 0; i < tiles.size(); i++) {
			Tile tile = (Tile) tiles.get(i);
			LOGGER.fine(tile.toString());
			if (!tile.isFilled()) {
				fillTile(tile);
			}
			for (int j = 0; j < tile.getNumUserData(); j++) {
				Polyline pline = (Polyline)tile.getdata(j);
				if (!env.disjoint(pline)) {
					return pline;
				}
			}
		}
		return null;
	}
	
	public void fillTile(Tile tile) throws IOException {
		for (int i = 0; i < geometries.size(); i++) {
			IGeometry geom = geometries.get(i);
			if (!tile.getExtent().disjoint(geom)) {
				tile.addData(geom);
				tile.setIsFilled(true);
			}
		}
	}
}
