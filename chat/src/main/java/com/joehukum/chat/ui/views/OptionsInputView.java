package com.joehukum.chat.ui.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.joehukum.chat.R;
import com.joehukum.chat.messages.objects.Option;
import com.joehukum.chat.ui.adapters.OptionListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pulkitkumar on 01/04/16.
 */
public class OptionsInputView extends FrameLayout
{
    public interface OptionClickCallback
    {
        void onOptionClick(Option option);
    }

    private OptionClickCallback mListener;
    private Context mContext;
    private ListView mListView;

    public OptionsInputView(Context context, OptionClickCallback listener)
    {
        super(context);
        mListener = listener;
        mContext = context;
        removeAllViews();
        addView(getOptionsList(context));

    }

    public void setOptions(final List<Option> options)
    {
        ArrayAdapter<Option> adapter = new OptionListAdapter(mContext, options);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                mListener.onOptionClick(options.get(position));
            }
        });
    }

    private View getOptionsList(Context context)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.list_option, null, false);
        mListView = (ListView) view.findViewById(R.id.list);
        return view;
    }
}
