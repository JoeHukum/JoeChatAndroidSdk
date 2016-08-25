package com.joehukum.chat.ui.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.joehukum.chat.R;

/**
 * Created by pulkitkumar on 29/03/16.
 */
public class TimeInputView extends FrameLayout implements View.OnClickListener
{
    public interface TimeInputCallback
    {
        void onClickTimeInput();
    }

    private TimeInputCallback mListener;

    public TimeInputView(Context context, TimeInputCallback listener)
    {
        super(context);
        mListener = listener;
        addView(getTimeInputView(context));
    }

    private View getTimeInputView(Context context)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.button_user_input, null, false);
        Button button = (Button) view.findViewById(R.id.button);
        button.setText(context.getString(R.string.time_input));
        button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v)
    {
        mListener.onClickTimeInput();
    }
}
