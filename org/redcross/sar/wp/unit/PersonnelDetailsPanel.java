package org.redcross.sar.wp.unit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.redcross.sar.gui.DiskoButtonFactory;
import org.redcross.sar.gui.renderers.SimpleListCellRenderer;
import org.redcross.sar.mso.data.IPersonnelIf;
import org.redcross.sar.mso.data.IPersonnelIf.PersonnelStatus;
import org.redcross.sar.mso.data.IPersonnelIf.PersonnelType;
import org.redcross.sar.util.except.IllegalMsoArgumentException;
import org.redcross.sar.util.mso.DTG;

/**
 * JPanel displaying team details.
 * Responsible for storing personnel in MSO.
 * 
 * @author thomasl
 */
public class PersonnelDetailsPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private final static ResourceBundle m_resources = ResourceBundle.getBundle("org.redcross.sar.wp.unit.unit");
	
	private IPersonnelIf m_currentPersonnel = null;
	private IDiskoWpUnit m_wpUnit;
	
	private JLabel m_topLabel;
	private JButton m_changeStatusButton;
	
	private JTextField m_nameTextField;
	private JTextField m_cellTextField;
	private JComboBox m_propertyComboBox;
	private JTextField m_organizationTextField;
	private JTextField m_departmentTextField;
	private JTextField m_roleTextField;
	private JTextField m_unitTextField;
	private JTextField m_calloutTextField;
	private JTextField m_estimatedArrivalTextField;
	private JTextField m_arrivedTextField;
	private JTextField m_releasedTextField;
	private JTextArea m_remarksTextArea;
	
	private boolean m_newPersonnel = false;

	public PersonnelDetailsPanel(IDiskoWpUnit wp)
	{
		m_wpUnit = wp;
		initialize();
	}
	
	private void initialize()
	{
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(4, 4, 4, 4);
		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		
		// Top
		gbc.gridwidth = 4;
		gbc.gridheight = 1;
		JPanel topPanel = new JPanel(new BorderLayout());
		m_topLabel = new JLabel();
		topPanel.add(m_topLabel, BorderLayout.CENTER);
		m_changeStatusButton = DiskoButtonFactory.createSmallButton("");
		m_changeStatusButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if(m_currentPersonnel != null)
				{
					String command = arg0.getActionCommand();
					PersonnelStatus newStatus = PersonnelStatus.valueOf(command);
					
					switch(newStatus)
					{
					case ON_ROUTE:
						IPersonnelIf newPersonnel = PersonnelUtilities.callOutPersonnel(m_currentPersonnel);
						// On route command might create new history instance of personnel
						if(newPersonnel != null)
						{
							m_currentPersonnel = newPersonnel;
						}
						break;
					case ARRIVED:
						 PersonnelUtilities.arrivedPersonnel(m_currentPersonnel);
						break;
					case RELEASED:
						PersonnelUtilities.releasePersonnel(m_currentPersonnel);
					}
					updateFieldContents();
				}
			}
		});
		topPanel.add(m_changeStatusButton, BorderLayout.EAST);
		this.add(topPanel, gbc);
		gbc.gridy++;
		
		// Name
		m_nameTextField = new JTextField();
		gbc.gridwidth = 3;
		layoutComponent(0, m_resources.getString("FullName.text"), m_nameTextField, gbc, 1);
		
		// Cell
		m_cellTextField = new JTextField();
		gbc.gridwidth = 3;
		layoutComponent(0, m_resources.getString("CellularPhone.text"), m_cellTextField, gbc, 1);
		
		// Property
		m_propertyComboBox = new JComboBox(PersonnelType.values());
		m_propertyComboBox.setRenderer(new SimpleListCellRenderer());
		gbc.gridwidth = 3;
		layoutComponent(0, m_resources.getString("Property.text"), m_propertyComboBox, gbc, 1);
		
		// Organization
		m_organizationTextField = new JTextField();
		gbc.gridwidth = 3;
		layoutComponent(0, m_resources.getString("Organization.text"), m_organizationTextField, gbc, 1);
		
		// Department
		m_departmentTextField = new JTextField();
		gbc.gridwidth = 3;
		layoutComponent(0, m_resources.getString("Department.text"), m_departmentTextField, gbc, 1);
		
		// Role
		gbc.gridy++;
		m_roleTextField = new JTextField();
		layoutComponent(0, m_resources.getString("Role.text"), m_roleTextField, gbc, 0);
		
		// Unit
		m_unitTextField = new JTextField();
		layoutComponent(2, m_resources.getString("Unit.text"), m_unitTextField, gbc, 1);
		
		// Alerted
		m_calloutTextField = new JTextField();
		layoutComponent(0, m_resources.getString("Alerted.text"), m_calloutTextField, gbc, 0);
		
		// Expected
		m_estimatedArrivalTextField = new JTextField();
		layoutComponent(2, m_resources.getString("ExpectedArrival.text"), m_estimatedArrivalTextField, gbc, 1);
		
		// Arrived
		m_arrivedTextField = new JTextField();
		layoutComponent(0, m_resources.getString("Arrived.text"), m_arrivedTextField, gbc, 0);
		
		// Dismissed
		m_releasedTextField = new JTextField();
		layoutComponent(2, m_resources.getString("Dismissed.text"), m_releasedTextField, gbc, 1);
		
		// Notes
		m_remarksTextArea = new JTextArea();
		m_remarksTextArea.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		m_remarksTextArea.setRows(8);
		m_remarksTextArea.setWrapStyleWord(true);
		m_remarksTextArea.setLineWrap(true);
		JScrollPane notesScrollPane = new JScrollPane(m_remarksTextArea);
		gbc.gridwidth = 3;
		gbc.weighty = 1.0;
		layoutComponent(0, m_resources.getString("Notes.text"), notesScrollPane, gbc, 5);
		
	}
	
	private void layoutComponent(int column, String label, JComponent component, GridBagConstraints gbc, int height)
	{
		gbc.weightx = 1.0;
		gbc.gridheight = Math.max(1, height);
		gbc.gridx = column + 1;
		this.add(component, gbc);
		
		gbc.weightx = 0.0;
		gbc.gridx = column;
		gbc.gridwidth = 1;
		this.add(new JLabel(label), gbc);
		
		gbc.gridy += height;
	}

	/**
	 * Updates personnel data in MSO. Creates new personnel if needed
	 */
	public void savePersonnel()
	{
		if(m_currentPersonnel == null && m_newPersonnel)
		{
			// Creating new personnel
			m_currentPersonnel = m_wpUnit.getMsoManager().createPersonnel();
			m_newPersonnel = false;
		}

		String[] name = m_nameTextField.getText().split(" ");
		StringBuilder firstName = new StringBuilder();
		for(int i=0; i<name.length-1; i++)
		{
			firstName.append(name[i] + " ");
		}
		
		if(firstName.toString() != null)
		{
			m_currentPersonnel.setFirstname(firstName.toString().trim());
		}
		m_currentPersonnel.setLastname(name[name.length-1].trim());

		String phone = m_cellTextField.getText();
		m_currentPersonnel.setTelephone1(phone);

		PersonnelType type= (PersonnelType)m_propertyComboBox.getSelectedItem();
		m_currentPersonnel.setType(type);

		String organization = m_organizationTextField.getText();
		m_currentPersonnel.setOrganization(organization);

		String department = m_departmentTextField.getText();
		m_currentPersonnel.setDepartment(department);

//		String role = m_roleTextField.getText();
//		m_currentPersonnel.set

//		m_unitTextField.getText();

		try
		{
			Calendar callout = DTG.DTGToCal(m_calloutTextField.getText());
			m_currentPersonnel.setCallOut(callout);
		}
		catch (IllegalMsoArgumentException e)
		{
		}

		try
		{
			Calendar estimatedArrival = DTG.DTGToCal(m_estimatedArrivalTextField.getText());
			m_currentPersonnel.setEstimatedArrival(estimatedArrival);
		} 
		catch (IllegalMsoArgumentException e)
		{
		}

		try
		{
			Calendar arrived = DTG.DTGToCal(m_arrivedTextField.getText());
			m_currentPersonnel.setArrived(arrived);
		} 
		catch (IllegalMsoArgumentException e)
		{
		}

		try
		{
			Calendar released = DTG.DTGToCal(m_releasedTextField.getText());
			m_currentPersonnel.setReleased(released);
		} 
		catch (IllegalMsoArgumentException e)
		{
		}

		String remarks = m_remarksTextArea.getText();
		m_currentPersonnel.setRemarks(remarks);
	}
	
	public void setNewPersonnel(boolean newPersonnel)
	{
		m_newPersonnel = newPersonnel;
	}
	
	/**
	 * Sets the current personnel, updates GUI contents
	 * @param personnel
	 */
	public void setPersonnel(IPersonnelIf personnel)
	{
		m_currentPersonnel = personnel;
		updateFieldContents();
	}
	
	private void updateFieldContents()
	{
		if(m_currentPersonnel == null)
		{
			m_nameTextField.setText("");
			m_cellTextField.setText("");
			m_propertyComboBox.setSelectedIndex(0);
			m_organizationTextField.setText("");
			m_departmentTextField.setText("");
			m_roleTextField.setText("");
			m_unitTextField.setText("");
			m_calloutTextField.setText("");
			m_estimatedArrivalTextField.setText("");
			m_arrivedTextField.setText("");
			m_releasedTextField.setText("");
			m_remarksTextArea.setText("");
			m_changeStatusButton.setText("");
		}
		else
		{
			m_topLabel.setText(m_currentPersonnel.getFirstname() + " " + m_currentPersonnel.getLastname() +
					" (" + m_currentPersonnel.getStatusText() + ")");
			m_nameTextField.setText(m_currentPersonnel.getFirstname() + " " + m_currentPersonnel.getLastname());
			m_cellTextField.setText(m_currentPersonnel.getTelephone1());
			m_propertyComboBox.setSelectedItem(m_currentPersonnel.getType());
			m_organizationTextField.setText(m_currentPersonnel.getOrganization());
			m_departmentTextField.setText(m_currentPersonnel.getDepartment());
//			m_roleTextField.setText(m_currentPersonnel.get);
//			m_unitTextField.setText(m_currentPersonnel.get);
			m_calloutTextField.setText(DTG.CalToDTG(m_currentPersonnel.getCallOut()));
			m_estimatedArrivalTextField.setText(DTG.CalToDTG(m_currentPersonnel.getEstimatedArrivalAttribute().getCalendar()));
			m_arrivedTextField.setText(DTG.CalToDTG(m_currentPersonnel.getArrived()));
			m_releasedTextField.setText(DTG.CalToDTG(m_currentPersonnel.getReleased()));
			m_remarksTextArea.setText(m_currentPersonnel.shortDescriptor());
			
			// Get next status for 
			PersonnelStatus status = m_currentPersonnel.getStatus();
			PersonnelStatus[] values = PersonnelStatus.values();
			status = values[(status.ordinal()+1)%values.length];
			if(status == PersonnelStatus.IDLE)
			{
				// Not possible to send personnel status back to idle, set to next
				status = values[(status.ordinal() + 1) % values.length];
			}
			m_changeStatusButton.setText(status.toString());
			m_changeStatusButton.setActionCommand(status.name());
		}
	}

	public IPersonnelIf getPersonnel()
	{
		return m_currentPersonnel;
	}
}
