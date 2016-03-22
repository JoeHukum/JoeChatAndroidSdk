package com.joehukum.chat.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.joehukum.chat.messages.objects.Message;

import java.util.List;

/**
 * Created by pulkitkumar on 17/03/16.
 */
public class ChatAdapter extends ArrayAdapter<Message>
{
    private Context mContext;
    private List<Message> mMessages;

    public ChatAdapter(Context context, List<Message> messages)
    {
        super(context, -1);
        mContext = context;
        mMessages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return super.getView(position, convertView, parent);
    }
}
