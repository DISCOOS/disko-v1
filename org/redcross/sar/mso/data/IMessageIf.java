package org.redcross.sar.mso.data;

public interface IMessageIf extends ITimeItemIf
{
    public boolean addBroadcastNotAccepted(ICommunicatorIf aReceiver);

    public boolean acceptBroadcastReceiver(ICommunicatorIf aReceiver);

    public MsoListImpl<ICommunicatorIf> getBroadcastReceivers();

    public MsoListImpl<ICommunicatorIf> getBroadcastAccepted();

    public boolean isBroadcast();

}