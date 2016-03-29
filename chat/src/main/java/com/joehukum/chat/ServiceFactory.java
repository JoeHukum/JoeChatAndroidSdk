package com.joehukum.chat;

import com.joehukum.chat.messages.database.MessageDatabaseService;
import com.joehukum.chat.messages.metadata.MetadataService;
import com.joehukum.chat.messages.network.MessageNetworkService;
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
    private static MessageDatabaseService mMessageDatabaseService;
    private static MessageNetworkService mMessageNetworkService;
    private static MetadataService mMetadataService;

    public static MessageDatabaseService MessageDatabaseService()
    {
        synchronized (mLock)
        {
            if(mMessageDatabaseService == null)
            {
                mMessageDatabaseService = new MessageDatabaseService();
            }
        }
        return mMessageDatabaseService;
    }

    public static MessageNetworkService MessageNetworkService()
    {
        synchronized (mLock)
        {
            if (mMessageNetworkService == null)
            {
                mMessageNetworkService = new MessageNetworkService();
            }
        }
        return mMessageNetworkService;
    }

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

    public static MetadataService MetaDataService()
    {
        synchronized (mLock)
        {
            if (mMetadataService == null)
            {
                mMetadataService = new MetadataService();
            }
        }
        return mMetadataService;
    }
}
