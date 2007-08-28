package org.redcross.sar.gui;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import org.redcross.sar.mso.data.ITaskIf;
import org.redcross.sar.wp.messageLog.DiskoButtonFactory;

/**
 * 
 * @author thomasl
 *
 */
public class TaskDialog extends DiskoDialog
{
	private static final long serialVersionUID = 1L;
	private JPanel m_contentsPanel = null;
	
	private ITaskIf m_currentTask = null;
	
	private JButton m_finishedButton = null;
	private JButton m_cancelButton = null;
	
	private JTextField m_taskTextField = null;
	private JComboBox m_typeComboBox = null;
	private JComboBox m_priorityComboBox = null;
	private JComboBox m_dueComboBox = null;
	private JComboBox m_responsibleComboBox = null;
	private JComboBox m_warningComboBox = null;
	private JComboBox m_statusComboBox = null;
	private JTextField m_progressTextField = null;
	private JTextArea m_descriptionTextArea = null;
	private JTextArea m_sourceTextArea = null;
	
	public TaskDialog(Frame owner)
	{
		super(owner);
		initialize();
	}
	
	private void initialize()
	{
		m_contentsPanel = new JPanel(new GridBagLayout());
		m_contentsPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(4, 4, 4, 4);
		
		// Task
		m_taskTextField = new JTextField();
		gbc.gridwidth = 3;
		addComponent(0, "Oppgave", m_taskTextField, 1, gbc);
		
		// Type
		Object[] types = {"Transport", "Ressurs", 
				"Etterretning", "Generell"}; // TODO Update
		m_typeComboBox = new JComboBox(types);
		gbc.gridwidth = 3;
		addComponent(0, "Type", m_typeComboBox, 1, gbc);
		
		// Priority
		Object[] priorities = {"Høg", "Normal", "Lav"}; // TODO update
		m_priorityComboBox = new JComboBox(priorities);
		addComponent(0, "Prioritet", m_priorityComboBox, 0, gbc);
		
		// Due
		Object[] dueTimes = {"5 min", "10 min", "30 min"}; // TODO
		m_dueComboBox = new JComboBox(dueTimes);
		addComponent(2, "Forfall", m_dueComboBox, 1, gbc);
		
		// Responsible
		Object[] responsible = {"NK", "SL"}; // TODO
		m_responsibleComboBox = new JComboBox(responsible);
		addComponent(0, "Ansvarlig", m_responsibleComboBox, 0, gbc);
		
		// Warning
		Object[] warnings = {"5 min" , "10 min", "30 min"}; // TODO
		m_warningComboBox = new JComboBox(warnings);
		addComponent(2, "Varsel", m_warningComboBox, 1, gbc);
		
		// Status
		Object[] statuses = {"Ubehandlet", "Startet", "Utsatt", "Ferdig"}; // TODO
		m_statusComboBox = new JComboBox(statuses);
		addComponent(0, "Status", m_statusComboBox, 0, gbc);
		
		// Progress
		m_progressTextField = new JTextField();
		addComponent(2, "Fremdrift", m_progressTextField, 1, gbc);
		
		// Description
		m_descriptionTextArea = new JTextArea();
		m_descriptionTextArea.setRows(4);
		gbc.gridwidth = 3;
		addComponent(0, "Beskrivelse", m_descriptionTextArea, 4, gbc);
		
		// Source
		m_sourceTextArea = new JTextArea();
		m_sourceTextArea.setRows(4);
		gbc.gridwidth = 3;
		addComponent(0, "Kilde", m_sourceTextArea, 4, gbc);
		
		// Finish button
		JPanel actionButtonPanel = new JPanel();
		m_finishedButton = DiskoButtonFactory.createSmallButton("Ferdig");
		m_finishedButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				updateTask();
				setVisible(false);
			}
		});
		actionButtonPanel.add(m_finishedButton);
		
		// Cancel button
		m_cancelButton = DiskoButtonFactory.createSmallButton("Avbryt");
		m_cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				fireDialogCanceled();
				setVisible(false);
			}
		});
		actionButtonPanel.add(m_cancelButton);
		
		gbc.gridwidth = 4;
		gbc.gridy++;
		m_contentsPanel.add(actionButtonPanel, gbc);
		
		this.add(m_contentsPanel);
		this.pack();
	}
	
	private void addComponent(int column, String labelText, JComponent component, int numRows, GridBagConstraints gbc)
	{
		gbc.gridheight = Math.max(1, numRows);
		gbc.gridx = column + 1;
		m_contentsPanel.add(component, gbc);
		
		JLabel label = new JLabel(labelText);
		gbc.gridx = column;
		gbc.gridwidth = 1;
		m_contentsPanel.add(label, gbc);
		
		gbc.gridy += numRows;
	}
	
	public void setTask(ITaskIf task)
	{
		m_currentTask = task;
		updateFields();
	}
	
	/**
	 * Extracts values from swing components and updates MSO task object
	 */
	public void updateTask()
	{
		// TODO
	}
	
	/**
	 * Updates swing component contents with values stored in current task
	 */
	private void updateFields()
	{
		
	}
}
