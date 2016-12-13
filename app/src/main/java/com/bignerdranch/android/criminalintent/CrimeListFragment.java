package com.bignerdranch.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by rana_ on 12/13/2016.
 */

public class CrimeListFragment extends Fragment {

    private static final String TAG ="CrimeListFragment:";

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //We need to inflate our own view, since we are part of a fragment
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);

        //Get the Recycler view
        mCrimeRecyclerView = (RecyclerView)v.findViewById(R.id.crime_recycler_view);

        /*
        This is a "just is" rule... need the manager or the RV will freak out. The reason why this
        needs to be added is that you layout manager can scroll any way you want...
         */

        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Moved out just to be clean
        updateUI();

        return v;
    }

    /*
    This is the wiring of everything all together:
     1. get the CrimeLab
     2. get the crimes and put them in a list.
     3. Create an andapter with that list
     4. add the adapter to the RV.

     */
    private void updateUI(){
        //Context is strange here.. we got the activity
        CrimeLab crimeLab = CrimeLab.getCrimeLab(getActivity());
        List<Crime> mCrimes = crimeLab.getCrimes();
        mAdapter = new CrimeAdapter(mCrimes);
        mCrimeRecyclerView.setAdapter(mAdapter);

    }

    /*
    We have our holder defined in our listFragement, since this is the only place that it really
    makes sense to use it... We extend viewholder since we want to leverage a generic inflater, like
    we did with the fragment creation.
     */
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Crime mCrime;
        private TextView mTitleTextView;
        private TextView mDateTextView;

        //ItemView comes from the parent RecyclerView.ViewHolder implementation...
        //WTF!
        public CrimeHolder(LayoutInflater inflater, ViewGroup parent){
            /*
            You want to inflate your own crime holder... don't have the adapter do it... (that is
             why it is here...)
             */

            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            mTitleTextView = (TextView)itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView)itemView.findViewById(R.id.crime_date);
            itemView.setOnClickListener(this);
        }


        public void bind(Crime crime){
            mCrime = crime;
            mDateTextView.setText(mCrime.getDate().toString());
            mTitleTextView.setText(mCrime.getTitle());
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), mCrime.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{

        private List<Crime> mCrimes;

        private CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            /*You are going to pass in the layoutInflater which will bind the view(List_item_Crime)
            to the crime holder object... it is implicit, but important... It is where itemView comes
            from.
             And we don't want to inflate it now, we might want to have a different holder in our list.
             */
            return new CrimeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: position ["+position+"]");
            holder.bind(mCrimes.get(position));

        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
