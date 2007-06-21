package org.redcross.sar.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Stian
 * Date: 12.jun.2007
 * Time: 12:43:19
 * To change this template use File | Settings | File Templates.
 */
public class DiskoBorder implements Border
{
    private int radius;
    private int thickness;

    DiskoBorder(int aRadius, int aThickness)
    {
        radius = aRadius;
        thickness = aThickness;
    }

    public Insets getBorderInsets(Component c)
    {
        return new Insets(radius*2 + thickness * 2,radius*2 + thickness * 2, radius + thickness*2,  radius + thickness*2);
    }


    public boolean isBorderOpaque()
    {
        return true;
    }


    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
    {
        System.out.println(x + " " + y + " " + width + " " + height);
        Graphics2D g2 = (Graphics2D) g;
        Stroke oldStroke = g2.getStroke();
        Stroke linestroke = new BasicStroke(thickness);
//        Stroke widestroke = new BasicStroke(radius);

        int vertDispl = radius + thickness;
        int horDispl = radius / 2 + thickness;

        g2.setStroke(linestroke);
        g.drawRoundRect(x + thickness / 2,
                y + thickness / 2,
                width - 1 - radius - thickness,
                height - 2 - radius - thickness,
                radius, radius);

        g.setColor(c.getBackground());
//        g.setColor(Color.red);
        g.drawRoundRect(x + thickness / 2 + horDispl,
                y + thickness / 2 + vertDispl,
                width - 1 - radius  - thickness- horDispl,
                height - 2 - radius  - thickness- vertDispl,
                radius, radius);

        g.setColor(c.getForeground());
        g2.setStroke(linestroke);
        g.drawRoundRect(x + thickness / 2 + horDispl,
                y + thickness / 2 + vertDispl,
                width - 1 - radius - thickness,
                height - 2 - radius - thickness,
                radius, radius);
        g2.setStroke(oldStroke);
    }

    public static void main(String args[])
    {
        JFrame fr = new JFrame();
        fr.setSize(400, 500);
        JLabel label = new JLabel("Testing");
        label.setBorder(new DiskoBorder(5, 2));
        JButton butt = new JButton("Test butt");
        butt.setBorder(new DiskoBorder(5,2));
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
