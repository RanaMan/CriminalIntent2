package com.bignerdranch.android.criminalintent;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;


import java.util.Date;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;



/**
 * Created by rana_ on 12/12/2016.
 */

public class CrimeFragment extends Fragment {

    public static final String ARG_CRIME_ID = "com.bignerdranch.android.CRIME_FRAGMENT.CRIME_ID";
    public static final String CRIME_FRAGMENT_CRIME_UPDATED = "com.bignerdranch.android.CRIME_FRAGMENT.CRIME_UPDATED";

    public static final String DIALOG_DATE = "DialogDate";
    public static final int REQUEST_DATE_CODE = 0;

    public static final String DIALOG_LIST = "DialogList";
    public static final int REQUEST_LIST_CODE = 1;

    public static final int REQUEST_CONTACT = 1;


    //This is our crime
    private Crime       mCrime;
    private EditText    mTitleField;
    private Button      mDateButton;
    private CheckBox    mCrimeSolvedCheckbox;
    private Button      mCrimeSuspectButton;
    private Button      mCrimeReportButton;

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
        mCrimeReportButton = (Button)v.findViewById(R.id.crime_report);
        mCrimeSuspectButton= (Button)v.findViewById(R.id.crime_suspect);

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

        //mDateButton Logic here
        mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setEnabled(true);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //This was used to get to the first/last item on the PageViewer **Example code**
//                ViewPager mViewPager = (ViewPager)getActivity().findViewById(R.id.crime_view_pager);
//                mViewPager.setCurrentItem(0);
                FragmentManager manager = getFragmentManager();

                //Just and example on how to add a Dialog of Radio buttons
//                DialogListFragment list = DialogListFragment.newInstance(new Date());
//                list.setTargetFragment(CrimeFragment.this, REQUEST_LIST_CODE);
//                list.show(manager, DIALOG_LIST);

                DatePickerFragment dialog = DatePickerFragment.newInstance(new Date());
                //The TargetFragment seems wierd,,, it is the "invoking Fragment"
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE_CODE);

                //Note that DIALOG Date is CrimeFragment's static argument, most likely needed for the onResume Method
                dialog.show(manager,DIALOG_DATE);
            }
        });

        //Now time for Checkbox love
        mCrimeSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });


        mCrimeReportButton = (Button)v.findViewById(R.id.crime_report);
        mCrimeReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create new intent and tell the action as SEND
                Intent i = new Intent(Intent.ACTION_SEND);

                //Setting the type as a MIME Text of plain.
                i.setType("text/plain");

                //Put in the extras which we expect anyone who is listening to leverage
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        mCrimeSuspectButton = (Button)v.findViewById(R.id.crime_suspect);

        //Create a new Pick Contact Intnet... knowing with the Type will be
        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);

        //Invoke the intent for result... easy peasy
        mCrimeSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        //This is the loadng of the suspect...
        if(mCrime.getSuspect() != null){
            mCrimeSuspectButton.setText(mCrime.getSuspect());
        }

        return  v;
    }

    private void thisItemWasLoaded(UUID crimeWhichWasUpdated){
        Intent data = new Intent();
        data.putExtra(CrimeActivity.EXTRA_CRIME_ID, crimeWhichWasUpdated);
        getActivity().setResult(RESULT_OK, data);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != RESULT_OK || data == null ){
            //the result wasn't OK... shitty
            Log.d(TAG, "**********onActivityResult: SHIT!!! NO DATA IN OUR RESPONSE");
            return;
        }

        //This is the code which we put in
        if(requestCode == REQUEST_DATE_CODE){
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.DIALOG_DATE_CODE);
            mCrime.setDate(date);
            mDateButton.setText(mCrime.getDate().toString());
        }

        if(requestCode == REQUEST_CONTACT && data != null){

            Uri contactUri = data.getData();
            // Specify which fields you want your query to return
            // values for.
            String[] queryFields = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME,
            };
            // Perform your query - the contactUri is like a "where"
            // clause here
            ContentResolver resolver = getActivity().getContentResolver();
            Cursor c = resolver
                    .query(contactUri, queryFields, null, null, null);

            try {
                // Double-check that you actually got results
                if (c.getCount() == 0) {
                    return;
                }

                // Pull out the first column of the first row of data -
                // that is your suspect's name.
                c.moveToFirst();

                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
                mCrimeSuspectButton.setText(suspect);
            } finally {
                c.close();
            }
        }

    }

    @Override
    public void onPause(){
        super.onPause();
           CrimeLab.getCrimeLab(getActivity()).updateCrime(mCrime);
    }

    private String getCrimeReport(){
        String solvedString = null;

        if(mCrime.isSolved()){
            solvedString = getString(R.string.crime_report_solved);
        }else{
            solvedString = getString(R.string.crime_report_unsolved);
        }


        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if(suspect == null){
            suspect = getString(R.string.crime_report_no_suspect);
        }else{
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report,
                mCrime.getTitle(), dateString, solvedString, suspect);

        return report;



    }

}
