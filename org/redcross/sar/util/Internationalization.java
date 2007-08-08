package org.redcross.sar.util;

import java.util.ResourceBundle;
/**
 * Created by IntelliJ IDEA.
 * User: vinjar
 * Date: 08.aug.2007
 */

/**
 * Class for internationational texts (and icons in the future).
 */
public class Internationalization
{
    /**
     * Get international text from a given java.util.ResourceBundle.
     *
     * @param aBundle The ResourceBundle to use
     * @param aKey Lookup value
     * @return The found value, or null if not found.
     */
    public static String getBundleText(ResourceBundle aBundle, String aKey)
    {
        if (aBundle == null)
        {
            return null;
        }
        try
        {
            return aBundle.getString(aKey);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Get international text from a given java.util.ResourceBundle.
     *
     * @param aBundle The ResourceBundle to use
     * @param aKey Lookup value
     * @return The found value, or aKey if not found.
     */
    public static String getFullBundleText(ResourceBundle aBundle, String aKey)
    {
        String retVal = getBundleText(aBundle, aKey);
        return retVal != null ? retVal : aKey;
    }

    /**
     * Get international Enum text from a given java.util.ResourceBundle.
     *
     * @param aBundle The ResourceBundle to use
     * @param anEnum Lookup value
     * @return The found value, or Enum.name() if not found.
     */
    public static String getEnumText(ResourceBundle aBundle, Enum anEnum)
    {
        String retVal = getBundleText(aBundle, anEnum.getClass().getSimpleName() + "." + anEnum.name() + ".text");
        return retVal != null ? retVal : anEnum.name();
    }


    /**
     * Translate international text from a given java.util.ResourceBundle.
     *
     * The method shall give the same type of results as {@link org.redcross.sar.app.Utils#translate(Object)}.
     *
     * @param aBundle The ResourceBundle to use
     * @param obj Lookup value
     * @return Same as {@link #getEnumText(java.util.ResourceBundle, Enum)} or {@link #getBundleText(java.util.ResourceBundle, String)}, depending on type of obj.
     */
    public static String translate(ResourceBundle aBundle, Object obj)
    {
        if (obj == null)
        {
            return "";
        }
        if (obj instanceof Enum)
        {
            return getEnumText(aBundle, (Enum) obj);
        }
        String str = obj.toString();
        String  key = str + ".text";
        return getBundleText(aBundle,key);
    }
}
