package com.jh;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by pulkitkumar on 26/04/16.
 */
public class User
{
    private @NonNull  String clientHash;
    private @Nullable String email;
    private @Nullable String phone;

    public User(String clientHash, String email, String phone)
    {
        this.clientHash = clientHash;
        this.email = email;
        this.phone = phone;
    }

    @NonNull
    public String getClientHash()
    {
        return clientHash;
    }

    @Nullable
    public String getEmail()
    {
        return email;
    }

    @Nullable
    public String getPhone()
    {
        return phone;
    }
}
