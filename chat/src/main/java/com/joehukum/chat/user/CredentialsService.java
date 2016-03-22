package com.joehukum.chat.user;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by pulkitkumar on 19/03/16.
 */
public class CredentialsService
{
    private static final String PREFERENCES = "userPreferences";
    private static final String AUTH_KEY = "authKey";
    private static final String PHONE = "phone";
    private static final String CLIENT_USER_ID = "clientUserId";
    private static final String EMPTY = "";

    private Credentials mCredentialsInstance;

    public void saveUserCredentials(Context context, String authKey, String clientUserId, String phone)
    {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AUTH_KEY, authKey);
        editor.putString(PHONE, phone);
        editor.putString(CLIENT_USER_ID, clientUserId);
        editor.commit();
        mCredentialsInstance = new Credentials(authKey, phone, clientUserId);
    }

    public Credentials getUserCredentials(Context context)
    {
        if (mCredentialsInstance == null)
        {
            SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            String authKey = preferences.getString(AUTH_KEY, EMPTY);
            String phone = preferences.getString(PHONE, EMPTY);
            String clientUserId = preferences.getString(CLIENT_USER_ID, EMPTY);
            mCredentialsInstance = new Credentials(authKey, phone, clientUserId);
        }
        return mCredentialsInstance;
    }
}
