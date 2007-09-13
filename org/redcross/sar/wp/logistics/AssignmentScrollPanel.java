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
 * <p/>
 * The panel shows assignment labels for a given list of assignments.
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
    private int m_firstIndex = 0;

    /**
     * Index of last item to show.
     * if equal to -1, show all items.
     */
    private int m_lastIndex = -1;

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

    /**
     * Selected unit if the panel contains assignments for a specific unit.
     */
    private IUnitIf m_selectedUnit;

    /**
     * Tell if icons shall be shown in the labels.
     */
    private final boolean m_showIcons;

    /**
     * Action handler for the labels
     */
    private final AssignmentLabel.AssignmentLabelActionHandler m_actionHandler;

    /**
     * Constructor.
     * <p/>
     * Define a panel with a given FlowLayout manager.
     *
     * @param aScrollPane     The surrounding scroll pane.
     * @param aLayoutManager  The layout manager used by the panel.
     * @param anActionHandler The {@link AssignmentLabel.AssignmentLabelActionHandler} that shall be used by te labels.
     * @param showIcons       <code>true</code> if icons shall be shown in the labels, <code>false</code> otherwise.
     * @see DiskoScrollPanel
     */
    public AssignmentScrollPanel(JScrollPane aScrollPane, FlowLayout aLayoutManager, AssignmentLabel.AssignmentLabelActionHandler anActionHandler, boolean showIcons)
    {
        super(aScrollPane, aLayoutManager);
        m_showIcons = showIcons;
        m_actionHandler = anActionHandler;
        initPanel();
    }

    /**
     * Constructor.
     * <p/>
     * Define a panel with a given GridLayout manager.
     *
     * @param aScrollPane     The surrounding scroll pane.
     * @param aLayoutManager  The layout manager used by the panel.
     * @param anActionHandler The {@link AssignmentLabel.AssignmentLabelActionHandler} that shall be used by te labels.
     * @param showIcons       <code>true</code> if icons shall be shown in the labels, <code>false</code> otherwise.
     * @see DiskoScrollPanel
     */
    public AssignmentScrollPanel(JScrollPane aScrollPane, GridLayout aLayoutManager, AssignmentLabel.AssignmentLabelActionHandler anActionHandler, boolean showIcons)
    {
        super(aScrollPane, aLayoutManager);
        m_showIcons = showIcons;
        m_actionHandler = anActionHandler;
        initPanel();
    }

    /**
     * Constructor.
     * <p/>
     * Define a panel with a given layout manager.
     *
     * @param aScrollPane      The surrounding scroll pane.
     * @param aLayoutManager   The layout manager used by the panel.
     * @param aHgap            Horizontal gap between labels.
     * @param aVgap            Vertical gap between labels.
     * @param isHorizontalFlow <code>true</code> if labels shall be displayed in horizontal rows, <code>false</code> if the
     *                         shall be shown in vertical columns.
     * @param anActionHandler  The {@link AssignmentLabel.AssignmentLabelActionHandler} that shall be used by te labels.
     * @param showIcons        <code>true</code> if icons shall be shown in the labels, <code>false</code> otherwise.
     * @see DiskoScrollPanel
     */
    public AssignmentScrollPanel(JScrollPane aScrollPane, LayoutManager aLayoutManager, int aHgap, int aVgap, boolean isHorizontalFlow, AssignmentLabel.AssignmentLabelActionHandler anActionHandler, boolean showIcons)
    {
        super(aScrollPane, aLayoutManager, aHgap, aVgap, isHorizontalFlow);
        m_showIcons = showIcons;
        m_actionHandler = anActionHandler;
        initPanel();
    }

    private void initPanel()
    {

        setFocusable(true);
        setEnabled(true);

        addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent e)
            {
            }

            public void focusLost(FocusEvent e)
            {
            }
        });

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
     *
     * @return The actual list.
     */
    public List<IAssignmentIf> getAssignmentList()
    {
        return m_assignmentList;
    }

    /**
     * Get first index to show.
     */
    public int getFirstIndex()
    {
        return m_firstIndex;
    }

    /**
     * Set first index to show.
     */
    public void setFirstIndex(int anIndex)
    {
        m_firstIndex = anIndex;
    }


    /**
     * Get last index to show.
     */
    public int getLastIndex()
    {
        return m_lastIndex;
    }

    /**
     * Set last index to show.
     * <p/>
     * If equal to -1, show all items.
     */
    public void setLastIndex(int anIndex)
    {
        m_lastIndex = anIndex;
    }

    /**
     * Get the current {@link IAssignmentIf.AssignmentStatus} for the assigments in the panel.
     */
    public IAssignmentIf.AssignmentStatus getSelectedStatus()
    {
        return m_selectedStatus;
    }

    /**
     * Set the current {@link IAssignmentIf.AssignmentStatus} for the assigments in the panel.
     */
    public void setSelectedStatus(IAssignmentIf.AssignmentStatus aSelectedStatus)
    {
        m_selectedStatus = aSelectedStatus;
    }


    /**
     * Get the {@link org.redcross.sar.mso.data.IUnitIf} owning the assigments in the panel.
     */
    public IUnitIf getSelectedUnit()
    {
        return m_selectedUnit;
    }

    /**
     * Set the {@link org.redcross.sar.mso.data.IUnitIf} owning the assigments in the panel.
     */
    public void setSelectedUnit(IUnitIf aSelectedUnit)
    {
        m_selectedUnit = aSelectedUnit;
    }

    /**
     * Clear the set of selected assignments.
     */
    public void clearSelected()
    {
        m_selected.clear();
    }

    /**
     * Add an assignments to the set of selected assignments.
     */
    public void addSelected(IAssignmentIf anAsg)
    {
        m_selected.add(anAsg);
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

        int firstIndex = Math.max(m_firstIndex, 0);
        int lastIndex = m_lastIndex < 0 ? m_assignmentList.size() - 1 : Math.min(m_assignmentList.size() - 1, m_lastIndex);
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
                    m_labels.add(new DTAssignmentLabel(icon, m_actionHandler, getTransferHandler())); // Inherit transfer handler from panel
                } else
                {
                    m_labels.get(iv).setAssignmentIcon(icon);
                }
            } else
            {
                if (m_labels.size() == iv)
                {
                    m_labels.ensureCapacity(lastIndex - firstIndex + 1);
                    m_labels.add(new DTAssignmentLabel(asg, m_actionHandler, getTransferHandler())); // Inherit transfer handler from panel
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
     *
     * @return The calculated number.
     */
    public int getMaxNonscrollItems()
    {
        return super.getMaxNonscrollItems(IconRenderer.AssignmentIcon.getIconSize());
    }

    public void setSelectedAssignment(IAssignmentIf anAssignment)
    {
        for (DTAssignmentLabel label : m_labels)
        {
            if (label.getAssignment() == anAssignment)
            {
                label.setSelected(true);
            } else if (label.isSelected())
            {
                label.setSelected(false);
            }
        }
    }

}