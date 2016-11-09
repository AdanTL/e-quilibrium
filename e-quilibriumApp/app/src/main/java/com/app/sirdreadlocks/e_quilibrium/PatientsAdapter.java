package com.app.sirdreadlocks.e_quilibrium;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by Adán on 09/11/2016.
 */

public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.CardViewHolder> {

    private HashMap<String,Patient> patients;
    private String[] mKeys;
    public PatientsAdapter(HashMap<String,Patient> patients){
        this.patients = patients;
        mKeys = this.patients.keySet().toArray(new String[patients.size()]);
    }


    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_list_patient, viewGroup, false);

        CardViewHolder cvh = new CardViewHolder(itemView);

        return cvh;
    }

    public void onBindViewHolder(CardViewHolder viewHolder,int pos){
        viewHolder.setName(patients.get(mKeys[pos]).getName());
        viewHolder.setText(patients.get(mKeys[pos]).getEmail());
    }

    public int getItemCount(){
        return patients.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public CardViewHolder(View v){
            super(v);
            mView = v;
        }

        public void setName(String name) {
            TextView field = (TextView) mView.findViewById(R.id.txtName);
            field.setText(name);
        }

        public void setText(String text) {
            TextView field = (TextView) mView.findViewById(R.id.txtEmail);
            field.setText(text);
        }
    }
}
