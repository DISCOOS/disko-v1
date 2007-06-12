package org.redcross.sar.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.Border;

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

   DiskoBorder(int radius)
   {
      this.radius = radius;
   }

   public Insets getBorderInsets(Component c)
   {
      return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
   }


   public boolean isBorderOpaque()
   {
      return true;
   }


   public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
   {
      g.drawRoundRect(x + 1, y, width - 2, height - 1, radius, radius);
      g.drawRoundRect(x + 1, y + radius, width - 2, height - 1 - radius, radius, radius);
   }

   public static void main(String args[])
   {
      JFrame fr = new JFrame();
      fr.setSize(400, 500);
      JLabel label = new JLabel("TEsting");
      label.setBorder(new DiskoBorder(5));
      JButton butt = new JButton("Test butt");
      butt.setBorder(new DiskoBorder(5));
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
