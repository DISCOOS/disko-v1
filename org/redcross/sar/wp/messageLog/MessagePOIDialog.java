package org.redcross.sar.wp.messageLog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IPOIIf.POIType;

/**
 * Dialog used in position and finding when editing the message log. A separate POI dialog class was created for 
 * the message log, should be merged with POIDialog / extract a common super class, if this should prove beneficial
 * 
 * @author thomasl
 *
 */
public class MessagePOIDialog extends DiskoDialog implements IEditMessageDialogIf
{
	protected JPanel m_contentsPanel = null;
	protected JButton m_okButton = null;
	protected JButton m_cancelButton = null;
	protected JButton m_showInMapButton = null;
	protected JLabel m_xLabel = null;
	protected JLabel m_yLabel = null;
	protected JTextField m_xField = null;
	protected JTextField m_yField = null;
	protected JComboBox m_poiTypesComboBox = null;
	protected POIType[] m_poiTypes = null;
	
	public MessagePOIDialog(IDiskoWpMessageLog wp)
	{
		super(wp.getApplication().getFrame());
		
		initialize();
	}
	
	private void initialize()
	{
		initContents();
		initButtons();
	}

	private void initButtons()
	{
		m_okButton = DiskoButtonFactory.createSmallOKButton();
		m_okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				fireDialogFinished();
			}
		});
		
		m_cancelButton = DiskoButtonFactory.createSmallCancelButton();
		m_cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				fireDialogCanceled();
			}
		});
		
		m_showInMapButton = DiskoButtonFactory.createSmallButton("Vis i kart");
		m_showInMapButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// TODO display map dialog
			}
		});
	}
	
	/**
	 * 
	 * @param types - POI types, set to null in order to hide combo box
	 */
	public void setPOITypes(POIType[] types)
	{
		if(types == null)
		{
			m_poiTypesComboBox.removeAllItems();
			m_poiTypesComboBox.setVisible(false);
		}
		else
		{
			for(POIType type : types)
			{
				m_poiTypesComboBox.addItem(type);
			}
			m_poiTypesComboBox.setSelectedItem(types[0]);
			m_poiTypesComboBox.setVisible(true);
		}
		m_poiTypes = types;
	}

	private void initContents()
	{
		m_contentsPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		
		// X
		//m_xLabel = new J
		
		// Combo box
		m_poiTypesComboBox = new JComboBox();
		this.setPOITypes(null);
		m_contentsPanel.add(m_poiTypesComboBox, gbc);
		
		this.add(m_contentsPanel);
		this.pack();
	}

	public void clearContents()
	{
		// TODO Auto-generated method stub
		
	}

	public void hideDialog()
	{
		// TODO Auto-generated method stub
		
	}

	public void newMessageSelected(IMessageIf message)
	{
		// TODO Auto-generated method stub
		
	}

	public void showDialog()
	{
		// TODO Auto-generated method stub
		
	}

}
