package org.redcross.sar.util.except;

/**
 * Indicates that an object id (that is supposed to be unique) is used over again. 
 */
public class DuplicateIdException extends MsoException
{
    public DuplicateIdException()
    {
        super();
    }

    public DuplicateIdException(String aMessage)
    {
        super(aMessage);
    }

}
