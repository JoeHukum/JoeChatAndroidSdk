package com.joehukum.chat.messages.objects;

import android.text.TextUtils;

/**
 * Created by pulkitkumar on 29/03/16.
 */
public class Option
{
    public static final String TEXT_PRICE_PATTERN = "%s -Rs %0.2f";
    private long id;
    private String text;
    private String price;

    public Option(long id, String text, String price)
    {
        this.id = id;
        this.text = text;
        this.price = price;
    }

    public long getId()
    {
        return id;
    }

    public String getText()
    {
        return text;
    }

    public String getPrice()
    {
        return price;
    }

    @Override
    public String toString()
    {
        if (TextUtils.isEmpty(price))
        {
            return text;
        } else
        {
            return String.format(TEXT_PRICE_PATTERN, text, price);
        }
    }
}
