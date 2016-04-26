package com.joehukum.chat.messages.objects;

import java.util.Date;

/**
 * Created by pulkitkumar on 25/04/16.
 */
public class DateMetaData
{
    private Date start;
    private Date end;

    public DateMetaData(Date start, Date end)
    {
        this.start = start;
        this.end = end;
    }

    public Date getStart()
    {
        return start;
    }

    public Date getEnd()
    {
        return end;
    }
}
