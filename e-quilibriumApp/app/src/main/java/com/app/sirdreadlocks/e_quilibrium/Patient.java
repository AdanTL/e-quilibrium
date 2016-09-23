package com.app.sirdreadlocks.e_quilibrium;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Register screen to save patient info before test.
 */
public class Patient extends AppCompatActivity {

    /**
     * UI variables
     */
    private EditText txtID;
    private EditText txtSurname;
    private EditText txtName;
    private Button btnSubmit;

    /**
     * DB
     */
    private SQLiteDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        txtID = (EditText)findViewById(R.id.inputID);
        txtName = (EditText)findViewById(R.id.inputName);
        txtSurname = (EditText)findViewById(R.id.inputSurname);

        btnSubmit = (Button)findViewById(R.id.submit);

        /**
         * Open DB writer mode
         */
        PatientSQLiteHelper pdbh = new PatientSQLiteHelper(this,"DBPatients", null, 1);

        db = pdbh.getWritableDatabase();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mID = txtID.getText().toString();
                String mSurname = txtSurname.getText().toString();
                String mName = txtName.getText().toString();

                ContentValues newRegister = new ContentValues();
                newRegister.put("ID", mID);
                newRegister.put("Surname", mSurname);
                newRegister.put("Name", mName);
                db.insert("Patient", null, newRegister);

                Intent intent =
                        new Intent(Patient.this, Measures.class);

                startActivity(intent);
            }
        });

    }
}

