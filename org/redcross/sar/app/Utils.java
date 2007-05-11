package org.redcross.sar.app;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import javax.swing.ImageIcon;

/**
 * Utility class containing access to methods for handling properties.
 * @author geira
 *
 */
public class Utils {
	
	private static Properties properties = null;
	
	/**
	 * Load the properties in a file with the given name
	 * @param fileName The name (path) of the file
	 * @return 
	 * @throws Exception
	 */
	public static Properties loadProperties(String fileName) throws Exception {
		Properties prop = new Properties();
		FileInputStream in = new FileInputStream(fileName);
		prop.load(in);
		in.close();
		return prop;
	}
	
	/**
	 * Get the default properties
	 * @return 
	 * @throws Exception
	 */
	public static Properties getProperties() {
		if (properties == null) {
			try {
				properties = loadProperties("properties");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return properties;
	}
	
	/**
	 * Create a image icon
	 * @param path The path to the icon image
	 * @param name A name to identify the icon
	 * @return A ImageIcon
	 * @throws Exception
	 */
	public static ImageIcon createImageIcon(String path, String name) 
			throws Exception {
		if (path != null) {
			java.net.URL imgURL = (new File(path)).toURI().toURL();
			return new ImageIcon(imgURL, name);
		}
		return null;
	 }
	
	public static String translate(Object obj) {
		if (obj == null) {
			return "";
		}
		Properties props = getProperties();
		String key = null;
		if (obj instanceof Enum) {
			Enum e = (Enum)obj;
			key = e.getClass().getSimpleName()+"."+e.name()+".text";
			return (props != null ? props.getProperty(key, e.name()) : e.name()).toUpperCase();
		}
		String str = obj.toString();
		key = str+".text";
		return props.getProperty(key, str).toUpperCase();
	}
	
	public static ImageIcon getIcon(Enum e) {
		if (e != null) {
			try {
				Properties props = getProperties();
				String key = e.getClass().getSimpleName()+"."+e.name()+".icon";
				return props != null ? createImageIcon(props.getProperty(key), e.name()) : null;
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return null;
	}
}
