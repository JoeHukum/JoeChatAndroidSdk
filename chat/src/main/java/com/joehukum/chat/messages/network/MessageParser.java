package com.joehukum.chat.messages.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.joehukum.chat.messages.objects.DateMetaData;
import com.joehukum.chat.messages.objects.Message;
import com.joehukum.chat.messages.objects.Option;

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
    private static final String MESSAGE_HASH = "tcktMsgHsh";
    private static final String MSG_PUB_NUB_JSON_KEY = "tcktMsg";
    private static final String MESSAGE = "message";
    private static final String CREATE_DATE = "createDate";
    private static final String TIME_PATTERN = "dd MMM yyyy HH:mm:ss";
    private static final String MESSAGE_CONTENT_TYPE = "messageContentType";
    private static final String CONTENT_TYPE_TEXT = "text";
    private static final String AUTHOR = "author";
    private static final String TICKETS = "apiTickets";
    private static final String FSYNC_MESSAGES = "apiTicketMessages";
    private static final String PUBLIC_NOTE = "publicNote";
    private static final String CUSTOMER = "Customer";
    private static final String MESSAGE_RESPONSE_TYPE = "replyContentType";
    private static final String TICKET_MESSAGE_OPTIONS = "ticketMessageOptions";
    private static final String TMO_DISPLAY_TEXT = "displayText";
    private static final String TMO_ID = "id";
    private static final String DATE_META_DATA = "mtdt";
    private static final String FROM = "frm";
    private static final String TO = "to";

    @Nullable
    public static List<Message> parseMessages(String response)
    {
        try
        {
            List<Message> messages = new ArrayList<>();
            JSONObject ticket = new JSONObject(response);
            if (ticket.has(TICKETS))
            {
                JSONArray array = ticket.getJSONArray(TICKETS);
                ticket = array.getJSONObject(0);
                array = ticket.getJSONArray(FSYNC_MESSAGES);
                for (int i = 0; i< array.length(); i++)
                {
                    Message message = parseMessage(array.getJSONObject(i));
                    if (message.isPublicNote() && !CUSTOMER.equals(message.getAuthor()))
                    {
                        messages.add(message);
                    }
                }
                return messages;
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
                if (message.isPublicNote() && !CUSTOMER.equals(message.getAuthor()))
                {
                    return message;
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
        if (messageJson.has(MESSAGE_RESPONSE_TYPE))
        {
            message.setResponseType(getResponseType(messageJson.getString(MESSAGE_RESPONSE_TYPE)));
            if (message.getResponseType() == Message.ResponseType.SEARCH_OPTION)
            {
                if (messageJson.has(TICKET_MESSAGE_OPTIONS))
                {
                    List<Option> options = parseOptions(messageJson.getJSONArray(TICKET_MESSAGE_OPTIONS));
                    message.setMetadata(options);
                }
            } else if (message.getResponseType() == Message.ResponseType.DATE)
            {
                if (messageJson.has(DATE_META_DATA))
                {
                    DateMetaData dateMetaData = parseDateMetadata(messageJson.getJSONObject(DATE_META_DATA));
                    message.setMetadata(dateMetaData);
                }
            }
        }
        if (messageJson.has(AUTHOR))
        {
            message.setAuthor(messageJson.getString(AUTHOR));
        }
        if (messageJson.has(PUBLIC_NOTE))
        {
            message.setPublicNote(messageJson.getBoolean(PUBLIC_NOTE));
        }
        message.setIsRead(true);
        message.setType(Message.Type.RECEIVED);
        return message;
    }

    private static DateMetaData parseDateMetadata(JSONObject jsonObject) throws JSONException
    {
        Date start = null, end = null;
        if (jsonObject.has(FROM))
        {
            start = parseDate(jsonObject.getString(FROM));
        }
        if (jsonObject.has(TO))
        {
            end = parseDate(jsonObject.getString(TO));
        }
        return new DateMetaData(start, end);
    }

    private static List<Option> parseOptions(JSONArray jsonArray) throws JSONException
    {
        List<Option> options = new ArrayList<>();
        for (int i=0; i< jsonArray.length(); i++)
        {
            JSONObject object = jsonArray.getJSONObject(i);
            int id = object.getInt(TMO_ID);
            String displayText = object.getString(TMO_DISPLAY_TEXT);
            options.add(new Option(String.valueOf(id), displayText));
        }
        return options;
    }

    private static Message.ResponseType getResponseType(String string)
    {
        if ("opt".equals(string))
        {
            return Message.ResponseType.SEARCH_OPTION;
        } else if ("cntNum".equals(string))
        {
            return Message.ResponseType.INT;
        } else if ("date".equals(string))
        {
            return Message.ResponseType.DATE;
        } else
        {
            return Message.ResponseType.TEXT;
        }
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
