package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;


/**
 * Created by rana_ on 12/12/2016.
 */

public class CrimeFragment extends Fragment {

    //This is our crime
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mCrimeSolvedCheckbox;

    private static final String TAG = "**CRIME FRAGMENT**";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Got to call super
        super.onCreate(savedInstanceState);
        //Make a new crime
        mCrime = new Crime();
    }


    /*
        NOTE: The creation of the view is done within the onCreateVIEW and *not* onCreate....
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        //We need to inflate our own view, since we are part of a fragment
        View v = inflater.inflate(R.layout.fragment_crime,container, false);

        //Wire Everything up
        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mDateButton = (Button)v.findViewById(R.id.crime_date);


        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //This is left blank
            }

            //Interestingly, we invoke this on every keystroke... no concept of "off focus"?
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Left blank as well
            }
        });

        //Crazy button shit here
        mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setEnabled(false);

        //Now time for Checkbox love
        mCrimeSolvedCheckbox = (CheckBox)v.findViewById(R.id.crime_solved);
        mCrimeSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        return  v;
    }


}
