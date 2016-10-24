package com.app.sirdreadlocks.e_quilibrium;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button btnNewPat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNewPat = (Button)findViewById(R.id.btnNewPat);

        btnNewPat.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                Intent intent =
                        new Intent(MainActivity.this, NewPatient.class);

                startActivity(intent);
            }
        });
    }
}