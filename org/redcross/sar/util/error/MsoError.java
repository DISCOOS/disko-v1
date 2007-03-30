package org.redcross.sar.util.error;

/**
 *
 */
public class MsoError extends Error
{
    /**
     * Create exception with a message.
     * @param aMessage The error message
     */
    public MsoError(String aMessage)
    {
        super("MSO System error: " + aMessage);
    }
}
