package org.redcross.sar.wp.logistikk;
/*
 * AssignmentLabelTransferHandler.java is mainly fetched from the 1.4
 * DragPictureDemo.java example.
 */

import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.util.except.IllegalOperationException;
import org.redcross.sar.wp.AbstractDiskoWpModule;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class AssignmentLabelTransferHandler extends TransferHandler
{
    DataFlavor m_labelFlavor;
    DTAssignmentLabel m_sourceLabel;
    Component m_targetComponent;

    boolean m_shouldRemove;
    AbstractDiskoWpModule m_wpModule;

    public AssignmentLabelTransferHandler(DataFlavor aFlavor, AbstractDiskoWpModule aWpModule)
    {
        m_labelFlavor = aFlavor;
        m_wpModule = aWpModule;
    }

    @Override
    public boolean importData(TransferSupport trs)
    {
        Transferable tr = trs.getTransferable();

        if (trs.isDrop())
        {
            DropLocation dropl = trs.getDropLocation();
            System.out.println("Drop location : " + dropl.getDropPoint());
        }

        if (canImport(trs))
        {
            if (m_targetComponent instanceof AssignmentScrollPanel)
            {
                AssignmentScrollPanel panel = (AssignmentScrollPanel) m_targetComponent;
                IAssignmentIf transferredAssignment = getTransferredAssignment(trs);
                try
                {
                    transferredAssignment.setStatusAndOwner(panel.getSelectedStatus(), null);
                    m_wpModule.getMsoModel().commit();
                    return true;
                }
                catch (IllegalOperationException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    protected Transferable createTransferable(JComponent c)
    {
        m_sourceLabel = (DTAssignmentLabel) c;
//        m_sourceIndex = m_sourceLabel.m_list.indexOf(m_sourceLabel.m_lp);
        m_shouldRemove = true;
        return new AssignmentTransferable(m_sourceLabel);
    }

    @Override
    public int getSourceActions(JComponent c)
    {
        return COPY_OR_MOVE;
    }

    @Override
    protected void exportDone(JComponent c, Transferable data, int action)
    {
//        if (m_shouldRemove && (action == MOVE))
//        {
//            if (m_sourceLabel.m_list == m_targetLabel.m_list && m_targetIndex < m_sourceIndex)
//            {
//                m_sourceIndex++;
//            }
//            m_sourceLabel.m_list.remove(m_sourceIndex);
//        }
//        m_sourceLabel = null;
// todo something else                m_wpModule.m_dataModified = true;
    }

    @Override
    public boolean canImport(TransferSupport trs)
    {
        m_targetComponent = trs.getComponent();
        DataFlavor[] flavors = trs.getDataFlavors();
        boolean flavorOk = false;

        for (DataFlavor flavor : flavors)
        {
            if (m_labelFlavor.equals(flavor))
            {
                flavorOk = true;
                break;
            }
        }

        if (!flavorOk)
        {
            return false;
        }

        if (trs.isDrop() && trs.getDropLocation() != null)
        {
            System.out.println("Drop location: " + trs.getDropLocation().getDropPoint());
        }

        IAssignmentIf transferredAssignment = getTransferredAssignment(trs);
        if (transferredAssignment == null)
        {
            return false;
        }

        if (m_targetComponent instanceof DTAssignmentLabel)
        {
            DTAssignmentLabel label = (DTAssignmentLabel) m_targetComponent;
            if (label == m_sourceLabel)
            {
                return false;
            }
            boolean ok;
            Component parent = label.getParent();
            if (parent instanceof AssignmentScrollPanel)
            {
                m_targetComponent = parent;
            }
        }

        if (m_targetComponent instanceof AssignmentScrollPanel)
        {
            AssignmentScrollPanel panel = (AssignmentScrollPanel) m_targetComponent;
            IAssignmentIf.AssignmentStatus panelStatus = panel.getSelectedStatus();
            return transferredAssignment.canChangeToStatus(panelStatus, null);
        }
        return false;
    }

    private IAssignmentIf getTransferredAssignment(TransferSupport trs)
    {
        Object transferObject;
        try
        {
            transferObject = trs.getTransferable().getTransferData(m_labelFlavor);
            if (transferObject instanceof IAssignmentIf)
            {
                return (IAssignmentIf) transferObject;
            }
        }
        catch (UnsupportedFlavorException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    class AssignmentTransferable implements Transferable
    {
        private IAssignmentIf m_assignment;

        AssignmentTransferable(DTAssignmentLabel label)
        {
            m_assignment = label.getAssignment();
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
            return new DataFlavor[]{m_labelFlavor};
        }

        public boolean isDataFlavorSupported(DataFlavor flavor)
        {
            return m_labelFlavor.equals(flavor);
        }
    }
}
