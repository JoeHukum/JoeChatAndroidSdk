package com.joehukum.chat.ui.views;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.joehukum.chat.R;
import com.joehukum.chat.messages.objects.Option;

import java.util.List;

/**
 * Created by pulkitkumar on 29/03/16.
 */
public class SearchAddItemsView extends FrameLayout
{
    public interface SearchAddItemsCallback
    {
        public void OnClickAdd(List<Option> selectedOptions);
    }

    private SearchAddItemsCallback mListener;
    private List<Option> mAllOptions;

    public SearchAddItemsView(Context context, SearchAddItemsCallback listener)
    {
        super(context);
        mListener = listener;
        addView(getSearchAddView(context));
    }

    public void setOptions(List<Option> options)
    {
        mAllOptions = options;
    }

    private View getSearchAddView(Context context)
    {
        View view = View.inflate(context, R.layout.search_add_user_input, null);

        return view;
    }
}
