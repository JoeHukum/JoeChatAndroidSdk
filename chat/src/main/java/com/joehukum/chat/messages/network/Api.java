package com.joehukum.chat.messages.network;

import android.support.annotation.NonNull;

/**
 * Created by pulkitkumar on 22/03/16.
 */
public class Api
{
    private static final String BASE_URL = "http://stagapi.gogetspeedy.com/api/";

    public static class Message
    {
        public static String Url()
        {
            return new StringBuilder(BASE_URL).append("conversation/v2/ticket/smart/").toString();
        }

        public static String Json()
        {
            return null;
        }
    }

    public static class Sync
    {
        public static String Url(String customerHash, String ticketMessageHash)
        {
            String template = new StringBuilder(BASE_URL).append("conversation/sync?")
                    .append("customerHash=%s")
                    .append("&ticketMessageHash=%s").toString();
            return String.format(customerHash, ticketMessageHash);
        }
    }

    public static class User
    {
        public static String Url()
        {
            return new StringBuilder(BASE_URL).append("/customer/init").toString();
        }

        public static String Json(@NonNull String phoneNumber, @NonNull String email, @NonNull String gcmId)
        {
            String jsonTemplate = new StringBuilder().append("{\"phone\":\"%s\",")
                    .append("\"verified\":true,")
                    .append("\"email\":\"%s\",")
                    .append("\"gcm_id\":\"%s\"}")
                    .toString();
            return String.format(jsonTemplate, phoneNumber, email, gcmId);
        }
    }
}
