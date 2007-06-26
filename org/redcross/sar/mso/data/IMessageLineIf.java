package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;

/**
 * User: vinjar
 * Date: 25.jun.2007
 */

/**
 *
 */
public interface IMessageLineIf extends IMsoObjectIf
{
    public enum MessageLineType
    {
        TEXT,
        POI,
        FINDING,
        ASSIGNED,
        STARTED,
        COMPLETE
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for ENUM attributes
    *-------------------------------------------------------------------------------------------*/

    public void setLineType(MessageLineType aLineType);

    public void setLineType(String aLineType);

    public MessageLineType getLineType();

    public IMsoModelIf.ModificationState getLineTypeState();

    public IAttributeIf.IMsoEnumIf<MessageLineType> getLineTypeAttribute();

    /*-------------------------------------------------------------------------------------------
    * Methods for attributes
    *-------------------------------------------------------------------------------------------*/

    public void setLineNumber(int aNumber);

    public int getLineNumber();

    public IMsoModelIf.ModificationState getLineNumberState();

    public IAttributeIf.IMsoIntegerIf getLineNumberAttribute();

    public void setText(String aText);

    public String getText();

    public IMsoModelIf.ModificationState getTextState();

    public IAttributeIf.IMsoStringIf getTextAttribute();

    /*-------------------------------------------------------------------------------------------
    * Methods for references
    *-------------------------------------------------------------------------------------------*/

    public void setPOI(IPOIIf aPoi);

    public IPOIIf getPOI();

    public IMsoModelIf.ModificationState getPOIState();

    public IMsoReferenceIf<IPOIIf> getPOIAttribute();
}
