package com.joehukum.chat.messages.sync;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by pulkitkumar on 21/03/16.
 */
public class GenericAccountService extends Service
{
    private static final String TAG = "GenericAccountService";
    //private static final String ACCOUNT_TYPE = "com.jh";
    private static final String JH_AUTHORITY = "com.joehukum.authority";
    private Authenticator mAuthenticator;

    /**
     * Obtain a handle to the {@link android.accounts.Account} used for sync in this application.
     *
     * @return Handle to application's account (not guaranteed to resolve unless CreateSyncAccount()
     * has been called)
     */

    public static Account GetAccount(Context context, final String accountName)
    {
        ApplicationInfo applicationInfo = null;
        try
        {
            applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = applicationInfo.metaData;
            String accountType = bundle.getString(JH_AUTHORITY);
            return new Account(accountName, accountType);
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onCreate()
    {
        Log.i(TAG, "Service created");
        mAuthenticator = new Authenticator(this);
    }

    @Override
    public void onDestroy()
    {
        Log.i(TAG, "Service destroyed");
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return mAuthenticator.getIBinder();
    }

    public class Authenticator extends AbstractAccountAuthenticator
    {
        public Authenticator(Context context)
        {
            super(context);
        }

        @Override
        public Bundle editProperties(AccountAuthenticatorResponse accountAuthenticatorResponse,
                                     String s)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Bundle addAccount(AccountAuthenticatorResponse accountAuthenticatorResponse,
                                 String s, String s2, String[] strings, Bundle bundle)
                throws NetworkErrorException
        {
            return null;
        }

        @Override
        public Bundle confirmCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse,
                                         Account account, Bundle bundle)
                throws NetworkErrorException
        {
            return null;
        }

        @Override
        public Bundle getAuthToken(AccountAuthenticatorResponse accountAuthenticatorResponse,
                                   Account account, String s, Bundle bundle)
                throws NetworkErrorException
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getAuthTokenLabel(String s)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Bundle updateCredentials(AccountAuthenticatorResponse accountAuthenticatorResponse,
                                        Account account, String s, Bundle bundle)
                throws NetworkErrorException
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Bundle hasFeatures(AccountAuthenticatorResponse accountAuthenticatorResponse,
                                  Account account, String[] strings)
                throws NetworkErrorException
        {
            throw new UnsupportedOperationException();
        }
    }

}

