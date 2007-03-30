package org.redcross.sar.mso.data;

public interface ICalloutIf extends IMsoObjectIf
{
    /**
     * Add a reference to an excisting personnel.
     *
     * @param aPersonnel The personnel to add.
     * @return <code>false</code> if personell exists in list already, <code>true</code> otherwise.
     */
    public boolean addPersonel(IPersonnelIf aPersonnel);
}