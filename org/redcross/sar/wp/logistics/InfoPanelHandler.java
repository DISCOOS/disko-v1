package org.redcross.sar.wp.logistics;

import com.esri.arcgis.interop.AutomationException;
import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.event.ITickEventListenerIf;
import org.redcross.sar.event.TickEvent;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IPersonnelIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent;
import org.redcross.sar.wp.IDiskoWpModule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.Vector;

/**
 *
 */
public class InfoPanelHandler implements IMsoUpdateListenerIf, ActionListener, ITickEventListenerIf
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

    private IDiskoWpLogistics m_wpModule;
    private JPanel m_infoPanel;
    private LogisticsInfoPanel m_unitInfoPanel;
    private LogisticsInfoPanel m_assignmentInfoPanel;
    private AssignmentScrollPanel m_unitAssignmentsPanel;

    private boolean m_shallReturnToList;

    private IUnitIf m_displayedUnit;
    private int m_displayedUnitSelection;

    private IAssignmentIf m_displayedAsssignment;
    private String m_displayedPanelName = "";

    private final AssignmentLabel.AssignmentLabelActionHandler m_assignmentLabelMouseListener;

    private final static long m_tickInterval = 60 * 1000; // once every minute.

    private long m_timeCounter;

    public InfoPanelHandler(JPanel anInfoPanel, IDiskoWpLogistics aWpModule, AssignmentLabel.AssignmentLabelActionHandler anActionHandler)
    {
        m_infoPanel = anInfoPanel;
        m_assignmentLabelMouseListener = anActionHandler;
        m_wpModule = aWpModule;

        m_infoPanel.add(new JPanel(), EMPTY_PANEL_NAME);

        initUnitInfoPanel();
        initAssignmentInfoPanel();
        initAssignmentListPanel();
        showPanel(EMPTY_PANEL_NAME);

        aWpModule.getMmsoEventManager().addClientUpdateListener(this);
        aWpModule.addTickEventListener(this);
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


    public void setTimeCounter(long aCounter)
    {
        m_timeCounter = aCounter;
    }

    public long getTimeCounter()
    {
        return m_timeCounter;
    }

    public long getInterval()
    {
        return m_tickInterval;
    }

    /**
     * Handle tick event.
     * <p/>
     * Update GUI due to tick events. Shall only update GUI objects that are depending on current time.
     *
     * @param e The evet object, not used.
     */
    public void handleTick(TickEvent e)
    {
        if (ASSIGNMENT_PANEL_NAME.equals(m_displayedPanelName))
        {
            renderAssignment();
        } else if (UNIT_PANEL_NAME.equals(m_displayedPanelName))
        {
            renderUnit();
        }
    }

    private void initUnitInfoPanel()
    {
        m_unitInfoPanel = new LogisticsInfoPanel(4, 2, 2);
        m_infoPanel.add(m_unitInfoPanel, UNIT_PANEL_NAME);
        m_unitInfoPanel.setHeaders(new String[]{m_wpModule.getText("UnitInfoPanel_hdr_0.text"),
                m_wpModule.getText("UnitInfoPanel_hdr_1.text"),
                m_wpModule.getText("UnitInfoPanel_hdr_2.text"),
                m_wpModule.getText("UnitInfoPanel_hdr_3.text"),
                m_wpModule.getText("UnitInfoPanel_hdr_4.text"),
                m_wpModule.getText("UnitInfoPanel_hdr_5.text")});

        m_unitInfoPanel.setButtons(new String[]{"icons/60x60/change.gif",          // todo Internationalize
                "icons/60x60/print.gif"}, new String[]{UNIT_CHANGE, UNIT_PRINT}, this);
    }

    private void initAssignmentInfoPanel()
    {
        m_assignmentInfoPanel = new LogisticsInfoPanel(5, 1, 4);
        m_infoPanel.add(m_assignmentInfoPanel, ASSIGNMENT_PANEL_NAME);
        m_assignmentInfoPanel.setHeaders(new String[]{m_wpModule.getText("AsgInfoPanel_hdr_0.text"),
                m_wpModule.getText("AsgInfoPanel_hdr_1.text"),
                m_wpModule.getText("AsgInfoPanel_hdr_2.text"),
                m_wpModule.getText("AsgInfoPanel_hdr_3.text"),
                m_wpModule.getText("AsgInfoPanel_hdr_4.text"),
                m_wpModule.getText("AsgInfoPanel_hdr_5.text")});
        m_assignmentInfoPanel.setButtons(new String[]{"icons/60x60/change.gif",          // todo Internationalize
                "icons/60x60/print.gif",
                "icons/60x60/result.gif",
                "icons/60x60/back.gif"}, new String[]{ASG_CHANGE, ASG_PRINT, ASG_RESULT, ASG_RETURN}, this);
    }

    private void initAssignmentListPanel()
    {
        // Build up a scrollpane with room for assignment labels.
        JScrollPane scrollpane = new JScrollPane();
        m_unitAssignmentsPanel = new AssignmentScrollPanel(scrollpane, new SpringLayout(), 5, 5, false, m_assignmentLabelMouseListener, false);
        m_unitAssignmentsPanel.setCols(1);
        JLabel hl = m_unitAssignmentsPanel.getHeaderLabel();
        hl.setHorizontalAlignment(SwingConstants.CENTER);
        hl.setPreferredSize(new Dimension(40, 40));
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
        if (m_displayedAsssignment == null)
        { // added by Geodata
            return;
        }
        m_assignmentInfoPanel.setTopText(0, m_displayedAsssignment.getNumber() + " (" + m_displayedAsssignment.getStatusText() + ")");
        m_assignmentInfoPanel.setTopText(1, m_displayedAsssignment.getTypeText());
        IUnitIf unit = m_displayedAsssignment.getOwningUnit();
        if (unit != null)
        {
            m_assignmentInfoPanel.setTopText(2, unit.getUnitNumber());
        } else
        {
            m_assignmentInfoPanel.setTopText(2, "");
        }
        m_assignmentInfoPanel.setTopText(3, m_displayedAsssignment.getPriorityText());
        m_assignmentInfoPanel.setTopText(4, hoursSince(m_displayedAsssignment.getTimeAssigned()));
        m_assignmentInfoPanel.setTopText(5, "");

        m_assignmentInfoPanel.setButtonEnabled(2, m_displayedAsssignment.hasBeenFinished());
        m_assignmentInfoPanel.setButtonVisible(3, m_shallReturnToList);
    }

    private String hoursSince(Calendar aTime)
    {
        if (aTime != null)
        {
            Calendar now = Calendar.getInstance();
            long actTimeInMillis = now.getTimeInMillis() - aTime.getTimeInMillis();
            float actTimeInHours = actTimeInMillis / 3600000;
            int displayedTimeInHours = Math.round(actTimeInHours);
            return Integer.toString(displayedTimeInHours);
        }
        return " ";
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
        hl.setText(MessageFormat.format(m_wpModule.getText("AsgListInfoPanel_hdr.text"),
                UnitTableModel.getSelectedAssignmentText(m_wpModule, m_displayedUnitSelection),
                m_displayedUnit.getUnitNumber()));
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
        m_unitInfoPanel.setTopText(0, m_displayedUnit.getUnitNumber() + " " + m_displayedUnit.getStatusText());
        m_unitInfoPanel.setTopText(1, m_displayedUnit.getCallSign());
        IAssignmentIf activeAsg = m_displayedUnit.getActiveAssignment();
        if (activeAsg != null)
        {
            m_unitInfoPanel.setTopText(2, Integer.toString(activeAsg.getNumber()));
            m_unitInfoPanel.setTopText(3, hoursSince(activeAsg.getTimeAssigned()));
        } else
        {
            m_unitInfoPanel.setTopText(2, "");
            m_unitInfoPanel.setTopText(3, "");
        }
        IPersonnelIf person = m_displayedUnit.getUnitLeader();
        if (person != null)
        {
            m_unitInfoPanel.setCenterText(1, person.getFirstname() + " " + person.getLastname());
        } else
        {
            m_unitInfoPanel.setTopText(4, " ");
        }

        Vector<String> memberNames = new Vector<String>();
        for (IPersonnelIf p : m_displayedUnit.getUnitPersonnelItems())
        {
            memberNames.add(p.getFirstname() + " " + p.getLastname());
        }
        m_unitInfoPanel.setCenterText(2, memberNames);
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
            try
            {
                IDiskoRole role = m_wpModule.getDiskoRole();
                String id = role.getName() + "Taktikk";
                IDiskoWpModule calledModule = role.getDiskoWpModule(id);
                if (calledModule != null)
                {
                    role.selectDiskoWpModule(calledModule);
                    calledModule.setCallingWp(m_wpModule.getName());
                    calledModule.getMap().zoomToMsoObject(m_displayedAsssignment);
                    m_wpModule.getMap().setSelected(m_displayedAsssignment, true);
                } else
                {
                    m_wpModule.showWarning("Taktikk er ikke funnet");
                }
            }
            catch (AutomationException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            catch (IOException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
}
