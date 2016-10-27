package com.joehukum.chat.messages.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.joehukum.chat.ServiceFactory;
import com.joehukum.chat.messages.network.exceptions.AppServerException;
import com.joehukum.chat.messages.objects.Message;
import com.joehukum.chat.user.Credentials;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by pulkitkumar on 17/03/16.
 */
public class MessageNetworkService
{
    public static final String START_OVER_TEXT = "/startOver";

    public boolean initChat(Context context)
    {
        Credentials credentials = ServiceFactory.CredentialsService().getUserCredentials(context.getApplicationContext());
        try
        {
            String response = HttpIO.makeRequest(context, Api.Chat.Url(credentials.getCustomerHash()), null, HttpIO.Method.GET);
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }

    public Observable<Boolean> sendFeedback(Context context, final String feedback, final float rating)
    {
        return Observable.just(context).map(new Func1<Context, Boolean>()
        {
            @Override
            public Boolean call(Context context)
            {
                try
                {
                    String ticketHash = ServiceFactory.CredentialsService().getTicketHash(context);
                    String response = HttpIO.makeRequest(context, Api.Feedback.Url(), Api.Feedback.Json(feedback, rating, ticketHash) , HttpIO.Method.POST);
                    return true;
                } catch (AppServerException e)
                {
                    return false;
                } catch (IOException e)
                {
                    return false;
                }
            }
        });
    }

    public boolean uploadMessage(Context context, @NonNull Message message) throws AppServerException, IOException
    {
        Credentials credentials = ServiceFactory.CredentialsService().getUserCredentials(context);
        String customerHash = credentials.getCustomerHash();
        String content = getContent(message);
        String response = HttpIO.makeRequest(context, Api.Message.Url(), Api.Message.Json(customerHash,
                content, getContentTypeString(message)), HttpIO.Method.POST);
        String messageHash = MessageParser.parseMessageHash(response);
        ServiceFactory.MessageDatabaseService().updateHash(context, messageHash, message.getId());
        return true;
    }

    private String getContentTypeString(Message message)
    {
        if (message.getContentType() == Message.ContentType.IMAGE)
        {
            return "img";
        } else
        {
            return "";
        }
    }

    private String getContent(Message message)
    {
        if (message.getContentType() == Message.ContentType.OPTION)
        {
            if (message.getMetadata() != null)
            {
                return message.getMetadata().toString();
            } else
            {
                return message.getContent();
            }
        } else
        {
            return message.getContent();
        }
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
                ServiceFactory.CredentialsService().saveTicketHash(context, message.getTicketHash());
            }
        }
    }
}
