package com.joehukum.chat;

import android.content.Context;
import android.content.Intent;

import com.anupcowkur.reservoir.Reservoir;
import com.joehukum.chat.messages.sync.SyncUtils;
import com.joehukum.chat.ui.activities.ChatActivity;

/**
 * Created by pulkitkumar on 17/03/16.
 */
public class JoeHukum
{
    public static void chat(Context context, String authKey, String channel, String clientUserIdentifier, String phoneNumber)
    {
        ServiceFactory.CredentialsService().saveUserCredentials(context, authKey, clientUserIdentifier, phoneNumber);
        if (!SyncUtils.isPrefSetupComplete(context))
        {
            SyncUtils.CreateSyncAccount(context);
        }
        Intent intent = ChatActivity.getIntent(context, channel);
        context.startActivity(intent);
    }

    public static void init(Context context)
    {
        initReservoir(context);
    }

    public static void initReservoir(Context context)
    {
        try
        {
            Reservoir.init(context, 32768); //in bytes
        } catch (Exception e)
        {
            //failure
        }
    }
}
