package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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

import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.bignerdranch.android.criminalintent.R.id.crime_view_pager;


/**
 * Created by rana_ on 12/12/2016.
 */

public class CrimeFragment extends Fragment {

    public static final String ARG_CRIME_ID = "com.bignerdranch.android.CRIME_FRAGMENT.CRIME_ID";
    public static final String CRIME_FRAGMENT_CRIME_UPDATED = "com.bignerdranch.android.CRIME_FRAGMENT.CRIME_UPDATED";

    //This is our crime
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mCrimeSolvedCheckbox;

    private static final String TAG = "**CRIME FRAGMENT**";

    public static CrimeFragment newInstance(UUID mCrimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, mCrimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static UUID crimeWhichWasUpdated(Intent data){
        return (UUID)data.getSerializableExtra(CRIME_FRAGMENT_CRIME_UPDATED);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Got to call super
        super.onCreate(savedInstanceState);
        //Make a new crime

        UUID crimeID = (UUID)getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.getCrimeLab(getActivity()).getCrime(crimeID);
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
        mCrimeSolvedCheckbox = (CheckBox)v.findViewById(R.id.crime_solved);

        thisItemWasLoaded(mCrime.getId());

        mTitleField.setText(mCrime.getTitle());
        mCrimeSolvedCheckbox.setChecked(mCrime.isSolved());

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
        mDateButton.setEnabled(true);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //This was used to get to the first/last item on the PageViewer
//                ViewPager mViewPager = (ViewPager)getActivity().findViewById(R.id.crime_view_pager);
//                mViewPager.setCurrentItem(0);
            }
        });

        //Now time for Checkbox love

        mCrimeSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        return  v;
    }

    private void thisItemWasLoaded(UUID crimeWhichWasUpdated){
        Intent data = new Intent();
        data.putExtra(CRIME_FRAGMENT_CRIME_UPDATED, crimeWhichWasUpdated);
        getActivity().setResult(RESULT_OK, data);
    }


}
