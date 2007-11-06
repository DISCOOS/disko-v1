package org.redcross.sar.wp.unit;

import org.redcross.sar.app.Utils;
import org.redcross.sar.gui.DiskoButtonFactory;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.data.IUnitIf.UnitType;
import org.redcross.sar.util.Internationalization;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumSet;

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
	private EnumSet<UnitType> m_listValues;
	private JList m_typeList;

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
		this.setPreferredSize(new Dimension(400, 500));
		m_contentsPanel = new JPanel();
		m_contentsPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		m_contentsPanel.setLayout(new BoxLayout(m_contentsPanel, BoxLayout.PAGE_AXIS));

		// Labels
		m_contentsPanel.add(new JLabel(m_wpUnit.getText("CreateNewUnit.text")));
		m_contentsPanel.add(Box.createRigidArea(new Dimension(10, 20)));
		m_contentsPanel.add(new JLabel(m_wpUnit.getText("ChooseUnitType.text")));

		// List
		m_typeList = new JList();
		m_listValues = EnumSet.of(
				UnitType.AIRCRAFT,
				UnitType.BOAT,
				UnitType.DOG,
				UnitType.TEAM,
				UnitType.VEHICLE);
		m_typeList.setListData(m_listValues.toArray());
		m_typeList.setCellRenderer(new UnitTypeCellRenderer());
		m_typeList.setFixedCellHeight(DiskoButtonFactory.SMALL_BUTTON_SIZE.height);
		m_typeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane tableScroller = new JScrollPane(m_typeList);
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
				int index = m_typeList.getSelectedIndex();
				m_type = (UnitType)m_listValues.toArray()[index];
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

	public class UnitTypeCellRenderer extends JLabel implements ListCellRenderer
	{
		private static final long serialVersionUID = 1L;

		public UnitTypeCellRenderer()
		{
			this.setOpaque(true);
		}

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean hasFocus)
		{
			UnitType type = (UnitType)value;

			ImageIcon icon = Utils.getIcon(type);
			this.setIcon(icon);

			String text = Internationalization.translate(type);
			setText(text);

			if(isSelected)
			{
				this.setBackground(list.getSelectionBackground());
				this.setForeground(list.getSelectionForeground());
			}
			else
			{
				this.setBackground(list.getBackground());
				this.setForeground(list.getForeground());
			}
			return this;
		}
	}
}
