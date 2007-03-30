package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

public class PersonnelListImpl extends MsoListImpl<IPersonnelIf> implements IPersonnelListIf
{

    public PersonnelListImpl(IMsoObjectIf anOwner, String theName, boolean isMain)
    {
        super(anOwner, theName, isMain);
    }

    public PersonnelListImpl(IMsoObjectIf anOwner, String theName, boolean isMain, int aSize)
    {
        super(anOwner, theName, isMain, aSize);
    }

    public IPersonnelIf createPersonnel(String aName)
    {
        checkCreateOp();
        return createdUniqueItem(new PersonnelImpl(makeUniqueId(), aName));
    }

    public IPersonnelIf createPersonnel(IMsoObjectIf.IObjectIdIf anObjectId,String aName) throws DuplicateIdException
    {
        checkCreateOp();
        return createdItem(new PersonnelImpl(anObjectId, aName));
    }
}