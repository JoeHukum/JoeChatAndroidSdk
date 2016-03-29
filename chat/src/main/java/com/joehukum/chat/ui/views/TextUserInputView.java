package com.joehukum.chat.ui.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.joehukum.chat.R;

/**
 * Created by pulkitkumar on 29/03/16.
 */
public class TextUserInputView extends FrameLayout implements View.OnClickListener
{
    public interface TextInputCallbacks
    {
        public void sendMessage(String message);
        public void onClickAttachment();
    }

    private TextInputCallbacks mListener;
    private EditText mMessage;

    public TextUserInputView(Context context, TextInputCallbacks listener)
    {
        super(context);
        mListener = listener;
        View view = getUserInputView(context);
        addView(view);
    }

    private View getUserInputView(Context context)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.user_input_text, null, false);
        mMessage = (EditText) view.findViewById(R.id.message);
        final ImageButton attachment = (ImageButton) view.findViewById(R.id.attachment);
        final ImageButton send = (ImageButton) view.findViewById(R.id.send);
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
        attachment.setVisibility(View.GONE); // todo: make it configurable based on client.
        send.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.send)
        {
            mListener.sendMessage(mMessage.getText().toString());
        } else if (v.getId() == R.id.attachment)
        {
            mListener.onClickAttachment();
        }

    }
}
