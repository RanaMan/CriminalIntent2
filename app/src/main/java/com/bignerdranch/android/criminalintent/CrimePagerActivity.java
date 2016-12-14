package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by rana_ on 12/13/2016.
 */

public class CrimePagerActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    public static final String EXTRA_CRIME_ID = "com.bignerdranch.com.EXTRA_CRIME_ID";
    public static final int CRIME_ACTIVITY_CODE = 666;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        mCrimes = CrimeLab.getCrimeLab(getApplication()).getCrimes();
        mViewPager = (ViewPager)findViewById(R.id.crime_view_pager);

        final UUID incomingCrimeID = (UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        //I think this is how we swipe... it is re-created...
        for(int i=0; i<mCrimes.size();i++){
            if(mCrimes.get(i).getId().equals(incomingCrimeID)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }

    /*
    This is the constructor on how the CrimePagerActivity should be called.
    */
    public static Intent newIntent(Context packageContext, UUID crimeID){
        Intent i = new Intent(packageContext, CrimePagerActivity.class);
        i.putExtra(EXTRA_CRIME_ID, crimeID);
        return i;
    }
}
