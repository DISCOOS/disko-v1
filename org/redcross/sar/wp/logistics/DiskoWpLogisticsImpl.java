package org.redcross.sar.wp.logistics;

import com.esri.arcgis.interop.AutomationException;
import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.gui.NavBar;
import org.redcross.sar.mso.data.AssignmentImpl;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.wp.AbstractDiskoWpModule;

import javax.swing.*;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.EnumSet;
import java.util.ResourceBundle;
/**
 * Created by IntelliJ IDEA.
 * User: vinjar
 * Date: 11.apr.2007
 * To change this template use File | Settings | File Templates.
 */

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
    }

    private void initialize()
    {
        loadProperties("properties");

        try
        {
            wpBundle = ResourceBundle.getBundle("org.redcross.sar.wp.logistics.logistics");
            System.out.println(wpBundle);
        }
        catch (java.util.MissingResourceException e)
        {
            System.out.println("Classname " + e.getClassName());
            System.out.println("Key " + e.getKey());
            e.printStackTrace();
        }

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
        EnumSet<NavBar.ToolCommandType> myInterests =
                EnumSet.of(NavBar.ToolCommandType.ZOOM_IN_TOOL);
        myInterests.add(NavBar.ToolCommandType.ZOOM_OUT_TOOL);
        myInterests.add(NavBar.ToolCommandType.PAN_TOOL);
        myInterests.add(NavBar.ToolCommandType.ZOOM_FULL_EXTENT_COMMAND);
        myInterests.add(NavBar.ToolCommandType.ZOOM_TO_LAST_EXTENT_FORWARD_COMMAND);
        myInterests.add(NavBar.ToolCommandType.ZOOM_TO_LAST_EXTENT_BACKWARD_COMMAND);
        myInterests.add(NavBar.ToolCommandType.MAP_TOGGLE_COMMAND);
        navBar.showButtons(myInterests);
        try
        {
            getMap().partialRefresh(null);
        }
        catch (AutomationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
                MessageFormat.format(question, anAssignment.getNumber(), AssignmentImpl.getEnumText(aTargetStatus),unitNumber),
                getText("confirm_assignmentTransfer_title.text"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);
        System.out.println(n);
        return n == 0;
    }

    public void showTransferWarning()
    {
        showWarning(getText("transfer_warning.text"));
    }

}
