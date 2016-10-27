package com.joehukum.chat.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.joehukum.chat.ServiceFactory;
import com.joehukum.chat.messages.network.Api;
import com.joehukum.chat.messages.network.HttpIO;
import com.joehukum.chat.messages.network.exceptions.AppServerException;

import java.io.IOException;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by pulkitkumar on 19/03/16.
 */
public class CredentialsService
{
    private static final String TAG = CredentialsService.class.getName();

    private static final String PREFERENCES = "userPreferences";

    private static final String AUTH_KEY = "authKey";
    private static final String PHONE = "phone";
    private static final String EMAIL = "email";
    private static final String CUSTOMER_HASH = "customerHash";
    private static final String USER_PARAMS = "userParams";
    private static final String TICKET_HASH = "ticketHash";

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
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        if (mCredentialsInstance == null)
        {
            String authKey = preferences.getString(AUTH_KEY, EMPTY);
            String phone = preferences.getString(PHONE, EMPTY);
            String email = preferences.getString(EMAIL, EMPTY);
            String customerHash = preferences.getString(CUSTOMER_HASH, EMPTY);
            mCredentialsInstance = new Credentials(authKey, phone, email, customerHash);
        }
        mCredentialsInstance.setParamString(readParamString(context));
        return mCredentialsInstance;
    }

    public Observable<Boolean> createUser(final Context context, final String authKey, final String phoneNumber, final String email)
    {
        return Observable.just(true).map(new Func1<Boolean, Boolean>()
        {
            @Override
            public Boolean call(Boolean aBoolean)
            {
                Credentials credentials = getUserCredentials(context);
                credentials.setAuthKey(authKey);
                credentials.setPhoneNumber(phoneNumber);
                credentials.setEmail(email);
                saveCredentials(context, credentials);
                credentials.setParamString(readParamString(context));
                return uploadCredentials(context, credentials);
            }
        });
    }

    @NonNull
    private Boolean uploadCredentials(Context context, Credentials credentials)
    {
        try
        {
            String response = HttpIO.makeRequest(context, Api.User.Url(), Api.User.Json(credentials.getCustomerHash(), credentials.getPhoneNumber(),
                    credentials.getEmail(), getFirebaseToken(), credentials.getParamString()), HttpIO.Method.POST);
            credentials = CredentialsParser.parseResponse(response, credentials.getAuthKey(), credentials.getPhoneNumber(), credentials.getEmail());
            credentials.setParamString(readParamString(context));
            saveCredentials(context, credentials);
        } catch (AppServerException e)
        {
            Log.wtf(TAG, e);
            return false;
        } catch (IOException e)
        {
            Log.wtf(TAG, e);
            return false;
        }
        return true;
    }

    private String getFirebaseToken()
    {
        String firebaseToken = FirebaseInstanceId.getInstance().getToken();
        return firebaseToken;
    }

    public String getChannel(Context context)
    {
        Credentials credentials = getUserCredentials(context);
        return credentials.getCustomerHash();
    }

    public void updateFirebaseToken(Context context)
    {
        Credentials credentials = getUserCredentials(context);
        uploadCredentials(context, credentials);
    }

    public void updateParams(Context context, Map<String, String> params)
    {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        preferences.edit().putString(USER_PARAMS, getParamString(params)).commit();
    }

    private String readParamString(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getString(USER_PARAMS, EMPTY);
    }

    private String getParamString(Map<String, String> params)
    {
        StringBuilder builder = new StringBuilder();
        if (params.size() > 0)
        {
            for (Map.Entry entry: params.entrySet())
            {
                builder.append(entry.getKey()).append(":").append(entry.getValue());
                builder.append(",");
            }
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public void saveTicketHash(Context context, String ticketHash)
    {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TICKET_HASH, ticketHash);
        editor.commit();
    }

    public String getTicketHash(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getString(TICKET_HASH, EMPTY);
    }
}
