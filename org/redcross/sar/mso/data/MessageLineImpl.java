package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.MsoCastException;
/**
 * Created by IntelliJ IDEA.
 * User: vinjar
 * Date: 25.jun.2007
 * To change this template use File | Settings | File Templates.
 */

/**
 *
 */
public class MessageLineImpl extends AbstractMsoObject implements IMessageLineIf
{
    private final AttributeImpl.MsoInteger m_lineNumber = new AttributeImpl.MsoInteger(this, "LineNumber");
    private final AttributeImpl.MsoString m_text = new AttributeImpl.MsoString(this, "Text");
    private final AttributeImpl.MsoEnum<MessageLineType> m_lineType = new AttributeImpl.MsoEnum<MessageLineType>(this, "LineType", MessageLineType.TEXT);

    private final MsoReferenceImpl<IPOIIf> m_POI = new MsoReferenceImpl<IPOIIf>(this, "POI", true);


    public MessageLineImpl(IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    protected void defineAttributes()
    {
        addAttribute(m_lineNumber);
        addAttribute(m_text);
        addAttribute(m_lineType);
    }

    protected void defineLists()
    {
    }

    protected void defineReferences()
    {
        addReference(m_POI);
    }

    public static MessageLineImpl implementationOf(IMessageLineIf anInterface) throws MsoCastException
        {
        try
        {
            return (MessageLineImpl) anInterface;
        }
        catch (ClassCastException e)
        {
            throw new MsoCastException("Illegal cast to MessageLineImpl");
        }
    }

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_MESSAGE;
    }

    public void addObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
    }

    public void removeObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for ENUM attributes
    *-------------------------------------------------------------------------------------------*/


    public void setLineType(MessageLineType aLineType)
    {
        m_lineType.setValue(aLineType);
    }

    public void setLineType(String aLineType)
    {
        m_lineType.setValue(aLineType);
    }

    public MessageLineType getLineType()
    {
        return m_lineType.getValue();
    }

    public IMsoModelIf.ModificationState getLineTypeState()
    {
        return m_lineType.getState();
    }

    public IAttributeIf.IMsoEnumIf<MessageLineType> getLineTypeAttribute()
    {
        return m_lineType;
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for attributes
    *-------------------------------------------------------------------------------------------*/

    public void setLineNumber(int aNumber)
    {
        m_lineNumber.setValue(aNumber);
    }

    public int getLineNumber()
    {
        return m_lineNumber.intValue();
    }

    public IMsoModelIf.ModificationState getLineNumberState()
    {
        return m_lineNumber.getState();
    }

    public IAttributeIf.IMsoIntegerIf getLineNumberAttribute()
    {
        return m_lineNumber;
    }


    public void setText(String aText)
    {
        m_text.setValue(aText);
    }

    public String getText()
    {
        return m_text.getString();
    }

    public IMsoModelIf.ModificationState getTextState()
    {
        return m_text.getState();
    }

    public IAttributeIf.IMsoStringIf getTextAttribute()
    {
        return m_text;
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for references
    *-------------------------------------------------------------------------------------------*/

    public void setPOI(IPOIIf aPoi)
    {
        m_POI.setReference(aPoi);
    }

    public IPOIIf getPOI()
    {
        return m_POI.getReference();
    }

    public IMsoModelIf.ModificationState getPOIState()
    {
        return m_POI.getState();
    }

    public IMsoReferenceIf<IPOIIf> getPOIAttribute()
    {
        return m_POI;
    }

    public String toString()
    {
        return  getLineNumber() + " " + getLineType().name() + " " + getText();
    }
}
