package org.redcross.sar.wp.tacktics;

import java.awt.Dimension;
import java.awt.Frame;
import org.redcross.sar.gui.DiskoDialog;
import com.borland.jbcl.layout.VerticalFlowLayout;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.border.BevelBorder;

public class PriorityDialog extends DiskoDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel = null;
	private ButtonGroup bgroup = null;
	private JCheckBox pri1CheckBox = null;
	private JCheckBox pri2CheckBox = null;
	private JCheckBox pri3CheckBox = null;
	private JCheckBox pri4CheckBox = null;
	private JCheckBox pri5CheckBox = null;
	
	private int priority = 1;
	
	public PriorityDialog(Frame frame) {
		super(frame);
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
            this.setPreferredSize(new Dimension(150, 130));
            this.pack();
		}
		catch (java.lang.Throwable e) {
			//  Do Something
		}
	}
	
	public int getPriority() {
		return priority;
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
				VerticalFlowLayout vfl = new VerticalFlowLayout();
				vfl.setVgap(0);
				vfl.setHgap(0);
				vfl.setAlignment(VerticalFlowLayout.TOP);
				contentPanel.setLayout(vfl);
				contentPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				
				contentPanel.add(getPri1CheckBox(), null);
				contentPanel.add(getPri2CheckBox(), null);
				contentPanel.add(getPri3CheckBox(), null);
				contentPanel.add(getPri4CheckBox(), null);
				contentPanel.add(getPri5CheckBox(), null);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return contentPanel;
	}

	/**
	 * This method initializes pri1CheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getPri1CheckBox() {
		if (pri1CheckBox == null) {
			try {
				pri1CheckBox = new JCheckBox();
				pri1CheckBox.setText("Primær søketeig");
				pri1CheckBox.setSelected(true);
				pri1CheckBox.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						setVisible(false);
						priority = 1;
					}
				});
				bgroup.add(pri1CheckBox);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return pri1CheckBox;
	}

	/**
	 * This method initializes pri2CheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getPri2CheckBox() {
		if (pri2CheckBox == null) {
			try {
				pri2CheckBox = new JCheckBox();
				pri2CheckBox.setText("Sekundær søketeig");
				pri2CheckBox.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						setVisible(false);
						priority = 2;
					}
				});
				bgroup.add(pri2CheckBox);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return pri2CheckBox;
	}

	/**
	 * This method initializes pri3CheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getPri3CheckBox() {
		if (pri3CheckBox == null) {
			try {
				pri3CheckBox = new JCheckBox();
				pri3CheckBox.setSelected(false);
				pri3CheckBox.setText("3. Prioritet");
				pri3CheckBox.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						setVisible(false);
						priority = 3;
					}
				});
				bgroup.add(pri3CheckBox);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return pri3CheckBox;
	}

	/**
	 * This method initializes pri4CheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getPri4CheckBox() {
		if (pri4CheckBox == null) {
			try {
				pri4CheckBox = new JCheckBox();
				pri4CheckBox.setText("4. Prioritet");
				pri4CheckBox.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						setVisible(false);
						priority = 4;
					}
				});
				bgroup.add(pri4CheckBox);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return pri4CheckBox;
	}

	/**
	 * This method initializes pri5CheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getPri5CheckBox() {
		if (pri5CheckBox == null) {
			try {
				pri5CheckBox = new JCheckBox();
				pri5CheckBox.setText("5. Prioritet");
				pri5CheckBox.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						setVisible(false);
						priority = 5;
					}
				});
				bgroup.add(pri5CheckBox);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return pri5CheckBox;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
