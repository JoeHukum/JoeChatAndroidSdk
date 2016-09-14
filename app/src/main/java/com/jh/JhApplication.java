package com.jh;

import android.support.multidex.MultiDexApplication;

import com.joehukum.chat.JoeHukum;

/**
 * Created by pulkitkumar on 25/04/16.
 */
public class JhApplication extends MultiDexApplication
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        JoeHukum.init(getApplicationContext());
    }
}
