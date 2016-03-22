package com.joehukum.chat.messages.network.exceptions;

/**
 * Created by pulkitkumar on 22/03/16.
 */
public abstract class AppServerException extends Exception
{
    public AppServerException()
    {
        super();
    }

    public AppServerException (String message)
    {
        super (message);
    }

    public AppServerException (Throwable cause)
    {
        super (cause);
    }

    public AppServerException (String message, Throwable cause)
    {
        super (message, cause);
    }
}
