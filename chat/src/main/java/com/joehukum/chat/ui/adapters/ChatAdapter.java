package com.joehukum.chat.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.joehukum.chat.R;
import com.joehukum.chat.messages.objects.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by pulkitkumar on 17/03/16.
 */
public class ChatAdapter extends RecyclerView.Adapter
{
    public static final String TIME_PATTERN = "hh:mm a";
    private Context mContext;
    private List<Message> mMessages;

    public ChatAdapter(Context context, List<Message> messages)
    {
        mContext = context;
        mMessages = messages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_item_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        ViewHolder viewHolder = (ViewHolder) holder;

        Message message = mMessages.get(position);
        if (message.getType() == Message.Type.RECEIVED)
        {   // show/hide bubbles
            viewHolder.receivedView.setVisibility(View.VISIBLE);
            viewHolder.sentView.setVisibility(View.GONE);
            // show sent content.
            if (message.getContentType() == Message.ContentType.TEXT)
            {
                viewHolder.receivedText.setVisibility(View.VISIBLE);
                viewHolder.receivedImage.setVisibility(View.GONE);
                viewHolder.receivedText.setText(message.getContent());
            }
            // show time.
            viewHolder.sentTime.setText(getFormattedDate(message.getTime()));
        } else if (message.getType() == Message.Type.SENT)
        {
            // show/hide bubbles.
            viewHolder.sentView.setVisibility(View.VISIBLE);
            viewHolder.receivedView.setVisibility(View.GONE);
            // show received content.
            if (message.getContentType() == Message.ContentType.TEXT ||
                    message.getContentType() == Message.ContentType.OPTION)
            {
                viewHolder.sentText.setVisibility(View.VISIBLE);
                viewHolder.sentImage.setVisibility(View.GONE);
                viewHolder.sentText.setText(message.getContent());
            }
            // show time.
            viewHolder.sentTime.setText(getFormattedDate(message.getTime()));
            // show tick mark.
            if (TextUtils.isEmpty(message.getMessageHash()))
            {
                viewHolder.deliveryStatus.setImageResource(R.drawable.ic_not_done);
            } else
            {
                viewHolder.deliveryStatus.setImageResource(R.drawable.ic_action_done);
            }
        }
    }

    private String getFormattedDate(Date time)
    {
        SimpleDateFormat format = new SimpleDateFormat(TIME_PATTERN);
        String formattedDate = format.format(time);
        return formattedDate;
    }

    @Override
    public int getItemCount()
    {
        if (mMessages == null)
        {
            return 0;
        }
        return mMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        View receivedView;
        View sentView;
        TextView receivedText;
        TextView sentText;
        TextView receivedTime;
        TextView sentTime;
        ImageView sentImage;
        ImageView receivedImage;
        ImageView deliveryStatus;
        Button userAction;

        public ViewHolder(View itemView)
        {
            super(itemView);
            receivedView = itemView.findViewById(R.id.received);
            sentView = itemView.findViewById(R.id.sent);
            receivedText = (TextView) itemView.findViewById(R.id.messageReceived);
            sentText = (TextView) itemView.findViewById(R.id.messageSent);
            receivedTime = (TextView) itemView.findViewById(R.id.timeReceived);
            sentTime = (TextView) itemView.findViewById(R.id.timeSent);
            receivedImage = (ImageView) itemView.findViewById(R.id.receivedImage);
            sentImage = (ImageView) itemView.findViewById(R.id.sentImage);
            deliveryStatus = (ImageView) itemView.findViewById(R.id.status);
            userAction = (Button) itemView.findViewById(R.id.userAction);
        }
    }
}
