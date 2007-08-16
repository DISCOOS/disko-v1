package org.redcross.sar.wp.messageLog;

import java.awt.Dimension;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JToggleButton;

import org.redcross.sar.app.Utils;
import org.redcross.sar.mso.data.IAssignmentIf;
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
	public final static Dimension SMALL_BUTTON_SIZE = new Dimension(60, 60);
	public final static Dimension LARGE_BUTTON_SIZE = new Dimension(180, 60);
	public final static Dimension ASSIGNMENT_BUTTON_SIZE = new Dimension(200, 60);
	
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
		
		button.setMinimumSize(SMALL_BUTTON_SIZE);
		button.setPreferredSize(SMALL_BUTTON_SIZE);
		button.setMaximumSize(SMALL_BUTTON_SIZE);
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
		
		button.setMinimumSize(LARGE_BUTTON_SIZE);
		button.setPreferredSize(LARGE_BUTTON_SIZE);
		button.setMaximumSize(LARGE_BUTTON_SIZE);
		return button;
	}

	public static JButton createSmallOKButton()
	{
		JButton button = new JButton();
		button.setMinimumSize(SMALL_BUTTON_SIZE);
		button.setPreferredSize(SMALL_BUTTON_SIZE);
		button.setMaximumSize(SMALL_BUTTON_SIZE);
		
		try
		{
			ResourceBundle resoureces = ResourceBundle.getBundle("properties");
			String iconPath = resoureces.getString("OkButton.icon");
			button.setIcon(Utils.createImageIcon(iconPath, "OK"));
		}
		catch(Exception e)
		{
			button.setText("OK");
		}
		return button;
	}

	public static JButton createSmallCancelButton()
	{
		JButton button = new JButton();
		button.setMinimumSize(SMALL_BUTTON_SIZE);
		button.setPreferredSize(SMALL_BUTTON_SIZE);
		button.setMaximumSize(SMALL_BUTTON_SIZE);
		
		try
		{
			ResourceBundle resoureces = ResourceBundle.getBundle("properties");
			String iconPath = resoureces.getString("CancelButton.icon");
			button.setIcon(Utils.createImageIcon(iconPath, "CANCEL"));
		}
		catch(Exception e)
		{
			// TODO internasjonaliser
			button.setText("Avbryt");
		}
		return button;
	}

	public static JButton createSmallButton(String string)
	{
		JButton button = new JButton();
		if(string != null)
		{
			button.setText(string);
		}
		
		button.setMinimumSize(SMALL_BUTTON_SIZE);
		button.setPreferredSize(SMALL_BUTTON_SIZE);
		button.setMaximumSize(SMALL_BUTTON_SIZE);
		
		return button;
	}

	public static JToggleButton createSmallToggleButton(String string)
	{
		JToggleButton button = new JToggleButton();
		if(string != null)
		{
			button.setText(string);
		}
		
		button.setMinimumSize(SMALL_BUTTON_SIZE);
		button.setPreferredSize(SMALL_BUTTON_SIZE);
		button.setMaximumSize(SMALL_BUTTON_SIZE);
		
		return button;
	}

	public static JButton createAssignmentButton(IAssignmentIf assignment)
	{
		JButton button = new JButton();
		
		button.setMinimumSize(ASSIGNMENT_BUTTON_SIZE);
		button.setPreferredSize(ASSIGNMENT_BUTTON_SIZE);
		button.setMaximumSize(ASSIGNMENT_BUTTON_SIZE);
		
		button.setText(assignment.getTypeText() + " " + assignment.getNumber());
		
		return button;
	}

	public static JToggleButton createAssignmentToggleButton(IAssignmentIf assignment)
	{
		JToggleButton button = new JToggleButton();

		button.setMinimumSize(ASSIGNMENT_BUTTON_SIZE);
		button.setPreferredSize(ASSIGNMENT_BUTTON_SIZE);
		button.setMaximumSize(ASSIGNMENT_BUTTON_SIZE);

		button.setText(assignment.getTypeText()+ " " + assignment.getNumber());

		return button;
	}

	public static JToggleButton createSmallAssignmentToggleButton(IAssignmentIf assignment)
	{
		JToggleButton button = new JToggleButton();
		
		button.setMinimumSize(SMALL_BUTTON_SIZE);
		button.setPreferredSize(SMALL_BUTTON_SIZE);
		button.setMaximumSize(SMALL_BUTTON_SIZE);
		
		// TODO use common label for assignments, move to UIFactory?
		button.setText(assignment.getNumber() + " " + assignment.getTypeText());
		
		return button;
	}
}
