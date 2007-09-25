package org.redcross.sar.wp.unit;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import org.redcross.sar.app.Utils;
import org.redcross.sar.gui.DiskoButtonFactory;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.data.IUnitIf.UnitType;
import org.redcross.sar.util.Internationalization;

/**
 * Dialog for choosing unit type
 * 
 * @author thomasl
 */
public class UnitTypeDialog extends DiskoDialog
{
	private static final long serialVersionUID = 1L;
	
	private JPanel m_contentsPanel;
	private JButton m_okButton;
	private JButton m_cancelButton;
	private JTable m_typeTable;
	
	private static UnitType m_type;
	
	private IDiskoWpUnit m_wpUnit;
	
	public UnitTypeDialog(IDiskoWpUnit wpUnit, JComponent parentComponent)
	{
		super(wpUnit.getApplication().getFrame());
		m_wpUnit = wpUnit;
		initialize(parentComponent);
	}
	
	private void initialize(JComponent parentComponent)
	{
		this.setLocationRelativeTo(parentComponent, DiskoDialog.POS_CENTER, false);
		m_contentsPanel = new JPanel();
		m_contentsPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		m_contentsPanel.setLayout(new BoxLayout(m_contentsPanel, BoxLayout.PAGE_AXIS));
		
		// Labels
		m_contentsPanel.add(new JLabel(m_wpUnit.getText("CreateNewUnit.text")));
		m_contentsPanel.add(Box.createRigidArea(new Dimension(10, 20)));
		m_contentsPanel.add(new JLabel(m_wpUnit.getText("ChooseUnitType.text")));
		
		// Table
		m_typeTable = new JTable(new UnitTypeTableModel());
		m_typeTable.getColumnModel().getColumn(0).setCellRenderer(new UnitTypeCellRenderer());
		m_typeTable.setRowHeight(DiskoButtonFactory.SMALL_BUTTON_SIZE.height);
		m_typeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		m_typeTable.setColumnSelectionAllowed(false);
		JScrollPane tableScroller = new JScrollPane(m_typeTable);
		m_contentsPanel.add(tableScroller);
		
		// Buttons
		JPanel actionButtonRow = new JPanel();
		
		m_cancelButton = DiskoButtonFactory.createSmallButton(DiskoButtonFactory.ButtonType.CancelButton);
		m_cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				fireDialogCanceled();
			}		
		});
		actionButtonRow.add(m_cancelButton);
		
		m_okButton = DiskoButtonFactory.createSmallButton(DiskoButtonFactory.ButtonType.OkButton);
		m_okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				int selectedType = m_typeTable.getSelectedRow();
				m_type = UnitType.values()[selectedType];
				fireDialogFinished();
			}	
		});
		actionButtonRow.add(m_okButton);
		
		m_contentsPanel.add(actionButtonRow);
		
		this.add(m_contentsPanel);
		this.pack();
	}
	
	public UnitType getUnitType()
	{
		return m_type;
	}
	
	/**
	 * Unit type table model
	 * 
	 * @author thomasl
	 */
	public class UnitTypeTableModel extends AbstractTableModel
	{
		private static final long serialVersionUID = 1L;

		public int getColumnCount()
		{
			return 1;
		}
		
	    @Override
	    public String getColumnName(int column)
	    {
	    	return null;
	    }

		public int getRowCount()
		{
			return UnitType.values().length;
		}

		public Object getValueAt(int rowIndex, int columnIndex)
		{
			UnitType type = UnitType.values()[rowIndex]; 
			return type;
		}
	}
	
	public class UnitTypeCellRenderer extends JLabel implements TableCellRenderer
	{
		private static final long serialVersionUID = 1L;
		
		private ResourceBundle m_resources = ResourceBundle.getBundle("org.redcross.sar.mso.data.properties.Unit");
		
		public UnitTypeCellRenderer()
		{
			this.setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column)
		{
			UnitType type = (UnitType)value;
			
			ImageIcon icon = Utils.getIcon(type);
			this.setIcon(icon);
			
			String text = Internationalization.getEnumText(m_resources, type);
			setText(text);
			
			if(isSelected)
			{
				this.setBackground(table.getSelectionBackground());
				this.setForeground(table.getSelectionForeground());
			}
			else
			{
				this.setBackground(table.getBackground());
				this.setForeground(table.getForeground());
			}
			return this;
		}
	}
}
