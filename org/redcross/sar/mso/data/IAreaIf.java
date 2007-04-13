package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.DuplicateIdException;
import org.redcross.sar.util.except.IllegalOperationException;
import org.redcross.sar.util.mso.*;

import java.util.Collection;

public interface IAreaIf extends IMsoObjectIf
{
    /*-------------------------------------------------------------------------------------------
    * Methods for attributes
    *-------------------------------------------------------------------------------------------*/

    public void setGeodata(GeoCollection aGeodata);

    public GeoCollection getGeodata();

    public IMsoModelIf.ModificationState getGeodataState();

    public IAttributeIf.IMsoGeoCollectionIf getGeodataAttribute();

    public void setRemarks(String aRemarks);

    public String getRemarks();

    public IMsoModelIf.ModificationState getRemarksState();

    public IAttributeIf.IMsoStringIf getRemarksAttribute();

    /*-------------------------------------------------------------------------------------------
    * Methods for lists
    *-------------------------------------------------------------------------------------------*/

    public void addAreaPOI(IPOIIf anIPOIIf) throws DuplicateIdException;

    public IPOIListIf getAreaPOIs();

    public IMsoModelIf.ModificationState getAreaPOIsState(IPOIIf anIPOIIf);

    public Collection<IPOIIf> getAreaPOIsItems();

    /*-------------------------------------------------------------------------------------------
    * Other specified methods
    *-------------------------------------------------------------------------------------------*/

    public IAssignmentIf getOwningAssignment();

    public void verifyAssignable(IAssignmentIf anAssignment) throws IllegalOperationException;

    public IMsoReferenceIf<IHypothesisIf> getHypotesisAttribute();

    public void setHypothesis(IHypothesisIf anHypothesis);
}