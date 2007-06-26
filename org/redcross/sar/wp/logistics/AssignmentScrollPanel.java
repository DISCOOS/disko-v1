package org.redcross.sar.wp.logistics;

import org.redcross.sar.gui.DiskoScrollPanel;
import org.redcross.sar.gui.renderers.IconRenderer;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IUnitIf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * Scroll panel for assignments.
 */
public class AssignmentScrollPanel extends DiskoScrollPanel
{
    /**
     * List of assignments to show.
     */
    private List<IAssignmentIf> m_assignmentList;

    /**
     * Index of first item to show.
     */
    private int m_minIndex = 0;

    /**
     * Index of last item to show.
     * i f equal to -1, show all items.
     */
    private int m_maxIndex = -1;

    /**
     * Pool of icons
     */
    private final Vector<IconRenderer.AssignmentIcon> m_icons = new Vector<IconRenderer.AssignmentIcon>();

    /**
     * Pool of labels
     */
    private final Vector<DTAssignmentLabel> m_labels = new Vector<DTAssignmentLabel>();

    /**
     * Set of (user) selected assignments.
     */
    private final Set<IAssignmentIf> m_selected = new HashSet<IAssignmentIf>();

    /**
     * Common mouse adapter.
     */

    /**
     * Status of assignments listed.
     */
    private IAssignmentIf.AssignmentStatus m_selectedStatus;

    private IUnitIf m_selectedUnit;

    private final boolean m_showIcons;

    private final AssignmentLabel.AssignmentLabelActionHandler m_actionHandler;

    public AssignmentScrollPanel(JScrollPane aScrollPane, LayoutManager aLayoutManager, AssignmentLabel.AssignmentLabelActionHandler anActionHandler, boolean showIcons)
    {
        super(aScrollPane, aLayoutManager);
        m_showIcons = showIcons;
        m_actionHandler = anActionHandler;

        setFocusable(true);
        setEnabled(true);
        System.out.println(this + " " + isFocusable());


        addFocusListener(new FocusListener(){
            public void focusGained(FocusEvent e)
            {
                System.out.println("Focus gained" + e.getComponent());
            }

            public void focusLost(FocusEvent e)
            {
                System.out.println("Focus lost" + e.getComponent());
            }
        });

//        m_mouseAdapter = new MouseInputAdapter(){
//            private MouseEvent m_firstMouseEvent = null;
//            @Override
//            public void mouseClicked(MouseEvent e)
//            {
//                System.out.println("MouseClicked" + e.getComponent() + " " + getComponentAt(e.getX(),e.getY()));
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e)
//            {
//                System.out.println("MousePressed" + e.getComponent());
//                m_firstMouseEvent = e;
//                e.consume();
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e)
//            {
//                System.out.println("MouseReleased" + e.getComponent());
//                m_firstMouseEvent = null;
//            }
//
//            @Override
//            public void mouseDragged(MouseEvent e)
//            {
//                System.out.println("MouseDragged" + e.getComponent());
//                if (m_firstMouseEvent != null) {
//                    e.consume();
//
//                    //If they are holding down the control key, COPY rather than MOVE
//                    int ctrlMask = InputEvent.CTRL_DOWN_MASK;
//                    int action = ((e.getModifiersEx() & ctrlMask) == ctrlMask) ?
//                          TransferHandler.COPY : TransferHandler.MOVE;
//
//                    int dx = Math.abs(e.getX() - m_firstMouseEvent.getX());
//                    int dy = Math.abs(e.getY() - m_firstMouseEvent.getY());
//                    //Arbitrarily define a 5-pixel shift as the
//                    //official beginning of a drag.
//                    if (dx > 5 || dy > 5) {
//                        //This is a drag, not a click.
//                        JComponent c = (JComponent)e.getSource();
//                        TransferHandler handler = c.getTransferHandler();
//                        //Tell the transfer handler to initiate the drag.
//                        if (handler != null)
//                        {
//                            handler.exportAsDrag(c, m_firstMouseEvent, action);
//                        }
//                        m_firstMouseEvent = null;
//                    }
//                }
//            }
//
//            @Override
//            public void mouseMoved(MouseEvent e)
//            {
//                System.out.println("MouseMoved" + e.getComponent());
//            }
//        };
    }

    /**
     * Set list of assignments to show
     *
     * @param anAssignmentList The actual list.
     */
    public void setAssignmentList(List<IAssignmentIf> anAssignmentList)
    {
        m_assignmentList = anAssignmentList;
    }

    /**
     * Get list of assignments to show
     * @return The actual list.
     */
    public List<IAssignmentIf> getAssignmentList()
    {
        return m_assignmentList;
    }

    public int getMinIndex()
    {
        return m_minIndex;
    }

    public void setMinIndex(int aMinIndex)
    {
        m_minIndex = aMinIndex;
    }

    public int getMaxIndex()
    {
        return m_maxIndex;
    }

    public void setMaxIndex(int aMaxIndex)
    {
        m_maxIndex = aMaxIndex;
    }

    public IAssignmentIf.AssignmentStatus getSelectedStatus()
    {
        return m_selectedStatus;
    }

    public void setSelectedStatus(IAssignmentIf.AssignmentStatus aSelectedStatus)
    {
        m_selectedStatus = aSelectedStatus;
    }


    public IUnitIf getSelectedUnit()
    {
        return m_selectedUnit;
    }

    public void setSelectedUnit(IUnitIf aSelectedUnit)
    {
        m_selectedUnit = aSelectedUnit;
    }

    public void clearSelected()
    {
        m_selected.clear();
    }

    public void addSelected(IAssignmentIf anAsg)
    {
        m_selected.add(anAsg);
    }

    public void removeAll()
    {
//        for (JLabel label : m_labels)
//        {
//            for (EventListener listener : label.getListeners(MouseListener.class))
//            {
//                label.removeMouseListener((MouseListener)listener);
//            }
//            for (EventListener listener : label.getListeners(MouseMotionListener.class))
//            {
//                label.removeMouseMotionListener((MouseMotionListener)listener);
//            }
//        }
        super.removeAll();
    }

    /**
     * Draw the panel.
     * Goes through the actual list from min to max, and draws the labels into the panel.
     * Finally the panel is resized to fit the list.
     */
    public void renderPanel()
    {
        removeAll();
        if (m_assignmentList == null)
        {
            return;
        }

        int firstIndex = Math.max(m_minIndex, 0);
        int lastIndex = m_maxIndex < 0 ? m_assignmentList.size() - 1 : Math.min(m_assignmentList.size() - 1, m_maxIndex);
        int iv = 0;
        IconRenderer.AssignmentIcon icon;

        for (int i = firstIndex; i <= lastIndex; i++)
        {
            IAssignmentIf asg = m_assignmentList.get(i);

            if (m_showIcons)
            {
                if (m_icons.size() == iv)
                {
                    icon = new IconRenderer.AssignmentIcon(asg, m_selected.contains(asg), null);
                    m_icons.ensureCapacity(lastIndex - firstIndex + 1);
                    m_icons.add(icon);
                } else
                {
                    icon = m_icons.get(iv);
                    icon.setAssignment(asg);
                    icon.setSelected(m_selected.contains(asg));
                }
                if (m_labels.size() == iv)
                {
                    m_labels.ensureCapacity(lastIndex - firstIndex + 1);
                    m_labels.add(new DTAssignmentLabel(icon, m_actionHandler,getTransferHandler())); // Inherit transfer handler from panel
                } else
                {
                    m_labels.get(iv).setAssignmentIcon(icon);
                }
            } else
            {
                if (m_labels.size() == iv)
                {
                    m_labels.ensureCapacity(lastIndex - firstIndex + 1);
                    m_labels.add(new DTAssignmentLabel(asg, m_actionHandler,getTransferHandler())); // Inherit transfer handler from panel
                } else
                {
                    m_labels.get(iv).setAssignment(asg);
                }
            }
            JLabel label = m_labels.get(iv);
            add(label);
            iv++;
        }
        resizePanel(true);
    }

    /**
     * Calculate the number of labels that can be located within the pane without scrolling.
     * @return The calculated number.
     */
    public int getMaxNonscrollItems()
    {
        return super.getMaxNonscrollItems(IconRenderer.AssignmentIcon.getIconSize());
    }
}
