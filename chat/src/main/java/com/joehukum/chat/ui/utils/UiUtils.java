package com.joehukum.chat.ui.utils;

import android.Manifest;
import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.joehukum.chat.R;

/**
 * Created by pulkitkumar on 18/03/16.
 */
public class UiUtils
{
    public static void requestWritePermission(final Activity activity, View view)
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {
            Snackbar.make(view,
                    activity.getString(R.string.external_storage_permission_pitch), Snackbar.LENGTH_INDEFINITE)
                    .setAction(activity.getString(R.string.ok), new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }
                    })
                    .show();

        } else
        {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
}
