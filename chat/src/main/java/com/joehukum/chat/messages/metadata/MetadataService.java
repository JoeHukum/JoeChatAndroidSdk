package com.joehukum.chat.messages.metadata;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import com.joehukum.chat.JoeHukum;
import com.joehukum.chat.messages.objects.DateMetaData;
import com.joehukum.chat.messages.objects.Option;
import com.joehukum.chat.ui.activities.ChatActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pulkitkumar on 29/03/16.
 */
public class MetadataService
{
    private static final String TAG = MetadataService.class.getName();

    @NonNull
    public List<Option> getOptions(Context context,@Nullable String messageHash)
    {
        if (!TextUtils.isEmpty(messageHash))
        {
            try
            {
                JoeHukum.initReservoir(context.getApplicationContext());
                if (Reservoir.contains(messageHash))
                {
                    Type resultType = new TypeToken<ArrayList<Option>>()
                    {
                    }.getType();
                    ArrayList<Option> options = Reservoir.get(messageHash, resultType);
                    return options;
                } else
                {
                    Log.e(TAG, "No options for ticket message " + messageHash);
                    return new ArrayList<>();
                }
            } catch (Exception e)
            {
                Log.wtf(TAG, e);
                return new ArrayList<>();
            }
        } else
        {
            return new ArrayList<>();
        }
    }

    public void saveOptions(String messageHash, List<Option> options)
    {
        if (!TextUtils.isEmpty(messageHash))
        {
            try
            {
                Reservoir.put(messageHash, options);
            } catch (Exception e)
            {
                Log.wtf(TAG, e);
            }
        }
    }

    public DateMetaData getDateMetaData(Context context, String messageHash)
    {
        if (!TextUtils.isEmpty(messageHash))
        {
            JoeHukum.initReservoir(context.getApplicationContext());
            try
            {
                if (Reservoir.contains(messageHash))
                {
                    return Reservoir.get(messageHash, DateMetaData.class);
                } else
                {
                    return null;
                }
            } catch (Exception e)
            {
                Log.wtf(TAG, e);
                return null;
            }
        } else
        {
            return null;
        }
    }

    public void saveDateMetadata(String messageHash, DateMetaData metadata)
    {
        if (!TextUtils.isEmpty(messageHash))
        {
            try
            {
                Reservoir.put(messageHash, metadata);
            } catch (Exception e)
            {
                Log.wtf(TAG, e);
            }
        }
    }

    public void ratingSent(String messageHash)
    {
        if (!TextUtils.isEmpty(messageHash))
        {
            try
            {
                Reservoir.put(messageHash, true);
            } catch (Exception e)
            {
                Log.wtf(TAG, e);
            }
        }
    }

    public boolean isRatingSent(Context context, String messageHash)
    {
        if (!TextUtils.isEmpty(messageHash))
        {
            try
            {
                JoeHukum.initReservoir(context.getApplicationContext());
                if (Reservoir.contains(messageHash))
                {
                    return Reservoir.get(messageHash, Boolean.class);
                } else
                {
                    return false;
                }
            } catch (Exception e)
            {
                Log.wtf(TAG, e);
                return false;
            }
        } else
        {
            return false;
        }
    }
}
