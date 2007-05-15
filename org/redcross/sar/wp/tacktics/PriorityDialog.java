package org.redcross.sar.wp.tacktics;

import java.awt.Dimension;
import java.awt.Frame;

import org.redcross.sar.app.Utils;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.renderers.CheckableListCellRenderer;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JList;
import java.awt.BorderLayout;

public class PriorityDialog extends DiskoDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel = null;
	private JList priorityList = null;
	
	public PriorityDialog(Frame frame) {
		super(frame);
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
            this.setPreferredSize(new Dimension(140, 125));
            this.pack();
		}
		catch (java.lang.Throwable e) {
			//  Do Something
		}
	}
	
	public int getPriority() {
		return getPriorityList().getSelectedIndex()+1;
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
				contentPanel.add(getPriorityList(), BorderLayout.CENTER);
				
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return contentPanel;
	}

	/**
	 * This method initializes priorityList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getPriorityList() {
		if (priorityList == null) {
			try {
				priorityList = new JList();
				priorityList.setCellRenderer(new CheckableListCellRenderer());
				Object[] listData = new Object[5];
				listData[0] = Utils.translate("PRIMARY_SEARCH_AREA");
				listData[1] = Utils.translate("SECONDARY_SEARCH_AREA");
				listData[2] = Utils.translate("PRIORITY3_SEARCH_AREA");
				listData[3] = Utils.translate("PRIORITY4_SEARCH_AREA");
				listData[4] = Utils.translate("PRIORITY5_SEARCH_AREA");
				
				priorityList.setListData(listData);
				priorityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				priorityList.setSelectedIndex(0);
				priorityList.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						setVisible(false);
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return priorityList;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
