package com.joehukum.chat.messages.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by pulkitkumar on 21/03/16.
 */
public class SyncService extends Service
{
    private static final Object mLock = new Object();
    private static SyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate()
    {
        synchronized (mLock)
        {
            if (sSyncAdapter == null)
            {
                sSyncAdapter = new SyncAdapter(getApplicationContext(), false);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
