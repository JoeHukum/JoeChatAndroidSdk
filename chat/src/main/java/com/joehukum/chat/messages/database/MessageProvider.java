package com.joehukum.chat.messages.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by pulkitkumar on 17/03/16.
 */
public class MessageProvider extends ContentProvider
{
    private static final String TAG = MessageProvider.class.getName();

    public static Uri MESSAGE_URI;
    private static Uri BASE_CONTENT_URI;
    private Object mLock = new Object();
    private SQLiteDatabase mDatabase;
    private static UriMatcher uriMatcher;

    private static final long NONE = 0;
    private static final String JH_AUTHORITY = "com.joehukum.authority";
    private static final String IDENTIFIER_MESSAGES = "messages";
    private static final int MESSAGES = 101;

    private void init()
    {
        String contentAuthority = "";
        ApplicationInfo applicationInfo = null;
        try
        {
            applicationInfo = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = applicationInfo.metaData;
            contentAuthority = bundle.getString(JH_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e)
        {
            Log.wtf(TAG, e);
        }
        BASE_CONTENT_URI = Uri.parse("content://" + contentAuthority);
        MESSAGE_URI = BASE_CONTENT_URI.buildUpon().appendPath(IDENTIFIER_MESSAGES).build();

        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(contentAuthority, IDENTIFIER_MESSAGES, MESSAGES);
    }

    @Override
    public boolean onCreate()
    {
        synchronized (mLock)
        {
            mDatabase = new SQLiteHelper(getContext()).getWritableDatabase();
            if (mDatabase != null)
            {
                init();
                return true;
            } else
            {
                Log.e(TAG, "database is null");
                return false;
            }
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        if (uriMatcher.match(uri) == MESSAGES)
        {
            Cursor cursor = mDatabase.query(TableMessage.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            return cursor;
        } else
        {
            Log.e(TAG, "query for unsupported Uri " + uri.toString());
            throw new UnsupportedOperationException("Unknown URI" + uri);
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri)
    {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        if (uriMatcher.match(uri) == MESSAGES)
        {
            long rowId = mDatabase.insert(TableMessage.TABLE_NAME, null, values);
            Uri updatedUri = ContentUris.withAppendedId(MESSAGE_URI, rowId);
            if (rowId > NONE)
            {
                getContext().getContentResolver().notifyChange(MESSAGE_URI, null, false);
            }
            return updatedUri;
        } else
        {
            Log.e(TAG, "insert for unsupported Uri " + uri.toString());
            throw new UnsupportedOperationException("Unknown URI" + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        if (uriMatcher.match(uri) == MESSAGES)
        {
            int count = mDatabase.delete(TableMessage.TABLE_NAME, selection, selectionArgs);
            if (count > NONE)
            {
                getContext().getContentResolver().notifyChange(MESSAGE_URI, null, false);
            }
            Log.i(TAG, count + " rows deleted in messages table");
            return count;
        } else
        {
            Log.e(TAG, "delete for unsupported Uri " + uri.toString());
            throw new UnsupportedOperationException("Unknown URI" + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        if (uriMatcher.match(uri) == MESSAGES)
        {
            int count = mDatabase.update(TableMessage.TABLE_NAME, values, selection, selectionArgs);
            if (count > NONE)
            {
                getContext().getContentResolver().notifyChange(MESSAGE_URI, null, false);
            }
            Log.i(TAG, count + " rows updated in messages table");
            return count;
        } else
        {
            Log.e(TAG, "update for unsupported Uri " + uri.toString());
            throw new UnsupportedOperationException("Unknown URI" + uri);
        }
    }
}
