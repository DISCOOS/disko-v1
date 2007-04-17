package org.redcross.sar.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.app.Utils;
import com.borland.jbcl.layout.VerticalFlowLayout;
import com.esri.arcgis.display.SimpleMarkerSymbol;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JScrollPane;



public class PUIDialog extends DiskoDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPanel = null;
	private NumPadDialog numPadDialog = null;
	private IDiskoApplication app = null;
	private JPanel northPanel = null;
	private JButton cancelButton = null;
	private JPanel southPanel = null;
	private JButton finishButton = null;
	private JTextField xCoordTextField = null;
	private JTextField yCoordTextField = null;
	private JComboBox typeComboBox = null;
	private JLabel txtAreaLabel = null;
	private JTextArea txtArea = null;
	private JPanel centerPanel = null;
	private JLabel xCoordLabel = null;
	private JLabel yCoordLabel = null;
	private JLabel typeLabel = null;
	private JScrollPane textAreaScrollPane = null;
	

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
		try {
            this.setPreferredSize(new Dimension(175,310));
            //this.setSize(new Dimension(175, 350));
            this.setContentPane(getContentPanel());
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
				txtAreaLabel = new JLabel();
				txtAreaLabel.setText("Beskrivelse:");
				contentPanel = new JPanel();
				contentPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				VerticalFlowLayout vfl = new VerticalFlowLayout();
				vfl.setVgap(0);
				vfl.setHgap(5);
				vfl.setAlignment(VerticalFlowLayout.TOP);
				contentPanel.setLayout(vfl);
				contentPanel.add(getNorthPanel(), null);
				contentPanel.add(getCenterPanel(), null);
				contentPanel.add(txtAreaLabel, null);
				contentPanel.add(getTextAreaScrollPane(), null);
				contentPanel.add(getSouthPanel(), null);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
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
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			flowLayout.setVgap(5);
			flowLayout.setHgap(0);
			northPanel = new JPanel();
			northPanel.setLayout(flowLayout);
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
			try {
				cancelButton = new JButton();
				cancelButton.setPreferredSize(new Dimension(36, 36));
				cancelButton.setMnemonic(KeyEvent.VK_UNDEFINED);
				String iconName = "quit.icon";
				Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				cancelButton.setIcon(icon);
				cancelButton.setText("");
				cancelButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return cancelButton;
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
			flowLayout1.setVgap(5);
			flowLayout1.setHgap(0);
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
			try {
				finishButton = new JButton();
				finishButton.setPreferredSize(new Dimension(36, 36));
				String iconName = "finish.icon";
				Icon icon = Utils.createImageIcon(app.getProperty(iconName),iconName);
				finishButton.setIcon(icon);
				finishButton.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return finishButton;
	}
	
	/**
	 * This method sets textvalue for xCoordTextField	
	 * 	
	 */
	public void setXCoordFieldText(String s){
		s = s.substring(0,s.indexOf("."));//ikke helt elegant, burde ligge i en util klasse som sjekker for locale komma/punktum settinger?
		this.xCoordTextField.setText(s);
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
					System.out.println("mouseClicked()"); 
					if (e.getClickCount() == 2){
						numPadDialog = app.getUIFactory().getNumPadDialog();
						Point p = xCoordTextField.getLocationOnScreen();
						p.setLocation(p.x + (xCoordTextField.getWidth()), p.y);
						numPadDialog.setLocation(p);					
						numPadDialog.setTextField(xCoordTextField);
						numPadDialog.setVisible(true);	
						
					}
				}
			});	
			
		}
		return xCoordTextField;
	}
	
	/**
	 * This method sets textvalue for yCoordTextField	
	 * 	
	 */	
	public void setYCoordFieldText(String s){
		s = s.substring(0,s.indexOf("."));
		this.yCoordTextField.setText(s);
	}
	/**
	 * This method initializes yCoordTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getYCoordTextField() {
		if (yCoordTextField == null) {
			yCoordTextField = new JTextField();
			
			yCoordTextField.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					System.out.println("mouseClicked() " + e.getClickCount());
					if (e.getClickCount() == 2){										
						numPadDialog = app.getUIFactory().getNumPadDialog();
						Point p = yCoordTextField.getLocationOnScreen();
						p.setLocation(p.x + (yCoordTextField.getWidth()), p.y);
						numPadDialog.setLocation(p);
						numPadDialog.setTextField(yCoordTextField);
						numPadDialog.setVisible(true);
					}
				}
			});
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
			typeComboBox.addItem("type 1");
			typeComboBox.addItem("type 2");
			typeComboBox.addItem("type 3");
		}
		return typeComboBox;
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
	/**
	 * This method initializes centerPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getCenterPanel() {
		if (centerPanel == null) {
			try {
				GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
				gridBagConstraints31.gridx = 0;
				gridBagConstraints31.anchor = GridBagConstraints.WEST;
				gridBagConstraints31.gridy = 2;
				typeLabel = new JLabel();
				typeLabel.setText("Type:");
				GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
				gridBagConstraints21.gridx = 0;
				gridBagConstraints21.anchor = GridBagConstraints.WEST;
				gridBagConstraints21.insets = new Insets(0, 0, 10, 0);
				gridBagConstraints21.gridy = 1;
				yCoordLabel = new JLabel();
				yCoordLabel.setText("Y koordinat:");
				GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
				gridBagConstraints11.gridx = 0;
				gridBagConstraints11.anchor = GridBagConstraints.WEST;
				gridBagConstraints11.insets = new Insets(0, 0, 10, 0);
				gridBagConstraints11.gridy = 0;
				xCoordLabel = new JLabel();
				xCoordLabel.setText("X koordinat:");
				GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
				gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
				gridBagConstraints5.gridy = 2;
				gridBagConstraints5.weightx = 1.0;
				gridBagConstraints5.insets = new Insets(0, 5, 0, 0);
				gridBagConstraints5.gridx = 1;
				GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
				gridBagConstraints4.gridx = 0;
				gridBagConstraints4.gridy = 2;
				GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
				gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
				gridBagConstraints3.gridy = 1;
				gridBagConstraints3.weightx = 1.0;
				gridBagConstraints3.insets = new Insets(0, 5, 10, 0);
				gridBagConstraints3.gridx = 1;
				GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
				gridBagConstraints2.gridx = 0;
				gridBagConstraints2.gridy = 1;
				GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
				gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
				gridBagConstraints1.gridy = 0;
				gridBagConstraints1.weightx = 1.0;
				gridBagConstraints1.insets = new Insets(0, 5, 10, 0);
				gridBagConstraints1.gridx = 1;
				GridBagConstraints gridBagConstraints = new GridBagConstraints();
				gridBagConstraints.gridx = 0;
				gridBagConstraints.gridy = 0;
				centerPanel = new JPanel();
				centerPanel.setLayout(new GridBagLayout());
				centerPanel.setPreferredSize(new Dimension(0, 100));
				centerPanel.add(getXCoordTextField(), gridBagConstraints1);
				centerPanel.add(getYCoordTextField(), gridBagConstraints3);
				centerPanel.add(getTypeComboBox(), gridBagConstraints5);
				centerPanel.add(xCoordLabel, gridBagConstraints11);
				centerPanel.add(yCoordLabel, gridBagConstraints21);
				centerPanel.add(typeLabel, gridBagConstraints31);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return centerPanel;
	}

	/**
	 * This method initializes textAreaScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getTextAreaScrollPane() {
		if (textAreaScrollPane == null) {
			try {
				textAreaScrollPane = new JScrollPane();
				textAreaScrollPane.setViewportView(getTxtArea());
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return textAreaScrollPane;
	}	

}  //  @jve:decl-index=0:visual-constraint="10,10"
