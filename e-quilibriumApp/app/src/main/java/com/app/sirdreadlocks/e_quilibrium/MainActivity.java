package com.app.sirdreadlocks.e_quilibrium;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Button btnNewPat,btnSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNewPat = (Button) findViewById(R.id.btnNewPat);
        btnSignOut = (Button) findViewById(R.id.btnListPat);


        btnNewPat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent =
                        new Intent(MainActivity.this, NewPatient.class);

                startActivity(intent);
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {
                                // user is now signed out
                                startActivity(new Intent(MainActivity.this, Authentication.class));
                                finish();
                            }
                        });
            }
        });

    }

    public void onClick(View v) {
        if (v.getId() == R.id.btnListPat) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // user is now signed out
                            startActivity(new Intent(MainActivity.this, Authentication.class));
                            finish();
                        }
                    });
        }
    }
}