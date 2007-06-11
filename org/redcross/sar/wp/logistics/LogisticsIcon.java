package org.redcross.sar.wp.logistics;

import org.redcross.sar.app.Utils;
import org.redcross.sar.mso.data.*;

import javax.swing.*;
import java.awt.*;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;

/**
 *
 */
public class LogisticsIcon implements Icon {
    // Fixed design parameters
    private final static int arcDiam = 8;
    private final static int innerTop = 5;
    private final static int innerLeft = 3;

    // Not so very fixed design parameter
    private final Color selectedColor = Color.pink;

    // Parameters related to source icon
    protected Image m_iconImage;
    private boolean m_isLocatedLeft;

    // Parameters related to current design
    private int m_width = 50;
    private int m_height = 50;
    private float m_iconResizeFactor;
    private String m_iconText;
    private boolean m_isMultiple;
    private boolean m_hasBorder;

    // Selection flag
    private boolean m_isSelected;

    protected static Properties m_iconProperties;


    public LogisticsIcon(Image anIconImage, boolean isLocatedLeft, String anIconText, int aWidth, int aHeight, float aResizeFactor, boolean isMultiple, boolean hasBorder, boolean isSelected) {
        m_iconImage = anIconImage;
        m_isLocatedLeft = isLocatedLeft;
        m_iconText = anIconText;
        m_width = aWidth;
        m_height = aHeight;
        m_iconResizeFactor = aResizeFactor;
        m_isMultiple = isMultiple;
        m_hasBorder = hasBorder;
        m_isSelected = isSelected;
    }

    public void setIconImage(Image anIconImage) {
        m_iconImage = anIconImage;
    }


    public void setIconText(String anIconText) {
        m_iconText = anIconText;
    }

    public void setSelected(boolean isSelected) {
        m_isSelected = isSelected;
    }

    public void setMultiple(boolean isMultiple) {
        m_isMultiple = isMultiple;
    }

    public void setLocatedLeft(boolean isLocatedLeft) {
        m_isLocatedLeft = isLocatedLeft;
    }

    public void setWidth(int aWidth) {
        m_width = aWidth;
    }

    public void setHeight(int aHeight) {
        m_height = aHeight;
    }

    public void setIconResizeFactor(float aResizeFactor) {
        m_iconResizeFactor = aResizeFactor;
    }

    public void setHasBorder(boolean hasBorder) {
        m_hasBorder = hasBorder;
    }

    public int getIconWidth() {
        return m_width;
    }

    public int getIconHeight() {
        return m_height;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        int dx1, dy1, dx2, dy2;
        int sx1, sy1, sx2, sy2;
        int tx, ty;
        int sw, sh;

        if (m_iconImage == null) {
            sw = 0;
            sh = 0;
            dx1 = 0;
            dy1 = 0;
            sx1 = 0;
            sy1 = 0;
            tx = m_width / 2 - (int) (5 * m_iconResizeFactor);
            ty = m_height / 2 + (int) (10 * m_iconResizeFactor);
        } else if (m_isLocatedLeft) {
            sw = 25;
            sh = 25;
            dx1 = innerLeft + 1;
            dy1 = innerTop + 6;
            sx1 = 3;
            sy1 = 10;
            tx = dx1 + (int) (20 * m_iconResizeFactor);
            ty = dy1 + (int) (20 * m_iconResizeFactor);
        } else {
            sw = 25;
            sh = 30;
            dx1 = m_width - sw - 6;
            dy1 = innerTop + 1;
            sx1 = 22;
            sy1 = 3;
            tx = 5;
            ty = m_height - 5;
        }
        dx2 = dx1 + (int) (sw * m_iconResizeFactor) - 1;
        dy2 = dy1 + (int) (sh * m_iconResizeFactor) - 1;
        sx2 = sx1 + sw - 1;
        sy2 = sy1 + sh - 1;

        g.translate(x, y);
        Graphics2D g2 = (Graphics2D) g;
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(2));

        Color bgColor;
        if (m_isSelected) {
            bgColor = selectedColor;
        } else {
            bgColor = Color.WHITE;
        }

        g.setColor(bgColor);
        if (m_hasBorder) {
            g.fillRoundRect(0, 0, m_width, m_height, arcDiam, arcDiam);
        } else {
            g.fillRect(0, 0, m_width, m_height);
        }

        g.setColor(c.getForeground());
        if (m_hasBorder) {
            g2.drawRoundRect(1, 1, m_width - 2, m_height - 2, arcDiam, arcDiam);
        }

        if (m_iconImage != null) {
            g.drawImage(m_iconImage, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgColor, null);
        }

        if (m_hasBorder && m_isMultiple) {
            g2.drawRoundRect(innerLeft, innerTop, m_width - 1 - innerLeft, m_height - 1 - innerTop, arcDiam, arcDiam);
        }

        g2.setStroke(oldStroke); // restore stroke

        Font oldFont = g.getFont();
        // Font size depends on string length an icon size
        int fontSize = (int) ((m_iconText.length() > 2 ? 15 : 18) * m_iconResizeFactor);
        g.setFont(oldFont.deriveFont(Font.BOLD, fontSize));
        g.drawString(m_iconText, tx, ty);
        g.setFont(oldFont);           //Restore font
        g.translate(-x, -y);   //Restore graphics object
    }

    protected static Properties getProperties() {
        if (m_iconProperties == null) {
            try {
                m_iconProperties = Utils.getProperties();
            }
            catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return m_iconProperties;
    }

    public boolean isSelectable() {
        return false;
    }

    public static class UnitIcon extends LogisticsIcon {
        static final Dimension m_iconSize = new Dimension(50, 50);

        static final HashMap<IUnitIf.UnitType, Image> m_images = new LinkedHashMap<IUnitIf.UnitType, Image>();
        IUnitIf m_unit;

        private void initImageMap() {
            String[] unitIconNames = new String[]{
                    "ManUnit.icon", // todo Use correct icon
                    "ManUnit.icon",
                    "DogUnit.icon",
                    "CarUnit.icon",  // todo Use correct icon
                    "CarUnit.icon",  // todo Use correct icon
                    "CarUnit.icon"
            };
            IUnitIf.UnitType[] unitTypes = new IUnitIf.UnitType[]{
                    IUnitIf.UnitType.COMMAND_POST,
                    IUnitIf.UnitType.TEAM,
                    IUnitIf.UnitType.DOG,
                    IUnitIf.UnitType.AIRCRAFT,
                    IUnitIf.UnitType.BOAT,
                    IUnitIf.UnitType.VEHICLE
            };

//            UnitType.COMMAND_POST.icon=icons/people.gif
//            UnitType.TEAM.icon=icons/people.gif
//            UnitType.DOG.icon=icons/dog.gif
//            UnitType.AIRCRAFT.icon=icons/people.gif
//            UnitType.BOAT.icon=icons/people.gif
//            UnitType.VEHICLE.icon=icons/car.gif


            EnumSet<IBoatIf.BoatSubType> allBoatSubTypes = EnumSet.allOf(IBoatIf.BoatSubType.class);

            for (IUnitIf.UnitType unitType : EnumSet.allOf(IUnitIf.UnitType.class)) {
                String unitTypeName = unitType.name();
                if (unitType == IUnitIf.UnitType.AIRCRAFT) {
                    for (IAircraftIf.AircraftSubType aircraftSubType : EnumSet.allOf(IAircraftIf.AircraftSubType.class)) {
                        String subTypeName = unitTypeName + "_" + aircraftSubType.name();
                        String iconName = subTypeName.toLowerCase() + ".icon";
                        System.out.println(subTypeName + " " + iconName);
                    }
                } else if (unitType == IUnitIf.UnitType.BOAT) {
                    for (IBoatIf.BoatSubType BoatSubType : EnumSet.allOf(IBoatIf.BoatSubType.class)) {
                        String subTypeName = unitTypeName + "_" + BoatSubType.name();
                        String iconName = subTypeName.toLowerCase() + ".icon";
                        System.out.println(subTypeName + " " + iconName);
                    }
                }
            }

            for (int i = 0; i < unitIconNames.length; i++) {
                try {
                    String iconName = unitIconNames[i];
                    Image image = Utils.createImageIcon(getProperties().getProperty(iconName), iconName).getImage();
                    m_images.put(unitTypes[i], image);
                }
                catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }

        public UnitIcon(IUnitIf aUnit, boolean isSelected) {
            super(null, true, null, m_iconSize.width, m_iconSize.height, 1.25F, false, false, isSelected);
            if (m_images.size() == 0) {
                initImageMap();
            }
            setUnit(aUnit);
        }

        public static Dimension getIconSize() {
            return m_iconSize;
        }

        public void setUnit(IUnitIf aUnit) {
            m_unit = aUnit;
            Image iconImage = m_images.get(aUnit.getType());
            setIconImage(iconImage);
            setIconText(Integer.toString(aUnit.getNumber()));
        }

        public IUnitIf getUnit() {
            return m_unit;
        }

        @Override
        public boolean isSelectable() {
            return true;
        }
    }

    public static class AssignmentIcon extends LogisticsIcon {
        static final Dimension m_iconSize = new Dimension(50, 50);

        IAssignmentIf m_assignment;
        java.util.List<IAssignmentIf> m_assignments;
        IUnitIf m_actUnit;
        int m_selectorIndex;

        private final boolean m_singleAssigmentIcon;

        static final HashMap<ISearchIf.SearchSubType, Image> m_searchImages = new LinkedHashMap<ISearchIf.SearchSubType, Image>();
        static final EnumSet<ISearchIf.SearchSubType> m_leftIcons = EnumSet.noneOf(ISearchIf.SearchSubType.class);

        private void initImageMap() {
            String[] searchIconNames = new String[]{
                    "PathSearch.icon",
                    "ManUnit.icon",
                    "ManUnit.icon", // todo correct icon
                    "PointSearch.icon",
                    "OnionSearch.icon",  // todo Use correct icon
                    "CarUnit.icon",  // todo Use correct icon
                    "DogUnit.icon"
            };
            ISearchIf.SearchSubType[] assignmentTypes = new ISearchIf.SearchSubType[]{
                    ISearchIf.SearchSubType.LINE,
                    ISearchIf.SearchSubType.PATROL,
                    ISearchIf.SearchSubType.URBAN,
                    ISearchIf.SearchSubType.SHORELINE,
                    ISearchIf.SearchSubType.MARINE,
                    ISearchIf.SearchSubType.AIR,
                    ISearchIf.SearchSubType.DOG
            };

            for (int i = 0; i < searchIconNames.length; i++) {
                String iconName = searchIconNames[i];
                try {

                    Image image = Utils.createImageIcon(getProperties().getProperty(iconName), iconName).getImage();
                    m_searchImages.put(assignmentTypes[i], image);
                }
                catch (Exception e) {
                    System.out.println("Icon not found " + getProperties().getProperty(iconName) + " :" + iconName);
                    //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

            m_leftIcons.add(ISearchIf.SearchSubType.DOG);
            m_leftIcons.add(ISearchIf.SearchSubType.PATROL);
            m_leftIcons.add(ISearchIf.SearchSubType.URBAN);
        }

        public AssignmentIcon(IAssignmentIf anAssignment, boolean isSelected) {
            super(null, false, null, m_iconSize.width, m_iconSize.height, 1.0F, false, true, isSelected);
            initImageMap();
            m_singleAssigmentIcon = true;
            setAssignment(anAssignment);
        }

        public AssignmentIcon(IUnitIf aUnit, int aSelectorIndex, boolean isSelected) {
            super(null, false, null, m_iconSize.width, m_iconSize.height, 1.0F, false, true, isSelected);
            initImageMap();
            m_singleAssigmentIcon = false;
            setAssignments(aUnit, aSelectorIndex);
        }

        public static Dimension getIconSize() {
            return m_iconSize;
        }

        private void setAssignmentIcon(IAssignmentIf anAssignment, boolean isMultiple) {
            if (anAssignment != null) {
                if (anAssignment instanceof ISearchIf) {
                    setIconImage(m_searchImages.get(((ISearchIf) anAssignment).getSubType()));
                } else {
                    setIconImage(m_searchImages.get(ISearchIf.SearchSubType.LINE));
                }
            } else {
                setIconImage(null);
            }
            setMultiple(isMultiple);
        }

        public void setAssignment(IAssignmentIf anAssignment) {
            // todo test on m_singleAssigmentIcon
            m_assignment = anAssignment;
            setIconText(Integer.toString(anAssignment.getNumber()));
            setAssignmentIcon(anAssignment, false);
//            m_buttonListener.setAssignment(anAssignment);
        }

        public void setAssignments(IUnitIf aUnit, int aSelectorIndex)
        {
            m_actUnit = aUnit;
            m_selectorIndex = aSelectorIndex;
            // todo test on m_singleAssigmentIcon
            m_assignments = UnitTableModel.getSelectedAssignments(m_actUnit,m_selectorIndex);
            if (m_assignments.size() == 0)
            {
                setIconText("");
                setIconImage(null);
                setHasBorder(false);
            } else {
                setIconText(Integer.toString(m_assignments.get(0).getNumber()));
                setAssignmentIcon(m_assignments.get(0), m_assignments.size() > 1);
                setHasBorder(true);
//            m_buttonListListener.setAssignmentList(theAssignments);
            }
        }

        public java.util.List<IAssignmentIf> getAssignmentList() {
            return m_assignments;
        }

        public boolean isSelectable() {
            return m_iconImage != null;
        }

        public IAssignmentIf getAssignment() {
            if (m_singleAssigmentIcon) {
                return m_assignment;
            }
            if (m_assignments != null && m_assignments.size() > 0) {
                return m_assignments.get(0);
            }
            return null;
        }
    }

    public static class InfoIcon extends LogisticsIcon {
        static final Dimension m_iconSize = new Dimension(30, 50);

        public InfoIcon(String anIconText, boolean isSelected) {
            super(null, true, null, m_iconSize.width, m_iconSize.height, 1.0F, false, false, isSelected);
            setInfo(anIconText);
        }

        public static Dimension getIconSize() {
            return m_iconSize;
        }

        public void setInfo(String anIconText) {
            if (anIconText.length() > 0) {
                setIconText("!");
            } else {
                setIconText("");
            }
        }

        @Override
        public boolean isSelectable() {
            return false;
        }
    }

}
