package com.joehukum.chat.messages.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.joehukum.chat.messages.objects.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pulkitkumar on 29/03/16.
 */
public class MessageParser
{
    private static final String TAG = MessageParser.class.getName();

    private static final String MESSAGE_JSON_KEY = "ticketMessage";
    private static final String MESSAGE_HASH = "ticketMessageHash";
    private static final String MSG_PUB_NUB_JSON_KEY = "tcktMsg";
    private static final String MESSAGE = "message";
    private static final String CREATE_DATE = "createDate";
    private static final String TIME_PATTERN = "dd MMM yyyy HH:mm:ss";
    private static final String MESSAGE_CONTENT_TYPE = "messageContentType";
    private static final String CONTENT_TYPE_TEXT = "text";
    private static final String AUTHOR = "author";
    public static final String TICKETS = "apiTickets";
    public static final String FSYNC_MESSAGES = "apiTicketMessages";

    @Nullable
    public static List<Message> parseMessages(String response)
    {
        try
        {
            List<Message> messages = new ArrayList<>();
            JSONObject ticket = new JSONObject(response);
            JSONArray array = ticket.getJSONArray(TICKETS);
            ticket = array.getJSONObject(0);
            array = ticket.getJSONArray(FSYNC_MESSAGES);
            for (int i = 0; i< array.length(); i++)
            {
                Message message = parseMessage(array.getJSONObject(i));
                messages.add(message);
            }
            return messages;
        } catch (JSONException e)
        {
            Log.wtf(TAG, e);
            return null;
        }
    }

    @Nullable
    public static String parseMessageHash(@Nullable  String response)
    {
        try
        {
            JSONObject object = new JSONObject(response);
            if (object.has(MESSAGE_JSON_KEY))
            {
                JSONObject messageJson = object.getJSONObject(MESSAGE_JSON_KEY);
                if (messageJson.has(MESSAGE_HASH))
                {
                    return messageJson.getString(MESSAGE_HASH);
                } else
                {
                    return null;
                }
            } else
            {
                return null;
            }
        } catch (JSONException e)
        {
            Log.wtf(TAG, e);
            return null;
        }
    }

    @Nullable
    public static Message parseMessagesPubNub(@Nullable String json)
    {
        try
        {
            JSONObject object = new JSONObject(json);
            if (object.has(MSG_PUB_NUB_JSON_KEY))
            {
                JSONObject messageJson  = object.getJSONObject(MSG_PUB_NUB_JSON_KEY);
                Message message = parseMessage(messageJson);
                return message;
            } else
            {
                return null;
            }
        } catch (JSONException e)
        {
            Log.wtf(TAG, e);
            return null;
        }
    }

    @NonNull
    private static Message parseMessage(JSONObject messageJson) throws JSONException
    {
        Message message = new Message();
        if (messageJson.has(MESSAGE))
        {
            message.setContent(messageJson.getString(MESSAGE));
        }
        if (messageJson.has(CREATE_DATE))
        {
            String time = messageJson.getString(CREATE_DATE);
            message.setTime(parseDate(time));
        }
        if (messageJson.has(MESSAGE_HASH))
        {
            message.setMessageHash(messageJson.getString(MESSAGE_HASH));
        }
        if (messageJson.has(MESSAGE_CONTENT_TYPE))
        {
            message.setContentType(getContentType(messageJson.getString(MESSAGE_CONTENT_TYPE)));
        }
        if (message.getContentType() != null)
        {
            switch (message.getContentType())
            {
                default:
                    break;
            }
        } //todo : message response type missing
        if (messageJson.has(AUTHOR))
        {
            message.setAuthor(messageJson.getString(AUTHOR));
        }
        message.setIsRead(true);
        message.setType(Message.Type.RECEIVED);
        return message;
    }

    private static Message.ContentType getContentType(String messageContentType)
    {
        if (TextUtils.isEmpty(messageContentType))
        {
            return Message.ContentType.TEXT;
        } else if (messageContentType.equals(CONTENT_TYPE_TEXT))
        {
            return Message.ContentType.TEXT;
        }
        return Message.ContentType.TEXT;
    }

    private static Date parseDate(String time)
    {
        SimpleDateFormat format = new SimpleDateFormat(TIME_PATTERN);
        try
        {
            return format.parse(time);
        } catch (ParseException e)
        {
            Log.wtf(TAG, e);
            return new Date();
        }
    }
}
