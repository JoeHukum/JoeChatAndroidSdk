package com.joehukum.chat.messages.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.joehukum.chat.BuildConfig;
import com.joehukum.chat.ServiceFactory;
import com.joehukum.chat.messages.network.exceptions.AppServerException;
import com.joehukum.chat.messages.network.exceptions.BadRequestException;
import com.joehukum.chat.messages.network.exceptions.ResourceNotFoundException;
import com.joehukum.chat.messages.network.exceptions.ServerErrorException;
import com.joehukum.chat.user.Credentials;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

/**
 * Created by pulkitkumar on 17/03/16.
 */
public class HttpIO
{
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final Object mLock = new Object();
    public static final String AUTH_KEY = "clHsh";
    private static OkHttpClient mOkHttpClient;

    public static enum Method
    {
        POST, PUT, GET
    }

    public static String makeRequest(Context context, @NonNull String url, @Nullable String json, Method method)
            throws IOException, AppServerException
    {
        Request request;
        switch (method)
        {
            case PUT:
                request = generatePutRequest(context, url, json);
                Log.v("HTTP:URL:PUT", url);
                Log.v("HTTP:JSON:PUT", json);
                break;
            case POST:
                request = generatePostRequest(context, url, json);
                Log.v("HTTP:URL:POST", url);
                Log.v("HTTP:JSON:POST", json);
                break;
            default:
                request = generateGetRequest(context, url);
                Log.v("HTTP:URL:GET", url);
                break;
        }
        Response response = getOkHttpClient().newCall(request).execute();
        if (!response.isSuccessful())
        {
            return checkResponse(response);
        }
        else
        {
            String resp = response.body().string();
            Log.v("HTTP:Response", resp);
            return resp;
        }
    }

    private static String checkResponse(Response response) throws AppServerException
    {
        if (response.code() == 404)
        {
            Log.e("HTTP", "404");
            throw new ResourceNotFoundException("404");
        }
        else if (response.code() == 403 || response.code() == 401)
        {
            Log.e("HTTP","403");
            throw new BadRequestException("403");
        }
        else if (response.code() >= 500)
        {
            Log.e("HTTP","500");
            throw new ServerErrorException("500");
        }
        else
        {
            throw new ResourceNotFoundException("Other"); // same handling as 404
        }
    }

    private static Request generateGetRequest(Context context, String url)
    {
        Request.Builder builder = new Request.Builder().url(url).get();
        builder = addDefaultHeaders(context, builder);
        return builder.build();
    }

    private static Request generatePostRequest(Context context, @NonNull String url, @NonNull String json)
    {
        Request.Builder builder = new Request.Builder().url(url).post(RequestBody.create(JSON, json));
        builder = addDefaultHeaders(context, builder);
        return builder.build();
    }

    private static Request generatePutRequest(Context context, @NonNull String url, @NonNull String json)
    {
        Request.Builder builder = new Request.Builder().url(url).put(RequestBody.create(JSON, json));
        builder = addDefaultHeaders(context, builder);
        return builder.build();
    }

    public static String multipartPost(Context context, @NonNull String url, @NonNull File file)
            throws IOException, AppServerException
    {
        Request.Builder builder = new Request.Builder().url(url);
        builder = addDefaultHeaders(context, builder);
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.addFormDataPart("file", file.getName(),
                RequestBody.create(MediaType.parse("image/jpeg"), file));

        multipartBuilder.type(MultipartBuilder.FORM);
        Request request = builder.post(multipartBuilder.build()).build();

        Response response = getOkHttpClient().newCall(request).execute();
        if (!response.isSuccessful())
        {
            return checkResponse(response);
        }
        else
        {
            String resp = response.body().string();
            Log.i("HTTP:Response", resp);
            return resp;
        }
    }

    private static OkHttpClient getOkHttpClient ()
    {
        synchronized (mLock)
        {
            if (mOkHttpClient != null)
            {
                return mOkHttpClient;
            }
            else
            {
                mOkHttpClient = new OkHttpClient();
                return mOkHttpClient;
            }
        }
    }

    private static Request.Builder addDefaultHeaders(Context context, Request.Builder builder)
    {
        Credentials credentials = ServiceFactory.CredentialsService().getUserCredentials(context);
        builder.addHeader(AUTH_KEY, credentials.getAuthKey());
        builder.addHeader("X-AndroidAppVersion", String.valueOf(BuildConfig.VERSION_CODE));
        return builder;
    }
}
