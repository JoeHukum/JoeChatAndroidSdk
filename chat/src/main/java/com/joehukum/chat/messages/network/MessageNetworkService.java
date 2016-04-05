package com.joehukum.chat.messages.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.joehukum.chat.ServiceFactory;
import com.joehukum.chat.messages.network.exceptions.AppServerException;
import com.joehukum.chat.messages.objects.Message;

import java.io.IOException;
import java.util.List;

/**
 * Created by pulkitkumar on 17/03/16.
 */
public class MessageNetworkService
{
    public boolean uploadMessage(@NonNull Message message) throws AppServerException, IOException
    {
        return true;
    }

    public void saveMessage(@Nullable Context context, @NonNull String messageJson)
    {
        Message message = MessageParser.parseMessage(messageJson);
        ServiceFactory.MessageDatabaseService().addMessage(context, message);
    }

    public void pullMessages(Context context, String latestHash)
    {
        //String json = HttpIO.makeRequest();
        List<Message> messages = MessageParser.parseMessages("");
        if (messages != null)
        {
            for (Message m: messages)
            {
                ServiceFactory.MessageDatabaseService().addMessage(context, m);
            }
        }
    }
}
