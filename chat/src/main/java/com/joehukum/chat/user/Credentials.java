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
    private String customerHash;
    private String clientName;

    public Credentials(String authKey, String phoneNumber, String email, String customerHash)
    {
        this.authKey = authKey;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.customerHash = customerHash;
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

    public String getCustomerHash()
    {
        if (customerHash == null)
        {
            return EMPTY;
        }
        return customerHash;
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
