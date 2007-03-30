package org.redcross.sar.mso.data;

import java.util.ArrayList;

/**
 *
 */
public interface IHierarchicalUnitIf extends IMsoObjectIf
{
    /**
     * Define superior AbstractUnit
     *
     * @param aSuperior The new superior unit
     * @return False if error (circular reference), otherwise True
     */
    public boolean setSuperior(IHierarchicalUnitIf aSuperior);

    /**
     * Get superior AbstractUnit
     *
     * @return The superior unit
     */
    public IHierarchicalUnitIf getSuperior();

    /**
     * Generate list of subordinates
     *
     * @return The list
     */
    public ArrayList<IHierarchicalUnitIf> getSubOrdinates();

}
