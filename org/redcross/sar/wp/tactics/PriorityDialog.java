package org.redcross.sar.wp.tactics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.redcross.sar.app.Utils;
import org.redcross.sar.event.IMsoLayerEventListener;
import org.redcross.sar.event.MsoLayerEvent;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.renderers.RadioListCellRenderer;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.ISearchAreaIf;

import com.esri.arcgis.interop.AutomationException;

public class PriorityDialog extends DiskoDialog implements IMsoLayerEventListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel = null;
	private JList priorityList = null;
	private IMsoFeature currentMsoFeature = null;
	
	public PriorityDialog(DiskoWpTacticsImpl wp) {
		super(wp.getApplication().getFrame());
		//listener
		IMsoFeatureLayer msoLayer = wp.getMap().getMsoLayer(IMsoFeatureLayer.LayerCode.SEARCH_AREA_LAYER);
		msoLayer.addDiskoLayerEventListener(this);
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		try {
            this.setContentPane(getContentPanel());
            this.setPreferredSize(new Dimension(200, 150));
            this.pack();
		}
		catch (java.lang.Throwable e) {
			//  Do Something
		}
	}
	
	public void reset() {
		getPriorityList().clearSelection();
	}
	
	public int getPriority() {
		return getPriorityList().getSelectedIndex()+1;
	}
	
	public void setPriority(int priority) {
		getPriorityList().setSelectedIndex(priority-1);
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
				priorityList.setCellRenderer(new RadioListCellRenderer());
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
						if(e.getValueIsAdjusting()) {
							return;
						}
						if (currentMsoFeature != null) {
							IMsoObjectIf msoObject = currentMsoFeature.getMsoObject();
							ISearchAreaIf searchArea = (ISearchAreaIf)msoObject;
							searchArea.setPriority(getPriority());
						}
						setVisible(false);
						fireDialogStateChanged();
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return priorityList;
	}

	public void onSelectionChanged(MsoLayerEvent e) throws IOException, AutomationException {
		IMsoFeatureLayer msoLayer = (IMsoFeatureLayer)e.getSource();
		List selection = msoLayer.getSelected();
		if (selection != null && selection.size() > 0) {
			currentMsoFeature = (IMsoFeature)selection.get(0);
			IMsoObjectIf msoObject = currentMsoFeature.getMsoObject();
			if (msoObject instanceof ISearchAreaIf) {
				ISearchAreaIf searchArea = (ISearchAreaIf)msoObject;
				setPriority(searchArea.getPriority());
			}
		}
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
