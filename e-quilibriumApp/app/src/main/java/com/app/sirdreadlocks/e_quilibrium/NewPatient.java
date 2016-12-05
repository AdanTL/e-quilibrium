package com.app.sirdreadlocks.e_quilibrium;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class NewPatient extends AppCompatActivity {

    private EditText txtName, txtSurname, txtEmail, txtPhone, txtID, etxtBirthDate;
    private Button btnSubmit;
    private DatabaseReference database;
    private FirebaseAuth auth;
    private Patient newPat;
    private DatePickerDialog birthDatePicker;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_patient);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("/"+auth.getCurrentUser().getUid()+"/patients");

        txtName = (EditText) findViewById(R.id.txtCard1);
        txtSurname = (EditText) findViewById(R.id.txtSurname);
        txtEmail = (EditText) findViewById(R.id.txtCard2);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtID = (EditText) findViewById(R.id.txtID);
        etxtBirthDate = (EditText) findViewById(R.id.etxtBirthdate);
        etxtBirthDate.setInputType(InputType.TYPE_NULL);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);


        Calendar newCalendar = Calendar.getInstance();
        birthDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year,monthOfYear,dayOfMonth);
                etxtBirthDate.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        etxtBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthDatePicker.show();
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPat = new Patient(
                        txtID.getText().toString(),
                        txtName.getText().toString(),
                        txtSurname.getText().toString(),
                        txtEmail.getText().toString(),
                        txtPhone.getText().toString());

                if(!newPat.isNull()) {

                    database.child(txtID.getText().toString()).setValue(newPat);

                    startActivity(new Intent(NewPatient.this, Measures.class));
                }else{
                    Toast.makeText(NewPatient.this,"New patient is wrong",Toast.LENGTH_SHORT).show();
                }

            }


        });


    }

}



