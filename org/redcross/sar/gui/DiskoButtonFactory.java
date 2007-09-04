package org.redcross.sar.gui;

import java.awt.Dimension;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JToggleButton;

import org.redcross.sar.app.Utils;
import org.redcross.sar.gui.renderers.IconRenderer;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.ICommunicatorIf;
import org.redcross.sar.mso.data.IUnitIf;

/**
 * Creates buttons. 
 * TODO Methods should probably be moved to {@link UIFactory} later on
 * 
 * @author thomasl
 */
public class DiskoButtonFactory
{
	public final static Dimension SMALL_BUTTON_SIZE = new Dimension(60, 60);
	public final static Dimension LARGE_BUTTON_SIZE = new Dimension(180, 60);
//	public final static Dimension ASSIGNMENT_BUTTON_SIZE = new Dimension(200, 60);
	
	private static Properties m_properties = null;
	private final static ResourceBundle m_unitResources = ResourceBundle.getBundle("org.redcross.sar.mso.data.properties.Unit");
	
	public enum ButtonType
	{
		CancelButton,
		OkButton,
		FinishedButton,
		DeleteButton
	};
	
	private static Properties getProperties()
	{
		if(m_properties == null)
		{
			try
			{
				m_properties = Utils.loadProperties("properties");
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return m_properties;
	}
	
	/**
	 * Creates a small JToggleButton based on the communicator
	 */
	public static JToggleButton createSmallToggleButton(ICommunicatorIf communicator)
	{
		JToggleButton button = new JToggleButton();
		
		if(communicator instanceof ICmdPostIf)
		{
			try
			{
				button.setIcon(Utils.createImageIcon(m_unitResources.getString("UnitType.COMMAND_POST.icon"), ""));
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
				button.setIcon(Utils.createImageIcon(m_unitResources.getString("UnitType."+unitType+".icon"), ""));
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
		
		if(communicator instanceof ICmdPostIf)
		{
			try
			{
				button.setIcon(Utils.createImageIcon(m_unitResources.getString("UnitType.COMMAND_POST.icon"), ""));
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
				button.setIcon(Utils.createImageIcon(m_unitResources.getString("UnitType."+unitType+".icon"), ""));
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		button.setText(communicator.getCommunicatorNumber() + "  " + communicator.getCallSign());
		
		button.setMinimumSize(LARGE_BUTTON_SIZE);
		button.setPreferredSize(LARGE_BUTTON_SIZE);
		button.setMaximumSize(LARGE_BUTTON_SIZE);
		
		return button;
	}

	public static JButton createSmallButton(String string)
	{
		JButton button = new JButton();
		if(!string.isEmpty())
		{
			button.setText(string);
		}
		
		button.setMinimumSize(SMALL_BUTTON_SIZE);
		button.setPreferredSize(SMALL_BUTTON_SIZE);
		button.setMaximumSize(SMALL_BUTTON_SIZE);
		
		return button;
	}
	
	public static JButton createSmallButton(String name, String iconPath)
	{
		JButton button = createSmallButton("");
		try
		{
			button.setIcon(Utils.createImageIcon(iconPath, name));
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return button;
	}
	
	public static JButton createSmallButton(ButtonType type)
	{
		JButton button = createSmallButton("", getProperties().getProperty(type.name() + ".icon"));
		return button;
	}

	public static JToggleButton createSmallToggleButton(String string)
	{
		JToggleButton button = new JToggleButton();
		if(!string.isEmpty())
		{
			button.setText(string);
		}
		
		button.setMinimumSize(SMALL_BUTTON_SIZE);
		button.setPreferredSize(SMALL_BUTTON_SIZE);
		button.setMaximumSize(SMALL_BUTTON_SIZE);
		
		return button;
	}
	
	public static JToggleButton createSmallToggleButton(String name, String iconPath)
	{
		JToggleButton button = createSmallToggleButton("");
		
		try
		{
			button.setIcon(Utils.createImageIcon(iconPath, name));
		} 
		catch (Exception e)
		{
			button.setText(name);
		}
		
		return button;
	}

	public static JButton createAssignmentButton(IAssignmentIf assignment)
	{
		JButton button = new JButton();
		
		button.setMinimumSize(LARGE_BUTTON_SIZE);
		button.setPreferredSize(LARGE_BUTTON_SIZE);
		button.setMaximumSize(LARGE_BUTTON_SIZE);
		
//		button.setText(assignment.getTypeText() + " " + assignment.getNumber());
		IconRenderer.AssignmentIcon icon = new IconRenderer.AssignmentIcon(assignment, false, null);
		button.setIcon(icon);
		
		return button;
	}

	public static JToggleButton createLargeToggleButton(IAssignmentIf assignment)
	{
		JToggleButton button = new JToggleButton();

		button.setMinimumSize(LARGE_BUTTON_SIZE);
		button.setPreferredSize(LARGE_BUTTON_SIZE);
		button.setMaximumSize(LARGE_BUTTON_SIZE);

		button.setText(assignment.getTypeText()+ " " + assignment.getNumber());
//		IconRenderer.AssignmentIcon icon = new IconRenderer.AssignmentIcon(assignment, false, null);
//		button.setIcon(icon);

		return button;
	}

	public static JToggleButton createSmallAssignmentToggleButton(IAssignmentIf assignment)
	{
		JToggleButton button = new JToggleButton();
		
		button.setMinimumSize(SMALL_BUTTON_SIZE);
		button.setPreferredSize(SMALL_BUTTON_SIZE);
		button.setMaximumSize(SMALL_BUTTON_SIZE);
		
		IconRenderer.AssignmentIcon icon = new IconRenderer.AssignmentIcon(assignment, false, null);
		button.setIcon(icon);
		
		return button;
	}

	public static JToggleButton createLargeToggleButton(String text)
	{
		JToggleButton button = new JToggleButton();
		
		button.setText(text);
		
		button.setMinimumSize(LARGE_BUTTON_SIZE);
		button.setPreferredSize(LARGE_BUTTON_SIZE);
		button.setMaximumSize(LARGE_BUTTON_SIZE);
		
		return button;
	}
}