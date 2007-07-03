package org.redcross.sar.wp.tactics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.table.TableColumn;

import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.event.IDiskoMapEventListener;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.models.POITableModel;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.feature.IMsoFeatureClass;
import org.redcross.sar.mso.data.IAreaIf;

import com.esri.arcgis.interop.AutomationException;

public class DescriptionDialog extends DiskoDialog implements IDiskoMapEventListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel = null;
	private JScrollPane tableScrollPane = null;
	private JTable poiTable = null;
	private JScrollPane descriptionScrollPane = null;
	private JTextArea descriptionTextArea = null;
	private DiskoWpTacticsImpl wp = null;
	private POITableModel tableModel = null;

	public DescriptionDialog(DiskoWpTacticsImpl wp) {
		super(wp.getApplication().getFrame());
		this.wp = wp;
		initialize();
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		try {
            this.setPreferredSize(new Dimension(800, 150));
            this.setContentPane(getContentPanel());
			this.pack();
		}
		catch (java.lang.Throwable e) {
			//  Do Something
		}
	}
	
	public void setArea(IAreaIf area) {
		getPOITableModel().setArea(area);
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
				contentPanel.add(getTableScrollPane(), BorderLayout.CENTER);
				//contentPanel.add(getDescriptionScrollPane(), BorderLayout.CENTER);
				contentPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return contentPanel;
	}

	/**
	 * This method initializes tableScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getTableScrollPane() {
		if (tableScrollPane == null) {
			try {
				tableScrollPane = new JScrollPane();
				tableScrollPane.setViewportView(getPoiTable());
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return tableScrollPane;
	}
	
	private POITableModel getPOITableModel() {
		if (tableModel == null) {
			tableModel = new POITableModel(wp.getMsoModel());
		}
		return tableModel;
	}

	/**
	 * This method initializes poiTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getPoiTable() {
		if (poiTable == null) {
			try {
				poiTable = new JTable(getPOITableModel());
				poiTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				setColumnWidths();
				
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return poiTable;
	}
	
	private void setColumnWidths() {
		for (int i = 0; i < 3; i++) {
			TableColumn column = getPoiTable().getColumnModel().getColumn(i);
			column.setResizable(false);
			switch(i) {
				case 0: 
					column.setPreferredWidth(35);
					break;
				case 1:
					column.setPreferredWidth(50);
					break;
				case 2: 
					column.setPreferredWidth(600);
					break;
			}
		}
	}

	/**
	 * This method initializes descriptionScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getDescriptionScrollPane() {
		if (descriptionScrollPane == null) {
			try {
				descriptionScrollPane = new JScrollPane();
				descriptionScrollPane.setViewportView(getDescriptionTextArea());
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return descriptionScrollPane;
	}

	/**
	 * This method initializes descriptionTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getDescriptionTextArea() {
		if (descriptionTextArea == null) {
			try {
				descriptionTextArea = new JTextArea();
				descriptionTextArea.setLineWrap(true);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return descriptionTextArea;
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
		IMsoFeatureClass msoFC = (IMsoFeatureClass)e.getSource();
		List selection = msoFC.getSelected();
		if (selection != null && selection.size() > 0) {
			IMsoFeature msoFeature = (IMsoFeature)selection.get(0);
			IAreaIf area = (IAreaIf)msoFeature.getMsoObject();
			getPOITableModel().setArea(area);
			return;
		}
		getPOITableModel().setArea(null);
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
