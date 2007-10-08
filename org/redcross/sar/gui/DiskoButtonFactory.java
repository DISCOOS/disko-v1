package org.redcross.sar.gui;

import java.awt.Dimension;
import java.awt.Font;
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
	public final static Dimension TABLE_BUTTON_SIZE = new Dimension(35, 35);
	public final static Dimension SMALL_BUTTON_SIZE = new Dimension(60, 60);
	public final static Dimension LARGE_BUTTON_SIZE = new Dimension(180, 60);
	
	private static Properties m_properties = null;
	private final static ResourceBundle m_unitResources = ResourceBundle.getBundle("org.redcross.sar.mso.data.properties.Unit");
	
	private final static Font BUTTON_FONT = new Font("TableButtonFont", Font.BOLD, 12);
	
	public enum ButtonType
	{
		CancelButton,
		OkButton,
		FinishedButton,
		DeleteButton,
		BackButton,
		NextButton
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
		
		button.setBorder(null);
		
		return button;
	}
	
	public static JButton createSmallButton()
	{
		JButton button = new JButton();
		
		button.setFont(BUTTON_FONT);
		
		button.setMinimumSize(SMALL_BUTTON_SIZE);
		button.setPreferredSize(SMALL_BUTTON_SIZE);
		button.setMaximumSize(SMALL_BUTTON_SIZE);
		
		button.setBorder(null);
		
		return button;
	}

	public static JButton createSmallButton(String string)
	{
		JButton button = createSmallButton();
		
		if(!string.isEmpty())
		{
			button.setText(string);
		}
		
		return button;
	}
	
	public static JButton createSmallButton(String name, String iconPath)
	{
		JButton button = createSmallButton();
		if(name.equals(""))
		{
			try
			{
				button.setIcon(Utils.createImageIcon(iconPath, name));
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			button.setText(name);
		}
		
		return button;
	}
	
	public static JButton createSmallButton(ButtonType type)
	{
		JButton button = createSmallButton(getProperties().getProperty(type.name() + ".text"), 
				getProperties().getProperty(type.name() + ".icon"));
		return button;
	}

	public static JToggleButton createSmallToggleButton(String string)
	{
		JToggleButton button = createSmallToggleButton();
		
		if(!string.isEmpty())
		{
			button.setText(string);
		}
		
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

		return button;
	}

	public static JToggleButton createSmallAssignmentToggleButton(IAssignmentIf assignment)
	{
		JToggleButton button = createSmallToggleButton();
		
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

	public static JButton createTableButton(String text)
	{
		JButton button = new JButton(text);
		button.setFont(BUTTON_FONT);
		button.setBorder(null);
		
		button.setMinimumSize(TABLE_BUTTON_SIZE);
		button.setPreferredSize(TABLE_BUTTON_SIZE);
		button.setMaximumSize(TABLE_BUTTON_SIZE);
		
		button.setFocusable(false);
		
		return button;
	}

	public static JToggleButton createSmallToggleButton()
	{
		JToggleButton button = new JToggleButton();
		
		button.setFont(BUTTON_FONT);

		button.setMinimumSize(SMALL_BUTTON_SIZE);
		button.setPreferredSize(SMALL_BUTTON_SIZE);
		button.setMaximumSize(SMALL_BUTTON_SIZE);
		
		button.setBorder(null);

		return button;
	}
}
