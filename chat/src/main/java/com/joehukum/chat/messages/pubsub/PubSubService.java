package com.joehukum.chat.messages.pubsub;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.joehukum.chat.ServiceFactory;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

/**
 * Created by pulkitkumar on 17/03/16.
 */
public class PubSubService
{
    private static final String TAG = PubSubService.class.getName();

    private static final String SCREEN_OPEN = "chatActive";
    private static final String SCREEN_PREFERENCES = "screenPreferences";
    public static final String MESSAGE_EVENT = "messageEvent";

    public static void subscribe(String channelName, final Context context)
    {

        chatActive(context);
        Channel channel = JhPubNub.getInstance(context).subscribe(channelName);

        channel.bind(MESSAGE_EVENT, new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String data)
            {
                Log.i(TAG, "message received");
                Log.i(TAG, data);
                if (context != null)
                {
                    ServiceFactory.MessageDatabaseService().savePubSubMessage(context, data);
                } else
                {
                    //do nothing
                }
            }
        });
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
