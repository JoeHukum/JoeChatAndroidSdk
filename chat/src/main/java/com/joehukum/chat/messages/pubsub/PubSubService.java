package com.joehukum.chat.messages.pubsub;

import android.content.Context;
import android.content.SharedPreferences;
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

    private static final String SCREEN_OPEN = "chatActive";
    private static final String SCREEN_PREFERENCES = "screenPreferences";

    public static void subscribe(String channel, final Context context)
    {
        try
        {
            chatActive(context);
            JhPubNub.getInstance(context).subscribe(channel, new Callback()
            {
                @Override
                public void successCallback(String channel, Object messageJson)
                {
                    super.successCallback(channel, messageJson);
                    Log.i(TAG, "message received");
                    Log.d(TAG, messageJson.toString());
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
        chatInactive(context);
    }

    public static void chatActive(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(SCREEN_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(SCREEN_OPEN, true);
        editor.commit();
    }

    public static void chatInactive(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(SCREEN_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(SCREEN_OPEN, true);
        editor.commit();
    }

    public static boolean isChatActive(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(SCREEN_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getBoolean(SCREEN_OPEN, false);
    }
}
