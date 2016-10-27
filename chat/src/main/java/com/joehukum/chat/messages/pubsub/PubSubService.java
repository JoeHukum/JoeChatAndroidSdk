package com.joehukum.chat.messages.pubsub;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.joehukum.chat.ServiceFactory;
import com.joehukum.chat.messages.sync.SyncUtils;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action;
import rx.functions.Func1;

/**
 * Created by pulkitkumar on 17/03/16.
 */
public class PubSubService
{
    private static final String TAG = PubSubService.class.getName();

    private static final String SCREEN_OPEN = "chatActive";
    private static final String SCREEN_PREFERENCES = "screenPreferences";
    private static final String MESSAGE_EVENT = "MessageOnTicket";

    public void subscribe(String channelName, final Context context)
    {
        chatActive(context);
        SyncUtils.TriggerRefresh(context.getApplicationContext());
        Channel channel = JhPubNub.getInstance().subscribe(channelName);

        channel.bind(MESSAGE_EVENT, new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, final String data)
            {
                Log.i(TAG, "message received");
                Log.i(TAG, data);
                ServiceFactory.MessageDatabaseService().savePubSubMessage(context, data);
            }
        });
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                initChat(context);
            }
        }, 2000);

    }

    public void initChat(final Context context)
    {
        Observable.just(true).map(new Func1<Boolean, Boolean>()
        {
            @Override
            public Boolean call(Boolean aBoolean)
            {
                ServiceFactory.MessageNetworkService().initChat(context);
                return true;
            }
        }).subscribe(new Subscriber<Boolean>()
        {
            @Override
            public void onCompleted()
            {

            }

            @Override
            public void onError(Throwable e)
            {

            }

            @Override
            public void onNext(Boolean aBoolean)
            {

            }
        });
    }

    public void unSubscribe(Context context, String channel)
    {
        JhPubNub.getInstance().unsubscribe(channel);
        chatInactive(context);
    }

    private static void chatActive(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(SCREEN_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(SCREEN_OPEN, true);
        editor.commit();
    }

    private void chatInactive(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(SCREEN_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(SCREEN_OPEN, true);
        editor.commit();
    }

    public boolean isChatActive(Context context)
    {
        SharedPreferences preferences = context.getSharedPreferences(SCREEN_PREFERENCES, Context.MODE_PRIVATE);
        return preferences.getBoolean(SCREEN_OPEN, false);
    }
}
