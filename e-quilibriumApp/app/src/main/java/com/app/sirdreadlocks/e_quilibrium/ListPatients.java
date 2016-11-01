package com.app.sirdreadlocks.e_quilibrium;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListPatients extends AppCompatActivity {
    private ArrayList<Patient> patients;
    private FirebaseListAdapter<Patient> adapter;
    private ListView listPatient;
    private DatabaseReference database;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_patients);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("patients/"+auth.getCurrentUser().getUid());
        patients = new ArrayList<>();
        adapter = new FirebaseListAdapter<Patient>(this, Patient.class, android.R.layout.simple_list_item_1, database) {
            @Override
            protected void populateView(View v, Patient model, int position) {
                TextView text = (TextView)v.findViewById(android.R.id.text1);
                text.setText(model.getId());

            }
        };

        listPatient = (ListView) findViewById(R.id.listPatient);
        listPatient.setAdapter(adapter);


    }
}
