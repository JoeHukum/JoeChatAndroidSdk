package com.joehukum.chat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.anupcowkur.reservoir.Reservoir;
import com.joehukum.chat.messages.sync.SyncUtils;
import com.joehukum.chat.ui.activities.ChatActivity;
import com.joehukum.chat.ui.utils.UiUtils;

import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pulkitkumar on 17/03/16.
 */
public class JoeHukum
{
    private static final String TAG = JoeHukum.class.getName();

    public static void chat(final Context context, String authKey, String phoneNumber, String email)
    {
        chat(context, authKey, phoneNumber, email, UiUtils.getProgressDialog(context));
    }

    public static void chat(final Context context, String authKey, String phoneNumber, String email, final ProgressDialog pd)
    {
        showProgress(pd);
        Observable<Boolean> observable = ServiceFactory.CredentialsService().createUser(context, authKey, phoneNumber, email);
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Boolean>()
                {
                    @Override
                    public void onCompleted()
                    {

                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        Log.wtf(TAG, e);
                    }

                    @Override
                    public void onNext(Boolean aBoolean)
                    {
                        hideProgress(pd);
                        if (!SyncUtils.isPrefSetupComplete(context))
                        {
                            SyncUtils.CreateSyncAccount(context);
                        }
                        String channelName = ServiceFactory.CredentialsService().getChannel(context);
                        Intent intent = ChatActivity.getIntent(context, channelName);
                        context.startActivity(intent);
                    }
                });
    }

    public static void setUserParams(Context context, Map<String, String> params)
    {
        ServiceFactory.CredentialsService().updateParams(context, params);
    }

    private static void hideProgress(ProgressDialog pd)
    {
        try
        {
            if (pd.isShowing())
            {
                pd.dismiss();
            }
        } catch (Exception e)
        {
            // ignore
        }
    }

    private static void showProgress(ProgressDialog pd)
    {
        try
        {
            pd.show();
        } catch (Exception e)
        {
            //ignore
        }
    }

    public static void init(Context context)
    {
        initReservoir(context);
    }

    public static void initReservoir(Context context)
    {
        try
        {
            Reservoir.init(context, 65536); //64kb in bytes
        } catch (Exception e)
        {
            Log.wtf(TAG, e);
        }
    }


}
