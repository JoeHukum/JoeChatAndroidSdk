package com.joehukum.chat.user;

/**
 * Created by pulkitkumar on 19/03/16.
 */
public class Credentials
{
    private String authKey;
    private String phoneNumber;
    private String clientUserId;
    private String contentAuthority;

    public Credentials(String authKey, String phoneNumber, String clientUserId)
    {
        this.authKey = authKey;
        this.phoneNumber = phoneNumber;
        this.clientUserId = clientUserId;
    }

    public String getAuthKey()
    {
        return authKey;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public String getClientUserId()
    {
        return clientUserId;
    }

    public String getContentAuthority()
    {
        return contentAuthority;
    }
}
