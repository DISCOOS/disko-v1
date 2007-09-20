package org.redcross.sar.wp.unit;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.redcross.sar.gui.DiskoButtonFactory;
import org.redcross.sar.mso.data.IPersonnelIf;

/**
 * Bottom panel displaying summary info about personnel
 * 
 * @author thomasl
 */
public class PersonnelBottomPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private final static ResourceBundle m_resources = ResourceBundle.getBundle("org.redcross.sar.wp.unit.unit");
	
	private IPersonnelIf m_currentPersonnel;
	
	private JTextField m_addressTextField;
	private JTextField m_postAreaTextField;
	private JTextField m_postNumberTextField;
	private JButton m_showInMapButton;
	
	public PersonnelBottomPanel()
	{
		initialize();
	}
	
	private void initialize()
	{
		this.setLayout(new GridBagLayout());
		GridBagConstraints topLevelConstraints = new GridBagConstraints();
		topLevelConstraints.fill = GridBagConstraints.BOTH;
		topLevelConstraints.gridheight = 1;
		topLevelConstraints.weightx = 1.0;
		
		// Top left
		JPanel topLeftPanel = new JPanel(new GridBagLayout());
		GridBagConstraints topLeftConstraints = new GridBagConstraints();
		topLeftConstraints.insets = new Insets(4, 4, 4, 4);
		topLeftConstraints.fill = GridBagConstraints.HORIZONTAL;
	
		topLeftConstraints.weightx = 0.0;
		topLeftPanel.add(new JLabel(m_resources.getString("Address.text")), topLeftConstraints);
		topLeftConstraints.gridwidth = 3;
		topLeftConstraints.gridx = 1;
		topLeftConstraints.weightx = 1.0;
		m_addressTextField = new JTextField();
		topLeftPanel.add(m_addressTextField, topLeftConstraints);
		
		topLeftConstraints.gridy = 1;
		topLeftConstraints.gridx = 0;
		topLeftConstraints.gridwidth = 1;
		topLeftConstraints.weightx = 0.0;
		topLeftPanel.add(new JLabel(m_resources.getString("PostArea.text")), topLeftConstraints);
		topLeftConstraints.gridx = 1;
		topLeftConstraints.weightx = 1.0;
		m_postAreaTextField = new JTextField();
		topLeftPanel.add(m_postAreaTextField, topLeftConstraints);
		
		topLeftConstraints.gridx = 2;
		topLeftConstraints.weightx = 0.0;
		topLeftPanel.add(new JLabel(m_resources.getString("PostNumber.text")), topLeftConstraints);
		topLeftConstraints.gridx = 3;
		topLeftConstraints.weightx = 1.0;
		m_postNumberTextField = new JTextField();
		topLeftPanel.add(m_postNumberTextField, topLeftConstraints);
		
		topLeftConstraints.gridx = 4;
		topLeftConstraints.gridy = 0;
		topLeftConstraints.gridheight = 2;
		topLeftConstraints.weightx = 0.0;
		m_showInMapButton = DiskoButtonFactory.createSmallButton("Show in map");
		topLeftPanel.add(m_showInMapButton, topLeftConstraints);
		
		topLevelConstraints.gridwidth = 2;
		topLevelConstraints.weighty = 0.5;
		this.add(topLeftPanel, topLevelConstraints);
		
		// Bottom left
		JPanel bottomLeftPanel = new JPanel();
		
		topLevelConstraints.gridy = 1;
		topLevelConstraints.weighty = 1.0;
		this.add(bottomLeftPanel, topLevelConstraints);
		
		// Right
		JPanel rightPanel = new JPanel();
	
		topLevelConstraints.gridx = 2;
		topLevelConstraints.gridy = 0;
		topLevelConstraints.gridwidth = 1;
		topLeftConstraints.gridheight = 3;
		this.add(rightPanel, topLevelConstraints);
	}

	/**
	 * Set the personnel the panel is currently displaying information about, updates component contents
	 * @param personnel
	 */
	public void setPersonnel(IPersonnelIf personnel)
	{
		m_currentPersonnel = personnel;
		updateComponentContents();
	}
	
	private void updateComponentContents()
	{
		if(m_currentPersonnel == null)
		{
			m_addressTextField.setText("");
			m_postAreaTextField.setText("");
			m_postNumberTextField.setText("");
			m_showInMapButton.setEnabled(false);
		}
		else
		{
//			m_addressTextField.setText(m_currentPersonnel.getResidence().toString());
//			m_postAreaTextField.setText()
//			m_postNumberTextField.setText(t);
			m_showInMapButton.setEnabled(true);
		}
	}
}
