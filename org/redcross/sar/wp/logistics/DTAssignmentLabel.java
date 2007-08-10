package org.redcross.sar.wp.logistics;
/*
 * DTAssignmentLabel.java is used by the 1.4 DragLabelDemo.java example.
 */


import org.redcross.sar.gui.renderers.IconRenderer;
import org.redcross.sar.mso.data.IAssignmentIf;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * A subclass of {@link AssignmentLabel} that supports Data Transfer.
 */

public class DTAssignmentLabel extends AssignmentLabel implements MouseMotionListener
{
    private MouseEvent firstMouseEvent = null;

    public DTAssignmentLabel(IconRenderer.AssignmentIcon anIcon, AssignmentLabelActionHandler anActionHandler, TransferHandler aTransferHandler)
    {
        super(anIcon, anActionHandler);
        initLabel(aTransferHandler);
    }

    public DTAssignmentLabel(IAssignmentIf anAssignment, AssignmentLabelActionHandler anActionHandler, TransferHandler aTransferHandler)
    {
        super(anAssignment,anActionHandler);
        initLabel(aTransferHandler);
    }

    private void initLabel(TransferHandler aTransferHandler)
    {
        setEnabled(true);
        addMouseMotionListener(this);
        setTransferHandler(aTransferHandler);
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        //Don't bother to drag if there is no assignment.
        if (getAssignment() == null)
        {
            return;
        }

        firstMouseEvent = e;
        e.consume();
    }

    public void mouseDragged(MouseEvent e)
    {
        //Don't bother to drag if the component represents no assignment
        if (getAssignment() == null)
        {
            return;
        }

        if (firstMouseEvent != null)
        {
            e.consume();


            int action = TransferHandler.MOVE;

            int dx = Math.abs(e.getX() - firstMouseEvent.getX());
            int dy = Math.abs(e.getY() - firstMouseEvent.getY());
            //Arbitrarily define a 5-pixel shift as the
            //official beginning of a drag.
            if (dx > 5 || dy > 5)
            {
                //This is a drag, not a click.
                JComponent c = (JComponent) e.getSource();
                TransferHandler handler = c.getTransferHandler();
                //Tell the transfer handler to initiate the drag.
                handler.exportAsDrag(c, firstMouseEvent, action);
                firstMouseEvent = null;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        firstMouseEvent = null;
    }

    public void mouseMoved(MouseEvent e)
    {
    }
}
