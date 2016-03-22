package com.joehukum.chat.messages.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.joehukum.chat.messages.objects.Message;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;


/**
 * Created by pulkitkumar on 17/03/16.
 */
public class MessageDatabaseService
{

    public static final int NONE = 0;
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
                return null;
            }
        });
    }

    public Observable<Boolean> updateHash(final Context context, @NonNull final String hash, final long id)
    {
        return Observable.just(true).map(new Func1<Boolean, Boolean>()
        {
            @Override
            public Boolean call(Boolean aBoolean)
            {
                ContentValues contentValues = TableMessage.hashContentValue(hash);
                int count = context.getContentResolver().update(
                        MessageProvider.MESSAGE_URI, contentValues,
                        TableMessage.whereId(), new String[]{String.valueOf(id)});
                return (count > NONE);
            }
        });
    }

    public Observable<Void> markAllRead(final Context context)
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

    public Observable<Message> getTicketForId(final Context context, final long id)
    {
        return Observable.just(context).map(new Func1<Context, Message>()
        {
            @Override
            public Message call(Context context)
            {
                Cursor cursor = null;
                try
                {
                    cursor = context.getContentResolver().query(MessageProvider.MESSAGE_URI, null,
                            TableMessage.whereId(), new String[]{String.valueOf(id)}, null);
                    List<Message> messages = TableMessage.getMessage(cursor);
                    Message message = null;
                    if (messages != null && !messages.isEmpty())
                    {
                        message = messages.get(0);
                    }
                    return message;
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

    public Observable<Message> getTicketForHash(final Context context, final String hash)
    {
        return Observable.just(true).map(new Func1<Boolean, Message>()
        {
            @Nullable
            @Override
            public Message call(Boolean aBoolean)
            {
                Cursor cursor = null;
                try
                {
                    cursor = context.getContentResolver().query(MessageProvider.MESSAGE_URI, null,
                            TableMessage.whereHash(), new String[]{hash}, null);
                    List<Message> messages = TableMessage.getMessage(cursor);
                    Message message = null;
                    if (messages != null && !messages.isEmpty())
                    {
                        message = messages.get(0);
                    }
                    return message;
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

    public Observable<List<Message>> getUnreadMessages(final Context context)
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
        });
    }

    public Observable<List<Message>> getAllMessage(final Context context)
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
                    cursor = context.getContentResolver().query(MessageProvider.MESSAGE_URI, null,
                            null, null, TableMessage.COLUMN_TIME + " desc");
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
}
