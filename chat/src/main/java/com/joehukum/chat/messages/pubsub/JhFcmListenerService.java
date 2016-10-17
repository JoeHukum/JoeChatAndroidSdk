package com.joehukum.chat.messages.pubsub;

import android.os.Bundle;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.joehukum.chat.messages.sync.SyncUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pulkitkumar on 12/04/16.
 */
public class JhFcmListenerService extends FirebaseMessagingService
{
    private static final String JH_SENDER_ID = "JhSenderID";

    private static final String SENDER_ID = "senderId";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);
        String from = remoteMessage.getFrom();
        Map data = remoteMessage.getData();
        onMessageReceived(from, data);
    }

    /**
     * Retrofitting GCM listener service.
     */
    public void onMessageReceived(String from, Bundle data)
    {
        Map map = new HashMap();
        for (String key : data.keySet())
        {
            map.put(key, data.get(key));
        }
        onMessageReceived(from, map);
    }

    private void onMessageReceived(String from, Map data)
    {
        if (!data.isEmpty() && "joe".equalsIgnoreCase(from))
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
