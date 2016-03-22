package com.joehukum.chat.messages.database;

import android.database.Cursor;

/**
 * Created by pulkitkumar on 21/03/16.
 */
public class DbUtils
{
    public static final String EMPTY = "";

    public static void closeCursor(Cursor... cursors)
    {
        for (Cursor cursor: cursors)
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }
}
