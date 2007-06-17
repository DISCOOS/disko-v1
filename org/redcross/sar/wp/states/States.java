package org.redcross.sar.wp.states;

import javax.swing.JScrollPane;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.border.*;
import javax.swing.BoxLayout;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;

public class States extends JScrollPane implements ComponentListener{

	private static final long serialVersionUID = 1L;
	private JPanel StateList = null;
	private JPanel Resources = null;
	private StateBar ProdState = null;

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
		this.setSize(470, 295);
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
			StateList.add(getProdState(), null);
			StateList.add(getResources(), null);
			getProdState().addComponentListener(this);
			getResources().addComponentListener(this);
		}
		return StateList;
	}

	/**
	 * This method initializes ProdState	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private StateBar getProdState() {
		if (ProdState == null) {
			ProdState = new StateBar(20,35);
			ProdState.setLimits(100,30,50);
			Border etched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder title = BorderFactory.createTitledBorder(etched, "Antall klare oppdrag");
			ProdState.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5), title));			
		}
		return ProdState;
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
