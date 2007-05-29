package org.redcross.sar.mso.data;

/**
 *
 */
public class DogImpl extends AbstractUnit implements IDogIf
{
    public DogImpl(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber)
    {
        super(anObjectId, aNumber);
    }

    protected void defineAttributes()
    {
        super.defineAttributes();
    }

    protected void defineLists()
    {
        super.defineLists();
    }

    protected void defineReferences()
    {
        super.defineReferences();
    }


    @Override
    public void addObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
        super.addObjectReference(anObject, aReferenceName);
    }

    @Override
    public void removeObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
        super.removeObjectReference(anObject, aReferenceName);
    }

    protected UnitType getTypeBySubclass()
    {
        return IUnitIf.UnitType.DOG;
    }

    public String getSubTypeName()
    {
        return "CAR"; // todo expand
    }

    public String toString()
    {
        return super.toString();
    }

}
