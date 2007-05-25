package org.redcross.sar.wp.logistics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.AbstractList;

/**
 *
 */
public class LogisticsInfoPanel extends JPanel
{
    private final static Dimension m_buttonDimension = new Dimension(50, 50);
    private JPanel m_topInfoPanel;
    private ScrollInfoPanel m_centerScrollPanel;
    private JPanel m_lowButtonPanel;
    private InternalInfoPanel[] m_topPanelElements;
    private InternalInfoPanel[] m_centerPanelElements;
    private JButton[] m_lowPanelButtons;


    public LogisticsInfoPanel()
    {
        super(new BorderLayout(0, 0));
//        setBackground(Color.WHITE);
//        setOpaque(true);

        m_topInfoPanel = new JPanel();
        add(m_topInfoPanel, BorderLayout.NORTH);
        initTopPanel();

        m_centerScrollPanel = new ScrollInfoPanel();
        JScrollPane scrollpane = new JScrollPane(m_centerScrollPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollpane, BorderLayout.CENTER);
        initCenterPanel();

        m_lowButtonPanel = new JPanel();
        add(m_lowButtonPanel, BorderLayout.SOUTH);
        initLowPanel();
    }

    private void initTopPanel()
    {
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        m_topInfoPanel.setLayout(gridbag);
        m_topInfoPanel.setBackground(Color.WHITE);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;

        m_topPanelElements = new InternalInfoPanel[5];

        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        for (int i = 0; i < 5; i++)
        {
            InternalInfoPanel ip = new InternalInfoPanel("P" + i, i != 0);
            ip.setText("T" + i);
            if (i == 0)
            {
                JLabel h = ip.getHeaderLabel();
                h.setHorizontalAlignment(SwingConstants.CENTER);
            }
            m_topInfoPanel.add(ip, c);
            m_topPanelElements[i] = ip;
            c.gridwidth = 1;
            if (i % 2 == 0)
            {
                c.gridx = 0;
                c.gridy++;
            } else
            {
                c.gridx++;
            }
        }
    }

    private void initCenterPanel()
    {
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        m_centerScrollPanel.setLayout(gridbag);
        m_centerScrollPanel.setBackground(Color.WHITE);

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;

        m_centerPanelElements = new InternalInfoPanel[2];

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = 0;
        for (int i = 0; i < 2; i++)
        {
            InternalInfoPanel ip = new InternalInfoPanel("P" + i, true);
            ip.setText("T" + i);
            m_centerScrollPanel.add(ip, c);
            m_centerPanelElements[i] = ip;
            c.gridy++;
       }
        m_centerPanelElements[1].setHeader("Field 2");
        m_centerPanelElements[1].setText(new String[]{"dsf lkjlsd jasdkjash kjadaksh dkjsa dkas hdkasda hsdkhask hdkahdkahdfl","ssdgsdgfsgg","sgsdgsdg","sgsdgsg","sgsdgg","sgsgdgsg",
                "The last line shows that, indeed, the field that is accessed does not depend on the run-time class of the referenced object; even if s holds a reference to an object of class T, the expression s.x refers to the x field of class S, because the type of the expression s is S. Objects of class T contain two fields named x, one for class T and one for its superclass S."});
    }

    private void initLowPanel()
    {
        m_lowButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        m_lowButtonPanel.setBackground(Color.WHITE);
        m_lowPanelButtons = new JButton[4];
        for (int i = 0; i < 4; i++)
        {
            JButton b = new JButton("B" + i);
            b.setMinimumSize(m_buttonDimension);
            b.setPreferredSize(m_buttonDimension);
            m_lowButtonPanel.add(b);
            b.setVisible(true);
        }
    }

    void configureButtonActions(ActionListener aListener, String[] theActionCommands)
    {
        int i = 0;
        for (JButton b : m_lowPanelButtons)
        {
            // remove existing listeners
            ActionListener[] listeners = b.getActionListeners();
            for (ActionListener l : listeners)
            {
                b.removeActionListener(l);
            }
            // add new listener and set command
            if (i < theActionCommands.length)
            {
                b.addActionListener(aListener);
                b.setActionCommand(theActionCommands[i]);
            }
            i++;
        }
    }

    public static class InternalInfoPanel extends JPanel
    {
        final static String lineSeparator = System.getProperty("line.separator");
        final static Font headerFont = new Font("Sans", Font.BOLD, 16);
        final static Font textFont = new Font("Sans", Font.PLAIN, 14);

        private JLabel m_headerLabel;
        private String m_headerText = "";
        private JTextArea m_textArea;
        private String m_displayText = "";
        private boolean m_hasTextField;

        public InternalInfoPanel(boolean hasTextField)
        {
            this("", hasTextField);
        }

        public InternalInfoPanel(String aHeader, boolean hasTextField)
        {
            super(new BorderLayout(0, 0));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

            m_hasTextField = hasTextField;

            m_headerLabel = new JLabel();
            add(m_headerLabel, BorderLayout.NORTH);
            m_headerLabel.setFont(headerFont);
            m_headerLabel.setOpaque(false); // get the same bacground as parent
            setHeader(aHeader);


            m_textArea = new JTextArea();
            add(m_textArea, BorderLayout.CENTER);
            m_textArea.setLineWrap(true);
            m_textArea.setWrapStyleWord(true);
            m_textArea.setFont(textFont);
//            m_textArea.setBackground(Color.WHITE);
            setText("");
        }

        JLabel getHeaderLabel()
        {
            return m_headerLabel;
        }

        JTextArea getTextArea()
        {
            return m_textArea;
        }

        public void setHeader(String aHeader)
        {
            m_headerText = aHeader.toUpperCase();
            if (m_hasTextField)
            {
                m_headerLabel.setText(m_headerText);
            } else
            {
                m_headerLabel.setText(m_headerText + " " + m_displayText);
            }
        }

        public void setText(AbstractList<String> theLines)
        {
            int charCount = 0;
            for (String s : theLines)
            {
                charCount += s.length() + lineSeparator.length();
            }
            StringBuilder sb = new StringBuilder(charCount);
            int i = 0;
            for (String s : theLines)
            {
                sb.append(s);
                i++;
                if (i < s.length())
                {
                    sb.append(lineSeparator);
                }
            }
            setText(sb.toString());
        }

        public void setText(String[] theLines)
        {
            int charCount = 0;
            for (String s : theLines)
            {
                charCount += s.length() + lineSeparator.length();
            }
            StringBuilder sb = new StringBuilder(charCount);
            int i = 0;
            for (String s : theLines)
            {
                sb.append(s);
                i++;
                if (i < s.length())
                {
                    sb.append(lineSeparator);
                }
            }
            setText(sb.toString());
        }

        public void setText(String aText)
        {
            m_displayText = aText;
            if (m_hasTextField)
            {
                m_textArea.setText(m_displayText);
                m_textArea.setVisible(m_displayText.length() > 0);
            } else
            {
                m_headerLabel.setText(m_headerText + " " + m_displayText);
                m_textArea.setVisible(false);
            }
        }
    }

    private class ScrollInfoPanel extends JPanel implements Scrollable
    {

        public Dimension getPreferredScrollableViewportSize()
        {
            return getPreferredSize();
        }

        public Dimension getPreferredSize()
        {
            return super.getPreferredSize();
        }


        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
        {
            return 0;
        }

        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
        {
            return 0;
        }

        public boolean getScrollableTracksViewportWidth()
        {
            return true;
        }

        public boolean getScrollableTracksViewportHeight()
        {
            return false;
        }
    }
}
