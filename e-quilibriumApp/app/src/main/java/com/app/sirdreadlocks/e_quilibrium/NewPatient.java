package com.app.sirdreadlocks.e_quilibrium;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewPatient extends AppCompatActivity {

    private EditText txtName, txtSurname, txtEmail, txtPhone, txtID, etxtBirthDate;
    private Button btnSubmit;
    private DatabaseReference database;
    private FirebaseAuth auth;
    private Patient newPat;
    private DatePickerDialog birthDatePicker;
    private SimpleDateFormat dateFormatter;
    private Calendar dateTime;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_patient);

        //Setup Navigation View
        Toolbar toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Patients list");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("/"+auth.getCurrentUser().getUid()+"/patients");

        txtName = (EditText) findViewById(R.id.txtCard1);
        txtSurname = (EditText) findViewById(R.id.txtSurname);
        txtEmail = (EditText) findViewById(R.id.txtCard2);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtID = (EditText) findViewById(R.id.txtID);
        etxtBirthDate = (EditText) findViewById(R.id.etxtBirthdate);
        etxtBirthDate.setInputType(InputType.TYPE_NULL);

        dateTime = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        birthDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateTime.set(year,monthOfYear,dayOfMonth);
                etxtBirthDate.setText(dateFormatter.format(dateTime.getTime()));
            }
        },dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH));

        etxtBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthDatePicker.show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPat = new Patient(
                        txtID.getText().toString(),
                        txtName.getText().toString(),
                        txtSurname.getText().toString(),
                        txtEmail.getText().toString(),
                        txtPhone.getText().toString(),
                        etxtBirthDate.getText().toString());

                if(!newPat.isNull()) {

                    database.child(txtID.getText().toString()).setValue(newPat);

                    Intent intent = new Intent(NewPatient.this, PatientDetails.class);

                    intent.putExtra("PATIENT",newPat);

                    startActivity(intent);

                }else{
                    Toast.makeText(NewPatient.this,"New patient is wrong",Toast.LENGTH_SHORT).show();
                }
            }


        });
    }
}



