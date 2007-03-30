package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.DuplicateIdException;

import java.util.Collection;

public interface IAreaIf extends IMsoObjectIf
{
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

    public IAssignmentIf getAssignment();

    public IHypothesisIf getHypotesis();

    public void addAreaPOIs(IPOIIf anIPOIIf) throws DuplicateIdException;

    public IPOIListIf getAreaPOIs();

    public IMsoModelIf.ModificationState getAreaPOIsState(IPOIIf anIPOIIf);

    public Collection<IPOIIf> getAreaPOIsItems();
}