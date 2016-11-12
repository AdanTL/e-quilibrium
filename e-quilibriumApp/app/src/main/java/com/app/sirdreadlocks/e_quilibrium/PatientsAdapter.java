package com.app.sirdreadlocks.e_quilibrium;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by Adán on 09/11/2016.
 */

public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.CardViewHolder>{

    private HashMap<String,Patient> patients;
    private String[] mKeys;
    public PatientsAdapter(HashMap<String,Patient> patients){
        this.patients = patients;
        mKeys = this.patients.keySet().toArray(new String[patients.size()]);
    }

    public CardViewHolder onCreateViewHolder(final ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_list_patient, viewGroup, false);

        CardViewHolder cvh = new CardViewHolder(itemView);

        return cvh;
    }

    public void onBindViewHolder(CardViewHolder viewHolder,int pos){
        viewHolder.holderBinder(patients.get(mKeys[pos]));
    }

    public int getItemCount(){
        return patients.size();
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
}
