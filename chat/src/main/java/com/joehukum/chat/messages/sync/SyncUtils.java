package com.joehukum.chat.messages.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.joehukum.chat.ServiceFactory;
import com.joehukum.chat.user.Credentials;

/**
 * Created by pulkitkumar on 21/03/16.
 */
public class SyncUtils
{
    private static final long SYNC_FREQUENCY = 60 * 10;  // 10 minutes (in seconds)
    private static final String PREF_SETUP_COMPLETE = "setup_complete";


    public static boolean isPrefSetupComplete(@Nullable Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_SETUP_COMPLETE, false);
    }

    /**
     * Create an entry for this application in the system account list, if it isn't already there.
     */
    public static void CreateSyncAccount(Context context)
    {
        boolean newAccount = false;
        boolean setupComplete = PreferenceManager
                .getDefaultSharedPreferences(context).getBoolean(PREF_SETUP_COMPLETE, false);

        Credentials credentials = ServiceFactory.CredentialsService().getUserCredentials(context);
        String contentAuthority = credentials.getContentAuthority();
        String userId = credentials.getClientUserId();

        // Create account, if it's missing. (Either first run, or user has deleted account.)
        Account account = GenericAccountService.GetAccount(credentials.getClientUserId());
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly(account, null, null))
        {
            // Inform the system that this account supports sync
            ContentResolver.setIsSyncable(account, contentAuthority, 1);
            // Inform the system that this account is eligible for auto sync when the network is up
            ContentResolver.setSyncAutomatically(account, contentAuthority, true);
            // Recommend a schedule for automatic synchronization. The system may modify this based
            // on other scheduled syncs and network utilization.
            Bundle bundle = new Bundle();
            ContentResolver.addPeriodicSync(account, contentAuthority, bundle, SYNC_FREQUENCY);
            newAccount = true;
        }

        // Schedule an initial sync if we detect problems with either our account or our local
        // data has been deleted. (Note that it's possible to clear app data WITHOUT affecting
        // the account list, so wee need to check both.)
        if (newAccount || !setupComplete)
        {
            TriggerRefresh(context);
            PreferenceManager.getDefaultSharedPreferences(context).edit()
                    .putBoolean(PREF_SETUP_COMPLETE, true).commit();
        }
    }

    /**
     * Helper method to trigger an immediate sync ("refresh").
     * <p/>
     * <p>This should only be used when we need to preempt the normal sync schedule. Typically, this
     * means the user has pressed the "refresh" button.
     * <p/>
     * Note that SYNC_EXTRAS_MANUAL will cause an immediate sync, without any optimization to
     * preserve battery life. If you know new data is available (perhaps via a GCM notification),
     * but the user is not actively waiting for that data, you should omit this flag; this will give
     * the OS additional freedom in scheduling your sync request.
     */
    public static void TriggerRefresh(Context context)
    {
        Bundle b = new Bundle();
        // Disable sync backoff and ignore sync preferences. In other words...perform sync NOW!
        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        Credentials credentials = ServiceFactory.CredentialsService().getUserCredentials(context);
        String contentAuthority = credentials.getContentAuthority();
        String accountName = credentials.getClientUserId();
        ContentResolver.requestSync(
                GenericAccountService.GetAccount(accountName),      // Sync account
                contentAuthority, // Content authority
                b);                                      // Extras
    }
}
