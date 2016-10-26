package com.joehukum.chat.messages.images;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.joehukum.chat.messages.database.DbUtils;
import com.joehukum.chat.messages.database.MessageProvider;
import com.joehukum.chat.messages.database.TableMessage;
import com.joehukum.chat.messages.network.Api;
import com.joehukum.chat.messages.network.HttpIO;
import com.joehukum.chat.messages.network.exceptions.AppServerException;
import com.joehukum.chat.messages.objects.ImageUpload;
import com.joehukum.chat.messages.objects.Message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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

    public File createExternalStoragePublicPicture(Context context)
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

    public Observable<String> sendImage(final Context context, String path)
    {
        return Observable.just(path).map(new Func1<String, String>()
        {
            @Override
            public String call(String p)
            {
                String response = null;
                try
                {
                    response = HttpIO.multipartPost(context, Api.Image.url(), new File(p));
                } catch (IOException e)
                {
                    Log.wtf(TAG, e);
                } catch (AppServerException e)
                {
                    Log.wtf(TAG, e);
                }
                ImageUpload image = ImageParser.parseResponse(response);
                if (image.isSuccess())
                {
                    return image.getUrl();
                } else
                {
                    return null;
                }
            }
        }).observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread());
    }

    public Bitmap loadFromUri(Uri fileUri, float maxWidth, float maxHeight)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileUri.getPath(), options);
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth)
        {
            if (imgRatio < maxRatio)
            {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio)
            {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else
            {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = true;
        options.inTempStorage = new byte[16 * 1024];
        Bitmap sampledBitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
        return createScaledBitmap(sampledBitmap, actualWidth, actualHeight);
    }


    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth)
        {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap)
        {
            inSampleSize++;
        }

        return inSampleSize;
    }

    private static Bitmap createScaledBitmap(Bitmap src, int reqWidth, int reqHeight)
    {
        int width = src.getWidth();
        int height = src.getHeight();

        float ratioHeight = (float) height / (float) reqHeight;
        float ratioWidth = (float) width / (float) reqWidth;

        int scaledWidth = width;
        int scaledHeight = height;

        if (ratioHeight > 1 || ratioWidth > 1)
        {
            float maxRatio = Math.max(ratioHeight, ratioWidth);
            scaledWidth = (int) (width / maxRatio);
            scaledHeight = (int) (height / maxRatio);
        }
        return Bitmap.createScaledBitmap(src, scaledWidth, scaledHeight, true);
    }

    public String getPathFromUri(Context context, Uri uri)
    {
        Cursor cursor = null;
        try
        {
            cursor = context.getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            String documentId = cursor.getString(0);
            if (documentId != null)
            {
                documentId = documentId.substring(documentId.lastIndexOf(':') + 1);
                cursor.close();
                cursor = context.getContentResolver().
                        query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                null, BaseColumns._ID + " = ? ", new String[]{documentId}, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
                String path = cursor.getString(columnIndex);

                return path;
            } else
            {
                return null;
            }
        } catch (CursorIndexOutOfBoundsException e)
        {
            return null;
        } finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }

}
