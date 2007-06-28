package org.redcross.sar.wp.tactics;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.NumPadDialog;

import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;

public class EstimateDialog extends DiskoDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel = null;
	private DiskoWpTacticsImpl wp = null;
	private JLabel timeLabel = null;
	private JTextField timeTextField = null;

	public EstimateDialog(DiskoWpTacticsImpl wp) {
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
            this.setContentPane(getContentPanel());
            this.setPreferredSize(new Dimension(800,50));
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
				timeTextField.setPreferredSize(new Dimension(100, 20));
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
						}
					}
				});	
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return timeTextField;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
