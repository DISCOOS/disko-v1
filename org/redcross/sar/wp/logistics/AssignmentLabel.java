package org.redcross.sar.wp.logistics;

import org.redcross.sar.mso.data.IAssignmentIf;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class AssignmentLabel extends JLabel implements MouseListener
{
    private final static Border InnerBorder = new LineBorder(Color.BLACK, 2, true);
    private final static Border OuterBorder = BorderFactory.createMatteBorder(2, 10, 2, 10, new Color(236, 233, 216));

    private final static Border LabelBorder = new CompoundBorder(OuterBorder, InnerBorder);

    private final static Dimension LabelDimension = new Dimension(50, 50);
    private IAssignmentIf m_assignment;

    AssignmentLabelClickHandler m_clickHandler;

    public AssignmentLabel(LogisticsIcon.AssignmentIcon anIcon, AssignmentLabelClickHandler aClickHandler)
    {
        super();
        setAssignmentIcon(anIcon);
        initLabel(aClickHandler);
    }

    public AssignmentLabel(IAssignmentIf anAssignment, AssignmentLabelClickHandler aClickHandler)
    {
        super();
        setAssignment(anAssignment);
        initLabel(aClickHandler);
    }

    private void initLabel(AssignmentLabelClickHandler aClickHandler)
    {
        m_clickHandler = aClickHandler;
        setFocusable(true);
        addMouseListener(this);
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

    public void setAssignmentIcon(LogisticsIcon.AssignmentIcon anIcon)
    {
        super.setIcon(anIcon);
        setText("");
        m_assignment = null;
    }

    public void setAssignment(IAssignmentIf anAssignment)
    {
        m_assignment = anAssignment;
        setText(Integer.toString(anAssignment.getNumber()) + " " + anAssignment.getType().toString());
        setIcon(null);
    }

    public IAssignmentIf getAssignment()
    {
        if (m_assignment != null)
        {
            return m_assignment;
        }
        Icon icon = getIcon();
        return icon instanceof LogisticsIcon.AssignmentIcon ? ((LogisticsIcon.AssignmentIcon) icon).getAssignment() : null;
    }

    public void mouseClicked(MouseEvent e)
    {
        //Since the user clicked on us, let's get focus!
        m_clickHandler.handleClick(getAssignment());

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

    public static interface AssignmentLabelClickHandler
    {
        public void handleClick(IAssignmentIf anAssignment);
    }
}
