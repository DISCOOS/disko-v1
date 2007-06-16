package org.redcross.sar.wp.states;

import javax.swing.JScrollPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.RenderingHints;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.BoxLayout;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.util.Random;
import java.lang.Math;

public class States extends JScrollPane implements ComponentListener{

	private static final long serialVersionUID = 1L;
	private JPanel StateList = null;
	private JPanel DDSCO3 = null;
	private JPanel Resources = null;

	/**
	 * This is the default constructor
	 */
	public States() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() 
	{
		this.setSize(443, 212);
		this.setViewportView(getStateList());
	}

	/**
	 * This method initializes StateList	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getStateList() {
		if (StateList == null) {
			StateList = new JPanel();
			StateList.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
			StateList.setLayout(new BoxLayout(getStateList(), BoxLayout.Y_AXIS));
			StateList.add(getDDSCO3(), null);
			StateList.add(getResources(), null);
			getDDSCO3().addComponentListener(this);
			getResources().addComponentListener(this);
		}
		return StateList;
	}

	/**
	 * This method initializes DDSCO3	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getDDSCO3() {
		if (DDSCO3 == null) {
			DDSCO3 = new DDSCO3State();
			DDSCO3.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5), BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));			
		}
		return DDSCO3;
	}

	private class DDSCO3State extends JPanel {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int m_step = 10;
		private int m_max = 100;
		private int m_count = 0;
		private int m_minimal = 0;
		private int m_optimal = 0;

		protected void paintComponent(Graphics g) {
			String tasks;
			// store state
			Color c = g.getColor();
			Font f = g.getFont();
			// cast to 2D
		    Graphics2D g2d = (Graphics2D)g;
			// create colors
		    Color cb = new Color(51,102,255);
		    Color co = new Color(255,102,0);
		    Color cy = new Color(255,204,0);
			// create font
		    int screenRes = Toolkit.getDefaultToolkit().getScreenResolution();
		    int fontSize = (int)Math.round(8.0 * screenRes / 72.0);
		    Font font = new Font(f.getName(), Font.PLAIN, fontSize);
		    // create math objects
		    Random rnd = new Random();
		    // paint background?
	        if (isOpaque()) { 
	            g.setColor(getBackground());
	            g.fillRect(0, 0, getWidth(), getHeight());
	        }
		    g2d.setFont(font);
		    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		    g2d.setColor(Color.BLACK);
		    m_max = rnd.nextInt(100);
		    m_step = getWidth()/m_max;
		    m_minimal = getWidth()/(2*m_step);
		    m_optimal = java.lang.Math.max(m_minimal+rnd.nextInt(m_max-m_minimal),1);
		    int m = m_minimal*m_step;
		    int o = m_optimal*m_step;
		    g2d.drawString("Marginalt", m-20, 22);
		    g2d.drawString("Optimalt", o-20, 22);
		    g2d.setColor(Color.GRAY);
		    g2d.drawLine(m, 25, m, getHeight()-17);
		    g2d.drawLine(o, 25, o, getHeight()-17);
		    g2d.drawRect(15, 25, getWidth()-32, getHeight()-42);
		    m_count = rnd.nextInt(m_max); 
		    int i = m_count*m_step;
		    if (i<m) {
		    	g2d.setColor(co);
		    }
		    else if (i >= m & i < o) {
		    	g2d.setColor(cy);
		    }
		    else {
		    	g2d.setColor(cb);
		    }
		    if (m_count - m_minimal > 0)
		    	tasks = "+ " + java.lang.Math.abs(m_count - m_minimal) + " oppgaver";
		    else if (m_count - m_minimal == 0)
		    	tasks = "+ 0 oppgaver";
		    else
		    	tasks = "- " + java.lang.Math.abs(m_count - m_minimal) + " oppgaver";

		    g2d.fillRect(30, 40, i-30, getHeight()-72);
		    g2d.setColor(Color.BLACK);
		    if(i-80>30)
		    	g2d.drawString(tasks, i-80, 72);
		    else
		    	g2d.drawString(tasks, 30, 72);
	        // restore	        
            g.setColor(c);
            g.setFont(f);
	    }
	}

	
	/**
	 * This method initializes Resources	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getResources() {
		if (Resources == null) {
			Resources = new JPanel();
			Resources.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5), BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
		}
		return Resources;
	}
	
	public void componentResized(ComponentEvent e) 
	{
		Dimension size = StateList.getSize();
		size.height = 130;
		e.getComponent().setMinimumSize(size);
		e.getComponent().setPreferredSize(size);
		e.getComponent().setMaximumSize(size);
		StateList.invalidate();
	}
	public void componentMoved(ComponentEvent e) {}
	public void componentShown(ComponentEvent e) {}
	public void componentHidden(ComponentEvent e) {}

	//private class DDSCO3Paint extends

}  //  @jve:decl-index=0:visual-constraint="10,-2"
