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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.joehukum.chat.R;
import com.joehukum.chat.ServiceFactory;
import com.joehukum.chat.messages.database.MessageProvider;
import com.joehukum.chat.messages.network.MessageNetworkService;
import com.joehukum.chat.messages.objects.DateMetaData;
import com.joehukum.chat.messages.objects.Message;
import com.joehukum.chat.messages.objects.Option;
import com.joehukum.chat.ui.adapters.ChatAdapter;
import com.joehukum.chat.ui.fragments.DatePickerFragment;
import com.joehukum.chat.ui.fragments.TimePickerFragment;
import com.joehukum.chat.ui.views.DateInputView;
import com.joehukum.chat.ui.views.SearchableOptionsView;
import com.joehukum.chat.ui.views.TextUserInputView;
import com.joehukum.chat.ui.views.TimeInputView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChatActivity extends AppCompatActivity implements TextUserInputView.TextInputCallbacks,
        SearchableOptionsView.SearchOptionSelectionCallback,
        DateInputView.DateInputCallbacks,
        TimeInputView.TimeInputCallback,
        DatePickerFragment.DateSelectedCallback
{
    private static final String TAG = ChatActivity.class.getName();
    private static final String CHANNEL_NAME = "channelName";
    private static final int CAMERA_POSITION = 0;
    private static final int GALLERY_POSITION = 1;
    public static final String DATE_PATTERN = "dd MMM yyyy";
    public static final SimpleDateFormat FORMATTER = new SimpleDateFormat(DATE_PATTERN);

    private static final int REQUEST_CODE_GALLERY = 11;
    private static final int REQUEST_CODE_CAMERA = 12;

    public static Intent getIntent(Context context, String channelName)
    {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(CHANNEL_NAME, channelName);
        return intent;
    }

    private String mChannelName;

    private RecyclerView mListView;
    private FrameLayout mUserInputContainer;
    private TextUserInputView mTextInput;
    private DateInputView mDateInput;
    private TimeInputView mTimeInput;
    private SearchableOptionsView mSearchableOptionInput;

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
                mUserInputContainer.addView(mDateInput);
                mDateInput.setMetadata(ServiceFactory.MetaDataService().getDateMetaData(this, message.getMessageHash()));
            } else if (message.getResponseType() == Message.ResponseType.TIME)
            {
                mUserInputContainer.removeAllViews();
                mUserInputContainer.addView(mTimeInput);
            } else if (message.getResponseType() == Message.ResponseType.SEARCH_OPTION)
            {
                List<Option> options = ServiceFactory.MetaDataService().getOptions(this, message.getMessageHash());
                mSearchableOptionInput.setOptions(options);
                mUserInputContainer.removeAllViews();
                mUserInputContainer.addView(mSearchableOptionInput);
                mSearchableOptionInput.takeInputFocus();
            } else if (message.getResponseType() == Message.ResponseType.INT)
            {
                mUserInputContainer.removeAllViews();
                mUserInputContainer.addView(mTextInput);
                mTextInput.setNumberInput(true);
                mTextInput.takeInputFocus();
            } else
            {
                mUserInputContainer.removeAllViews();
                mUserInputContainer.addView(mTextInput);
                mTextInput.setNumberInput(false);
                mTextInput.takeInputFocus();
            }
        } else
        {
            mUserInputContainer.removeAllViews();
            mUserInputContainer.addView(mTextInput);
            mTextInput.setNumberInput(false);
            mTextInput.takeInputFocus();
        }
    }

    private Message getLastMessage()
    {
        if (mMessages != null && !mMessages.isEmpty())
        {
            return mMessages.get(0);
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setReverseLayout(true);
        mListView.setLayoutManager(layoutManager);
        mListView.setAdapter(mAdapter);
    }

    private void initializeInputLayouts()
    {
        mTextInput = new TextUserInputView(this, this);
        mDateInput = new DateInputView(this, this);
        mTimeInput = new TimeInputView(this, this);
        mSearchableOptionInput = new SearchableOptionsView(this, this);
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
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void startOver()
    {
        sendTextMessage(MessageNetworkService.START_OVER_TEXT);
    }

    public void sendMessage(Message message)
    {
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

    @Override
    public void sendTextMessage(String input)
    {
        if (!TextUtils.isEmpty(input))
        {
            Message message = ServiceFactory.MessageDatabaseService().generateTextMessage(input);
            sendMessage(message);
        }
    }

    @Override
    public void onDateSelected(Date date)
    {
        String dateStr = FORMATTER.format(date);
        sendTextMessage(dateStr);
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
    public void onClickDateInput(DateMetaData metaData)
    {
        DatePickerFragment.open(getSupportFragmentManager(), metaData);
    }


    @Override
    public void onClickSearchableOption(Option option)
    {
        if (option != null)
        {
            Message message = ServiceFactory.MessageDatabaseService().generateOptionMessage(option);
            sendMessage(message);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.startOver)
        {
            startOver();
            return true;
        } else
        {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed()
    {
        finish();
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
