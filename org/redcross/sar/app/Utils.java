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
	
	/**
	 * Get the properties in a file with the given name
	 * @param fileName The name (path) of the file
	 * @return 
	 * @throws Exception
	 */
	public static Properties getProperties(String fileName) throws Exception {
		Properties prop = new Properties();
		FileInputStream in = new FileInputStream(fileName);
		prop.load(in);
		in.close();
		return prop;
	}
	
	/**
	 * Create a image icon
	 * @param path The path to the icon image
	 * @param name A name to identify the icon
	 * @return A ImageIcon
	 * @throws Exception
	 */
	public final static ImageIcon createImageIcon(String path, String name) 
			throws Exception {
		java.net.URL imgURL = (new File(path)).toURI().toURL();
	    return new ImageIcon(imgURL, name);
	  }
}
