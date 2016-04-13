package com.joehukum.chat.messages.pubsub;

import android.content.Context;
import android.util.Log;

import com.joehukum.chat.ServiceFactory;
import com.pubnub.api.Callback;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

/**
 * Created by pulkitkumar on 17/03/16.
 */
public class PubSubService
{
    private static final String TAG = PubSubService.class.getName();

    public static void subscribe(String channel, final Context context)
    {
        try
        {
            JhPubNub.getInstance(context).subscribe(channel, new Callback()
            {
                @Override
                public void successCallback(String channel, Object messageJson)
                {
                    super.successCallback(channel, messageJson);
                    Log.i(TAG, "message received");
                    if (context != null)
                    {
                        ServiceFactory.MessageDatabaseService().savePubSubMessage(context, messageJson.toString());
                    } else
                    {
                        //do nothing
                    }
                }

                @Override
                public void connectCallback(String channel, Object message)
                {
                    super.connectCallback(channel, message);
                    Log.i(TAG, "connected");
                }

                @Override
                public void reconnectCallback(String channel, Object message)
                {
                    super.reconnectCallback(channel, message);
                    Log.i(TAG, "re-connected");
                }

                @Override
                public void disconnectCallback(String channel, Object message)
                {
                    super.disconnectCallback(channel, message);
                    Log.e(TAG, "disconnected");
                }

                @Override
                public void errorCallback(String channel, PubnubError error)
                {
                    super.errorCallback(channel, error);
                    Log.e(TAG, "error callback pubnub");
                }
            });
        } catch (PubnubException e)
        {
            Log.wtf(TAG, e);
        }
    }

    public static void unSubscribe(Context context, String channel)
    {
        JhPubNub.getInstance(context).unsubscribe(channel);
    }
}
