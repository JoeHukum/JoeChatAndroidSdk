package com.joehukum.chat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.anupcowkur.reservoir.Reservoir;
import com.joehukum.chat.messages.sync.SyncUtils;
import com.joehukum.chat.ui.activities.ChatActivity;
import com.joehukum.chat.ui.utils.UiUtils;
import com.joehukum.chat.user.Credentials;

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
        final ProgressDialog pd = UiUtils.getProgressDialog(context);
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

    public static void init(Context context, String authority)
    {
        initReservoir(context);
    }

    public static void initReservoir(Context context)
    {
        try
        {
            Reservoir.init(context, 32768); //32kb in bytes
        } catch (Exception e)
        {
            Log.wtf(TAG, e);
        }
    }
}
