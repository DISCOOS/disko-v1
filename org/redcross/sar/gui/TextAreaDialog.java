package org.redcross.sar.gui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

public class TextAreaDialog extends DiskoDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel = null;
	private JScrollPane textAreaScrollPane = null;
	private JTextArea inputTextArea = null;
	private JLabel headerLabel = null;
	
	public TextAreaDialog(Frame owner) {
		super(owner);
		initialize();
		// TODO Auto-generated constructor stub
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		try {
            this.setPreferredSize(new Dimension(600, 125));
            this.setContentPane(getContentPanel());
            this.pack();
		}
		catch (java.lang.Throwable e) {
			//  Do Something
		}
	}
	
	public String getText() {
		return getInputTextArea().getText();
	}
	
	public void setText(String text) {
		getInputTextArea().setText(text);
	}
	
	public void setHeaderText(String text) {
		headerLabel.setText(text);
	}

	/**
	 * This method initializes contentPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getContentPanel() {
		if (contentPanel == null) {
			try {
				headerLabel = new JLabel();
//				headerLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
				contentPanel = new JPanel();
				contentPanel.setLayout(new BorderLayout());
				contentPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				contentPanel.add(getTextAreaScrollPane(), BorderLayout.CENTER);
				contentPanel.add(headerLabel, BorderLayout.NORTH);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return contentPanel;
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
				textAreaScrollPane.setViewportView(getInputTextArea());
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return textAreaScrollPane;
	}

	/**
	 * This method initializes inputTextArea	
	 * 	
	 * @return javax.swing.JTextArea	
	 */
	private JTextArea getInputTextArea() {
		if (inputTextArea == null) {
			try {
				inputTextArea = new JTextArea();
				inputTextArea.setLineWrap(true);
			} catch (java.lang.Throwable e) {
				// TODO: Something
			}
		}
		return inputTextArea;
	}

}
