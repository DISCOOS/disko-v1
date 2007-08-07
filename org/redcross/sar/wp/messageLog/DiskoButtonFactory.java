package org.redcross.sar.wp.messageLog;

import java.awt.Dimension;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JToggleButton;

import org.redcross.sar.app.Utils;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.ICommunicatorIf;
import org.redcross.sar.mso.data.IUnitIf;

/**
 * Creates buttons. 
 * TODO Methods should probably be moved to some GUI factory class later on
 * @author thomasl
 *
 */
public class DiskoButtonFactory
{
	/**
	 * Creates a small JToggleButton based on the communicator
	 * TODO should be moved to some utility class
	 */
	public static JToggleButton createSmallToggleButton(ICommunicatorIf communicator)
	{
		JToggleButton button = new JToggleButton();
		ResourceBundle resources = null;
		try
		{
			resources = ResourceBundle.getBundle("org.redcross.sar.mso.data.properties.Unit");
		}
		catch(MissingResourceException e)
		{
			System.err.println("Error getting unit resources in ButtonFactory");
			return button;
		}
		
		if(communicator instanceof ICmdPostIf)
		{
			try
			{
				button.setIcon(Utils.createImageIcon(resources.getString("UnitType.COMMAND_POST.icon"), ""));
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else if(communicator instanceof IUnitIf)
		{
			String unitType = ((IUnitIf)communicator).getType().name();
			try
			{
				button.setIcon(Utils.createImageIcon(resources.getString("UnitType."+unitType+".icon"), ""));
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		button.setMinimumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		button.setPreferredSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		button.setMaximumSize(MessageLogPanel.SMALL_BUTTON_SIZE);
		return button;
	}
	
	/**
	 * Creates a large JToggleButton based on the communicator
	 * TODO should be moved to some utility class
	 */
	public static JToggleButton createLargeToggleButton(ICommunicatorIf communicator)
	{
		JToggleButton button = new JToggleButton();
		ResourceBundle resources = null;
		try
		{
			resources = ResourceBundle.getBundle("org.redcross.sar.mso.data.properties.Unit");
		}
		catch(MissingResourceException e)
		{
			System.err.println("Error getting unit resources in ButtonFactory");
			return button;
		}
		
		if(communicator instanceof ICmdPostIf)
		{
			try
			{
				button.setIcon(Utils.createImageIcon(resources.getString("UnitType.COMMAND_POST.icon"), ""));
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else if(communicator instanceof IUnitIf)
		{
			String unitType = ((IUnitIf)communicator).getType().name();
			try
			{
				button.setIcon(Utils.createImageIcon(resources.getString("UnitType."+unitType+".icon"), ""));
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		button.setText(communicator.getCommunicatorNumberPrefix() + communicator.getCommunicatorNumber() +
				"  " + communicator.getCallSign());
		
		button.setMinimumSize(new Dimension(MessageLogPanel.SMALL_BUTTON_SIZE.width*3,
				MessageLogPanel.SMALL_BUTTON_SIZE.height));
		button.setPreferredSize(new Dimension(MessageLogPanel.SMALL_BUTTON_SIZE.width*3,
				MessageLogPanel.SMALL_BUTTON_SIZE.height));
		button.setMaximumSize(new Dimension(MessageLogPanel.SMALL_BUTTON_SIZE.width*3,
				MessageLogPanel.SMALL_BUTTON_SIZE.height));
		return button;
	}
}