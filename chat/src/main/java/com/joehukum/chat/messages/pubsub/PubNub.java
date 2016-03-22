package com.joehukum.chat.messages.pubsub;

import com.pubnub.api.Pubnub;

/**
 * Created by pulkitkumar on 17/03/16.
 */
public class PubNub
{
    private static final String PUBLISH_KEY = "pub-c-fa5144c8-8c96-46a1-9b96-af84a8993ebe";
    private static final String SUBSCRIBE_KEY = "sub-c-f01ca44a-53a1-11e5-9028-02ee2ddab7fe";

    private static Object mLock = new Object();
    private static Pubnub instance;

    public static Pubnub getInstance()
    {
        synchronized (mLock)
        {
            if (instance == null)
            {
                instance = new Pubnub(PUBLISH_KEY, SUBSCRIBE_KEY);
            }
        }
        return instance;
    }
}
