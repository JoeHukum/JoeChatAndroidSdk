package com.joehukum.chat.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.joehukum.chat.R;
import com.joehukum.chat.messages.objects.Option;

import java.util.List;

/**
 * Created by pulkitkumar on 27/10/16.
 */

public class OptionListAdapter extends ArrayAdapter<Option>
{
    private Context context;

    public OptionListAdapter(Context context, List<Option> options)
    {
        super(context, -1 , options);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.option_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Option option = getItem(position);
        viewHolder.text.setText(option.getDisplayText());
        return convertView;
    }

    private static class ViewHolder
    {
        TextView text;
        public ViewHolder(View view)
        {
            this.text = (TextView) view.findViewById(R.id.text);
        }
    }
}
