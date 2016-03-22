package com.joehukum.chat.messages.network.exceptions;

/**
 * 500+ http response.
 */
public class ServerErrorException extends AppServerException
{
    public ServerErrorException ()
    {
        super();
    }

    public ServerErrorException (String message)
    {
        super (message);
    }

    public ServerErrorException (Throwable cause)
    {
        super (cause);
    }

    public ServerErrorException (String message, Throwable cause)
    {
        super (message, cause);
    }
}
