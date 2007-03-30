package org.redcross.sar.modelDriver;

import org.redcross.sar.mso.data.AbstractMsoObject;
import org.redcross.sar.mso.data.IMsoObjectIf;

import java.util.Date;
import java.util.Random;

/**
 *   For documentation, see {@link  org.redcross.sar.modelDriver.IModelDriverIf}
 */
public class ModelDriver implements IModelDriverIf
{
    Random m_rand = new Random(89652467667623L);

    public IMsoObjectIf.IObjectIdIf makeObjectId()
    {
        int rand = m_rand.nextInt(1000);
        long time = new Date().getTime();
        return new AbstractMsoObject.ObjectId(Long.toString(time)+"."+Integer.toString(rand));

    }
}
