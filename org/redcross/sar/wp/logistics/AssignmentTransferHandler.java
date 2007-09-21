package org.redcross.sar.wp.logistics;
/*
 * AssignmentTransferHandler.java is mainly fetched from the 1.4
 * DragPictureDemo.java example.
 */

import org.redcross.sar.gui.renderers.IconRenderer;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.util.AssignmentTransferUtilities;
import org.redcross.sar.util.except.IllegalOperationException;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
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
    IDiskoWpLogistics m_wpModule;

    AssignmentLabel m_tmpLabel;

    public AssignmentTransferHandler(IDiskoWpLogistics aWpModule) throws ClassNotFoundException
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
            int tableDropRow = -1;
            UnitTableModel unitTableModel = null;
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
                    tableDropRow = targetTable.convertRowIndexToModel(dr.getRow());
                    int dropColumn = targetTable.convertColumnIndexToModel(dr.getColumn());
                    unitTableModel = (UnitTableModel) targetTable.getModel();
                    targetUnit = unitTableModel.getUnitAt(tableDropRow);
                    targetStatus = UnitTableModel.getSelectedAssignmentStatus(dropColumn - 1);
                }
            }

            // Ask for confirmation and perform transfer
            if (m_wpModule.confirmTransfer(transferredAssignment, targetStatus, targetUnit))
            {
                IAssignmentIf.AssignmentStatus oldAssignemtStatus = transferredAssignment.getStatus();
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
                    AssignmentTransferUtilities.createAssignmentChangeMessage(m_wpModule.getMsoManager(), targetUnit, transferredAssignment, oldAssignemtStatus);
                    m_wpModule.getMsoModel().commit();
                    if (unitTableModel != null)
                    {
                        unitTableModel.scrollToTableCellPosition(tableDropRow);
                    }
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
            return AssignmentTransferUtilities.assignmentCanChangeToStatus(transferredAssignment, panel.getSelectedStatus(), panel.getSelectedUnit());
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


    @Override
    /**
     * Overridden version that handles drag from a UnitTable.
     * Generate an {@link AssignmentLabel} for the table cell, that is used for exporting the drag.
     */
    public void exportAsDrag(JComponent comp, InputEvent e, int action)
    {
        if (comp instanceof JTable && e instanceof MouseEvent)
        {
            MouseEvent me = (MouseEvent) e;
            JTable table = (JTable) comp;
            TableColumnModel columnModel = table.getColumnModel();
            int column = columnModel.getColumnIndexAtX(me.getX());
            int row = me.getY() / table.getRowHeight(0);
            Object value;

            if (row >= table.getRowCount() || row < 0 ||
                    column >= table.getColumnCount() || column < 0)
            {
                return;
            }

            value = table.getValueAt(row, column);
            if (!(value instanceof IconRenderer.AssignmentIcon))
            {
                return;
            }

            IconRenderer.AssignmentIcon icon = (IconRenderer.AssignmentIcon) value;
            IAssignmentIf asg = icon.getAssignment();
            if (asg == null)
            {
                return;
            }
            if (m_tmpLabel == null)
            {
                m_tmpLabel = new AssignmentLabel(icon.getAssignment(), null);
                m_tmpLabel.setTransferHandler(this);
            } else
            {
                m_tmpLabel.setAssignment(icon.getAssignment());
            }
            super.exportAsDrag(m_tmpLabel, e, action);
        } else
        {
            super.exportAsDrag(comp, e, action);
        }
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
