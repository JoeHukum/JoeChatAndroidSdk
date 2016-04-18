package com.joehukum.chat.messages.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.joehukum.chat.ServiceFactory;
import com.joehukum.chat.messages.network.exceptions.AppServerException;
import com.joehukum.chat.messages.objects.Message;
import com.joehukum.chat.user.Credentials;

import java.io.IOException;
import java.util.List;

/**
 * Created by pulkitkumar on 17/03/16.
 */
public class MessageNetworkService
{
    public boolean uploadMessage(Context context, @NonNull Message message) throws AppServerException, IOException
    {
        Credentials credentials = ServiceFactory.CredentialsService().getUserCredentials(context);
        String customerHash = credentials.getCustomerHash();
        String response = HttpIO.makeRequest(context, Api.Message.Url(), Api.Message.Json(customerHash, message.getContent()), HttpIO.Method.POST);
        String messageHash = MessageParser.parseMessageHash(response);
        ServiceFactory.MessageDatabaseService().updateHash(context, messageHash, message.getId());
        return true;
    }

    public void pullMessages(Context context, String latestHash) throws IOException, AppServerException
    {
        Credentials credentials = ServiceFactory.CredentialsService().getUserCredentials(context);
        String customerHash = credentials.getCustomerHash();
        String url = Api.Sync.Url(customerHash, latestHash);
        String response = HttpIO.makeRequest(context, url, null, HttpIO.Method.GET);
        List<Message> messages = MessageParser.parseMessages(response);
        if (messages != null)
        {
            for (Message message: messages)
            {
                ServiceFactory.MessageDatabaseService().addMessage(context, message);
            }
        }
    }
}
