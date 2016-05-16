package com.joehukum.chat.messages.pubsub;

import android.content.Context;

import com.joehukum.chat.ServiceFactory;
import com.joehukum.chat.user.Credentials;
import com.pusher.client.Pusher;

/**
 * Created by pulkitkumar on 17/03/16.
 */
public class JhPubNub
{
    private static final String PUSHER_API_KEY = "";

    private static Object mLock = new Object();
    private static Pusher instance;

    protected static Pusher getInstance(Context context)
    {
        synchronized (mLock)
        {
            if (instance == null)
            {
                Credentials credentials = ServiceFactory.CredentialsService().getUserCredentials(context);
                instance = new Pusher(PUSHER_API_KEY);
                instance.connect();
            }
        }
        return instance;
    }
}
