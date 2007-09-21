package org.redcross.sar.mso.data;

import java.util.Comparator;

/**
 *
 */
public interface ICommunicatorIf extends IMsoObjectIf
{
    public final static Comparator<ICommunicatorIf> COMMUNICATOR_COMPARATOR = new Comparator<ICommunicatorIf>()
    {
        public int compare(ICommunicatorIf o1, ICommunicatorIf o2)
        {
            return o1.getCommunicatorNumber() - o2.getCommunicatorNumber();
        }
    };

    public void setCallSign(String aCallsign);

    public String getCallSign();

    public char getCommunicatorNumberPrefix();

    public int getCommunicatorNumber();
}
