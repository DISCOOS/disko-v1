package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.MsoModelImpl;
import org.redcross.sar.util.Internationalization;
import org.redcross.sar.util.except.MsoCastException;
import org.redcross.sar.util.mso.DTG;

import java.text.MessageFormat;
import java.util.Calendar;
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
    private final AttributeImpl.MsoString m_lineText = new AttributeImpl.MsoString(this, "LineText");
    private final AttributeImpl.MsoCalendar m_operationTime = new AttributeImpl.MsoCalendar(this, "OperationTime");
    private final AttributeImpl.MsoEnum<MessageLineType> m_lineType = new AttributeImpl.MsoEnum<MessageLineType>(this, "LineType", MessageLineType.TEXT);

    private final MsoReferenceImpl<IPOIIf> m_linePOI = new MsoReferenceImpl<IPOIIf>(this, "LinePOI", true);
    private final MsoReferenceImpl<IAssignmentIf> m_lineAssignment = new MsoReferenceImpl<IAssignmentIf>(this, "LineAssignment", true);

    public static String getText(String aKey)
    {
        return Internationalization.getFullBundleText(Internationalization.getBundle(IMessageLineIf.class), aKey);
    }

    public String getLineTypeText()
    {
        return m_lineType.getInternationalName();
    }

    public MessageLineImpl(IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    protected void defineAttributes()
    {
        addAttribute(m_lineNumber);
        addAttribute(m_lineText);
        addAttribute(m_lineType);
        addAttribute(m_operationTime);
    }

    protected void defineLists()
    {
    }

    protected void defineReferences()
    {
        addReference(m_linePOI);
        addReference(m_lineAssignment);
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
        return IMsoManagerIf.MsoClassCode.CLASSCODE_MESSAGELINE;
    }

    public boolean addObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
        return true;
    }

    public boolean removeObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
        return true;
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


    public void setLineText(String aText)
    {
        m_lineText.setValue(aText);
    }

    public String getLineText()
    {
        return m_lineText.getString();
    }

    public IMsoModelIf.ModificationState getLineTextState()
    {
        return m_lineText.getState();
    }

    public IAttributeIf.IMsoStringIf getLineTextAttribute()
    {
        return m_lineText;
    }

    public void setOperationTime(Calendar anOperationTime)
    {
        m_operationTime.setValue(anOperationTime);
    }

    public Calendar getOperationTime()
    {
        return m_operationTime.getCalendar();
    }

    public IMsoModelIf.ModificationState getOperationTimeState()
    {
        return m_operationTime.getState();
    }

    public IAttributeIf.IMsoCalendarIf getOperationTimeAttribute()
    {
        return m_operationTime;
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for references
    *-------------------------------------------------------------------------------------------*/

    public void setLinePOI(IPOIIf aPoi)
    {
        m_linePOI.setReference(aPoi);
    }

    public IPOIIf getLinePOI()
    {
        return m_linePOI.getReference();
    }

    public IMsoModelIf.ModificationState getLinePOIState()
    {
        return m_linePOI.getState();
    }

    public IMsoReferenceIf<IPOIIf> geLinetPOIAttribute()
    {
        return m_linePOI;
    }


    public void setLineAssignment(IAssignmentIf anAssignment)
    {
        m_lineAssignment.setReference(anAssignment);
    }

    public IAssignmentIf getLineAssignment()
    {
        return m_lineAssignment.getReference();
    }

    public IMsoModelIf.ModificationState getLineAssignmentState()
    {
        return m_lineAssignment.getState();
    }

    public IMsoReferenceIf<IAssignmentIf> geLinetAssignmentAttribute()
    {
        return m_lineAssignment;
    }

    @Override
    public String toString()
    {
        if (m_lineType == null)
        {
            return "";
        }
        switch (getLineType())
        {
            case ASSIGNED:
            case STARTED:
            case COMPLETE:
                if (getLineAssignment() != null)
                {
                    return MessageFormat.format("{0} {1} {2} {3} {4}",
                            getLineNumber(), getLineAssignment().getTypeText(), getLineAssignment().getNumber(), getLineTypeText(), DTG.CalToDTG(getOperationTime()));
                } else
                {
                    return "";
                }

            case POI:
            case FINDING:
                IPOIIf poi = getLinePOI();
                if (poi != null)
                {
                    return getLineNumber() + " " + getLineTypeText() + " " + poi.getTypeText() + " " + poi.getPosition();
                }
                return getLineNumber() + " " + getLineTypeText() + " NULL";
            case TEXT:
                return getLineNumber() + " " + getLineText();
            default:
                return getLineNumber() + " " + getLineTypeText() + " " + getLineText();
        }
    }

    private final static SelfSelector<IMessageLineIf, IMessageIf> owningMessageSelector = new SelfSelector<IMessageLineIf, IMessageIf>()
    {
        public boolean select(IMessageIf anObject)
        {
            return (anObject.getMessageLines().contains(m_object));
        }
    };

    public IMessageIf getOwningMessage()
    {
        owningMessageSelector.setSelfObject(this);
        return MsoModelImpl.getInstance().getMsoManager().getCmdPost().getMessageLog().selectSingleItem(owningMessageSelector);
    }


}
