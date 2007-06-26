package org.redcross.sar.wp.messageLog;

import org.redcross.sar.map.IDiskoMap;
import org.redcross.sar.mso.data.IMessageLogIf;

import javax.swing.*;
import javax.swing.table.JTableHeader;
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
    private JPanel WorkspacePanel;
    private JSplitPane m_splitter1;
    private DiskoWpMessageLogImpl m_wpModule;
    private IDiskoMap m_map;
    private JTable m_logTable;
    private JScrollPane m_scrollPane1;
    JPanel m_logPanel;
    IMessageLogIf m_messageLog;

    public MessageLogPanel(DiskoWpMessageLogImpl aWp)
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

        m_logPanel = new JPanel();
        m_logPanel.setLayout(new BorderLayout(0, 0));
        m_logPanel.setFocusCycleRoot(true);
        m_splitter1.setRightComponent(m_logPanel);

        m_scrollPane1 = new JScrollPane();
        m_scrollPane1.setOpaque(false);
        m_logPanel.add(m_scrollPane1, BorderLayout.CENTER);
        m_logTable = new JTable();
        m_scrollPane1.setViewportView(m_logTable);

        initLogTable();
    }

    private void initLogTable()
    {
        final LogTableModel model = new LogTableModel(m_logTable, m_wpModule, m_messageLog);
        m_logTable.setModel(model);
        m_logTable.setAutoCreateColumnsFromModel(true);
        m_logTable.setShowHorizontalLines(false);
        m_logTable.setShowVerticalLines(true);
        m_logTable.setRowMargin(2);
        JTableHeader tableHeader = m_logTable.getTableHeader();
        tableHeader.setResizingAllowed(false);
        tableHeader.setReorderingAllowed(false);

    }

    public JPanel getPanel()
    {
        setTableData();
        return WorkspacePanel;
    }

    private void setTableData()
    {
    }


}
