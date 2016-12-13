package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by rana_ on 12/13/2016.
 */

public abstract class SingleFagmentActivity extends FragmentActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();

        //KEY POINT: you are calling findFRAGMENTById *not* findVIEWById
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null){

            //This is where the fragment is created  and remember that the view is inflated in the
            //Java fragment
            fragment = createFragment();

            fm.beginTransaction()
                    .add(R.id.fragment_container,fragment)
                    .commit();
        }

    }
}
