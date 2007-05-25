package org.redcross.sar.wp.logistics;

import org.redcross.sar.mso.data.IAssignmentIf;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class AssignmentLabel extends JLabel implements MouseListener
{
    public AssignmentLabel(LogisticsIcon.AssignmentIcon anIcon)
    {
        super(anIcon);
        setFocusable(true);
        addMouseListener(this);
    }

    public void setAssignmentIcon(LogisticsIcon.AssignmentIcon anIcon)
    {
        super.setIcon(anIcon);
    }

    public  IAssignmentIf getAssignment()
    {
        Icon icon = getIcon();
        return icon instanceof LogisticsIcon.AssignmentIcon ? ((LogisticsIcon.AssignmentIcon)icon).getAssignment() : null;
    }

    public void mouseClicked(MouseEvent e)
    {
        //Since the user clicked on us, let's get focus!
        System.out.println("Mouse clicked");
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }

    public void mousePressed(MouseEvent e)
    {
        //Since the user pressed on us, let's get focus!
    }

    public void mouseReleased(MouseEvent e)
    {
    }
}
