package com.joehukum.chat.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.FrameLayout;

import com.joehukum.chat.R;
import com.joehukum.chat.ServiceFactory;
import com.joehukum.chat.messages.database.MessageProvider;
import com.joehukum.chat.messages.objects.Message;
import com.joehukum.chat.messages.objects.Option;
import com.joehukum.chat.ui.adapters.ChatAdapter;
import com.joehukum.chat.ui.fragments.DatePickerFragment;
import com.joehukum.chat.ui.fragments.TimePickerFragment;
import com.joehukum.chat.ui.views.DateInputView;
import com.joehukum.chat.ui.views.OptionsInputView;

import com.joehukum.chat.ui.views.TextUserInputView;
import com.joehukum.chat.ui.views.TimeInputView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChatActivity extends AppCompatActivity implements TextUserInputView.TextInputCallbacks,
        //SearchAddItemsView.SearchAddItemsCallback,
        DateInputView.DateInputCallbacks,
        TimeInputView.TimeInputCallback, OptionsInputView.OptionClickCallback
{
    private static final String TAG = ChatActivity.class.getName();
    private static final String CHANNEL_NAME = "channelName";
    private static final int CAMERA_POSITION = 0;
    private static final int GALLERY_POSITION = 1;
    private static final int REQUEST_CODE_GALLERY = 11;
    private static final int REQUEST_CODE_CAMERA = 12;

    public static Intent getIntent(Context context, String channel)
    {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(CHANNEL_NAME, channel);
        return intent;
    }

    private String mChannelName;

    private RecyclerView mListView;
    private FrameLayout mUserInputContainer;
    private TextUserInputView mTextInputView;
    private DateInputView mDateInputView;
    private TimeInputView mTimeInputView;
    //private SearchAddItemsView mSearchAddInputView;

    private ChatAdapter mAdapter;
    private List<Message> mMessages;
    private ContentObserver mObserver;

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

    @Override
    protected void onStart()
    {
        super.onStart();
        populateMessages();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        ServiceFactory.PubSubService().subscribe(mChannelName, this);
        getContentResolver().registerContentObserver(MessageProvider.MESSAGE_URI, true, mObserver);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        ServiceFactory.PubSubService().unSubscribe(this, mChannelName);
        getContentResolver().unregisterContentObserver(mObserver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putString(CHANNEL_NAME, mChannelName);
        super.onSaveInstanceState(outState);
    }

    private void populateMessages()
    {
        ServiceFactory.MessageDatabaseService().getMessages(this)
                .observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Message>>()
                {
                    @Override
                    public void onCompleted()
                    {
                        Log.i(TAG, "Observer completed");
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        Log.wtf(TAG, e);
                    }

                    @Override
                    public void onNext(List<Message> messages)
                    {
                        if (messages != null)
                        {
                            mMessages.clear();
                            mMessages.addAll(messages);
                            mAdapter.notifyDataSetChanged();
                        }
                        updateUserInputLayout();
                    }
                });
    }

    private void updateUserInputLayout()
    {
        Message message = getLastMessage();
        if (message != null && message.getType() == Message.Type.RECEIVED)
        {
            if (message.getResponseType() == Message.ResponseType.DATE)
            {
                mUserInputContainer.removeAllViews();
                mUserInputContainer.addView(mDateInputView);
            } else if (message.getResponseType() == Message.ResponseType.TIME)
            {
                mUserInputContainer.removeAllViews();
                mUserInputContainer.addView(mTimeInputView);
            } else if (message.getResponseType() == Message.ResponseType.SEARCH_OPTION)
            {
//                List<Option> options = ServiceFactory.MetaDataService().getOptions(this, message.getId());
//                mSearchAddInputView.setOptions(options);
//                mUserInputContainer.removeAllViews();
//                mUserInputContainer.addView(mSearchAddInputView);
            } else
            {
                mUserInputContainer.removeAllViews();
                mUserInputContainer.addView(mTextInputView);
            }
        } else
        {
            mUserInputContainer.removeAllViews();
            mUserInputContainer.addView(mTextInputView);
        }
    }

    private Message getLastMessage()
    {
        if (mMessages != null && !mMessages.isEmpty())
        {
            return mMessages.get(mMessages.size()-1);
        } else
        {
            return null;
        }
    }

    private void initializeUI()
    {
        initializeInputLayouts();
        mListView = (RecyclerView) findViewById(R.id.list);
        mUserInputContainer = (FrameLayout) findViewById(R.id.userInputContainer);

        mMessages = new ArrayList<>();
        mAdapter = new ChatAdapter(this, mMessages);
        mListView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mListView.setAdapter(mAdapter);
    }

    private void initializeInputLayouts()
    {
        mTextInputView = new TextUserInputView(this, this);
        mDateInputView = new DateInputView(this, this);
        mTimeInputView = new TimeInputView(this, this);
        //mSearchAddInputView = new SearchAddItemsView(this, this);
    }

    private void init(Bundle savedInstanceState)
    {
        mChannelName = savedInstanceState.getString(CHANNEL_NAME);
        mObserver = new ContentObserver(new Handler())
        {
            @Override
            public void onChange(boolean selfChange)
            {
                super.onChange(selfChange);
                Log.i(TAG, "Observer Changed");
                populateMessages();
            }
        };
        setUpToolbar();
    }

    private void setUpToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // todo: add text for title.
        setSupportActionBar(toolbar);
    }

    @Override
    public void sendMessage(String input)
    {
        if (!TextUtils.isEmpty(input))
        {
            Message message = new Message();
            message.setTime(new Date());
            message.setContent(input);
            message.setType(Message.Type.SENT);
            message.setContentType(Message.ContentType.TEXT);
            ServiceFactory.MessageDatabaseService().addMessage(this, message)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<Boolean>()
                    {
                        @Override
                        public void onCompleted()
                        {

                        }

                        @Override
                        public void onError(Throwable e)
                        {

                        }

                        @Override
                        public void onNext(Boolean aBoolean)
                        {
                            //todo
                        }
                    });
        }
    }

    @Override
    public void onClickAttachment()
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
                    //Intent intent = SendImageActivity.getCameraIntent(ChatActivity.this);
                    //startActivityForResult(intent, REQUEST_CODE_CAMERA);
                } else if (which == GALLERY_POSITION)
                {
                    //Intent intent = SendImageActivity.getGalleryIntent(ChatActivity.this);
                    //startActivityForResult(intent, REQUEST_CODE_GALLERY);
                }
            }
        });
        builder.show();
    }

    @Override
    public void onClickTimeInput()
    {
        TimePickerFragment.open(getSupportFragmentManager());
    }

    @Override
    public void onClickDateInput()
    {
        DatePickerFragment.open(getSupportFragmentManager());
    }

//    @Override
//    public void onClickAdd(List<Option> selectedOptions)
//    {
//        String data = TextUtils.join(", ", selectedOptions);
//        sendMessage(data);
//    }

    @Override
    public void onOptionClick(Option option)
    {
        sendMessage(option.toString());
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mAdapter = null;
        mMessages = null;
        mListView = null;
        mChannelName = null;
        mObserver = null;
    }
}
