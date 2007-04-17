package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

public class AssignmentListImpl extends MsoListImpl<IAssignmentIf> implements IAssignmentListIf
{

    public AssignmentListImpl(IMsoObjectIf anOwner, String theName, boolean isMain)
    {
        super(anOwner, theName, isMain);
    }

    public AssignmentListImpl(IMsoObjectIf anOwner, String theName, boolean isMain, int aSize)
    {
        super(anOwner, theName, isMain, aSize);
    }

    public IAssignmentIf createAssignment()
    {
        checkCreateOp();
        return createdUniqueItem(new AssignmentImpl(makeUniqueId(), makeSerialNumber()));
    }

    public IAssignmentIf createAssignment(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber) throws DuplicateIdException
    {
        checkCreateOp();
        return createdItem(new AssignmentImpl(anObjectId, aNumber));
    }

    public ISearchIf createSearch()
    {
        checkCreateOp();
        return (ISearchIf) createdUniqueItem(new SearchImpl(makeUniqueId(), makeSerialNumber()));
    }

    public ISearchIf createSearch(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber) throws DuplicateIdException
    {
        checkCreateOp();
        return (ISearchIf) createdItem(new SearchImpl(anObjectId, aNumber));
    }

    public IAssistanceIf createAssistance()
    {
        checkCreateOp();
        return (IAssistanceIf) createdUniqueItem(new AssistanceImpl(makeUniqueId(), makeSerialNumber()));
    }

    public IAssistanceIf createAssistance(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber) throws DuplicateIdException
    {
        checkCreateOp();
        return (IAssistanceIf) createdItem(new AssistanceImpl(anObjectId, aNumber));
    }

}