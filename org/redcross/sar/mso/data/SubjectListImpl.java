package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

public class SubjectListImpl extends MsoListImpl<ISubjectIf> implements ISubjectListIf
{

    public SubjectListImpl(IMsoObjectIf anOwner, String theName, boolean isMain)
    {
        super(anOwner, theName, isMain);
    }

    public SubjectListImpl(IMsoObjectIf anOwner, String theName, boolean isMain, int aSize)
    {
        super(anOwner, theName, isMain, aSize);
    }

    public ISubjectIf createSubject(String aName, String aDescription)
    {
        checkCreateOp();
        return createdUniqueItem(new SubjectImpl(makeUniqueId(), aName, aDescription));
    }

    public ISubjectIf createSubject(IMsoObjectIf.IObjectIdIf anObjectId, String aName, String aDescription) throws DuplicateIdException
    {
        checkCreateOp();
        return createdItem(new SubjectImpl(anObjectId, aName, aDescription));
    }


}