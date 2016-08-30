package com.joehukum.chat.ui.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.joehukum.chat.messages.objects.DateMetaData;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by pulkitkumar on 29/03/16.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    private static final String SELECT_DATE = "Select Date";
    private static final String TAG = DatePickerFragment.class.getName();

    private DateMetaData mMetaData;
    private DateSelectedCallback mListener;

    public interface DateSelectedCallback
    {
        void onDateSelected(Date date);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        mListener = (DateSelectedCallback) activity;
    }

    public static void open(FragmentManager fm, DateMetaData metaData)
    {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setMetaData(metaData);
        fragment.show(fm, SELECT_DATE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        setMetaDataAttributes(dialog);
        return dialog;
    }

    private void setMetaDataAttributes(DatePickerDialog dialog)
    {
        if (mMetaData != null)
        {
            if (mMetaData.getStart() != null)
            {
                dialog.getDatePicker().setMinDate(mMetaData.getStart().getTime());
            }
            if (mMetaData.getEnd() != null)
            {
                dialog.getDatePicker().setMaxDate(mMetaData.getEnd().getTime());
            }
        }
    }

    private void setMetaData(DateMetaData metaData)
    {
        mMetaData = metaData;
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Date date = calendar.getTime();
        if (mListener != null)
        {
            mListener.onDateSelected(date);
        } else
        {
            Log.i(TAG, "onDateSet: activity null");
        }
    }

    @Override
    public void onDetach()
    {
        mListener = null;
        super.onDetach();
    }
}
