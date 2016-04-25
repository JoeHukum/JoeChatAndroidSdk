package com.jh;

import android.app.Application;

import com.joehukum.chat.JoeHukum;

/**
 * Created by pulkitkumar on 25/04/16.
 */
public class JhApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        JoeHukum.init(getApplicationContext());
    }
}
