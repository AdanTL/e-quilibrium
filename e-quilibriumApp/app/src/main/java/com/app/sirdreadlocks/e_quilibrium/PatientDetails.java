package com.app.sirdreadlocks.e_quilibrium;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PatientDetails extends Fragment implements SearchView.OnQueryTextListener{

    private HashMap<String,Test> tests;
    private RecyclerView listTests;
    private DatabaseReference database;
    private FirebaseAuth auth;
    private TestsAdapter adapter;
    private Patient patient;
    private DrawerLayout drawerLayout;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_patient_details,container,false);

        //patient = (Patient) getIntent().getSerializableExtra("PATIENT");
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference(auth.getCurrentUser().getUid()+"/tests/44566511");
        listTests = (RecyclerView) v.findViewById(R.id.listTests);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String,Test>> t = new GenericTypeIndicator<HashMap<String, Test>>(){};
                tests = dataSnapshot.getValue(t);
                if(tests != null) {
                    adapter = new TestsAdapter(tests);
                    listTests.setAdapter(adapter);
                    listTests.setHasFixedSize(true);
                    listTests.setLayoutManager(new LinearLayoutManager(getContext()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                NewTestAlert alert = new NewTestAlert(patient);
                String tag = "Alert";
                alert.show(fragmentManager,tag);
            }
        });
        return v;
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
}
