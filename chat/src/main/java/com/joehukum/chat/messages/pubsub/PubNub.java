package com.joehukum.chat.messages.pubsub;

import android.content.Context;

import com.joehukum.chat.ServiceFactory;
import com.joehukum.chat.user.Credentials;
import com.pubnub.api.Pubnub;

/**
 * Created by pulkitkumar on 17/03/16.
 */
public class PubNub
{

    private static Object mLock = new Object();
    private static Pubnub instance;

    public static Pubnub getInstance(Context context)
    {
        synchronized (mLock)
        {
            if (instance == null)
            {
                Credentials credentials = ServiceFactory.CredentialsService().getUserCredentials(context);
                instance = new Pubnub(credentials.getPubNubPublishKey(), credentials.getPubNubSubscribeKey());
            }
        }
        return instance;
    }
}
