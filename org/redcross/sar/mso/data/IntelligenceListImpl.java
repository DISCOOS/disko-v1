package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

public class IntelligenceListImpl extends MsoListImpl<IIntelligenceIf> implements IIntelligenceListIf
{

    public IntelligenceListImpl(IMsoObjectIf anOwner, String theName, boolean isMain, int aSize)
    {
        super(anOwner, theName, isMain, aSize);
    }

    public IIntelligenceIf createIntelligence()
    {
        checkCreateOp();
        return createdUniqueItem(new IntelligenceImpl(makeUniqueId()));
    }

    public IIntelligenceIf createIntelligence(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException
    {
        checkCreateOp();
        return createdItem(new IntelligenceImpl(anObjectId));
    }

}