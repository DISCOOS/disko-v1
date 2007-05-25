package org.redcross.sar.wp.logistikk;
/*
 * DTAssignmentLabel.java is used by the 1.4 DragLabelDemo.java example.
 */


import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;


//A subclass of Picture that supports Data Transfer.
public class DTAssignmentLabel extends AssignmentLabel implements MouseMotionListener
{
    private MouseEvent firstMouseEvent = null;

    public DTAssignmentLabel(LogisticsIcon.AssignmentIcon anIcon,TransferHandler aTransferHandler)
    {
        super(anIcon);
        setEnabled(true);
        addMouseMotionListener(this);
        setTransferHandler(aTransferHandler);

//        //Add the cut/copy/paste actions to the action map.
//        //This step is necessary because the menu's action listener
//        //looks for these actions to fire.
//        ActionMap map = this.getActionMap();
//        map.put(TransferHandler.getCutAction().getValue(Action.NAME),
//                TransferHandler.getCutAction());
//        map.put(TransferHandler.getCopyAction().getValue(Action.NAME),
//                TransferHandler.getCopyAction());
//        map.put(TransferHandler.getPasteAction().getValue(Action.NAME),
//                TransferHandler.getPasteAction());
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        //Don't bother to drag if there is no icon.
        if (getIcon() == null)
        {
            return;
        }

        firstMouseEvent = e;
        e.consume();
    }

    public void mouseDragged(MouseEvent e)
    {
        //Don't bother to drag if the component displays no icon.
        if (getIcon() == null)
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
