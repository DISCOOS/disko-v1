package org.redcross.sar.wp.logistics;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent;
import org.redcross.sar.wp.AbstractDiskoWpModule;

import com.esri.arcgis.interop.AutomationException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.EnumSet;

/**
 *
 */
public class InfoPanelHandler implements IMsoUpdateListenerIf, ActionListener
{
    private final static String EMPTY_PANEL_NAME = "EmptyPanel";
    private final static String UNIT_PANEL_NAME = "UnitPanel";
    private final static String ASSIGNMENT_PANEL_NAME = "AssignmentPanel";
    private final static String ASSIGNMENT_LIST_PANEL_NAME = "AssignmentList";
    private static final String ASG_RESULT = "AsgResult";
    private static final String ASG_RETURN = "AsgReturn";
    private static final String ASG_PRINT = "AsgPrint";
    private static final String ASG_CHANGE = "AsgChange";
    private static final String UNIT_PRINT = "UnitPrint";
    private static final String UNIT_CHANGE = "UnitChange";

    private AbstractDiskoWpModule aWpModule;
    private JPanel m_infoPanel;
    private LogisticsInfoPanel m_unitInfoPanel;
    private LogisticsInfoPanel m_assignmentInfoPanel;
    private AssignmentScrollPanel m_unitAssignmentsPanel;

    private boolean m_shallReturnToList;

    private IUnitIf m_displayedUnit;
    private int m_displayedUnitSelection;

    private IAssignmentIf m_displayedAsssignment;
    private String m_displayedPanelName = "";

    private final AssignmentLabel.AssignmentLabelClickHandler m_assignmentLabelMouseListener;

    public InfoPanelHandler(JPanel anInfoPanel, AbstractDiskoWpModule aWpModule, AssignmentLabel.AssignmentLabelClickHandler aClickHandler)
    {
        m_infoPanel = anInfoPanel;
        m_assignmentLabelMouseListener = aClickHandler;
        this.aWpModule = aWpModule;

        m_infoPanel.add(new JPanel(),EMPTY_PANEL_NAME);

        initUnitInfoPanel();
        initAssignmentInfoPanel();
        initAssignmentListPanel();

        aWpModule.getMmsoEventManager().addClientUpdateListener(this);
        showPanel(EMPTY_PANEL_NAME);
    }

    private final EnumSet<IMsoManagerIf.MsoClassCode> myInterests = EnumSet.of(IMsoManagerIf.MsoClassCode.CLASSCODE_UNIT,
            IMsoManagerIf.MsoClassCode.CLASSCODE_ASSIGNMENT,
            IMsoManagerIf.MsoClassCode.CLASSCODE_PERSONNEL);

    public boolean hasInterestIn(IMsoObjectIf aMsoObject)
    {
        return myInterests.contains(aMsoObject.getMsoClassCode());
    }

    public void handleMsoUpdateEvent(MsoEvent.Update e)
    {
        if (ASSIGNMENT_PANEL_NAME.equals(m_displayedPanelName))
        {
            renderAssignment();
        } else if (ASSIGNMENT_LIST_PANEL_NAME.equals(m_displayedPanelName))
        {
            renderAssignmentList();
        } else if (UNIT_PANEL_NAME.equals(m_displayedPanelName))
        {
            renderUnit();
        }
    }

    private void initUnitInfoPanel()
    {
        m_unitInfoPanel = new LogisticsInfoPanel(5, 2, 2);
        m_infoPanel.add(m_unitInfoPanel, UNIT_PANEL_NAME);
        m_unitInfoPanel.setHeaders(new String[]{"Enhet", "5-tone", "", "Oppdrag", "Innsatstid", "Leder", "Medlemmer"});
        m_unitInfoPanel.setButtons(new String[]{"ENDRE", "SKRIV UT"}, new String[]{UNIT_CHANGE, UNIT_PRINT}, this);
    }

    private void initAssignmentInfoPanel()
    {
        m_assignmentInfoPanel = new LogisticsInfoPanel(5, 1, 4);
        m_infoPanel.add(m_assignmentInfoPanel, ASSIGNMENT_PANEL_NAME);
        m_assignmentInfoPanel.setHeaders(new String[]{"Oppdrag", "Type", "Enhet", "Prioritet", "Innsatstid", "Fritekst"});
        m_assignmentInfoPanel.setButtons(new String[]{"ENDRE", "SKRIV UT", "RESULTAT", "TILBAKE"}, new String[]{ASG_CHANGE, ASG_PRINT, ASG_RESULT, ASG_RETURN}, this);
    }

    private void initAssignmentListPanel()
    {
        // Build up a scrollpane with room for assignment labels.
        JScrollPane scrollpane = new JScrollPane();
        m_unitAssignmentsPanel = new AssignmentScrollPanel(scrollpane, new GridLayout(0,1,5,5), m_assignmentLabelMouseListener,false);
        JLabel hl = m_unitAssignmentsPanel.getHeaderLabel();
        hl.setHorizontalAlignment(SwingConstants.CENTER);
        hl.setPreferredSize(new Dimension(40,40));
        m_infoPanel.add(scrollpane, ASSIGNMENT_LIST_PANEL_NAME);
    }

    void setAssignment(IAssignmentIf anAssignment, boolean shallReturnToList)
    {
        m_displayedAsssignment = anAssignment;
        m_shallReturnToList = shallReturnToList;
        renderAssignment();
        showPanel(ASSIGNMENT_PANEL_NAME);
    }

    private void renderAssignment()
    {
        m_assignmentInfoPanel.setTopText(0, m_displayedAsssignment.getNumber() + " (" + m_displayedAsssignment.getStatus().toString() + ")");
        m_assignmentInfoPanel.setTopText(1, m_displayedAsssignment.getType().toString());
        IUnitIf unit = m_displayedAsssignment.getOwningUnit();
        if (unit != null)
        {
            m_assignmentInfoPanel.setTopText(2, unit.getUnitNumber());
        } else
        {
            m_assignmentInfoPanel.setTopText(2, "");
        }
        m_assignmentInfoPanel.setTopText(3,m_displayedAsssignment.getPriority().toString());
        m_assignmentInfoPanel.setTopText(4, m_displayedAsssignment.getType().toString());
        m_assignmentInfoPanel.setButtonEnabled(2, m_displayedAsssignment.hasBeenFinished());

        m_assignmentInfoPanel.setButtonVisible(3, m_shallReturnToList);
    }

    void setUnitSelection(IUnitIf aUnit, int aSelectionIndex)
    {
        m_displayedUnit = aUnit;
        m_displayedUnitSelection = aSelectionIndex;
        setupUnitAssignmentPanel();
        renderAssignmentList();
        showPanel(ASSIGNMENT_LIST_PANEL_NAME);
    }

    private void setupUnitAssignmentPanel()
    {
        JLabel hl = m_unitAssignmentsPanel.getHeaderLabel();
        hl.setText(UnitTableModel.getSelectedAssignmentText(m_displayedUnitSelection) + " oppdrag for " + m_displayedUnit.getUnitNumber());
        m_unitAssignmentsPanel.setSelectedUnit(m_displayedUnit);
        m_unitAssignmentsPanel.setSelectedStatus(UnitTableModel.getSelectedAssignmentStatus(m_displayedUnitSelection));
    }

    void setSelectionTransferHandler(TransferHandler aTransferHandler)
    {
        m_unitAssignmentsPanel.setTransferHandler(aTransferHandler);
    }

    private void renderAssignmentList()
    {
        java.util.List<IAssignmentIf> l = UnitTableModel.getSelectedAssignments(m_displayedUnit, m_displayedUnitSelection);
        m_unitAssignmentsPanel.setAssignmentList(l);
        m_unitAssignmentsPanel.renderPanel();
    }

    void setUnit(IUnitIf aUnit)
    {
        m_displayedUnit = aUnit;
        renderUnit();
        showPanel(UNIT_PANEL_NAME);
    }

    void renderUnit()
    {
        m_unitInfoPanel.setTopText(0, m_displayedUnit.getUnitNumber() + " " + m_displayedUnit.getStatus().toString());
        m_unitInfoPanel.setTopText(1, m_displayedUnit.getCallSign());
        m_unitInfoPanel.setTopText(2, "");
        IAssignmentIf activeAsg = m_displayedUnit.getActiveAssignment();
        if (activeAsg != null)
        {
            m_unitInfoPanel.setTopText(3, Integer.toString(activeAsg.getNumber()));
            Calendar asgAsgTime = activeAsg.getTimeAssigned();
            Calendar now = Calendar.getInstance();
            long actTimeInMillis = now.getTimeInMillis() - asgAsgTime.getTimeInMillis();
            float actTimeInHours = actTimeInMillis / 3600000;
            int displayedTimeInHours = Math.round(actTimeInHours);
            m_unitInfoPanel.setTopText(4, Integer.toString(displayedTimeInHours));
        } else
        {
            m_unitInfoPanel.setTopText(3, "");
            m_unitInfoPanel.setTopText(4, "");
        }

        m_unitInfoPanel.repaint();
    }

    private void showPanel(String aPanelName)
    {
        m_displayedPanelName = aPanelName;
        CardLayout cl = (CardLayout) m_infoPanel.getLayout();
        cl.show(m_infoPanel, aPanelName);
    }


    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        if (command == null || command.length() == 0)
        {
            return;
        }
        if (command.equalsIgnoreCase(UNIT_CHANGE))
        {
            System.out.println("Trykk 1: " + command + m_displayedUnit.getUnitNumber());
        } else if (command.equalsIgnoreCase(UNIT_PRINT))
        {
            System.out.println("Trykk 2: " + command + m_displayedUnit.getUnitNumber());
        } else if (command.equalsIgnoreCase(ASG_RESULT))
        {
            System.out.println("Trykk 3: " + command + m_displayedAsssignment.getNumber());
        } else if (command.equalsIgnoreCase(ASG_RETURN))
        {
            System.out.println("Trykk 4: " + command + m_displayedAsssignment.getNumber());
            renderAssignmentList();
            showPanel(ASSIGNMENT_LIST_PANEL_NAME);
        } else if (command.equalsIgnoreCase(ASG_PRINT))
        {
            System.out.println("Trykk 5: " + command + m_displayedAsssignment.getNumber());
        } else if (command.equalsIgnoreCase(ASG_CHANGE))
        {
            System.out.println("Trykk 6: " + command + m_displayedAsssignment.getNumber());
            try {
            	aWpModule.getApplication().getCurrentRole().selectDiskoWpModule(0);
				aWpModule.getMap().setSelected(m_displayedAsssignment, true);
			} catch (AutomationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
    }
}
