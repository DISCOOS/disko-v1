package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.DuplicateIdException;

import java.util.Collection;

/**
 *
 */
public interface IOperationIf extends IMsoObjectIf
{
    /*-------------------------------------------------------------------------------------------
    * Methods for attributes
    *-------------------------------------------------------------------------------------------*/

    public void setNumber(String aNumber);

    public String getNumber();

    public IMsoModelIf.ModificationState getNumberState();

    public IAttributeIf.IMsoStringIf getNumberAttribute();

    public void setNumberPrefix(String aNumberPrefix);

    public String getNumberPrefix();

    public IMsoModelIf.ModificationState getNumberPrefixState();

    public IAttributeIf.IMsoStringIf getNumberPrefixAttribute();

    /*-------------------------------------------------------------------------------------------
    * Methods for lists
    *-------------------------------------------------------------------------------------------*/

    public void addCmdPostList(ICmdPostIf anICmdPostIf);

    public ICmdPostListIf getCmdPostList();

    public IMsoModelIf.ModificationState getCmdPostListState(ICmdPostIf anICmdPostIf);

    public Collection<ICmdPostIf> getCmdPostListItems();

    /*-------------------------------------------------------------------------------------------
    * Other specified functions
    *-------------------------------------------------------------------------------------------*/

    public String getOperationNumber();
}
