package org.redcross.sar.wp.unit;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ResourceBundle;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.redcross.sar.gui.renderers.SimpleListCellRenderer;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IPersonnelIf;
import org.redcross.sar.mso.data.IPersonnelIf.PersonnelStatus;
import org.redcross.sar.mso.data.IPersonnelIf.PersonnelType;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.Update;
import org.redcross.sar.util.mso.DTG;

/**
 * Panel displaying personnel details at bottom of main panel
 * 
 * @author thomasl
 *
 */
public class PersonnelDetailsBottomPanel extends JPanel implements IMsoUpdateListenerIf
{
	private static final long serialVersionUID = 1L;
	
	private static final ResourceBundle m_resources = ResourceBundle.getBundle("org.redcross.sar.wp.unit.unit");
	
	private JTextField m_nameTextField;
	private JTextField m_calloutTextField;
	private JTextField m_cellularTextField;
	private JComboBox m_propertyComboBox;
	private JTextField m_estimatedArrivalTextField;
	private JTextField m_remarksTextField;
	private JTextField m_arrivedTextField;
	private JTextField m_organizationTextField;
	private JTextField m_departmentTextField;
	private JTextField m_releasedTextField;
	
	private IPersonnelIf m_currentPersonnel;

	public PersonnelDetailsBottomPanel(IDiskoWpUnit wp)
	{
		wp.getMmsoEventManager().addClientUpdateListener(this);
		initialize();
	}
	
	private void initialize()
	{
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(4, 4, 4, 4);
		gbc.gridy = 0;
		
		m_nameTextField = new JTextField();
		gbc.gridwidth = 3;
		layoutComponent(0, m_resources.getString("FullName.text"), m_nameTextField, gbc, 0);
		
		m_calloutTextField = new JTextField();
		layoutComponent(4, m_resources.getString("CallOut.text"), m_calloutTextField, gbc, 1);
		
		m_cellularTextField = new JTextField();
		layoutComponent(0, m_resources.getString("CellularPhone.text"), m_cellularTextField, gbc, 0);
		
		ResourceBundle personnelResources = ResourceBundle.getBundle("org.redcross.sar.mso.data.properties.Personnel");
		m_propertyComboBox = new JComboBox(PersonnelType.values());
		m_propertyComboBox.setRenderer(new SimpleListCellRenderer(personnelResources));
		layoutComponent(2, m_resources.getString("Property.text"), m_propertyComboBox, gbc, 0);
		
		m_estimatedArrivalTextField = new JTextField();
		layoutComponent(4, m_resources.getString("ExpectedArrival.text"), m_estimatedArrivalTextField, gbc, 1);
		
		m_remarksTextField = new JTextField();
		gbc.gridwidth = 3;
		layoutComponent(0, m_resources.getString("Notes.text"), m_remarksTextField, gbc, 0);
		
		m_arrivedTextField = new JTextField();
		layoutComponent(4, m_resources.getString("Arrived.text"), m_arrivedTextField, gbc, 1);
		
		m_organizationTextField = new JTextField();
		layoutComponent(0, m_resources.getString("Organization.text"), m_organizationTextField, gbc, 0);
		
		m_departmentTextField = new JTextField();
		layoutComponent(2, m_resources.getString("Department.text"), m_departmentTextField, gbc, 0);
		
		m_releasedTextField = new JTextField();
		layoutComponent(4, m_resources.getString("Released.text"), m_releasedTextField, gbc, 1);
		
		JScrollPane mainPanel = new JScrollPane();
		mainPanel.setViewportView(this);
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
	 * Set the current personnel
	 * @param personnel
	 */
	public void setPersonnel(IPersonnelIf personnel)
	{
		m_currentPersonnel = personnel;
	}
	
	/**
	 * Set field contents to that of the current personnel
	 */
	public void updateFieldContents()
	{
		if(m_currentPersonnel == null)
		{
			// Clear contents
			m_nameTextField.setText("");
			m_calloutTextField.setText("");
			m_cellularTextField.setText("");
			m_propertyComboBox.setSelectedItem(null);
			m_estimatedArrivalTextField.setText("");
			m_remarksTextField.setText("");
			m_arrivedTextField.setText("");
			m_organizationTextField.setText("");
			m_departmentTextField.setText("");
			m_releasedTextField.setText("");
		}
		else
		{
			String name = m_currentPersonnel.getFirstname() + " " + m_currentPersonnel.getLastname();
			m_nameTextField.setText(name);
			
			String callout = m_currentPersonnel.getCallOut() == null ? "" 
					: DTG.CalToDTG(m_currentPersonnel.getCallOut());
			m_calloutTextField.setText(callout);
			
			m_cellularTextField.setText(m_currentPersonnel.getTelephone1());
			
			m_propertyComboBox.setSelectedItem(m_currentPersonnel.getType());
			
			String estimatedArrival = m_currentPersonnel.getEstimatedArrival() == null ? "" 
					: DTG.CalToDTG(m_currentPersonnel.getEstimatedArrival());
			m_estimatedArrivalTextField.setText(estimatedArrival);
			
			m_remarksTextField.setText(m_currentPersonnel.getRemarks());
			
			String arrived = m_currentPersonnel.getArrived() == null ? ""
					: DTG.CalToDTG(m_currentPersonnel.getArrived());
			m_arrivedTextField.setText(arrived);
			
			m_organizationTextField.setText(m_currentPersonnel.getOrganization());
			
			m_departmentTextField.setText(m_currentPersonnel.getDepartment());
			
			if(m_currentPersonnel.getStatus() == PersonnelStatus.RELEASED)
			{
				m_releasedTextField.setText(m_resources.getString("Yes.text"));
			}
			else
			{
				m_releasedTextField.setText(m_resources.getString("No.text"));
			}
		}
	}

	/**
	 * Update field contents if mso object changes
	 */
	public void handleMsoUpdateEvent(Update e)
	{
		IPersonnelIf personnel = (IPersonnelIf)e.getSource();
		if(personnel == m_currentPersonnel)
		{
			updateFieldContents();
		}
	}

	public boolean hasInterestIn(IMsoObjectIf msoObject)
	{
		return msoObject.getMsoClassCode() == IMsoManagerIf.MsoClassCode.CLASSCODE_PERSONNEL;
	}
}
