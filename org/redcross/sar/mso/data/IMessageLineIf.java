package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;

import java.util.Calendar;
import java.util.Comparator;

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

    public final static Comparator<IMessageLineIf> MESSAGE_LINE_TIME_COMPARATOR = new Comparator<IMessageLineIf>()
    {
        public int compare(IMessageLineIf o1, IMessageLineIf o2)
        {
            return o2.getOperationTime().compareTo(o1.getOperationTime()); // Sort according to latest time
        }
    };

    public final static Comparator<IMessageLineIf> LINE_NUMBER_COMPARATOR = new Comparator<IMessageLineIf>()
    {
        public int compare(IMessageLineIf m1, IMessageLineIf m2)
        {
            return m1.getLineNumber() - m2.getLineNumber();
        }
    };


    /*-------------------------------------------------------------------------------------------
    * Methods for ENUM attributes
    *-------------------------------------------------------------------------------------------*/

    public void setLineType(MessageLineType aLineType);

    public void setLineType(String aLineType);

    public MessageLineType getLineType();

    public IMsoModelIf.ModificationState getLineTypeState();

    public IAttributeIf.IMsoEnumIf<MessageLineType> getLineTypeAttribute();

    public String getLineTypeText();

    /*-------------------------------------------------------------------------------------------
    * Methods for attributes
    *-------------------------------------------------------------------------------------------*/

    public void setLineNumber(int aNumber);

    public int getLineNumber();

    public IMsoModelIf.ModificationState getLineNumberState();

    public IAttributeIf.IMsoIntegerIf getLineNumberAttribute();

    public void setLineText(String aText);

    public String getLineText();

    public IMsoModelIf.ModificationState getLineTextState();

    public IAttributeIf.IMsoStringIf getLineTextAttribute();

    public void setOperationTime(Calendar anOperationTime);

    public Calendar getOperationTime();

    public IMsoModelIf.ModificationState getOperationTimeState();

    public IAttributeIf.IMsoCalendarIf getOperationTimeAttribute();


    /*-------------------------------------------------------------------------------------------
    * Methods for references
    *-------------------------------------------------------------------------------------------*/

    public void setLinePOI(IPOIIf aPoi);

    public IPOIIf getLinePOI();

    public IMsoModelIf.ModificationState getLinePOIState();

    public IMsoReferenceIf<IPOIIf> geLinetPOIAttribute();

    public void setLineAssignment(IAssignmentIf anAssignment);

    public IAssignmentIf getLineAssignment();

    public IMsoModelIf.ModificationState getLineAssignmentState();

    public IMsoReferenceIf<IAssignmentIf> geLinetAssignmentAttribute();

    public IMessageIf getOwningMessage();
}
