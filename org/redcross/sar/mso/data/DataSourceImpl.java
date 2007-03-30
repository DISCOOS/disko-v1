package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;

public class DataSourceImpl extends AbstractMsoObject implements IDataSourceIf
{
    public DataSourceImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    protected void defineAttributes()
    {
    }

    protected void defineLists()
    {
    }

    protected void defineReferences()
    {
    }

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_DATASOURCE;
    }


}