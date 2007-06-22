package org.redcross.sar.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;

/**
 * Created by IntelliJ IDEA.
 * User: Stian
 * Date: 12.jun.2007
 * Time: 12:43:19
 * To change this template use File | Settings | File Templates.
 */
public class DiskoBorder implements Border
{
    private final int radius;
    private final int halfThickness;
    private final boolean doubleLine;
    private Insets insets;
    private final Stroke linestroke;
    boolean top;
    boolean left;
    boolean bottom;
    boolean right;

    public DiskoBorder(int aThickness, int aDiameter, boolean isDouble)
    {
        radius = (aDiameter + 1) / 2;
        halfThickness = (aThickness + 1) / 2;
        doubleLine = isDouble;
        linestroke = new BasicStroke(halfThickness * 2);
        insets = doubleLine ?
                new Insets(radius * 2 + halfThickness * 2, radius * 2 + halfThickness * 2, radius + halfThickness * 2, radius + halfThickness * 2) :
                new Insets(radius * 2 + halfThickness * 4, radius * 2 + halfThickness * 4, radius * 2 + halfThickness * 4, radius * 2 + halfThickness * 4);
        top = true;
        left = true;
        bottom = true;
        right = true;
    }

    public DiskoBorder(int aThickness, int aDiameter, boolean showTop, boolean showLeft, boolean showBottom, boolean showRight)
    {
        radius = (aDiameter + 1) / 2;
        halfThickness = (aThickness + 1) / 2;
        doubleLine = false;
        linestroke = new BasicStroke(halfThickness * 2);
        insets = new Insets(radius * 2 + halfThickness * 4, radius * 2 + halfThickness * 4, radius * 2 + halfThickness * 4, radius * 2 + halfThickness * 4);
        top = showTop;
        left = showLeft;
        bottom = showBottom;
        right = showRight;
    }

    public Insets getBorderInsets(Component c)
    {
        return insets;
    }

    public boolean isBorderOpaque()
    {
        return true;
    }


    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
    {
        Graphics2D g2 = (Graphics2D) g;
        Stroke oldStroke = g2.getStroke();

        g.setColor(c.getForeground());
        g2.setStroke(linestroke);

        Arc2D corner = new Arc2D.Float();
        Line2D line = new Line2D.Double();

        if (doubleLine)
        {
            int horDispl = radius;
            int vertDispl = radius + halfThickness;
            g.drawRoundRect(x + halfThickness + horDispl,
                    y + halfThickness + vertDispl,
                    width - halfThickness * 2 - horDispl,
                    height - halfThickness * 2 - vertDispl,
                    radius * 2, radius * 2);

            // lower left corner
            corner.setArc(x + halfThickness, y + height - radius * 2 - halfThickness - vertDispl, radius * 2, radius * 2, 180, 90, Arc2D.OPEN);
            g2.draw(corner);
            // left side
            line.setLine(x + halfThickness, y + halfThickness + radius, x + halfThickness, y + height - radius - halfThickness - vertDispl);
            g2.draw(line);
            // upper left corner
            corner.setArc(x + halfThickness, y + halfThickness, radius * 2, radius * 2, 90, 90, Arc2D.OPEN);
            g2.draw(corner);
            // top side
            line.setLine(x + halfThickness + radius, y + halfThickness, x + width - radius - halfThickness - horDispl, y + halfThickness);
            g2.draw(line);
            // upper right corner
            corner.setArc(x + width - radius * 2 - halfThickness - horDispl, y + halfThickness, radius * 2, radius * 2, 0, 90, Arc2D.OPEN);
            g2.draw(corner);
        } else
        {
            int startInset, endInset;
            // lower left corner
            if (left && bottom)
            {
                corner.setArc(x + halfThickness, y + height - radius * 2 - halfThickness, radius * 2, radius * 2, 180, 90, Arc2D.OPEN);
                g2.draw(corner);
            }
            // left side
            if (left)
            {
                startInset = top ? radius : 0;
                endInset = bottom ? radius : 0;
                line.setLine(x + halfThickness, y + halfThickness + startInset, x + halfThickness, y + height - endInset - halfThickness);
                g2.draw(line);
            }
            // upper left corner
            if (left && top)
            {
                corner.setArc(x + halfThickness, y + halfThickness, radius * 2, radius * 2, 90, 90, Arc2D.OPEN);
                g2.draw(corner);
            }
            // top side
            if (top)
            {
                startInset = left ? radius : 0;
                endInset = right ? radius : 0;
                line.setLine(x + halfThickness + startInset, y + halfThickness, x + width - endInset - halfThickness, y + halfThickness);
                g2.draw(line);
            }
            // upper right corner
            if (top && right)
            {
                corner.setArc(x + width - radius * 2 - halfThickness, y + halfThickness, radius * 2, radius * 2, 0, 90, Arc2D.OPEN);
                g2.draw(corner);
            }
            // right side
            if (right)
            {
                startInset = top ? radius : 0;
                endInset = bottom ? radius : 0;
                line.setLine(x + width - halfThickness, y + halfThickness + startInset, x + width - halfThickness, y + height - endInset - halfThickness);
                g2.draw(line);
            }
            // lower right corner
            if (right && bottom)
            {
                corner.setArc(x + width - radius * 2 - halfThickness, y + height - radius * 2 - halfThickness, radius * 2, radius * 2, 270, 90, Arc2D.OPEN);
                g2.draw(corner);
            }
            // bottom side
            if (bottom)
            {
                startInset = left ? radius : 0;
                endInset = right ? radius : 0;
                line.setLine(x + halfThickness + startInset, y + height - halfThickness, x + width - endInset - halfThickness, y + height - halfThickness);
                g2.draw(line);
            }
        }
        g2.setStroke(oldStroke);
    }

    public static void main(String args[])
    {
        JFrame fr = new JFrame();
        fr.setSize(400, 500);
        JLabel label = new JLabel("Testing");
        label.setBorder(new DiskoBorder(12, 56, false));
        label.setBackground(Color.YELLOW);
        label.setOpaque(true);
        JButton butt = new JButton("Test butt");
        butt.setBorder(new DiskoBorder(3, 6, true,false,true,true));
        fr.getContentPane().add(label, BorderLayout.NORTH);
        fr.getContentPane().add(butt, BorderLayout.SOUTH);
        fr.setVisible(true);
        fr.addWindowListener(new WindowAdapter()
        {

            public void windowClosed(WindowEvent e)
            {
                System.exit(0);
            }
        });
    }

}
