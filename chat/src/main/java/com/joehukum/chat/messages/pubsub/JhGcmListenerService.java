package com.joehukum.chat.messages.pubsub;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;
import com.joehukum.chat.messages.sync.SyncUtils;

/**
 * Created by pulkitkumar on 12/04/16.
 */
public class JhGcmListenerService extends GcmListenerService
{
    private static final String JH_SENDER_ID = "JhSenderID";

    private static final String SENDER_ID = "senderId";

    @Override
    public void onMessageReceived(String from, Bundle data)
    {
        if (data != null && !data.isEmpty())
        {
            if (JH_SENDER_ID.equals(data.getString(SENDER_ID)))
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
