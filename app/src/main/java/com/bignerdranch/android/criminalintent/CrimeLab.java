package com.bignerdranch.android.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by rana_ on 12/13/2016.
 */

public class CrimeLab {

    //The declaration of the singleton Iinstance
    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;

    //This is the public implementation of the get getCrimeLab
    public static CrimeLab getCrimeLab(Context context){
        if(sCrimeLab== null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    //This is the PRIVATE Constructor for CrimeLab
    private CrimeLab(Context context){
        mCrimes = new ArrayList<>();

        //Make the default
        for(int i=0;i<100;i++){
            Crime defaultCrime = new Crime();
            defaultCrime.setTitle("Crime # [" +i+"]");
            defaultCrime.setSolved(i%2==0);
            defaultCrime.setRequiresPolice(i%2==0);
            mCrimes.add(defaultCrime);
        }

    }

    public List<Crime> getCrimes(){
        return mCrimes;
    }

    public Crime getCrime(UUID id){
        for(Crime crime:mCrimes){
            if(crime.getId().equals(id)){
                return crime;
            }
        }
        return null;
    }
}
