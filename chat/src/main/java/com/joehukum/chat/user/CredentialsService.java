package com.joehukum.chat.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.joehukum.chat.messages.network.Api;
import com.joehukum.chat.messages.network.HttpIO;
import com.joehukum.chat.messages.network.exceptions.AppServerException;

import java.io.IOException;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by pulkitkumar on 19/03/16.
 */
public class CredentialsService
{
    private static final String TAG = CredentialsService.class.getName();

    private static final String PREFERENCES = "userPreferences";
    private static final String GOOGLE_PROJECT_ID = "991819025530";

    private static final String AUTH_KEY = "authKey";
    private static final String PHONE = "phone";
    private static final String EMAIL = "email";
    private static final String CUSTOMER_HASH = "customerHash";
    private static final String CONTENT_AUTHORITY = "contentAuthority";

    private static final String EMPTY = "";

    private Credentials mCredentialsInstance;

    private void saveCredentials(Context context, Credentials credentials)
    {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AUTH_KEY, credentials.getAuthKey());
        editor.putString(PHONE, credentials.getPhoneNumber());
        editor.putString(EMAIL, credentials.getEmail());
        editor.putString(CUSTOMER_HASH, credentials.getCustomerHash());
        editor.commit();
        mCredentialsInstance = credentials;
    }

    public Credentials getUserCredentials(Context context)
    {
        if (mCredentialsInstance == null)
        {
            SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            String authKey = preferences.getString(AUTH_KEY, EMPTY);
            String phone = preferences.getString(PHONE, EMPTY);
            String email = preferences.getString(EMAIL, EMPTY);
            String customerHash = preferences.getString(CUSTOMER_HASH, EMPTY);
            String contentAuthority = preferences.getString(CONTENT_AUTHORITY, EMPTY);
            mCredentialsInstance = new Credentials(authKey, phone, email, customerHash, contentAuthority);
        }
        return mCredentialsInstance;
    }

    public Observable<Boolean> createUser(final Context context, final String authKey, final String phoneNumber, final String email)
    {
        return Observable.just(true).map(new Func1<Boolean, Boolean>()
        {
            @Override
            public Boolean call(Boolean aBoolean)
            {
                String gcmId = getGcmId(context);
                try
                {
                    Credentials credentials = new Credentials(authKey, phoneNumber, email, null, null);
                    saveCredentials(context, credentials);
                    String response = HttpIO.makeRequest(context, Api.User.Url(), Api.User.Json(phoneNumber, email, gcmId), HttpIO.Method.POST);
                    credentials = CredentialsParser.parseResponse(response, authKey, phoneNumber, email);
                    saveCredentials(context, credentials);
                } catch (IOException e)
                {
                    Log.wtf(TAG, e);
                    return false;
                } catch (AppServerException e)
                {
                    Log.wtf(TAG, e);
                    return false;
                }
                return true;
            }
        });
    }

    private String getGcmId(Context context)
    {
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        try
        {
            String regId = gcm.register(GOOGLE_PROJECT_ID);
            return regId;
        } catch (IOException e)
        {
            Log.wtf(TAG, e);
            return EMPTY;
        }
    }

    public String getChannel(Context context)
    {
        Credentials credentials = getUserCredentials(context);
        return credentials.getCustomerHash();
    }
}
