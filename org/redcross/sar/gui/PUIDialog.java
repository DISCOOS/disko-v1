package org.redcross.sar.gui;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import com.borland.jbcl.layout.VerticalFlowLayout;
import org.redcross.sar.app.IDiskoApplication;
//import com.geodata.engine.disko.app.DiskoApplication;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

public class PUIDialog extends DiskoDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private NumPadDialog numPadDialog = null;
	private IDiskoApplication app = null;
	private JPanel contentPanel = null;
	private JPanel northPanel = null;
	private JButton cancelButton = null;
	private JPanel centerPanel = null;
	private JPanel southPanel = null;
	private JButton finishButton = null;
	private JLabel puiLabel = null;
	private JLabel xCoordLabel = null;
	private JPanel puiInfoPanel = null;
	private JTextField xCoordTextField = null;
	private JLabel yCoordLabel = null;
	private JTextField yCoordTextField = null;
	private JLabel typeLabel = null;
	private JComboBox typeComboBox = null;
	private JPanel coordsPanel1 = null;
	private JPanel txtAreaPanel = null;
	private JScrollPane jScrollPane = null;
	private JLabel txtAreaLabel = null;
	private JTextArea txtArea = null;
	
	public PUIDialog(IDiskoApplication app) {
		super(app.getFrame());
		this.app = app;
		initialize();
		// TODO Auto-generated constructor stub
	}
	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setSize(new Dimension(223, 486));
        this.setContentPane(getContentPanel());
        this.pack();
			
	}
	
	/**
	 * This method initializes contentPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getContentPanel() {
		if (contentPanel == null) {
			contentPanel = new JPanel();
			contentPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			contentPanel.setLayout(new BorderLayout());
			contentPanel.setPreferredSize(new Dimension(150, 300));
			contentPanel.add(getNorthPanel(), BorderLayout.NORTH);
			contentPanel.add(getCenterPanel(), BorderLayout.CENTER);
			contentPanel.add(getSouthPanel(), BorderLayout.SOUTH);
		}
		return contentPanel;
	}
	/**
	 * This method initializes northPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getNorthPanel() {
		if (northPanel == null) {
			puiLabel = new JLabel();
			puiLabel.setText("PUI dialog");
			puiLabel.setHorizontalAlignment(SwingConstants.TRAILING);
			puiLabel.setHorizontalTextPosition(SwingConstants.TRAILING);
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			northPanel = new JPanel();
			northPanel.setLayout(flowLayout);
			northPanel.add(puiLabel, null);
			northPanel.add(getCancelButton(), null);
		}
		return northPanel;
	}
	/**
	 * This method initializes cancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setPreferredSize(new Dimension(36, 36));
			cancelButton.setMnemonic(KeyEvent.VK_UNDEFINED);
			cancelButton.setIcon(new ImageIcon("C:/Utvikling/workspace/DISKO/icons/quit.gif"));
			cancelButton.setText("");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return cancelButton;
	}
	/**
	 * This method initializes centerPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCenterPanel() {
		if (centerPanel == null) {		
			
			centerPanel = new JPanel();
			VerticalFlowLayout vfl = new VerticalFlowLayout();
			vfl.setVgap(2);
			vfl.setHgap(2);
			vfl.setAlignment(VerticalFlowLayout.TOP);
			centerPanel.setLayout(vfl);
			centerPanel.add(getPuiInfoPanel(), null);
			
		}
		return centerPanel;
	}
	/**
	 * This method initializes southPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSouthPanel() {
		if (southPanel == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setAlignment(FlowLayout.RIGHT);
			southPanel = new JPanel();
			southPanel.setLayout(flowLayout1);
			southPanel.add(getFinishButton(), null);
		}
		return southPanel;
	}
	/**
	 * This method initializes finishButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getFinishButton() {
		if (finishButton == null) {
			finishButton = new JButton();
			finishButton.setPreferredSize(new Dimension(36, 36));
			finishButton.setIcon(new ImageIcon("C:/Utvikling/workspace/DISKO/icons/finish.gif"));
			finishButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return finishButton;
	}
	
	/**
	 * This method initializes puiInfoPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPuiInfoPanel() {
		if (puiInfoPanel == null) {			
			puiInfoPanel = new JPanel();
			VerticalFlowLayout vfl = new VerticalFlowLayout();
			vfl.setAlignment(VerticalFlowLayout.TOP);
			puiInfoPanel.setLayout(vfl);
			
			puiInfoPanel.add(getCoordsPanel1(), null);
			puiInfoPanel.add(getTxtAreaPanel(), null);
		}
		return puiInfoPanel;
	}
	/**
	 * This method initializes xCoordTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getXCoordTextField() {
		if (xCoordTextField == null) {
			xCoordTextField = new JTextField();
			
			xCoordTextField.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					System.out.println("mouseClicked()"); // TODO Auto-generated Event stub mouseClicked()
					//e.
					numPadDialog = app.getUIFactory().getNumPadDialog();
					numPadDialog.setLocation(xCoordTextField.getLocationOnScreen());
					numPadDialog.setVisible(true);
					
				}
			});
			
		}
		return xCoordTextField;
	}
	/**
	 * This method initializes yCoordTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getYCoordTextField() {
		if (yCoordTextField == null) {
			yCoordTextField = new JTextField();
			//add actionListener
		}
		return yCoordTextField;
	}
	/**
	 * This method initializes typeComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getTypeComboBox() {
		if (typeComboBox == null) {
			typeComboBox = new JComboBox();
		}
		return typeComboBox;
	}
	
		
	/**
	 * This method initializes coordsPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCoordsPanel1() {
		if (coordsPanel1 == null) {
			coordsPanel1 = new JPanel();
						
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(3);
			gridLayout.setColumns(2);
			coordsPanel1.setLayout(gridLayout);
			
			xCoordLabel = new JLabel();
			xCoordLabel.setText("X koordinat");
			coordsPanel1.add(xCoordLabel, null);
			coordsPanel1.add(getXCoordTextField(), null);
			
			yCoordLabel = new JLabel();
			yCoordLabel.setText("Y koordinat");
			coordsPanel1.add(yCoordLabel, null);
			coordsPanel1.add(getYCoordTextField(), null);
			
			typeLabel = new JLabel();
			typeLabel.setText("Type");
			coordsPanel1.add(typeLabel, null);
			coordsPanel1.add(getTypeComboBox(), null);
			
		}
		return coordsPanel1;
	}
	/**
	 * This method initializes txtAreaPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getTxtAreaPanel() {
		if (txtAreaPanel == null) {
			
			txtAreaPanel = new JPanel();
			VerticalFlowLayout vfl = new VerticalFlowLayout();
			vfl.setAlignment(VerticalFlowLayout.TOP);
			vfl.setHgap(0);
			vfl.setVgap(0);
			txtAreaPanel.setLayout(vfl);	
			
			//add content
			txtAreaLabel = new JLabel();
			txtAreaLabel.setText("Beskrivelse");
			txtAreaPanel.add(txtAreaLabel, null);
			txtAreaPanel.add(getJScrollPane());			
			
		}
		return txtAreaPanel;
	}
	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getTxtArea());
		}
		return jScrollPane;
	}
	/**
	 * This method initializes txtArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getTxtArea() {
		if (txtArea == null) {
			txtArea = new JTextArea();
			txtArea.setRows(5);
			txtArea.setColumns(1);
		}
		return txtArea;
	}	
	
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
