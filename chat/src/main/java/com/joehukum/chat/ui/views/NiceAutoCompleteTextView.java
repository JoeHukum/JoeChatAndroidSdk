package com.joehukum.chat.ui.views;

import android.content.Context;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

/**
 * Created by pulkitkumar on 25/04/16.
 */
public class NiceAutoCompleteTextView extends AppCompatAutoCompleteTextView
{
    private boolean mSelectionFromPopUp;
    {
        addTextWatcher();
    }

    public NiceAutoCompleteTextView(Context context)
    {
        super(context);
    }

    public NiceAutoCompleteTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public NiceAutoCompleteTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }


    private void addTextWatcher()
    {
        addTextChangedListener(new TextWatcher()
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
                mSelectionFromPopUp = false;
            }
        });
    }

    @Override
    protected void replaceText(CharSequence text)
    {
        super.replaceText(text);
        mSelectionFromPopUp = true;
    }

    @Override
    public boolean enoughToFilter()
    {
        return true;
    }

    public boolean isSelectionFromPopUp()
    {
        return mSelectionFromPopUp;
    }
}
