package com.app.sirdreadlocks.e_quilibrium;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PatientDetails extends AppCompatActivity {

    private HashMap<String,Test> tests;
    private RecyclerView listTests;
    private DatabaseReference database;
    private FirebaseAuth auth;
    private TestsAdapter adapter;
    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = new Intent(this,PosturalResults.class);
        intent.putExtra("PATIENT",patient);

        patient = (Patient) getIntent().getSerializableExtra("PATIENT");
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference(auth.getCurrentUser().getUid()+"/tests/"+patient.getId());

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String,Test>> t = new GenericTypeIndicator<HashMap<String, Test>>(){};
                tests = dataSnapshot.getValue(t);
                if(tests != null) {
                    adapter = new TestsAdapter(tests);
                    listTests.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listTests = (RecyclerView) findViewById(R.id.listTests);
        listTests.setHasFixedSize(true);
        listTests.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                NewTestAlert alert = new NewTestAlert(patient);
                String tag = "Alert";
                alert.show(fragmentManager,tag);
            }
        });
    }
}
