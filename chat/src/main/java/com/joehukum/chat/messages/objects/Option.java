package com.joehukum.chat.messages.objects;

import android.text.TextUtils;

/**
 * Created by pulkitkumar on 29/03/16.
 */
public class Option
{
    public static final String TEXT_PRICE_PATTERN = "%s -Rs %0.2f";
    private String id;
    private String displayText;

    public Option(String id, String displayText)
    {
        this.id = id;
        this.displayText = displayText;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getDisplayText()
    {
        return displayText;
    }

    public void setDisplayText(String displayText)
    {
        this.displayText = displayText;
    }
}
