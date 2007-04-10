package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.mso.*;

public interface ISearchAreaIf extends IMsoObjectIf
{

    public enum SearchAreaStatus
    {
        PROCESSING,
        ABORTED,
        FINISHED
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for ENUM attributes
    *-------------------------------------------------------------------------------------------*/

    public void setStatus(SearchAreaStatus aStatus);

    public void setStatus(String aStatus);

    public SearchAreaStatus getStatus();

    public IMsoModelIf.ModificationState getStatusState();

    public IAttributeIf.IMsoEnumIf<SearchAreaStatus> getStatusAttribute();

    /*-------------------------------------------------------------------------------------------
    * Methods for attributes
    *-------------------------------------------------------------------------------------------*/

    public void setGeodata(Polygon aGeodata);

    public Polygon getGeodata();

    public IMsoModelIf.ModificationState getGeodataState();

    public IAttributeIf.IMsoPolygonIf getGeodataAttribute();

    public void setPriority(int aPriority);

    public int getPriority();

    public IMsoModelIf.ModificationState getPriorityState();

    public IAttributeIf.IMsoIntegerIf getPriorityAttribute();

    public void setRemarks(String aRemarks);

    public String getRemarks();

    public IMsoModelIf.ModificationState getRemarksState();

    public IAttributeIf.IMsoStringIf getRemarksAttribute();
}