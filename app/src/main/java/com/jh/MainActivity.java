package com.jh;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.joehukum.chat.JoeHukum;
import com.joehukum.chat.messages.objects.Message;
import com.joehukum.chat.ui.notification.NotificationHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final String email = "pk01@joehukum.com";
        final String phone = "1234511132";
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                JoeHukum.chat(MainActivity.this, "joe", phone, email);
            }
        });
        JoeHukum.chat(this, "joe", phone, email);
    }
}
