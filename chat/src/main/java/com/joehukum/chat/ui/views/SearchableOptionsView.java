package com.joehukum.chat.ui.views;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.joehukum.chat.R;
import com.joehukum.chat.messages.objects.Option;
import com.joehukum.chat.ui.adapters.AutoCompleteAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pulkitkumar on 29/03/16.
 */
public class SearchableOptionsView extends LinearLayout
{
    public interface SearchOptionSelectionCallback
    {
        void onClickSearchableOption(Option option);
    }

    private NiceAutoCompleteTextView mAutoComplete;
    private SearchOptionSelectionCallback mListener;
    private List<Option> mOptions;
    private AutoCompleteAdapter mAdapter;
    private Option mOption;

    public SearchableOptionsView(Context context, SearchOptionSelectionCallback listener)
    {
        super(context);
        mListener = listener;
        mOptions = new ArrayList<>();

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int padding = (int) context.getResources().getDimension(R.dimen.padding_default);
        setPadding(padding, padding, padding, padding);
        setLayoutParams(params);
        // inflate layout.
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.search_user_input, this);
        initializeViews(context);
    }

    public void setOptions(List<Option> options)
    {
        mAdapter.setOptions(options);
    }

    private void initializeViews(Context context)
    {
        mAutoComplete = (NiceAutoCompleteTextView) findViewById(R.id.auto_complete);
        final FloatingActionButton send = (FloatingActionButton) findViewById(R.id.send);
        mAdapter = new AutoCompleteAdapter(context, mOptions);

        mAutoComplete.setAdapter(mAdapter);
        send.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mOption == null && mAutoComplete.isSelectionFromPopUp())
                {
                    mListener.onClickSearchableOption(mOption);
                    mAutoComplete.setText(null);
                } else
                {
                    mAutoComplete.setError(getContext().getString(R.string.auto_complete_error));
                }
            }
        });
        mAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                mOption = mOptions.get(position);
                mListener.onClickSearchableOption(mOption);
                mAutoComplete.setText(null);
            }
        });
    }


    public void takeInputFocus()
    {
        mAutoComplete.requestFocus();
    }
}
