package org.redcross.sar.wp.logistics;
/*
 * AssignmentLabelTransferHandler.java is mainly fetched from the 1.4
 * DragPictureDemo.java example.
 */

import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.util.AssignmentTransferMessageCreator;
import org.redcross.sar.util.except.IllegalOperationException;

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
    DTAssignmentLabel m_targetLabel;
    Component m_targetComponent;

    boolean m_shouldRemove;
    DiskoWpLogisticsImpl m_wpModule;

    public AssignmentLabelTransferHandler(DataFlavor aFlavor, DiskoWpLogisticsImpl aWpModule)
    {
        m_labelFlavor = aFlavor;
        m_wpModule = aWpModule;
    }

    @Override
    public boolean importData(TransferSupport trs)
    {
        if (trs.isDrop())
        {
            DropLocation dropl = trs.getDropLocation();
            System.out.println("Drop location : " + dropl.getDropPoint());
        }

        if (canImport(trs))
        {

            IAssignmentIf transferredAssignment = getTransferredAssignment(trs);
            IUnitIf sourceUnit = transferredAssignment.getOwningUnit();
            IAssignmentIf.AssignmentStatus sourceStatus = transferredAssignment.getStatus();

            IUnitIf targetUnit = null;
            IAssignmentIf.AssignmentStatus targetStatus = IAssignmentIf.AssignmentStatus.EMPTY;

            if (m_targetComponent instanceof AssignmentScrollPanel)
            {
                AssignmentScrollPanel panel = (AssignmentScrollPanel) m_targetComponent;
                targetUnit = panel.getSelectedUnit();
                targetStatus = panel.getSelectedStatus();
            }

            if (m_wpModule.confirmTransfer(transferredAssignment, targetStatus, targetUnit))
            {
                boolean transferOk = false;
                if (m_targetLabel != null && targetStatus == IAssignmentIf.AssignmentStatus.ALLOCATED)
                {
                    transferOk = targetUnit.addAllocatedAssignment(transferredAssignment, m_targetLabel.getAssignment());
                } else
                {
                    try
                    {
                        transferredAssignment.setStatusAndOwner(targetStatus, targetUnit);
                        transferOk = true;
                    }
                    catch (IllegalOperationException e)
                    {
                    }
                }
                if (transferOk)
                {
                    AssignmentTransferMessageCreator.createMessage(m_wpModule, targetUnit, transferredAssignment);
                    m_wpModule.getMsoModel().commit();
                    return true;
                } else
                {
                    m_wpModule.showTransferWarning();
                }
            }

        }
        return false;
    }

    @Override
    protected Transferable createTransferable(JComponent c)
    {
        m_sourceLabel = (DTAssignmentLabel) c;
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
    }

    @Override
    public boolean canImport(TransferSupport trs)
    {
        m_targetComponent = trs.getComponent();
        m_targetLabel = null;
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
                m_targetLabel = label;
                m_targetComponent = parent;
            }
        }

        if (m_targetComponent instanceof AssignmentScrollPanel)
        {
            AssignmentScrollPanel panel = (AssignmentScrollPanel) m_targetComponent;
            return transferredAssignment.canChangeToStatus(panel.getSelectedStatus(), panel.getSelectedUnit());
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
            if (m_assignment == null) // todo remove when verified OK
            {
                System.out.println("Assignement is null");
            }
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
