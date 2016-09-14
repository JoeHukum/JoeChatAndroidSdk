package com.joehukum.chat.messages.network;

import android.support.annotation.NonNull;

/**
 * Created by pulkitkumar on 22/03/16.
 */
public class Api
{
    private static final String BASE_URL = "http://api.gogetspeedy.com/";

    public static class Message
    {
        public static String Url()
        {
            return new StringBuilder(BASE_URL).append("api/conversation/v2/ticket/smart/").toString();
        }

        public static String Json(String customerHash, String message)
        {
            String template = new StringBuilder("{\n")
                    .append("\t\"customer_hash\": \"%s\",\n")
                    .append("\t\"channel\":\"android_app_channel\",")
                    .append("\t\"message\": \"%s\"\n")
                    .append("}").toString();
            return String.format(template, customerHash, message);
        }
    }

    public static class Sync
    {
        public static String Url(String customerHash, String ticketMessageHash)
        {
            String template = new StringBuilder(BASE_URL).append("api/conversation/v2/fsync?orderBy=asc")
                    .append("&customerHash=%s")
                    .append("&ticketMessageHash=%s")
                    .append("&showCustomerMsg=false")
                    .toString();
            return String.format(template, customerHash, ticketMessageHash);
        }
    }

    public static class User
    {
        public static String Url()
        {
            return new StringBuilder(BASE_URL).append("api/customer/init").toString();
        }

        public static String Json(@NonNull String phoneNumber, @NonNull String email, @NonNull String firebaseToken)
        {
            String jsonTemplate = new StringBuilder().append("{\"phone\":\"%s\",")
                    .append("\"verified\":true,")
                    .append("\"email\":\"%s\",")
                    .append("\"channel\":\"android_app_channel\",")
                    .append("\"firebase_token\":\"%s\"}")
                    .toString();
            return String.format(jsonTemplate, phoneNumber, email, firebaseToken);
        }
    }
}
