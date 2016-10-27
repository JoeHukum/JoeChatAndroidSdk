package com.joehukum.chat.messages.network;

/**
 * Created by pulkitkumar on 26/10/16.
 */
import android.util.Log;
import com.joehukum.chat.messages.objects.ImageUpload;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * To parse image response.
 */
public class ImageParser
{
    private static final String TAG = ImageParser.class.getName();

    public static ImageUpload parseResponse(String response)
    {
        String url = "";
        boolean success = false;
        try
        {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("excp"))
            {
                success = !jsonObject.getBoolean("excp");
            }
            if (jsonObject.has("url"))
            {
                url = jsonObject.getString("url");
            }
        } catch (JSONException je)
        {
            Log.wtf(TAG, je);
        }
        return new ImageUpload(success, url);
    }
}
