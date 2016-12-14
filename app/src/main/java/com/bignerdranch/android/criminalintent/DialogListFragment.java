package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import java.util.Date;
import java.util.GregorianCalendar;

import static android.content.ContentValues.TAG;

/**
 * Created by rana_ on 12/14/2016.
 */

public class DialogListFragment extends DialogFragment {

    private RadioGroup mRadioGroup;


    //We want to create a newInstance class to ensure that it is created properly
    public static DialogListFragment newInstance(Date date){
        Bundle args = new Bundle();
        //args.putSerializable(ARG_DATE, date);
        DialogListFragment fragment = new DialogListFragment();
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedIntanceState){


        View v = LayoutInflater.from(getActivity()).inflate(R.layout.temp_dialog_list,null);
        mRadioGroup = (RadioGroup)v.findViewById(R.id.temp_dialog_id);

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.radio_title)
                .setView(v) //<- IMPORTANT!
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: Clicked OK");
                    }
                })
                .create();

    }
}
