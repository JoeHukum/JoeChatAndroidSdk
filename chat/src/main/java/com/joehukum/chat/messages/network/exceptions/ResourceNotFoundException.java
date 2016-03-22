package com.joehukum.chat.messages.network.exceptions;

/**
 * 404 http response.
 */
public class ResourceNotFoundException extends AppServerException
{
    public ResourceNotFoundException ()
    {
        super();
    }

    public ResourceNotFoundException (String message)
    {
        super (message);
    }

    public ResourceNotFoundException (Throwable cause)
    {
        super (cause);
    }

    public ResourceNotFoundException (String message, Throwable cause)
    {
        super (message, cause);
    }
}
