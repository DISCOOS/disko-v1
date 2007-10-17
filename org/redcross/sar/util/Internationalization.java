package org.redcross.sar.util;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IUnitIf;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
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
    private static Map<Class, ResourceBundle> resourceBundles = new LinkedHashMap<Class, ResourceBundle>();

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
            Enum e = (Enum)obj;
            String key = e.getClass().getSimpleName()+"."+e.name()+".text";

            return getEnumText(aBundle, (Enum) obj);
        }
        String str = obj.toString();
        String  key = str + ".text";
        return getBundleText(aBundle,key);
    }

    /**
     * Translate international text for an Enum
     *
     * The method shall give the same type of results as {@link org.redcross.sar.app.Utils#translate(Object)}.
     *
     * @param anEnum Lookup value
     * @return Same as {@link #getEnumText(java.util.ResourceBundle, Enum)} or {@link #getBundleText(java.util.ResourceBundle, String)}, depending on type of obj.
     */

    public static String translate (Enum anEnum)
    {
        Class enumClass = anEnum.getClass();
        ResourceBundle bundle = resourceBundles.get(enumClass);
        if (bundle == null)
        { // Determine properties-file from class name. Requires certain naming rules.
            String className = anEnum.getClass().getName();
            String simpleName = anEnum.getClass().getSimpleName();
            String regEx = ".*\\.I\\w*If\\$" + simpleName;
            if (Pattern.matches(regEx,className))
            {
                String bundleName = className.replaceFirst("\\.I",".properties.").replaceFirst("If\\$" + simpleName,"");
                bundle = ResourceBundle.getBundle(bundleName);
                resourceBundles.put(enumClass,bundle);
            }
        }
        return translate(bundle,anEnum);
    }

    public static void main(String[] args)
    {
        System.out.println(translate(IUnitIf.UnitStatus.PAUSED));
        System.out.println(translate(IAssignmentIf.AssignmentStatus.ABORTED));
        System.out.println(translate(IUnitIf.UnitStatus.INITIALIZING));
        System.out.println(translate(IMsoManagerIf.MsoClassCode.CLASSCODE_ENVIRONMENT));

    }
}
