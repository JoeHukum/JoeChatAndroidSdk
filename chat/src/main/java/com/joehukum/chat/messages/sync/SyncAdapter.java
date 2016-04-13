package com.joehukum.chat.messages.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.joehukum.chat.ServiceFactory;
import com.joehukum.chat.messages.network.exceptions.AppServerException;
import com.joehukum.chat.messages.objects.Message;

import java.io.IOException;
import java.util.List;

/**
 * Created by pulkitkumar on 21/03/16.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter
{
    private static final String TAG = SyncAdapter.class.getName();

    public SyncAdapter(Context context, boolean autoInitialize)
    {
        this(context, autoInitialize, false);
    }

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs)
    {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult)
    {
        try
        {
            uploadUnsyncedMessages();
            pullMessages();
        } catch (AppServerException e)
        {
            Log.wtf(TAG, e);
        } catch (IOException e)
        {
            Log.wtf(TAG, e);
        }
    }

    private void uploadUnsyncedMessages() throws AppServerException, IOException
    {
        List<Message> messages = ServiceFactory.MessageDatabaseService().getUnSyncedMessages(getContext());
        if (messages != null)
        {
            for (Message message: messages)
            {
                ServiceFactory.MessageNetworkService().uploadMessage(getContext(), message);
            }
        }
    }

    private void pullMessages() throws AppServerException, IOException
    {
        Message message = ServiceFactory.MessageDatabaseService().getLatestMessage(getContext());
        String latestHash = message == null? null : message.getMessageHash();
        ServiceFactory.MessageNetworkService().pullMessages(getContext(), latestHash);
    }

    private void showUnreadNotification()
    {

    }
}
