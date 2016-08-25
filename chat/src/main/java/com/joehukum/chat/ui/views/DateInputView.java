package com.joehukum.chat.ui.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.joehukum.chat.R;
import com.joehukum.chat.messages.objects.DateMetaData;

/**
 * Created by pulkitkumar on 29/03/16.
 */
public class DateInputView extends FrameLayout implements View.OnClickListener
{
    public interface DateInputCallbacks
    {
        void onClickDateInput(DateMetaData metaData);
    }

    private DateInputCallbacks mListener;
    private DateMetaData mMetaData;

    public DateInputView(Context context, DateInputCallbacks listener)
    {
        super(context);
        mListener = listener;
        addView(getDateInputView(context));
    }

    private View getDateInputView(Context context)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.button_user_input, null, false);
        Button button = (Button) view.findViewById(R.id.button);
        button.setText(context.getString(R.string.select_date));
        button.setOnClickListener(this);
        return view;
    }

    public void setMetadata(DateMetaData dateMetaData)
    {
        mMetaData = dateMetaData;
    }

    @Override
    public void onClick(View v)
    {
        mListener.onClickDateInput(mMetaData);
    }
}
