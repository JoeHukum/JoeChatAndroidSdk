package com.joehukum.chat.ui.views;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.joehukum.chat.R;
import com.joehukum.chat.messages.objects.Option;
import com.joehukum.chat.ui.adapters.AutoCompleteAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pulkitkumar on 29/03/16.
 */
public class SearchableOptionsView extends FrameLayout
{
    public interface SearchOptionSelectionCallback
    {
        void onClickSearchableOption(Option option);
    }

    private SearchOptionSelectionCallback mListener;
    private List<Option> mOptions;
    private AutoCompleteAdapter mAdapter;
    private Option mOption;

    public SearchableOptionsView(Context context, SearchOptionSelectionCallback listener)
    {
        super(context);
        mListener = listener;
        mOptions = new ArrayList<>();
        addView(getSearchAddView(context));
    }

    public void setOptions(List<Option> options)
    {
        mOptions.clear();
        mOptions.addAll(options);
        mAdapter.notifyDataSetChanged();
    }

    private View getSearchAddView(Context context)
    {
        View view = View.inflate(context, R.layout.search_user_input, null);
        final NiceAutoCompleteTextView autoComplete = (NiceAutoCompleteTextView) view.findViewById(R.id.auto_complete);
        final FloatingActionButton send = (FloatingActionButton) view.findViewById(R.id.send);
        mAdapter = new AutoCompleteAdapter(context, mOptions);

        autoComplete.setAdapter(mAdapter);
        send.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mOption == null && autoComplete.isSelectionFromPopUp())
                {
                    mListener.onClickSearchableOption(mOption);
                } else
                {
                    autoComplete.setError(getContext().getString(R.string.auto_complete_error));
                }
            }
        });
        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                mOption = mOptions.get(position);
                mListener.onClickSearchableOption(mOption);
            }
        });
        return view;
    }
}
