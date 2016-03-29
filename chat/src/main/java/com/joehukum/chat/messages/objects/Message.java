package com.joehukum.chat.messages.objects;

import java.util.Date;

/**
 * Created by pulkitkumar on 17/03/16.
 */
public class Message
{
    public static final long MINIMUM_ID = -1;
    private long id;
    private Type type;
    private String messageHash;
    private Date time;
    private String author;
    private String content;
    private ContentType contentType;
    private ResponseType responseType;
    private boolean isRead;
    private String metadata;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    public String getMessageHash()
    {
        return messageHash;
    }

    public void setMessageHash(String messageHash)
    {
        this.messageHash = messageHash;
    }

    public Date getTime()
    {
        return time;
    }

    public void setTime(Date time)
    {
        this.time = time;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public ContentType getContentType()
    {
        return contentType;
    }

    public void setContentType(ContentType contentType)
    {
        this.contentType = contentType;
    }

    public ResponseType getResponseType()
    {
        return responseType;
    }

    public void setResponseType(ResponseType responseType)
    {
        this.responseType = responseType;
    }

    public boolean isRead()
    {
        return isRead;
    }

    public void setIsRead(boolean isRead)
    {
        this.isRead = isRead;
    }

    public void setMetadata(String metadata)
    {
        this.metadata = metadata;
    }

    public String getMetadata()
    {
        return metadata;
    }

    public enum ResponseType
    {
        TEXT("TEXT"), OPTIONS("OPTIONS"), ADDRESS("ADDRESS"), INT("INT"), DATE("DATE"), TIME("TIME")
        , SEARCH_OPTION("SEARCH_OPTION"),  PAYMENT("PAYMENT");

        private String name;

        ResponseType(String s)
        {
            name = s;
        }

        public String getName()
        {
            return name;
        }
    }

    public enum ContentType
    {
        TEXT("TEXT"), IMAGE("IMAGE");

        private String name;

        ContentType(String s)
        {
            name = s;
        }

        public String getName()
        {
            return name;
        }
    }

    public enum Type
    {
        SENT("SENT"), RECEIVED("RECEIVED");

        private String name;

        Type(String s)
        {
            name = s;
        }

        public String getName()
        {
            return name;
        }
    }
}
