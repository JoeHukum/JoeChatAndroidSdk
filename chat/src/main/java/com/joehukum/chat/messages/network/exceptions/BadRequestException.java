package com.joehukum.chat.messages.network.exceptions;

/**
 * To be thrown on 401 / 403 http response.
 */
public class BadRequestException extends AppServerException
{
    public BadRequestException ()
    {
        super();
    }

    public BadRequestException (String message)
    {
        super (message);
    }

    public BadRequestException (Throwable cause)
    {
        super (cause);
    }

    public BadRequestException (String message, Throwable cause)
    {
        super (message, cause);
    }
}