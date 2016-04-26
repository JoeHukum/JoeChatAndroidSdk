package com.joehukum.chat.ui.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.joehukum.chat.messages.objects.Option;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pulkitkumar on 04/04/16.
 */
public class AutoCompleteAdapter extends ArrayAdapter<Option>
{
    private static final CharSequence EMPTY = "";
    private Filter  mFilter = new Filter()
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            if (constraint == null)
            {
                constraint = EMPTY;
            }
            String searchTerm = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            List<Option> filteredOptions = new ArrayList<>();
            for (Option option : mAllOptions)
            {
                if (!TextUtils.isEmpty(option.getDisplayText()))
                {
                    if (option.getDisplayText().toLowerCase().contains(searchTerm))
                    {
                        filteredOptions.add(option);
                    }
                }
            }
            results.values = filteredOptions;
            results.count = filteredOptions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            List<Option> filteredList = (List<Option>) results.values;
            if (results != null && results.count > 0)
            {
                clear();
                for (Option option : filteredList)
                {
                    add(option);
                }
                notifyDataSetChanged();
            }
        }
    };

    private List<Option> mOptions;
    private List<Option> mAllOptions;

    public AutoCompleteAdapter(Context context, List<Option> objects)
    {
        super(context, android.R.layout.simple_list_item_1, objects);
        mOptions = objects;
        mAllOptions = new ArrayList<>(objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return super.getView(position, convertView, parent);
    }

    @Override
    public Filter getFilter()
    {
        return mFilter;
    }

    public void setOptions(List<Option> options)
    {
        mOptions.clear();
        mOptions.addAll(options);
        mAllOptions.clear();
        mAllOptions.addAll(options);
        notifyDataSetChanged();
    }
}
