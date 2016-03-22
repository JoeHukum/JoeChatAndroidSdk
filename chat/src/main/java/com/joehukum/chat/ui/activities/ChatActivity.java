package com.joehukum.chat.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.joehukum.chat.R;
import com.joehukum.chat.ServiceFactory;
import com.joehukum.chat.messages.objects.Message;
import com.joehukum.chat.ui.adapters.ChatAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final String CHANNEL_NAME = "channelName";
    private static final int REQUEST_CODE_GALLERY = 11;
    private static final int REQUEST_CODE_CAMERA = 12;
    private static final int CAMERA_POSITION = 0;
    private static final int GALLERY_POSITION = 1;

    public static Intent getIntent(Context context, String channel)
    {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(CHANNEL_NAME, channel);
        return intent;
    }

    private String mChannelName;

    private ListView mListView;
    private ChatAdapter mAdapter;
    private List<Message> mMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        if (savedInstanceState == null)
        {
            init(getIntent().getExtras());
        } else
        {
            init(savedInstanceState);
        }
        initializeUI();
    }

    private void initializeUI()
    {
        final ImageButton attachment = (ImageButton) findViewById(R.id.attachment);
        final FloatingActionButton send = (FloatingActionButton) findViewById(R.id.send);
        mListView = (ListView) findViewById(R.id.list);

        mMessages = new ArrayList<>();
        mAdapter = new ChatAdapter(this, mMessages);
        mListView.setAdapter(mAdapter);

        attachment.setOnClickListener(this);
        send.setOnClickListener(this);
    }

    private void init(Bundle savedInstanceState)
    {
        mChannelName = savedInstanceState.getString(CHANNEL_NAME);
        setUpToolbar();
    }

    private void setUpToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        ServiceFactory.PubSubService().subscribe(mChannelName, this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        ServiceFactory.PubSubService().unSubscribe(mChannelName);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putString(CHANNEL_NAME, mChannelName);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.attachment)
        {
            CharSequence colors[] = new CharSequence[]
                    {getString(R.string.attach_dialog_camera), getString(R.string.attach_dialog_gallery)};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.attach_dialog_title));
            builder.setItems(colors, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    if (which == CAMERA_POSITION)
                    {
                        Intent intent = SendImageActivity.getCameraIntent(ChatActivity.this);
                        startActivityForResult(intent, REQUEST_CODE_CAMERA);
                    } else if (which == GALLERY_POSITION)
                    {
                        Intent intent = SendImageActivity.getGalleryIntent(ChatActivity.this);
                        startActivityForResult(intent, REQUEST_CODE_GALLERY);
                    }
                }
            });
            builder.show();
        }
    }
}
