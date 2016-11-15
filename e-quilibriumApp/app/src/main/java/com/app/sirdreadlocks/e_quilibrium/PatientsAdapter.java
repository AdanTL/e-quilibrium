package com.app.sirdreadlocks.e_quilibrium;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Adán on 09/11/2016.
 */

public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.CardViewHolder> implements Filterable{

    private ArrayList<Patient> patients;
    private ArrayList<Patient> patientsFilter;
    private CustomFilter mFilter;

    public PatientsAdapter(HashMap<String,Patient> patients){
        this.patients = new ArrayList<>();
        this.patients.addAll(patients.values());
        this.patientsFilter = new ArrayList<>();
        this.patientsFilter.addAll(patients.values());
        this.mFilter = new CustomFilter(PatientsAdapter.this);
    }

    public CardViewHolder onCreateViewHolder(final ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_list_patient, viewGroup, false);

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

    public static class CardViewHolder extends RecyclerView.ViewHolder{
        private Patient pat;
        private View mView;

        public CardViewHolder(View v){
            super(v);
            mView = v;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"amor "+pat.getName(),Toast.LENGTH_LONG).show();
                    v.getContext().startActivity(new Intent(v.getContext(),Measures.class));
                }
            });
        }

        public void holderBinder(Patient pat) {
            this.pat = pat;
            TextView name = (TextView) mView.findViewById(R.id.txtName);
            TextView email = (TextView) mView.findViewById(R.id.txtEmail);
            name.setText(pat.getName());
            email.setText(pat.getEmail());
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
                    if (patient.getName().toLowerCase().contains(filterPattern)) {
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
