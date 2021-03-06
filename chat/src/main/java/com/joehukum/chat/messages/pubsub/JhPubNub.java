package com.joehukum.chat.messages.pubsub;

import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;

/**
 * Created by pulkitkumar on 17/03/16.
 */
public class JhPubNub
{
    private static final String PUSHER_API_KEY = "69947de7ae6620e02a34"; //todo : sync from customer update
    private static final String CLUSTER_CODE = "ap1";

    private static final Object mLock = new Object();
    private static Pusher instance;

    protected static Pusher getInstance()
    {
        synchronized (mLock)
        {
            if (instance == null)
            {
                PusherOptions options = new PusherOptions();
                options.setCluster(CLUSTER_CODE);
                instance = new Pusher(PUSHER_API_KEY, options);
                instance.connect();
            }
        }
        return instance;
    }
}
