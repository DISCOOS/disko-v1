package org.redcross.sar.wp.logistics;

import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.gui.NavBar;
import org.redcross.sar.mso.data.AssignmentImpl;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.wp.AbstractDiskoWpModule;
import org.redcross.sar.wp.TestData.BuildTestData;

import javax.swing.*;
import java.text.MessageFormat;
import java.util.EnumSet;

/**
 *
 */
public class DiskoWpLogisticsImpl extends AbstractDiskoWpModule implements IDiskoWpLogistics
{
    LogisticsPanel m_logisticsPanel;

    public DiskoWpLogisticsImpl(IDiskoRole role)
    {
        super(role);
        initialize();
        if (false)
        {
            BuildTestData.createUnitsAndAssignments(getMsoModel());
            BuildTestData.createMessages(getMsoModel());
        }
    }

    private void initialize()
    {
        loadProperties("properties");
        assignWpBundle("org.redcross.sar.wp.logistics.logistics");

        m_logisticsPanel = new LogisticsPanel(this);
        layoutComponent(m_logisticsPanel.getPanel());

        //lp.getMapPanel().add((JComponent)super.getMap());

//        DiskoMap map = getMap();
//        map.setIsEditable(true);
//        layoutComponent(map);
    }

    public void activated()
    {
        super.activated();
        NavBar navBar = getApplication().getNavBar();
        EnumSet<NavBar.ToolCommandType> myTools =
                EnumSet.of(NavBar.ToolCommandType.ZOOM_IN_TOOL);
        myTools.add(NavBar.ToolCommandType.ZOOM_OUT_TOOL);
        myTools.add(NavBar.ToolCommandType.PAN_TOOL);
        myTools.add(NavBar.ToolCommandType.ZOOM_FULL_EXTENT_COMMAND);
        myTools.add(NavBar.ToolCommandType.ZOOM_TO_LAST_EXTENT_FORWARD_COMMAND);
        myTools.add(NavBar.ToolCommandType.ZOOM_TO_LAST_EXTENT_BACKWARD_COMMAND);
        myTools.add(NavBar.ToolCommandType.MAP_TOGGLE_COMMAND);
        myTools.add(NavBar.ToolCommandType.SELECT_FEATURE_TOOL);

        navBar.showButtons(myTools);
    }

    /* (non-Javadoc)
    * @see com.geodata.engine.disko.task.DiskoAp#getName()
    */
    public String getName()
    {
        return "Logistikk";
    }

    /* (non-Javadoc)
     * @see com.geodata.engine.disko.task.DiskoAp#cancel()
     */
    public void cancel()
    {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see com.geodata.engine.disko.task.DiskoAp#finish()
     */
    public void finish()
    {
        // TODO Auto-generated method stub
    }


    private String[] options = null;

    public boolean confirmTransfer(IAssignmentIf anAssignment, IAssignmentIf.AssignmentStatus aTargetStatus, IUnitIf aTargetUnit)
    {
        if (options == null)
        {
            options = new String[]{getText("yes.text"), "Nei"};
        }
        IUnitIf owningUnit = anAssignment.getOwningUnit();
        IAssignmentIf.AssignmentStatus sourceStatus = anAssignment.getStatus();

        String question;
        if (owningUnit == aTargetUnit)
        {
            if (aTargetStatus == IAssignmentIf.AssignmentStatus.ALLOCATED && sourceStatus == aTargetStatus)
            {
                question = "confirm_assignmentTransfer_q4.text";
            } else
            {
                question = "confirm_assignmentTransfer_q3.text";
            }
        } else if (aTargetUnit != null)
        {
            question = "confirm_assignmentTransfer_q2.text";
        } else
        {
            question = "confirm_assignmentTransfer_q1.text";
        }

        String unitNumber = aTargetUnit != null ? aTargetUnit.getUnitNumber() : "";
        question = getText(question);

        int n = JOptionPane.showOptionDialog(m_logisticsPanel.getPanel(),
                MessageFormat.format(question, anAssignment.getNumber(), AssignmentImpl.getStatusText(aTargetStatus), unitNumber),
                getText("confirm_assignmentTransfer_title.text"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        return n == 0;
    }

    public void showTransferWarning()
    {
        showWarning(getText("transfer_warning.text"));
    }

}
