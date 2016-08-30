package com.joehukum.chat.user;

import com.google.firebase.iid.FirebaseInstanceIdService;
import com.joehukum.chat.ServiceFactory;

/**
 * Created by pulkitkumar on 01/08/16.
 */
public class FirebaseTokenRefreshService extends FirebaseInstanceIdService
{
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        ServiceFactory.CredentialsService().updateFirebaseToken(getApplicationContext());
    }
}
