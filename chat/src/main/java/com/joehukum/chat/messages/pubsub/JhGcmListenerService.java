package com.joehukum.chat.messages.pubsub;

import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.joehukum.chat.messages.sync.SyncUtils;

import java.util.Map;

/**
 * Created by pulkitkumar on 12/04/16.
 */
public class JhGcmListenerService extends FirebaseMessagingService
{
    private static final String JH_SENDER_ID = "JhSenderID";

    private static final String SENDER_ID = "senderId";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);
        String from = remoteMessage.getFrom();
        Map data = remoteMessage.getData();
        if (remoteMessage != null && !data.isEmpty())
        {
            if (JH_SENDER_ID.equals(data.get(SENDER_ID)))
            {
                pullMessage();
            } else
            {
                return;
            }
        } else
        {
            return;
        }

    }

    private void pullMessage()
    {
        SyncUtils.TriggerRefresh(getApplicationContext());
    }

}
