package com.joehukum.chat.messages.objects;

/**
 * Created by pulkitkumar on 26/10/16.
 */

public class ImageUpload
{
    private boolean success;
    private String imageUrl;

    public ImageUpload(boolean success, String url)
    {
        this.success = success;
        this.imageUrl = url;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

}