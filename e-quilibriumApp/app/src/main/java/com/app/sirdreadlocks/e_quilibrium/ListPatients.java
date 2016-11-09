package com.app.sirdreadlocks.e_quilibrium;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListPatients extends AppCompatActivity {
    private ArrayList<Patient> patients;
    private RecyclerView listPatient;
    private DatabaseReference database;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_patients);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("patients/"+auth.getCurrentUser().getUid());
        patients = new ArrayList<>();


        listPatient = (RecyclerView) findViewById(R.id.listPatient);
        listPatient.setHasFixedSize(true);
        listPatient.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Patient,CardViewHolder> adapter =
                new FirebaseRecyclerAdapter<Patient, CardViewHolder>(
                        Patient.class,
                        R.layout.card_list_patient,
                        CardViewHolder.class,
                        database
                ) {
            @Override
            protected void populateViewHolder(CardViewHolder viewHolder, Patient patient, int position) {
                viewHolder.setText(patient.getId());
                viewHolder.setName(patient.getName());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ListPatients.this, Measures.class));
                    }
                });

            }
        };

        listPatient.setAdapter(adapter);
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
