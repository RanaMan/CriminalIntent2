package com.bignerdranch.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import database.CrimeBaseHelper;
import database.CrimeCursorWrapper;
import database.CrimeDbSchema;
import static database.CrimeDbSchema.*;
/**
 * Created by rana_ on 12/13/2016.
 */

public class CrimeLab {

    //The declaration of the singleton Iinstance
    private static CrimeLab sCrimeLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    //This is the public implementation of the get getCrimeLab
    public static CrimeLab getCrimeLab(Context context){
        if(sCrimeLab== null){
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    public Crime getCrime(UUID id){
        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " =  ? ",new String[] {id.toString()});

        try{
            if(cursor.getCount() ==0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        }finally {
            cursor.close();
        }

    }
    //This is the PRIVATE Constructor for CrimeLab
    private CrimeLab(Context context){
        mContext = context;
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();

    }

    public List<Crime> getCrimes(){

        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null,null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return crimes;
    }



    public void addCrime(Crime crime) {
        ContentValues values = getContentValues(crime);
        mDatabase.insert(CrimeTable.NAME, null,values);
    }

    public void updateCrime(Crime crime){
        String uuid = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME, values,
                CrimeTable.Cols.UUID + " = ?",
                new String[] {uuid});
    }

    public File getPhotoFile(Crime crime){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, crime.getPhotoFilename());
    }


    private static ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.DATE, crime.getDate().toString());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1: 0);
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());
        return  values;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null, //Columns - null selects all columns
                whereClause,
                whereArgs,
                null,//gruopBy
                null, // having
                null //order by
        );
        return new CrimeCursorWrapper(cursor);
    }

}
