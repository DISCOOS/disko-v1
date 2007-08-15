package org.redcross.sar.wp.messageLog;

import org.redcross.sar.map.IDiskoMap;
import org.redcross.sar.mso.data.IMessageLogIf;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: vinjar
 * Date: 25.jun.2007
 * To change this template use File | Settings | File Templates.
 */

/**
 *
 */
public class MessageLogPanel
{
	public static final int PANEL_WIDTH = 800;
	
	private static final String MAP_ID = "MAP";
	private static final String LOG_ID = "LOG";
	
    private JPanel WorkspacePanel;
    private MessageLogTopPanel m_topPanel;
    private static JSplitPane m_splitter1;
    private static IDiskoWpMessageLog m_wpModule;
    private static IDiskoMap m_map;
    private static JTable m_logTable;
    private MessageRowSelectionListener m_rowSelectionListener;
    private static JScrollPane m_scrollPane1;
    private static JPanel m_bottomPanel;
    private IMessageLogIf m_messageLog;
    
    
	public static Dimension SMALL_BUTTON_SIZE = new Dimension(60, 60);

    public MessageLogPanel(IDiskoWpMessageLog aWp)
    {
        m_wpModule = aWp;
        m_map = m_wpModule.getMap();
        m_messageLog = m_wpModule.getMsoManager().getCmdPost().getMessageLog();

        WorkspacePanel = new JPanel();
        WorkspacePanel.setLayout(new BorderLayout(0, 0));

        m_splitter1 = new JSplitPane();
        m_splitter1.setContinuousLayout(true);
        m_splitter1.setRequestFocusEnabled(true);
        m_splitter1.setOrientation(0);

        WorkspacePanel.add(m_splitter1, BorderLayout.CENTER);

        initTopPanel();
        initBottomPanel();
    }

	private void initTopPanel()
    {
        m_topPanel = new MessageLogTopPanel(m_messageLog);
        m_topPanel.setWp(m_wpModule);
        m_topPanel.initialize();
        m_topPanel.setMinimumSize(new Dimension(PANEL_WIDTH, MessageLogTopPanel.PANEL_HEIGHT));
        // Top panel should be informed of updates to mso model
        m_wpModule.getMmsoEventManager().addClientUpdateListener(m_topPanel);
        
        m_splitter1.setContinuousLayout(true);
        m_splitter1.setDividerLocation(MessageLogTopPanel.PANEL_HEIGHT);
        m_splitter1.setResizeWeight(0.0);
        m_splitter1.setLeftComponent(m_topPanel);
    }

    private void initBottomPanel()
    {
    	m_bottomPanel = new JPanel();
    	CardLayout layout = new CardLayout();
    	m_bottomPanel.setLayout(layout);
    	m_bottomPanel.setFocusCycleRoot(true);
    	m_splitter1.setRightComponent(m_bottomPanel);

    	m_scrollPane1 = new JScrollPane();
    	m_scrollPane1.setOpaque(false);
    	m_bottomPanel.add(m_scrollPane1, LOG_ID);

    	m_logTable = new JTable();
    	m_scrollPane1.setViewportView(m_logTable);

    	m_rowSelectionListener = new MessageRowSelectionListener(m_topPanel);
        m_logTable.getSelectionModel().addListSelectionListener(m_rowSelectionListener);
   
        final LogTableModel model = new LogTableModel(m_logTable, m_wpModule, m_messageLog);
        m_rowSelectionListener.setModel(model);
        m_rowSelectionListener.setRowMap(model.getRowMap());
        m_logTable.setModel(model);
        m_logTable.setAutoCreateColumnsFromModel(true);
        m_logTable.setShowHorizontalLines(true);
        m_logTable.setShowVerticalLines(true);
        m_logTable.setRowSelectionAllowed(true);
        m_logTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m_logTable.setRowMargin(2);
        m_logTable.setRowHeight(22);
        
        // Register the log table with the row selection listener
        m_rowSelectionListener.setTable(m_logTable);

        // Set column widths
        TableColumn column = m_logTable.getColumnModel().getColumn(0);
        column.setMaxWidth(MessageLogTopPanel.SMALL_PANEL_WIDTH);
        column = m_logTable.getColumnModel().getColumn(1);
        column.setMaxWidth(MessageLogTopPanel.SMALL_PANEL_WIDTH+1);
        column = m_logTable.getColumnModel().getColumn(2);
        column.setMaxWidth(MessageLogTopPanel.SMALL_PANEL_WIDTH+1);
        column = m_logTable.getColumnModel().getColumn(3);
        column.setMaxWidth(MessageLogTopPanel.SMALL_PANEL_WIDTH+1);
        column = m_logTable.getColumnModel().getColumn(5);
        column.setMaxWidth(MessageLogTopPanel.SMALL_PANEL_WIDTH*2);
        column.setMinWidth(MessageLogTopPanel.SMALL_PANEL_WIDTH*2);
        column = m_logTable.getColumnModel().getColumn(6);
        column.setMaxWidth(MessageLogTopPanel.SMALL_PANEL_WIDTH-1);

        // Init custom renderer
        column = m_logTable.getColumnModel().getColumn(4);
        column.setCellRenderer(new MessageTableRenderer());

        JTableHeader tableHeader = m_logTable.getTableHeader();
        tableHeader.setResizingAllowed(false);
        tableHeader.setReorderingAllowed(false);
        
        m_bottomPanel.add((JComponent)m_map, MAP_ID);
    }

    public JPanel getPanel()
    {
        setTableData();
        return WorkspacePanel;
    }

    private void setTableData()
    {
        LogTableModel ltm = (LogTableModel) m_logTable.getModel();
        ltm.buildTable();
    }

	public void setSmallButtonSize(Dimension smallButtonSize)
	{
		SMALL_BUTTON_SIZE = smallButtonSize;
	}

	public void setWpMessageLog(DiskoWpMessageLogImpl diskoWpMessageLogImpl)
	{
		m_wpModule = diskoWpMessageLogImpl;
	}

	public void hideDialogs()
	{
		m_topPanel.hideDialogs();
	}

	public void clearSelection()
	{
		m_topPanel.clearSelection();
	}
	
	public static void showMap()
	{
		CardLayout cards = (CardLayout)m_bottomPanel.getLayout();
		cards.show(m_bottomPanel, MAP_ID);
	}
	
	public static void hideMap()
	{
		CardLayout cards = (CardLayout)m_bottomPanel.getLayout();
		cards.show(m_bottomPanel, LOG_ID);
	}
	
	public static IDiskoMap getMap()
	{
		return m_map;
	}
}
