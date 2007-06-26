package org.redcross.sar.wp.logistics;

import org.redcross.sar.gui.DiskoBorder;
import org.redcross.sar.gui.renderers.IconRenderer;
import org.redcross.sar.mso.data.IAssignmentIf;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.MessageFormat;


public class AssignmentLabel extends JLabel implements MouseListener, FocusListener
{
    private final static Border LabelBorder = new DiskoBorder(2, 6, false);

    private final static Dimension LabelDimension = new Dimension(50, 50);
    private IAssignmentIf m_assignment;
    private boolean m_isSelected;

    AssignmentLabelActionHandler m_actionHandler;

    public AssignmentLabel(IconRenderer.AssignmentIcon anIcon, AssignmentLabelActionHandler anActionHandler)
    {
        super();
        setAssignmentIcon(anIcon);
        initLabel(anActionHandler);
    }

    public AssignmentLabel(IAssignmentIf anAssignment, AssignmentLabelActionHandler anActionHandler)
    {
        super();
        setAssignment(anAssignment);
        initLabel(anActionHandler);
    }

    private void initLabel(AssignmentLabelActionHandler anActionHandler)
    {
        m_actionHandler = anActionHandler;
        setFocusable(true);
        addMouseListener(this);
        addFocusListener(this);
        m_isSelected = false;
        setAppearence();
    }

    private void setAppearence()
    {
        if (m_assignment != null)
        {
            setBackground(Color.WHITE);
            setOpaque(true);
            setBorder(LabelBorder);
            setPreferredSize(LabelDimension);
            setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    private void setSelected(boolean isSelected)
    {
        if (m_assignment != null)
        {

        } else
        {
            Icon icon = getIcon();
            if (icon instanceof IconRenderer.AssignmentIcon)
            {
                ((IconRenderer.AssignmentIcon) icon).setSelected(isSelected);
            }
        }
        repaint();
    }

    public void setAssignmentIcon(IconRenderer.AssignmentIcon anIcon)
    {
        super.setIcon(anIcon);
        setText("");
        m_assignment = null;
    }

    public void setAssignment(IAssignmentIf anAssignment)
    {
        m_assignment = anAssignment;
        setText(MessageFormat.format("{0}: {1} {2}", anAssignment.getPrioritySequence(), Integer.toString(anAssignment.getNumber()), anAssignment.getTypeText()));
        setIcon(null);
    }

    public IAssignmentIf getAssignment()
    {
        if (m_assignment != null)
        {
            return m_assignment;
        }
        Icon icon = getIcon();
        return icon instanceof IconRenderer.AssignmentIcon ? ((IconRenderer.AssignmentIcon) icon).getAssignment() : null;
    }

    public void mouseClicked(MouseEvent e)
    {
        //Since the user clicked on us, let's get focus!
        requestFocus();
        setSelected(true);
        m_actionHandler.handleClick(getAssignment());

    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }

    public void mousePressed(MouseEvent e)
    {
    }

    public void mouseReleased(MouseEvent e)
    {
    }

    @Override
    public boolean isFocusable()
    {
        return true;
    }


    public void focusGained(FocusEvent e)
    {
        System.out.println("Focus gained: " + getAssignment());
    }

    public void focusLost(FocusEvent e)
    {
        setSelected(false);
        System.out.println("Focus lost: " + getAssignment());
    }

    public static interface AssignmentLabelActionHandler
    {
        public void handleClick(IAssignmentIf anAssignment);
    }
}
