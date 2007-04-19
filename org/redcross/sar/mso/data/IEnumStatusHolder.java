package org.redcross.sar.mso.data;
/**
 * Created by IntelliJ IDEA.
 * User: vinjar
 * Date: 18.apr.2007
 * To change this template use File | Settings | File Templates.
 */

/**
 *
 */
public interface IEnumStatusHolder<E extends Enum> extends IMsoObjectIf
{
    public E getStatus();
}
