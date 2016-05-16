package com.joehukum.chat.user;

import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pulkitkumar on 05/04/16.
 */
public class CredentialsParser
{
    private static final String TAG = CredentialsParser.class.getName();
    private static final String EMPTY = "";
    public static final String CUSTOMER_DETAIL = "customerDetail";
    public static final String CUSTOMER_HASH = "customer_hash";
    public static final String CONTENT_AUTHORITY = "androidPackageName";

    @Nullable
    public static Credentials parseResponse(String response, String authKey, String phoneNumber, String email)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(response);
            String customerHash = EMPTY, contentAuthority = EMPTY;
            if (jsonObject.has(CONTENT_AUTHORITY))
            {
                contentAuthority = jsonObject.getString(CONTENT_AUTHORITY);
            }
            if (jsonObject.has(CUSTOMER_DETAIL))
            {
                JSONObject customerJson = jsonObject.getJSONObject(CUSTOMER_DETAIL);
                if (customerJson.has(CUSTOMER_HASH))
                {
                    customerHash = customerJson.getString(CUSTOMER_HASH);
                }
            }
            Credentials credentials = new Credentials(authKey, phoneNumber, email, customerHash, contentAuthority);
            return credentials;
        } catch (JSONException e)
        {
            Log.wtf(TAG, e);
            return null;
        }
    }
}
