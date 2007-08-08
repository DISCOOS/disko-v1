package org.redcross.sar.wp.tactics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;

import org.redcross.sar.event.IMsoLayerEventListener;
import org.redcross.sar.event.MsoLayerEvent;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.UnitTable;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IUnitIf;

import com.esri.arcgis.interop.AutomationException;

public class UnitSelectionDialog extends DiskoDialog implements IMsoLayerEventListener {

	private static final long serialVersionUID = 1L;
	private IMsoModelIf msoModel = null;
	private JPanel contentPanel = null;
	private JScrollPane jScrollPane = null;
	private UnitTable unitTable = null;
	
	public UnitSelectionDialog(DiskoWpTacticsImpl wp) {
		super(wp.getApplication().getFrame());
		this.msoModel = wp.getMsoModel();
		//listener
		IMsoFeatureLayer msoLayer = wp.getApplication().getDiskoMapManager().
		getMsoLayer(IMsoFeatureLayer.LayerCode.AREA_LAYER);
		msoLayer.addDiskoLayerEventListener(this);
		initialize();
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		try {
            this.setPreferredSize(new Dimension(210, 225));
            this.setContentPane(getContentPanel());
			this.pack();
		}
		catch (java.lang.Throwable e) {
			//  Do Something
		}
	}
	
	public IUnitIf getSelectedUnit() {
		JTable table = getUnitTable();
		int row = table.getSelectedRow();
		int col = table.getSelectedColumn();
		if (row > -1 && col > -1) {
			return (IUnitIf)table.getValueAt(row, col);
		}
		return null;
	}
	
	public void selectedAssignedUnit(IAssignmentIf assignment) {
		JTable table = getUnitTable();
		for (int row = 0; row < table.getRowCount(); row++) {
			for (int col = 0; col < table.getColumnCount(); col++) {
				IUnitIf unit = (IUnitIf)table.getValueAt(row, col);
				if (unit != null) {
					List list = unit.getAssignedAssignments();
					if (list != null && list.contains(assignment)) {
						table.setRowSelectionInterval(row, row);
						table.setColumnSelectionInterval(col, col);
						return;
					}
				}
			}
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
				contentPanel.add(getJScrollPane(), BorderLayout.CENTER);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return contentPanel;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			try {
				jScrollPane = new JScrollPane();
				jScrollPane.getViewport().setBackground(Color.white);
				jScrollPane.setViewportView(getUnitTable());
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return jScrollPane;
	}

	/**
	 * This method initializes unitTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private UnitTable getUnitTable() {
		if (unitTable == null) {
			try {
				unitTable = new UnitTable(msoModel, 3);
				unitTable.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseReleased(java.awt.event.MouseEvent e) {
						setVisible(false);
						fireDialogStateChanged();
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return unitTable;
	}

	public void onSelectionChanged(MsoLayerEvent e) throws IOException, AutomationException {
		IMsoFeatureLayer msoLayer = (IMsoFeatureLayer)e.getSource();
		List selection = msoLayer.getSelected();
		if (selection != null && selection.size() > 0) {
			IMsoFeature msoFeature = (IMsoFeature)selection.get(0);
			IAreaIf area = (IAreaIf)msoFeature.getMsoObject();
			IAssignmentIf assignment = area.getOwningAssignment();
			selectedAssignedUnit(assignment);
			return;
		}
	}

}  //  @jve:decl-index=0:visual-constraint="10,2"
