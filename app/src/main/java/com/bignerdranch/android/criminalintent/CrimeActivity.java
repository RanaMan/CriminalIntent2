package com.bignerdranch.android.criminalintent;

import android.support.v4.app.Fragment;

public class CrimeActivity extends SingleFagmentActivity {

    /*
      Ok, we have created an abstract class which CrimeActivity extends... the onCreate method is
      implemented in the singleFragmentActivity, but we need to ensure that we add our specific fragment...

      (Remember, the *CrimeActivity* leverages *CrimeFragment*!)

     */
    @Override
    protected Fragment createFragment(){
        return new CrimeFragment();
    }
}
