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

    protected UnitType getTypeBySubclass()
    {
        return IUnitIf.UnitType.DOG;
    }

    public String toString()
    {
        return super.toString();
    }

}
