package com.bignerdranch.android.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by rana_ on 12/13/2016.
 */

public class CrimeListActivity extends SingleFagmentActivity {

    @Override
    public Fragment createFragment(){
        return new CrimeListFragment();
    }
}
