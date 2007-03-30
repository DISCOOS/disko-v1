package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.util.except.MsoCastException;

public class CheckpointImpl extends AbstractMsoObject implements ICheckpointIf
{
    private final MsoReferenceImpl<ITaskIf> m_taskReference = new MsoReferenceImpl<ITaskIf>(this,"CheckpointTask",true);

    public CheckpointImpl(IMsoObjectIf.IObjectIdIf anObjectId)
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
        addReference(m_taskReference);
    }

    public static CheckpointImpl implementationOf(ICheckpointIf anInterface) throws MsoCastException
    {
        try
        {
            return (CheckpointImpl) anInterface;
        }
        catch (ClassCastException e)
        {
            throw new MsoCastException("Illegal cast to CheckpointImpl");
        }
    }

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_CHECKPOINT;
    }


}