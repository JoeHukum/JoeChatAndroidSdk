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

import java.util.List;

/**
 * Created by pulkitkumar on 01/04/16.
 */
public class OptionsInputView extends FrameLayout
{
    public interface OptionClickCallback
    {
        public void onOptionClick(Option option);
    }

    private OptionClickCallback mListener;

    public OptionsInputView(Context context, List<Option> options, OptionClickCallback listener)
    {
        super(context);
        mListener = listener;
        removeAllViews();
        addView(getOptionsList(context, options));
    }

    private View getOptionsList(Context context, final List<Option> options)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.list_option, null, false);
        ListView listView = (ListView) view.findViewById(R.id.list);

        ArrayAdapter<Option> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, options);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                mListener.onOptionClick(options.get(position));
            }
        });
        return view;
    }
}
