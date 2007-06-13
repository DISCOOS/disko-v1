package org.redcross.sar.wp.tactics;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.event.IDiskoMapEventListener;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.UnitTable;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IUnitIf;

import com.esri.arcgis.interop.AutomationException;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import java.awt.BorderLayout;
import java.io.IOException;
import java.util.List;

public class UnitSelectionDialog extends DiskoDialog implements IDiskoMapEventListener {

	private static final long serialVersionUID = 1L;
	private IMsoModelIf msoModel = null;
	private JPanel contentPanel = null;
	private JScrollPane jScrollPane = null;
	private UnitTable unitTable = null;
	
	public UnitSelectionDialog(IDiskoApplication app) {
		super(app.getFrame());
		this.msoModel = app.getMsoModel();
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
					if (list != null && list.contains(unit)) {
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
					}
				});
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return unitTable;
	}

	public void editLayerChanged(DiskoMapEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void onAfterScreenDraw(DiskoMapEvent e) throws IOException, AutomationException {
		// TODO Auto-generated method stub
		
	}

	public void onExtentUpdated(DiskoMapEvent e) throws IOException, AutomationException {
		// TODO Auto-generated method stub
		
	}

	public void onMapReplaced(DiskoMapEvent e) throws IOException, AutomationException {
		// TODO Auto-generated method stub
		
	}

	public void onSelectionChanged(DiskoMapEvent e) throws IOException, AutomationException {
		// TODO Auto-generated method stub
		
	}

}  //  @jve:decl-index=0:visual-constraint="10,2"
