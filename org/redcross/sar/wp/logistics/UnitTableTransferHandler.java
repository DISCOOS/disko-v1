package org.redcross.sar.wp.logistics;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
/**
 * Created by IntelliJ IDEA.
 * User: vinjar
 * Date: 22.jun.2007
 * To change this template use File | Settings | File Templates.
 */

/**
 *
 */
public class UnitTableTransferHandler extends TransferHandler
{


    public UnitTableTransferHandler()
    {
        System.out.println("UnitTableTransferHandler created");
    }

    @Override
    public boolean canImport(TransferSupport support)
    {
        if (support.isDrop())
        {
            JTable.DropLocation dr = (JTable.DropLocation)support.getDropLocation();
            System.out.println("UnitTableTransferHandler can import " + dr.getColumn() + " " + dr.getRow());
        }
        return false;
    }


    @Override
    protected Transferable createTransferable(JComponent c)
    {
        System.out.println("UnitTableTransferHandler createTr" + c.toString());
        return new StringTransferable("xxx");
    }


    @Override
    public boolean importData(TransferSupport support)
    {
        System.out.println("UnitTableTransferHandler importData" + support.toString());
        return false;
    }

    class StringTransferable implements Transferable
    {
        private String m_assignment;

        StringTransferable(String label)
        {
            m_assignment = label;
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
        {
            if (!isDataFlavorSupported(flavor))
            {
                throw new UnsupportedFlavorException(flavor);
            }
            return m_assignment;
        }

        public DataFlavor[] getTransferDataFlavors()
        {
            return new DataFlavor[]{DataFlavor.stringFlavor};
        }

        public boolean isDataFlavorSupported(DataFlavor flavor)
        {
            return DataFlavor.stringFlavor.equals(flavor);
        }
    }


}
