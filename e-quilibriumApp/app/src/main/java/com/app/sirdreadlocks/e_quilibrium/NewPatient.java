package com.app.sirdreadlocks.e_quilibrium;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewPatient extends AppCompatActivity {

    private EditText txtName, txtSurname, txtEmail, txtPhone, txtID;
    private Button btnSubmit;
    private DatabaseReference database;
    private FirebaseAuth auth;
    private Patient newPat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_patient);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("patients");

        txtName = (EditText) findViewById(R.id.txtName);
        txtSurname = (EditText) findViewById(R.id.txtSurname);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtID = (EditText) findViewById(R.id.txtID);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPat = new Patient(
                        txtName.getText().toString(),
                        txtSurname.getText().toString(),
                        txtEmail.getText().toString(),
                        txtPhone.getText().toString());

                database.child(auth.getCurrentUser().getUid()).child(txtID.getText().toString()).setValue(newPat);

                startActivity(new Intent(NewPatient.this,Measures.class));

            }


        });


    }

}



