package com.joehukum.chat.messages.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.joehukum.chat.messages.objects.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pulkitkumar on 17/03/16.
 */
public class TableMessage
{
    private static final String TAG = TableMessage.class.getName();

    public static final String TABLE_NAME = "message";
    public static final String COLUMN_ID = "id";
    private static final String COLUMN_HASH = "message_hash";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_CONTENT = "content";
    private static final String COlUMN_CONTENT_TYPE = "content_type";
    private static final String COLUMN_RESPONSE_TYPE = "response_type";
    public static final String COLUMN_TIME = "date";
    private static final String COLUMN_AUTHOR = "author";
    private static final String COLUMN_IS_READ = "is_read";
    private static final String COLUMN_METADATA = "metadata";
    private static final String COLUMN_REF_MESSAGE_HASH = "ref_message_hash";

    public static void onCreate(@NonNull SQLiteDatabase db)
    {
        StringBuilder builder = new StringBuilder("Create table ").append(TABLE_NAME).append(" ( ")
                .append(COLUMN_ID).append(" integer primary key autoincrement, ")
                .append(COLUMN_HASH).append(" text, ")
                .append(COLUMN_TYPE).append(" text, ")// not empty
                .append(COLUMN_CONTENT).append(" text, ")
                .append(COlUMN_CONTENT_TYPE).append(" text, ")// not empty
                .append(COLUMN_RESPONSE_TYPE).append(" text, ")// not empty
                .append(COLUMN_TIME).append(" long, ")// not null
                .append(COLUMN_AUTHOR).append(" text, ")
                .append(COLUMN_METADATA).append(" text, ")
                .append(COLUMN_REF_MESSAGE_HASH).append(" text, ")
                .append(COLUMN_IS_READ).append(" boolean default false);");
        Log.i(TAG, builder.toString());
        db.execSQL(builder.toString());
    }

    @Nullable
    public static List<Message> getMessage(@Nullable Cursor cursor)
    {
        if (cursor == null)
        {
            return null;
        } else
        {
            if (cursor.moveToFirst())
            {
                List<Message> messages = new ArrayList<>();
                do
                {
                    Message message = new Message();
                    message.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                    message.setMessageHash(cursor.getString(cursor.getColumnIndex(COLUMN_HASH)));
                    message.setType(Message.Type.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_TYPE))));
                    message.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)));
                    message.setContentType(Message.ContentType.valueOf(cursor.getString(cursor.getColumnIndex(COlUMN_CONTENT_TYPE))));
                    message.setResponseType(Message.ResponseType.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_RESPONSE_TYPE))));
                    message.setTime(new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_TIME))));
                    message.setAuthor(cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR)));
                    message.setMetadata(cursor.getString(cursor.getColumnIndex(COLUMN_METADATA)));
                    message.setIsRead(cursor.getInt(cursor.getColumnIndex(COLUMN_IS_READ)) > 0);
                    message.setMetadata(cursor.getString(cursor.getColumnIndex(COLUMN_METADATA)));
                    message.setReferenceMessageHash(cursor.getString(cursor.getColumnIndex(COLUMN_REF_MESSAGE_HASH)));
                    messages.add(message);
                } while (cursor.moveToNext());
                return messages;
            } else
            {
                return null;
            }
        }
    }

    @NonNull
    public static ContentValues buildContentValues(@NonNull Message message)
    {
        ContentValues contentValues = new ContentValues();
        //id auto increment.
        if (!TextUtils.isEmpty(message.getMessageHash()))
        {
            contentValues.put(COLUMN_HASH, message.getMessageHash());
        }
        if (message.getType() != null)
        {
            contentValues.put(COLUMN_TYPE, message.getType().getName());
        }
        if (!TextUtils.isEmpty(message.getContent()))
        {
            contentValues.put(COLUMN_CONTENT, message.getContent());
        }
        if (message.getContentType() != null)
        {
            contentValues.put(COlUMN_CONTENT_TYPE, message.getContentType().getName());
        }
        if (message.getResponseType() != null)
        {
            contentValues.put(COLUMN_RESPONSE_TYPE, message.getResponseType().getName());
        }
        if (message.getTime() != null)
        {
            contentValues.put(COLUMN_TIME, message.getTime().getTime());
        }
        if (!TextUtils.isEmpty(message.getAuthor()))
        {
            contentValues.put(COLUMN_AUTHOR, message.getAuthor());
        }
        if (message.isRead())
        {
            contentValues.put(COLUMN_IS_READ, message.isRead());
        }
        if (message.getMetadata() != null)
        {
            contentValues.put(COLUMN_METADATA, message.getMetadata().toString());
        }
        if (message.getReferenceMessageHash() != null)
        {
            contentValues.put(COLUMN_REF_MESSAGE_HASH, message.getReferenceMessageHash());
        }
        return contentValues;
    }

    public static String whereId()
    {
        StringBuilder builder = new StringBuilder().append(COLUMN_ID).append("=?");
        return builder.toString();
    }

    public static String whereHash()
    {
        StringBuilder builder = new StringBuilder().append(COLUMN_HASH).append("=?");
        return builder.toString();
    }

    public static String whereHashIsNull()
    {
        return new StringBuilder().append(COLUMN_HASH).append(" is null ").toString();
    }

    public static String whereRead()
    {
        StringBuilder builder = new StringBuilder().append(COLUMN_IS_READ).append("=?");
        return builder.toString();
    }

    public static ContentValues contentUpdateValue(String message)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTENT, message);
        return values;
    }

    public static ContentValues hashContentValue(String hash)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_HASH, hash);
        return values;
    }

    public static ContentValues readContentValue()
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_READ, true);
        return values;
    }

    public static String whereHashNotNullAndType()
    {
        return new StringBuilder().append(COLUMN_HASH).append(" is not null and ").append(COLUMN_TYPE).append(" = ?").toString();
    }
}
