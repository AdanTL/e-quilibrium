package com.app.sirdreadlocks.e_quilibrium;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Adán on 09/11/2016.
 */

public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.CardViewHolder> implements Filterable{

    private ArrayList<Patient> patients;
    private ArrayList<Patient> patientsFilter;
    private CustomFilter mFilter;
    private DatabaseReference database;

    public PatientsAdapter(HashMap<String,Patient> patients, DatabaseReference database){
        this.patients = new ArrayList<>();
        this.patients.addAll(patients.values());
        this.patientsFilter = new ArrayList<>();
        this.patientsFilter.addAll(patients.values());
        this.mFilter = new CustomFilter(PatientsAdapter.this);
        this.database = database;
    }

    public CardViewHolder onCreateViewHolder(final ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_list, viewGroup, false);

        CardViewHolder cvh = new CardViewHolder(itemView);

        return cvh;
    }

    public void onBindViewHolder(CardViewHolder viewHolder,final int pos){
        viewHolder.holderBinder(patientsFilter.get(pos));
    }

    public int getItemCount(){
        return patientsFilter.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public class CardViewHolder extends RecyclerView.ViewHolder{
        private Patient pat;
        private View mView;

        public CardViewHolder(View v){
            super(v);
            mView = v;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment newFragment = new PatientDetails();
                    MainActivity feeds = (MainActivity) v.getContext();
                    feeds.switchContent(newFragment);
/*                    Intent intent =
                            new Intent(v.getContext(), PatientDetails.class);
                    intent.putExtra("PATIENT",pat);
                    v.getContext().startActivity(intent);*/
                }
            });
        }

        public void holderBinder(final Patient pat) {
            this.pat = pat;
            TextView name = (TextView) mView.findViewById(R.id.txtCard1);
            TextView email = (TextView) mView.findViewById(R.id.txtCard2);
            Button btnDel = (Button) mView.findViewById(R.id.btnDel);
            name.setText(pat.getName());
            email.setText(pat.getEmail());
            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    database.child(pat.getId()).removeValue();
                    int i = patientsFilter.indexOf(pat);
                    patientsFilter.remove(i);
                    notifyItemRemoved(i);
                    //v.getContext().startActivity(new Intent(v.getContext(),PatientList.class));
                }
            });
        }

    }



    public class CustomFilter extends Filter {
        private PatientsAdapter patientsAdapter;

        private CustomFilter(PatientsAdapter patientsAdapter) {
            super();
            this.patientsAdapter = patientsAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            patientsFilter.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                patientsFilter.addAll(patients);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final Patient patient : patients) {
                    if (patient.getName().toLowerCase().contains(filterPattern)||
                            patient.getId().toLowerCase().contains(filterPattern)||
                            patient.getSurname().toLowerCase().contains(filterPattern)||
                            patient.getEmail().toLowerCase().contains(filterPattern)||
                            patient.getPhone().toLowerCase().contains(filterPattern)) {
                        patientsFilter.add(patient);
                    }
                }
            }
            results.values = patientsFilter;
            results.count = patientsFilter.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            this.patientsAdapter.notifyDataSetChanged();
        }
    }
}
