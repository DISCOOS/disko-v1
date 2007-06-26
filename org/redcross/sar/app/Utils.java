package org.redcross.sar.app;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.swing.ImageIcon;

import org.redcross.sar.gui.ErrorDialog;

/**
 * Utility class containing access to methods for handling properties.
 * @author geira
 *
 */
public class Utils {
	
	private static Properties properties = null;
	private static ErrorDialog errorDialog = null;
	
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
	
	public static String getProperty(String key) {
		return getProperties().getProperty(key);
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
			File file = new File(path);
			if (file.exists()) {
				java.net.URL imgURL = file.toURI().toURL();
				return new ImageIcon(imgURL, name);
			}
		}
      else
      {
         BufferedImage defaultImage = new BufferedImage(30, 30, BufferedImage.TYPE_INT_RGB);

         java.awt.Graphics2D g2 = defaultImage.createGraphics();
         java.awt.Color col = new java.awt.Color(255, 0, 0);
         g2.setColor(col);
         g2.fill3DRect(0, 0, 30, 30, true);

         ImageIcon img=new ImageIcon(defaultImage,name+" not found");

         return img;
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
			return (props != null ? props.getProperty(key, e.name()) : e.name());
		}
		String str = obj.toString();
		key = str+".text";
		return props.getProperty(key, str);
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
	
	public static ErrorDialog getErrorDialog() {
		if (errorDialog == null) {
			errorDialog = new ErrorDialog(null);
		}
		return errorDialog;
	}
}
