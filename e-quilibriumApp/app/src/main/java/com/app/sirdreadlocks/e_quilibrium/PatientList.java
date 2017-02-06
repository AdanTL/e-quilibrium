package com.app.sirdreadlocks.e_quilibrium;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.ViewGroup;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ui.email.SignInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PatientList extends Fragment implements SearchView.OnQueryTextListener{
    //private List patients;
    private HashMap<String,Patient> patients;
    private RecyclerView listPatient;
    private DatabaseReference database;
    private FirebaseAuth auth;
    private PatientsAdapter adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_patient_list,container,false);

        listPatient = (RecyclerView) v.findViewById(R.id.listPatient);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {startActivity(new Intent(getActivity(), NewPatient.class));
            }
        });

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference(auth.getCurrentUser().getUid()+"/patients");

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String,Patient>> t = new GenericTypeIndicator<HashMap<String, Patient>>(){};
                patients = dataSnapshot.getValue(t);
                if(patients != null) {
                    adapter = new PatientsAdapter(patients, database);
                    listPatient.setAdapter(adapter);
                    listPatient.setHasFixedSize(true);
                    listPatient.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

}
