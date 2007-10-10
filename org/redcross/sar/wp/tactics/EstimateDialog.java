package org.redcross.sar.wp.tactics;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import org.redcross.sar.event.IMsoLayerEventListener;
import org.redcross.sar.event.MsoLayerEvent;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.NumPadDialog;
import org.redcross.sar.map.feature.IMsoFeature;
import org.redcross.sar.map.layer.IMsoFeatureLayer;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.ISearchIf;

import com.esri.arcgis.interop.AutomationException;

public class EstimateDialog extends DiskoDialog implements IMsoLayerEventListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel = null;
	private DiskoWpTacticsImpl wp = null;
	private JLabel timeLabel = null;
	private JTextField timeTextField = null;

	public EstimateDialog(DiskoWpTacticsImpl wp) {
		super(wp.getApplication().getFrame());
		this.wp = wp;
		//listener
		IMsoFeatureLayer msoLayer = wp.getMap().getMsoLayer(IMsoFeatureLayer.LayerCode.AREA_LAYER);
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
            this.setContentPane(getContentPanel());
            this.setPreferredSize(new Dimension(800,50));
            this.pack();
		}
		catch (java.lang.Throwable e) {
			//  Do Something
		}
	}
	
	private void reset() {
		getTimeTextField().setText(null);
	}
	
	public int getEstimatedTime() {
		try {
			return Integer.parseInt(getTimeTextField().getText());
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
		}
		return 0;
	}
	
	public void setEstimatedTime(int estimatedTime) {
		getTimeTextField().setText(String.valueOf(estimatedTime));
	}

	/**
	 * This method initializes contentPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getContentPanel() {
		if (contentPanel == null) {
			try {
				GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
				gridBagConstraints1.fill = GridBagConstraints.VERTICAL;
				gridBagConstraints1.gridy = 0;
				gridBagConstraints1.weightx = 1.0;
				gridBagConstraints1.anchor = GridBagConstraints.WEST;
				gridBagConstraints1.gridx = 1;
				GridBagConstraints gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.gridx = 0;
				gridBagConstraints.gridy = 0;
				timeLabel = new JLabel();
				timeLabel.setText("Estimert tidsbruk:");
				contentPanel = new JPanel();
				contentPanel.setLayout(new GridBagLayout());
				contentPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				contentPanel.add(timeLabel, gridBagConstraints);
				contentPanel.add(getTimeTextField(), gridBagConstraints1);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return contentPanel;
	}

	/**
	 * This method initializes timeTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTimeTextField() {
		if (timeTextField == null) {
			try {
				timeTextField = new JTextField();
				timeTextField.setText("0");
				timeTextField.setPreferredSize(new Dimension(100, 20));
				timeTextField.addKeyListener(new java.awt.event.KeyAdapter() {
					public void keyTyped(java.awt.event.KeyEvent e) {
						fireDialogStateChanged();
					}
				});
				timeTextField.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent e) {					
						if (e.getClickCount() == 2){
							NumPadDialog npDialog = wp.getApplication().
								getUIFactory().getNumPadDialog();
							Point p = timeTextField.getLocationOnScreen();
							p.setLocation(p.x + timeTextField.getWidth()-
									npDialog.getWidth(), p.y-npDialog.getHeight()-2);
							npDialog.setLocation(p);					
							npDialog.setTextField(timeTextField);
							npDialog.setVisible(true);	
							fireDialogStateChanged();
						}
					}
				});	
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return timeTextField;
	}

	public void editLayerChanged(MsoLayerEvent e) {
		// TODO Auto-generated method stub
	}

	public void onAfterScreenDraw(MsoLayerEvent e) throws IOException, AutomationException {
		// TODO Auto-generated method stub
	}

	public void onExtentUpdated(MsoLayerEvent e) throws IOException, AutomationException {
		// TODO Auto-generated method stub
	}

	public void onMapReplaced(MsoLayerEvent e) throws IOException, AutomationException {
		// TODO Auto-generated method stub
	}

	public void onSelectionChanged(MsoLayerEvent e) throws IOException, AutomationException {
		IMsoFeatureLayer msoLayer = (IMsoFeatureLayer)e.getSource();
		List selection = msoLayer.getSelected();
		if (selection != null && selection.size() > 0) {
			IMsoFeature msoFeature = (IMsoFeature)selection.get(0);
			IAreaIf area = (IAreaIf)msoFeature.getMsoObject();
			IAssignmentIf assignment = area.getOwningAssignment();
			if (assignment instanceof ISearchIf) {
				ISearchIf search = (ISearchIf)assignment;
				setEstimatedTime(search.getPlannedProgress());
				return;
			}
		}
		reset();
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
