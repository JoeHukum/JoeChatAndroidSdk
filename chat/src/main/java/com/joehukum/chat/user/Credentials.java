package com.joehukum.chat.user;

/**
 * Created by pulkitkumar on 19/03/16.
 */
public class Credentials
{
    private static final String EMPTY = "";
    private String authKey;
    private String phoneNumber;
    private String email;
    private String pubNubSubscribeKey;
    private String pubNubPublishKey;
    private String customerHash;
    private String contentAuthority;
    private String clientName;

    public Credentials(String authKey, String phoneNumber, String email, String publishKey, String subscribeKey, String customerHash, String contentAuthority)
    {
        this.authKey = authKey;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.pubNubPublishKey = publishKey;
        this.pubNubSubscribeKey = subscribeKey;
        this.customerHash = customerHash;
        this.contentAuthority = contentAuthority;
    }

    public String getAuthKey()
    {
        if (authKey == null)
        {
            return EMPTY;
        }
        return authKey;
    }

    public String getPhoneNumber()
    {
        if (phoneNumber == null)
        {
            return EMPTY;
        }
        return phoneNumber;
    }

    public String getEmail()
    {
        if (email == null)
        {
            return EMPTY;
        }
        return email;
    }

    public String getPubNubSubscribeKey()
    {
        if (pubNubSubscribeKey == null)
        {
            return EMPTY;
        }
        return pubNubSubscribeKey;
    }

    public String getPubNubPublishKey()
    {
        if (pubNubPublishKey == null)
        {
            return EMPTY;
        }
        return pubNubPublishKey;
    }

    public String getCustomerHash()
    {
        if (customerHash == null)
        {
            return EMPTY;
        }
        return customerHash;
    }

    public String getContentAuthority()
    {
        if (contentAuthority == null)
        {
            return EMPTY;
        }
        return contentAuthority;
    }

    public String getClientName()
    {
        return clientName;
    }

    public void setClientName(String clientName)
    {
        this.clientName = clientName;
    }
}
