package org.redcross.sar.gui;

import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;

public class NumPadDialog extends JDialog {

	private JPanel componentPanel = null;  //  @jve:decl-index=0:visual-constraint="328,118"
	private static final long serialVersionUID = 1L;
	private JButton oneButton = null;
	private JButton twoButton = null;
	private JButton threeButton = null;
	private JButton fourButton = null;
	private JButton fiveButton = null;
	private JButton sixButton = null;
	private JButton sevenjButton = null;
	private JButton eightButton = null;
	private JButton nineButton = null;
	private JButton zeroButton = null;
	private JButton delButton = null;
	private JButton okButton = null;	
	private JTextField jtf = null;
	private Dimension size = new Dimension(50,40);

	public NumPadDialog(Frame owner) {
		super(owner);
		initialize();
		this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);		
	}
	
	private void initialize(){
		this.setSize(new Dimension(200, 200));
        this.setContentPane(getComponentPanel());
        this.setUndecorated(true);
        this.addFocusListener(new java.awt.event.FocusAdapter() {
        	public void focusLost(java.awt.event.FocusEvent e) {
        		System.out.println("focusLost()"); // TODO Auto-generated Event stub focusLost()        		
        	}
        });
        this.setAlwaysOnTop(true);
        this.pack();
	}
	
	public void setTextField(JTextField jtf){
		System.out.println("kult skjult");
		this.jtf = jtf;
		System.out.println(":" + jtf.getText() + ":");
	}

	//test
	public void setTextField(String s){
		System.out.println(s);
	}
	
	private void subtractTextFieldValue(){
		if (jtf.getText().length() > 0){			
				jtf.setText(jtf.getText().substring(0, jtf.getText().length()-1));
		}		
	}
	
	private void addTextFieldValue(String s){
		System.out.println("skal sette textverdi");
		if (jtf.getText().length() == 0){
			jtf.setText(s);
		}
		else{ 
			jtf.setText(jtf.getText()+s);			
		}
		System.out.println(jtf.getText());
	}
	
	/**
	 * This method initializes componentPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getComponentPanel() {
		if (componentPanel == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(4);
			gridLayout.setColumns(3);
			componentPanel = new JPanel();
			componentPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			componentPanel.setLayout(gridLayout);		
			componentPanel.setSize(new Dimension(215, 214));
			componentPanel.add(getOneButton(), null);
			componentPanel.add(getTwoButton(), null);
			componentPanel.add(getThreeButton(), null);
			componentPanel.add(getFourButton(), null);
			componentPanel.add(getFiveButton(), null);
			componentPanel.add(getSixButton(), null);
			componentPanel.add(getSevenjButton(), null);
			componentPanel.add(getEightButton(), null);
			componentPanel.add(getNineButton(), null);
			componentPanel.add(getZeroButton(), null);
			componentPanel.add(getDelButton(), null);
			componentPanel.add(getOkButton(), null);
		}
		return componentPanel;
	}

	/**
	 * This method initializes oneButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOneButton() {
		if (oneButton == null) {
			oneButton = new JButton();
			oneButton.setPreferredSize(size);
			oneButton.setText("1");
			oneButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("1, tallet er 1"); // TODO Auto-generated Event stub actionPerformed()
					//setTextFieldValue("1", true);
				}
			});
			
			oneButton.doClick();
			
		}
		return oneButton;
	}

	/**
	 * This method initializes twoButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getTwoButton() {
		if (twoButton == null) {
			twoButton = new JButton();
			twoButton.setPreferredSize(size);
			twoButton.setText("2");
			twoButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					addTextFieldValue("2");
				}
			});
		}
		return twoButton;
	}

	/**
	 * This method initializes threeButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getThreeButton() {
		if (threeButton == null) {
			threeButton = new JButton();
			threeButton.setPreferredSize(size);
			threeButton.setText("3");
			threeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					addTextFieldValue("3");
				}
			});
		}
		return threeButton;
	}

	/**
	 * This method initializes fourButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getFourButton() {
		if (fourButton == null) {
			fourButton = new JButton();
			fourButton.setPreferredSize(size);
			fourButton.setText("4");
			fourButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					addTextFieldValue("4");
				}
			});
		}
		return fourButton;
	}

	/**
	 * This method initializes fiveButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getFiveButton() {
		if (fiveButton == null) {
			fiveButton = new JButton();
			fiveButton.setPreferredSize(size);
			twoButton.setToolTipText("5");
			fiveButton.setText("5");
			fiveButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					addTextFieldValue("5");
				}
			});
		}
		return fiveButton;
	}

	/**
	 * This method initializes sixButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSixButton() {
		if (sixButton == null) {
			sixButton = new JButton();
			sixButton.setPreferredSize(size);
			sixButton.setText("6");
			sixButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					addTextFieldValue("6");
				}
			});
		}
		return sixButton;
	}

	/**
	 * This method initializes sevenjButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getSevenjButton() {
		if (sevenjButton == null) {
			sevenjButton = new JButton();
			sevenjButton.setPreferredSize(size);
			sevenjButton.setToolTipText("");
			sevenjButton.setText("7");
			sevenjButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					addTextFieldValue("7");
				}
			});
		}
		return sevenjButton;
	}

	/**
	 * This method initializes eightButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getEightButton() {
		if (eightButton == null) {
			eightButton = new JButton();
			eightButton.setPreferredSize(size);
			eightButton.setText("8");
			eightButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					addTextFieldValue("8");
				}
			});
		}
		return eightButton;
	}

	/**
	 * This method initializes nineButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getNineButton() {
		if (nineButton == null) {
			nineButton = new JButton();
			nineButton.setPreferredSize(size);
			nineButton.setText("9");
			nineButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					addTextFieldValue("9");
				}
			});
		}
		return nineButton;
	}

	/**
	 * This method initializes zeroButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getZeroButton() {
		if (zeroButton == null) {
			zeroButton = new JButton();
			zeroButton.setPreferredSize(size);
			zeroButton.setText("0");
			zeroButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					addTextFieldValue("0");
				}
			});
		}
		return zeroButton;
	}

	/**
	 * This method initializes delButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getDelButton() {
		if (delButton == null) {
			delButton = new JButton();
			delButton.setPreferredSize(size);
			delButton.setText("Del");
			delButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					subtractTextFieldValue();
				}
			});
		}
		return delButton;
	}

	/**
	 * This method initializes okButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setPreferredSize(size);
			okButton.setText("OK");
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("ok"); // TODO Auto-generated Event stub actionPerformed()
					setVisible(false);
				}
			});
		}
		return okButton;
	}
	


}  //  @jve:decl-index=0:visual-constraint="43,48"
