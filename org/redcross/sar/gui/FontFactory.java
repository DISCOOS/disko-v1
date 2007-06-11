package org.redcross.sar.gui;

import java.awt.*;

/**
 *
 */
public class FontFactory
{
    private final static String DefaultFont = "Tahoma";
    private final static int MediumSize = 14;
    private final static int LargeSize = 16;
    private final static Font DEFAULT_PLAIN_MEDIUM = new Font(DefaultFont,Font.PLAIN,MediumSize);
    private final static Font DEFAULT_BOLD_MEDIUM = new Font(DefaultFont,Font.BOLD,MediumSize);
    private final static Font DEFAULT_PLAIN_LARGE = new Font(DefaultFont,Font.PLAIN,LargeSize);
    private final static Font DEFAULT_BOLD_LARGE = new Font(DefaultFont,Font.BOLD,LargeSize);

    public static Font popupFont()
    {
        return DEFAULT_PLAIN_LARGE;
    }

    public static Font labelFont()
    {
        return DEFAULT_BOLD_LARGE;
    }

    public static Font headerFontBold()
    {
        return DEFAULT_BOLD_LARGE;
    }

    public static Font headerFont()
    {
        return DEFAULT_PLAIN_LARGE;
    }

    public static Font textFontMedium()
    {
        return DEFAULT_PLAIN_MEDIUM;
    }

}
