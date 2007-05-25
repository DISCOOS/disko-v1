package org.redcross.sar.wp.logistikk;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Vector;

/**
 *
 */
public class DiskoScrollPanel extends JPanel implements Scrollable
{
    protected final JScrollPane m_surroundingScrollPane;
    protected final LayoutManager m_layoutManager;
    protected Dimension m_defaultDimension = new Dimension(10, 10);
    protected Dimension m_childDimension = new Dimension();
    protected Vector<Component> m_childrenComponents = new Vector<Component>();
    private boolean m_childSizeSet = false;
    private JLabel m_header;
    private Corner m_corner;

    protected int m_hgap = 5;
    protected int m_vgap = 5;


    public DiskoScrollPanel(JScrollPane aScrollPane, LayoutManager aLayoutManager)
    {
        super(aLayoutManager);
        m_surroundingScrollPane = aScrollPane;
        m_layoutManager = aLayoutManager;

        if (m_layoutManager instanceof FlowLayout)
        {
            m_hgap = ((FlowLayout) aLayoutManager).getHgap();
            m_vgap = ((FlowLayout) aLayoutManager).getVgap();
        }
        m_surroundingScrollPane.setViewportView(this);

        m_header = new JLabel();
        m_surroundingScrollPane.setColumnHeaderView(m_header);

        m_corner = new DiskoScrollPanel.Corner(m_header.getBackground());
        m_surroundingScrollPane.setCorner(JScrollPane.UPPER_TRAILING_CORNER, m_corner);
        setBackground(Color.white);
        m_surroundingScrollPane.setViewportBorder(BorderFactory.createLineBorder(Color.black));

        ComponentAdapter resizeHandler = new ComponentAdapter()
        {
            public void componentResized(ComponentEvent e)
            {
                resizePanel(false);
            }
        };
        addComponentListener(resizeHandler);
        getParent().addComponentListener(resizeHandler);
    }

    /**
     * Resize the panel according to width and max label size.
     *
     * @param dataChanged Indicated that the method is called due to change of data content.
     */
    protected void resizePanel(boolean dataChanged)
    {
        if (!m_childSizeSet)
        {
            int width = m_defaultDimension.width;
            int height = m_defaultDimension.height;
            for (Component c : m_childrenComponents)
            {
                m_childSizeSet = c.getWidth() > 0;
                width = Math.max(width, c.getWidth());
                height = Math.max(height, c.getHeight());
            }
            m_childDimension.width = width;
            m_childDimension.height = height;
        }

        Dimension myDimension = getSize();
        int cols = Math.max(myDimension.width / (m_childDimension.width + m_hgap), 1);
        int rows = (m_childrenComponents.size() / cols) + 1;
        int newHeight = rows * (m_childDimension.height + m_vgap);
        newHeight = Math.max(newHeight, getParent().getHeight());
        if (newHeight != myDimension.height)
        {
            myDimension.height = newHeight;
            setPreferredSize(myDimension);
            revalidate();
            repaint();
        } else if (dataChanged)
        {
            revalidate();
            repaint();
        }
    }

    @Override
    public Component add(Component aComponent)
    {
        m_childrenComponents.add(aComponent);
        m_childSizeSet = false;
        return super.add(aComponent);
    }

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
        if (direction == SwingConstants.HORIZONTAL)
        {
            return m_defaultDimension.width;
        } else
        {
            return m_defaultDimension.height;
        }
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
    {
        if (direction == SwingConstants.HORIZONTAL)
        {
            return m_defaultDimension.width * 5;
        } else
        {
            return m_defaultDimension.height * 5;
        }
    }

    public boolean getScrollableTracksViewportWidth()
    {
        return true;
    }

    public boolean getScrollableTracksViewportHeight()
    {
        return false;
    }

    public int getMaxNonscrollItems(Dimension aDimension)
    {
        Dimension availableDim = getParent().getSize();
        int cols = availableDim.width / (aDimension.width + m_hgap);
        cols = Math.max(cols, 1);
        int rows = availableDim.height / (aDimension.height + m_vgap);
        rows = Math.max(rows, 1);
        return cols * rows - 1;
    }

    public void removeAll()
    {
        m_childSizeSet = false;
        m_childrenComponents.clear();
        super.removeAll();
    }

    public JLabel getHeaderLabel()
    {
        return m_header;
    }

    public void setHeaderPopupHandler(AbstractPopupHandler aPopupHandler)
    {
        PopupListener listener = new PopupListener(aPopupHandler);
        m_header.addMouseListener(listener);
        m_corner.addMouseListener(listener);
    }

    public static class Corner extends JComponent
    {
        Color m_background;

        public Corner(Color aBackground)
        {
            m_background = aBackground;
        }

        public void setBackground(Color aBackground)
        {
            m_background = aBackground;
        }

        /**
         * Fill with given color.
         *
         * @param g My graphics
         */
        protected void paintComponent(Graphics g)
        {
            g.setColor(m_background);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}
