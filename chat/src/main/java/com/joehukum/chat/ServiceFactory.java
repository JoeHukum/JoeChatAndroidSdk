package com.joehukum.chat;

import com.joehukum.chat.messages.pubsub.PubSubService;
import com.joehukum.chat.user.CredentialsService;

/**
 * Created by pulkitkumar on 17/03/16.
 */
public class ServiceFactory
{
    private static Object mLock = new Object();

    private static PubSubService mPubSubService;
    private static CredentialsService mCredentialsService;

    public static PubSubService PubSubService()
    {
        synchronized (mLock)
        {
            if (mPubSubService == null)
            {
                mPubSubService = new PubSubService();
            }
        }
        return mPubSubService;
    }

    public static CredentialsService CredentialsService()
    {
        synchronized (mLock)
        {
            if (mCredentialsService == null)
            {
                mCredentialsService = new CredentialsService();
            }
        }
        return mCredentialsService;
    }
}
