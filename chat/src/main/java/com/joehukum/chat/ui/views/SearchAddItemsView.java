package com.joehukum.chat.ui.views;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;

import com.dpizarro.autolabel.library.AutoLabelUI;
import com.dpizarro.autolabel.library.Label;
import com.joehukum.chat.R;
import com.joehukum.chat.messages.objects.Option;
import com.joehukum.chat.ui.adapters.AutoCompleteAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pulkitkumar on 29/03/16.
 */
public class SearchAddItemsView extends FrameLayout
{
    public interface SearchAddItemsCallback
    {
        void onClickAdd(List<Option> selectedOptions);
    }

    private SearchAddItemsCallback mListener;
    private List<Option> mAllOptions;
    private AutoCompleteAdapter mAdapter;

    public SearchAddItemsView(Context context, SearchAddItemsCallback listener)
    {
        super(context);
        mListener = listener;
        mAllOptions = new ArrayList<>();
        addView(getSearchAddView(context));
    }

    public void setOptions(List<Option> options)
    {
        mAllOptions.clear();
        mAllOptions.addAll(options);
        mAdapter.notifyDataSetChanged();
    }

    private View getSearchAddView(Context context)
    {
        View view = View.inflate(context, R.layout.search_add_user_input, null);
        final AutoCompleteTextView autoComplete = (AutoCompleteTextView) view.findViewById(R.id.auto_complete);
        final FloatingActionButton send = (FloatingActionButton) view.findViewById(R.id.send);
        final AutoLabelUI autoLabel = (AutoLabelUI) view.findViewById(R.id.label_view);
        mAdapter = new AutoCompleteAdapter(context, mAllOptions);

        autoComplete.setAdapter(mAdapter);
        send.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                List<Label> labels = autoLabel.getLabels();
                if (labels == null || labels.isEmpty())
                {
                    return;
                }
                mListener.onClickAdd(getSelectedOptions(autoLabel.getLabels()));
            }
        });
        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                autoLabel.addLabel(mAdapter.getItem(position).toString());
            }
        });
        return view;
    }

    private List<Option> getSelectedOptions(List<Label> labels)
    {
        List<Option> options = new ArrayList<>();
        for (Label label: labels)
        {
            for (Option option: mAllOptions)
            {
                if (option.toString().equals(label.getText()))
                {
                    options.add(option);
                }
            }
        }
        return options;
    }
}
