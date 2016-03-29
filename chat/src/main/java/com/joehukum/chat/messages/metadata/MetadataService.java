package com.joehukum.chat.messages.metadata;

import android.content.Context;
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

    public List<Option> getOptions(Context context, long id)
    {
        if (id > 0)
        {
            try
            {
                JoeHukum.initReservoir(context);
                if (Reservoir.contains(String.valueOf(id)))
                {
                    Type resultType = new TypeToken<ArrayList<Option>>()
                    {
                    }.getType();
                    ArrayList<Option> options = Reservoir.get(String.valueOf(id), resultType);
                    return options;
                } else
                {
                    Log.e(TAG, "No options for ticket message " + id);
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

    public void saveOptions(long id, List<Option> options)
    {
        if (id > 0)
        {
            try
            {
                Reservoir.put(String.valueOf(id), options);
            } catch (Exception e)
            {
                Log.wtf(TAG, e);
            }
        }
    }
}
