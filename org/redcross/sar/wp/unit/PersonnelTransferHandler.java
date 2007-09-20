package org.redcross.sar.wp.unit;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.table.TableModel;

import org.redcross.sar.mso.data.IPersonnelIf;
import org.redcross.sar.mso.data.IPersonnelListIf;
import org.redcross.sar.wp.unit.UnitDetailsPanel.UnitPersonnelTableModel;

/**
 * Implements drag and drop for unit/personnel assignment
 * 
 * @author thomasl
 */
public class PersonnelTransferHandler extends TransferHandler
{
	private static final long serialVersionUID = 1L;

	private static DataFlavor m_personnelFlavor;

	public PersonnelTransferHandler() throws ClassNotFoundException
	{
		if (m_personnelFlavor == null)
		{
			m_personnelFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=org.redcross.sar.mso.data.IPersonnelIf");
		}
	}

	/**
	 * Exports personnel from {@link PersonnelOverviewTableModel} or {@link UnitPersonnelTableModel}, depending
	 * on which way the user drags.
	 */
	@Override
	protected Transferable createTransferable(JComponent c)
	{
		JTable table = (JTable)c;
		int selectedRow = table.getSelectedRow();
		TableModel model = table.getModel();
		IPersonnelIf personnel = null;
		if(model instanceof PersonnelOverviewTableModel)
		{
			// Exporting from personnel overview table
			PersonnelOverviewTableModel overviewModel = (PersonnelOverviewTableModel)model;
			personnel = overviewModel.getPersonnel(selectedRow);
		}
		else if(model instanceof UnitPersonnelTableModel)
		{
			// Exporting from unit personnel table
			UnitPersonnelTableModel unitModel = (UnitPersonnelTableModel)model;
			personnel = unitModel.getPersonnel(selectedRow);
		}
		
		PersonnelTransferable transferable = new PersonnelTransferable(personnel);
		return transferable;
	}

	@Override
	public boolean canImport(TransferSupport support)
	{
		DataFlavor[] flavors = support.getDataFlavors();
        boolean flavorOk = false;

        for (DataFlavor flavor : flavors)
        {
            if (m_personnelFlavor.equals(flavor))
            {
                flavorOk = true;
                break;
            }
        }
        if (!flavorOk)
        {
            return false;
        }
        
        // Always allowed to remove personnel from a unit (?)
        JTable targetTable = (JTable)support.getComponent();
        if(targetTable.getModel() instanceof PersonnelOverviewTableModel)
        {
        	return true;
        }
        
		// Check for valid personnel transfer. Only applicable when transferring to a unit
        try
		{
			IPersonnelIf personnel = (IPersonnelIf)support.getTransferable().getTransferData(m_personnelFlavor);
			return PersonnelUtilities.canAssignPersonnelToUnit(personnel);
		} 
        catch (UnsupportedFlavorException e)
		{
			e.printStackTrace();
		} 
        catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int getSourceActions(JComponent c) 
	{
		return MOVE;
	}

	/**
	 * Imports personnel from personnel overview table to unit personnel table
	 */
	@Override
	public boolean importData(TransferSupport support) 
	{
		if(canImport(support))
		{
			Transferable transferable = support.getTransferable();
			JTable table = (JTable)support.getComponent();
			TableModel model = table.getModel();
			
			if(model instanceof UnitPersonnelTableModel)
			{
				// Unit personnel table is transfer target
				UnitPersonnelTableModel unitModel = (UnitPersonnelTableModel)model;
				// Add personnel to unit personnel list
				try
				{
					IPersonnelIf personnel = (IPersonnelIf)transferable.getTransferData(m_personnelFlavor);
					IPersonnelListIf personnelList = unitModel.getPersonnel();
					if(personnelList.contains(personnel))
					{
						// Dont't import if already in list
						return false;
					}
					else
					{
						personnelList.add(personnel);
					}
					unitModel.fireTableDataChanged();
				} 
				catch (UnsupportedFlavorException e)
				{
					e.printStackTrace();
					return false;
				} 
				catch (IOException e)
				{
					e.printStackTrace();
					return false;
				}
			}
			
			return true;
		}
		return false; 
	}
	
	/**
	 * Removes personnel from unit if source of a completed drag was the unit personnel table
	 */
	@Override
	protected void exportDone(JComponent c, Transferable data, int action) 
	{
		if(action == TransferHandler.NONE)
		{
			return;
		}
		
		if(c instanceof JTable)
		{
			JTable table = (JTable)c;
			TableModel model = table.getModel();
			if(model instanceof UnitPersonnelTableModel)
			{
				// Drag source was unit personnel table
				UnitPersonnelTableModel unitModel = (UnitPersonnelTableModel)model;
				try
				{
					IPersonnelIf personnel = (IPersonnelIf)data.getTransferData(m_personnelFlavor);
					unitModel.getPersonnel().removeReference(personnel);
					unitModel.fireTableDataChanged();
				} 
				catch (UnsupportedFlavorException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		// TODO Commit changes right away
    }

	/**
	 * Defines a transferable personnel class
	 * 
	 * @author thomasl
	 */
	class PersonnelTransferable implements Transferable
	{
		private IPersonnelIf m_personnel;

		public PersonnelTransferable(IPersonnelIf personnel)
		{
			m_personnel = personnel;
		}

		public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException
		{
			if (!isDataFlavorSupported(flavor))
			{
				throw new UnsupportedFlavorException(flavor);
			}

			return m_personnel;
		}

		public DataFlavor[] getTransferDataFlavors()
		{
			return new DataFlavor[]{m_personnelFlavor};
		}

		public boolean isDataFlavorSupported(DataFlavor flavor)
		{
			return m_personnelFlavor.equals(flavor);
		}

	}
}
