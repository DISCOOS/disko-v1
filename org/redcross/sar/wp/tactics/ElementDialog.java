package org.redcross.sar.wp.tactics;

import java.awt.Dimension;
import java.awt.Frame;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.renderers.RadioListCellRenderer;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.JList;
import java.awt.BorderLayout;

public class ElementDialog extends DiskoDialog {

	private static final long serialVersionUID = 1L;
	private ListSelectionListener listener = null;
	private JPanel contentPanel = null;
	private JList elementList = null;
	
	public ElementDialog(Frame frame, ListSelectionListener listener) {
		super(frame);
		this.listener = listener;
		initialize();
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		try {
            this.setContentPane(getContentPanel());
            this.setPreferredSize(new Dimension(200, 275));
            this.pack();
		}
		catch (java.lang.Throwable e) {
			//  Do Something
		}
	}

	/**
	 * This method initializes contentPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getContentPanel() {
		if (contentPanel == null) {
			try {
				contentPanel = new JPanel();
				contentPanel.setLayout(new BorderLayout());
				contentPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				contentPanel.add(getElementList(), BorderLayout.CENTER);
				
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return contentPanel;
	}

	/**
	 * This method initializes elementList	
	 * 	
	 * @return javax.swing.JList	
	 */
	JList getElementList() {
		if (elementList == null) {
			try {
				elementList = new JList();
				elementList.setCellRenderer(new RadioListCellRenderer());
				elementList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				elementList.addListSelectionListener(listener);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return elementList;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
