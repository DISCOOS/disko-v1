package org.redcross.sar.map;

import java.io.IOException;

import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.systemUI.ICommand;

import org.redcross.sar.app.IDiskoApplication;

public class MapToggleCommand implements ICommand {

	private static final long serialVersionUID = 1L;
	
	public int getBitmap() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getCaption() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCategory() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getHelpContextID() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getHelpFile() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getMessage() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTooltip() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isChecked() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEnabled() throws IOException, AutomationException {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void onClick() throws IOException, AutomationException {
		// TODO Auto-generated method stub

	}

	public void onCreate(Object arg0) throws IOException, AutomationException {
		// TODO Auto-generated method stub

	}
	
	
	public void onClick(IDiskoApplication app) throws IOException, AutomationException {
		try{	
			DiskoMapManagerImpl man = (DiskoMapManagerImpl) app.getDiskoMapManager();
			man.toggleMap(); 
		}catch (Exception ioe){
			System.out.println("togglemap failed");
		}

	}

}
