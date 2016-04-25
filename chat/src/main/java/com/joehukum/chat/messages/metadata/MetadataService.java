package com.joehukum.chat.messages.metadata;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import com.joehukum.chat.JoeHukum;
import com.joehukum.chat.messages.objects.Option;

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
                JoeHukum.initReservoir(context);
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
}
