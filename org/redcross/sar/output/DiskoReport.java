package org.redcross.sar.output;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.*;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.view.JasperViewer;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.map.DiskoMap;
import org.redcross.sar.mso.MsoModelImpl;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.wp.tactics.DiskoWpTacticsImpl;

import com.esri.arcgis.carto.IActiveView;
import com.esri.arcgis.display.IOutputRasterSettings;
import com.esri.arcgis.geometry.Envelope;
import com.esri.arcgis.geometry.IEnvelope;
import com.esri.arcgis.output.ExportPNG;
import com.esri.arcgis.output.IExport;
import com.esri.arcgis.display.tagRECT;

public class DiskoReport {
	
	private IDiskoApplication app = null;
	private String reportTemplate_path = null;
	private String jasperfiles_path = null;
	
	private int numberOfTemplates;
	private String[] jrxmlFiles = null;
	private String[] jasperFiles = null;
	private List <JasperPrint>jrprintFiles = null;
	
	private MsoModelImpl m_msoModel = null;
	private IActiveView activeView = null;
	
	private DiskoMap diskoMap_print = new DiskoMap();
	
	private final static String outputPrintFormat = ".PNG";
	
	//test
	private com.esri.arcgis.carto.Map cartoMap = null;
	//private IMap 
	
	private DiskoWpTacticsImpl wp = null;
	
	public DiskoReport(IDiskoApplication app){
		System.out.println("test");
		this.app = app;
		//test
		m_msoModel = (MsoModelImpl) app.getMsoModel();
		//m_msoModel.getMsoManager().getOperation().
		
		
		diskoMap_print = (DiskoMap) app.getCurrentMap();
		//getSelected mso object
		diskoMap_print.getSelection();
		diskoMap_print.zoomToMsoObject();//zoom to selected
		//set printscale
		try{
			activeView = diskoMap_print.getActiveView();
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		//diskoMap.getMap().
		try{
			cartoMap = (com.esri.arcgis.carto.Map) diskoMap_print.getMap();
		}
		catch(IOException ioe){
			System.out.println("funker ikke");
			ioe.printStackTrace();
		}
		
		this.reportTemplate_path = app.getProperty("report.template.path");
		this.jasperfiles_path = app.getProperty("report.jasperfiles.path");
		numberOfTemplates = Integer.parseInt(app.getProperty("report.NUMBER_OF_TEMPLATES"));
		jrxmlFiles = new String[numberOfTemplates];
		jasperFiles = new String[numberOfTemplates];
		jrprintFiles = new ArrayList<JasperPrint>(numberOfTemplates);
		System.out.println("test");
		jrxmlFiles[0] = app.getProperty("report.TACTICS_TEMPLATE");
		/*jrxmlFiles[1] = app.getProperty("report.UNITLOG_TEMPLATE");
		jrxmlFiles[2] = app.getProperty("report.PARTICIPANTS_TEMPLATE");
		jrxmlFiles[3] = app.getProperty("report.COMMUNICATIONLOG_TEMPLATE");
		jrxmlFiles[4] = app.getProperty("report.MISSIONLOG_TEMPLATE");*/
	}	
	
	/**
	 * 
	 *
	 */
	public void compile(){
		System.out.println("DiskoReport: compile()");		
		//loope igjennom og generere jasper filer		
		String jasperFileName = new String();
		try{			
			for(int i=0; i < jrxmlFiles.length; i++){
				jasperFileName = jrxmlFiles[i].substring(0,jrxmlFiles[i].indexOf(".")+1) + "jasper";
				JasperCompileManager.compileReportToFile(this.reportTemplate_path + jrxmlFiles[i], this.jasperfiles_path + jasperFileName);
				jasperFiles[i] = jasperFileName;
			}			
		}
		catch(JRException jre){
			jre.printStackTrace();
			System.out.println("Kompilering av .jrxml fil feilet");
		}		
	}
	
	public void printAssignments(List<IAssignmentIf> assignments){
		System.out.println("printAssignments()");
		//inntil videre kjøres kompilering også
		compile();		
		//*tar for gitt at vi skal printe oppdrag
		//må få tilsendt aktuelt oppdrag fra ListDialog		
		//eksportere mapobject
		String exportMapPath = exportMap();
		//må finne MSO object med tilhørende MSO geometri til aktuelt oppdrag
		//ekstraherer verdrier fra assignmentslista
		Map assignmentsMap = extractAssignmentValues(assignments, exportMapPath);
		//lage kartutsnitt til aktuell geometri (se "rediger" i listdialog). kartutsnitt skal ha målestokk 1:50000
		fill(jasperFiles, assignmentsMap);
		//preview	
		JasperPrint jPrint = (JasperPrint) jrprintFiles.get(0);
		view(jPrint);		
	}
	
	public void printPreview(){
		
		
		//test
		System.out.println("DiskoReport: viewPreview()");
		compile();
		//fylle jasperfilene med data og produserer jrprint file
		//if(jasperFiles != null)
		Map map = fillMap();
		fill(jasperFiles, map);
		//preview	
		JasperPrint jPrint = (JasperPrint) jrprintFiles.get(0);
		view(jPrint);
	}
	
	private void fill(String[] fileNames, Map map){
		System.out.println("DiskoReport: fill(), params: map_path = " + map.get("map_path") + ", sok_nr = " + map.get("sok_nr"));
		File sourceFile = null;
		File destFile = null;
		try{
			for (int i=0; i < fileNames.length; i++){
				sourceFile = new File(jasperfiles_path + fileNames[i]);
				JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
				
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JREmptyDataSource());
				
				destFile = new File(sourceFile.getParent(), jasperReport.getName() + ".jrprint");
				JRSaver.saveObject(jasperPrint, destFile);
				
				jrprintFiles.add(jasperPrint);
				
				//test
				System.out.println("jasperPrint name: " + jasperPrint.getName() + jasperPrint.getPageHeight());
				System.out.println("jasperprint, antall sider: " + jasperPrint.getPages().size());
			}
		} catch(JRException jre){
			jre.printStackTrace();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	private void view(JasperPrint jrprintFile){		
		System.out.println("DiskoReport: view() " + jrprintFile);
		try{
			JasperViewer.viewReport(jrprintFile);
		}
		catch(Exception e){
			e.printStackTrace();
			
		}
	}
	
	private Map fillMap(){
		System.out.println("DiskoReport: fillMap()");
		Map<String, String> map = new HashMap<String,String>();
		//map.put( "map_path", "C:/1_prosjekt/DISKO/rapport/jasperreport_testdata/50248790.jpg" );
	    //map.put( "sok_nr", "4" );
		return map;
	}
	
	private Map extractAssignmentValues(List<IAssignmentIf> assignments, String exportMapPath){
		Map<String, Object> map = new HashMap<String,Object>();
		
		MissionOrderPrintParams missionOrderPrint = new MissionOrderPrintParams();
		map = missionOrderPrint.setPrintParams(assignments, exportMapPath);
				
		return map;
		
	}
	
	private String exportMap(){
		System.out.println("exportMap()");
		long imgResolution = 100;
		long imgResampleRatio = 1;
		long hdc;
		tagRECT exportRECT = new tagRECT();
		String outputDir = app.getProperty("report.output.path");
		long randomNumber = ((long)Math.random() * 10000)+1;
		System.out.println("Math.random(): " + Math.random() + "randomNumber: " + randomNumber);
		String outputFileName = app.getProperty("report.output.printname") + randomNumber;
		//long rnd = 
		String exportPath = outputDir+outputFileName+outputPrintFormat;
		IExport docExport = null;
		IEnvelope pixelBoundsEnv = null;
		try{
			docExport = new ExportPNG();
			
			//set export filename
			System.out.println(exportPath);
	        docExport.setExportFileName(exportPath);
	        
	        //set outputResolution
	        docExport.setResolution(imgResolution);
	        			
			//always set the output quality of the DISPLAY to 1 for image export formats
	        setOutputQuality(activeView, imgResampleRatio);				                
	        
	        //get the device context of the screeen (bare windows relevant)
	        //todo
	        
	        //Get the bounds of the deviceframe for the screen.
			//todo
	        	        			
	        //Assign envelope object to the export object
	        pixelBoundsEnv = new Envelope();
	        exportRECT.bottom = 500;
	        exportRECT.right = 500;
	        exportRECT.top = 0;
	        exportRECT.left = 0;
	        pixelBoundsEnv.putCoords(exportRECT.left, exportRECT.top, exportRECT.right, exportRECT.bottom);
	       
	        docExport.setPixelBounds(pixelBoundsEnv);
	        
	        //export
	        System.out.println("starter eksport");
	        //ready to export
	        hdc = docExport.startExporting();
	        
	        //Redraw the active view, rendering it to the exporter object
	        activeView.output((int) hdc, (int)docExport.getResolution(), exportRECT, null, null);
	        
	        //finish export
	        docExport.finishExporting();
	        docExport.cleanup();
	        
	        System.out.println("ferdig eksportert");
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
		
		return exportPath;
	}
	
	private void setOutputQuality(IActiveView activeView, long imgResampleRatio){
		//trenger vi???
		try{
			IOutputRasterSettings outputRasterSettings = (IOutputRasterSettings) activeView.getScreenDisplay().getDisplayTransformation();
			outputRasterSettings.setResampleRatio((int)imgResampleRatio);
		} catch(IOException ioe){
			ioe.printStackTrace();
		}
		 
	}
	

}
