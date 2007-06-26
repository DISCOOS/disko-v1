package org.redcross.sar.wp.logistics;
/*
 * AssignmentTransferHandler.java is mainly fetched from the 1.4
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

/**
 * Transfer handler for Drag and Drop transfers of assignments in Logistics WP
 */
public class AssignmentTransferHandler extends TransferHandler
{
    private static DataFlavor m_assignmentFlavor;
    private IAssignmentIf m_sourceAssignment;

    IAssignmentIf m_targetAssignment;
    Component m_targetComponent;

    boolean m_shouldRemove;
    DiskoWpLogisticsImpl m_wpModule;

    public AssignmentTransferHandler(DiskoWpLogisticsImpl aWpModule) throws ClassNotFoundException
    {
        m_wpModule = aWpModule;
        if (m_assignmentFlavor == null)
        {
            m_assignmentFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=org.redcross.sar.mso.data.IAssignmentIf");
        }
    }

    @Override
    public boolean importData(TransferSupport trs)
    {
        if (canImport(trs))
        {
            IAssignmentIf transferredAssignment = getTransferredAssignment(trs);
            if (transferredAssignment == null)
            {
                return false;
            }

            IUnitIf targetUnit = null;
            IAssignmentIf.AssignmentStatus targetStatus = IAssignmentIf.AssignmentStatus.EMPTY;

            if (m_targetComponent instanceof AssignmentScrollPanel)
            {  // drag into an AssignmentScrollPanel
                AssignmentScrollPanel panel = (AssignmentScrollPanel) m_targetComponent;
                targetUnit = panel.getSelectedUnit();
                targetStatus = panel.getSelectedStatus();
            } else
            if (m_targetComponent instanceof JTable && ((JTable) m_targetComponent).getModel() instanceof UnitTableModel)
            { // drag into a unit table
                JTable targetTable = (JTable) m_targetComponent;
                if (trs.isDrop())
                {
                    JTable.DropLocation dr = (JTable.DropLocation) trs.getDropLocation();
                    int dropRow = targetTable.convertRowIndexToModel(dr.getRow());
                    int dropColumn = targetTable.convertColumnIndexToModel(dr.getColumn());
                    UnitTableModel tm = (UnitTableModel) targetTable.getModel();
                    targetUnit = tm.getUnitAt(dropRow);
                    targetStatus = UnitTableModel.getSelectedAssignmentStatus(dropColumn - 1);
                }
            }

            // Ask for confirmation and perform transfer
            if (m_wpModule.confirmTransfer(transferredAssignment, targetStatus, targetUnit))
            {
                boolean transferOk = false;
                if (m_targetAssignment != null && targetStatus == IAssignmentIf.AssignmentStatus.ALLOCATED)
                {
                    transferOk = targetUnit.addAllocatedAssignment(transferredAssignment, m_targetAssignment);
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
        m_shouldRemove = true;
        if (c instanceof AssignmentLabel)
        {
            m_sourceAssignment = ((AssignmentLabel) c).getAssignment();
            return new AssignmentTransferable(m_sourceAssignment);
        }
        return null;
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
        m_targetAssignment = null;
        DataFlavor[] flavors = trs.getDataFlavors();
        boolean flavorOk = false;

        for (DataFlavor flavor : flavors)
        {
            if (m_assignmentFlavor.equals(flavor))
            {
                flavorOk = true;
                break;
            }
        }

        if (!flavorOk)
        {
            return false;
        }

        IAssignmentIf transferredAssignment = getTransferredAssignment(trs);
        if (transferredAssignment == null)
        {
            return false;
        }

        if (m_targetComponent instanceof DTAssignmentLabel)
        {
            DTAssignmentLabel label = (DTAssignmentLabel) m_targetComponent;
            if (label.getAssignment() == m_sourceAssignment)
            {
                return false;
            }
            m_targetAssignment = label.getAssignment();
            Component parent = label.getParent();
            if (parent instanceof AssignmentScrollPanel)
            {
                m_targetComponent = parent;
            }
        }

        if (m_targetComponent instanceof AssignmentScrollPanel)
        {
            AssignmentScrollPanel panel = (AssignmentScrollPanel) m_targetComponent;
            return transferredAssignment.canChangeToStatus(panel.getSelectedStatus(), panel.getSelectedUnit());
        } else if (m_targetComponent instanceof JTable)
        {
            JTable targetTable = (JTable) m_targetComponent;
            if (trs.isDrop())
            {
                JTable.DropLocation dr = (JTable.DropLocation) trs.getDropLocation();
                int dropRow = targetTable.convertRowIndexToModel(dr.getRow());
                int dropColumn = targetTable.convertColumnIndexToModel(dr.getColumn());
                UnitTableModel tm = (UnitTableModel) targetTable.getModel();
                return tm.canAcceptAssignment(transferredAssignment, dropRow, dropColumn);
            }
        }

        return false;
    }

    private IAssignmentIf getTransferredAssignment(TransferSupport trs)
    {
        Object transferObject;
        try
        {
            transferObject = trs.getTransferable().getTransferData(m_assignmentFlavor);
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

        AssignmentTransferable(IAssignmentIf anAssignment)
        {
            m_assignment = anAssignment;
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
            return new DataFlavor[]{m_assignmentFlavor};
        }

        public boolean isDataFlavorSupported(DataFlavor flavor)
        {
            return m_assignmentFlavor.equals(flavor);
        }
    }
}
