package com.joehukum.chat.messages.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.joehukum.chat.ServiceFactory;
import com.joehukum.chat.messages.network.MessageParser;
import com.joehukum.chat.messages.objects.DateMetaData;
import com.joehukum.chat.messages.objects.Message;
import com.joehukum.chat.messages.objects.Option;
import com.joehukum.chat.messages.sync.SyncUtils;

import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;


/**
 * Created by pulkitkumar on 17/03/16.
 */
public class MessageDatabaseService
{
    private static final int NONE = 0;

    private static final String TAG = MessageDatabaseService.class.getName();

    public Observable<Boolean> addMessage(final Context context, @NonNull final Message message)
    {
        return Observable.just(true).map(new Func1<Boolean, Boolean>()
        {
            @Override
            public Boolean call(Boolean aBoolean)
            {
                ContentValues values = TableMessage.buildContentValues(message);
                context.getContentResolver().insert(MessageProvider.MESSAGE_URI, values);
                SyncUtils.TriggerRefresh(context);
                return null;
            }
        });
    }

    public Boolean updateHash(final Context context, @NonNull final String hash, final long id)
    {
        ContentValues contentValues = TableMessage.hashContentValue(hash);
        int count = context.getContentResolver().update(
                MessageProvider.MESSAGE_URI, contentValues,
                TableMessage.whereId(), new String[]{String.valueOf(id)});
        return (count > NONE);
    }

    private Observable<Void> markAllRead(final Context context)
    {
        return Observable.just(true).map(new Func1<Boolean, Void>()
        {
            @Override
            public Void call(Boolean aBoolean)
            {
                ContentValues contentValues = TableMessage.readContentValue();
                context.getContentResolver().update(MessageProvider.MESSAGE_URI, contentValues, null, null);
                return null;
            }
        });
    }

    public List<Message> getUnreadMessages(final Context context)
    {
        Cursor cursor = null;
        try
        {
            cursor = context.getContentResolver().query(MessageProvider.MESSAGE_URI, null,
                    TableMessage.whereRead(), new String[]{String.valueOf(false)},
                    TableMessage.COLUMN_TIME + " desc");
            List<Message> messages = TableMessage.getMessage(cursor);
            return messages;
        } catch (SQLException e)
        {
            Log.wtf(TAG, e);
            return null;
        } finally
        {
            DbUtils.closeCursor(cursor);
        }
    }

    public Observable<List<Message>> getMessages(final Context context)
    {
        return Observable.just(context).map(new Func1<Context, List<Message>>()
        {
            @Nullable
            @Override
            public List<Message> call(Context context)
            {
                Cursor cursor = null;
                try
                {
                    cursor = context.getContentResolver().query(MessageProvider.MESSAGE_URI, null, null
                            , null, TableMessage.COLUMN_ID + " asc");
                    markAllRead(context);
                    List<Message> messages = TableMessage.getMessage(cursor);
                    return messages;
                } catch (SQLException e)
                {
                    Log.wtf(TAG, e);
                    return null;
                } finally
                {
                    DbUtils.closeCursor(cursor);
                }
            }
        });
    }

    @Nullable
    public List<Message> getUnSyncedMessages(Context context)
    {
        Cursor cursor = null;
        try
        {
            cursor = context.getContentResolver().query(MessageProvider.MESSAGE_URI, null,
                    TableMessage.whereHashIsNull(), null, TableMessage.COLUMN_TIME + " asc");
            List<Message> messages = TableMessage.getMessage(cursor);
            return messages;
        } catch (SQLException e)
        {
            Log.wtf(TAG, e);
            return null;
        } finally
        {
            DbUtils.closeCursor(cursor);
        }
    }

    public Message getLatestMessage(Context context)
    {
        Cursor cursor = null;
        try
        {
            cursor = context.getContentResolver().query(MessageProvider.MESSAGE_URI, null, TableMessage.whereHashNotNullAndType(),
                    new String[]{Message.Type.RECEIVED.getName()}, TableMessage.COLUMN_TIME + " desc");
            List<Message> messages = TableMessage.getMessage(cursor);
            if (messages != null && ! messages.isEmpty())
            {
                return messages.get(0);
            } else
            {
                return null;
            }
        } catch (SQLException e)
        {
            Log.wtf(TAG, e);
            return null;
        } finally
        {
            DbUtils.closeCursor(cursor);
        }
    }

    public void savePubSubMessage(@NonNull Context context, @NonNull String json)
    {
        Message message = MessageParser.parseMessagesPubNub(json);
        if (message != null)
        {
            if (message.getMetadata() != null)
            {
                saveMessageMetadata(message);
            }
            ContentValues values = TableMessage.buildContentValues(message);
            context.getContentResolver().insert(MessageProvider.MESSAGE_URI, values);
        }
    }

    private void saveMessageMetadata(Message message)
    {
        if (message.getResponseType() != null && message.getResponseType() == Message.ResponseType.SEARCH_OPTION)
        {
            ServiceFactory.MetaDataService().saveOptions(message.getMessageHash(), (List<Option>) message.getMetadata());
        } else if (message.getResponseType() != null && message.getResponseType() == Message.ResponseType.DATE)
        {
            ServiceFactory.MetaDataService().saveDateMetadata(message.getMessageHash(), (DateMetaData) message.getMetadata());
        }
    }

    private Message getGenericMessage()
    {
        Message message = new Message();
        message.setTime(new Date());
        message.setType(Message.Type.SENT);
        message.setContentType(Message.ContentType.TEXT);
        return message;
    }

    public Message generateTextMessage(String input)
    {
        Message message = getGenericMessage();
        message.setContent(input);
        return message;
    }

    public Message generateOptionMessage(Option option)
    {
        Message message = getGenericMessage();
        message.setContentType(Message.ContentType.OPTION);
        message.setContent(option.getDisplayText());
        message.setMetadata(option.getId());
        return message;
    }
}
