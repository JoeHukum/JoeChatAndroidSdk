package com.joehukum.chat.messages.images;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.joehukum.chat.messages.database.DbUtils;
import com.joehukum.chat.messages.database.MessageProvider;
import com.joehukum.chat.messages.database.TableMessage;
import com.joehukum.chat.messages.objects.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pulkitkumar on 22/03/16.
 */
public class ImageService
{
    private static final String TAG = ImageService.class.getName();
    private final Object mImageLock = new Object();

    public void downloadImages(final Context context)
    {
        synchronized (mImageLock)
        {
            Map<Long, String> imageMessages = getNonDownloadedImages(context);
            if (imageMessages != null)
            {
                for (final Map.Entry<Long, String> entry : imageMessages.entrySet())
                {
                    InputStream input = null;
                    try
                    {
                        input = new java.net.URL(entry.getValue()).openStream();
                    } catch (IOException e)
                    {
                        //ignore download.
                    }
                    // Decode Bitmap

                    Bitmap resource = BitmapFactory.decodeStream(input);
                    FileOutputStream out = null;
                    try
                    {
                        File file = createExternalStoragePublicPicture(context);
                        out = new FileOutputStream(file);
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                        Log.i(TAG, "Local image url: " + file.getAbsolutePath());
                        updateImageUrls(context, entry.getKey(), file.getAbsolutePath());
                    } catch (FileNotFoundException e)
                    {
                        Log.wtf(TAG, e);
                    } catch (IOException e)
                    {
                        Log.wtf(TAG, e);
                    }
                }
            }
        }
    }

    private void updateImageUrls(Context context, Long key, String absolutePath)
    {
        context.getContentResolver().update(MessageProvider.MESSAGE_URI,
                TableMessage.contentUpdateValue(absolutePath),
                TableMessage.whereId(),
                new String[]{String.valueOf(key)});
    }

    private File createExternalStoragePublicPicture(Context context)
    {
        File directory = Environment.getExternalStoragePublicDirectory(
                new StringBuilder().append(Environment.DIRECTORY_PICTURES).append(File.separator).append("Joe Hukum").toString());
        directory.mkdirs();
        File file = new File(directory, new StringBuilder().append("Jh_").append(String.valueOf(System.currentTimeMillis())).append(".jpg").toString());

        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        Uri contentUri = Uri.fromFile(file);
        Log.i("Path->>", file.getAbsolutePath());
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
        return file;
    }

    /**
     * Gets all images which are not downloaded. (Text starts with http).
     */
    public Map<Long, String> getNonDownloadedImages(Context context)
    {
        Cursor cursor = null;
        Map<Long, String> ticketMessageImageMap = new LinkedHashMap<>();
        try
        {
            cursor = context.getContentResolver().query(MessageProvider.MESSAGE_URI, null, null, null, null);
            List<Message> messages = TableMessage.getMessage(cursor);
            if (messages != null)
            {
                for (Message message : messages)
                {
                    if (!TextUtils.isEmpty(message.getContent()) && message.getContent().startsWith("http"))
                    {
                        ticketMessageImageMap.put(message.getId(), message.getContent());
                    }
                }
            }
        } catch (SQLException e)
        {
            Log.wtf(TAG, e);
            return ticketMessageImageMap;
        } finally
        {
            DbUtils.closeCursor(cursor);
        }
        return ticketMessageImageMap;
    }


}
