package com.joehukum.chat.ui.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.joehukum.chat.R;
import com.joehukum.chat.ServiceFactory;
import com.joehukum.chat.messages.objects.Message;
import com.joehukum.chat.ui.activities.ChatActivity;
import com.joehukum.chat.user.Credentials;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pulkitkumar on 16/04/16.
 */
public class NotificationHelper
{
    private static final String TAG = NotificationHelper.class.getName();

    private static final String ICON_RES = "com.joehukum.chat.notification.icon";
    public static final String DRAWABLE = "drawable";
    private static final int JH_NOTIFICATION_ID = 101;

    public static void showNotification (Context context, @NonNull List<Message> messageList)
    {
        String title = getNotificationTitle(context);
        String summary = getNotificationSummary(context, messageList.size());
        List<String> allMessages = getMessageStrings(messageList);
        Intent notificationClick = getChatIntent(context);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, notificationClick, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_joe_notif);
        builder.setLargeIcon(getClientIcon(context));
        builder.setContentTitle(title);
        builder.setContentText(summary);
        builder.setStyle(getStyle(allMessages, title, summary));
        builder.setContentIntent(resultPendingIntent);
        builder.setSound(defaultSoundUri);
        builder.setGroupSummary(true);
        builder.setAutoCancel(true);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(JH_NOTIFICATION_ID, builder.build());
    }

    private static String getNotificationSummary(Context context, int size)
    {
        if (size == 1)
        {
            return context.getString(R.string.notification_summary_one);
        } else
        {
            return String.format(context.getString(R.string.notification_summary), size);
        }
    }

    private static String getNotificationTitle(Context context)
    {
        String template= context.getString(R.string.notifcation_template);
        Credentials credentials = ServiceFactory.CredentialsService().getUserCredentials(context);
        return String.format(template, credentials.getClientName());
    }

    private static NotificationCompat.Style getStyle(List<String> messages, String title, String summary)
    {
        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        int i = 0;
        for (String message : messages)
        {
            style.addLine(message);
            i++;
            if (i > 3)
            {
                style.addLine("and "+(messages.size() - 3) + " more");
                break;
            }
        }
        style.setBigContentTitle(title);
        style.setSummaryText(summary);
        return style;
    }

    private static List<String> getMessageStrings(List<Message> messageList)
    {
        List<String> messages = new ArrayList<>();
        for (Message message: messageList)
        {
            messages.add(message.getDisplayText());
        }
        return messages;
    }

    private static Intent getChatIntent(Context context)
    {
        Intent intent = ChatActivity.getIntent(context, ServiceFactory.CredentialsService().getChannel(context));
        context.startActivity(intent);
        return intent;
    }

    private static Bitmap getClientIcon(Context context)
    {
        ApplicationInfo applicationInfo = null;
        try
        {
            applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = applicationInfo.metaData;
            int id = bundle.getInt(ICON_RES);
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
            return bitmap;
        } catch (PackageManager.NameNotFoundException e)
        {
            Log.wtf(TAG, e);
            return null;
        }
    }
}
