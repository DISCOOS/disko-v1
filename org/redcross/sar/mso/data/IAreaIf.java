package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.DuplicateIdException;

import java.util.Collection;

public interface IAreaIf extends IMsoObjectIf
{
    /*-------------------------------------------------------------------------------------------
    * Methods for attributes
    *-------------------------------------------------------------------------------------------*/

// todo     public void setGeodata(geodata aGeodata);

//    public void setGeodata(geodata aGeodata);
//
//    public geodata getGeodata();
//
//    public IMsoModelIf.ModificationState getGeodataState();
//
//    public IAttributeIf.IMsoGeodataIf getGeodataAttribute();

    public void setRemarks(String aRemarks);

    public String getRemarks();

    public IMsoModelIf.ModificationState getRemarksState();

    public IAttributeIf.IMsoStringIf getRemarksAttribute();

    /*-------------------------------------------------------------------------------------------
    * Methods for lists
    *-------------------------------------------------------------------------------------------*/

    public void addAreaPOIs(IPOIIf anIPOIIf) throws DuplicateIdException;

    public IPOIListIf getAreaPOIs();

    public IMsoModelIf.ModificationState getAreaPOIsState(IPOIIf anIPOIIf);

    public Collection<IPOIIf> getAreaPOIsItems();

    /*-------------------------------------------------------------------------------------------
    * Other specified methods
    *-------------------------------------------------------------------------------------------*/

    public IAssignmentIf getAssignment();

    public IMsoReferenceIf<IHypothesisIf> getHypotesisAttribute();

    public void setHypothesis(IHypothesisIf anHypothesis);
}