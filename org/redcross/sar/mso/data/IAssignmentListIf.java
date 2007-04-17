package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

/**
 *
 */
public interface IAssignmentListIf extends IMsoListIf<IAssignmentIf>
{
    public IAssignmentIf createAssignment();

    public IAssignmentIf createAssignment(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber) throws DuplicateIdException;

    public ISearchIf createSearch();

    public ISearchIf createSearch(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber) throws DuplicateIdException;

    public IAssistanceIf createAssistance();

    public IAssistanceIf createAssistance(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber) throws DuplicateIdException;
}
