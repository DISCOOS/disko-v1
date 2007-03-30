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
        return createdUniqueItem(new AssignmentImpl(makeUniqueId()));
    }

    public IAssignmentIf createAssignment(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException
    {
        checkCreateOp();
        return createdItem(new AssignmentImpl(anObjectId));
    }
}