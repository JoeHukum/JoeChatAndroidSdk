package com.jh;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by pulkitkumar on 26/04/16.
 */
public class UserStore
{
    private static final String SHARED_PREFERENCES = "userSharedPref";
    private static final String CLIENT_HASH = "ClientHash";
    private static final String EMAIL = "EMAIL";
    private static final String PHONE = "phone";
    private static final String EMPTY = "";

    public void saveUser(Context context, User user)
    {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CLIENT_HASH, user.getClientHash());
        editor.putString(EMAIL, user.getEmail());
        editor.putString(PHONE, user.getPhone());
        editor.commit();
    }

    @Nullable
    public User getUser(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String clientHash = preferences.getString(CLIENT_HASH, EMPTY);
        if (TextUtils.isEmpty(clientHash))
        {
            return null;
        } else
        {
            String email = preferences.getString(EMAIL, EMPTY);
            String phone = preferences.getString(PHONE, EMPTY);
            return new User(clientHash, email, phone);
        }
    }
}
