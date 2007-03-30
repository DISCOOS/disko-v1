package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

/**
 *
 */
public interface IUnitIf extends IHierarchicalUnitIf, ICommunicatorIf
{
    public void addAssignment(IAssignmentIf anAssignment) throws DuplicateIdException;

    public void removeAssignment(IAssignmentIf anAssignment);
}
