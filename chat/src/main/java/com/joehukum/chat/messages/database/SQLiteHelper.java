package com.joehukum.chat.messages.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by pulkitkumar on 17/03/16.
 */
class SQLiteHelper extends SQLiteOpenHelper
{
    private static final String TAG = SQLiteHelper.class.getName();

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "jh_chat.db";

    public SQLiteHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try
        {
            TableMessage.onCreate(db);
        } catch (SQLException e)
        {
            Log.wtf(TAG, e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
