package com.joehukum.chat.ui.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.TimePicker;

import com.joehukum.chat.ui.views.TextUserInputView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by pulkitkumar on 29/03/16.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener
{
    public static final String DATE_PATTERN = "hh:mm a";
    public static final SimpleDateFormat FORMATTER = new SimpleDateFormat(DATE_PATTERN);
    private static final String SELECT_TIME = "Select Time";
    private static final String TAG = TimePickerFragment.class.getName();

    public static void open(FragmentManager fm)
    {
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.show(fm, SELECT_TIME);
    }

    private TextUserInputView.TextInputCallbacks mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        mListener = (TextUserInputView.TextInputCallbacks) getActivity();
        final Calendar c = Calendar.getInstance();
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog dialog = new TimePickerDialog(getActivity(), this, hourOfDay, minute, false);
        return dialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        String dateStr = FORMATTER.format(c.getTime());
        if (mListener != null)
        {
            mListener.sendMessage(dateStr);
        } else
        {
            Log.e(TAG, "listener null");
        }
    }
}

