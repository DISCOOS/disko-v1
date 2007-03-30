package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

public class EquipmentListImpl extends MsoListImpl<IEquipmentIf> implements IEquipmentListIf
{

    public EquipmentListImpl(IMsoObjectIf anOwner, String theName, boolean isMain)
    {
        super(anOwner, theName, isMain);
    }

    public EquipmentListImpl(IMsoObjectIf anOwner, String theName, boolean isMain, int aSize)
    {
        super(anOwner, theName, isMain, aSize);
    }

    public IEquipmentIf createEquipment()
    {
        checkCreateOp();
        return createdUniqueItem(new EquipmentImpl(makeUniqueId()));
    }

    public IEquipmentIf createEquipment(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException
    {
        checkCreateOp();
        return createdItem(new EquipmentImpl(anObjectId));
    }


}