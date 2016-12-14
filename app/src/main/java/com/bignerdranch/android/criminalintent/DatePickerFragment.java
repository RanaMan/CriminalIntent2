package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by rana_ on 12/13/2016.
 */

public class DatePickerFragment extends DialogFragment {

    public static final String DIALOG_DATE_CODE = "com.bignerdranch.android.date";
    public static final String ARG_DATE = "date";

    private DatePicker mDatePicker;


    //We want to create a newInstance class to ensure that it is created properly
    public static DatePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){

        Date incomingDate = (Date)getArguments().getSerializable(ARG_DATE);

        //The date is really a timestamp, we need to get the integer values from it.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(incomingDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        //Inflate the view
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date,null);

        //Init the datePicker
        final DatePicker datePicker = (DatePicker) v.findViewById(R.id.dialog_date_picker);
        datePicker.init(year, month, day, null);

        //Jam that shit in there...
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.date_picker_title)
                .setView(v) //<- IMPORTANT!
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth();
                        int day = datePicker.getDayOfMonth();
                        Date date = new GregorianCalendar(year, month, day).getTime();
                        sendResult(Activity.RESULT_OK, date);
                    }
                })
                .create();
    }

    public void sendResult(int resultCode, Date date){
        if(getTargetFragment() == null){
            //we don't know who invoked us... shitty
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(DIALOG_DATE_CODE, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
