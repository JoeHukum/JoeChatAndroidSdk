package com.joehukum.chat.ui.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by pulkitkumar on 29/03/16.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    public static final String DATE_PATTERN = "dd MMM yyyy";
    public static final SimpleDateFormat FORMATTER = new SimpleDateFormat(DATE_PATTERN);
    private static final String SELECT_DATE = "Select Date";
    private static final String TAG = DatePickerFragment.class.getName();

    public static void open(FragmentManager fm)
    {
        DatePickerFragment fragment = new DatePickerFragment();
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
        return dialog;
    }


    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth)
    {
        Date date = new Date(year-1900, monthOfYear, dayOfMonth);
        String dateStr = FORMATTER.format(date);

    }
}
