package com.joehukum.chat.ui.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.joehukum.chat.R;
import com.joehukum.chat.ui.activities.ChatActivity;

/**
 * Created by pulkitkumar on 29/03/16.
 */
public class TextUserInputView extends LinearLayout implements View.OnClickListener
{
    public interface TextInputCallbacks
    {
        public void sendTextMessage(String message);

        public void onClickAttachment();
    }

    private TextInputCallbacks mListener;
    private EditText mMessage;

    public TextUserInputView(Context context, TextInputCallbacks listener)
    {
        super(context);
        mListener = listener;
        inflateView(context);
        addListeners();
    }

    private void inflateView(Context context)
    {
        // set default attribute values
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int padding = (int) context.getResources().getDimension(R.dimen.padding_default);
        setPadding(padding, padding, padding, padding);
        setLayoutParams(params);
        // inflate layout.
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.user_input_text, this);
    }

    private void addListeners()
    {
        mMessage = (EditText) findViewById(R.id.message);
        final ImageButton attachment = (ImageButton) findViewById(R.id.attachment);
        final ImageButton send = (ImageButton) findViewById(R.id.send);
        mMessage.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (TextUtils.isEmpty(s))
                {
                    send.setEnabled(false);
                } else
                {
                    send.setEnabled(true);
                }
            }
        });

        attachment.setOnClickListener(this);
        attachment.setVisibility(View.GONE);
        send.setOnClickListener(this);
    }

    public void takeInputFocus()
    {
        mMessage.requestFocus();
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.send)
        {
            String text = mMessage.getText().toString();
            mMessage.setText(null);
            mListener.sendTextMessage(text);
        } else if (v.getId() == R.id.attachment)
        {
            mListener.onClickAttachment();
        }
    }
}
