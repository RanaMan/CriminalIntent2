package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFagmentActivity {

    public static final String EXTRA_CRIME_ID = "com.bignerdranch.com.EXTRA_CRIME_ID";
    public static final int CRIME_ACTIVITY_CODE = 666;


    /*
      Ok, we have created an abstract class which CrimeActivity extends... the onCreate method is
      implemented in the singleFragmentActivity, but we need to ensure that we add our specific fragment...

      (Remember, the *CrimeActivity* leverages *CrimeFragment*!)

     */
    @Override
    protected Fragment createFragment(){
        UUID incomingCrimeID = (UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return CrimeFragment.newInstance(incomingCrimeID);
    }

    /*
    This is the constructor on how the CrimeActivity should be called.
     */
    public static Intent newIntent(Context packageContext, UUID crimeID){
        Intent i = new Intent(packageContext, CrimeActivity.class);
        i.putExtra(EXTRA_CRIME_ID, crimeID);
        return i;
    }
}
