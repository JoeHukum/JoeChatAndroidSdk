package com.joehukum.chat.user;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pulkitkumar on 05/04/16.
 */
public class UserParser
{
    private static final String TAG = UserParser.class.getName();
    private static final String EMPTY = "";
    public static final String PUB_NUB_SUBSCRIBE_KEY = "pubNubSubscribeKey";
    public static final String PUB_NUB_PUBLISH_KEY = "pubNubPublishKey";
    public static final String CUSTOMER_DETAIL = "customerDetail";
    public static final String CUSTOMER_HASH = "customer_hash";
    public static final String CONTENT_AUTHORITY = "android_provider";

    @Nullable
    public static Credentials parseResponse(String response, String authKey, String phoneNumber, String email)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(response);
            String subscribeKey = EMPTY, publishKey = EMPTY, customerHash = EMPTY, contentAuthority = EMPTY;
            if (jsonObject.has(PUB_NUB_SUBSCRIBE_KEY))
            {
                subscribeKey = jsonObject.getString(PUB_NUB_SUBSCRIBE_KEY);
            }
            if (jsonObject.has(PUB_NUB_PUBLISH_KEY))
            {
                publishKey = jsonObject.getString(PUB_NUB_PUBLISH_KEY);
            }
            if (jsonObject.has(CUSTOMER_DETAIL))
            {
                JSONObject customerJson = jsonObject.getJSONObject(CUSTOMER_DETAIL);
                if (customerJson.has(CUSTOMER_HASH))
                {
                    customerHash = customerJson.getString(CUSTOMER_HASH);
                }
            }
            if (jsonObject.has(CONTENT_AUTHORITY))
            {
                contentAuthority = jsonObject.getString(CONTENT_AUTHORITY);
            }
            Credentials credentials = new Credentials(authKey, phoneNumber, email, publishKey, subscribeKey, customerHash, contentAuthority);
            return credentials;
        } catch (JSONException e)
        {
            Log.wtf(TAG, e);
            return null;
        }
    }
}
