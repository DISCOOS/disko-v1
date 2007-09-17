package org.redcross.sar.wp.unit;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.TransferHandler;

import org.redcross.sar.mso.data.IPersonnelIf;

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
	 
	 @Override
	 public boolean canImport(TransferSupport support)
	 {
		 // TODO implement
		 return false;
	 }
	 
	 @Override
	 public boolean importData(TransferSupport support) 
	 {
		 // TODO implement
		return false; 
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
