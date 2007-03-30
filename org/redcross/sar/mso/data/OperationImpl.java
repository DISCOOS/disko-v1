/**
 *
 */
package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;

public class OperationImpl extends AbstractMsoObject implements IOperationIf
{
    private final CmdPostListImpl m_cmdPostList = new CmdPostListImpl(this, "", true);

    public OperationImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    protected void defineAttributes()
    {
    }

    protected void defineLists()
    {
        addList(m_cmdPostList);
    }

    protected void defineReferences()
    {
    }

    public ICmdPostListIf getCmdPostList()
    {
        return m_cmdPostList;
    }


    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATION;
    }
}
