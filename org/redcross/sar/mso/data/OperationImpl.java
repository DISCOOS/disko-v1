/**
 *
 */
package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.DuplicateIdException;

import java.util.Collection;

public class OperationImpl extends AbstractMsoObject implements IOperationIf
{
    private final AttributeImpl.MsoString m_number = new AttributeImpl.MsoString(this, "Number");
    private final AttributeImpl.MsoString m_numberPrefix = new AttributeImpl.MsoString(this, "NumberPrefix");

    private final CmdPostListImpl m_cmdPostList = new CmdPostListImpl(this, "CmdPostList", true);

    public OperationImpl(IMsoObjectIf.IObjectIdIf anObjectId, String aNumberPrefix, String aNumber)
    {
        super(anObjectId);
        setNumberPrefix(aNumberPrefix);
        setNumber(aNumber);
    }

    protected void defineAttributes()
    {
        addAttribute(m_number);
        addAttribute(m_numberPrefix);
    }

    protected void defineLists()
    {
        addList(m_cmdPostList);
    }

    protected void defineReferences()
    {
    }

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATION;
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for attributes
    *-------------------------------------------------------------------------------------------*/

    public void setNumber(String aNumber)
    {
        m_number.setValue(aNumber);
    }

    public String getNumber()
    {
        return m_number.getString();
    }

    public IMsoModelIf.ModificationState getNumberState()
    {
        return m_number.getState();
    }

    public IAttributeIf.IMsoStringIf getNumberAttribute()
    {
        return m_number;
    }

    public void setNumberPrefix(String aNumberPrefix)
    {
        m_numberPrefix.setValue(aNumberPrefix);
    }

    public String getNumberPrefix()
    {
        return m_numberPrefix.getString();
    }

    public IMsoModelIf.ModificationState getNumberPrefixState()
    {
        return m_numberPrefix.getState();
    }

    public IAttributeIf.IMsoStringIf getNumberPrefixAttribute()
    {
        return m_numberPrefix;
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for lists
    *-------------------------------------------------------------------------------------------*/

    public void addCmdPostList(ICmdPostIf anICmdPostIf) throws DuplicateIdException
    {
        m_cmdPostList.add(anICmdPostIf);
    }

    public ICmdPostListIf getCmdPostList()
    {
        return m_cmdPostList;
    }

    public IMsoModelIf.ModificationState getCmdPostListState(ICmdPostIf anICmdPostIf)
    {
        return m_cmdPostList.getState(anICmdPostIf);
    }

    public Collection<ICmdPostIf> getCmdPostListItems()
    {
        return m_cmdPostList.getItems();
    }

    /*-------------------------------------------------------------------------------------------
    * Other specified functions
    *-------------------------------------------------------------------------------------------*/

    public String getOperationNumber()
    {
        return null; /*todo*/
    }
}
