package org.redcross.sar.gui;

import java.awt.Frame;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;

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

	public NumPadDialog(Frame owner) {
		super(owner);
		initialize();
		this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
	}
	
	private void initialize(){
		this.setSize(new Dimension(200, 200));
        this.setContentPane(getComponentPanel());
        this.pack();
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
			oneButton.setToolTipText("");
			oneButton.setText("1");
			oneButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("1, tallet er 1"); // TODO Auto-generated Event stub actionPerformed()
					
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
			twoButton.setToolTipText("");
			twoButton.setText("2");
			twoButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("2, tallet er 2"); // TODO Auto-generated Event stub actionPerformed()
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
			threeButton.setText("3");
			threeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("3, tallet er 3()"); // TODO Auto-generated Event stub actionPerformed()
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
			fourButton.setText("4");
			fourButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("4, tallet er 4()"); // TODO Auto-generated Event stub actionPerformed()
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
			fiveButton.setText("5");
			fiveButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("5, tallet er 5()"); // TODO Auto-generated Event stub actionPerformed()
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
			sixButton.setText("6");
			sixButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("6, tallet er 6"); // TODO Auto-generated Event stub actionPerformed()
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
			sevenjButton.setToolTipText("");
			sevenjButton.setText("7");
			sevenjButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("7, tallet er 7"); // TODO Auto-generated Event stub actionPerformed()
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
			eightButton.setText("8");
			eightButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("8, tallet er 8"); // TODO Auto-generated Event stub actionPerformed()
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
			nineButton.setText("9");
			nineButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("9, tallet er 9"); // TODO Auto-generated Event stub actionPerformed()
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
			zeroButton.setToolTipText("");
			zeroButton.setText("0");
			zeroButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("0, tallet er 0"); // TODO Auto-generated Event stub actionPerformed()
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
			delButton.setText("Del");
			delButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("Slett"); // TODO Auto-generated Event stub actionPerformed()
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
			okButton.setText("OK");
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("ok"); // TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return okButton;
	}
	
	

}
