package org.redcross.sar.wp.logistics;

import com.esri.arcgis.interop.AutomationException;
import org.redcross.sar.event.IMsoLayerEventListener;
import org.redcross.sar.event.MsoLayerEvent;
import org.redcross.sar.gui.renderers.IconRenderer;
import org.redcross.sar.map.IDiskoMap;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.*;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.EnumSet;
/**
 * Created by IntelliJ IDEA.
 * User: vinjar
 * Date: 11.apr.2007
 * To change this template use File | Settings | File Templates.
 */

/**
 *
 */
public class LogisticsPanel implements IMsoUpdateListenerIf, IMsoLayerEventListener
{

    private JPanel WorkspacePanel;
    private IDiskoMap m_map;
    private JPanel m_assignmentPanel;
    private JPanel m_unitPanel;
    private JPanel m_infoPanel;
    //    private UnitJTable m_unit1Table;
    //    private JTable m_assignmentTable;
    private JTable m_unitTable;
    private JSplitPane m_splitter1;
    private JSplitPane m_splitter2;
    private JSplitPane m_splitter3;
    private JScrollPane m_scrollPane1;
    private JScrollPane m_AssignmentSubPaneLeft;
    private JScrollPane m_AssignmentSubPaneRight;
    private IDiskoWpLogistics m_wpModule;
    private IUnitListIf m_unitList;
    private IAssignmentListIf m_assignmentList;

    private IAssignmentIf m_mapSelectedAssignment;
    private AssignmentScrollPanel m_selectableAssignmentsPanel;
    private AssignmentScrollPanel m_priAssignmentsPanel;
    private AssignmentTransferHandler m_assignmentTransferHandler;

    private InfoPanelHandler m_infoPanelHandler;

    private AssignmentLabel.AssignmentLabelActionHandler m_labelActionHandler;
    private AssignmentLabel.AssignmentLabelActionHandler m_listPanelActionHandler;

    private IconRenderer.LogisticsIconActionHandler m_iconActionHandler;

    private boolean m_mapSelectedByButton = false;

    public LogisticsPanel(IDiskoWpLogistics aWp)
    {
        setupUI();
        m_wpModule = aWp;
        m_map = m_wpModule.getMap();
        m_unitList = m_wpModule.getMsoManager().getCmdPost().getUnitList();
        m_assignmentList = m_wpModule.getMsoManager().getCmdPost().getAssignmentList();


        if (!defineTransferHandler())
        {
            return;
        }
        defineSubpanelActionHandlers();
        m_splitter3.setLeftComponent((JComponent) m_map);
//        setSplitters();
//        setPanelSizes();
        initUnitTable();
        initInfoPanels();
        initAssignmentPanels();
        addToListeners();
        WorkspacePanel.addComponentListener(new ComponentListener()
        {
            boolean initialized = false;
            public void componentResized(ComponentEvent e)
            {
            }

            public void componentMoved(ComponentEvent e)
            {
            }

            public void componentShown(ComponentEvent e)
            {
                if (! initialized)
                {
                    setSplitters();
                    setPanelSizes();
                    WorkspacePanel.validate();
                    initialized = true;
                }
            }

            public void componentHidden(ComponentEvent e)
            {
            }
        });
    }

    private boolean defineTransferHandler()
    {
        try
        {
            m_assignmentTransferHandler = new AssignmentTransferHandler(m_wpModule);
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return false;
        }
        return true;
    }

    private void defineSubpanelActionHandlers()
    {
        m_labelActionHandler = new AssignmentLabel.AssignmentLabelActionHandler()
        {
            public void handleClick(IAssignmentIf anAssignment)
            {
                singelAssignmentClick(anAssignment, false);
            }
        };

        m_listPanelActionHandler = new AssignmentLabel.AssignmentLabelActionHandler()
        {
            public void handleClick(IAssignmentIf anAssignment)
            {
                singelAssignmentClick(anAssignment, true);
            }
        };

        m_iconActionHandler = new IconRenderer.LogisticsIconActionHandler()
        {
            public void handleClick(IUnitIf aUnit)
            {
                setSelectedAssignmentInPanels(null);
                getInfoPanelHandler().setUnit(aUnit);
            }

            public void handleClick(IAssignmentIf anAssignment)
            {
                singelAssignmentClick(anAssignment, false);
            }

            public void handleClick(IUnitIf aUnit, int aSelectorIndex)
            {
                setSelectedAssignmentInPanels(null);
                getInfoPanelHandler().setUnitSelection(aUnit, aSelectorIndex);
            }
        };
    }

    private void singelAssignmentClick(IAssignmentIf anAssignment, boolean calledFromListPanel)
    {
        setSelectedAssignmentInPanels(anAssignment);
        getInfoPanelHandler().setAssignment(anAssignment, calledFromListPanel);
        try
        {
            if (m_mapSelectedAssignment != null)
            {
                m_map.setSelected(m_mapSelectedAssignment, false);
                m_mapSelectedAssignment = null;
            }

            if (anAssignment.getPlannedArea() != null)
            {
                m_mapSelectedAssignment = anAssignment;
                m_map.zoomToMsoObject(m_mapSelectedAssignment);
                m_mapSelectedByButton = true;
                m_map.setSelected(m_mapSelectedAssignment, true);
                m_mapSelectedByButton = false;
            }
        }
        catch (AutomationException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void setSelectedAssignmentInPanels(IAssignmentIf anAssignment)
    {
        m_selectableAssignmentsPanel.setSelectedAssignment(anAssignment);
        m_priAssignmentsPanel.setSelectedAssignment(anAssignment);
    }

    private void initAssignmentPanels()
    {
        m_AssignmentSubPaneLeft.setPreferredSize(new Dimension(120, 0));
        m_AssignmentSubPaneLeft.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        m_AssignmentSubPaneLeft.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        m_AssignmentSubPaneRight.setMinimumSize(new Dimension(60, 0));
        m_AssignmentSubPaneRight.setPreferredSize(new Dimension(60, 0));
        m_AssignmentSubPaneRight.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        m_AssignmentSubPaneRight.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        m_selectableAssignmentsPanel = new AssignmentScrollPanel(m_AssignmentSubPaneLeft, new FlowLayout(FlowLayout.LEFT, 5, 5), m_labelActionHandler, true);
        m_selectableAssignmentsPanel.setTransferHandler(m_assignmentTransferHandler);

        JLabel hl;
        hl = m_selectableAssignmentsPanel.getHeaderLabel();
        hl.setHorizontalAlignment(SwingConstants.CENTER);
        hl.setPreferredSize(new Dimension(40, 40));

        m_priAssignmentsPanel = new AssignmentScrollPanel(m_AssignmentSubPaneRight, new FlowLayout(FlowLayout.LEFT, 5, 5), m_labelActionHandler, true);
        m_priAssignmentsPanel.setTransferHandler(m_assignmentTransferHandler);
        hl = m_priAssignmentsPanel.getHeaderLabel();
        hl.setHorizontalAlignment(SwingConstants.CENTER);
        hl.setPreferredSize(new Dimension(40, 40));

        AssignmentDisplayModel adm = new AssignmentDisplayModel(m_selectableAssignmentsPanel, m_priAssignmentsPanel, m_wpModule.getMmsoEventManager(), m_assignmentList);
    }

    private void initUnitTable()
    {
        final UnitTableModel model = new UnitTableModel(m_unitTable, m_wpModule, m_unitList, m_iconActionHandler);
        m_unitTable.setModel(model);
        m_unitTable.setAutoCreateColumnsFromModel(true);
        m_unitTable.setDefaultRenderer(IconRenderer.UnitIcon.class, new LogisticsIconRenderer());
        m_unitTable.setDefaultRenderer(IconRenderer.AssignmentIcon.class, new LogisticsIconRenderer());
        m_unitTable.setDefaultRenderer(IconRenderer.InfoIcon.class, new LogisticsIconRenderer.InfoIconRenderer());
        m_unitTable.setShowHorizontalLines(false);
        m_unitTable.setShowVerticalLines(true);
        m_unitTable.setRowMargin(2);
        JTableHeader tableHeader = m_unitTable.getTableHeader();
        tableHeader.setResizingAllowed(false);
        tableHeader.setReorderingAllowed(false);
        m_unitTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m_unitTable.setCellSelectionEnabled(true);
        JTableHeader th = m_unitTable.getTableHeader();
        th.setPreferredSize(new Dimension(40, 40));

        m_unitTable.setTransferHandler(m_assignmentTransferHandler);
        m_unitTable.setDragEnabled(true);

        ListSelectionListener l = new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                if (e.getValueIsAdjusting() || !m_unitTable.hasFocus())
                {
                    return;
                }

                model.setSelectedCell(m_unitTable.getSelectionModel().getLeadSelectionIndex(),
                        m_unitTable.getColumnModel().getSelectionModel().
                                getLeadSelectionIndex());

            }
        };
        m_unitTable.getSelectionModel().addListSelectionListener(l);
        m_unitTable.getColumnModel().getSelectionModel().addListSelectionListener(l);

        m_unitTable.addFocusListener(new FocusListener()
        {
            public void focusGained(FocusEvent e)
            {
                model.setSelectedCell(m_unitTable.getSelectionModel().getLeadSelectionIndex(),
                        m_unitTable.getColumnModel().getSelectionModel().
                                getLeadSelectionIndex());
            }

            public void focusLost(FocusEvent e)
            {
                m_unitTable.clearSelection();
            }
        });
    }

    private void initInfoPanels()
    {
        m_infoPanelHandler = new InfoPanelHandler(m_infoPanel, m_wpModule, m_listPanelActionHandler);
        m_infoPanelHandler.setSelectionTransferHandler(m_assignmentTransferHandler);
    }

    private void addToListeners()
    {
        m_wpModule.getMmsoEventManager().addClientUpdateListener(this);
        IMsoFeatureLayer msoLayer = m_wpModule.getApplication().getDiskoMapManager().
                getMsoLayer(IMsoFeatureLayer.LayerCode.AREA_LAYER);
        msoLayer.addDiskoLayerEventListener(this);
    }

    private void setPanelSizes()
    {
        // minimum and preferred sizes are nice to have
        m_unitPanel.setMinimumSize(new Dimension(320, 600));
        m_unitPanel.setPreferredSize(new Dimension(320, 600));
        m_assignmentPanel.setMinimumSize(new Dimension(250, 600));
        m_assignmentPanel.setPreferredSize(new Dimension(250, 600));
        m_infoPanel.setMinimumSize(new Dimension(325, 200));
        m_infoPanel.setPreferredSize(new Dimension(325, 200));
    }

    private void setSplitters()
    {
        // Splitter between map/info panels and assignment/unit panels
        m_splitter1.setContinuousLayout(true);
        m_splitter1.setDividerLocation(Math.max(375,m_splitter1.getWidth() - 590));
        m_splitter1.setResizeWeight(1.0);

        // Splitter between assignment and unit panels
        m_splitter2.setContinuousLayout(true);
        m_splitter2.setDividerLocation(250);
        m_splitter2.setResizeWeight(1.0);

        // Splitter between map and info panels, make tha map initially a square
        m_splitter3.setContinuousLayout(true);
        m_splitter3.setDividerLocation(Math.max(375,m_splitter3.getHeight()-280));
        m_splitter3.setResizeWeight(1.0);
    }

    private final EnumSet<IMsoManagerIf.MsoClassCode> myInterests = EnumSet.of(IMsoManagerIf.MsoClassCode.CLASSCODE_UNIT,
            IMsoManagerIf.MsoClassCode.CLASSCODE_ASSIGNMENT);

    public boolean hasInterestIn(IMsoObjectIf aMsoObject)
    {
        return myInterests.contains(aMsoObject.getMsoClassCode());
    }

    public void handleMsoUpdateEvent(MsoEvent.Update e)
    {
    }

    public JPanel getPanel()
    {
        setTableData();
        return WorkspacePanel;
    }

    public IDiskoMap getMap()
    {
        return m_map;
    }

    public InfoPanelHandler getInfoPanelHandler()
    {
        return m_infoPanelHandler;
    }

    public JTable getUnitTable()
    {
        return m_unitTable;
    }

    private void setTableData()
    {
        UnitTableModel utm = (UnitTableModel) m_unitTable.getModel();
        utm.buildTable();
    }

    public void onSelectionChanged(MsoLayerEvent e) throws IOException, AutomationException
    {
        IMsoFeatureLayer msoLayer = (IMsoFeatureLayer) e.getSource();
        java.util.List selection = msoLayer.getSelected();
        if (selection != null && selection.size() > 0)
        {
            IMsoFeature msoFeature = (IMsoFeature) selection.get(0);
            IMsoObjectIf msoObject = msoFeature.getMsoObject();
            if (msoObject instanceof IAreaIf)
            {
                IAssignmentIf assignment = ((IAreaIf) msoObject).getOwningAssignment();
                if (assignment != null)
                {
                    m_mapSelectedAssignment = ((IAreaIf) msoObject).getOwningAssignment();
                    setSelectedAssignmentInPanels(m_mapSelectedAssignment);
                    if (!m_mapSelectedByButton)
                    {
                        getInfoPanelHandler().setAssignment(m_mapSelectedAssignment, false);
                    }
                    m_map.zoomToMsoObject(m_mapSelectedAssignment);
                }
            }
        }
    }


    private void setupUI()
    {
        WorkspacePanel = new JPanel();
        WorkspacePanel.setLayout(new BorderLayout(0, 0));
        m_splitter1 = new JSplitPane();
        m_splitter1.setContinuousLayout(true);
        m_splitter1.setRequestFocusEnabled(true);
        WorkspacePanel.add(m_splitter1, BorderLayout.CENTER);
        m_splitter2 = new JSplitPane();
        m_splitter2.setContinuousLayout(true);
        m_splitter1.setRightComponent(m_splitter2);
        m_unitPanel = new JPanel();
        m_unitPanel.setLayout(new BorderLayout(0, 0));
        m_unitPanel.setFocusCycleRoot(true);
        m_splitter2.setRightComponent(m_unitPanel);
        m_scrollPane1 = new JScrollPane();
        m_scrollPane1.setOpaque(false);
        m_unitPanel.add(m_scrollPane1, BorderLayout.CENTER);
        m_unitTable = new JTable();
        m_scrollPane1.setViewportView(m_unitTable);
        m_assignmentPanel = new JPanel();
        m_assignmentPanel.setLayout(new BorderLayout(0, 0));
        m_splitter2.setLeftComponent(m_assignmentPanel);
        m_AssignmentSubPaneLeft = new JScrollPane();
        m_assignmentPanel.add(m_AssignmentSubPaneLeft, BorderLayout.CENTER);
        m_AssignmentSubPaneRight = new JScrollPane();
        m_AssignmentSubPaneRight.setRequestFocusEnabled(false);
        m_assignmentPanel.add(m_AssignmentSubPaneRight, BorderLayout.EAST);
        m_splitter3 = new JSplitPane();
        m_splitter3.setContinuousLayout(true);
        m_splitter3.setOrientation(0);
        m_splitter1.setLeftComponent(m_splitter3);
        m_infoPanel = new JPanel();
        m_infoPanel.setLayout(new CardLayout(0, 0));
        m_splitter3.setRightComponent(m_infoPanel);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$()
    {
        WorkspacePanel = new JPanel();
        WorkspacePanel.setLayout(new BorderLayout(0, 0));
        m_splitter1 = new JSplitPane();
        m_splitter1.setContinuousLayout(true);
        m_splitter1.setRequestFocusEnabled(true);
        WorkspacePanel.add(m_splitter1, BorderLayout.CENTER);
        m_splitter2 = new JSplitPane();
        m_splitter2.setContinuousLayout(true);
        m_splitter1.setRightComponent(m_splitter2);
        m_unitPanel = new JPanel();
        m_unitPanel.setLayout(new BorderLayout(0, 0));
        m_unitPanel.setFocusCycleRoot(true);
        m_splitter2.setRightComponent(m_unitPanel);
        m_scrollPane1 = new JScrollPane();
        m_scrollPane1.setOpaque(false);
        m_unitPanel.add(m_scrollPane1, BorderLayout.CENTER);
        m_unitTable = new JTable();
        m_scrollPane1.setViewportView(m_unitTable);
        m_assignmentPanel = new JPanel();
        m_assignmentPanel.setLayout(new BorderLayout(0, 0));
        m_splitter2.setLeftComponent(m_assignmentPanel);
        m_AssignmentSubPaneLeft = new JScrollPane();
        m_assignmentPanel.add(m_AssignmentSubPaneLeft, BorderLayout.CENTER);
        m_AssignmentSubPaneRight = new JScrollPane();
        m_AssignmentSubPaneRight.setRequestFocusEnabled(false);
        m_assignmentPanel.add(m_AssignmentSubPaneRight, BorderLayout.EAST);
        m_splitter3 = new JSplitPane();
        m_splitter3.setContinuousLayout(true);
        m_splitter3.setOrientation(0);
        m_splitter1.setLeftComponent(m_splitter3);
        m_infoPanel = new JPanel();
        m_infoPanel.setLayout(new CardLayout(0, 0));
        m_splitter3.setRightComponent(m_infoPanel);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return WorkspacePanel;
    }
}
