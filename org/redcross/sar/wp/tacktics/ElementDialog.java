package org.redcross.sar.wp.tacktics;

import java.awt.Dimension;
import java.awt.Frame;

import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.ISearchIf;
import org.redcross.sar.mso.data.ISearchIf.SearchSubType;

import com.borland.jbcl.layout.VerticalFlowLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class ElementDialog extends DiskoDialog {

	private static final long serialVersionUID = 1L;
	private ActionListener listener = null;
	private JPanel contentPanel = null;
	private ButtonGroup bgroup = null;
	private JCheckBox operationAreaCheckBox = null;
	private JCheckBox searchAreaCheckBox = null;
	private JLabel jLabel = null;
	private JCheckBox patrolSearchCheckBox = null;
	private JCheckBox lineSearchCheckBox = null;
	private JCheckBox rodeSearchCheckBox = null;
	private JCheckBox beachSearchCheckBox = null;
	private JCheckBox maritimeSearchCheckBox = null;
	private JCheckBox dogSearchCheckBox = null;
	private JCheckBox airSearchCheckBox = null;
	private JCheckBox generalTaskCheckBox = null;
	
	public ElementDialog(Frame frame, ActionListener listener) {
		super(frame);
		this.listener = listener;
		bgroup = new ButtonGroup();
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
            this.setPreferredSize(new Dimension(150, 250));
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
				jLabel = new JLabel();
				jLabel.setText("       OPPDRAG");
				jLabel.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
				jLabel.setHorizontalAlignment(SwingConstants.LEFT);
				contentPanel = new JPanel();
				VerticalFlowLayout vfl = new VerticalFlowLayout();
				vfl.setVgap(0);
				vfl.setHgap(0);
				vfl.setAlignment(VerticalFlowLayout.TOP);
				contentPanel.setLayout(vfl);
				contentPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				
				contentPanel.add(getOperationAreaCheckBox(), null);
				contentPanel.add(getSearchAreaCheckBox(), null);
				contentPanel.add(jLabel, null);
				contentPanel.add(getPatrolSearchCheckBox(), null);
				contentPanel.add(getLineSearchCheckBox(), null);
				contentPanel.add(getRodeSearchCheckBox(), null);
				contentPanel.add(getBeachSearchCheckBox(), null);
				contentPanel.add(getMaritimeSearchCheckBox(), null);
				contentPanel.add(getDogSearchCheckBox(), null);
				contentPanel.add(getAirSearchCheckBox(), null);
				contentPanel.add(getGeneralTaskCheckBox(), null);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return contentPanel;
	}

	/**
	 * This method initializes operationAreaCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getOperationAreaCheckBox() {
		if (operationAreaCheckBox == null) {
			try {
				operationAreaCheckBox = new JCheckBox();
				operationAreaCheckBox.setText("Operasjonsomr�de");
				String cmd = IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA.toString();
				operationAreaCheckBox.setActionCommand(cmd);
				operationAreaCheckBox.addActionListener(listener);
				bgroup.add(operationAreaCheckBox);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return operationAreaCheckBox;
	}

	/**
	 * This method initializes searchAreaCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getSearchAreaCheckBox() {
		if (searchAreaCheckBox == null) {
			try {
				searchAreaCheckBox = new JCheckBox();
				searchAreaCheckBox.setText("S�keomr�de");
				String cmd = IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA.toString();
				searchAreaCheckBox.setActionCommand(cmd);
				searchAreaCheckBox.addActionListener(listener);
				bgroup.add(searchAreaCheckBox);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return searchAreaCheckBox;
	}

	/**
	 * This method initializes patrolSearchCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getPatrolSearchCheckBox() {
		if (patrolSearchCheckBox == null) {
			try {
				patrolSearchCheckBox = new JCheckBox();
				patrolSearchCheckBox.setSelected(true);
				patrolSearchCheckBox.setText("Patruljes�k");
				String cmd = SearchSubType.PATROL.toString();
				patrolSearchCheckBox.setActionCommand(cmd);
				patrolSearchCheckBox.addActionListener(listener);
				bgroup.add(patrolSearchCheckBox);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return patrolSearchCheckBox;
	}

	/**
	 * This method initializes lineSearchCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getLineSearchCheckBox() {
		if (lineSearchCheckBox == null) {
			try {
				lineSearchCheckBox = new JCheckBox();
				lineSearchCheckBox.setText("Linjes�k");
				String cmd = SearchSubType.LINE.toString();
				lineSearchCheckBox.setActionCommand(cmd);
				lineSearchCheckBox.addActionListener(listener);
				bgroup.add(lineSearchCheckBox);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return lineSearchCheckBox;
	}

	/**
	 * This method initializes rodeSearchCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getRodeSearchCheckBox() {
		if (rodeSearchCheckBox == null) {
			try {
				rodeSearchCheckBox = new JCheckBox();
				rodeSearchCheckBox.setText("Rodes�k");
				rodeSearchCheckBox.setEnabled(false);
				rodeSearchCheckBox.setActionCommand("ELEMENT_RODE_SEARCH");
				rodeSearchCheckBox.addActionListener(listener);
				bgroup.add(rodeSearchCheckBox);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return rodeSearchCheckBox;
	}

	/**
	 * This method initializes beachSearchCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getBeachSearchCheckBox() {
		if (beachSearchCheckBox == null) {
			try {
				beachSearchCheckBox = new JCheckBox();
				beachSearchCheckBox.setText("Strands�k");
				String cmd = SearchSubType.SHORELINE.toString();
				beachSearchCheckBox.setActionCommand(cmd);
				beachSearchCheckBox.addActionListener(listener);
				bgroup.add(beachSearchCheckBox);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return beachSearchCheckBox;
	}

	/**
	 * This method initializes maritimeSearchCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getMaritimeSearchCheckBox() {
		if (maritimeSearchCheckBox == null) {
			try {
				maritimeSearchCheckBox = new JCheckBox();
				maritimeSearchCheckBox.setText("Maritime s�k");
				String cmd = SearchSubType.MARINE.toString();
				maritimeSearchCheckBox.setActionCommand(cmd);
				maritimeSearchCheckBox.addActionListener(listener);
				bgroup.add(maritimeSearchCheckBox);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return maritimeSearchCheckBox;
	}

	/**
	 * This method initializes dogSearchCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getDogSearchCheckBox() {
		if (dogSearchCheckBox == null) {
			try {
				dogSearchCheckBox = new JCheckBox();
				dogSearchCheckBox.setText("Hundes�k");
				String cmd = SearchSubType.DOG.toString();
				dogSearchCheckBox.setActionCommand(cmd);
				dogSearchCheckBox.addActionListener(listener);
				bgroup.add(dogSearchCheckBox);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return dogSearchCheckBox;
	}

	/**
	 * This method initializes airSearchCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getAirSearchCheckBox() {
		if (airSearchCheckBox == null) {
			try {
				airSearchCheckBox = new JCheckBox();
				airSearchCheckBox.setText("Flys�k");
				String cmd = SearchSubType.AIR.toString();
				airSearchCheckBox.setActionCommand(cmd);
				airSearchCheckBox.addActionListener(listener);
				bgroup.add(airSearchCheckBox);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return airSearchCheckBox;
	}

	/**
	 * This method initializes generalTaskCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getGeneralTaskCheckBox() {
		if (generalTaskCheckBox == null) {
			try {
				generalTaskCheckBox = new JCheckBox();
				generalTaskCheckBox.setText("Generellt oppdrag");
				generalTaskCheckBox.setEnabled(false);
				generalTaskCheckBox.setActionCommand("ELEMENT_GENERAL_TASK");
				generalTaskCheckBox.addActionListener(listener);
				bgroup.add(generalTaskCheckBox);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return generalTaskCheckBox;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
